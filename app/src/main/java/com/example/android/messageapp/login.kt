package com.example.android.messageapp

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [login.newInstance] factory method to
 * create an instance of this fragment.
 */
class login : Fragment() {
    private lateinit var enterEmail: TextInputEditText
    private lateinit var enterPassword: TextInputEditText
    private lateinit var loginButton: AppCompatButton
    private lateinit var googleButton: AppCompatButton
    private lateinit var progressBar: ProgressBar

//    google signin client :
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var googleSignInOptions : GoogleSignInOptions

    private lateinit var fAuth: FirebaseAuth

// a way to launch the intent  :
    private lateinit var resultLaunch:ActivityResultLauncher<Intent>

    private val RC_SIGN_IN = 1011

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        enterEmail = view.findViewById(R.id.etLoginEmail)
        enterPassword = view.findViewById(R.id.etLoginPassword)
        loginButton = view.findViewById(R.id.loginButton)
        googleButton = view.findViewById(R.id.googleLoginButton)
        progressBar = view.findViewById(R.id.loginProgressBar)


        loginButton.setOnClickListener {
            //            TextUtils is a function to check the string :
            if (TextUtils.isEmpty(enterEmail.text.toString())) {
                enterEmail.error = "Email is required : "
            } else if (TextUtils.isEmpty(enterPassword.text.toString())) {
                enterPassword.error = "Password is required : "
            } else {
                progressBar.visibility = View.VISIBLE
                val email: String = enterEmail.text.toString()
                val password: String = enterPassword.text.toString()
                signIn(email, password)
            }
        }

        googleButton.setOnClickListener {
            createRequest()
        }
        //here we give the result launch a contract  for what purpose we need to use
        resultLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
                if (result.resultCode== Activity.RESULT_OK){
                    val launchData = result.data
                    val task = GoogleSignIn.getSignedInAccountFromIntent(launchData)
                    try{
                        val account = task.getResult(ApiException::class.java)
                        Log.d("Gmail ID", "firebaseAuthWithGoogle : $account")

                        firebaseAuthWithGoogle(account?.idToken)

                    }catch (e:ApiException){
                        Log.w("Error", "Google Signin Failed : ",e)
                    }
                }

        }


        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment login.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            login().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun signIn(email:String, password:String){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener{
            task->
            if(task.isSuccessful){
//                context here as we are in the fragment and not in the activity :
                Toast.makeText(context, "user login Successful -_-", Toast.LENGTH_SHORT).show()

            }
        }

    }

    private fun createRequest() {
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


    }

    private fun firebaseAuthWithGoogle(idToken: String?) {

    }


}