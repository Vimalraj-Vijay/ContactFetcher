package com.vimalvijay.contactfetcher

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vimalvijay.contactfetcher.adapters.ContactListAdapter
import com.vimalvijay.contactfetcher.helpers.ContactClickListener
import com.vimalvijay.contactfetcher.helpers.SharedDatas

const val requestCodeForContact = 202

class MainActivity : AppCompatActivity(), ContactClickListener {
    lateinit var contactViewModel: ContactViewModel
    lateinit var rvContacts: RecyclerView
    lateinit var tvEmpty: TextView
    private lateinit var contactListAdapter: ContactListAdapter

    lateinit var sharedDatas: SharedDatas
    var contactNumberList = HashSet<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvContacts = findViewById<RecyclerView>(R.id.rv_contacts)
        tvEmpty = findViewById<TextView>(R.id.tv_empty)
        contactListAdapter = ContactListAdapter(this)
        rvContacts.layoutManager = LinearLayoutManager(this)
        rvContacts.adapter = contactListAdapter
        sharedDatas = SharedDatas(this)
        checkAppContactPermission()

    }

    private fun initViewModel() {
        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
    }

    private fun checkAppContactPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_CONTACTS
            )
            || ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_CALL_LOG
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_PHONE_STATE
            )
        ) {
            Toast.makeText(this, getString(R.string.permission), Toast.LENGTH_LONG).show()
            accessPermission()
        } else {
            accessPermission()
        }
    }

    private fun accessPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CALL_LOG
            ),
            requestCodeForContact
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            requestCodeForContact -> {
                if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED)) {
                    initViewModel()
                    getAllContacts()
                } else {
                    tvEmpty.text = getString(R.string.permission_settings)
                    Toast.makeText(this, getString(R.string.permission_settings), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun getAllContacts() {
        contactViewModel.getAllContacts().observe(this, {
            if (it.isEmpty()) {
                rvContacts.visibility = View.GONE
                tvEmpty.visibility = View.VISIBLE
                tvEmpty.text = getString(R.string.empty_contact)
            } else {
                rvContacts.visibility = View.VISIBLE
                tvEmpty.visibility = View.GONE
                contactListAdapter.loadAllContacts(it.toMutableList())
            }
        })
    }

    override fun getContactData(name: String, number: String, isSelected: Boolean) {
        if (isSelected) {
            contactNumberList.add(name + "_" + number)
        } else {
            contactNumberList.remove(name + "_" + number)
        }
        sharedDatas.saveData("ContactsSaved", TextUtils.join(",", contactNumberList))
    }
}