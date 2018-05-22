package rcarb.bibleit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class QuestionsListActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private TextView mQuestions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_questions);

        mQuestions = findViewById(R.id.question_tv);
        mDatabaseReference = FirebaseDatabase.getInstance("https://bible-it-admin.firebaseio.com")
                .getReference().child("bible-question");
        mDatabaseReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        collectQuestions((Map<String, Object>) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    private void collectQuestions(Map<String, Object> questions){
        ArrayList<String> bibleQuestions = new ArrayList<>();

        for (Map.Entry<String, Object> question: questions.entrySet()){
            Map singleQuestion = (Map)question.getValue();
            bibleQuestions.add((String)singleQuestion.get("question"));
        }

        StringBuilder showQuestions = new StringBuilder();
        for (int i = 0; i < bibleQuestions.size(); i++){
            String x = bibleQuestions.get(i);
            showQuestions.append(x +"\n\n");
        }

        mQuestions.setText(showQuestions);

    }
}
