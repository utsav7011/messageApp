package com.example.android.messageapp

import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class Profile : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var profileNameShow: TextView;
    private lateinit var profileEmailShow: TextView
    private lateinit var profileStatusShow: TextView

    private lateinit var profilePictureShow: CircleImageView
    private lateinit var profilePictureAdd: ImageView

    private lateinit var profileNameEdit: TextInputLayout
    private lateinit var profileEmailEdit: TextInputLayout
    private lateinit var profileStatusEdit: TextInputLayout

    private lateinit var profileUpdate: Button
    private lateinit var profileSave: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var editName: TextInputEditText
    private lateinit var editEmail: TextInputEditText
    private lateinit var editStatus: TextInputEditText

    //    firebae var:
    private lateinit var auth: FirebaseAuth
    private lateinit var fstore: FirebaseFirestore
    private lateinit var db: DocumentReference

    private lateinit var userid: String

    //    bitmap var;
    private lateinit var image: ByteArray

    //     var for storage reference :
    private lateinit var storageReference: StorageReference
    val register = registerForActivityResult(ActivityResultContracts.TakePicturePreview()){
        uploadImage(it)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()

        profileNameEdit = view.findViewById(R.id.profileName)
        profileNameShow = view.findViewById(R.id.txtProfileName)
        editName = view.findViewById(R.id.etProfileName)

        profileEmailEdit = view.findViewById(R.id.profileEmail)
        profileEmailShow = view.findViewById(R.id.txtProfileEmail)
        editEmail = view.findViewById(R.id.etProfileEmail)

        profileStatusEdit = view.findViewById(R.id.profileStatus)
        profileStatusShow = view.findViewById(R.id.txtProfileStatus)
        editStatus = view.findViewById(R.id.etProfileStatus)

        profilePictureShow = view.findViewById(R.id.imgProfileImage)
        profilePictureAdd = view.findViewById(R.id.imgAddProfileImage)

        profileUpdate = view.findViewById(R.id.btUpdateProfile)
        profileSave = view.findViewById(R.id.btSaveProfile)

        progressBar = view.findViewById(R.id.profileProgressBar)


        profileUpdate.visibility = View.VISIBLE
        userid = auth.currentUser!!.uid

        //        firebase storage reference:
//        here we set a path that
//        there is a fo;der of name user id and there with a particular name there would be a image

        storageReference = FirebaseStorage.getInstance().reference.child("$userid/profilePhoto")

        db = fstore.collection("users").document(userid)
        db.addSnapshotListener { value, error ->
            if (error != null) {
                Log.d("Error", "Unable to fetch Data ")
            } else {
                profileNameShow.text = value?.getString("userName")
                profileEmailShow.text = value?.getString("userEmail")
                profileStatusShow.text = value?.getString("userStatus")

//                to get the profile pic from the url in the database and
//                incase the profile pic is missing then we have given a preset profile pic to avoid crashes
               Picasso.get().load(value?.getString("userProfilePhoto")).error(R.drawable.user).into(profilePictureShow)
            }
        }

        profileUpdate.setOnClickListener {

            profileNameShow.visibility = View.GONE
            profileEmailShow.visibility = View.GONE
            profileStatusShow.visibility = View.GONE

            profileNameEdit.visibility = View.VISIBLE
            profileStatusEdit.visibility = View.VISIBLE
            profileEmailEdit.visibility = View.VISIBLE
            profileSave.visibility = View.VISIBLE
            profileUpdate.visibility = View.GONE

            // to save the text that was previously entered :
            editName.text =
                Editable.Factory.getInstance().newEditable(profileNameShow.text.toString())
            editEmail.text =
                Editable.Factory.getInstance().newEditable(profileEmailShow.text.toString())
            editStatus.text =
                Editable.Factory.getInstance().newEditable(profileStatusShow.text.toString())


        }

        profileSave.setOnClickListener {
            profileNameShow.visibility = View.VISIBLE
            profileEmailShow.visibility = View.VISIBLE
            profileStatusShow.visibility = View.VISIBLE

            profileNameEdit.visibility = View.GONE
            profileStatusEdit.visibility = View.GONE
            profileEmailEdit.visibility = View.GONE
            profileSave.visibility = View.GONE
            profileUpdate.visibility = View.VISIBLE

            val obj = mutableMapOf<String, String>()
            obj["userName"] = editName.text.toString()
            obj["userEmail"] = editEmail.text.toString()
            obj["userStatus"] = editStatus.text.toString()


            db.set(obj).addOnSuccessListener {
                Log.d("Success", "Data Successfully Updated ")
            }
        }

        profilePictureAdd.setOnClickListener {
            capturePhoto()
        }


        return view

    }

    private fun capturePhoto() {
        register.launch(null)
    }

    private fun uploadImage(it: Bitmap?) {
        val baos = ByteArrayOutputStream()
//        the below function reduces the size of the bitmap :
        it!!.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        image = baos.toByteArray()
//        first line below gives the snapshot
//        2nd line goves the url :
        storageReference.putBytes(image).addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener {
//                store the url in the user document :
                val obj = mutableMapOf<String, String>()
                obj["userProfilePhoto"] = it.toString()
//                it in above line is the url of the image stored in the cloud storage :
                db.update(obj as Map<String,Any>).addOnSuccessListener {
                    Log.d("OnSuccess","Profile PictureUpdated ")
                }
            }
        }
    }


}