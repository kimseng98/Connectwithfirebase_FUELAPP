package com.example.connectwithfirebase.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.connectwithfirebase.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FingerprintActivity : AppCompatActivity() {

    private lateinit var etFingerPrint: EditText
    private lateinit var etChecking: EditText
    private lateinit var btCheckFinger: Button
    private lateinit var progressDialog: ProgressDialog

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference()
    private val userId = "UserID"
    private val checkingKey = "Checking"
    private lateinit var fingerprintListener: ValueEventListener
    private lateinit var checkingListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fingerprint_main)

        etFingerPrint = findViewById(R.id.etFingerPrintID)
        etChecking = findViewById(R.id.etChecking)
        btCheckFinger = findViewById(R.id.btnFingerPrint)

        // Create a reference to the location "Fingerprint -> UserID" in your database
        val dataRefFingerprint = databaseReference.child("Fingerprint").child(userId)
        fingerprintListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val data = dataSnapshot.getValue(Int::class.java)
                    etFingerPrint.setText(data.toString())
                } else {
                    etFingerPrint.setText("Not Found")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors that may occur while fetching the data
            }
        }
        dataRefFingerprint.addValueEventListener(fingerprintListener)

        // Create a reference to the location "Fingerprint -> Checking" in your database
        val dataRefChecking = databaseReference.child("Fingerprint").child(checkingKey)
        checkingListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val data = dataSnapshot.getValue(Int::class.java)
                    etChecking.setText(data.toString())
                    if(data == 2)
                        progressDialog.dismiss()

                } else {
                    etChecking.setText("Not Found")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors that may occur while fetching the data
            }
        }
        dataRefChecking.addValueEventListener(checkingListener)
        btCheckFinger.setOnClickListener {
            // Show the loading dialog
            showProgressDialog()

            // Perform any necessary tasks or actions here

            // For example, you can trigger actions to start the fingerprint check
            // and update the "Checking" value in the Firebase database.
            // When the value becomes 2, the dialog will be dismissed by the listener.
        }


    }
    private fun showProgressDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Place your finger on the fingerprint reader")
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove the listeners when the activity is destroyed to avoid memory leaks
        databaseReference.child("Fingerprint").child(userId).removeEventListener(fingerprintListener)
        databaseReference.child("Fingerprint").child(checkingKey).removeEventListener(checkingListener)
    }
}