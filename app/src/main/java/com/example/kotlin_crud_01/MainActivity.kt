package com.example.kotlin_crud_01

import MainAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import androidx.appcompat.widget.SearchView
import com.example.kotlin_crud_01.R

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mainAdapter: MainAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var floatingActionButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.rv)
        recyclerView.layoutManager = LinearLayoutManager(this)

        databaseReference = FirebaseDatabase.getInstance().reference.child("spareparts")

        val options = FirebaseRecyclerOptions.Builder<MainModel>()
            .setQuery(databaseReference, MainModel::class.java)
            .build()

        mainAdapter = MainAdapter(options)
        recyclerView.adapter = mainAdapter

        floatingActionButton = findViewById(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener {
            startActivity(Intent(applicationContext, AddActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        mainAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mainAdapter.stopListening()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val item: MenuItem = menu.findItem(R.id.search)
        val searchView: SearchView = item.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                txtSearch(newText ?: "")
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun txtSearch(str: String) {
        val newOptions = FirebaseRecyclerOptions.Builder<MainModel>()
            .setQuery(databaseReference.orderByChild("name").startAt(str).endAt(str + "~"), MainModel::class.java)
            .build()


        Log.d("FirebaseQuery", "Query: ${databaseReference.orderByChild("name").startAt(str).endAt(str + "~")}")

        mainAdapter.updateOptions(newOptions)
        recyclerView.adapter = mainAdapter
    }
}