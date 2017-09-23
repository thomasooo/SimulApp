package simulapp.EconSimul.forms;

import org.jfree.ui.RefineryUtilities;
import simulapp.EconSimul.graphing.LineChart;
import simulapp.metrics.metricUnit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by Tomas Ballon on 4.3.2017.
 */
public class graphDataForm {

    public JPanel panel;
    private String graphName;
    private HashMap<String, metricUnit> graphData = new HashMap<>();
    protected HashMap<String, JCheckBox> fields = new HashMap<>();
    private JButton generate;
    private int lastTime;

    public graphDataForm(HashMap<String, metricUnit> d, String name, int lastT){
        graphData = d;
        graphName = name;
        lastTime = lastT;

        panel.setLayout(new GridLayout(0, 1));

        JPanel row = new JPanel(new FlowLayout());
        JLabel title = new JLabel(graphName);
        title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 20));
        row.add(title);
        panel.add(row);

        JPanel row1 = new JPanel(new GridLayout());
        JLabel tx = new JLabel("Select Data:");
        title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 20));
        row1.add(tx);
        panel.add(row1);

        for (HashMap.Entry<String, metricUnit> m : graphData.entrySet()){
            addRow(m.getKey());
        }

        JPanel row2 = new JPanel(new FlowLayout());
        generate = new JButton("Generate graph");
        row2.add(generate);
        panel.add(row2);

        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                generateGraph();
            }
        });
    }

    public void addRow(String index){
        JCheckBox chck = new JCheckBox(index);

        fields.put(index, chck);

        JPanel row = new JPanel(new GridLayout());

        row.add(chck);
        panel.add(row);
        panel.revalidate();
    }

    public void generateGraph(){
        LineChart chart = new LineChart(graphName);
        for (HashMap.Entry<String, metricUnit> m : graphData.entrySet()){
            if(fields.get(m.getKey()).isSelected()) {
                if(Math.round(lastTime / 12) < 1){
                    showMessageDialog(null, "This graph is emtpy (low data, try run simulation longer).");
                    return;
                }
                HashMap<Integer, Double> vals = m.getValue().getCountsByInterval(0, lastTime, Math.round(lastTime / 12));
                chart.setData(m.getKey(), vals);
            }
        }

        chart.createGraph();
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);

        //close window
        Window window = SwingUtilities.getWindowAncestor(panel);
        window.dispose();
    }
}
