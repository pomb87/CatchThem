package games.pombdev.catchthem;

import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import games.pombdev.catchthem.game.GameView;

public class GameActivity extends AppCompatActivity {

    //declaring gameview
    private GameView gameView;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Getting display object
        Display display = getWindowManager().getDefaultDisplay();

        //Getting the screen resolution into point object
        Point size = new Point();
        display.getSize(size);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544/1033173712");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        boolean adRequest = new AdRequest.Builder()
                .addTestDevice("833D1B561FFDE098DADF90348C419AAB")
                .build().isTestDevice(this);
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .addTestDevice("833D1B561FFDE098DADF90348C419AAB")
                .build());
        //mInterstitialAd.loadAd(new AdRequest.Builder().build());
        //mInterstitialAd.loadAd(new AdRequest.Builder.addTestDevice("833D1B561FFDE098DADF90348C419AAB"));


        RelativeLayout layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        gameView = new GameView(this, size.x, size.y, mInterstitialAd);
        layout.addView(gameView);

        //adding it to contentview
        setContentView(layout);
    }

    //pausing the game when activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    //running the game when activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

}
