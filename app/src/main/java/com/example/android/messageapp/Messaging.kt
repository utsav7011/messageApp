package com.example.android.messageapp

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.Calendar
import kotlin.math.min

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Messaging.newInstance] factory method to
 * create an instance of this fragment.
 */
class Messaging : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var sendMessageEditText: EditText
    private lateinit var sendMessageButton: FloatingActionButton
    private lateinit var fstore: FirebaseFirestore
    private lateinit var fauth: FirebaseAuth
    private lateinit var messageLayoutManager: RecyclerView.LayoutManager
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var db: DocumentReference
    private lateinit var userid: String
    private val messageInfo = arrayListOf<messageModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_messaging, container, false)
//xml var initialization
        messageRecyclerView = view.findViewById(R.id.messageRecyclerView)
        sendMessageEditText = view.findViewById(R.id.etSendMessage)
        sendMessageButton = view.findViewById(R.id.btSendMessage)

//        firebase initialization
        fstore = FirebaseFirestore.getInstance()
        fauth = FirebaseAuth.getInstance()

        userid = fauth.currentUser!!.uid.toString()
        fstore.collection("chats").whereArrayContains("uids", userid)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.d("onError", "Error in Fetching the data ")
                } else {
//                fetch the snapshot in the document to read it and fetch the required data :
                    val list = snapshot?.documents
                    if (list != null) {
                        for (doc in list) {
                            db = fstore.collection("chats").document(doc.id).collection("message")
                                .document()
                            fstore.collection("chats").document(doc.id).collection("message")
                                .orderBy("id",Query.Direction.ASCENDING)
                                .addSnapshotListener { snapshot, exception ->

                                    if (snapshot != null) {
                                        if (!snapshot.isEmpty){
                                            messageInfo.clear()
                                            val list = snapshot.documents
                                            for (document in list){
                                                val obj = messageModel(document.getString("messageSender").toString(),
                                                document.getString("message").toString(),
                                                document.getString("messageTime").toString())
                                                messageInfo.add(obj)
                                                messageAdapter = MessageAdapter(context as Activity, messageInfo)
                                                messageRecyclerView.adapter = messageAdapter
                                                messageRecyclerView.layoutManager = messageLayoutManager
//                                                to scroll to the n-1th element of the document :
                                                messageRecyclerView.scrollToPosition(list.size-1)
                                                messageRecyclerView.adapter!!.notifyDataSetChanged()
                                            }
                                        }
                                    }

                                }
                        }
                    }

                }
            }

//      layoutManager initialization :
        messageLayoutManager = LinearLayoutManager(context)
        messageRecyclerView = view.findViewById(R.id.messageRecyclerView)


//        send butotn click Listener :
        sendMessageButton.setOnClickListener {
            sendMessage()
        }

        return view
    }

    private fun sendMessage() {
        val message = sendMessageEditText.text.toString()
        if (TextUtils.isEmpty(message)) {
            sendMessageEditText.error = "Empty Message cannot be sent : "
        } else {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            val timeStamp = "$hour : $minute"
            val messageObject = mutableMapOf<String, Any>().also {
                it["message"] = message
                it["messageId"] = 1
                it["messageSender"] = userid
//                it["messageReceiver"] =
                it["messageTime"] = timeStamp
            }
            db.set(messageObject).addOnSuccessListener {
                Log.d("onSuccess", "Successfully sent Message")
            }

        }
    }


}
