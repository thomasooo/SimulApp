package simulapp.util;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by Tomas Ballon on 29.1.2017.
 */
public final class geometry {

    public static final void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
        drawArrow(g, x1, y1, x2, y2, 8);
    }

    public static final void drawArrow(Graphics g, int x1, int y1, int x2, int y2, int arrSize) {
        Graphics2D g2 = (Graphics2D) g.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g2.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g2.drawLine(0, 0, len, 0);
        g2.fillPolygon(new int[] {len, len-arrSize, len-arrSize, len}, new int[] {0, -arrSize, arrSize, 0}, 4);
    }

    public static final Point midPoint(Point p1, Point p2){
        return new Point(((p1.x + p2.x) / 2), ((p1.y + p2.y) / 2));
    }

    //kde sa ma zacat kreslit oval / rec a pod
    public static final Point getStartXY(int x, int y, int w, int h){
        return new Point((x - (w / 2)), (y - (h / 2)));
    }

    public static final Point getNextPos(int x1, int y1, int x2, int y2, int speed){
        int i = 0;
        int resX = x1;
        int resY = y1;

        while(i < speed) {
            int deltaX = x2 - x1;
            int deltaY = y2 - y1;

            double distance = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));

            double velX = (deltaX / distance) * 5;
            double velY = (deltaY / distance) * 5;

            resX = x1 + (int) velX;
            resY = y1 + (int) velY;

            if(resX == x2 && resY == y2){
                break;
            }

            x1 = resX;
            y1 = resY;

            i++;

        }
        return new Point(resX, resY);
    }

}
