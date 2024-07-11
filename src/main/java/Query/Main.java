package Query;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		var result = Query.getQuery();
		
		
		// Convert ResultSet to a list of Map entries
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
			while (result.next()) {
			    Map<String, Object> row = new HashMap<>();
			    row.put("test_scope", result.getString("test_scope"));
			    row.put("load_time_avg",result.getDouble("avg(load_time)"));
			    row.put("load_time_max",result.getDouble("max(load_time)"));
			   
			    resultList.add(row);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        
        //
        resultList.stream()
        .forEach(row -> {
            String scope = (String) row.get("test_scope");
            double averageResult = (Double) row.get("load_time_avg");
            double maxResult = (Double) row.get("load_time_max");
            System.out.println(scope + " | " +  averageResult);
            dataset.addValue(averageResult, "AVG TEST RESULTS", scope);
            dataset.addValue(maxResult, "MAX TEST RESULTS", scope);
            
        });
        
        // create JfreeChart
        JFreeChart chart =  ChartFactory.createLineChart("Average and Max Load Times for Different WorkFlows","TEST SCOPE","LOAD TIME", dataset, PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips
                false // urls
                );
        

        
        
        // Customize x-axis (CategoryAxis)
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        CategoryAxis xAxis = plot.getDomainAxis();
        xAxis.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12)); // Adjust tick label font

        // Customize primary y-axis (Average Result)
        NumberAxis yAxis1 = new NumberAxis("Average Result");
        yAxis1.setAutoRangeIncludesZero(false); // Ensure range does not include zero to leave space for annotation
        plot.setRangeAxis(0, yAxis1); // Set as primary (left) y-axis

        // Customize secondary y-axis (Max Result)
        NumberAxis yAxis2 = new NumberAxis("Max Result");
        yAxis2.setAutoRangeIncludesZero(false); // Ensure range does not include zero to leave space for annotation
        plot.setRangeAxis(1, yAxis2); // Set as secondary (right) y-axis
        plot.setRangeAxisLocation(1, org.jfree.chart.axis.AxisLocation.BOTTOM_OR_RIGHT); // Align to right side of plot area

        // Customize renderer for lines
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE); // Set color for Average Result line
        renderer.setSeriesStroke(0, new BasicStroke(2.0f)); // Set stroke for Average Result line
        renderer.setSeriesPaint(1, Color.RED); // Set color for Max Result line
        renderer.setSeriesStroke(1, new BasicStroke(2.0f)); // Set stroke for Max Result line
        
        
        
        // Item label generator to display values on data points
        CategoryItemLabelGenerator labelGenerator = new StandardCategoryItemLabelGenerator("{2}", new DecimalFormat("0.0"));
        renderer.setDefaultItemLabelGenerator(labelGenerator);
        renderer.setDefaultItemLabelsVisible(true);
        
        
        
        
        
        plot.setRenderer(renderer);
        
     
        // Save the chart as an image file
        File imageFile = new File("average_max_results_chart.png");
        try {
			ChartUtils.saveChartAsPNG(imageFile, chart, 800, 600);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        //
         chart = ChartFactory.createBarChart(
                "Average Load Times for Different WorkFlows", // chart title
                "TEST SCOPE", // domain axis label
                "LOAD TIME", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips
                false // urls
        );
        
         
         
         
         
         
         
         
         imageFile = new File("average_results_bar_chart.png");
         try {
			ChartUtils.saveChartAsPNG(imageFile, chart, 800, 600);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
        
        
        
        try {
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}