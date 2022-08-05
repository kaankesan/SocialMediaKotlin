package com.kaankesan.instagramclonekotlin

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kaankesan.instagramclonekotlin.adapter.adapter
import com.kaankesan.instagramclonekotlin.databinding.ActivityFeedBinding
import com.kaankesan.instagramclonekotlin.model.Posts

class FeedActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFeedBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var arrayList : ArrayList<Posts>
    private lateinit var adapter : adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        arrayList = arrayListOf()

        getData()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = adapter(arrayList)
        binding.recyclerView.adapter = adapter



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.sign_out){
            auth.signOut()
            val intent = Intent(this@FeedActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        if(item.itemId == R.id.add_post){
            val intent = Intent(this@FeedActivity,UploadActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getData(){


        db.collection("Posts").orderBy("Date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->

            if(error != null){
                Toast.makeText(this@FeedActivity,error.localizedMessage,Toast.LENGTH_SHORT).show()
            }else{

                if(value != null){
                    if(!value.isEmpty){

                        for(documents in value.documents){
                            val comment = documents.get("Comment") as String
                            val downloadUrl = documents.get("downloadUrl") as String
                            val mail = documents.get("Email") as String

                            val post = Posts(mail,comment,downloadUrl)

                            arrayList.add(post)

                        }

                        adapter.notifyDataSetChanged()

                    }
                }

            }

        }

    }


}