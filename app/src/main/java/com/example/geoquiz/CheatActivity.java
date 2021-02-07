package com.example.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class CheatActivity extends AppCompatActivity {

    private static final String TAG = "CheatActivity";
    private static final String EXTRA_ANSWER_IS_TRUE = "com.example.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.example.geoquiz.answer_shown";
    private static final String IS_ANSWER_SHOWN = "isAnswerShownKey";
    private static boolean isAnswerShown;
    private boolean mAnswerIsTrue;

    private TextView mAnswerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null) {
            isAnswerShown = savedInstanceState.getBoolean(IS_ANSWER_SHOWN);
        }
        if(isAnswerShown){
            setAnswerShownResult();
        }
        Log.d(TAG, "isAnswerShown = " + isAnswerShown);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = findViewById(R.id.answer_text_view);
        Button showAnswerButton = findViewById(R.id.show_answer_button);

        showAnswerButton.setOnClickListener(v -> {
            mAnswerTextView.setText(mAnswerIsTrue ? R.string.true_button : R.string.false_button);
            setAnswerShownResult();
        });
    }

    public static Intent newIntent(Context context, boolean answerIsTrue) {
        Log.d(TAG, "newIntent()");
        Intent intent = new Intent(context, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent resultIntent){
        Log.d(TAG, "wasAnswerShown()");
        return resultIntent.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    private void setAnswerShownResult() {
        Log.d(TAG, "setAnswerShowResult()");
        Intent dataIntent = new Intent();
        dataIntent.putExtra(EXTRA_ANSWER_SHOWN, true);
        setResult(RESULT_OK, dataIntent);
        isAnswerShown = true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSavedInstanceState(Bundle)");
        outState.putBoolean(IS_ANSWER_SHOWN, isAnswerShown);
    }
}