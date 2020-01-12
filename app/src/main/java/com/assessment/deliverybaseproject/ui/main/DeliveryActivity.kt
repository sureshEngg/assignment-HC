package com.assessment.deliverybaseproject.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.assessment.deliverybaseproject.R
import com.assessment.deliverybaseproject.ui.deliveryList.DeliveryListFragment

class DeliveryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_list)

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction().add(
                R.id.fl_container,
                DeliveryListFragment.newInstance()
            ).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
