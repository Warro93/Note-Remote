package com.example.noteremote.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.noteremote.Note;

import java.util.List;

@Dao
public interface NoteDAO {

    @Query("SELECT * FROM note")
    List<Note> getAllNotes();

    @Insert
    void insertAll(Note... notes);

    @Delete
    void delete(Note note);

}
