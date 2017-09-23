package simulapp.main;

import simulapp.EconSimul.forms.fullResultsForm;
import simulapp.graphics.canvasObjects.manipulation.cManipulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import static javax.swing.JOptionPane.showMessageDialog;
import static simulapp.EconSimul.manipulation.simulManipulator.clock;
import static simulapp.util.math.parseIntFromString;

/**
 * Created by Tomas Ballon on 21.1.2017.
 */
public class mainGUI {
    public JPanel panelMain;
    private JButton SHOWCANVASButton;
    JRadioButton addingRadioButton;
    private JRadioButton deletingRadioButton;
    private JRadioButton editingRadioButton;
    private JRadioButton selectingRadioButton;
    private JRadioButton connectingRadioButton;
    private JRadioButton disconnectingRadioButton;
    private JRadioButton crawlinfRadioButton;
    private JRadioButton nextPropabilityRadioButton;
    private JRadioButton metricsRadioButton;
    private JButton results;
    public JTextField simulationSpeedTextField;
    public JTextField moveSpeedTextField;
    private JButton applySpeedsButton;
    private JButton SAVEButton;
    private JButton LOADButton;
    private JButton RESTARTButton;
    private JButton CLEARSIMULATIONButton;

    private fullResultsForm resultsForm;
    private JFrame resultsFrame;

    private ButtonGroup radioGroup;
    public simulApp app;

    private String lastMode;

    public mainGUI(simulApp app) {
        this.app = app;

        lastMode = cManipulator.ADDING;

        addingRadioButton.setText(cManipulator.ADDING);
        editingRadioButton.setText(cManipulator.EDITING);
        deletingRadioButton.setText(cManipulator.DELETING);
        selectingRadioButton.setText(cManipulator.SELECTING);
        nextPropabilityRadioButton.setText(cManipulator.VISITPROBABILITY);
        metricsRadioButton.setText(cManipulator.METRICS);
        connectingRadioButton.setText(cManipulator.ADDINGCHILD);
        disconnectingRadioButton.setText(cManipulator.DELETINGCHILD);
        crawlinfRadioButton.setText(cManipulator.CRAWLING);

        radioGroup = new ButtonGroup();
        radioGroup.add(addingRadioButton);
        radioGroup.add(deletingRadioButton);
        radioGroup.add(editingRadioButton);
        radioGroup.add(selectingRadioButton);
        radioGroup.add(nextPropabilityRadioButton);
        radioGroup.add(metricsRadioButton);
        radioGroup.add(connectingRadioButton);
        radioGroup.add(disconnectingRadioButton);
        radioGroup.add(crawlinfRadioButton);

        addingRadioButton.setSelected(true);

        simulationSpeedTextField.setText("1");
        moveSpeedTextField.setText("1");

        ActionListener listener1 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(getCurrentMode().equals(cManipulator.CRAWLING)){
                    if(!lastMode.equals(cManipulator.CRAWLING)){
                        app.resetClockNoChangemode();
                        simulApp.manipulator.crawler.addShapes(simulApp.manipulator.getCObjects().getShapes());
                        simulApp.manipulator.crawler.run();
                    }else{
                        addingRadioButton.setSelected(true);
                    }
                }
                simulApp.manipulator.onChangeMode(getCurrentMode());
                lastMode = getCurrentMode();
            }
        };

        addingRadioButton.addActionListener(listener1);
        selectingRadioButton.addActionListener(listener1);
        editingRadioButton.addActionListener(listener1);
        connectingRadioButton.addActionListener(listener1);
        disconnectingRadioButton.addActionListener(listener1);
        deletingRadioButton.addActionListener(listener1);
        crawlinfRadioButton.addActionListener(listener1);
        results.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
             /*   if(getCurrentMode().equals(cManipulator.CRAWLING)){
                    showMessageDialog(null, "Results may be inaccurate while simulation running.");
                }*/
                resultsForm = new fullResultsForm(app);

                if(resultsForm.isEmpty()){
                    showMessageDialog(null, "Results are emtpy.");
                    resultsForm = null;
                    return;
                }

                resultsFrame = new JFrame("Total results (" + resultsForm.getTimeTo() + ")");
                resultsFrame.setContentPane(resultsForm.resultsPanel);
                resultsFrame.pack();
                resultsFrame.setSize(1000, 300);

                //centrovanie okna na stred - current display
                GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                int winW = resultsFrame.getSize().width;
                int winH = resultsFrame.getSize().height;
                int xx = (gd.getDisplayMode().getWidth() - winW) / 2;
                int yy = (gd.getDisplayMode().getHeight() - winH) / 2;

                resultsFrame.setLocation(xx, yy);
                resultsFrame.setVisible(true);
            }
        });
        applySpeedsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                applySpeeds();
            }
        });
        simulationSpeedTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                super.keyPressed(keyEvent);
                if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
                    applySpeeds();
                }
            }
        });
        moveSpeedTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                super.keyPressed(keyEvent);
                if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
                    applySpeeds();
                }
            }
        });
        SAVEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                app.saveDialog();
            }
        });
        LOADButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                app.loadDialog();
                lastMode = cManipulator.ADDING;
            }
        });
        RESTARTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                app.resetClock();
                lastMode = cManipulator.ADDING;
            }
        });
        CLEARSIMULATIONButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                app.clearSimulation();
                lastMode = cManipulator.ADDING;
            }
        });
    }

    public String getCurrentMode(){
        for (Enumeration<AbstractButton> buttons = radioGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return null;
    }

    public void applySpeeds(){
        int simulSpeed = parseIntFromString(simulationSpeedTextField.getText(), clock.getTickSpeed());
        int moveSpeed = parseIntFromString(moveSpeedTextField.getText(), simulApp.manipulator.getMoveSpeed());

        if(simulSpeed > 1000){
            simulSpeed = 1000;
        }

        if(moveSpeed > 1000){
            moveSpeed = 1000;
        }

        clock.setTickSpeed(simulSpeed);
        simulApp.manipulator.setMoveSpeed(moveSpeed);

        //ak nahodou neboli integerove, tak sa prepisu povodnymi
        simulationSpeedTextField.setText(String.valueOf(simulSpeed));
        moveSpeedTextField.setText(String.valueOf(moveSpeed));
    }

}
