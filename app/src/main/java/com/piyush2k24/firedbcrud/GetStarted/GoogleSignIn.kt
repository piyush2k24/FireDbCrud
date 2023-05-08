package com.piyush2k24.firedbcrud.GetStarted

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.piyush2k24.firedbcrud.R
import com.piyush2k24.firedbcrud.Activities.UserOperation
import com.piyush2k24.firedbcrud.databinding.GoogleSigninBinding

class GoogleSignIn : AppCompatActivity() {
    private lateinit var binding: GoogleSigninBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=GoogleSigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()

        val googleSigninOption=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient=GoogleSignIn.getClient(this,googleSigninOption)

        Callers()
    }

    private fun Callers(){
        binding.GoogleSignIn.setOnClickListener {
            GSignIn()
        }
    }

    private fun GSignIn(){
        val signInIntent=googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
                if (result.resultCode==Activity.RESULT_OK){
                    val task=GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    handleResults(task)
                }
    }

    private fun handleResults(task : Task<GoogleSignInAccount>){
        if (task.isSuccessful){
            val account:GoogleSignInAccount?=task.result
            if(account!=null){
                updateUI(account)
            }
        }else{
            showToast(task.exception.toString())
        }
    }

    private fun updateUI(account: GoogleSignInAccount){
        val credential=GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    showToast("Successfully Sign In")
                    startActivity(Intent(this@GoogleSignIn, UserOperation::class.java))
                }else{
                    showToast(it.exception.toString())
                }
            }
    }
    private fun showToast(str : String){
        Toast.makeText(applicationContext,str,Toast.LENGTH_SHORT).show()
    }

}