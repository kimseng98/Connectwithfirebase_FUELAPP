package com.example.connectwithfirebase.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.connectwithfirebase.models.EmployeeModel
import com.example.connectwithfirebase.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var etDeviceName: EditText
    private lateinit var etDefaultValue: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etDeviceName = findViewById(R.id.etDeviceName)
        etDefaultValue = findViewById(R.id.etDefaultValue)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("FYP")

        btnSaveData.setOnClickListener {
            saveEmployeeData()
        }
    }

    private fun saveEmployeeData() {

        //getting values
        val empName = etDeviceName.text.toString()
        val empAge = etDefaultValue.text.toString()

        if (empName.isEmpty()) {
            etDeviceName.error = "Please enter name"
        }
        if (empAge.isEmpty()) {
            etDefaultValue.error = "Please enter age"
        }


        val empId = empName

        val employee = EmployeeModel(empId, empName, empAge)

        dbRef.child(empId).setValue(employee)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                etDeviceName.text.clear()
                etDefaultValue.text.clear()


            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }

    }

}