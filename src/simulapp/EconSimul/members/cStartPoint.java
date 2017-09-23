package simulapp.EconSimul.members;

import kotlin.Pair;
import simulapp.EconSimul.manipulation.simulManipulator;
import simulapp.EconSimul.savers.cStartPointSaver;
import simulapp.graphics.canvasObjects.manipulation.cManipulator;
import simulapp.graphics.canvasObjects.members.cEllipse;
import simulapp.main.simulApp;
import simulapp.metrics.Measure;
import simulapp.util.geometry;
import simulapp.util.timeDepender;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static simulapp.EconSimul.manipulation.simulManipulator.clock;
import static simulapp.util.math.randomUniform;

/**
 * Created by Tomas Ballon on 4.2.2017.
 */
public class cStartPoint extends cEllipse {

    protected int personsPerCreatingMin;
    protected int personsPerCreatingMax;
    protected int personsPerCreatingNow;
    protected int countTicksToCreatingMin;
    protected int countTicksToCreatingMax;
    protected int countTicksToCreatingNow;
    protected double personMoneyAvailableMax;
    protected double personMoneyAvailableMin;
    protected int personTimeAvailableMax;
    protected int personTimeAvailableMin;
    protected int personsPriority;
    protected int currentTime;
    protected Color personsColor;
    public timeDepender<Pair<Integer, Integer>> personsPerCreatingTimeDependent; //personsPerCreating time dependent
    public timeDepender<Pair<Integer, Integer>> countTicksToCreatingTimeDependent; //countTicksToCreating time dependent

    protected int currentTick;

    protected ArrayList<cPerson> persons = new ArrayList<>();

    //tu sa vytvaraju cPersons
    public cStartPoint(String name, int x, int y){
        super(simulApp.indexes.get(), name, x, y);
        init(name, x, y);
    }

    public cStartPoint(String index, String name, int x, int y){
        super(index, name, x, y);
        init(name, x, y);
    }

    private void init(String name, int x, int y){
        crawlerStart = true;
        type = simulManipulator.CSTARTENDPOINT;

        setPersonsPerCreating(1, 5);
        setCountTicksToCreating(100, 200);
        setPersonMoneyAvailable(100, 1000);
        setPersonTimeAvailable(1000, 1000000);
        currentTick = 0;
        personsPriority = 0;

        //aby sa dodrzalo poradie vypisu
        meter.addInc(Measure.NEW_PERSONS, currentTime, 0);
        meter.addAvg(Measure.AVG_TIME_PER_PERSON);
        meter.addAvg(Measure.AVG_MONEY_PER_PERSON);

        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        personsColor = new Color(r, g, b);
        personsPerCreatingTimeDependent = new timeDepender<Pair<Integer, Integer>>();
        countTicksToCreatingTimeDependent = new timeDepender<Pair<Integer, Integer>>();

        setColor(personsColor);
    }

    public int getPersonsPerCreatingMin(){
        return personsPerCreatingMin;
    }

    public int getPersonsPerCreatingMax(){
        return personsPerCreatingMax;
    }

    public int getCountTicksToCreatingMin(){
        return countTicksToCreatingMin;
    }

    public int getCountTicksToCreatingMax(){
        return countTicksToCreatingMax;
    }

    public int getPersonTimeAvailableMin(){
        return personTimeAvailableMin;
    }

    public int getPersonTimeAvailableMax(){
        return personTimeAvailableMax;
    }

    public double getPersonMoneyAvailableMin(){
        return personMoneyAvailableMin;
    }

    public double getPersonMoneyAvailableMax(){
        return personMoneyAvailableMax;
    }

    public void setPersonsPerCreating(int min, int max){
        personsPerCreatingMax = max;
        personsPerCreatingMin = min;
        personsPerCreatingNow = randomUniform(personsPerCreatingMin, personsPerCreatingMax);
    }

    public void setCountTicksToCreating(int min, int max){
        countTicksToCreatingMin = min;
        countTicksToCreatingMax = max;
        countTicksToCreatingNow = randomUniform(countTicksToCreatingMin, countTicksToCreatingMax);
    }

    public void setPersonMoneyAvailable(double min, double max){
        personMoneyAvailableMax = max;
        personMoneyAvailableMin = min;
    }

    public void setPersonTimeAvailable(int min, int max){
        personTimeAvailableMax = max;
        personTimeAvailableMin = min;
    }

    public void setPersonsPriority(int priority){
        personsPriority = priority;
    }

    public int getPersonsPriority(){
        return personsPriority;
    }

    public Color getPersonsColor(){
        return personsColor;
    }

    public void setPersonsColor(Color c){
        personsColor = c;
    }

    //vrati vytvorene osoby a vymaze vnutorne
    public ArrayList<cPerson> getPersons(){
        ArrayList<cPerson> p = (ArrayList<cPerson>) persons.clone();
        persons.clear();
        return p;
    }

    public void paint(Graphics g){
        super.paint(g);

        int out = (int) meter.getByUnitName(Measure.NEW_PERSONS).getTotal();
        String str = "n:" + String.valueOf(out);

        Point startDraw = geometry.getStartXY(X, Y, width, height);

        Font font = new Font("Arial", Font.PLAIN, 12);
        FontMetrics metrics = g.getFontMetrics(font);

        int textX = (X - (metrics.stringWidth(str) / 2));
        int textY = startDraw.y + height + (metrics.getHeight() * 2) + 5;

        g.setFont(font);
        g.drawString(str, textX, textY);
    }

    public void tick(int time){
        currentTime = time;
        if(!simulApp.mainGui.getCurrentMode().equals(cManipulator.CRAWLING)){
            return;
        }

        if (currentTick >= countTicksToCreatingNow || currentTime == 0) {
            int totalTime = 0;
            double totalMoney = 0;

            Pair<Integer, Integer> minmaxCreating = personsPerCreatingTimeDependent.getByTime(clock.getHour(), clock.getMinute());
            if(minmaxCreating != null){
                personsPerCreatingNow = randomUniform(minmaxCreating.getFirst(), minmaxCreating.getSecond());
            }

            for (int i = 0; i < personsPerCreatingNow; i++) {
                double personMoneyAvailable = randomUniform(personMoneyAvailableMin, personMoneyAvailableMax);
                int personTimeAvailable = randomUniform(personTimeAvailableMin, personTimeAvailableMax);
                totalMoney += personMoneyAvailable;
                totalTime += personTimeAvailable;

                cPerson person = new cPerson(this);

                person.setMoneyAvailable(personMoneyAvailable);
                person.setTimeAvailable(personTimeAvailable);
                person.setPriority(personsPriority);
                person.setMainColor(personsColor);
                person.setParentPoint(this);
                persons.add(person);
            }

            int averageTimeMeter = 0;
            double averageMoneyMeter = 0;
            if (personsPerCreatingNow > 0) {
                averageTimeMeter = Math.round(totalTime / personsPerCreatingNow);
                averageMoneyMeter = totalMoney / personsPerCreatingNow;
                meter.addAvg(Measure.AVG_TIME_PER_PERSON, currentTime, averageTimeMeter);
                meter.addAvg(Measure.AVG_MONEY_PER_PERSON, currentTime, averageMoneyMeter);

            }

            meter.addInc(Measure.NEW_PERSONS, currentTime, personsPerCreatingNow);

            personsPerCreatingNow = randomUniform(personsPerCreatingMin, personsPerCreatingMax);
            countTicksToCreatingNow = randomUniform(countTicksToCreatingMin, countTicksToCreatingMax);

            Pair<Integer, Integer> minmaxTime = countTicksToCreatingTimeDependent.getByTime(clock.getHour(), clock.getMinute());
            if(minmaxTime != null){
                countTicksToCreatingNow = randomUniform(minmaxTime.getFirst(), minmaxTime.getSecond());
            }

            currentTick = 0;
        }
        currentTick++;
    }

    public cStartPointSaver save(){
        return new cStartPointSaver(this);
    }

}
