package com.example.geoquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String INDEXES_LIST = "indexesList";

    private Button mTrueButton;
    private Button mFalseButton;
    private TextView mQuestionTextView;

    private final Question[] mQuestions = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    private int mCurrentIndex = 0;
    private List<Integer> mAnswerIndexesList = new ArrayList<>();
    private double mPercentOfResults = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mAnswerIndexesList = savedInstanceState.getIntegerArrayList(INDEXES_LIST);
        }
        mQuestionTextView = findViewById(R.id.question_text_view);
        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);
        ImageButton nextButton = findViewById(R.id.next_button);
        ImageButton prevButton = findViewById(R.id.prev_button);


        updateQuestionScreen();

        mTrueButton.setOnClickListener(v -> checkAnswer(true));

        mFalseButton.setOnClickListener(v -> checkAnswer(false));

        nextButton.setOnClickListener(v -> moveToNextQuestion());

        prevButton.setOnClickListener(v -> moveToPrevQuestion());

        mQuestionTextView.setOnClickListener(v -> moveToNextQuestion());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState()");
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putIntegerArrayList(INDEXES_LIST, (ArrayList<Integer>) mAnswerIndexesList);
    }

    private void updateQuestion() {
        int question = mQuestions[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestions[mCurrentIndex].isAnswerTrue();

        int messageResId;

        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            mPercentOfResults += (double) 100 / mQuestions.length;
        } else {
            messageResId = R.string.incorrect_toast;
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);

        mAnswerIndexesList.add(mCurrentIndex);

        if(mAnswerIndexesList.size() == mQuestions.length){
            Toast.makeText(QuizActivity.this, "You results: " + (int) mPercentOfResults + "% of the correct answers", Toast.LENGTH_LONG).show();
        }
    }

    private void moveToNextQuestion() {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestions.length;
        updateQuestionScreen();
    }

    private void moveToPrevQuestion() {
        if (mCurrentIndex == 0) {
            mCurrentIndex = mQuestions.length;
        }
        mCurrentIndex = (mCurrentIndex - 1) % mQuestions.length;
        updateQuestionScreen();
    }

    private void enableAnswerButtons(){
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
    }

    private void disableAnswerButtons(){
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
    }

    private void updateQuestionScreen(){
        updateQuestion();
        if (mAnswerIndexesList.contains(mCurrentIndex)){
            disableAnswerButtons();
        } else {
            enableAnswerButtons();
        }
    }
}