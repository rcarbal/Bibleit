package rcarb.bibleit;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

import rcarb.bibleit.objects.BibleQuestions;


public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView mVoiceInputTv;
    private ImageButton mSpeakBtn;

    //Firebase Database
// Firebase instance variables
    private DatabaseReference mFirebaseReference;
    private LinearLayout mButtonLinearLayout;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonLinearLayout= findViewById(R.id.button_layout);
        mButtonLinearLayout.setVisibility(View.INVISIBLE);
        mVoiceInputTv = (TextView) findViewById(R.id.voiceInput);
        mSpeakBtn = (ImageButton) findViewById(R.id.btnSpeak);
        mSpeakBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });

        mFirebaseReference = FirebaseDatabase.getInstance("https://bible-it-admin.firebaseio.com")
                .getReference();
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Ask a question");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mVoiceInputTv.setText(result.get(0)+"?");
                }
                break;
            }

        }

        if (mVoiceInputTv.getText().toString().equals("")){
            //TODO if textview is empty
        }else {
            mButtonLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    //Sends Voice dictation to firebase database.
    public void sendToFirebaseDatatabaseCloud(View view){
        BibleQuestions question = null;
        String questionString = "";
        if (mVoiceInputTv.getText().toString().equals("")){
            //TODO if textview is empty
        }else {
            question = new BibleQuestions(mVoiceInputTv.getText().toString());
            mFirebaseReference.child("bible-question").push().setValue(question);
            questionString = question.getQuestion();
        }

        mButtonLinearLayout.setVisibility(View.INVISIBLE);
        mVoiceInputTv.setText("");
        Toast.makeText(this,"QUESTION: "+questionString + " SUBMITED"
                , Toast.LENGTH_LONG).show();
    }

    public void clearQuestion(View view){
        mVoiceInputTv.setText("");
        mButtonLinearLayout.setVisibility(View.INVISIBLE);
    }

    public void questionsActivity(View view){
        Intent intent = new Intent(MainActivity.this, QuestionsListActivity.class);
        startActivity(intent);
    }
}