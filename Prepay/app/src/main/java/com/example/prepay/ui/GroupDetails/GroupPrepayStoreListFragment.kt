package com.example.prepay.ui.GroupDetails

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.RetrofitUtil
import com.example.prepay.SharedPreferencesUtil
import com.example.prepay.data.model.dto.Restaurant
import com.example.prepay.data.model.dto.RestaurantData
import com.example.prepay.data.response.BootPayChargeReq
import com.example.prepay.data.response.TeamIdStoreRes
import com.example.prepay.data.response.TeamStoreReq
import com.example.prepay.databinding.DialogBootpaySearchRequestBinding
import com.example.prepay.databinding.FragmentGroupPaymentHistoryBinding
import com.example.prepay.databinding.FragmentGroupPrepayStoreListBinding
import com.example.prepay.ui.CreateGroup.CreateGroupViewModel
import com.example.prepay.ui.MainActivity
import com.example.prepay.ui.MainActivityViewModel
import com.example.prepay.ui.RestaurantDetails.RestaurantDetailsViewModel
import com.example.prepay.util.BootPayManager
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Timer
import java.util.TimerTask

private const val TAG = "GroupPrepayStoreListFra_싸피"
class GroupPrepayStoreListFragment: BaseFragment<FragmentGroupPrepayStoreListBinding>(
    FragmentGroupPrepayStoreListBinding::bind,
    R.layout.fragment_group_prepay_store_list
),RestaurantAdapter.OnRestaurantClickListener{
    private lateinit var mainActivity: MainActivity
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var restaurantList: List<TeamIdStoreRes>

    //뷰모델
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val restaurantDetailsViewModel : RestaurantDetailsViewModel by viewModels()
    private val viewModel: GroupDetailsFragmentViewModel by viewModels()

    private lateinit var searchAdapter: RestaurantSearchAdapter
    private lateinit var bootPayDialog : DialogBootpaySearchRequestBinding
    private var selectedRestaurantName: String = ""
    private val createGroupViewModel: CreateGroupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initViewModel()
        initEvent()
    }

    fun initEvent(){
        viewModel.getTeamDetail(SharedPreferencesUtil.getAccessToken()!!,activityViewModel.teamId.value!!)
        viewModel.getMyTeamRestaurantList(SharedPreferencesUtil.getAccessToken()!!,activityViewModel.teamId.value!!)
        binding.addRestaurant.setOnClickListener {
            addRestaurantClick()
        }

        binding.diningTogetherQrBtn.setOnClickListener {
            lifecycleScope.launch {
                runCatching {
                    RetrofitUtil.qrService.qrTeamCreate(SharedPreferencesUtil.getAccessToken()!!,activityViewModel.teamId.value!!.toInt())
                }.onSuccess {
                    showQRDialog(it.message+":"+ SharedPreferencesUtil.getAccessToken()!!+":"+activityViewModel.teamId.value.toString())
                }.onFailure {
                    mainActivity.showToast("결제 권한이 없습니다")
                }
            }
        }

        binding.createPrivateQr.setOnClickListener {
            lifecycleScope.launch {
                runCatching {
                    RetrofitUtil.qrService.qrPrivateCreate(SharedPreferencesUtil.getAccessToken()!!,activityViewModel.teamId.value!!.toInt())
                }.onSuccess {
                    showQRDialog(it.message+":"+ SharedPreferencesUtil.getAccessToken()!!+":"+activityViewModel.teamId.value.toString())
                }.onFailure {
                    mainActivity.showToast("가격이 부족합니다")
                }
            }
            //mainActivity.broadcast("hello","hello")
        }
    }

    fun initAdapter(){
        restaurantList = emptyList()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        restaurantAdapter = RestaurantAdapter(restaurantList,this)
        binding.recyclerView.adapter = restaurantAdapter
    }

    private fun initViewModel(){
        viewModel.storeListInfo.observe(viewLifecycleOwner){it->
            restaurantAdapter.teamIdStoreResList = it
            Log.d(TAG,it.toString())
            restaurantAdapter.notifyDataSetChanged()
        }
        viewModel.teamDetail.observe(viewLifecycleOwner){it->
            Log.d(TAG,"감지된 데이터입니다입니다"+it.toString())
            binding.dayLimitTxt.text = CommonUtils.makeComma(it.dailyPriceLimit-it.usedAmount)
            binding.groupPriceTxt.text = CommonUtils.makeComma(it.teamBalance)
        }

        activityViewModel.moneyValue.observe(viewLifecycleOwner){it->
            Log.d(TAG,"가격변동"+it.toString())
            binding.dayLimitTxt.text = CommonUtils.makeComma(it-viewModel.teamDetail.value!!.usedAmount)
        }
    }

    private fun addRestaurantClick() {
        bootPayDialog = DialogBootpaySearchRequestBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = AlertDialog.Builder(requireContext())
            .setView(bootPayDialog.root)
            .create()

        dialog.show()
        viewModel.getStoreId(SharedPreferencesUtil.getAccessToken()!!, activityViewModel.teamId.value!!.toString().toLong())
        initRecyclerView()
        setOnQueryTextListener()
        observeStoresListInfo()

        bootPayDialog.cancel.setOnClickListener {
            dialog.dismiss()
        }
        bootPayDialog.confirm.setOnClickListener {
            val storeId = createGroupViewModel.storeId.value
            val totalPrice = bootPayDialog.totalPrice.text.toString()
            val teamId : Long? = activityViewModel.teamId.value
            Log.d(TAG, "selectedRestaurantName: $selectedRestaurantName")
            Log.d(TAG, "totalPrice: $totalPrice")
            if (selectedRestaurantName.isNotEmpty() && totalPrice.isNotEmpty()) {
                BootPayManager.startPayment(requireActivity(), selectedRestaurantName, totalPrice) { receiptId, price ->

                    Log.d(TAG, "storeId: $storeId")
                    Log.d(TAG, "teamId: $teamId, price: ${price}")
                    Log.d(TAG, "receiptId: $receiptId")

                    registerStore(storeId,teamId)
                    lifecycleScope.launch {
                        try {
                            val chargeReceipt = teamId?.let { teamId -> storeId?.let { storeId ->
                                BootPayChargeReq(
                                    teamId.toInt(),storeId, totalPrice.toInt(), receiptId)
                            } }
                            val response = withContext(Dispatchers.IO) {
                                delay(1000)
                                chargeReceipt?.let { it ->
                                    RetrofitUtil.bootPayService.getBootPay(SharedPreferencesUtil.getAccessToken()!!,
                                        it
                                    )
                                }
                            }

                            if (response?.isSuccessful == true) {
                                mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT)
                            } else {
                                Log.e(TAG, "영수증 올리기 실패: ${response}")
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "네트워크 요청 실패: ${e.localizedMessage}", e)
                        }
                    }
                }
            } else {
                showError("식당 이름과 결제금액을 모두 입력해주세요.")
            }
        }
    }

    private fun ComputePrice(){
        viewModel.getTeamDetail(SharedPreferencesUtil.getAccessToken()!!,activityViewModel.teamId.value!!)
        viewModel.getMyTeamRestaurantList(SharedPreferencesUtil.getAccessToken()!!,activityViewModel.teamId.value!!)
    }


    /**
     * QR 코드를 생성하여 다이얼로그로 표시하는 함수
     *
     * @param context 다이얼로그를 표시할 컨텍스트 (Fragment 내에서는 requireContext()를 사용)
     * @param url QR 코드에 인코딩할 URL (기본값: "https://www.naver.com")
     */
    fun showQRDialog(url: String = "https://www.naver.com") {
        val context = this@GroupPrepayStoreListFragment.requireContext()

        // 타이머 생성
        val timer = Timer()

        // 다이얼로그 레이아웃 인플레이트
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_create_qr, null)

        // QR 코드 생성 및 이미지뷰에 적용
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(url, BarcodeFormat.QR_CODE, 400, 400)
            val imageViewQrCode = dialogView.findViewById<ImageView>(R.id.imageViewQrCode)
            imageViewQrCode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.e("QRDialog", "QR 코드 생성 실패", e)
        }

        // AlertDialog 생성
        val dialog = androidx.appcompat.app.AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(true)  // 뒤로 가기 버튼 허용
            .create()


        // 다이얼로그 닫힐 때 타이머 취소
        dialog.setOnDismissListener {
            timer.cancel()
            ComputePrice()
        //initialView()
        }

        // 다이얼로그 표시
        dialog.show()

        // 60초 카운트다운 타이머 시작
        var seconds = 60
        val qrTimer = dialogView.findViewById<TextView>(R.id.qr_timer)
        qrTimer?.text = "남은 시간: 60초"

        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                seconds--
                // UI 업데이트는 메인 스레드에서 수행
                this@GroupPrepayStoreListFragment.requireActivity().runOnUiThread {
                    qrTimer?.text = "남은 시간: ${seconds}초"
                }
                if (seconds <= 0) {
                    // 시간이 다 되었으면 다이얼로그를 닫고 타이머 취소
                    this@GroupPrepayStoreListFragment.requireActivity().runOnUiThread {
                        if (dialog.isShowing) {
                            dialog.dismiss()
                        }
                    }
                    timer.cancel()
                    ComputePrice()
                //initialView()
                }
            }
        }, 1000, 1000)
    }

    override fun onRestaurantClick(storeName : String, teamIdStoreResId: Int) {
        activityViewModel.setStoreId(teamIdStoreResId)
        activityViewModel.setStoreName(storeName)
        val restaurantData = RestaurantData(storeName, teamIdStoreResId)
        restaurantDetailsViewModel.setRestaurantData(restaurantData)
        mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.RESTAURANT_DETAILS_FRAGMENT)
    }

    private fun registerStore(storeId: Int?, teamId: Long?) {
        lifecycleScope.launch {
            try {
                Log.d("registerStore", "storeId: $storeId")
                Log.d("registerStore", "teamId: $teamId")

                if (teamId == null || storeId == null) {
                    return@launch
                }
                val request = TeamStoreReq(storeId, teamId.toInt())
                val response = withContext(Dispatchers.IO) {
                    RetrofitUtil.teamService.createStore(SharedPreferencesUtil.getAccessToken()!!, request)
                }

                Log.d(TAG, "response: $response")

                if (response.isSuccessful) {
                } else {
                    Log.e(TAG, "스토어 연결 실패: ${response?.errorBody()?.string()}")
                }

            } catch (e: Exception) {
                Log.e(TAG, "네트워크 요청 실패: ${e.localizedMessage}", e)
            } finally {

            }
        }
    }


    private fun setOnQueryTextListener() {
        bootPayDialog.addRestaurantName.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    bootPayDialog.searchResults.visibility = View.GONE
                } else {
                    filterSearchResults(newText)
                    bootPayDialog.searchResults.visibility = View.VISIBLE
                }
                return true
            }
        })
    }

    private fun showError(message: String) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton("확인", null)
            .show()
    }

    private fun initRecyclerView() {
        searchAdapter = RestaurantSearchAdapter ({ selectedRestaurant ->
            bootPayDialog.addRestaurantName.setQuery(selectedRestaurant.name, false)
            selectedRestaurantName = selectedRestaurant.name
            bootPayDialog.searchResults.visibility = View.GONE
        }, createGroupViewModel)

        bootPayDialog.searchResults.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
        }
    }

    private fun filterSearchResults(query: String) {
        val storeList = viewModel.storesListInfo.value
        if (storeList == null) {
            Log.e(TAG, "storesListInfo is null, cannot filter results.")
            return
        }

        val filteredList = getSearchResults(query)
        Log.d(TAG, "filterSearchResults: $filteredList")
        searchAdapter.submitList(filteredList)
    }

    private fun getSearchResults(query: String): List<Restaurant> {
        // 여기에 식당 검색 로직 추가
        val storeList = viewModel.storesListInfo.value
        Log.d(TAG, "getSearchResults: ${viewModel.storesListInfo.value}")
        return storeList?.map { store ->
            Restaurant(store.storeId, store.storeName)
        }?.filter { it.name.contains(query, ignoreCase = true) } ?: emptyList()

    }
    private fun observeStoresListInfo() {
        viewModel.storesListInfo.observe(viewLifecycleOwner) { stores ->
            val queryText = bootPayDialog.addRestaurantName.query.toString()

            if (stores.isNotEmpty() && queryText.isNotEmpty()) {
                val restaurantList = stores.map { Restaurant(it.storeId, it.storeName) }
                searchAdapter.submitList(restaurantList)
                bootPayDialog.searchResults.visibility = View.VISIBLE
            } else {
                searchAdapter.submitList(emptyList())
                bootPayDialog.searchResults.visibility = View.GONE
            }
        }
    }
}