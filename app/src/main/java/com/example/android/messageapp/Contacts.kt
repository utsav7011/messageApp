package com.example.android.messageapp

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Document

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Contacts.newInstance] factory method to
 * create an instance of this fragment.
 */
class Contacts : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var contactsRecyclerView: RecyclerView
    private lateinit var contactLayoutManager: RecyclerView.LayoutManager
    private lateinit var contactAdapter: ContactsAdapter
    private val contactsInfo = arrayListOf<User>()
    private lateinit var fstore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)

        auth  = FirebaseAuth.getInstance()
        contactsRecyclerView = view.findViewById(R.id.contactsRecyclerView)
        contactLayoutManager = LinearLayoutManager(context as Activity)
//firebase variable initialization :
        fstore = FirebaseFirestore.getInstance()
        fstore.collection("users").get().addOnSuccessListener {
//            if userid of 2 users matching in the document then we will not show the id of that user:

            if (!it.isEmpty) {
                val listContact = it.documents
                for (i in listContact) {
                    if (i.id == auth.currentUser?.uid) {
                        Log.d("onFound", "This is User Account")
                    } else {

                        val contact = User(i.getString("userName").toString(),
                                            i.getString("userEmail").toString(),
                                            i.getString("userStatus").toString(),
                                            i.getString("userProfilePhoto").toString())

                        contactsInfo.add(contact)
                        contactAdapter = ContactsAdapter(context as Activity, contactsInfo)
                        contactsRecyclerView.adapter = contactAdapter
                        contactsRecyclerView.layoutManager = contactLayoutManager

        //        after every item we need to add the line to separate the one from other
//                        contactsRecyclerView.addItemDecoration(DividerItemDecoration(contactsRecyclerView.context,
//                            (contactLayoutManager as LinearLayoutManager).orientation))
                    }
                }
            }

        }
        return view
    }


}