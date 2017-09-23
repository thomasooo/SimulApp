package simulapp.graphics.forms;

import simulapp.graphics.canvasObjects.members.cShape;
import simulapp.main.simulApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Tomas Ballon on 22.1.2017.
 */
public class cShapeForm {
    public JFormattedTextField formattedTextField1;
    public JTextField valueTextField;
    public JPanel panelMain;

    private JComboBox comboBox1;
    private JButton ADDButton;
    private JComboBox isCrawlerStartComboBox;
    private simulApp app;
    private int clickedX;
    private int clickedY;

    public cShapeForm(simulApp app, int x, int y){
        this.app = app;

        clickedX = x;
        clickedY = y;

        comboBox1.addItem(cShape.CELLIPSE);
        comboBox1.addItem(cShape.CRECTANGLE);
        comboBox1.addItem(cShape.CTEXT);

        isCrawlerStartComboBox.addItem(cShape.NOT_CRAWLERSTART);
        isCrawlerStartComboBox.addItem(cShape.CRAWLERSTART);

        ADDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                submit();
            }
        });

        valueTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
                    submit();
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });
    }

    //overloaded / viac parametrov na vstupe - edit form
    public cShapeForm(simulApp app, int x, int y, String selectedType, String index, String value, String crawlerStart){
        this(app, x, y);

        isCrawlerStartComboBox.setSelectedItem(crawlerStart);
        comboBox1.setSelectedItem(selectedType);
        formattedTextField1.setText(index);
        valueTextField.setText(value);

        comboBox1.setEnabled(false);
        formattedTextField1.setEditable(false);
    }

    public void submit(){
        app.manipulator.addCShape(comboBox1.getSelectedItem().toString(), formattedTextField1.getText().toString(), valueTextField.getText().toString(), isCrawlerStartComboBox.getSelectedItem().toString(), clickedX, clickedY);

        Window window = SwingUtilities.getWindowAncestor(panelMain);
        window.dispose();
    }

}
