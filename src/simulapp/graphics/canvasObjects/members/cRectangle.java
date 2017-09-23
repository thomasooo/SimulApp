package simulapp.graphics.canvasObjects.members;

import simulapp.util.geometry;

import java.awt.*;

/**
 * Created by Tomas Ballon on 28.1.2017.
 */
public class cRectangle extends cShape{

    public cRectangle(String index, String val, int x, int y){
        super(index, val, x, y, cShape.CRECTANGLE);
    }

    public void paintShape(Graphics g){
        g.setColor(color);

        Point startDraw = geometry.getStartXY(X, Y, width, height);

        g.fillRect(startDraw.x, startDraw.y, width, height);

        if(selected){
            g.setColor(Color.RED);
            g.drawRect(startDraw.x, startDraw.y, width, height);
        }

        if(crawlerStart) {
            Point startDravC = geometry.getStartXY(X, Y, (width + 10), (height + 10));
            g.setColor(Color.GREEN);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(startDravC.x, startDravC.y, (width + 10), (height + 10));
        }

        g.setColor(fontColor);

        Font font = new Font("Arial", Font.BOLD, (height / 2));
        FontMetrics metrics = g.getFontMetrics(font);

        int textX = (X - (metrics.stringWidth(value) / 2));
        int textY = startDraw.y + height + metrics.getHeight();

        g.setFont(font);
        g.drawString(value, textX, textY);
    }

}
