package com.example.kbmproje.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.kbmproje.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        //giris yapan kullanıcının hatırlanmasını,sisteme tekrar tekrar girmesini engelleme
        val guncelKullanici=auth.currentUser
        if(guncelKullanici != null){
            val intent = Intent(this,AnaSayfaActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun girisYap(view: View) {
        auth.signInWithEmailAndPassword(emailText.text.toString(),passwordText.text.toString()).addOnCompleteListener { task->
            if (task.isSuccessful){
                val guncelKullanici = auth.currentUser?.email.toString()
                Toast.makeText(this, "Hoşgeldin: ${guncelKullanici}",Toast.LENGTH_LONG).show()
                val intent = Intent(this, AnaSayfaActivity::class.java )
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener{ exception ->
            Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }

    fun kayitOl(view: View) {
        val email = emailText.text.toString()
        val sifre = passwordText.text.toString()
        println(email)

        auth.createUserWithEmailAndPassword(email, sifre).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this, AnaSayfaActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }

    }
}