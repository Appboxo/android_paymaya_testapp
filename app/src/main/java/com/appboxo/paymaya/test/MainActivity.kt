package com.appboxo.paymaya.test

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.appboxo.sdk.Appboxo
import com.appboxo.ui.main.AppboxoActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.miniapp_btn).setOnClickListener {
            Appboxo.getMiniapp("app34795")
                .setAuthListener { _, miniapp ->
                    GlobalScope.launch(Dispatchers.IO) {
                        runCatching {
                            val url =
                                URL("https://9w1kyzp49e.execute-api.ap-southeast-1.amazonaws.com/Test/encryptedUserIdentifier")
                            val urlConnection = url.openConnection() as HttpURLConnection
                            val text = urlConnection.inputStream.bufferedReader().readText()
                            val json = JSONObject(text)
                            miniapp.setAuthCode(json.getString("body"))
                        }
                    }
                }
                .setPaymentEventListener { appboxoActivity: AppboxoActivity, miniapp, paymentData ->
                    appboxoActivity.doOnActivityResult { requestCode, resultCode, data ->
                        if (requestCode == 12345) {
                            if (resultCode == Activity.RESULT_OK) {
                                val hostAppOrderId = data?.getStringExtra("hostappOrderId") ?: ""
                                paymentData.hostappOrderId = hostAppOrderId
                                paymentData.status = "success"
                            } else
                                paymentData.status = "cancel"
                            miniapp.sendPaymentResult(paymentData)
                        }
                    }
                    appboxoActivity.startActivityForResult(
                        Intent(appboxoActivity, PaymentActivity::class.java)
                            .putExtra("transactionToken", paymentData.transactionToken)
                            .putExtra("amount", paymentData.amount)
                            .putExtra("currency", paymentData.currency)
                            .putExtra("orderId", paymentData.miniappOrderId),
                        12345
                    )
                }
                .open(this)
        }
    }
}