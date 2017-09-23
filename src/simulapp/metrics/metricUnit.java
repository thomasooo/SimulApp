package simulapp.metrics;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by Tomas Ballon on 12.2.2017.
 */
public class metricUnit {

    protected String unitName;
    protected ConcurrentSkipListMap<Integer, Double> timeValue; //kluc je cas, value je value
    protected boolean isAvg;
    protected boolean maxGetter;

    public metricUnit(String name){
        unitName = name;
        timeValue = new ConcurrentSkipListMap<>();
        isAvg = false;
        maxGetter = false;
    }

    public void setAvg(boolean avg){
        isAvg = avg;
    }

    public void setMaxGetter(boolean mg){
        maxGetter = mg;
    }

    public void add(int time, double val){
        timeValue.put(time, val);
    }

    public double get(int time){
        if(!timeValue.containsKey(time)){
            return 0;
        }
        return timeValue.get(time);
    }

    public boolean hasData(int time){
        if(!timeValue.containsKey(time)){
            return false;
        }
        return true;
    }

    //vrati kompletny sucet
    public double getTotal(){
        if(isAvg){
            return getTotalAvg();
        }
        if(maxGetter){
            return getTotalMax();
        }
        double res = 0;
        try {
            for (Double tv : timeValue.values()) {
                res += tv;
            }
        }catch (ConcurrentModificationException e){
            System.out.println("getCountInTimeInterval concurrent");
        }
        return res;
    }

    public double getTotalAvg(){
        double r = 0;
        try{
            for(Double tv: timeValue.values()){
                r += tv;
            }
        if(timeValue.size() == 0){
            return 0;
        }
        }catch (ConcurrentModificationException e){
            System.out.println("getCountInTimeInterval concurrent");
            return 0;
        }
        return r / timeValue.size();
    }

    public double getTotalMax(){
        double r = 0;
        try{
            for(Double tv: timeValue.values()){
                if(r < tv){
                    r = tv;
                }
            }
            if(timeValue.size() == 0){
                return 0;
            }
        }catch (ConcurrentModificationException e){
            System.out.println("getCountInTimeInterval concurrent");
            return 0;
        }
        return r;
    }

    //zoskupi vysledky na zaklade casoveho intervalu - kluc e cas, value je pocet hodnot
    public HashMap<Integer, Double> getCountsByInterval(int from, int to, int timeInterval){
        HashMap<Integer, Double> res = new HashMap<>();

        int currentFrom = from;
        for(int i = (from + timeInterval); i <= to; i += timeInterval){
            if(maxGetter) {
                res.put(i, getCountInTimeIntervalMaxGetter(currentFrom, i - 1));
            }else{
                res.put(i, getCountInTimeInterval(currentFrom, i - 1));
            }
            currentFrom = i;
        }

        return res;
    }

    protected double getCountInTimeInterval(int from, int to){
        double res = 0;
        int cnt = 0;
        try {
            for (HashMap.Entry<Integer, Double> tv : timeValue.entrySet()) {
                if (tv.getKey() >= from && tv.getKey() <= to) {
                    res += tv.getValue();
                    cnt++;
                }
            }
            if (isAvg && cnt != 0) {
                return (res / cnt);
            }
        }catch (ConcurrentModificationException e){
            System.out.println("getCountInTimeInterval concurrent");
        }
        return res;
    }

    protected double getCountInTimeIntervalMaxGetter(int from, int to){
        double res = 0;
        try {
            for (HashMap.Entry<Integer, Double> tv : timeValue.entrySet()) {
                if (tv.getKey() >= from && tv.getKey() <= to) {
                    if(res < tv.getValue()) {
                        res = tv.getValue();
                    }
                }
            }
        }catch (ConcurrentModificationException e){
            System.out.println("getCountInTimeInterval concurrent");
        }
        return res;
    }

}
