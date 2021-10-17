package com.example.noteremote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private final List<Note> notes;
    private final Context context;

    public RecyclerViewAdapter(List<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }

    @androidx.annotation.NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @androidx.annotation.NonNull ViewGroup viewGroup, int i) {
       View v = LayoutInflater.from(viewGroup.getContext()).
               inflate(R.layout.layout_row, viewGroup, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @androidx.annotation.NonNull MyViewHolder myViewHolder, int i) {
        Note note = notes.get(i);
        myViewHolder.title.setText(note.getTitle());
        myViewHolder.text.setText(note.getNote());
        myViewHolder.date.setText(note.getDate());
        myViewHolder.touchLayout.setOnClickListener(v -> {
            MainActivity.sendObject(context, i);
            notifyItemChanged(myViewHolder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private final TextView title;
        private final TextView text;
        private final TextView date;
        private final RelativeLayout touchLayout;

        public MyViewHolder(@NonNull @androidx.annotation.NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            text = itemView.findViewById(R.id.text);
            date = itemView.findViewById(R.id.date);
            touchLayout = itemView.findViewById(R.id.touch_layout);
        }
    }

}
