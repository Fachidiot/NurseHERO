package com.fachidiot.nursehro.MainFragment

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.facebook.login.LoginManager
import com.fachidiot.nursehro.LoginActivity
import com.fachidiot.nursehro.Class.MySharedPreferences
import com.fachidiot.nursehro.Class.CustomUserInfo
import com.fachidiot.nursehro.Class.ProgressDialog
import com.fachidiot.nursehro.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.main_account_fragment.*
import java.io.File

class MainAccountFragment : Fragment() {
    private val permission_list = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private var customProgressDialog: ProgressDialog? = null

    private var imageUri: Uri? = null
    private var pathUri: String? = null
    private var tempFile: File? = null
    private var imageSet: Boolean = false

    private lateinit var mFirebaseAuth : FirebaseAuth
    private lateinit var mFirebaseStorage: FirebaseStorage
    private lateinit var mFirebaseStoreDatabase: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //????????? ?????? ??????
        customProgressDialog = ProgressDialog(context)
        //???????????? ????????????
        customProgressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        customProgressDialog!!.setCancelable(false)
        customProgressDialog!!.show()

        mFirebaseStoreDatabase = Firebase.firestore
        mFirebaseStorage = FirebaseStorage.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()

        if(mFirebaseAuth.currentUser != null)
            setInfo()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_account_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        UserProfile_image.setOnClickListener {
            onPermissionCheck()
        }

        LogoutButton.setOnClickListener {
            logOut()
        }

        //LogoutButton.setOnClickListener {
        //    imageAdd()
        //}

        LoginButton.setOnClickListener {
            activity?.let {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
        }
 
    }


    // ?????? ?????????
    private fun gotoAlbum() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val tag = "Album Message"
        if (resultCode != AppCompatActivity.RESULT_OK) { // ????????? ????????????
            Toast.makeText(context, "?????? ???????????????", Toast.LENGTH_SHORT).show()
            if (tempFile != null) {
                if (tempFile!!.exists()) {
                    if (tempFile!!.delete()) {
                        Log.e(tag, tempFile!!.absolutePath.toString() + " ?????? ??????")
                        tempFile = null
                    }
                }
            }
            return
        }
        when (requestCode) {
            1 -> {
                // ?????? ??????
                // Uri
                if (data != null) {
                    imageUri = data.data
                }
                if (data != null) {
                    pathUri = getPath(data.data)
                }
                Log.d(this.toString(), "PICK_FROM_ALBUM photoUri : $imageUri")
                imageAdd()
                UserProfile_image.setImageURI(imageUri) // ????????? ??????
                imageSet = true
            }
        }
    }

    // uri ???????????? ????????????
    private fun getPath(uri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursorLoader = context?.let {
            CursorLoader(
                it,
                uri!!, proj, null, null, null
            )
        }
        val cursor = cursorLoader!!.loadInBackground()
        val index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(index)
    }

    private fun imageAdd() {
        // path
        val file: Uri = Uri.fromFile(File(pathUri))

        //TODO : ?????? ???????????? ??????????????????~

        // ??????????????? ????????? ??? ????????? ????????? ??????
        val storageReference: StorageReference = mFirebaseStorage.reference
            .child("userprofileImages")
            .child("uid/" + file.lastPathSegment)

        storageReference.putFile(imageUri!!).addOnCompleteListener {

            val userRef = mFirebaseAuth.currentUser?.let {
                mFirebaseStoreDatabase.collection("users").document(
                    it.uid)
            }
            userRef?.get()?.addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject<CustomUserInfo>()
                val userprofileImageRef = mFirebaseStoreDatabase.collection("users").document("${user?.uid}")

                userprofileImageRef
                    .update("profileImage", file.lastPathSegment)
                    .addOnSuccessListener { Log.d(this.toString(), "DocumentSnapshot successfully updated!") }
                    .addOnFailureListener { e -> Log.w(this.toString(), "Error updating document", e) }
            }

        }
    }

    private fun logOut() {
        mFirebaseAuth.signOut()
        LoginManager.getInstance().logOut()
        context?.let { MySharedPreferences.setAuto(it, false) }

        activity?.let {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

        LogoutButton.visibility = View.INVISIBLE

        LoginButton.visibility = View.VISIBLE


        val drawable : Drawable = resources.getDrawable(R.drawable.icon_nurse)

        UserProfile_image.setImageResource(R.drawable.icon_nurse)
        TextView_username.text = "Nickname"
        TextView_Firstname.text = "Firstname"
        TextView_Lastname.text = "Lastname"
        TextView_location.text = "location"
        TextView_age.text = "age"
        TextView_Nurse.text = "Nurse"
        iv_Sex.visibility = View.INVISIBLE

        Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show()
    }

    private fun setInfo() {
        val userRef = mFirebaseAuth.currentUser?.let {
            mFirebaseStoreDatabase.collection("users").document(
                it.uid)
        }
        try
        {
            userRef?.get()?.addOnSuccessListener { documentSnapshot ->
            val user = documentSnapshot.toObject<CustomUserInfo>()
                if (user != null) {
                    if (user.profileImage != "null") {
                        val storageRef: StorageReference = mFirebaseStorage.reference
                        storageRef.child("userprofileImages/uid/${user.profileImage}").downloadUrl
                            .addOnCompleteListener(OnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    activity?.let {
                                        Glide.with(it)
                                            .load(task.result)
                                            .into(UserProfile_image)
                                    }
                                } else {
                                    Log.e(this.toString(), "loading userprofileImage error")
                                }

                            }).addOnFailureListener {
                                Log.w(this.toString(), "userprofileImage Loading Failed")
                            }
                    }
                }
                TextView_username.text = user?.userNickname
                TextView_Firstname.text = user?.userFirstname
                TextView_Lastname.text = user?.userLastname
                TextView_location.text = user?.location
                TextView_age.text = user?.age.toString()
                TextView_Nurse.text = user?.nurse.toString()
                iv_Sex.visibility = View.VISIBLE
                if (user?.sex == true)
                    iv_Sex.setImageResource(R.drawable.icon_male_color)
                else
                    iv_Sex.setImageResource(R.drawable.icon_female_color)
            }
        } catch (e : Exception) {

        }

        customProgressDialog!!.dismiss()
    }

    private fun onPermissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission_list, 0)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //when (requestCode) {
        //    0 -> {
        //        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
        //            gotoAlbum()
        //        } else {

        //        }
        //    }
        //}

        gotoAlbum()
    }
}