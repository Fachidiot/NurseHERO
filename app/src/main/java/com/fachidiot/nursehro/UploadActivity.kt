package com.fachidiot.nursehro

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fachidiot.nursehro.Class.ImageDTO
import com.fachidiot.nursehro.MainFragment.FragmentMainProfile
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_upload.*
import java.io.File


class UploadActivity : AppCompatActivity() {

    private lateinit var mFirebaseAuth : FirebaseAuth
    private lateinit var mFirebaseStorage : FirebaseStorage
    private lateinit var mFirebaseDatabase : FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseStorage = FirebaseStorage.getInstance()
        mFirebaseDatabase = FirebaseDatabase.getInstance()

    }

    private fun uploadImg(uri: String) {
        try {
            // Create a storage reference from our app
            val storageRef: StorageReference = mFirebaseStorage.reference
            val file: Uri = Uri.fromFile(File(uri))
            val riversRef = storageRef.child("images/" + file.lastPathSegment)
            val uploadTask = riversRef.putFile(file)
            val urlTask: Task<Uri?> = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }

                // Continue with the task to get the download URL
                riversRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@UploadActivity, "업로드 성공", Toast.LENGTH_SHORT).show()

                    //파이어베이스에 데이터베이스 업로드
                    val downloadUrl: Uri? = task.result
                    val imageDTO = ImageDTO(
                        downloadUrl.toString(),
                        etTitle.text.toString(),
                        etDesc.text.toString(),
                        mFirebaseAuth.currentUser!!.uid,
                        mFirebaseAuth.currentUser!!.email.toString()
                    )

                    //image 라는 테이블에 json 형태로 담긴다.
                    //database.getReference().child("Profile").setValue(imageDTO);
                    //  .set  :  데이터를 넣는다  .push()  :  데이터가 쌓인다.
                    mFirebaseDatabase.reference.child("Profile").push().setValue(imageDTO)
                    val intent = Intent(applicationContext, FragmentMainProfile::class.java)
                    startActivity(intent)
                } else {
                    // Handle failures
                    // ...
                }
            }
        } catch (e: NullPointerException) {
            Toast.makeText(this@UploadActivity, "이미지 선택 안함", Toast.LENGTH_SHORT).show()
        }
    }

}