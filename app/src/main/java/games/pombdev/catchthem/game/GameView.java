package games.pombdev.catchthem.game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;
import java.util.logging.Logger;

import games.pombdev.catchthem.MenuActivity;
import games.pombdev.catchthem.R;

public class GameView extends SurfaceView implements Runnable {

    volatile boolean playing;
    private Thread gameThread = null;
    int screenX;
    int screenY;
    private Player player;
    private Collision collision;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    //the falling objects
    private FallingObject fallenObjects;
    //base speed for level 1
    int baseSpeed = 15;
    //speed factor
    int speedfactor = 5;
    //current level
    int level;
    int playerAngle = 0;
    int score = 0;
    int highScore1 = 0;
    private boolean drawGood;
    private boolean drawBad;
    int lives;
    boolean isGameOver;
    Bitmap bitmapHeart;
    private boolean isGameStart;
    private boolean reverse;
    private int speedLimit = 5;
    private int reverseLimit = 10;
    //the high Scores Holder
    int highScore[] = new int[10];
    //Shared Prefernces to store the High Scores
    SharedPreferences sharedPreferences;

    //context to be used in onTouchEvent to cause the activity transition from GameAvtivity to MainActivity.
    Context context;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        player = new Player(context, screenX, screenY);
        //single enemy initialization
        reverse = false;
        fallenObjects = new FallingObject(context, screenX, screenY, level, baseSpeed + (speedfactor*level), generateRandomNumber(), reverse);
        surfaceHolder = getHolder();
        paint = new Paint();
        collision = new Collision();

        this.screenX = screenX;
        this.screenY = screenY;
        lives = 3;
        isGameOver = false;
        isGameStart = true;

        sharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);

        //initializing the array high scores with the previous values
        highScore[0] = sharedPreferences.getInt("score1", 0);
        highScore[1] = sharedPreferences.getInt("score2", 0);
        highScore[2] = sharedPreferences.getInt("score3", 0);
        highScore[3] = sharedPreferences.getInt("score4", 0);
        highScore[4] = sharedPreferences.getInt("score5", 0);
        highScore[5] = sharedPreferences.getInt("score6", 0);
        highScore[6] = sharedPreferences.getInt("score7", 0);
        highScore[7] = sharedPreferences.getInt("score8", 0);
        highScore[8] = sharedPreferences.getInt("score9", 0);
        highScore[9] = sharedPreferences.getInt("score10", 0);

        highScore1 = highScore[0];

        //initializing context
        this.context = context;
        bitmapHeart = Bitmap.createScaledBitmap(rotateBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.heart), 0), 100,100, false);
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            paint.setColor(Color.WHITE);
            if (isGameOver) {
                GameViewHelper.paintGameover(canvas, paint, score);
            }
            else if (isGameStart) {
                GameViewHelper.paintGameStartMessage(canvas, paint, score);
            } else {
                GameViewHelper.paintScore(canvas, paint, score, this.screenX);
                GameViewHelper.paintHighScore(canvas, paint, highScore1, this.screenX);
                GameViewHelper.paintPlayerAndFallenObj(canvas, player, fallenObjects, paint);
                GameViewHelper.paintCollision(canvas, drawGood || drawBad, collision, paint);
                GameViewHelper.paintHearts(canvas, lives, paint, bitmapHeart, this.screenX, this.screenY);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }



    private void update() {
        if (drawGood || drawBad) {
            try {
                gameThread.sleep(200);
            } catch (Exception e) {
                Log.getStackTraceString(e);
            }
        }
        drawBad = false;
        drawGood = false;
        player.update();
        if (!isGameOver && !isGameStart) {
            if (Rect.intersects(player.getDetectCollision(), fallenObjects.getDetectCollision())) {
                if ((playerAngle == fallenObjects.getAngleFallenObject() && !reverse)
                        || playerAngle == fallenObjects.getAngleFallenObjectReverse() && reverse) {
                    //incrementing score as time passes
                    score++;
                    drawGood = true;
                    mapCollision(fallenObjects.getX(), fallenObjects.getY(), true);
                    if (score % 5 == 0 && level < speedLimit) {
                        level++;
                    }
                    if (score % 15 == 0 && lives < 3) {
                        lives++;
                    }
                    if (score > reverseLimit) {
                        reverse = generateRandomNumber() % 2 == 0;
                    }
                } else {
                    drawBad = true;
                    mapCollision(fallenObjects.getX(), fallenObjects.getY(), false);
                    lives = lives - 1;
                    if (lives < 1) {
                        isGameOver = true;
                        //set the highscore to the correct places
                        assignHighscore();

                        //storing the scores through shared Preferences
                        SharedPreferences.Editor e = sharedPreferences.edit();

                        for (int i = 0; i < 10; i++) {

                            int j = i + 1;
                            e.putInt("score" + j, highScore[i]);
                        }
                        e.apply();
                    }
                }
                fallenObjects = new FallingObject(context, screenX, screenY, level, baseSpeed + (speedfactor * level), generateRandomNumber(), reverse);
            }
            fallenObjects.update(reverse);
        }
    }

    private void mapCollision(int x, int y, boolean good) {
        if (good) {
            collision.setBitmap(Bitmap.createScaledBitmap(rotateBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.good), 0), 100,100, false));
        } else {
            collision.setBitmap(Bitmap.createScaledBitmap(rotateBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bad), 0), 100,100, false));
        }
        collision.setX(fallenObjects.getX());
        if (reverse) {
            collision.setY(fallenObjects.getY() + 50);
        } else {
            collision.setY(fallenObjects.getY() - 50);
        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();

        int X = (int) event.getX();
        int Y = (int) event.getY();

        switch (eventaction) {

            case MotionEvent.ACTION_DOWN:

                if(isGameOver){
                    if(eventaction==MotionEvent.ACTION_DOWN){
                        context.startActivity(new Intent(context, MenuActivity.class));
                    }
                }
            //rotate player
                player.setBitmap(rotateBitmap(player.getBitmap(), 90));
                updatePlayerAngle(90);
                if (isGameStart) {
                    isGameStart = false;
                }
            break;

            case MotionEvent.ACTION_UP:
                // touch drop - just do things here after dropping
                break;
        }
        // redraw the canvas
        invalidate();
        return true;
    }

    private static Bitmap rotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void updatePlayerAngle(int angle) {
        playerAngle = playerAngle + angle;
        if (playerAngle == 360) {
            playerAngle = 0;
        }
    }

    private int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(4) + 1;
    }

    private void assignHighscore() {
        //Assigning the scores to the highscore integer array
        for (int i = 0; i < 10; i++) {
            if (highScore[i] < score) {

                //Assign the scores to their correct places
                switch (i) {
                    case 0:
                        highScore[9] = highScore[8];
                        highScore[8] = highScore[7];
                        highScore[7] = highScore[6];
                        highScore[6] = highScore[5];
                        highScore[5] = highScore[4];
                        highScore[4] = highScore[3];
                        highScore[3] = highScore[2];
                        highScore[2] = highScore[1];
                        highScore[1] = highScore[0];
                        break;
                    case 1:
                        highScore[9] = highScore[8];
                        highScore[8] = highScore[7];
                        highScore[7] = highScore[6];
                        highScore[6] = highScore[5];
                        highScore[5] = highScore[4];
                        highScore[4] = highScore[3];
                        highScore[3] = highScore[2];
                        highScore[2] = highScore[1];
                        break;
                    case 2:
                        highScore[9] = highScore[8];
                        highScore[8] = highScore[7];
                        highScore[7] = highScore[6];
                        highScore[6] = highScore[5];
                        highScore[5] = highScore[4];
                        highScore[4] = highScore[3];
                        highScore[3] = highScore[2];
                        break;
                    case 3:
                        highScore[9] = highScore[8];
                        highScore[8] = highScore[7];
                        highScore[7] = highScore[6];
                        highScore[6] = highScore[5];
                        highScore[5] = highScore[4];
                        highScore[4] = highScore[3];
                        break;
                    case 4:
                        highScore[9] = highScore[8];
                        highScore[8] = highScore[7];
                        highScore[7] = highScore[6];
                        highScore[6] = highScore[5];
                        highScore[5] = highScore[4];
                        break;
                    case 5:
                        highScore[9] = highScore[8];
                        highScore[8] = highScore[7];
                        highScore[7] = highScore[6];
                        highScore[6] = highScore[5];
                        break;
                    case 6:
                        highScore[9] = highScore[8];
                        highScore[8] = highScore[7];
                        highScore[7] = highScore[6];
                        break;
                    case 7:
                        highScore[9] = highScore[8];
                        highScore[8] = highScore[7];
                        break;
                    case 8:
                        highScore[9] = highScore[8];
                        break;
                    default:
                        break;
                }

                highScore[i] = score;
                break;
            }
        }
    }
}
