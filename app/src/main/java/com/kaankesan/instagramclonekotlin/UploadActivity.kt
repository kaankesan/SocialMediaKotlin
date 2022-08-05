package com.kaankesan.instagramclonekotlin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kaankesan.instagramclonekotlin.databinding.ActivityUploadBinding
import java.util.*

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private lateinit var launcher : ActivityResultLauncher<Intent>
    private lateinit var permission : ActivityResultLauncher<String>
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore
    private lateinit var storage : FirebaseStorage
    private var selectedImage : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        register()

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        binding.imageView.setOnClickListener {

            if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Snackbar.make(view,"Permission Needed",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){

                        //request
                        permission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                    }.show()

                }else{
                    //Request
                    permission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }

            }else{
                val intentToGalley = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                launcher.launch(intentToGalley)
            }

        }

        binding.saveButton.setOnClickListener {

            val uuid = UUID.randomUUID()
            val imageName = "$uuid.jpg"

            val reference = storage.reference
            val imageReference = reference.child("images").child(imageName)

            if(selectedImage != null){
                imageReference.putFile(selectedImage!!).addOnFailureListener{

                    Toast.makeText(this@UploadActivity,it.localizedMessage,Toast.LENGTH_SHORT).show()

                }.addOnSuccessListener{

                    val uploadPictureReference = storage.reference.child("images").child(imageName)
                    uploadPictureReference.downloadUrl.addOnSuccessListener { result ->

                        val downloadUrl = result.toString()

                        if(auth.currentUser != null){
                            val postMap = hashMapOf<String,Any>()
                            postMap["downloadUrl"] = downloadUrl
                            postMap["Email"] = auth.currentUser!!.email!!
                            postMap["Comment"] = binding.Comment.text.toString()
                            postMap["Date"] = Timestamp.now()

                            firestore.collection("Posts").add(postMap).addOnSuccessListener {

                                val intent = Intent(this,FeedActivity::class.java)
                                startActivity(intent)
                                finish()

                            }.addOnFailureListener{
                                Toast.makeText(this@UploadActivity,it.localizedMessage,Toast.LENGTH_SHORT).show()
                            }

                        }

                    }

                }
            }

        }

    }

    private fun register(){

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                val intentFromResult = result.data

                if(intentFromResult != null){
                  selectedImage = intentFromResult.data
                    selectedImage?.let {
                        binding.imageView.setImageURI(it)
                    }
                }

            }
        }

        permission = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->

            if(result){
                val intentToGalley = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                launcher.launch(intentToGalley)

            }else{
                Toast.makeText(this@UploadActivity,"Permission Denied",Toast.LENGTH_SHORT).show()
            }

        }

    }

}