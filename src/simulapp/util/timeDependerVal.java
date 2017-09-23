package simulapp.util;

import java.io.Serializable;

/**
 * Created by Tomas Ballon on 8.4.2017.
 */
public class timeDependerVal<T> implements Serializable {

    private int hour;
    private int minute;
    private T value;

    public timeDependerVal(int h, int m, T v){
        hour = h;
        minute = m;
        value = v;
    }

    public int getHour(){
        return hour;
    }

    public int getMinute(){
        return minute;
    }

    public T getValue(){
        return value;
    }

    public void setHour(int h){
        hour = h;
    }

    public void setMinute(int m){
        minute = m;
    }

    public void setValue(T v){
        value = v;
    }
}
