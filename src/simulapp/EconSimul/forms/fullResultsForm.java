package simulapp.EconSimul.forms;

import simulapp.EconSimul.manipulation.simulManipulator;
import simulapp.EconSimul.members.cClock;
import simulapp.graphics.canvasObjects.members.cShape;
import simulapp.main.simulApp;
import simulapp.metrics.Measure;
import simulapp.metrics.metricUnit;
import simulapp.util.TableHeaderToolTips;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tomas Ballon on 5.3.2017.
 */
public class fullResultsForm {
    public JPanel resultsPanel;

    private simulApp app;
    private boolean isEmpty;
    private JTable table1;
    private JTable table2;
    private int maxTime;

    protected JFrame gfwindow;
    protected graphDataForm gf;

    public fullResultsForm(simulApp a){
        app = a;
        isEmpty = true;
        table1 = null;
        table2 = null;
        maxTime = 0;
        resultsPanel.setLayout(new GridLayout(2, 1));

        HashMap<String, cShape> shapes = simulApp.manipulator.shapes.getShapes();
        boolean show = false;

        ArrayList<String> columnNames = new ArrayList<>();
        ArrayList<ArrayList<Object>> data = new ArrayList<>();
        HashMap<String, Integer> colWidths = new HashMap<>();
        //prve vypiseme startpointy
        for(cShape s: shapes.values()){
            if(s.getType().equals(simulManipulator.CSTARTENDPOINT)){
                if(maxTime < s.getMeter().getLastTime()){
                    maxTime = s.getMeter().getLastTime();
                }
                if(columnNames.size() == 0){
                    //header
                    columnNames.add("Index");
                    colWidths.put("Index", 60);
                    columnNames.add("Label");
                    colWidths.put("Label", 60);
                    for(Map.Entry<String, metricUnit> m: s.getMeter().getAll().entrySet()){
                        columnNames.add(m.getKey());
                    }
                    colWidths.put(Measure.NEW_PERSONS, 100);
                }
                ArrayList<Object> al = new ArrayList<>();
                al.add(s.getIndex().replace("auto_", ""));
                al.add(s.getText());
                for(Map.Entry<String, metricUnit> m: s.getMeter().getAll().entrySet()){
                    al.add(m.getValue().getTotal());

                }
                data.add(al);
            }
        }

        if(columnNames.size() > 0){
            isEmpty = false;
            table1 = addTable(columnNames, data);
            //setColsWidth(table1, colWidths);
        }

        columnNames.clear();
        data.clear();
        colWidths.clear();
        //druhe vypiseme processingcenters
        for(cShape s: shapes.values()){
            if(s.getType().equals(simulManipulator.CPROCESSINGPOINT)){
                if(columnNames.size() == 0){
                    //header
                    columnNames.add("Index");
                    colWidths.put("Index", 60);
                    columnNames.add("Label");
                    colWidths.put("Label", 60);
                    for(Map.Entry<String, metricUnit> m: s.getMeter().getAll().entrySet()){
                        columnNames.add(m.getKey());
                    }
                    colWidths.put(Measure.WAITING_PERSONS, 100);
                    colWidths.put(Measure.ENTERING_PERSONS, 100);
                    colWidths.put(Measure.OUT_PERSONS, 100);
                    colWidths.put(Measure.OUT_OF_TIME_PERSONS, 150);
                    colWidths.put(Measure.OUT_OF_TIME_WAITING_PERSONS, 100);
                }
                ArrayList<Object> al = new ArrayList<>();
                al.add(s.getIndex().replace("auto_", ""));
                al.add(s.getText());
                for(Map.Entry<String, metricUnit> m: s.getMeter().getAll().entrySet()){
                    al.add(m.getValue().getTotal());
                }
                data.add(al);
            }
        }

        if(columnNames.size() > 0){
            isEmpty = false;
            table2 = addTable(columnNames, data);
            //setColsWidth(table2, colWidths);
        }

        if(table1 != null){
            table1.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = table1.rowAtPoint(evt.getPoint());
                    int col = table1.columnAtPoint(evt.getPoint());
                    if (row >= 0 && col >= 0) {
                        showGraphByIndex("auto_" + table1.getValueAt(row, 0));
                    }
                }
            });
        }

        if(table2 != null){
            table2.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = table2.rowAtPoint(evt.getPoint());
                    int col = table2.columnAtPoint(evt.getPoint());
                    if (row >= 0 && col >= 0) {
                        showGraphByIndex("auto_" + table2.getValueAt(row, 0));
                    }
                }
            });
        }

    }

    public JTable addTable(ArrayList <String> cn, ArrayList<ArrayList<Object>> d){
        if(d.size() == 0){
            return null;
        }

        int i = 0;
        int j = 0;
        String[] columnNames = new String[cn.size()];
        Object[][] data = new Object[d.size()][d.get(0).size()];

        for(String c: cn){
            columnNames[i++] = c;
        }

        i = 0;
        for(ArrayList<Object> o: d){
            for(Object ob: o){
                //formatovanie vzsledkov - 2 desatinne double / int / string
                if(ob instanceof Double) {
                    double tmp = (double) Math.round((double) ob * 100) / 100; //zaokruhlenie na 2 desatinne miesta
                    if(Math.ceil(tmp) == Math.floor(tmp)){
                        data[i][j++] =  (int) tmp;
                    }else {
                        data[i][j++] = tmp;
                    }
                }else{
                    data[i][j++] = ob;
                }
            }
            i++;
            j = 0;
        }

        JPanel p = new JPanel();
        p.setLayout(new GridLayout());
        JTable table = new JTable(data, columnNames);
        JScrollPane tableContainer = new JScrollPane(table);
        p.add(tableContainer, BorderLayout.CENTER);
        resultsPanel.add(p);
        table.getTableHeader().setReorderingAllowed(false);

        TableHeaderToolTips tips = new TableHeaderToolTips();
        for (int c = 0; c < table.getColumnCount(); c++) {
            TableColumn col = table.getColumnModel().getColumn(c);
            tips.setToolTip(col, col.getHeaderValue().toString());
        }
        table.getTableHeader().addMouseMotionListener(tips);

        return table;
    }

    public void setColsWidth(JTable tbl, HashMap<String, Integer> widths){
        for(HashMap.Entry<String, Integer> e: widths.entrySet()){
            tbl.getColumn(e.getKey()).setMaxWidth(e.getValue());
        }
    }

    public void showGraphByIndex(String index){
        cShape sh = simulApp.manipulator.shapes.getShapeByIndex(index);

        if(sh == null){
            return;
        }

        gf = new graphDataForm(sh.getMeter().getAll(), sh.getText(), sh.getMeter().getLastTime());

        gfwindow = new JFrame();
        gfwindow.setContentPane(gf.panel);
        gfwindow.pack();

        //centrovanie okna na stred - current display
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int winW = gfwindow.getSize().width;
        int winH = gfwindow.getSize().height;
        int xx = (gd.getDisplayMode().getWidth() - winW) / 2;
        int yy = (gd.getDisplayMode().getHeight() - winH) / 2;

        gfwindow.setLocation(xx, yy);
        gfwindow.setVisible(true);
    }

    //true, ak neobsahuje ziadne data
    public boolean isEmpty(){
        return isEmpty;
    }

    //v ktorom case boli vygenerovane resulty
    public String getTimeTo(){
        cClock tmpClock = new cClock("tmp", maxTime);
        return tmpClock.getStringTime();
    }

}