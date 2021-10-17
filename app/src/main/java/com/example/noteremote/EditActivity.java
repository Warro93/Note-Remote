package com.example.noteremote;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import androidx.room.Room;

import com.example.noteremote.Database.AppDatabase;
import com.example.noteremote.databinding.ActivityEditBinding;

public class EditActivity extends AppCompatActivity {

    private ActivityEditBinding binding;
    private final static String TAG = EditActivity.class.getName();
    private final static String ACTION_KEY = TAG + ".action.key";
    private final static String NOTE_KEY = TAG + ".nota.key";
    private char action;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private EditText editTitle, editText;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        editText = findViewById(R.id.editText);
        editTitle = findViewById(R.id.editTitle);
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);

        db = Room.databaseBuilder(this, AppDatabase.class, "note_database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        collapsingToolbarLayout.setTitle(" "); //Spazio per togliere titolo da toolbar

        action = getIntent().getCharExtra(ACTION_KEY, 'n');
        final Note note = (Note) getIntent().getSerializableExtra(NOTE_KEY);

        if(action == 'e'){
            editTitle.setText(note.getTitle());
            editText.setText(note.getNote());
        }

        FloatingActionButton saveFab = binding.saveFab;
        saveFab.setOnClickListener(view -> {

            String textNote = editText.getText().toString().trim();
            String textTitle = editTitle.getText().toString().trim();

            if(!textNote.isEmpty() || !textTitle.isEmpty()){

                switch(action){
                    //aggiungi nota
                    case 'a':
                        db.noteDao().insertAll(new Note(textTitle, textNote));
                        break;

                    //modidica nota
                    case 'e':
                        db.noteDao().delete(note);
                        Note newNote = new Note(textTitle, textNote);
                        db.noteDao().insertAll(newNote);
                        Intent i = new Intent(this, MainActivity.class);
                        startActivity(i);
                        break;
                }

                MainActivity.adapterNotifyAll();
                finish();

            }else
                Snackbar.make(view, "Inserisci almeno un campo!", Snackbar.LENGTH_SHORT).show();
        });

    }

    public static Intent getIntentEdit(Context context, char action){
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(ACTION_KEY , action);
        return intent;
    }

    public static Intent getIntentEdit(Context context, char action, Note note){
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra(ACTION_KEY, action);
        intent.putExtra(NOTE_KEY, note);
        return intent;
    }
}