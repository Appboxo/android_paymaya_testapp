package com.appboxo.paymaya.test

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val transactionToken = intent.getStringExtra("transactionToken")
        val amount = intent.getDoubleExtra("amount", 0.0)
        val currency = intent.getStringExtra("currency")
        val orderId = intent.getStringExtra("orderId")

        findViewById<Button>(R.id.pay).setOnClickListener {
            setResult(Activity.RESULT_OK, intent.putExtra("hostappOrderId", "order123456789"))
            finish()
        }
        findViewById<Button>(R.id.cancel).setOnClickListener {
            finish()
        }
        findViewById<TextView>(R.id.amount).text = "$currency $amount"
        findViewById<TextView>(R.id.checkout_id).text = orderId

    }
}