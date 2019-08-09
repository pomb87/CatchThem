package games.pombdev.catchthem.game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    volatile boolean playing;
    private Thread gameThread = null;
    int screenX;
    int screenY;
    private Player player;
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

    boolean flag;
    int playerAngle = 0;

    //context to be used in onTouchEvent to cause the activity transition from GameAvtivity to MainActivity.
    Context context;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        player = new Player(context, screenX, screenY);
        //single enemy initialization
        fallenObjects = new FallingObject(context, screenX, screenY, level, baseSpeed + (speedfactor*level), generateRandomNumber());
        surfaceHolder = getHolder();
        paint = new Paint();

        this.screenX = screenX;
        this.screenY = screenY;

        //initializing context
        this.context = context;
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
            //drawing the score on the game screen
            paint.setTextSize(50);
            canvas.drawText("Score:", this.screenX - 150, 50, paint);

            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint);
            canvas.drawBitmap(
                    fallenObjects.getBitmap(),
                    fallenObjects.getX(),
                    fallenObjects.getY(),
                    paint);
            surfaceHolder.unlockCanvasAndPost(canvas);

        }

    }

    private void update() {
        if (fallenObjects.getX() >= screenX) {
            flag = true;
        }
        player.update();
        if (Rect.intersects(player.getDetectCollision(), fallenObjects.getDetectCollision()) && playerAngle == fallenObjects.getAngleFallenObject()) {
            //check player rotation with color of fallenObject

            //incrementing score as time passes
            //score++;

            //reset fallen object
            fallenObjects = new FallingObject(context, screenX, screenY, level, baseSpeed + (speedfactor*level), generateRandomNumber());


        }
        if (flag) {

            fallenObjects.setX(screenX);
            fallenObjects = new FallingObject(context, screenX, screenY, level, baseSpeed + (speedfactor*level), generateRandomNumber());
                    //setting the flag false so that the else part is executed only when new enemy enters the screen
            flag = false;
        }
        fallenObjects.update();

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
