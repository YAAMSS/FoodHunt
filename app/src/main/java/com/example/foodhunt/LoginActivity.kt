package com.example.foodhunt

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"
    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        mAuth = FirebaseAuth.getInstance()
        signInButton?.setOnClickListener {
            onViewClicked()
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        mAuthListener = FirebaseAuth.AuthStateListener {
            //val user = firebaseAuth.currentUser
            Log.d(TAG, "onAuthStateChanged:signed_in:" + user?.uid)
            // ...
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth?.currentUser
        currentUser?.let { updateUI(it) }
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth?.removeAuthStateListener(mAuthListener!!)
        }
    }


    private fun onViewClicked() {
        progressBar?.visibility = View.VISIBLE
        if (TextUtils.isEmpty(etUserEmail.text)) {
            Toast.makeText(applicationContext, "Please enter email...", Toast.LENGTH_LONG).show()
            progressBar.visibility = View.GONE
            return
        }

        if (TextUtils.isEmpty(etPassword.text)) {
            Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_LONG).show()
            progressBar.visibility = View.GONE
            return
        }
        mAuth?.createUserWithEmailAndPassword(
            etUserEmail.text.toString(),
            etPassword.text.toString()
        )
            ?.addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        this, "Authentication successful.",
                        Toast.LENGTH_SHORT
                    ).show()
                    val user = mAuth!!.currentUser
                    user?.let { it1 -> updateUI(it1) }
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", it.exception)
                    Toast.makeText(
                        this, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = View.GONE
                    updateUI(null)
                }
            }
    }
}

