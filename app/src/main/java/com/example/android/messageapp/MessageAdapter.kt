package com.example.android.messageapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter (val context: Context, val messageList: ArrayList<messageModel>):RecyclerView.Adapter<MessageAdapter.MessageViewHolder>(){
    class MessageViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val message:TextView = view.findViewById(R.id.txtMessage)
        val time:TextView = view.findViewById(R.id.txtTime)
    }
    private val left = 0
    private val right = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        if (viewType == right){
            val messageView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_send,parent,false)
            return MessageViewHolder(messageView)
        }else{
            val messageView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_receive,parent, false)
            return MessageViewHolder(messageView)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].sender == FirebaseAuth.getInstance().currentUser?.uid.toString()){
            right

        }else{
            left
        }
    }
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val list = messageList[position]
        holder.message.text = list.message
        holder.time.text = list.timeStamp
    }

    override fun getItemCount(): Int {
        return messageList.size
    }


}
