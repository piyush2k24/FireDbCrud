package com.piyush2k24.firedbcrud

import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever.BitmapParams
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.piyush2k24.firedbcrud.Holder.holder
import com.piyush2k24.firedbcrud.databinding.ActivityUserOperationBinding
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.Base64

class UserOperation : AppCompatActivity() {
    private lateinit var binding: ActivityUserOperationBinding
    private lateinit var databaseReference: DatabaseReference
    var sImage:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUserOperationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseReference=FirebaseDatabase.getInstance().getReference("Users")
        caller()
    }

    private fun caller(){
        binding.UserImage.setOnClickListener {
            val fileIntent=Intent(Intent.ACTION_GET_CONTENT)
            fileIntent.setType("image/*")
            ActivityResultLauncher.launch(fileIntent)
        }
        binding.UserAdd.setOnClickListener {
            CreateNewUser()
        }
    }

    private val ActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
                if(result.resultCode==AppCompatActivity.RESULT_OK){
                    val uri= result.data!!.data
                    try{
                        val inputStream=applicationContext?.contentResolver?.openInputStream(uri!!)
                        val bitmap=BitmapFactory.decodeStream(inputStream)
                        val stream=ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
                        val bytes=stream.toByteArray()
                        sImage= android.util.Base64.encodeToString(bytes,android.util.Base64.DEFAULT)
                        binding.UserImage.setImageBitmap(bitmap)
                        inputStream!!.close()
                        showToast("Image Selected")
                    }catch (e:Exception){
                        showToast(e.message.toString())
                    }
                }
    }

    private fun CreateNewUser(){
        val Uid=databaseReference.push().key!!

        val Users=holder(
            sImage,
            binding.UserName.text.toString(),
            binding.UserEmail.text.toString(),
            binding.UserDesignation.text.toString()
        )

        databaseReference.child(Uid).setValue(Users)
            .addOnCompleteListener {
                showToast("User Add ❤️")

                sImage=""
                binding.UserImage.setImageBitmap(null)
                binding.UserName.text?.clear()
                binding.UserEmail.text?.clear()
                binding.UserDesignation.text?.clear()
            }
            .addOnFailureListener {
                showToast(it.message.toString())
            }
    }
    private fun showToast(str:String){
        Toast.makeText(applicationContext,str,Toast.LENGTH_SHORT).show()
    }
}