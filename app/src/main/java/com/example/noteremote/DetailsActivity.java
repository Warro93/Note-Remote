package com.example.noteremote;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import androidx.room.Room;

import com.example.noteremote.Database.AppDatabase;
import com.example.noteremote.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity {

    private final static String TAG = DetailsActivity.class.getName();
    private final static String KEY = TAG + ".key";
    private AppDatabase db;
    private Note note;

    private static final String titleAlertDialog = "Conferma cancellazione";
    private static final String msgAlertDialog = "Sei sicuro di voler eliminare la nota?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.noteremote.databinding.ActivityDetailsBinding binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        TextView textNote = findViewById(R.id.textNote);

        db = Room.databaseBuilder(DetailsActivity.this, AppDatabase.class, "note_database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        note = (Note) getIntent().getSerializableExtra(KEY);

        assert note != null;

        collapsingToolbarLayout.setTitle(note.getTitle());
        textNote.setText(note.getNote());

        FloatingActionButton editFab = binding.editFab;
        editFab.setOnClickListener(view ->
                startActivity(EditActivity
                        .getIntentEdit(DetailsActivity.this, 'e', note)));

        FloatingActionButton deleteFab = binding.deleteFab;
        deleteFab.setOnClickListener(view -> showAlert());
    }

    private void showAlert(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DetailsActivity.this);

        alertBuilder.setTitle(titleAlertDialog);
        alertBuilder.setMessage(msgAlertDialog);
        alertBuilder.setCancelable(true);

        alertBuilder.setPositiveButton("SI", (dialog, which) -> {
            db.noteDao().delete(note);
            MainActivity.adapterNotifyAll();
            finish();
        });

        alertBuilder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    public static Intent getDetailsIntent(Context context, Note note){
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(KEY, note);
        return intent;
    }
}