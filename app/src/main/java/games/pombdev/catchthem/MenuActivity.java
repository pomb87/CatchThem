package games.pombdev.catchthem;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MenuActivity extends Activity implements View.OnClickListener {

    // play button
    private ImageButton buttonPlay;
    //high score button
    private ImageButton buttonScore;
    //end button
    private ImageButton buttonEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //setting the orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //getting the button
        buttonPlay = findViewById(R.id.buttonPlay);

        //initializing the highscore button
        buttonScore = findViewById(R.id.buttonScore);

        //initializing the end button
        buttonEnd = findViewById(R.id.buttonEnd);

        //setting the on click listener to high score button
        buttonScore.setOnClickListener(this);
        //setting the on click listener to play now button
        buttonPlay.setOnClickListener(this);
        //setting the on click listener to end now button
        buttonEnd.setOnClickListener(this);
    }

    // the onclick methods
    @Override
    public void onClick(View v) {

        if (v == buttonPlay) {
            //the transition from MainActivity to GameActivity
            startActivity(new Intent(MenuActivity.this, GameActivity.class));
        }
        if (v == buttonScore) {
            //the transition from MainActivity to HighScore activity
            startActivity(new Intent(MenuActivity.this, HighscoreActivity.class));
        }
        if (v == buttonEnd) {
            //finish all activities and exit app
            //finishAndRemoveTask();
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // GameView.stopMusic();
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

}