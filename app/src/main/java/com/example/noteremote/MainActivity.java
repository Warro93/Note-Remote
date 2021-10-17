package com.example.noteremote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import androidx.room.Room;

import com.example.noteremote.Database.AppDatabase;
import com.example.noteremote.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @SuppressLint("StaticFieldLeak")
    private static RecyclerViewAdapter adapter;
    private static AppDatabase db;
    private static List<Note> listaNote;
    @SuppressLint("StaticFieldLeak")
    private static RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    private static Context baseContext;
    @SuppressLint("StaticFieldLeak")
    private static View viewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(view ->
                startActivity(EditActivity.getIntentEdit(MainActivity.this, 'a')));

        baseContext = MainActivity.this;
        viewLayout = findViewById(R.id.viewLayout);

        if(savedInstanceState == null){
            db = Room.databaseBuilder(MainActivity.this, AppDatabase.class, "note_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();

            getValue();
        }

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapter(listaNote, MainActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    @SuppressLint("NotifyDataSetChanged")
    private static void getValue() {

        listaNote = db.noteDao().getAllNotes();

        recyclerView = viewLayout.findViewById((R.id.recyclerView));
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(baseContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapter(listaNote, baseContext);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public static void adapterNotifyAll(){
        getValue();
    }

    public static void sendObject(Context context, int pos) {
        context.startActivity(DetailsActivity.
                getDetailsIntent(context, listaNote.get(pos)));
    }

}