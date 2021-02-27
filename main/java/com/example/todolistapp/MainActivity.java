package com.example.todolistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    SQLiteDatabase database;
    ArrayList<NoteModel> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        notes = new ArrayList<>();
        database = this.openOrCreateDatabase("todoapp",MODE_PRIVATE,null);
        database.execSQL("CREATE TABLE IF NOT EXISTS notes_table (id INTEGER PRIMARY KEY,text VARCHAR,favorite INT DEFAULT 0,done INT DEFAULT 0)");



        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editText = findViewById(R.id.input_editText);
                String input = editText.getText().toString();

                if(!input.equals("")){
                    System.out.println(editText.getText());
                    database.execSQL("INSERT INTO notes_table (text) VALUES ('"+input+"')");
                    editText.setText("");

                    getNotes();
                    recyclerViewAdapter = new RecyclerViewAdapter(notes,database);
                    recyclerView.setAdapter(recyclerViewAdapter);

                    displayMessage("Not başarıyla eklendi.");
                }
                else{
                    displayMessage("Not metni boş olmamalıdır.");
                }



            }
        });




        recyclerView = findViewById(R.id.recyclerview1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        getNotes();

        recyclerViewAdapter = new RecyclerViewAdapter(notes,database);
        recyclerView.setAdapter(recyclerViewAdapter);


    }

    private void getNotes(){

        notes.clear();

        Cursor cursor = database.rawQuery("SELECT * FROM notes_table ORDER BY favorite DESC",null);


        int idIx = cursor.getColumnIndex("id");
        int textIx = cursor.getColumnIndex("text");
        int favIx = cursor.getColumnIndex("favorite");
        int doneIx = cursor.getColumnIndex("done");


        while(cursor.moveToNext()){

            int id = cursor.getInt(idIx);
            String text = cursor.getString(textIx);
            int favorite = cursor.getInt(favIx);
            int done = cursor.getInt(doneIx);

            NoteModel note = new NoteModel(id,text,favorite,done);
            notes.add(note);

        }

        cursor.close();

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        int clickedItemPosition = item.getOrder();
        System.out.println(clickedItemPosition);


        switch (item.getItemId()){

            case 121:
                notes.get(clickedItemPosition).setIsFavorite((notes.get(clickedItemPosition).getIsFavorite()+1)%2);
                database.execSQL("UPDATE notes_table SET favorite = '"+notes.get(clickedItemPosition).getIsFavorite()+"' WHERE id = '"+notes.get(clickedItemPosition).getNoteId()+"'");
                recyclerViewAdapter.notifyDataSetChanged();

                if (notes.get(clickedItemPosition).getIsFavorite() == 1){
                    displayMessage("Not favorilere eklendi");
                }
                else{
                    displayMessage("Not favorilerden çıkartıldı.");
                }

                getNotes();
                recyclerViewAdapter = new RecyclerViewAdapter(notes,database);
                recyclerView.setAdapter(recyclerViewAdapter);

                break;
            case 122:

                database.execSQL("DELETE FROM notes_table WHERE id = '"+notes.get(clickedItemPosition).getNoteId()+"'");
                notes.remove(clickedItemPosition);
                recyclerView.removeViewAt(clickedItemPosition);
                recyclerViewAdapter.notifyItemRemoved(clickedItemPosition);
                recyclerViewAdapter.notifyItemRangeChanged(clickedItemPosition,notes.size());
                displayMessage("Not silindi.");

                break;


        }

        return super.onContextItemSelected(item);
    }

    private void displayMessage(String message){

        //Snackbar.make(findViewById(R.id.rootView),message,Snackbar.LENGTH_SHORT).show();
        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();

    }
}