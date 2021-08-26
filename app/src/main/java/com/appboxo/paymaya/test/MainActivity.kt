package com.appboxo.paymaya.test

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.appboxo.sdk.Appboxo
import com.appboxo.ui.main.AppboxoActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.miniapp_btn).setOnClickListener {
            Appboxo.getMiniapp("app34795")
                .setAuthListener { _, miniapp ->
                    miniapp.setAuthCode("x+aipn0ODYv4+PZnqiogWVR1rGA4YQFlwmFTT6GV2Rza9LelWQjFUXy5k1SMgmZolDGd5+zh0IJc3vokMBldC74qkKUUoPI1z4K381kbBOW0az+xfs/UCZFkQZbSq5iEn7fofVHxCaSh66JZjZrjzuc9dSkW3IaUBjW/krZpV2Qr58emYQrUqBHb2N7YM01msXJPaasLbEaS3I3FCZ3cfcoSTZxzWQwQ3Chv2jpcaDEJ/7nHekInkwjgAa5x03Eu9bhlzv3uqbQd7xMOFEIRCGOubYkXn2udueeQuzG+tYv+CuShiI6KRbpXwtWmQidgsO1LhAP4KK6IZg4j3OHoZg==")
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