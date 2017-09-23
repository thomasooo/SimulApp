package simulapp.EconSimul.forms;

import simulapp.graphics.canvasObjects.members.cShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import static simulapp.graphics.canvasObjects.members.cShape.*;
import static simulapp.util.math.parseDoubleFromString;

/**
 * Created by Tomas Ballon on 11.2.2017.
 */
public class visitProbForm {
    public JPanel SetProp;
    private cShape shape;
    private HashMap<String, JTextField> fields = new HashMap<>();
    private JButton save;
    private JComboBox<String> distribution;
    private JTextField distParam;
    private JLabel devlam;

    public visitProbForm(cShape sh){
        SetProp.setLayout(new GridLayout(0, 1));
        shape = sh;

        addControlRow(sh);

        for(cShape s: shape.getChildren().values()){
            addRow(s.getIndex(), s.getText(), shape.getVisitProbability(s.getIndex()));
        }

        JPanel row = new JPanel(new FlowLayout());
        save = new JButton("Save");
        row.add(save);
        SetProp.add(row);

        distribution.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                checkControlRowActiveElements();
            }});

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                performAction();
            }
        });

        KeyListener listenEnter = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
                    performAction();
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        };

        for(JTextField f: fields.values()){
            f.addKeyListener(listenEnter);
        }

        checkControlRowActiveElements();
    }

    protected void performAction(){
        for(Map.Entry<String, JTextField> t: fields.entrySet()){
            shape.setVisitProbability(t.getKey(), parseDoubleFromString(t.getValue().getText()));
            shape.setChildDistribution(distribution.getSelectedItem().toString());
//            shape.setParamDistribution(parseDoubleFromString(distParam.getText()));
        }
        //close window
        Window window = SwingUtilities.getWindowAncestor(SetProp);
        window.dispose();
    }

    public void addControlRow(cShape sh){
        distribution = new JComboBox<>();

        JLabel dist = new JLabel("Distribution");
        /*
        distParam = new JTextField();
        devlam = new JLabel("Parameter");
        */
        /*
        distribution.addItem(UNIFORM_DISTRIBUTION);
        distribution.addItem(NORMAL_DISTRIBUTION);
        distribution.addItem(EXPONENTIAL_DISTRIBUTION);
        */
        distribution.addItem(CIRCULAR_DISTRIBUTION);
        distribution.addItem(PERCENTUAL_DISTRIBUTION);
        distribution.addItem(SHORTEST_QUEUE_DISTRIBUTION);

        if(sh != null){
            distribution.setSelectedItem(sh.getChildDistribution());
//            distParam.setText(String.valueOf(sh.getParamDistribution()));
        }

        JPanel row = new JPanel(new GridLayout());
//        JPanel row2 = new JPanel(new GridLayout());

        distribution.setSize(300, 20);
//        distParam.setSize(300, 20);
        row.add(dist);
        row.add(distribution);
//        row2.add(devlam);
//        row2.add(distParam);
        SetProp.add(row);
//        SetProp.add(row2);
        SetProp.revalidate();
        checkControlRowActiveElements();
    }

    public void checkControlRowActiveElements(){
        if(distribution.getSelectedItem().toString().equals(UNIFORM_DISTRIBUTION)){
//            distParam.setText("0");
//            distParam.setEditable(false);
            devlam.setText("---");
        }else{
//            distParam.setEditable(true);
        }
        if(distribution.getSelectedItem().toString().equals(NORMAL_DISTRIBUTION)) {
            devlam.setText("Deviance");
        }
        if(distribution.getSelectedItem().toString().equals(EXPONENTIAL_DISTRIBUTION)) {
            devlam.setText("Lambda");
        }
        if(distribution.getSelectedItem().toString().equals(PERCENTUAL_DISTRIBUTION)) {
            shape.recalculatePercentualValues();
            for (HashMap.Entry<String, JTextField> f: fields.entrySet()){
                f.getValue().setVisible(true);
                f.getValue().setText(String.valueOf(shape.getVisitProbability(f.getKey())));
            }
        }
        if(distribution.getSelectedItem().toString().equals(CIRCULAR_DISTRIBUTION) || distribution.getSelectedItem().toString().equals(SHORTEST_QUEUE_DISTRIBUTION)) {
            for (HashMap.Entry<String, JTextField> f: fields.entrySet()){
                f.getValue().setVisible(false);
            }
        }
    }

    public void addRow(String index, String name, double value){
        JLabel label = new JLabel(name);
        JTextField textField = new JTextField(String.valueOf(value), 20);

        fields.put(index, textField);

        JPanel row = new JPanel(new GridLayout());

        textField.setSize(300, 20);
        row.add(label);
        row.add(textField);
        SetProp.add(row);
        SetProp.revalidate();
    }
}
