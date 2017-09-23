package simulapp.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static simulapp.util.math.parseIntFromString;

/**
 * Created by Tomas Ballon on 8.4.2017.
 */
public class timeDepender<T> implements Serializable {

    private TreeMap<Integer, TreeMap<Integer, T>> vals = new TreeMap<>();

    public timeDepender(){

    }

    public void addFrom(int hour, int minute, T value){
        if(hour > 23){
            hour = 23;
        }
        if(minute > 59){
            minute = 59;
        }
        if(vals.get(hour) == null){
            vals.put(hour, new TreeMap<Integer, T>());
        }

        TreeMap<Integer, T> minVals = vals.get(hour);
        minVals.put(minute, value);
    }

    public void addFrom(timeDependerVal<T> v){
        addFrom(v.getHour(), v.getMinute(), v.getValue());
    }

    public void addAll(ArrayList<timeDependerVal<T>> v){
        for(timeDependerVal<T> val: v){
            addFrom(val);
        }
    }

    public void clear(){
        vals.clear();
    }

    public void deleteByTime(int hour, int minute){
        if(hour > 23){
            hour = 23;
        }
        if(minute > 59){
            minute = 59;
        }
        if(vals.get(hour) == null){
            return;
        }

        TreeMap<Integer, T> minVals = vals.get(hour);
        minVals.remove(minute);
    }

    public void deleteByTime(String hour, String minute){
        deleteByTime(parseIntFromString(hour, -1), parseIntFromString(minute, -1));
    }

    public T getByTime(int fromHour, int fromMinute){
        if(fromHour > 23){
            fromHour = 23;
        }
        if(fromMinute > 59){
            fromMinute = 59;
        }
        int startHour = fromHour;
        int startMinute = fromMinute;
        boolean exist = false;
        TreeMap<Integer, T> mins = null;
        T val = null;

        for (Map.Entry<Integer, TreeMap<Integer, T>> h: vals.entrySet()){
            if(h.getKey() <= startHour && h.getValue().size() > 0){
                mins = h.getValue();
                exist = true;
            }
        }

        if(!exist){
            return null;
        }

        for (Map.Entry<Integer, T> m: mins.entrySet()){
            if(m.getKey() <= startMinute){
                val = m.getValue();
            }
        }

        return val;
    }

    public ArrayList<timeDependerVal<T>> getAll(){
        ArrayList<timeDependerVal<T>> res = new ArrayList<>();
        for (Map.Entry<Integer, TreeMap<Integer, T>> h: vals.entrySet()){
            for (Map.Entry<Integer, T> m: h.getValue().entrySet()){
                res.add(new timeDependerVal<T>(h.getKey(), m.getKey(), m.getValue()));
            }
        }

        return res;
    }

    public int size(){
        return getAll().size();
    }
}
