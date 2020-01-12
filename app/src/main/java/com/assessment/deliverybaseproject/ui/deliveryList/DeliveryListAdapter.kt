package com.assessment.deliverybaseproject.ui.deliveryList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.assessment.deliverybaseproject.databinding.LayoutItemDeliveryBinding
import com.assessment.deliverybaseproject.model.Delivery
import com.assessment.deliverybaseproject.ui.deliveryList.footer.FooterViewHolder

class DeliveryListAdapter(var listener: DeliveryItemListener) :
    PagedListAdapter<Delivery, RecyclerView.ViewHolder>(DELIVERY_COMPARATOR) {

    private var isNetworkInProgress: Boolean = false

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_DATA) {
            val itemBinding = LayoutItemDeliveryBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            DeliveryViewHolder(itemBinding)
        } else {
            FooterViewHolder.create(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_DATA)
            (holder as DeliveryViewHolder).bind(getItem(position))
        else (holder as FooterViewHolder)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) VIEW_TYPE_DATA else VIEW_TYPE_FOOTER
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (isNetworkInProgress)
    }

    inner class DeliveryViewHolder internal constructor(
        private val mDataBinding: LayoutItemDeliveryBinding
    ) : RecyclerView.ViewHolder(mDataBinding.root), View.OnClickListener {

        init {
            val itemView = mDataBinding.root
            itemView.setOnClickListener(this)
        }

        internal fun bind(delivery: Delivery?) {
            if (delivery != null) {
                mDataBinding.viewModel = delivery
            }
        }

        override fun onClick(view: View) {
            if (adapterPosition > RecyclerView.NO_POSITION) {
                val delivery = getItem(adapterPosition)
                if (delivery != null) {
                    listener.onDeliverySelected(delivery)
                }
            }
        }
    }

    fun setNetworkProgress(isProgress: Boolean) {
        isNetworkInProgress = isProgress
        try {
            notifyItemChanged(super.getItemCount())
        } catch (ex: ArrayIndexOutOfBoundsException) {
            Log.d(LOG_TAG, ex.message)
        }
    }

    companion object {
        private const val VIEW_TYPE_DATA = 1
        private const val VIEW_TYPE_FOOTER = 2
        private val LOG_TAG = DeliveryListAdapter::class.java.simpleName
        private val DELIVERY_COMPARATOR = object : DiffUtil.ItemCallback<Delivery>() {
            override fun areItemsTheSame(oldItem: Delivery, newItem: Delivery): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Delivery, newItem: Delivery): Boolean {
                return oldItem == newItem
            }
        }
    }
}