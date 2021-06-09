package com.vimalvijay.contactfetcher.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vimalvijay.contactfetcher.ContactModel
import com.vimalvijay.contactfetcher.databinding.ContactListBinding
import com.vimalvijay.contactfetcher.helpers.ContactClickListener

class ContactListAdapter(val contactClickListener: ContactClickListener) :
    RecyclerView.Adapter<ContactListAdapter.ViewHolder>() {
    var contactList: MutableList<ContactModel> = ArrayList()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ContactListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contactModel = contactList[position]
        holder.bind(contactModel)
        holder.itemView.setOnClickListener {
            contactModel.isSelected = !contactModel.isSelected
            contactClickListener.getContactData(
                contactList[position].name,
                contactList[position].number,
                contactModel.isSelected
            )
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    inner class ViewHolder(
        private val contactListBinding: ContactListBinding
    ) : RecyclerView.ViewHolder(contactListBinding.root) {
        fun bind(contactModel: ContactModel) {
            if (contactModel.isSelected) {
                contactListBinding.ivSelect.visibility = View.VISIBLE
            } else {
                contactListBinding.ivSelect.visibility = View.GONE
            }
            contactListBinding.contactModel = contactModel
            contactListBinding.executePendingBindings()
        }
    }

    fun loadAllContacts(contactList: MutableList<ContactModel>) {
        this.contactList = contactList
        notifyDataSetChanged()
    }
}