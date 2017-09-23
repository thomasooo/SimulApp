package simulapp.util;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Tomas Ballon on 4.2.2017.
 */
public class indexManager {

    protected Queue<String> indexesToReuse = new LinkedList<>();
    protected int postfix;

    public indexManager(){
        postfix = 1;
    }

    public String get(){
        String s = String.valueOf(postfix);
        if(indexesToReuse.size() > 0){
            return indexesToReuse.poll();
        }
        s = "auto_" + s;
        postfix++;
        return s;
    }

    public void addToReuse(String s){
        indexesToReuse.add(s);
    }

    public int getPostfix(){
        return postfix;
    }

    public void setPostfix(int p){
        postfix = p;
        indexesToReuse.clear();
    }

}
