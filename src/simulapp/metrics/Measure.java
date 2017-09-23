package simulapp.metrics;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Tomas Ballon on 12.2.2017.
 */
public class Measure {

    public static final String NEW_PERSONS = "New";
    public static final String OUT_PERSONS = "Out";
    public static final String WAITING_PERSONS = "Waiting";
    public static final String EXITED_PERSONS = "Exited"; //osoby uplne opustili graf
    public static final String OUT_OF_TIME_WAITING_PERSONS = "Out waiting";
    public static final String OUT_OF_MONEY_PERSONS = "Out of money";
    public static final String OUT_OF_TIME_PERSONS = "Out of time";
    public static final String ENTERING_PERSONS = "Entering";
    public static final String AVG_CONSUMED_TIME_PER_WAITING_PERSON = "Avg consum. time per waiting";
    public static final String AVG_CONSUMED_TIME_PER_PERSON = "Avg consum. time per person";
    public static final String AVG_CONSUMED_MONEY_PER_PERSON = "Avg consum. money per person";
    public static final String AVG_TIME_PER_PERSON = "Avg time per person";
    public static final String AVG_MONEY_PER_PERSON = "Avg money per person";
    public static final String MAX_IN = "Max in";
    public static final String MAX_WAITING = "Max waiting";
    private int lastTime;

    public LinkedHashMap<String, metricUnit> measures;

    public Measure(){
        measures = new LinkedHashMap<>();
        lastTime = 0;
    }

    public void setMaxGetter(String unitName){
        if(!measures.containsKey(unitName)){
            metricUnit newU = new metricUnit(unitName);
            measures.put(unitName, newU);
        }

        measures.get(unitName).setMaxGetter(true);
    }

    //avg hodnota v danom case
    public void addAvg(String unitName, int time, double valuesSum){
        addAvg(unitName, time, valuesSum, 1);
    }

    //avg hodnota v danom case
    public void addAvg(String unitName, int time, double valuesSum, int valuesCnt){
        if(valuesCnt < 1){
            return;
        }

        if(lastTime < time){
            lastTime = time;
        }

        int isNew = 1;
        if(!measures.containsKey(unitName)){
            metricUnit newU = new metricUnit(unitName);
            measures.put(unitName, newU);
        }

        metricUnit u = measures.get(unitName);
        if(u == null){
            System.out.println("Error getting meter unit.");
            return;
        }

        if(u.hasData(time)){
            isNew = 2;
        }

        u.setAvg(true);

        double value = valuesSum / valuesCnt;
        value = (value + u.get(time)) / isNew;

        if(value < 0){
            u.add(time, 0);
        }else{
            u.add(time, value);
        }
    }

    //inicializacia avg
    public void addAvg(String unitName){
        if(!measures.containsKey(unitName)){
            metricUnit newU = new metricUnit(unitName);
            measures.put(unitName, newU);
        }
    }

    //ak v rovnaky cas existuje hodnota, tak ju incrementne
    public void addInc(String unitName, int time, double value){
        if(!measures.containsKey(unitName)){
            metricUnit newU = new metricUnit(unitName);
            measures.put(unitName, newU);
        }

        if(lastTime < time){
            lastTime = time;
        }

        metricUnit u = measures.get(unitName);
        if(u == null){
            System.out.println("Error getting meter unit.");
            return;
        }

        if(value + u.get(time) < 0){
            u.add(time, 0);
        }else{
            u.add(time, value + u.get(time));
        }
    }

    //ak v rovnaky cas existuje hodnota, tak ju incrementne
    public void addInc(String unitName, int time, double value, boolean minusEnabled){
        if(!minusEnabled){
            addInc(unitName, time, value);
            return;
        }

        if(lastTime < time){
            lastTime = time;
        }

        if(!measures.containsKey(unitName)){
            metricUnit newU = new metricUnit(unitName);
            measures.put(unitName, newU);
        }

        metricUnit u = measures.get(unitName);
        if(u == null){
            System.out.println("Error getting meter unit.");
            return;
        }

        u.add(time, value + u.get(time));
    }

    public void addIfMax(String unitName, int time, double value){
        if(!measures.containsKey(unitName)){
            metricUnit newU = new metricUnit(unitName);
            measures.put(unitName, newU);
        }

        if(lastTime < time){
            lastTime = time;
        }

        metricUnit u = measures.get(unitName);
        if(u == null){
            System.out.println("Error getting meter unit.");
            return;
        }

        if(u.get(time) < value){
            u.add(time, value);
        }
    }

    //ak v rovnaky cas existuje hodnota, prepise
    public void add(String unitName, int time, double value){
        if(!measures.containsKey(unitName)){
            metricUnit newU = new metricUnit(unitName);
            measures.put(unitName, newU);
        }

        if(lastTime < time){
            lastTime = time;
        }

        metricUnit u = measures.get(unitName);
        if(u == null){
            System.out.println("Error getting meter unit.");
            return;
        }

        u.add(time, value);
    }

    public HashMap<String, metricUnit> getAll(){
        return measures;
    }

    public metricUnit getByUnitName(String unitName){
        if(!measures.containsKey(unitName)){
            return null;
        }
        return measures.get(unitName);
    }

    public int getLastTime(){
        return lastTime;
    }

}
