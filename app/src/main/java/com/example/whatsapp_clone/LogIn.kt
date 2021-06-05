package com.example.whatsapp_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class LogIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        if(FirebaseAuth.getInstance().currentUser!=null){
            startActivity(Intent(this , MainActivity::class.java))
        }
    }
}