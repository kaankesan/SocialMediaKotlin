package com.kaankesan.instagramclonekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.kaankesan.instagramclonekotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()


        val currentUser = auth.currentUser


        if(currentUser != null){

            val intent = Intent(this@MainActivity,FeedActivity::class.java)
            startActivity(intent)
            finish()

        }


        binding.SignIn.setOnClickListener {

            val mail = binding.mail.text.toString()
            val password = binding.Password.text.toString()

            if(mail.isEmpty() || password.isEmpty()){
                Toast.makeText(this@MainActivity,"Enter Mail And Password",Toast.LENGTH_SHORT).show()

            }else{

                auth.signInWithEmailAndPassword(mail,password).addOnSuccessListener {

                    val intent = Intent(this@MainActivity,FeedActivity::class.java)
                    startActivity(intent)
                    finish()

                }.addOnFailureListener{

                    Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_SHORT).show()

                }
            }

        }

        binding.signUp.setOnClickListener {

            val mail = binding.mail.text.toString()
            val password = binding.Password.text.toString()

            if(mail.isEmpty() || password.isEmpty()){
                Toast.makeText(this@MainActivity,"Enter Mail And Password",Toast.LENGTH_SHORT).show()

            }else{

                auth.createUserWithEmailAndPassword(mail,password).addOnSuccessListener {

                    val intent = Intent(this@MainActivity,FeedActivity::class.java)
                    startActivity(intent)
                    finish()

                }.addOnFailureListener {

                    Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_SHORT).show()

                }

            }

        }

    }

}