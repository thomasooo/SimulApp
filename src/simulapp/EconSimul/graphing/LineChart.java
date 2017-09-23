package simulapp.EconSimul.graphing;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Tomas Ballon on 4.3.2017.
 */
public class LineChart extends JFrame {

    private ArrayList<XYSeries> datasetData = new ArrayList<>();
    private String title;

    public LineChart(String graphName){
        super(graphName);

        title = graphName;

    }

    public void createGraph(){
        final XYDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset, title);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }

    public void setData(String name, HashMap<Integer, Double> d){
        XYSeries serieData = new XYSeries(name);

        for(HashMap.Entry<Integer, Double> item: d.entrySet()) {
            serieData.add(item.getKey(), item.getValue());
        }

        datasetData.add(serieData);
    }

    private XYDataset createDataset(){
        XYSeriesCollection res = new XYSeriesCollection();

        for(XYSeries s: datasetData){
            res.addSeries(s);
        }

        return res;
    }

    private JFreeChart createChart(final XYDataset dataset, String name) {
        final JFreeChart chart = ChartFactory.createXYLineChart(
                name,      // chart title
                "minutes",
                "units",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        return chart;

    }

}
