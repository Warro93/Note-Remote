package com.example.noteremote.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.noteremote.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract NoteDAO noteDao();

}
