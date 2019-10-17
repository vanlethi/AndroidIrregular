package com.example.android_irregularverbs.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ListQuestion {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "question")
    public String question;

    @ColumnInfo(name = "list_answer")
    public String list_answer;

    @ColumnInfo(name ="correct_answer")
    public String correct_answer;
}