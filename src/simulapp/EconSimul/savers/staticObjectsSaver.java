package simulapp.EconSimul.savers;

import simulapp.graphics.canvasObjects.members.cShape;

import java.util.LinkedHashMap;

/**
 * Created by Tomas Ballon on 8.4.2017.
 */
public class staticObjectsSaver extends cShapesSaver implements java.io.Serializable {

    public staticObjectsSaver(LinkedHashMap<String, cShape> shps){
        super(shps);
    }

    public staticObjectsSaver get(){
        return this;
    }

    public LinkedHashMap<String, cShape> load(){
        return super.load();
    }
}
