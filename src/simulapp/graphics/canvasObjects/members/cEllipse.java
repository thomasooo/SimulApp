package simulapp.graphics.canvasObjects.members;

import simulapp.util.geometry;

import java.awt.*;

/**
 * Created by Tomas Ballon on 28.1.2017.
 */
public class cEllipse extends cShape {

    public cEllipse(String index, String val, int x, int y){
        super(index, val, x, y, cShape.CELLIPSE);
    }

    public void paintShape(Graphics g){
        g.setColor(color);

        Point startDraw = geometry.getStartXY(X, Y, width, height);

        g.fillOval(startDraw.x, startDraw.y, width, height);

        if(selected){
            g.setColor(Color.RED);
            g.drawOval(startDraw.x, startDraw.y, width, height);
        }

        if(crawlerStart) {
            Point startDravC = geometry.getStartXY(X, Y, (width + 10), (height + 10));
            g.setColor(Color.GREEN);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(startDravC.x, startDravC.y, (width + 10), (height + 10));
        }

        g.setColor(fontColor);

        Font font = new Font("Arial", Font.BOLD, (height / 2));
        FontMetrics metrics = g.getFontMetrics(font);

        int textX = (X - (metrics.stringWidth(value) / 2));
        int textY = startDraw.y + height + metrics.getHeight();

        g.setFont(font);
        g.drawString(value, textX, textY);
    }

    public void paintImage(Graphics g){
        g.setColor(color);

        Point startDraw = geometry.getStartXY(X, Y, width, height);

        g.drawImage(resizedImage, startDraw.x, startDraw.y, null);

        if(crawlerStart) {
            Point startDrawC = geometry.getStartXY(X, Y, (width + 10), (height + 10));
            g.setColor(Color.GREEN);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(startDrawC.x, startDrawC.y, (width + 10), (height + 10));
        }

        if(selected){
            Point startDrawC2 = geometry.getStartXY(X, Y, (width + 15), (height + 15));
            g.setColor(Color.RED);
            g.drawOval(startDrawC2.x, startDrawC2.y, width + 15, height + 15);
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
