package com.example.android_irregularverbs.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ListQuestion.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ListQuestionDao userDao();
}