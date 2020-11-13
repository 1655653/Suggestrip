package com.example.suggestripapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_account_mng2.*
import java.util.*

class AccountMngActivity : AppCompatActivity() {
    var uri_propic: Uri = "".toUri()
    var uri_propic_modified = false
    companion object {
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_mng2)

        val user = Firebase.auth.currentUser

        //settaggi base
        et_nick.setText(user?.displayName, TextView.BufferType.EDITABLE);
        et_email.setText(user?.email, TextView.BufferType.EDITABLE);
        iv_user.setImageURI(user?.photoUrl)
        Picasso.get().load(user?.photoUrl).fit().centerCrop().into(iv_user)

        //accesso a galleria quando tap on image
        iv_user.setOnClickListener {
            //check runtime permission
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                //check if permission denied
                if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_CODE)
                else{ //permission granted
                    pickImageFromGallery()
                }
            }
            else{ //permission granted
                pickImageFromGallery()
            }
        }
        val porchetta = et_password.text.toString()
        et_password.setOnFocusChangeListener { view, b -> if(b) et_password.setText("") }

        //apply changes
        btn_apply_changes.setOnClickListener {
            if (et_nick.text.isEmpty() || et_email.text.isEmpty() || et_password.text.isEmpty()){
                val alertDialog = AlertDialog.Builder(this@AccountMngActivity)
                alertDialog.setTitle("Alert")
                alertDialog.setMessage("Can not proceed, an empty field has been set")
                alertDialog.setNegativeButton(android.R.string.no) { dialog, which ->
                    Toast.makeText(applicationContext, "EDITS CANCELED", Toast.LENGTH_SHORT).show()
                    et_nick.setText(user?.displayName, TextView.BufferType.EDITABLE);
                    et_email.setText(user?.email, TextView.BufferType.EDITABLE);
                }
                alertDialog.show()
                et_password.setText(porchetta)
            }
            else if(uri_propic_modified == true || et_nick.text.toString() != user?.displayName || et_email.text.toString() != user?.email || et_password.text.toString() != porchetta){
                val alertDialog = AlertDialog.Builder(this@AccountMngActivity)
                alertDialog.setTitle("Alert")
                alertDialog.setMessage("You sure you want to apply changes ?")
                alertDialog.setPositiveButton(android.R.string.yes) { dialog, which ->
                    val profileUpdates = userProfileChangeRequest {
                        displayName = et_nick.text.toString()
                        user!!.updateEmail(et_email.text.toString())
                        photoUri = uri_propic
                    }

                    if(et_password.text.toString() != porchetta){
                        val newPassword = et_password.text.toString()
                        user!!.updatePassword(newPassword)
                    }

                    user!!.updateProfile(profileUpdates).addOnCompleteListener { task ->
                        if (task.isSuccessful) {Toast.makeText(
                            applicationContext,
                            "EDITS CORRECTLY CHANGED",
                            Toast.LENGTH_SHORT
                        ).show()
                            et_nick.setText(user?.displayName, TextView.BufferType.EDITABLE);
                            et_email.setText(user?.email, TextView.BufferType.EDITABLE);
                            Picasso.get().load(user?.photoUrl).fit().centerCrop().into(iv_user)
                            uri_propic_modified = false
                            Log.d("aoporcodio", user.photoUrl.toString())
                        }
                    }



                }
                alertDialog.setNegativeButton(android.R.string.no) { dialog, which ->
                    Toast.makeText(applicationContext, "EDITS CANCELED", Toast.LENGTH_SHORT).show()
                    et_nick.setText(user?.displayName, TextView.BufferType.EDITABLE)
                    et_email.setText(user?.email, TextView.BufferType.EDITABLE);

                }
                alertDialog.show()
            }
        }

        //log_out
        btn_sign_out.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this@AccountMngActivity)
            alertDialog.setTitle("Alert")
            alertDialog.setMessage("You sure you want to log out ?")
            alertDialog.setPositiveButton(android.R.string.yes) { dialog, which ->
                AuthUI.getInstance().signOut(this@AccountMngActivity)
                    .addOnCompleteListener {
                        showSignOutOPtions()
                        val intent = Intent(this, MainActivity::class.java).apply {}
                        startActivity(intent)
                    }
                    .addOnFailureListener{
                            e -> Toast.makeText(this@AccountMngActivity, e.message, Toast.LENGTH_LONG ).show()
                    }
            }
            alertDialog.setNegativeButton(android.R.string.no) { dialog, which ->
            }
            alertDialog.show()

        }
    }

    private fun showSignOutOPtions() {
        var providers = Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()
        )
        startActivityForResult(AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false)
            .setTheme(R.style.AppTheme)
            .setLogo(R.drawable.logo)
            .build(), 666)
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            Picasso.get().load(data?.data).fit().centerCrop().into(iv_user)
            uri_propic = data?.data!!
            uri_propic_modified = true
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}