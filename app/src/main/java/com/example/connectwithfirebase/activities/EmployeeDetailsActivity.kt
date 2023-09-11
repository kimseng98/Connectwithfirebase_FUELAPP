package com.example.connectwithfirebase.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.connectwithfirebase.models.DeviceModel
import com.example.connectwithfirebase.R
import com.google.firebase.database.FirebaseDatabase

class EmployeeDetailsActivity : AppCompatActivity() {

    private lateinit var tvEmpId: TextView
    private lateinit var tvEmpName: TextView
    private lateinit var tvEmpValue: TextView
    private lateinit var tvEmpAge: TextView
    //private lateinit var tvEmpSalary: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("deviceId").toString(),
                intent.getStringExtra("deviceName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("deviceId").toString()
            )
        }
    }
    private fun initView() {
        tvEmpId = findViewById(R.id.tvEmpId)
        tvEmpName = findViewById(R.id.tvEmpName)
        //tvEmpValue = findViewById(R.id.tvEmpValue)
        tvEmpAge = findViewById(R.id.tvEmpAge)
        //tvEmpSalary = findViewById(R.id.tvEmpSalary)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvEmpId.text = intent.getStringExtra("deviceId")
        tvEmpName.text = intent.getStringExtra("deviceName")
        //tvEmpValue.text = intent.getStringExtra("deviceDefaultValue")
        val defaultValue = 0 // You can change this to your preferred default value
        val deviceDefaultValue = intent.getIntExtra("deviceDefaultValue", defaultValue)

        // Set the integer value as text for tvEmpAge
        tvEmpAge.text = deviceDefaultValue.toString()
        //tvEmpSalary.text = intent.getStringExtra("empSalary")

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("FYP").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openUpdateDialog(
        empId: String,
        empName: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etName = mDialogView.findViewById<EditText>(R.id.etDeviceName)
        val etValue = mDialogView.findViewById<EditText>(R.id.etDefaultValue)
        //val etEmpSalary = mDialogView.findViewById<EditText>(R.id.etEmpSalary)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etName.setText(intent.getStringExtra("deviceName").toString())
        etValue.setText(intent.getStringExtra("deviceDefaultValue").toString())
        //etEmpSalary.setText(intent.getStringExtra("empSalary").toString())

        mDialog.setTitle("Updating $empName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            val empValueString = etValue.text.toString()

            // Convert empValueString to an Int
            val empValueInt: Int? = empValueString.toIntOrNull()
            if (empValueInt != null) {
                updateEmpData(
                    empId,
                    etName.text.toString(),
                    empValueInt,
                    //etEmpSalary.text.toString()
                )
            }

            Toast.makeText(applicationContext, "Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvEmpName.text = etName.text.toString()
            tvEmpAge.text = empValueInt.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateEmpData(
        id: String,
        name: String,
        value: Int,

        ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("FYP").child(id)
        val empInfo = DeviceModel(id, name, value)
        dbRef.setValue(empInfo)
    }

}