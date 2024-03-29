package com.example.upcoin

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.widget.SwitchCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Set padding for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val switchButton = findViewById<SwitchCompat>(R.id.switchButton)
        val textView = findViewById<TextView>(R.id.incexp)
        val editText = findViewById<EditText>(R.id.inputID)
        val textViewB = findViewById<TextView>(R.id.balanceID)
        val myButton = findViewById<Button>(R.id.myButton)

        // Display current balance
        val dbCurrent = userData(this).readableDatabase
        val cursor = dbCurrent.rawQuery("SELECT SUM(amount) FROM UserData", null)
        var currentAmount = 0.0
        if (cursor.moveToFirst()) {
            currentAmount = cursor.getDouble(0)
        }
        cursor.close()
        dbCurrent.close()
        textViewB.text = "₱ $currentAmount"

        switchButton.setOnCheckedChangeListener { _, isChecked ->
            textView.text = if (isChecked) "Expense" else "Income"
        }

        myButton.setOnClickListener {
            val inputAmount = editText.text.toString().toDoubleOrNull() ?: 0.0
            val multiplier = if (switchButton.isChecked) -1 else 1
            val result = inputAmount * multiplier

            // Insert new amount into database
            val db = userData(this).writableDatabase
            try {
                val values = ContentValues().apply {
                    put("amount", result)
                }
                db.insert("UserData", null, values)
            } catch (e: Exception) {
                Log.e("userData", "Error inserting data into database", e)
            } finally {
                db.close()
            }

            // Update total balance
            val dbSum = userData(this).readableDatabase
            val sumCursor = dbSum.rawQuery("SELECT SUM(amount) FROM UserData", null)
            var totalAmount = 0.0
            if (sumCursor.moveToFirst()) {
                totalAmount = sumCursor.getDouble(0)
            }
            sumCursor.close()
            dbSum.close()
            textViewB.text = "₱ $totalAmount"

            // Clear input text field
            editText.text.clear()
        }
    }
}


