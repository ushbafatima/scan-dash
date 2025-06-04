package SalesManagement;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import javax.swing.JPanel;

public class SalesChart extends JPanel {
    private TimeSeriesCollection dataset;
    private JFreeChart chart;
    private ChartPanel chartPanel;

    public SalesChart() {
        dataset = new TimeSeriesCollection();
        createChart();
        setupLayout();
    }

    private void createChart() {
        chart = ChartFactory.createTimeSeriesChart(
                null,                // chart title
                "Date",             // x axis label
                "Revenue ($)",      // y axis label
                dataset,            // data
                false,             // legend
                true,              // tooltips
                false             // urls
        );

        // Customize the chart
        chart.setBackgroundPaint(new Color(30, 30, 30));

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(45, 45, 45));
        plot.setDomainGridlinePaint(new Color(70, 70, 70));
        plot.setRangeGridlinePaint(new Color(70, 70, 70));

        // Customize the line
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 150, 255));
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesShapesVisible(0, true);

        // Customize axes
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
        dateAxis.setTickLabelPaint(Color.WHITE);
        dateAxis.setLabelPaint(Color.WHITE);

        plot.getRangeAxis().setTickLabelPaint(Color.WHITE);
        plot.getRangeAxis().setLabelPaint(Color.WHITE);

        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(700, 400));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);
    }

    public void updateChart() {
        Map<LocalDate, Double> salesData = SalesDataAccess.getDailySales();

        TimeSeries series = new TimeSeries("Daily Sales");

        for (Map.Entry<LocalDate, Double> entry : salesData.entrySet()) {
            series.add(
                    new Day(entry.getKey().getDayOfMonth(),
                            entry.getKey().getMonthValue(),
                            entry.getKey().getYear()),
                    entry.getValue()
            );
        }

        dataset.removeAllSeries();
        dataset.addSeries(series);
    }

    public ChartPanel getChartPanel() {
        return chartPanel;
    }
}