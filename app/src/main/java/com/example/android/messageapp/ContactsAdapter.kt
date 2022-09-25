package com.example.android.messageapp

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.auth.User
import com.squareup.picasso.Picasso

class ContactsAdapter(val context:Context, private val contactList:ArrayList<com.example.android.messageapp.User>)
    :RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {
    class ContactsViewHolder(view:View):RecyclerView.ViewHolder(view ) {

        val name :TextView = view.findViewById(R.id.txtName)
        val email:TextView = view.findViewById(R.id.txtEmail)
        val status:TextView = view.findViewById(R.id.txtStatus)
        val image:ImageView = view.findViewById(R.id.imgProfileImage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val contactView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_contacts,parent,false)
        return ContactsViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val list = contactList[position]
        holder.name.text = list.profileName
        holder.email.text = list.profileEmail
        holder.status.text = list.profileStatus
        Picasso.get().load(list.profilePicture).error(R.drawable.ic_account).into(holder.image)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }
}