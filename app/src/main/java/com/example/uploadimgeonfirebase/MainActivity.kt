package com.example.uploadimgeonfirebase

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class MainActivity : AppCompatActivity() {

    lateinit var choose_img: Button
    lateinit var upload_img: Button
    lateinit var image_view: ImageView
    var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        choose_img = findViewById(R.id.choose_image)
        upload_img = findViewById(R.id.upload_image)
        image_view = findViewById(R.id.image_view)

        choose_img.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent,"Choose Image to Upload"),0
            )
        }

        upload_img.setOnClickListener {
            if (fileUri != null){
                uploadImage()
            }else{
                Toast.makeText(applicationContext,"Please Select Image to Upload",
                    Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == RESULT_OK && data != null && data.data != null){
            fileUri = data.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver,fileUri)
                image_view.setImageBitmap(bitmap)

            }catch (e: Exception){
                Log.e("Exception","Error: "+e)
            }
        }
    }

    fun uploadImage(){
        if (fileUri!= null){
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading Image...")
            progressDialog.setMessage("Processing...")
            progressDialog.show()

            val ref: StorageReference = FirebaseStorage.getInstance().getReference()
                .child(UUID.randomUUID().toString())
            ref.putFile(fileUri!!).addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext,"File Uploaded Successfully", Toast.LENGTH_LONG).show()
            }.addOnFailureListener{
                progressDialog.dismiss()
                Toast.makeText(applicationContext,"File Upload Failed...", Toast.LENGTH_LONG).show()
            }
        }
    }
}