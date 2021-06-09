package com.vimalvijay.contactfetcher

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import androidx.lifecycle.MutableLiveData
import com.vimalvijay.contactfetcher.helpers.SharedDatas


class ContactRepository(val context: Context) {
    var sharedDatas: SharedDatas = SharedDatas(context)

    var contactList: MutableLiveData<LinkedHashSet<ContactModel>> =
        MutableLiveData<LinkedHashSet<ContactModel>>()

    fun fetchAllContacts() {
        var contactsName: String
        var contactsNumber: String
        val contactModel = LinkedHashSet<ContactModel>()
        val contactCursor: Cursor? = context.contentResolver.query(
            Phone.CONTENT_URI,
            arrayOf(Phone._ID, Phone.DISPLAY_NAME, Phone.NUMBER),
            null,
            null,
            Phone.DISPLAY_NAME + " ASC"
        )

        while (contactCursor?.moveToNext()!!) {
            contactsName =
                contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY))
            contactsNumber =
                contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            contactModel.add(
                ContactModel(
                    contactsName, contactsNumber, isContactedSelected(
                        contactsNumber
                    )
                )
            )
        }
        contactCursor.close()
        contactList.postValue(contactModel)
    }

    fun loadAllContacts(): MutableLiveData<LinkedHashSet<ContactModel>> {
        return contactList
    }

    fun isContactedSelected(number: String): Boolean {
        if (sharedDatas.getData("ContactsSaved").isNullOrEmpty()) {
            return false
        } else {
            val array = sharedDatas.getData("ContactsSaved")?.split(",")
            for (i in array?.indices!!) {
                if (array[i].split("_")[1].equals(number, true)) {
                    return true
                }
            }
            return false
        }
    }

}