package com.fachidiot.nursehro.RegisterFragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.content.CursorLoader
import com.fachidiot.nursehro.Class.CustomUserInfo
import com.fachidiot.nursehro.R
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_register.*
import java.io.File


class RegisterActivity : AppCompatActivity() {
    private val permission_list = arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var mFirebaseStorage: FirebaseStorage
    private lateinit var mFirebaseStoreDatabase: FirebaseFirestore

    private var imageUri: Uri? = null
    private var pathUri: String? = null
    private var tempFile: File? = null
    private var imageSet: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().reference
        mFirebaseStoreDatabase = Firebase.firestore
        mFirebaseStorage = FirebaseStorage.getInstance()

        button_back.setOnClickListener {
            finish()
        }

        imageView_AddPicture.setOnClickListener {
            onPermissionCheck()
        }

        RelativeLayout_Register.setOnClickListener {
            checkInfo()
        }

        TextInputEditText_Password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            override fun afterTextChanged(s: Editable) {
                if (TextInputEditText_Password.text.toString().length < 8) {
                    TextInputEditText_Password.error = "Password needs more 8 character"
                } else {
                    TextInputEditText_Password.error = null // null 은 에러 메시지를 지워주는 기능
                }
            }
        })

        TextInputEditText_Comfirm_Password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            override fun afterTextChanged(s: Editable) {
                if (TextInputEditText_Comfirm_Password.text.toString().length < 8) {
                    TextInputEditText_Comfirm_Password.error = "Password needs more 8 character"
                } else {
                    TextInputEditText_Comfirm_Password.error = null // null 은 에러 메시지를 지워주는 기능
                }
            }
        })

    }


    // 앨범 메소드
    private fun gotoAlbum() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val tag = "Album Message"
        if (resultCode != RESULT_OK) { // 코드가 틀릴경우
            Toast.makeText(this, "취소 되었습니다", Toast.LENGTH_SHORT).show()
            if (tempFile != null) {
                if (tempFile!!.exists()) {
                    if (tempFile!!.delete()) {
                        Log.e(tag, tempFile!!.absolutePath.toString() + " 삭제 성공")
                        tempFile = null
                    }
                }
            }
            return
        }
        when (requestCode) {
            1 -> {
                // 코드 일치
                // Uri
                if (data != null) {
                    imageUri = data.data
                }
                if (data != null) {
                    pathUri = getPath(data.data)
                }
                Log.d(this.toString(), "PICK_FROM_ALBUM photoUri : $imageUri")
                imageView_AddPicture.setImageURI(imageUri) // 이미지 띄움
                imageSet = true
            }
        }
    }

    // uri 절대경로 가져오기
    private fun getPath(uri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursorLoader = CursorLoader(
            this,
            uri!!, proj, null, null, null
        )
        val cursor = cursorLoader.loadInBackground()
        val index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(index)
    }

    private fun createEmail() {
        try {
            this.mFirebaseAuth.createUserWithEmailAndPassword(
                TextInputEditText_Email.text.toString(),
                TextInputEditText_Password.text.toString()
            ).addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    // 회원가입 성공시
                    // uid에 task, 선택된 사진을 file에 할당
                    val uid: String = it.result?.user?.uid.toString()

                    if (imageSet)
                    {
                        // path
                        val file: Uri = Uri.fromFile(File(pathUri))

                        // 스토리지에 방생성 후 선택한 이미지 넣음
                        val storageReference: StorageReference = mFirebaseStorage.reference
                            .child("userprofileImages")
                            .child("uid/" + file.lastPathSegment)

                        storageReference.putFile(imageUri!!).addOnCompleteListener {
                            //task -> val imageUrl: Task<Uri> = task.result?.storage?.downloadUrl as Task<Uri>
                            //while (!imageUrl.isComplete) {
                            //}

                            var location : String = ""
                            if(intent.hasExtra("dong")){
                                location = "${intent.getStringExtra(" region ")}/${intent.getStringExtra("sigungu")}/${intent.getStringExtra("dong")}"
                            } else{
                                location = "${intent.getStringExtra(" region ")}/${intent.getStringExtra(" sigungu ")}"
                            }
                            val geocoder = Geocoder(baseContext)
                            val addresses: List<Address> = geocoder.getFromLocationName(location, 3)
                            val address : Address = addresses[0]
                            val latLng = LatLng(address.latitude, address.longitude)

                            val userModel = CustomUserInfo(
                                intent.getBooleanExtra("nurse", false),
                                TextInputEditText_Nickname.text.toString(),
                                TextInputEditText_FirstName.text.toString(),
                                TextInputEditText_LastName.text.toString(),
                                file.lastPathSegment,
                                location,
                                latLng,
                                intent.getBooleanExtra("sex", false),
                                intent.getIntExtra("age", -99),
                                uid
                            )

                            // database에 저장
                            mFirebaseStoreDatabase.collection("users").document(uid)
                                .set(userModel)
                                .addOnSuccessListener { Log.d("FireStore", "Success") }
                                .addOnFailureListener { e -> Log.w("FireStore", "Failed", e) }


                        }
                    } else {

                        var location : String = ""
                        if(intent.hasExtra("dong")){
                            location = "${intent.getStringExtra(" region ")}/${intent.getStringExtra("sigungu")}/${intent.getStringExtra("dong")}"
                        } else{
                            location = "${intent.getStringExtra(" region ")}/${intent.getStringExtra(" sigungu ")}"
                        }
                        val geocoder = Geocoder(baseContext)
                        val addresses: List<Address> = geocoder.getFromLocationName(location, 3)
                        val address : Address = addresses[0]
                        val latLng = LatLng(address.latitude, address.longitude)

                        val userModel = CustomUserInfo(
                            intent.getBooleanExtra("nurse", false),
                            TextInputEditText_Nickname.text.toString(),
                            TextInputEditText_FirstName.text.toString(),
                            TextInputEditText_LastName.text.toString(),
                            "null",
                            location,
                            latLng,
                            intent.getBooleanExtra("sex", false),
                            intent.getIntExtra("age", -99),
                            uid
                        )

                        // database에 저장
                        mFirebaseStoreDatabase.collection("users").document(uid)
                            .set(userModel)
                            .addOnSuccessListener { Log.d("FireStore", "Success") }
                            .addOnFailureListener { e -> Log.w("FireStore", "Failed", e) }
                    }

                    Toast.makeText(this, "Authentication Success.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(
                        this,
                        RegisterSuccessActivity::class.java
                    )
                    intent.putExtra("email", TextInputEditText_Email.text.toString())
                    intent.putExtra("password", TextInputEditText_Password.text.toString())
                    startActivity(intent)
                } else {
                    // 회원가입 실패시 (가능성 : 중복계정이 있을시)
                    Toast.makeText(this, "이미 존재하는 이메일입니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        catch (e : Exception) {
            //Toast.makeText(baseContext, "이미 존재하는 이메일입니다.", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

    }

    private fun checkInfo() {
        if (TextInputEditText_FirstName.text.toString().isEmpty())
            TextInputEditText_FirstName.error = "Enter your first name"
        else if (TextInputEditText_LastName.text.toString().isEmpty())
            TextInputEditText_LastName.error = "Enter your last name"
        else if (TextInputEditText_Nickname.text.toString().isEmpty())
            TextInputEditText_Nickname.error = "Enter your nickname"
        else if (TextInputEditText_Email.text.toString().isEmpty())
            TextInputEditText_Email.error = "Enter your email"
        else if (!TextInputEditText_Email.text.toString().contains("@") || !TextInputEditText_Email.text.toString().contains(".com"))
            TextInputEditText_Email.error = "Check your email"
        else if (TextInputEditText_Password.text.toString().isEmpty())
            TextInputEditText_Password.error = "Enter your password"
        else if (TextInputEditText_Comfirm_Password.text.toString().isEmpty())
            TextInputEditText_Comfirm_Password.error = "Enter one more password"
        else if (TextInputEditText_Password.text.toString() != TextInputEditText_Comfirm_Password.text.toString()) {
            TextInputEditText_Password.error = "Your password is not correct"
            TextInputEditText_Comfirm_Password.error = "Your password is not correct"
        } else if (!CheckBox_Policy.isChecked)
            CheckBox_Policy.error = "Please ``check the checkbox"
        else
            createEmail()
    }

    private fun onPermissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission_list, 0)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when (requestCode) {
                0 -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                            gotoAlbum()
                        } else {
                            Toast.makeText(this, "내부 저장소 권한 허용없이 갤러리에 접근할 수 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        else
            gotoAlbum()
    }
}