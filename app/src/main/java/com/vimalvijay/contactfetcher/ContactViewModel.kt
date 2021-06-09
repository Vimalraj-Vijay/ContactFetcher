package com.vimalvijay.contactfetcher

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class ContactViewModel(application: Application) : AndroidViewModel(application) {
    var contactRepository: ContactRepository = ContactRepository(application.applicationContext)
    private var contactList: MutableLiveData<LinkedHashSet<ContactModel>>

    init {
        contactRepository.fetchAllContacts()
        contactList = contactRepository.loadAllContacts()
    }

    fun getAllContacts(): MutableLiveData<LinkedHashSet<ContactModel>> {
        return contactList
    }
}