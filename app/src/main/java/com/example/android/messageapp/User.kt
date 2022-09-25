package com.example.android.messageapp

import android.provider.ContactsContract.CommonDataKinds.Email

data class User(

    val profileName:String,
    val profileEmail: String,
    val profileStatus:String,
    val profilePicture:String

    )
