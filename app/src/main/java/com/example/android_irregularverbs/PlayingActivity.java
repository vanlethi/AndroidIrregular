package com.example.android_irregularverbs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_irregularverbs.Database.AppDatabase;
import com.example.android_irregularverbs.Database.ListQuestion;

import java.util.List;

public class PlayingActivity extends AppCompatActivity {
    AppDatabase db;
    int count=0;//Tính câu trả lời player trả lời đúng
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();
//        addListQuestion();
        displayQuestionAndHandle(0);
    }

    //Hàm hiển thị câu hỏi và xử lý trong quá trình chơi game
    @SuppressLint("StaticFieldLeak")
    public void displayQuestionAndHandle(final int index_question) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                final List<ListQuestion> questions = db.userDao().getAll();
                final TextView text_view_question = findViewById(R.id.text_view_question);
                final LinearLayout liner_layout_list = findViewById(R.id.liner_layout_list);
                final TextView text_view_indexquestion = findViewById(R.id.text_view_indexquestion);
                final TextView text_view_timer = findViewById(R.id.text_view_timer);
                final AlertDialog.Builder alert_message= new AlertDialog.Builder(PlayingActivity.this);
                alert_message.setTitle("Irregular Game Message");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Kiểm tra vị trí câu hỏi chuẩn bị xuất hiện
                        // Nếu nhỏ hơn size của mảng thì tiếp tục hiển thị câu hỏi, lớn hơn thì xuất hiện alert message
                        //Nếu lớn hơn size của mảng thì hiển thị message và trở về màn hình MainActivity
                        if(index_question < questions.size()){
                            //Đếm ngược thời gian cho mỗi câu hỏi(5s/question)
                            final CountDownTimer countDownTimer = new CountDownTimer(5000, 1000){
                                public  void onTick(long millisUntilFinished){
                                    //Hiển thị thời gian đếm ngược
                                    text_view_timer.setText("Timer: " + millisUntilFinished / 1000);
                                }
                                @Override
                                public void onFinish() {
                                    alert_message.setMessage("Time done");
                                    alert_message.setIcon(R.drawable.icon_bad);
                                    alert_message.setPositiveButton("Next question", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //Dùng cancel() ngừng countDownTimer
                                            cancel();
                                            //remove tất cả câu hỏi và câu trả lời
                                            liner_layout_list.removeAllViews();
                                            //Gọi lại hàm với vị trí câu hỏi +1(phương pháp đệ quy)
                                            displayQuestionAndHandle(index_question +1);
                                        }
                                    }).show();
                                }
                            }.start();
                            text_view_question.setText(questions.get(index_question).question);
                            text_view_indexquestion.setText("Question: "+(index_question+1)+"/"+questions.size());
                            //Cắt chuỗi các câu trả lời thành mảng, sau đó in ra màn hình + thiết lập sự kiện cho mỗi câu trả lời khi click vào
                            String[] list_answer = questions.get(index_question).list_answer.split(", ");
                            for(int i =0; i<list_answer.length; i++){
                                final Button btn_answer = new Button(PlayingActivity.this);
                                btn_answer.setText(list_answer[i]);
                                btn_answer.setId(i);
                                btn_answer.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //So sánh giá trị câu trả lời được click với câu trả lời đúng
                                        //Nếu đúng: hiển thị message và tăng count lên 1 đơn vị sau đó nhảy sang câu hỏi khác
                                        //Nếu sai: chỉ hiển thị message sau đó nhảy sang câu hỏi khác
                                        if(btn_answer.getText().equals(questions.get(index_question).correct_answer)){
                                            alert_message.setMessage("Correct Answer");
                                            alert_message.setIcon(R.drawable.icon_smile);
                                            count ++;
                                        }else {
                                            alert_message.setMessage("Incorrect Answer");
                                            alert_message.setIcon(R.drawable.icon_bad);
                                        }
                                        alert_message.setPositiveButton("Next question", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                    countDownTimer.cancel();
                                                    liner_layout_list.removeAllViews();
                                                    displayQuestionAndHandle(index_question +1);
                                            }
                                        }).show();
                                    }
                                });
                                liner_layout_list.addView(btn_answer);
                            }
                        }else {
                            alert_message.setMessage("Question Passed: "+ count);
                            alert_message.setPositiveButton("Go back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(PlayingActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }).show();
                        }
                    }
                });
                return null;
            }
        }.execute();
    }

    //Hàm thêm câu hỏi vào DB
    public void addListQuestion() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ListQuestion question = new ListQuestion();
                question.question = "What is the past simple of the verb to begin?";
                question.list_answer = "begun, began, begin, begon";
                question.correct_answer= "began";
                db.userDao().insertAll(question);
                question.question = "Fill the correct answer: 'Have you ___ your lost dog yet?'";
                question.list_answer = "find, found, both are true, both are fail";
                question.correct_answer= "found";
                db.userDao().insertAll(question);
                question.question = "Fill the correct answer: 'She spoke too softly. I couldn't ___ her'";
                question.list_answer = "hear, heard, both are true, both are fail";
                question.correct_answer= "hear";
                db.userDao().insertAll(question);
                question.question = "Fill the correct answer: 'We went shopping and I ___ a new pair of jeans'";
                question.list_answer = "win, won, both are true, both are fail";
                question.correct_answer= "won";
                db.userDao().insertAll(question);
                return null;
            }
        }.execute();
    }
}
