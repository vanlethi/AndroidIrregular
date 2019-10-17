package com.example.android_irregularverbs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_irregularverbs.Database.AppDatabase;
import com.example.android_irregularverbs.Database.ListQuestion;

import java.util.List;

public class PlayingActivity extends AppCompatActivity {
    AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();
//        addListQuestion();
        getAndDisplayListquestion();
    }

    @SuppressLint("StaticFieldLeak")
    public void getAndDisplayListquestion() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                final List<ListQuestion> questions = db.userDao().getAll();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PlayingActivity.this, "Size: " + questions.size(), Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
            }
        }.execute();
    }

    public void addListQuestion() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ListQuestion question = new ListQuestion();
                question.question = "What is the past simple of the verb to begin?";
                question.list_answer = "begun, began, begin, begon";
                question.correct_answer= "began";
                db.userDao().insertAll(question);
                return null;
            }
        }.execute();
    }
}
