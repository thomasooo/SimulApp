package simulapp.EconSimul.members;

import simulapp.EconSimul.manipulation.simulManipulator;
import simulapp.graphics.canvasObjects.manipulation.cManipulator;
import simulapp.graphics.canvasObjects.members.cText;
import simulapp.main.simulApp;

import java.awt.*;

/**
 * Created by Tomas Ballon on 1.2.2017.
 */
public class cClock extends cText {

    protected int time;
    protected int tickSpeed;
    protected int tickDelay;
    protected int counter;
    protected simulApp app;

    public cClock(String index, int x, int y, simulApp app){
        super(index, "", x, y);

        type = simulManipulator.CCLOCK;
        this.app = app;
        time = 0;
        counter = 0;
        tickDelay = 1;
        tickSpeed = 1;
        setText(getStringTime());
    }

    //tmp hodiny bez vykreslovania sluziace hlavne na vypocty
    public cClock(String index, int time){
        this(index, 0, 0, null);
        setTime(time);
    }

    //zabranenie kliknutia a tym aj manipulacie s objektom
    public boolean isClicked(int x, int y){
        return false;
    }

    public int getTickSpeed(){
        return tickSpeed;
    }

    public String getStringTime(){
        String time = "";
        if(getDay() < 10){
            time += "0";
        }
        time += getDay() + " days ";
        if(getHour() < 10){
            time += "0";
        }
        time += getHour() + " h ";
        if(getMinute() < 10){
            time += "0";
        }
        time += getMinute() + " m ";
        return time;
    }

    public int getDay(){
        return time / 24 / 60;
    }

    public int getHour(){
        return time / 60 % 24;
    }

    public int getMinute(){
        return time % 60;
    }

    public int getTime(){
        return time;
    }

    public void setTime(int minutes){
        time = minutes;
        setText(getStringTime());
    }

    public void setTickSpeed(int speed){
        tickSpeed = speed;
    }

    public void setTickDelay(int delay){
        tickDelay = delay;
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

        //int textX = (X - (metrics.stringWidth(value) / 2));
        int textX = X;
        int textY = (Y + (metrics.getHeight() / 2));

        g.setFont(font);
        g.drawString(value, textX, textY);
    }

    public void tick(){
        if (simulApp.mainGui.getCurrentMode().equals(cManipulator.CRAWLING)) {
            counter++;
            if (counter > tickDelay) {
                time += tickSpeed;
                counter = 0;
                setText(getStringTime());
            }
        } else {
            setText(getStringTime());
            return;
        }
    }

}
