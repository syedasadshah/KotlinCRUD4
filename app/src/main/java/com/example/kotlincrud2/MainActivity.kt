package com.example.kotlincrud2

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.row.view.*

class MainActivity : AppCompatActivity() {

    var listNotes =ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //load from db
        LoadQuery("%")
    }

    private fun LoadQuery(title: String) {
        var dbManager = DbManager(this)
        val projections = arrayOf("ID","Title","Description")
        val selectionArgs = arrayOf("Title")
        val cursor = dbManager.Query(projections,"Title like ?", selectionArgs, "Title")
        listNotes.clear()
        if (cursor.moveToFirst()){
                do {
                    val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                    val Title = cursor.getString(cursor.getColumnIndex("Title"))
                    val Description = cursor.getString(
                        cursor.getColumnIndex("Description")
                    )
                    listNotes.add(Note(ID, Title, Description))
                } while (cursor.moveToNext())
            }

        //adapter
        var myNotesAdapter = MyNotesAdapter(this, listNotes)


    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        //forsearchmenu
        val sv: SearchView = menu!!.findItem(R.id.search_bar).actionView as SearchView
        val sm =getSystemService(Context.SEARCH_SERVICE) as SearchManager
    } */

    inner class MyNotesAdapter : BaseAdapter {

        var listNotesAdapter = ArrayList<Note>()
        var context: Context? = null

        constructor( context: Context?, listNotesArray: ArrayList<Note>) : super() {
            this.listNotesAdapter = listNotesArray
            this.context = context
        }


        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//inflate layout row.xml here
            var myView = layoutInflater.inflate(R.layout.row, null)
            val myNote = listNotesAdapter[position]

            myView.title_tv.text =myNote.title
            myView.dis_tv.text = myNote.des

            //delete button clicked
            myView.deleteButton.setOnClickListener {
                var dbManager = DbManager(this.context!!)
                val selectionArgs = arrayOf(myNote.title.toString())

                dbManager.delete("ID=?", selectionArgs)
                LoadQuery("%")
            }
            //update btn click
            myView.editButton.setOnClickListener {
                goToUpdateFunc(myNote)
            }

            return myView
       }

        override fun getItem(position: Int): Any {
          return listNotesAdapter[position]

        }

        override fun getItemId(position: Int): Long {
          return position.toLong()
        }

        override fun getCount(): Int {
            return listNotesAdapter.size
        }

    }

    private fun goToUpdateFunc(myNote: Note) {
        var intent = Intent(this, AddActivity::class.java)
        intent.putExtra("ID", myNote.id)
        intent.putExtra("Title", myNote.title)
        intent.putExtra("des", myNote.des )
        startActivity(intent)

    }

}


