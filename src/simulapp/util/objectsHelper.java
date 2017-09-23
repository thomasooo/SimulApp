package simulapp.util;

import java.util.Queue;

/**
 * Created by Tomas Ballon on 5.2.2017.
 */
public final class objectsHelper {

    public static final <T> Queue<T> cloneQueue(Queue<T> qu){
        try {
            Queue<T> p = qu.getClass().newInstance();
            for (T e : qu) {
                p.add(e);
            }
            return p;
        } catch (IllegalAccessException e) {
            System.out.println("IllegalAccessException - cloneQueue");
        } catch (InstantiationException e) {
            System.out.println("InstantiationException - cloneQueue");
        }
        System.out.println("Not cloned - cloneQueue");
        return qu;
    }

}
