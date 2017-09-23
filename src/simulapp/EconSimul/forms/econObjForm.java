package simulapp.EconSimul.forms;

import kotlin.Pair;
import simulapp.EconSimul.members.cProcessingPoint;
import simulapp.EconSimul.members.cStartPoint;
import simulapp.main.simulApp;
import simulapp.util.timeDepender;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static simulapp.util.math.parseDoubleFromString;
import static simulapp.util.math.parseIntFromString;

/**
 * Created by Tomas Ballon on 4.2.2017.
 */
public class econObjForm {
    private JTabbedPane tabbedPane1;
    public JPanel panel1;

    //processing center
    public JTextField labelTextField1;
    private JTextField capacityTextField;
    private JTextField minMoneyConsumptionTextField;
    private JTextField maxMoneyConsumptionTextField;
    private JTextField minTimeConsumptionTextField;
    private JTextField maxTimeConsumptionTextField;
    private JButton CREATEPROCESSINGCENTERButton;

    //start point
    private JTextField labelTextField;
    private JTextField minTicksToGenerateTextField;
    private JTextField maxTicksToGenerateTextField;
    private JTextField minPersonsPerCreatingTextField;
    private JTextField maxPersonsPerCreatingTextField;
    private JTextField personMaxMoneyTextField;
    private JTextField personMinMoneyTextField;
    private JTextField personMaxTimeTextField;
    private JTextField personMinTimeTextField;
    private JButton CREATESTARTPOINTButton;
    private JTextField imageTextField;
    private JButton browsePrImage;
    private JTextField imageTextField1;
    private JButton browseSPImage;
    private JTextField personPriorityTextField;
    private JButton moreTimeButton;
    private JButton morePersonsButton;
    private JButton moreCapaButton;
    private JButton morePMoneyButton;
    private JButton morePTimeButton;

    protected simulApp app;

    protected int clickX;
    protected int clickY;
    protected boolean update;
    protected cStartPoint sp;
    protected cProcessingPoint pp;

    //default
    public econObjForm(simulApp a){
        app = a;
        sp = null;
        pp = null;

        CREATEPROCESSINGCENTERButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                performAddProcessingCenter();
            }
        });

        CREATESTARTPOINTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                performAddStartPoint();
            }
        });

        KeyListener listenStartPoint = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
                    performAddStartPoint();
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        };

        KeyListener listenProcessingCenter = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
                    performAddProcessingCenter();
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        };

        //processing center
        labelTextField1.addKeyListener(listenProcessingCenter);
        capacityTextField.addKeyListener(listenProcessingCenter);
        minMoneyConsumptionTextField.addKeyListener(listenProcessingCenter);
        maxMoneyConsumptionTextField.addKeyListener(listenProcessingCenter);
        minTimeConsumptionTextField.addKeyListener(listenProcessingCenter);
        maxTimeConsumptionTextField.addKeyListener(listenProcessingCenter);

        //start point
        labelTextField.addKeyListener(listenStartPoint);
        minTicksToGenerateTextField.addKeyListener(listenStartPoint);
        maxTicksToGenerateTextField.addKeyListener(listenStartPoint);
        minPersonsPerCreatingTextField.addKeyListener(listenStartPoint);
        maxPersonsPerCreatingTextField.addKeyListener(listenStartPoint);
        personMaxMoneyTextField.addKeyListener(listenStartPoint);
        personMinMoneyTextField.addKeyListener(listenStartPoint);
        personMaxTimeTextField.addKeyListener(listenStartPoint);
        personMinTimeTextField.addKeyListener(listenStartPoint);
        personPriorityTextField.addKeyListener(listenStartPoint);

        moreTimeButton.setEnabled(false);
        morePersonsButton.setEnabled(false);
        moreCapaButton.setEnabled(false);
        morePMoneyButton.setEnabled(false);
        morePTimeButton.setEnabled(false);

        browsePrImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String img = loadImage();
                if(img != null){
                    imageTextField.setText(img);
                }
            }
        });

        browseSPImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String img = loadImage();
                if(img != null){
                    imageTextField1.setText(img);
                }
            }
        });
        moreTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(update){
                    showTimeDependForm(sp.countTicksToCreatingTimeDependent, "minutes to generate");
                }
            }
        });
        morePersonsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(update){
                    showTimeDependForm(sp.personsPerCreatingTimeDependent, "persons per generate");
                }
            }
        });
        moreCapaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(update){
                    showTimeDependForm(pp.capacityTimeDependent, "capacity");
                }
            }
        });
        morePMoneyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(update){
                    showTimeDependForm(pp.moneyConsumptionTimeDependent, "moeny consumption");
                }
            }
        });
        morePTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(update){
                    showTimeDependForm(pp.timeConsumptionTimeDependent, "time consumption");
                }
            }
        });
    }

    //adding
    public econObjForm(simulApp a, int x, int y){
        this(a);

        clickX = x;
        clickY = y;
        update = false;

        //start point default values
        minTicksToGenerateTextField.setText("1");
        maxTicksToGenerateTextField.setText("15");
        minPersonsPerCreatingTextField.setText("1");
        maxPersonsPerCreatingTextField.setText("5");
        personMinMoneyTextField.setText("100");
        personMaxMoneyTextField.setText("1000");
        personMinTimeTextField.setText("1000");
        personMaxTimeTextField.setText("10000");
        personPriorityTextField.setText("0");

        //processing center default values
        capacityTextField.setText("5");
        minMoneyConsumptionTextField.setText("2");
        maxMoneyConsumptionTextField.setText("100");
        minTimeConsumptionTextField.setText("10");
        maxTimeConsumptionTextField.setText("100");
    }

    //editing processing point
    public econObjForm(simulApp a, cProcessingPoint p){
        this(a);

        clickX = p.getX();
        clickY = p.getY();
        update = true;
        pp = p;

        tabbedPane1.setEnabledAt(1, false);
        CREATEPROCESSINGCENTERButton.setText("UPDATE PROCESSING CENTER");

        //processing center default values
        labelTextField1.setText(p.getText());
        capacityTextField.setText(String.valueOf(p.getCapacity()));
        minMoneyConsumptionTextField.setText(String.valueOf(p.getMoneyConsumptionMin()));
        maxMoneyConsumptionTextField.setText(String.valueOf(p.getMoneyConsumptionMax()));
        minTimeConsumptionTextField.setText(String.valueOf(p.getTimeConsumptionMin()));
        maxTimeConsumptionTextField.setText(String.valueOf(p.getTimeConsumptionMax()));
        imageTextField.setText(p.getImgSrc());

        moreCapaButton.setEnabled(true);
        morePMoneyButton.setEnabled(true);
        morePTimeButton.setEnabled(true);
    }

    //editing start point
    public econObjForm(simulApp a, cStartPoint p){
        this(a);

        clickX = p.getX();
        clickY = p.getY();
        update = true;
        sp = p;
        tabbedPane1.setEnabledAt(0, false);
        tabbedPane1.setSelectedIndex(1);
        CREATESTARTPOINTButton.setText("UPDATE START POINT");

        //start point default values
        labelTextField.setText(p.getText());
        minTicksToGenerateTextField.setText(String.valueOf(p.getCountTicksToCreatingMin()));
        maxTicksToGenerateTextField.setText(String.valueOf(p.getCountTicksToCreatingMax()));
        minPersonsPerCreatingTextField.setText(String.valueOf(p.getPersonsPerCreatingMin()));
        maxPersonsPerCreatingTextField.setText(String.valueOf(p.getPersonsPerCreatingMax()));
        personMinMoneyTextField.setText(String.valueOf(p.getPersonMoneyAvailableMin()));
        personMaxMoneyTextField.setText(String.valueOf(p.getPersonMoneyAvailableMax()));
        personMinTimeTextField.setText(String.valueOf(p.getPersonTimeAvailableMin()));
        personMaxTimeTextField.setText(String.valueOf(p.getPersonTimeAvailableMax()));
        personPriorityTextField.setText(String.valueOf(p.getPersonsPriority()));
        imageTextField1.setText(p.getImgSrc());

        moreTimeButton.setEnabled(true);
        morePersonsButton.setEnabled(true);
    }

    protected void performAddStartPoint(){
        if(update){
            updateStartPoint();
        }else {
            addStartPoint();
        }
    }

    protected void performAddProcessingCenter(){
        if(update){
            updateProcessingCenter();
        }else {
            addProcessingCenter();
        }
    }

    protected void addProcessingCenter(){
        cProcessingPoint p = new cProcessingPoint(labelTextField1.getText(), clickX, clickY);
        setProcessingPoint(p);
    }

    protected void addStartPoint(){
        cStartPoint p = new cStartPoint(labelTextField.getText(), clickX, clickY);
        setStartPoint(p);
    }

    protected void updateProcessingCenter(){
        setProcessingPoint(pp);
    }

    protected void updateStartPoint(){
        setStartPoint(sp);
    }

    protected void setProcessingPoint(cProcessingPoint p){
        if(p == null){
            return;
        }

        p.setText(labelTextField1.getText());
        p.setCapacity(parseIntFromString(capacityTextField.getText(), 5));
        p.setMoneyConsumption(
                parseDoubleFromString(minMoneyConsumptionTextField.getText(), 2),
                parseDoubleFromString(maxMoneyConsumptionTextField.getText(), 100));

        p.setTimeConsumption(
                parseIntFromString(minTimeConsumptionTextField.getText(), 10),
                parseIntFromString(maxTimeConsumptionTextField.getText(), 100));

        p.setImgSrc(imageTextField.getText());

        app.manipulator.shapes.addCShape(p.getIndex(), p);
        Window window = SwingUtilities.getWindowAncestor(panel1);
        window.dispose();
    }

    protected void setStartPoint(cStartPoint p){
        if(p == null){
            return;
        }

        p.setText(labelTextField.getText());
        p.setCountTicksToCreating(
                parseIntFromString(minTicksToGenerateTextField.getText(), 100),
                parseIntFromString(maxTicksToGenerateTextField.getText(), 200));

        p.setPersonsPerCreating(
                parseIntFromString(minPersonsPerCreatingTextField.getText(), 1),
                parseIntFromString(maxPersonsPerCreatingTextField.getText(), 5));

        p.setPersonMoneyAvailable(
                parseDoubleFromString(personMinMoneyTextField.getText(), 100),
                parseDoubleFromString(personMaxMoneyTextField.getText(), 1000));

        p.setPersonTimeAvailable(
                parseIntFromString(personMinTimeTextField.getText(), 1000),
                parseIntFromString(personMaxTimeTextField.getText(), 10000));

        p.setPersonsPriority(parseIntFromString(personPriorityTextField.getText(), 0));

        p.setImgSrc(imageTextField1.getText());

        app.manipulator.shapes.addCShape(p.getIndex(), p);
        Window window = SwingUtilities.getWindowAncestor(panel1);
        window.dispose();
    }

    public String loadImage(){
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(panel1);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    public void showTimeDependForm(timeDepender<Pair<Integer, Integer>> td, String title){
        timeDependForm tdForm = new timeDependForm(td);

        JFrame tdFrame = new JFrame("Time Dependent Values - " + title);
        tdFrame.setContentPane(tdForm.tdPanel);
        tdFrame.pack();
        tdFrame.setSize(300, 300);

        //centrovanie okna na stred - current display
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int winW = tdFrame.getSize().width;
        int winH = tdFrame.getSize().height;
        int xx = (gd.getDisplayMode().getWidth() - winW) / 2;
        int yy = (gd.getDisplayMode().getHeight() - winH) / 2;

        tdFrame.setLocation(xx, yy);
        tdFrame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
