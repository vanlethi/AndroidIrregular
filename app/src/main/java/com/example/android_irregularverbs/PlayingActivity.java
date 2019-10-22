package com.example.android_irregularverbs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        getAndDisplayListquestion(0);
    }

    @SuppressLint("StaticFieldLeak")
    public void getAndDisplayListquestion(final int index_question) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
//                MediaPlayer ring= MediaPlayer.create(PlayingActivity.this,R.drawable.);
//                ring.start();
                final List<ListQuestion> questions = db.userDao().getAll();
                final TextView text_view_question = findViewById(R.id.text_view_question);
                final LinearLayout liner_layout_list = findViewById(R.id.liner_layout_list);
                final AlertDialog.Builder alert_message= new AlertDialog.Builder(PlayingActivity.this);
                alert_message.setTitle("Irregular Game Message");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text_view_question.setText(questions.get(index_question).question);
                        String[] list_answer = questions.get(index_question).list_answer.split(", ");
                        for(int i =0; i<list_answer.length; i++){
                            final Button btn_answer = new Button(PlayingActivity.this);
                            btn_answer.setText(list_answer[i]);
                            btn_answer.setId(i);
                            btn_answer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(btn_answer.getText().equals(questions.get(index_question).correct_answer)){
                                        alert_message.setMessage("Correct Answer");
                                        alert_message.setIcon(R.drawable.icon_smile);
                                    }else {
                                        alert_message.setMessage("Incorrect Answer");
                                        alert_message.setIcon(R.drawable.icon_bad);
                                    }
                                    alert_message.setPositiveButton("Next question", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if(index_question > questions.size()){
                                                Toast.makeText(getApplicationContext(), "Het cau hoi roi nhe", Toast.LENGTH_LONG).show();
                                            }else {
                                                liner_layout_list.removeAllViews();
                                                getAndDisplayListquestion(index_question +1);
                                            }
                                        }
                                    }).show();
                                }
                            });
                            liner_layout_list.addView(btn_answer);
                        }
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
                question.question = "Fill the correct answer for this sentence 'Have you ______________ your lost dog yet?'";
                question.list_answer = "find, found, both are true, both are fail";
                question.correct_answer= "found";
                db.userDao().insertAll(question);
                question.question = "Fill the correct answer for this sentence 'She spoke too softly. I couldn't ______________ her'";
                question.list_answer = "hear, heard, both are true, both are fail";
                question.correct_answer= "hear";
                db.userDao().insertAll(question);
                question.question = "Fill the correct answer for this sentence 'We went shopping and I ______________ a new pair of jeans'";
                question.list_answer = "win, won, both are true, both are fail";
                question.correct_answer= "won";
                db.userDao().insertAll(question);
                return null;
            }
        }.execute();
    }
}
