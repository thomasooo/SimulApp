package simulapp.EconSimul.members;

import kotlin.Pair;
import simulapp.EconSimul.manipulation.simulManipulator;
import simulapp.EconSimul.savers.cProcessingPointSaver;
import simulapp.graphics.canvasObjects.members.cRectangle;
import simulapp.main.simulApp;
import simulapp.metrics.Measure;
import simulapp.util.PriorityStack;
import simulapp.util.geometry;
import simulapp.util.timeDepender;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import static simulapp.EconSimul.manipulation.simulManipulator.clock;
import static simulapp.util.math.randomUniform;
import static simulapp.util.objectsHelper.cloneQueue;

/**
 * Created by Tomas Ballon on 4.2.2017.
 */
public class cProcessingPoint extends cRectangle {

    protected double moneyConsumptionMin;
    protected double moneyConsumptionMax;
    protected int timeConsumptionMin;
    protected int timeConsumptionMax;
    protected int capacity;
    protected int currentTime;

    protected ArrayList<cPerson> personsIn = new ArrayList<>(); //osoby, ktore su vo vnutry
    protected PriorityStack<cPerson> personsWaiting = new PriorityStack<>(); //osoby, ktore cakaju v rade
    protected Queue<cPerson> personsOut = new LinkedList<>(); //osoby, ktore su volne a mozu pokracovat dalej
    protected ArrayList<cPerson> personsOutOfMoney = new ArrayList<>(); //osoby, ktore minuli peniaze
    protected ArrayList<cPerson> personsOutOfTime = new ArrayList<>(); //osoby, ktore minuli cas
    protected ArrayList<cPerson> personsExited = new ArrayList<>(); //osoby, ktore nemaju kam pokracovat - odisli
    public timeDepender<Pair<Integer, Integer>> moneyConsumptionTimeDependent; //personsPerCreating time dependent
    public timeDepender<Pair<Integer, Integer>> timeConsumptionTimeDependent; //personsPerCreating time dependent
    public timeDepender<Pair<Integer, Integer>> capacityTimeDependent; //personsPerCreating time dependent

    public cProcessingPoint(String name, int x, int y){
        super(simulApp.indexes.get(), name, x, y);
        init(name, x, y);
    }

    public cProcessingPoint(String index, String name, int x, int y){
        super(index, name, x, y);
        init(name, x, y);
    }

    private void init(String name, int x, int y){
        type = simulManipulator.CPROCESSINGPOINT;
        moneyConsumptionMin = 0;
        moneyConsumptionMax = 0;
        timeConsumptionMin = 0;
        timeConsumptionMax = 0;
        capacity = 0;
        currentTime = 0;

        moneyConsumptionTimeDependent = new timeDepender<>();
        timeConsumptionTimeDependent = new timeDepender<>();
        capacityTimeDependent = new timeDepender<>();

        //aby sa dodrzalo poradie vypisu
        meter.addInc(Measure.WAITING_PERSONS, currentTime, 0);
        meter.addInc(Measure.ENTERING_PERSONS, currentTime, 0);
        meter.addInc(Measure.OUT_PERSONS, currentTime,  0);
        //meter.addInc(Measure.EXITED_PERSONS, currentTime, 0); //zhoduje sa s out
        meter.addInc(Measure.OUT_OF_TIME_WAITING_PERSONS, currentTime, 0);
        meter.addInc(Measure.OUT_OF_TIME_PERSONS, currentTime, 0);
        meter.addInc(Measure.OUT_OF_MONEY_PERSONS, currentTime, 0);
        meter.addAvg(Measure.AVG_CONSUMED_TIME_PER_WAITING_PERSON);
        meter.addAvg(Measure.AVG_CONSUMED_TIME_PER_PERSON);
        meter.addAvg(Measure.AVG_CONSUMED_MONEY_PER_PERSON);
        meter.setMaxGetter(Measure.MAX_IN);
        meter.setMaxGetter(Measure.MAX_WAITING);
    }

    public void setCapacity(int cap){
        capacity = cap;
    }

    public int getCapacity(){
        return capacity;
    }

    public int getWaintingPersonsCount(){
        return personsWaiting.size();
    }

    public void setTimeConsumption(int cons){
        timeConsumptionMin = timeConsumptionMax = cons;
    }

    public void setTimeConsumption(int min, int max){
        timeConsumptionMin = min;
        timeConsumptionMax = max;
    }

    public void setMoneyConsumption(double cons){
        moneyConsumptionMin = moneyConsumptionMax = cons;
    }

    public void setMoneyConsumption(double min, double max){
        moneyConsumptionMin = min;
        moneyConsumptionMax = max;
    }

    public double getMoneyConsumptionMin(){
        return moneyConsumptionMin;
    }

    public double getMoneyConsumptionMax(){
        return moneyConsumptionMax;
    }

    public int getTimeConsumptionMin(){
        return timeConsumptionMin;
    }

    public int getTimeConsumptionMax(){
        return timeConsumptionMax;
    }

    //da osoby do processing pointu, alebo do jeho cakacieho queue
    public void addPerson(cPerson person){
        person.setEndShape(null);

        int enteringPersons = 0;
        int waitingPersons = 0;
        int totalTime = 0;
        double totalMoney = 0;

        int capacityNow = capacity;
        Pair<Integer, Integer> minmaxCreating = capacityTimeDependent.getByTime(clock.getHour(), clock.getMinute());
        if(minmaxCreating != null){
            capacityNow = randomUniform(minmaxCreating.getFirst(), minmaxCreating.getSecond());
        }

        if(personsIn.size() < capacityNow){
            enteringPersons++;

            int timeConsumption = randomUniform(timeConsumptionMin, timeConsumptionMax);
            double moneyConsumption = randomUniform(moneyConsumptionMin, moneyConsumptionMax);

            Pair<Integer, Integer> minmax1Creating = timeConsumptionTimeDependent.getByTime(clock.getHour(), clock.getMinute());
            if(minmax1Creating != null){
                timeConsumption = randomUniform(minmax1Creating.getFirst(), minmax1Creating.getSecond());
            }

            Pair<Integer, Integer> minmax2Creating = moneyConsumptionTimeDependent.getByTime(clock.getHour(), clock.getMinute());
            if(minmax2Creating != null){
                moneyConsumption = randomUniform(minmax2Creating.getFirst(), minmax2Creating.getSecond());
            }

            totalTime += timeConsumption;
            totalMoney += moneyConsumption;

            if(!person.moneyConsoumate(moneyConsumption)){
                personsOutOfMoney.add(person);
                meter.addInc(Measure.OUT_OF_MONEY_PERSONS, currentTime, 1);
                return;
            }

            if(!person.timeConsoumate(timeConsumption)){
                personsOutOfTime.add(person);
                meter.addInc(Measure.OUT_OF_TIME_PERSONS, currentTime, 1);
                return;
            }

            person.setNextAction(currentTime + timeConsumption, cPerson.ON_ROUTE_STATE);
            person.setInProcessingPointState();

            personsIn.add(person);
        }else{
            person.setWaitingState(currentTime);

            personsWaiting.add(person, person.getPriority());
            waitingPersons++;
        }

        int consumedTimeMeter = 0;
        double consumedMoneyMeter = 0;
        if(enteringPersons > 0){
            consumedTimeMeter = Math.round(totalTime / enteringPersons);
            consumedMoneyMeter = totalMoney / enteringPersons;
            meter.addAvg(Measure.AVG_CONSUMED_TIME_PER_PERSON, currentTime, consumedTimeMeter);
            meter.addAvg(Measure.AVG_CONSUMED_MONEY_PER_PERSON, currentTime, consumedMoneyMeter);
        }

        meter.addInc(Measure.WAITING_PERSONS, currentTime, waitingPersons);
        meter.addInc(Measure.ENTERING_PERSONS, currentTime, enteringPersons);
    }

    public void exitPersons(Queue<cPerson> persons) {
        for(cPerson p: persons){
            exitPerson(p);
        }
    }

    public void exitPersons(ArrayList<cPerson> persons) {
        for(cPerson p: persons){
            exitPerson(p);
        }
    }

    public void exitPerson(cPerson p){
        p.setExitedState();
        personsExited.add(p);

        //meter.addInc(Measure.EXITED_PERSONS, currentTime, 1);
    }

    //vsetky maju este IN_PROCESSING_POINT_STATE
    public Queue<cPerson> getPersons(){
        Queue<cPerson> p = cloneQueue(personsOut);
        personsOut.clear();
        return p;
    }

    public void paint(Graphics g){
        super.paint(g);

        int out = (int) meter.getByUnitName(Measure.OUT_PERSONS).getTotal();
        int outUnits = (int) (meter.getByUnitName(Measure.OUT_OF_MONEY_PERSONS).getTotal() + meter.getByUnitName(Measure.OUT_OF_TIME_PERSONS).getTotal());
        String str = "w:" + String.valueOf(personsWaiting.size()) + "  in:" + String.valueOf(personsIn.size());
        if(out > 0){
            str +=  "  o:" + String.valueOf(out);
        }
        if(outUnits > 0){
            str +=  "  u:" + String.valueOf(outUnits);
        }

        Point startDraw = geometry.getStartXY(X, Y, width, height);

        Font font = new Font("Arial", Font.PLAIN, 12);
        FontMetrics metrics = g.getFontMetrics(font);

        int textX = (X - (metrics.stringWidth(str) / 2));
        int textY = startDraw.y + height + (metrics.getHeight() * 2) + 5;

        g.setFont(font);
        g.drawString(str, textX, textY);
    }

    public void tick(int currTime) {
        currentTime = currTime;
        int pIn = 0;
        int pOut = 0;

        //process vsetkych in
        for (Iterator<cPerson> iterator = personsIn.iterator(); iterator.hasNext(); ) {
            cPerson person = iterator.next();
            if (person.isProcessed()) {
                if (person.getCurrentState() != cPerson.OUT_OF_AVAILABLE_UNITS_STATE) {
                    personsOut.add(person);
                    pOut++;
                }
//TODO - out of TIME?
                iterator.remove();
            }
        }

        int capacityNow = capacity;
        Pair<Integer, Integer> minmaxCreating = capacityTimeDependent.getByTime(clock.getHour(), clock.getMinute());
        if(minmaxCreating != null){
            capacityNow = randomUniform(minmaxCreating.getFirst(), minmaxCreating.getSecond());
        }

        int pEndWaiting;
        pEndWaiting = 0;
        //ak je volna kapacita a caka niekto, pustime ho dnu
        while (personsIn.size() < capacityNow) {
            cPerson person = personsWaiting.poll();
            if (person == null) {
                break;
            }
            meter.addAvg(Measure.AVG_CONSUMED_TIME_PER_WAITING_PERSON, currentTime, person.endWaitingGetConsumedTime(currentTime));
            if(!person.endWaiting(currentTime)){
                personsOutOfTime.add(person);
                meter.addInc(Measure.OUT_OF_TIME_WAITING_PERSONS, currentTime, 1);
                personsOut.add(person);
                pEndWaiting++;
                continue;
            }
            pIn++;
            addPerson(person);
        }

        meter.addInc(Measure.OUT_PERSONS, currentTime, pOut);
        meter.addInc(Measure.WAITING_PERSONS, currentTime, -(pIn + pEndWaiting));
        meter.addIfMax(Measure.MAX_WAITING, currentTime, personsWaiting.size());
        meter.addIfMax(Measure.MAX_IN, currentTime, personsIn.size());
    }

    public cProcessingPointSaver save(){
        return new cProcessingPointSaver(this);
    }
}
