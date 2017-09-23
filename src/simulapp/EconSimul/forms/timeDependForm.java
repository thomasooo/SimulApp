package simulapp.EconSimul.forms;

import kotlin.Pair;
import simulapp.util.timeDepender;
import simulapp.util.timeDependerVal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Tomas Ballon on 8.4.2017.
 */
public class timeDependForm {

    private timeDepender<Pair<Integer, Integer>> td;
    private JButton addButton;
    public JPanel tdPanel;
    private JButton ADDNEWButton;
    private JTable table1;
    private JButton CLOSEButton;
    DefaultTableModel model;

    public timeDependForm(timeDepender<Pair<Integer, Integer>> t){
        td = t;

        ArrayList<String> columnNames = new ArrayList<>();
        columnNames.add("From Hour");
        columnNames.add("Minute");
        columnNames.add("Min. value");
        columnNames.add("Max. value");

        model = new DefaultTableModel();

        model.setColumnIdentifiers(columnNames.toArray());

        table1.setModel(model);

        fillTable();

        table1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table1.rowAtPoint(evt.getPoint());
                int col = table1.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                    showAddTimeDependForm(row);
                }
            }
        });
        ADDNEWButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                showAddTimeDependForm();
            }
        });
        CLOSEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Window window = SwingUtilities.getWindowAncestor(tdPanel);
                window.dispose();
            }
        });
    }

    public void fillTable(){
        int i = 0;
        Object[] data = new Object[model.getColumnCount()];

        int rowCount = model.getRowCount();
        for (int ii = rowCount - 1; ii >= 0; ii--) {
            model.removeRow(i);
        }

        i = 0;
        for(timeDependerVal<Pair<Integer, Integer>> v: td.getAll()){
            data[i++] = v.getHour();
            data[i++] = v.getMinute();
            data[i++] = v.getValue().getFirst();
            data[i] = v.getValue().getSecond();
            i = 0;
            model.addRow(data);
        }
        table1.getTableHeader().setReorderingAllowed(false);
    }

    public void showAddTimeDependForm(){
        addTimeDependForm tdForm = new addTimeDependForm(td, model, this);
        showForm(tdForm);
    }

    public void showAddTimeDependForm(int rowNum){
        addTimeDependForm tdForm = new addTimeDependForm(td, model, this, rowNum);
        showForm(tdForm);
    }

    protected void showForm(addTimeDependForm tdForm){
        JFrame tdFrame = new JFrame("Set Value");
        tdFrame.setContentPane(tdForm.adtPanel);
        tdFrame.pack();
       // tdFrame.setSize(600, 300);

        //centrovanie okna na stred - current display
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int winW = tdFrame.getSize().width;
        int winH = tdFrame.getSize().height;
        int xx = (gd.getDisplayMode().getWidth() - winW) / 2;
        int yy = (gd.getDisplayMode().getHeight() - winH) / 2;

        tdFrame.setLocation(xx, yy);
        tdFrame.setVisible(true);
    }
}
