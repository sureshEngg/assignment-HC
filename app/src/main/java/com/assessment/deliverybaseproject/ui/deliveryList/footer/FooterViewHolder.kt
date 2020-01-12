package com.assessment.deliverybaseproject.ui.deliveryList.footer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.assessment.deliverybaseproject.R

class FooterViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        fun create(parent: ViewGroup): FooterViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_footer, parent, false)
            return FooterViewHolder(view)
        }
    }
}