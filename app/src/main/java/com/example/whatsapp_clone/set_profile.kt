package com.example.whatsapp_clone

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class set_profile : AppCompatActivity() {

    private var userId = FirebaseAuth.getInstance().currentUser!!.uid
    private lateinit var name : EditText
    private lateinit var image:ImageView
    val SELECT_PICTURE = 100
    private lateinit var imageUri:Uri
    private var downloadUri:Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_profile)

        name = findViewById(R.id.profile_name)
        image = findViewById(R.id.profile_image)

        image.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                openGallery()
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
            if(resultCode == SELECT_PICTURE){
                imageUri = data?.data!!
                if(null != imageUri){
                    image.setImageURI(imageUri)
                }
            }
        }
    }
}