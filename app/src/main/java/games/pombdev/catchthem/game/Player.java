package games.pombdev.catchthem.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;

import games.pombdev.catchthem.R;

public class Player {
    private Bitmap bitmap;
    private int x;
    private int y;
    private int maxX;
    private int minX;

    private Rect detectCollision;

    public Player(Context context, int screenX, int screenY) {
        //fetch player bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        //scale player bitmap
        bitmap = Bitmap.createScaledBitmap(bitmap, 300,300, false);

        x = (screenX - bitmap.getWidth()) / 2;
        // y = (0 + bitmap.getHeight());
        y = (screenY - bitmap.getHeight()) / 2;


        maxX = screenX - bitmap.getWidth();
        minX = 0;

        //initializing rect object
        detectCollision = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update() {

        if (x < minX) {
            x = minX;
        }
        if (x > maxX) {
            x = maxX;
        }

        //adding top, left, bottom and right to the rect object
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();

    }

    //one more getter for getting the rect object
    public Rect getDetectCollision() {
        return detectCollision;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap (Bitmap bitmap) {
        this.bitmap = bitmap;
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

}
