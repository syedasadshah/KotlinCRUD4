package com.example.kotlincrud2

import android.content.ContentValues
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add.*
import java.lang.Exception

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AddActivity : AppCompatActivity() {

    val dbTable = "Notes"
    var id = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        try {
            val bundle:Bundle = intent.extras
            id = bundle.getInt("ID", 0)
            if (id != 0){
                title_et.setText(bundle.getString("Title"))
                desc_et.setText(bundle.getString("des"))
            }
        }catch (ex:Exception){

        }
    }

    fun addFunc(@Suppress("UNUSED_PARAMETER")view: View){
        var dbManager = DbManager(this)
        var values = ContentValues()
        values.put("Title", title_et.text.toString())
        values.put("Description", desc_et.text.toString())

        if (id == 0){
            val id = dbManager.insert(values)
            if (id > 0){
                Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()
                finish()
            }
            else{
                Toast.makeText(this, "Error Adding note", Toast.LENGTH_SHORT).show()

            }
        }
        else {
            var selectionArgs = arrayOf(id.toString())
            val ID = dbManager.update(values, "ID=?", selectionArgs)

            if (ID > 0) {
                Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error Adding note", Toast.LENGTH_SHORT).show()

            }
        }
    }


}
