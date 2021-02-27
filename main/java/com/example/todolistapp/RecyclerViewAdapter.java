package com.example.todolistapp;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.NamedNodeMap;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.PostHolder> {

    public interface OnItemClickListener {
        public void onItemClicked(int position);
    }

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }

    ArrayList<NoteModel> noteModels;
    public SQLiteDatabase database;

    public RecyclerViewAdapter(ArrayList<NoteModel> noteModels,SQLiteDatabase db) {
        this.noteModels = noteModels;
        this.database = db;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_row,parent,false);


        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        final NoteModel note = noteModels.get(position);
        holder.not.setText(note.getNoteText());
        if (note.getIsFavorite() == 1){
            holder.star.setImageResource(R.drawable.ic_baseline_filledstar_24);
        }
        else{
            holder.star.setImageResource(R.drawable.ic_baseline_emptystar_outline_24);
        }
        if (note.getIsDone() == 1){
            holder.tick.setImageResource(R.drawable.done_image);
        }
        else{
            holder.tick.setImageResource(R.drawable.not_done_image);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (note.getIsDone() == 1){
                    holder.tick.setImageResource(R.drawable.done_image);
                    note.setIsDone(0);
                }
                else {
                    holder.tick.setImageResource(R.drawable.not_done_image);
                    note.setIsDone(1);
                }

                database.execSQL("UPDATE notes_table SET done = '"+note.getIsDone()+"' WHERE id = '"+note.getNoteId()+"'");
                notifyDataSetChanged();

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return noteModels.size();
    }

    class PostHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        ImageView star;
        ImageView tick;
        TextView not;
        CardView cardView;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            star = itemView.findViewById(R.id.imageview_star);
            tick = itemView.findViewById(R.id.imageview_tick);
            not = itemView.findViewById(R.id.textView_not);
            cardView = itemView.findViewById(R.id.cdView);
            cardView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Ne yapmak istiyorsunuz?");
            menu.add(this.getAdapterPosition(),121,getAdapterPosition(),"Notu Favorilere Ekle/Çıkar.");
            menu.add(this.getAdapterPosition(),122,getAdapterPosition(),"Notu Sil.");
        }
    }



}