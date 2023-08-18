package com.a.autoreminderapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a.autoreminderapp.R
import com.a.autoreminderapp.data.entities.Part
import com.a.autoreminderapp.ui.adapters.PartClickInterface
import com.a.autoreminderapp.ui.adapters.PartLongClickInterface
import com.a.autoreminderapp.ui.adapters.PartRVAdapter
import com.a.autoreminderapp.ui.viewmodels.PartViewModelFactory
import com.a.autoreminderapp.ui.viewmodels.PartViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

// активити списка запчастей для категории
class CategoryPartsActivity : AppCompatActivity(), PartClickInterface, PartLongClickInterface {

    lateinit var rvParts: RecyclerView
    lateinit var fabAdd: FloatingActionButton

    var categoryId = -1

    val vmPart: PartViewModel by viewModels {
        PartViewModelFactory(application, categoryId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_parts)


        val categoryName = intent.getStringExtra("name")

        title = categoryName

        categoryId = intent.getIntExtra("id", -1)

        rvParts = findViewById(R.id.rvParts)
        fabAdd = findViewById(R.id.fabAddPart)

        rvParts.layoutManager = LinearLayoutManager(application)
        val partRVAdapter = PartRVAdapter(application, this, this)

        rvParts.adapter = partRVAdapter

        vmPart.allParts.observe(this, Observer { list ->
            list?.let {
                partRVAdapter.updateList(it)
            }
        } )

        fabAdd.setOnClickListener{
            val intent = Intent(this@CategoryPartsActivity, AddEditPartActivity::class.java)
            intent.putExtra("category", categoryId)
            intent.putExtra("categoryName", title)
            startActivity(intent)
            finish()
        }
    }

    override fun onClick(part: Part) {
        val intent = Intent(this@CategoryPartsActivity, AddEditPartActivity::class.java)
        intent.putExtra("action", "edit")
        intent.putExtra("id", part.id)
        intent.putExtra("category", categoryId)
        intent.putExtra("categoryName", title)
        intent.putExtra("name", part.name)
        intent.putExtra("maxServiceLife", part.maxServiceLife)
        intent.putExtra("currentServiceLife", part.currentServiceLife)
        intent.putExtra("brand", part.brand)
        intent.putExtra("description", part.description)
        startActivity(intent)
    }

    override fun onLongClick(part: Part) {
        vmPart.deletePart(part)
        Toast.makeText(this, "Деталь '${part.name}' удалена", Toast.LENGTH_LONG).show()
    }
}