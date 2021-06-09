package com.vimalvijay.contactfetcher.helpers

interface ContactClickListener {
    fun getContactData(name: String, number: String, isSelected: Boolean)
}