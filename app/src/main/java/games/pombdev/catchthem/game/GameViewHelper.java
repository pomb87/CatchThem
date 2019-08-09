package games.pombdev.catchthem.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class GameViewHelper {
    public static void paintHearts(Canvas canvas, int lives, Paint paint, Bitmap bitmap, int screenX, int screenY) {
        //draw amounts of hearts
        for (int i = 0; i < lives; i++) {
            canvas.drawBitmap(
                    bitmap,
                    (screenX / 2) - (i*100),
                    (screenY / 2) + 300,
                    paint);
        }
    }

    public static void paintCollision(Canvas canvas, boolean draw, Collision collision, Paint paint) {
        if (draw) {
            canvas.drawBitmap(
                    collision.getBitmap(),
                    collision.getX(),
                    collision.getY(),
                    paint);
        }
    }

    public static void paintPlayerAndFallenObj(Canvas canvas, Player player, FallingObject fallenObjects, Paint paint) {
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
    }

    public static void paintScore(Canvas canvas, Paint paint, int score, int screenX) {
        //drawing the score on the game screen
        paint.setTextSize(50);
        canvas.drawText("Score:" + score, screenX - 250, 50, paint);
    }

    public static void paintGameover(Canvas canvas, Paint paint, int score) {
        paint.setTextSize(130);
        paint.setTextAlign(Paint.Align.CENTER);
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
        canvas.drawText("Game Over", canvas.getWidth() / 2, yPos, paint);
    }
}
