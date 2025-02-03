package com.example.prepay.ui.CreateGroup

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.prepay.BaseFragment
import com.example.prepay.R
import com.example.prepay.databinding.FragmentBootPayBinding
import com.example.prepay.ui.MainActivity
import kr.co.bootpay.android.Bootpay
import kr.co.bootpay.android.events.BootpayEventListener
import kr.co.bootpay.android.models.BootExtra
import kr.co.bootpay.android.models.BootItem
import kr.co.bootpay.android.models.BootUser
import kr.co.bootpay.android.models.Payload

class BootPayFragment : BaseFragment<FragmentBootPayBinding>(
    FragmentBootPayBinding::bind,
    R.layout.fragment_boot_pay
) {
    private lateinit var mainActivity: MainActivity

    val AppId = "67a0ae6c4fb27baaf86e5571"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val restaurantName = binding.restaurantNameText.text
        val totalPrice = binding.totalPrice.text
        binding.button3.setOnClickListener {
            goBootpayRequest(restaurantName.toString(), totalPrice.toString())
        }
    }

    fun goBootpayRequest(restaurantName: String, totalPrice:String) {
        val user = BootUser().setPhone("010-1234-5678") // 구매자 정보
        val extra = BootExtra()
            .setCardQuota("0,2,3") // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)

        val price = totalPrice.toInt().toDouble()

        val pg = "이니시스"
        val method = "카드"

        val items: MutableList<BootItem> = ArrayList()
        val item1 = BootItem().setName(restaurantName).setId("ITEM_CODE_MOUSE").setQty(1).setPrice(
            totalPrice.toInt().toDouble()
        )
        items.add(item1)

        val payload = Payload()
        payload.setApplicationId(AppId)
            .setOrderName("부트페이 결제테스트")
            .setPg(pg)
            .setOrderId("1234")
            .setMethod(method)
            .setPrice(price)
            .setUser(user)
            .setExtra(extra).items = items

        val map: MutableMap<String, Any> = HashMap()
        map["1"] = "abcdef"
        map["2"] = "abcdef55"
        map["3"] = 1234
        payload.metadata = map

        Bootpay.init(requireActivity().supportFragmentManager)
            .setPayload(payload)
            .setEventListener(object : BootpayEventListener {
                override fun onCancel(data: String) {
                    Log.d("bootpay", "cancel: $data")
                }

                override fun onError(data: String) {
                    Log.d("bootpay", "error: $data")
                }

                override fun onClose() {
                    Log.d("bootpay", "close")
                    Bootpay.removePaymentWindow()
                }


                override fun onIssued(data: String) {
                    Log.d("bootpay", "issued: $data")
                }

                override fun onConfirm(data: String): Boolean {
                    Log.d("bootpay", "confirm: $data")
                    //                        Bootpay.transactionConfirm(data); //재고가 있어서 결제를 진행하려 할때 true (방법 1)
                    return true //재고가 있어서 결제를 진행하려 할때 true (방법 2)
                    //                        return false; //결제를 진행하지 않을때 false
                }

                override fun onDone(data: String) {
                    Log.d("done", data)
                }
            }).requestPayment()
    }
}