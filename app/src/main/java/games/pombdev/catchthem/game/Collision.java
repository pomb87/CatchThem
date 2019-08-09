package games.pombdev.catchthem.game;

import android.graphics.Bitmap;

public class Collision {
    private Bitmap bitmap;
    private int x;
    private int y;

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
