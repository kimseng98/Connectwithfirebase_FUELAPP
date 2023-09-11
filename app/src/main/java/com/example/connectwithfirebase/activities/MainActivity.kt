package com.example.connectwithfirebase.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.connectwithfirebase.R
import com.example.connectwithfirebase.adapters.EmpAdapter
import com.example.connectwithfirebase.models.DeviceModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var btnInsertData: Button
    private lateinit var btnFetchData: Button
    private lateinit var empRecyclerView: RecyclerView
    private lateinit var empList: ArrayList<DeviceModel>
    private lateinit var dbRef: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        empRecyclerView = findViewById(R.id.rvEmpMain)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        empList = arrayListOf<DeviceModel>()

        getEmployeesData()

        val sheet = findViewById<FrameLayout>(R.id.sheet)
        BottomSheetBehavior.from(sheet).apply{
            peekHeight=450
            this.state=BottomSheetBehavior.STATE_COLLAPSED
        }

        val firebase : DatabaseReference = FirebaseDatabase.getInstance().getReference()

        btnInsertData = findViewById(R.id.btnInsertData)
        btnFetchData = findViewById(R.id.btnFetchData)

        btnInsertData.setOnClickListener {
            val intent = Intent(this, InsertionActivity::class.java)
            startActivity(intent)
        }

        btnFetchData.setOnClickListener {
            val intent = Intent(this, FetchingActivity::class.java)
            startActivity(intent)
        }

    }


    private fun getEmployeesData() {

        empRecyclerView.visibility = View.GONE


        dbRef = FirebaseDatabase.getInstance().getReference("FYP")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                empList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val empData = empSnap.getValue(DeviceModel::class.java)
                        empList.add(empData!!)
                    }
                    val mAdapter = EmpAdapter(empList)
                    empRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : EmpAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@MainActivity, EmployeeDetailsActivity::class.java)

                            //putextras
                            intent.putExtra("deviceId",empList[position].deviceId)
                            intent.putExtra("deviceName",empList[position].deviceName)
                            intent.putExtra("deviceDefaultValue",empList[position].deviceDefaultValue)
                            //intent.putExtra("empSalary",empList[position].empSalary)
                            startActivity(intent)
                        }

                    })

                    empRecyclerView.visibility = View.VISIBLE

                }
                else
                {
                    Toast.makeText(this@MainActivity,"nono", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}