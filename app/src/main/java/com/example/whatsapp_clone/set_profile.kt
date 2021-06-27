package com.example.whatsapp_clone

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
//import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlin.math.log

class set_profile : AppCompatActivity() {

    private var userId = FirebaseAuth.getInstance().currentUser!!.uid
    private lateinit var name : EditText
    private lateinit var image:ImageView
    private lateinit var button: Button
    val SELECT_PICTURE = 100
    private lateinit var imageUri:Uri
    private var downloadUri:Uri? = null
    private val _db = FirebaseDatabase.getInstance().getReference().child(userId)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_profile)

        name = findViewById(R.id.profile_name)
        image = findViewById(R.id.profile_image)
        button = findViewById(R.id.profile_next)
        image.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                openGallery()
            }
        })

        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val profile_name:String = name.text.toString()

                val ref = FirebaseStorage.getInstance().reference?.child(userId+"/profile_pic")
                val uploadTask = ref?.putFile(imageUri)
                val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot , Task<Uri>>{
                    if (!it.isSuccessful){
                        it.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation ref.downloadUrl
                }  ).addOnCompleteListener {
                    if (it.isSuccessful){
                        downloadUri = it.result!!
                    }
                }
                UserProfileChangeRequest.Builder().displayName = profile_name
                UserProfileChangeRequest.Builder().photoUri = downloadUri
                UserProfileChangeRequest.Builder().build()

                startActivity(Intent(this@set_profile , MainActivity::class.java))

            }
        })

    }

    fun openGallery(){
        intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent , "Select Picture") , SELECT_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            Log.d(TAG , " RESULT_OK")
            if(requestCode == SELECT_PICTURE){
                imageUri = data?.data!!
                Toast.makeText(this , imageUri.toString() , Toast.LENGTH_SHORT).show()
                if(null != imageUri){
                    image.setImageURI(imageUri)
                }
            }else{
                Log.d(TAG , "no SELECT Picture")
            }
        }
    }
}