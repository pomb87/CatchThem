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
    private boolean drawGood;
    private boolean drawBad;
    int lives;
    boolean isGameOver;
    Bitmap bitmapHeart;

    //context to be used in onTouchEvent to cause the activity transition from GameAvtivity to MainActivity.
    Context context;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        player = new Player(context, screenX, screenY);
        //single enemy initialization
        fallenObjects = new FallingObject(context, screenX, screenY, level, baseSpeed + (speedfactor*level), generateRandomNumber());
        surfaceHolder = getHolder();
        paint = new Paint();
        collision = new Collision();

        this.screenX = screenX;
        this.screenY = screenY;
        lives = 3;
        isGameOver = false;

        //initializing context
        this.context = context;
        bitmapHeart = Bitmap.createScaledBitmap(rotateBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.heart), 270), 100,100, false);
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
            } else {
                GameViewHelper.paintScore(canvas, paint, score, this.screenX);
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
        if (!isGameOver) {
            if (Rect.intersects(player.getDetectCollision(), fallenObjects.getDetectCollision())) {
                if (playerAngle == fallenObjects.getAngleFallenObject()) {
                    //incrementing score as time passes
                    score++;
                    drawGood = true;
                    mapCollision(fallenObjects.getX(), fallenObjects.getY(), true);
                } else {
                    drawBad = true;
                    mapCollision(fallenObjects.getX(), fallenObjects.getY(), false);
                    lives = lives - 1;
                    if (lives < 1) {
                        isGameOver = true;
                    }
                }
                fallenObjects = new FallingObject(context, screenX, screenY, level, baseSpeed + (speedfactor * level), generateRandomNumber());
            }
            fallenObjects.update();
        }
    }

    private void mapCollision(int x, int y, boolean good) {
        if (good) {
            collision.setBitmap(Bitmap.createScaledBitmap(rotateBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.good), 270), 100,100, false));
        } else {
            collision.setBitmap(Bitmap.createScaledBitmap(rotateBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bad), 270), 100,100, false));
        }
      collision.setX(fallenObjects.getX()-50);
        collision.setY(fallenObjects.getY());
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
            //rotate player
                player.setBitmap(rotateBitmap(player.getBitmap(), 90));
                updatePlayerAngle(90);
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
}
