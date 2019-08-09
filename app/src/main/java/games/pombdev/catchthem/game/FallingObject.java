package games.pombdev.catchthem.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

import games.pombdev.catchthem.R;

public class FallingObject {

    private Bitmap bitmap;
    private int x;
    private int y;
    private int speed = 1;

    private int maxX;
    private int minX;

    private int maxY;
    private int minY;
    private int angleFallenObject = 0;

    //creating a rect object for a friendly ship
    private Rect detectCollision;


    public FallingObject(Context context, int screenX, int screenY, int level, int lvlSpeed, int random) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.fo_blue);
        switch (random) {
            case 1:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.fo_yellow);
                this.angleFallenObject = 0;
                break;

            case 2:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.fo_green);
                this.angleFallenObject = 90;
                break;

            case 3:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.fo_red);
                this.angleFallenObject = 180;
                break;

            case 4:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.fo_blue);
                this.angleFallenObject = 270;
                break;
            default:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.fo_blue);
                break;
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, 100,100, false);

        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        Random generator = new Random();
        this.speed = generator.nextInt(6) + lvlSpeed;
        x = 0;
        y = (screenY - bitmap.getHeight()) / 2;

        if (x <= minX + bitmap.getWidth()) {
            x = minX + bitmap.getWidth();
        }

        //initializing rect object
        detectCollision = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update() {

        x += speed ;

        if (x < minX) {
            x = minX;
        }
        if (x > maxX) {
            x = maxX;
        }
        //Adding the top, left, bottom and right to the rect object
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();
    }



    //one more getter for getting the rect object
    public Rect getDetectCollision() {
        return detectCollision;
    }

    //getters
    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getAngleFallenObject() {
        return angleFallenObject;
    }
}

