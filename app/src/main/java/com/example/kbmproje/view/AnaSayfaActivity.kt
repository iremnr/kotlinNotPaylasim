package com.example.kbmproje.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kbmproje.model.Post
import com.example.kbmproje.R
import com.example.kbmproje.adapter.HaberRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_ana_sayfa.*


class AnaSayfaActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore
    private lateinit var recyclerViewAdapter : HaberRecyclerAdapter


    var postListesi = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ana_sayfa)


        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        verileriAl()

        var layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerViewAdapter = HaberRecyclerAdapter(postListesi)
        recyclerView.adapter = recyclerViewAdapter
    }



    fun verileriAl() {
        database.collection("Post").orderBy("tarih",Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
            } else {
                if (snapshot != null) {
                    if(!snapshot.isEmpty) {
                        val documents = snapshot.documents

                        postListesi.clear()

                        for (document in documents ) {
                            val kullaniciEmail = document.get("kullaniciemail") as String
                            val kullaniciYorum = document.get("kullaniciyorum") as String
                            val gorselUrl = document.get("gorselurl") as String

                            val indirilenPost = Post(kullaniciEmail,kullaniciYorum,gorselUrl)
                            postListesi.add(indirilenPost)
                        }

                        recyclerViewAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = getMenuInflater()
        menuInflater.inflate(R.menu.opsiyon_menusu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.fotogarf_paylas) {
            //foto payla??ma aktivitesine gidilecek
            val intent = Intent(this, FotoPaylasmaActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.cikis_yap){
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

}