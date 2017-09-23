package simulapp.graphics.canvasObjects.members;

import simulapp.util.geometry;

import java.awt.*;

/**
 * Created by Tomas Ballon on 29.1.2017.
 */
public class cCrawlerPoint extends cShape {

    protected cShape startShape;
    protected cShape endShape;
    protected boolean movingEnabled;

    public cCrawlerPoint(String index, cShape start, cShape end){
        super(index, "", start.getX(), start.getY(), cShape.CCRAWLERPOINT);

        width = 15;
        height = 15;

        color = Color.RED;

        startShape = start;
        endShape = end;
        movingEnabled = true;
    }

    public void move(){
        move(1);
    }

    public void move(int speed){
        if(!movingEnabled || inFinish()){
            return;
        }
        Point pos = geometry.getNextPos(X, Y, endShape.getX(), endShape.getY(), speed);
        X = pos.x;
        Y = pos.y;
    }

    public void setMovingEnabled(boolean m){
        movingEnabled = m;
    }

    public boolean inFinish(){
        if(endShape == null){
            return false;
        }
        if(Math.abs(X - endShape.getX()) < 5 && Math.abs(Y - endShape.getY()) < 5){
            return true;
        }
        return false;
    }

    public void setStartShape(cShape start){
        startShape = start;
    }

    public void setEndShape(cShape end) {
        endShape = end;
    }

    public cShape getStartShape(){
        return startShape;
    }

    public cShape getEndShape(){
        return endShape;
    }

    public void paint(Graphics g){
        g.setColor(color);

        Point startDraw = geometry.getStartXY(X, Y, width, height);
        g.fillOval(startDraw.x, startDraw.y, width, height);
    }

}
