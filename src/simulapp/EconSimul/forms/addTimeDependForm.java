package simulapp.EconSimul.forms;

import kotlin.Pair;
import simulapp.util.timeDepender;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static simulapp.util.math.parseIntFromString;

/**
 * Created by Tomas Ballon on 8.4.2017.
 */
public class addTimeDependForm {
    private JTextField hourTextField;
    private JTextField minTextField;
    private JTextField minuteTextField;
    private JTextField maxTextField;
    private JButton SAVEButton;
    private JButton REMOVEButton;
    public JPanel adtPanel;

    private timeDepender<Pair<Integer, Integer>> td;
    private timeDependForm tableForm;
    private DefaultTableModel tm;
    private boolean adding;
    private Integer rowNum;

    public addTimeDependForm(){
        adding = true;
        rowNum = null;
        td = null;
        tableForm = null;

        REMOVEButton.setEnabled(false);

        KeyListener listen = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
                    saveAction();
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        };

        hourTextField.addKeyListener(listen);
        minuteTextField.addKeyListener(listen);
        minTextField.addKeyListener(listen);
        maxTextField.addKeyListener(listen);

        REMOVEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!adding) {
                    td.deleteByTime(tm.getValueAt(rowNum, 0).toString(), tm.getValueAt(rowNum, 1).toString());
                }
                tableForm.fillTable();
                Window window = SwingUtilities.getWindowAncestor(adtPanel);
                window.dispose();
            }
        });
        SAVEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveAction();
            }
        });
    }

    public addTimeDependForm(timeDepender<Pair<Integer, Integer>> d, DefaultTableModel m, timeDependForm f){
        this();
        td = d;
        tm = m;
        tableForm = f;
    }

    public addTimeDependForm(timeDepender<Pair<Integer, Integer>> d, DefaultTableModel m, timeDependForm f, int rn){
        this(d, m, f);
        adding = false;
        rowNum = rn;
        REMOVEButton.setEnabled(true);

        hourTextField.setText(m.getValueAt(rn, 0).toString());
        minuteTextField.setText(m.getValueAt(rn, 1).toString());
        minTextField.setText(m.getValueAt(rn, 2).toString());
        maxTextField.setText(m.getValueAt(rn, 3).toString());
    }

    private void saveAction(){
        if(!adding) {
            td.deleteByTime(tm.getValueAt(rowNum, 0).toString(), tm.getValueAt(rowNum, 1).toString());
        }

        int min = 0;
        int max = 0;
        if(parseIntFromString(minTextField.getText()) <= parseIntFromString(maxTextField.getText())){
            min = parseIntFromString(minTextField.getText());
            max = parseIntFromString(maxTextField.getText());
        }else{
            max = parseIntFromString(minTextField.getText());
            min = parseIntFromString(maxTextField.getText());
        }

        td.addFrom(parseIntFromString(hourTextField.getText()), parseIntFromString(minuteTextField.getText()), new Pair<Integer, Integer>(min, max));
        tableForm.fillTable();
        Window window = SwingUtilities.getWindowAncestor(adtPanel);
        window.dispose();
    }
}
