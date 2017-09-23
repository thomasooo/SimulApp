package simulapp.graphics.canvasObjects.members;

import java.awt.*;

/**
 * Created by Tomas Ballon on 22.1.2017.
 */
public class cText extends cShape {

    public cText(String index, String val, int x, int y){
        super(index, val, x, y, cShape.CTEXT);
    }

    public boolean isClicked(int x, int y){
        Font font = new Font("Arial", Font.BOLD, (15));
        Canvas c = new Canvas();
        FontMetrics metrics = c.getFontMetrics(font);

        int minX = (X - (metrics.stringWidth(value) / 2));
        int maxX = minX + metrics.stringWidth(value);
        int minY = (Y - metrics.getHeight());
        int maxY = (Y + (metrics.getHeight() / 2));

        if(x >= minX && x <= maxX && y >= minY && y <= maxY){
            return true;
        }
        return false;
    }

    public void paint(Graphics g){
        g.setColor(fontColor);
        if(crawlerStart) {
            g.setColor(Color.GREEN);
        }
        if(selected){
            g.setColor(Color.RED);
        }

        Font font = new Font("Arial", Font.BOLD, (15));
        FontMetrics metrics = g.getFontMetrics(font);

        int textX = (X - (metrics.stringWidth(value) / 2));
        int textY = (Y + (metrics.getHeight() / 2));

        g.setFont(font);
        g.drawString(value, textX, textY);
    }

}
