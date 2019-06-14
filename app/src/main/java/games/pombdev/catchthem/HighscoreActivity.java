package games.pombdev.catchthem;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HighscoreActivity extends AppCompatActivity {

    TextView textView,textView2,textView3,textView4,textView5,textView6,textView7,textView8,textView9,textView10;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        //initializing the textViews
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.textView6);
        textView7 = findViewById(R.id.textView7);
        textView8 = findViewById(R.id.textView8);
        textView9 = findViewById(R.id.textView9);
        textView10 = findViewById(R.id.textView10);

        sharedPreferences  = getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);

        //setting the values to the textViews
        textView.setText(getString(R.string.highscore1, sharedPreferences.getInt("score1",0)));
        textView2.setText(getString(R.string.highscore2, sharedPreferences.getInt("score2",0)));
        textView3.setText(getString(R.string.highscore3, sharedPreferences.getInt("score3",0)));
        textView4.setText(getString(R.string.highscore4, sharedPreferences.getInt("score4",0)));
        textView5.setText(getString(R.string.highscore5, sharedPreferences.getInt("score5",0)));
        textView6.setText(getString(R.string.highscore6, sharedPreferences.getInt("score6",0)));
        textView7.setText(getString(R.string.highscore7, sharedPreferences.getInt("score7",0)));
        textView8.setText(getString(R.string.highscore8, sharedPreferences.getInt("score8",0)));
        textView9.setText(getString(R.string.highscore9, sharedPreferences.getInt("score9",0)));
        textView10.setText(getString(R.string.highscore10, sharedPreferences.getInt("score10",0)));

    }
}