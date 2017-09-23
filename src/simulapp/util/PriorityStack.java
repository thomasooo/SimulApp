package simulapp.util;

import java.util.*;

/**
 * Created by Tomas Ballon on 3.4.2017.
 */
public class PriorityStack <T> {

    private TreeMap<Integer, Queue<T>> multiStack;

    public <T> PriorityStack(){
        multiStack = new TreeMap<>();
    }

    public void add(T item){
        add(item, 0);
    }

    public void add(T item, int priority){
        Queue<T> subStack = multiStack.get(priority);
        if(subStack == null){
            subStack = new LinkedList<T>();
            multiStack.put(priority, subStack);
        }
        subStack.add(item);
    }

    public int size(){
        int size = 0;

        for(Map.Entry<Integer, Queue<T>> item: multiStack.entrySet()){
            size += item.getValue().size();
        }

        return size;
    }

    public T poll(){
        int greathestKey = -1;
        for(Map.Entry<Integer, Queue<T>> item: multiStack.entrySet()){
            if(item.getKey() > greathestKey && item.getValue().size() > 0){
                greathestKey = item.getKey();
            }
        }
        Queue<T> tmpItem = multiStack.get(greathestKey);
        if(tmpItem == null){
            return null;
        }
        return tmpItem.poll();
    }
}
