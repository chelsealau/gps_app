package com.example.basicgps.database.entityDAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.basicgps.database.entities.Score;

import java.util.List;

@Dao
public interface ScoreDAO {
    @Insert
    void insertScore(Score scoreToInsert);

    @Update
    void updateScore(Score scoreToUpdate);

    @Delete
    void deleteScore(Score scoreToDelete);

    @Query("SELECT * FROM scores LIMIT 1")
    Score getSavedScore();

    @Query("SELECT COUNT(*) FROM scores")
    int documentCount();

    @Query("DELETE FROM scores")
    void clearTable();
}
