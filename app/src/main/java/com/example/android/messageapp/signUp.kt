package com.example.android.messageapp

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [signUp.newInstance] factory method to
 * create an instance of this fragment.
 */
class signUp : Fragment() {


    private lateinit var enterEmail: TextInputEditText
    private lateinit var enterPassword: TextInputEditText
    private lateinit var enterConfirmationPassword :TextInputEditText
    private lateinit var signUpButton : AppCompatButton
    private lateinit var progressBar:ProgressBar

    private lateinit var fAuth :FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var db : DocumentReference

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        enterEmail = view.findViewById(R.id.etSignUpEmail)
        enterPassword = view.findViewById(R.id.etSignUpPassword)
        enterConfirmationPassword = view.findViewById(R.id.etsignUpConfirmationPassword)
        signUpButton = view.findViewById(R.id.signUpButton)
        progressBar = view.findViewById(R.id.signUpProgressBar)
        fAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        signUpButton.setOnClickListener{
            val email:String = enterEmail.text.toString()
            val password:String = enterPassword.text.toString()
            val conformPassword = enterConfirmationPassword.text.toString()

//            TextUtils is a function to check the string :
            if (TextUtils.isEmpty(email)){
                enterEmail.error = "Email is required : "
            }else if (TextUtils.isEmpty(password)){
                enterPassword.error = "Password is required : "
            }else
                if (password.length <6){
                    enterPassword.error = "password length must be greater than 6"
                }
            else
                if (password != conformPassword){
                    enterConfirmationPassword.error = "password and confirm password must be same : "
                }
            else{
                    progressBar.visibility = View.VISIBLE
                    createAccount(email,password)
                }
        }

        return view
    }

    private fun createAccount(em:String, password:String){
        fAuth.createUserWithEmailAndPassword(em,password).addOnCompleteListener{
            task->
                if (task.isSuccessful){
                    val userInfo = fAuth.currentUser?.uid
                    db = fStore.collection("users").document(userInfo.toString())
                    val obj = mutableMapOf<String,String>()
                    obj["userEmail"] = em
                    obj["userPassword"] = password
                    obj["uerStatus"] = ""
                    obj["userName"] = ""
                    db.set(obj).addOnSuccessListener {
                        Log.d("OnSuccess", "User Created Successfully : ")
                        progressBar.visibility = View.GONE

                        Toast.makeText(context, "Welcome to the JQAS App",Toast.LENGTH_SHORT).show()

                    }
                }
        }
    }

}