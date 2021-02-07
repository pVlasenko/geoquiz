package com.example.geoquiz;

import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String INDEXES_LIST = "indexesList";
    private static final String CHEATED_INDEX_LIST = "cheatedIndexList";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private TextView mQuestionTextView;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;

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
    private List<Integer> mCheatedAnswerIndexList = new ArrayList<>();
    private double mPercentOfResults = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mAnswerIndexesList = savedInstanceState.getIntegerArrayList(INDEXES_LIST);
            mCheatedAnswerIndexList = savedInstanceState.getIntegerArrayList(CHEATED_INDEX_LIST);
        }
        mQuestionTextView = findViewById(R.id.question_text_view);
        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);
        mNextButton = findViewById(R.id.next_button);
        mPrevButton = findViewById(R.id.prev_button);
        mCheatButton = findViewById(R.id.cheat_button);


        updateQuestionScreen();

        mTrueButton.setOnClickListener(v -> checkAnswer(true));

        mFalseButton.setOnClickListener(v -> checkAnswer(false));

        mNextButton.setOnClickListener(v -> moveToNextQuestion());

        mPrevButton.setOnClickListener(v -> moveToPrevQuestion());

        mQuestionTextView.setOnClickListener(v -> moveToNextQuestion());

        mCheatButton.setOnClickListener(v -> {
            boolean answerIsTrue = mQuestions[mCurrentIndex].isAnswerTrue();
            Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
            startActivityForResult(intent, REQUEST_CODE_CHEAT);
        });
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
        outState.putIntegerArrayList(CHEATED_INDEX_LIST, (ArrayList<Integer>) mCheatedAnswerIndexList);
    }

    private void updateQuestion() {
        int question = mQuestions[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestions[mCurrentIndex].isAnswerTrue();
        int messageResId;
        if (mCheatedAnswerIndexList.contains(mCurrentIndex)) {
            messageResId = R.string.judgment_toast;
        } else {

            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mPercentOfResults += (double) 100 / mQuestions.length;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

        disableAnswerButtons();

        mAnswerIndexesList.add(mCurrentIndex);

        if (mAnswerIndexesList.size() == mQuestions.length) {
//            mNextButton.setBackgroundColor(0xFFD5D5D5);
            mNextButton.setEnabled(false);
//            mPrevButton.setBackgroundColor(0xFFD5D5D5);
            mPrevButton.setEnabled(false);
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

    private void enableAnswerButtons() {
        mTrueButton.setBackgroundColor(0xFF156C3C);
        mTrueButton.setEnabled(true);
        mFalseButton.setBackgroundColor(0xFFA6071F);
        mFalseButton.setEnabled(true);
        mCheatButton.setBackgroundColor(0xFFC19103);
        mCheatButton.setEnabled(true);
    }

    private void disableAnswerButtons() {
        mTrueButton.setBackgroundColor(0xFFD5D5D5);
        mTrueButton.setEnabled(false);
        mFalseButton.setBackgroundColor(0xFFD5D5D5);
        mFalseButton.setEnabled(false);
        mCheatButton.setBackgroundColor(0xFFD5D5D5);
        mCheatButton.setEnabled(false);
    }

    private void updateQuestionScreen() {
        updateQuestion();
        if (mAnswerIndexesList.contains(mCurrentIndex)) {
            disableAnswerButtons();
        } else {
            enableAnswerButtons();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            if (CheatActivity.wasAnswerShown(data)){
                mCheatedAnswerIndexList.add(mCurrentIndex);
            }
        }
    }
}