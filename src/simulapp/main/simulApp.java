package simulapp.main;

import simulapp.EconSimul.manipulation.personObjects;
import simulapp.EconSimul.manipulation.simulManipulator;
import simulapp.EconSimul.savers.cShapesSaver;
import simulapp.graphics.canvas.simuCanvas;
import simulapp.graphics.canvasObjects.manipulation.cManipulator;
import simulapp.util.indexManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by Tomas Ballon on 22.1.2017.
 */
public class simulApp {

    public static indexManager indexes;
    public static mainGUI mainGui;
    public static cManipulator manipulator;
    public static simuCanvas canvas;
    //menu
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu simulationMenu;

    public simulApp(int canvasWidth, int canvasHeight){
        indexes = new indexManager();

        //init hlavneho formu
        mainGui = new mainGUI(this);

        //menu
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        simulationMenu = new JMenu("Simulation");

        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.setActionCommand("New");

        JMenuItem openMenuItem = new JMenuItem("Load");
        openMenuItem.setActionCommand("Open");

        JMenuItem saveMenuItem = new JMenuItem("Save As");
        saveMenuItem.setActionCommand("Save");

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setActionCommand("Exit");

        JMenuItem clearSimulation = new JMenuItem("Clear Simulation");
        clearSimulation.setActionCommand("CSimul");

        JMenuItem resetClock = new JMenuItem("Reset Clock");
        clearSimulation.setActionCommand("RClock");

        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(exitMenuItem);

        simulationMenu.add(clearSimulation);
        simulationMenu.add(resetClock);

        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                clearSimulation();
            }
        });
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                loadDialog();
            }
        });
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveDialog();
            }
        });
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int option = JOptionPane.showConfirmDialog(null, "Close simulApp?", "Warning", JOptionPane.YES_NO_OPTION);
                if (option == 0) {
                    System.exit(0);
                }
            }
        });
        clearSimulation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                clearSimulation();
            }
        });
        resetClock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                resetClock();
            }
        });

        menuBar.add(fileMenu);
        menuBar.add(simulationMenu);

        //mainGui
        JFrame frame = new JFrame("SimulApp");
        frame.setContentPane(mainGui.panelMain);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent we){
                int option = JOptionPane.showConfirmDialog(null, "Close simulApp?", "Warning", JOptionPane.YES_NO_OPTION);
                if (option == 0) {
                    System.exit(0);
                }
            }
        });

        /*
        String[] options = {"simulManipulator", "cManipulator"};
        int dialogResult = JOptionPane.showOptionDialog(null,
                "Select manipulator for this session.",
                "Select",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        if(dialogResult == JOptionPane.YES_OPTION){
            manipulator = new simulManipulator(this); //miesto cManipulator
        }else{
            manipulator = new cManipulator(this);
        }
        */
        manipulator = new simulManipulator(this); //miesto cManipulator


        canvas = new simuCanvas(this, canvasWidth, canvasHeight);
        canvas.show();
    }

    public void mouseDown(int x, int y){
        manipulator.mouseDown(x, y);
    }

    public void mouseUp(int x, int y){
        manipulator.mouseUp(x, y);
    }

    //opakuje sa pri kazdom ticku
    public void tick(){
        Point mousePosition = canvas.getMousePosition();

        manipulator.tick(mousePosition);
        canvas.repaint();

        manipulator.addCustomText("currentMode", "MODE: " + mainGui.getCurrentMode(), 100, 20);
    }

    public void saveDialog(){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("SimulApp simulations *.sas", "sas");
        fileChooser.setFileFilter(filter);
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filename = file.getPath();

            //extension
            String extension = "";
            int i = filename.lastIndexOf('.');
            int p = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));
            if (i > p) {
                extension = filename.substring(i+1);
            }
            if(!extension.equals("sas")){
                filename = filename + ".sas";
            }

            if((new File(filename).exists())){
                int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to override existing file?", "Warning", JOptionPane.YES_NO_OPTION);
                if (option != 0) {
                    return;
                }
            }

            try {
                FileOutputStream fileOut =
                        new FileOutputStream(filename);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(manipulator.shapes.save());
                out.close();
                fileOut.close();
                showMessageDialog(null, "Succesfully saved.");
            }catch(IOException ie) {
                ie.printStackTrace();
                showMessageDialog(null, "Error saving simulation.");
            }
        }
    }

    public void loadDialog(){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("SimulApp simulations *.sas", "sas");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if(selectedFile == null){
                return;
            }
            cShapesSaver shapes = null;
            try {
                FileInputStream fileIn = new FileInputStream(selectedFile);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                shapes = (cShapesSaver) in.readObject();
                manipulator = new simulManipulator(this);
                manipulator.shapes.setShapes(shapes.load());
                manipulator.initClock();
                manipulator.crawler = new personObjects(this);
                mainGui.addingRadioButton.setSelected(true);

                in.close();
                fileIn.close();
            }catch(IOException i) {
                i.printStackTrace();
                showMessageDialog(null, "Error loading simulation.");
                return;
            }catch(ClassNotFoundException c) {
                showMessageDialog(null, "Error loading simulation.");
                c.printStackTrace();
                return;
            }
        }
    }

    public void clearSimulation(){
        int option = JOptionPane.showConfirmDialog(null, "Clear workspace?", "Warning", JOptionPane.YES_NO_OPTION);
        if (option != 0) {
            return;
        }
        cShapesSaver shapes = new cShapesSaver(null);
        manipulator = new simulManipulator(this);
        manipulator.shapes.setShapes(shapes.load());
        manipulator.initClock();
        manipulator.crawler = new personObjects(this);
        mainGui.addingRadioButton.setSelected(true);
    }

    public void resetClock(){
        cShapesSaver shapes = manipulator.shapes.save();
        manipulator = new simulManipulator(this);
        manipulator.shapes.setShapes(shapes.load());
        manipulator.initClock();
        manipulator.crawler = new personObjects(this);
        mainGui.addingRadioButton.setSelected(true);
    }

    public void resetClockNoChangemode(){
        cShapesSaver shapes = manipulator.shapes.save();
        manipulator = new simulManipulator(this);
        manipulator.shapes.setShapes(shapes.load());
        manipulator.initClock();
        manipulator.crawler = new personObjects(this);
    }
}
