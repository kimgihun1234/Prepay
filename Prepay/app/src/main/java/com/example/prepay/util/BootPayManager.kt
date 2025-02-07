package com.example.prepay.util

import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.example.prepay.R
import com.example.prepay.R.string.bootPayApiKey
import kr.co.bootpay.android.Bootpay
import kr.co.bootpay.android.events.BootpayEventListener
import kr.co.bootpay.android.models.BootExtra
import kr.co.bootpay.android.models.BootItem
import kr.co.bootpay.android.models.BootUser
import kr.co.bootpay.android.models.Payload
import org.json.JSONObject
import java.lang.Exception

object BootPayManager {
    private const val TAG = "BootPayManager"


    fun startPayment(activity: FragmentActivity, restaurantName: String, totalPrice: String, onPaymentSuccess: (String, Int) -> Unit) {
        val bootPayKey = activity.getString(R.string.bootPayApiKey)  // Context 사용해서 가져오기
        val user = BootUser().setPhone("010-1234-5678")
        val extra = BootExtra().setCardQuota("0,2,3")
        val price = totalPrice.toDoubleOrNull() ?: 0.0
        val item = BootItem().setName(restaurantName)
            .setId("ITEM_CODE")
            .setQty(1)
            .setPrice(price)

        val payload = Payload().apply {
            setApplicationId(bootPayKey)
            setOrderName(restaurantName)
            setPg("이니시스")
            setOrderId("1234")
            setMethod("카드")
            setPrice(price)
            setUser(user)
            setExtra(extra)
            items = listOf(item)
            metadata = mapOf("1" to "abcdef", "2" to "abcdef55", "3" to 1234)
        }

        Bootpay.init(activity.supportFragmentManager)
            .setPayload(payload)
            .setEventListener(object : BootpayEventListener {
                override fun onCancel(data: String?) {
                    Log.d("bootpay", "cancel: $data")
                }

                override fun onError(data: String?) {
                    Log.d("bootpay", "error: $data")
                }
                override fun onClose() {
                    Log.d(TAG, "close")
                    Bootpay.removePaymentWindow()
                }
                override fun onIssued(data: String?) {
                    Log.d("bootpay", "issued: $data")
                }

                override fun onConfirm(data: String) : Boolean {
                    Log.d("bootpay", "confirm: $data")
                    //                        Bootpay.transactionConfirm(data); //재고가 있어서 결제를 진행하려 할때 true (방법 1)
                    return true //재고가 있어서 결제를 진행하려 할때 true (방법 2)
                    //                        return false; //결제를 진행하지 않을때 false
                }
                override fun onDone(data: String) {
                    Log.d("done", data)
                    try {
                        val jsonObject = JSONObject(data)
                        val dataObject = jsonObject.getJSONObject("data")
                        val receiptId = dataObject.getString("receipt_id")
                        val price = dataObject.getString("price")
                        Log.d("receipt ID", receiptId)
                        Log.d("price", price)
                        onPaymentSuccess(receiptId, price.toInt())
                    } catch (e: Exception) {
                        Log.e("JSON Error", "Failed to parse receipt_id: ${e.message}")
                    }
                }
            }).requestPayment()
    }

}