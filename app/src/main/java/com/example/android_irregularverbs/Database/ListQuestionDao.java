package com.example.android_irregularverbs.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ListQuestionDao {
    @Query("SELECT * FROM ListQuestion")
    List<ListQuestion> getAll();

    @Insert
    void insertAll(ListQuestion... questions);

    @Delete
    void delete(ListQuestion question);

    @Query("DELETE FROM ListQuestion")
    void deleteAll();
}