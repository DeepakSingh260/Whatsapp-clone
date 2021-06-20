package com.example.whatsapp_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class LogIn : AppCompatActivity() {


    lateinit var  entreNo:EditText
    lateinit var entreOTP:EditText
    lateinit var enterOTPTEXT :TextView
    lateinit var verify_opt:Button
    lateinit var get_otp:Button
    var storedVerificationId:String = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    lateinit var auth :FirebaseAuth
    private lateinit var callbacks:PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        if(FirebaseAuth.getInstance().currentUser!=null){
            startActivity(Intent(this , MainActivity::class.java))
        }

        auth = FirebaseAuth.getInstance()

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks (){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(p0)
                startActivity(Intent(this@LogIn , MainActivity::class.java))
            }

            override fun onVerificationFailed(p0: FirebaseException) {

            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                storedVerificationId = p0
                resendToken = p1
            }
        }

        entreNo = findViewById(R.id.entre_no)
        entreOTP = findViewById(R.id.entre_otp)
        enterOTPTEXT  = findViewById(R.id.textview2)
        verify_opt = findViewById(R.id.verify_otp)
        get_otp = findViewById(R.id.get_otp)
        enterOTPTEXT.visibility = View.INVISIBLE
        verify_opt.visibility = View.INVISIBLE
        entreOTP.visibility = View.INVISIBLE

       get_otp.setOnClickListener(object : View.OnClickListener {
           override fun onClick(v: View?) {

               val phone  = entreNo.text

               enterOTPTEXT.visibility = View.INVISIBLE
               enterOTPTEXT.visibility = View.INVISIBLE
               enterOTPTEXT.visibility = View.INVISIBLE
               startPhoneVerification(phone.toString())
           }
       })

        verify_opt.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                val otp = entreOTP.text.toString()
                val credential = verifyPhoneNumberWithCode(storedVerificationId , otp)
                signInWithPhoneAuthCredential(credential)
            }
        })

    }

    private fun signInWithPhoneAuthCredential(p0: PhoneAuthCredential) {
        auth.signInWithCredential(p0).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = task.result?.user
                startActivity(Intent(this , ))
            }
        }
    }


    private fun startPhoneVerification(phonenumber:String){
        val options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(phonenumber).setTimeout(60L , TimeUnit.SECONDS).setActivity(this).build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private fun verifyPhoneNumberWithCode(verificationId:String? , code:String): PhoneAuthCredential {
        return PhoneAuthProvider.getCredential(verificationId!! , code)
    }


}