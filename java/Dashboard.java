import javax.swing.*;
import java.util.*;
// import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.sql.*; // resultset
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.XYSeries.*;
import org.knowm.xchart.style.markers.*;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;


public class Dashboard{

  JFrame frame; // the pop up window for our dashboard
  // JPanel finalpanel; // the final panel that all the sub panels will be added to it in show_dashboard() function

  JPanel panel1Aa; // panel for part 1Aa: customer_count_yearly
  JPanel panel1Ab; // panel for part 1Ab: customer_count_monthly
  JPanel panel1Ac; // panel for part 1Ac: customer_count_weekly

  JPanel panel1Ba; // panel for part 1Aa: sales_amount_yearly
  JPanel panel1Bb; // panel for part 1Ab: sales_amount_monthly
  JPanel panel1Bc; // panel for part 1Ac: sales_amount_weekly

  JPanel panel1Ca; // panel for part 1Ca: sales_count_yearly
  JPanel panel1Cb; // panel for part 1Cb: sales_count_monthly
  JPanel panel1Cc; // panel for part 1Cc: sales_count_weekly

  JPanel panel2A; // panel for part 2A: employee age histogram

  JPanel panel3A;
  JPanel panel3B;
  JPanel panel3C;
  JPanel panel3D;

  JPanel panel2B;  // pane1 for part 2B: emloyee total salary histogram

  JPanel panel4A; // panel for part 4A: product_top_10_Jan_to_Mar
  JPanel panel4B; // panel for part 4B: product_top_10_Apr_to_Jun
  JPanel panel4C; // panel for part 4C: product_top_10_Jul_to_Sep
  JPanel panel4D; // panel for part 4D: product_top_10_Oct_to_Dec

  JPanel panel5A; // panel for part 5A : customer birth year
  JPanel panel5B; // panel for part 5B: customer gender
  JPanel panel5C; // panel for part 5C: customer education
  JPanel panel5D; // panel for part 5D: customer marrital status
  JPanel panel5E; // panel for part 5E: customer yearly income
  JPanel finalPanel;

  JScrollPane scroll; // added when needed, not being used now

  // Part 1A chart members
  XYChart num_customer_yearly_chart;
  XYChart num_customer_monthly_chart;
  XYChart num_customer_weekly_chart;


  // Part 1B chart members
  XYChart sales_amount_yearly_chart;
  XYChart sales_amount_monthly_chart;
  XYChart sales_amount_weekly_chart;

  // Part 1C chart members
  XYChart sales_count_yearly_chart;
  XYChart sales_count_monthly_chart;
  XYChart sales_count_weekly_chart;

  // Part 4 chart members
  PieChart product_top_10_Jan_to_Mar_chart;
  PieChart product_top_10_Apr_to_Jun_chart;
  PieChart product_top_10_Jul_to_Sep_chart;
  PieChart product_top_10_Oct_to_Dec_chart;

  // Part 5 chart members
  PieChart customer_birth_year_chart;
  PieChart customer_gender_chart;
  CategoryChart customer_education_chart;
  PieChart customer_marrital_status_chart;
  CategoryChart customer_yearly_income_chart;

  // Part 2 chart members
  Histogram employee_age_histogram; // histogram needs to be added a category chart to display histogram
  CategoryChart employee_age_chart;
  private Histogram employee_salary_histogram;
  private CategoryChart employee_salary_chart;

  // Part 3 chart members
  CategoryChart regional_sales_count_chart;
  CategoryChart regional_sales_amount_chart;
  CategoryChart regional_sales_customer_chart;
  CategoryChart regional_aggregate_sum_chart;

  // constructor
  Dashboard(){
    this.frame = new JFrame("dashboard for adventureworks");

    this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.frame.setSize(2000,2000);

    this.frame.setVisible(false);
  }

  // Part 1Aa setter: XY graph example
  public void set_num_customer_yearly(List<Integer>x, List<Integer>y) throws Exception {
    try {
      this.num_customer_yearly_chart = new XYChartBuilder().width(400).height(400).title("number of customers each year").build();
      this.num_customer_yearly_chart.addSeries("customer_yearly",x,y);

      this.panel1Aa = new XChartPanel(this.num_customer_yearly_chart);
      // hide the legend
      this.num_customer_yearly_chart.getStyler().setLegendVisible(false);


    } catch(Exception e){
      throw new Exception(e);
    }
  }

  // Part 1Ab setter: XY graph example
  public void set_num_customer_monthly(List<Integer>x2001, List<Integer>y2001,List<Integer>x2002, List<Integer>y2002,List<Integer>x2003, List<Integer>y2003,List<Integer>x2004, List<Integer>y2004) throws Exception {
    try {
      this.num_customer_monthly_chart = new XYChartBuilder().width(400).height(400).title("number of customers each month").build();
      this.num_customer_monthly_chart.addSeries("2001",x2001,y2001);
      this.num_customer_monthly_chart.addSeries("2002",x2002,y2002);
      this.num_customer_monthly_chart.addSeries("2003",x2003,y2003);
      this.num_customer_monthly_chart.addSeries("2004",x2004,y2004);
      this.panel1Ab = new XChartPanel(this.num_customer_monthly_chart);

    } catch(Exception e){
      throw new Exception(e);
    }
  }

  // Part 1Ac setter: XY graph example
  public void set_num_customer_weekly(List<Integer>x2001, List<Integer>y2001,List<Integer>x2002, List<Integer>y2002,List<Integer>x2003, List<Integer>y2003,List<Integer>x2004, List<Integer>y2004) throws Exception {
    try {
      this.num_customer_weekly_chart = new XYChartBuilder().width(400).height(400).title("number of customers each week").build();
      this.num_customer_weekly_chart.addSeries("2001",x2001,y2001);
      this.num_customer_weekly_chart.addSeries("2002",x2002,y2002);
      this.num_customer_weekly_chart.addSeries("2003",x2003,y2003);
      this.num_customer_weekly_chart.addSeries("2004",x2004,y2004);
      this.panel1Ac = new XChartPanel(this.num_customer_weekly_chart);

    } catch(Exception e){
      throw new Exception(e);
    }
  }

  // Part 1Ba setter: XY graph example
  public void set_sales_amount_yearly(List<Integer>x, List<Double>y) throws Exception {
    try {
      this.sales_amount_yearly_chart = new XYChartBuilder().width(400).height(400).title("sales amount each year").build();
      this.sales_amount_yearly_chart.addSeries("sales_amount_yearly",x,y);

      this.panel1Ba = new XChartPanel(this.sales_amount_yearly_chart);
      // hide the legend
      this.sales_amount_yearly_chart.getStyler().setLegendVisible(false);


    } catch(Exception e){
      throw new Exception(e);
    }
  }
  // Part 1Bb setter: XY graph example
  public void set_sales_amount_monthly(List<Integer>x2001, List<Double>y2001,List<Integer>x2002, List<Double>y2002,List<Integer>x2003, List<Double>y2003,List<Integer>x2004, List<Double>y2004) throws Exception {
    try {
      this.sales_amount_monthly_chart = new XYChartBuilder().width(400).height(400).title("sales amount each month").build();
      this.sales_amount_monthly_chart.addSeries("2001",x2001,y2001);
      this.sales_amount_monthly_chart.addSeries("2002",x2002,y2002);
      this.sales_amount_monthly_chart.addSeries("2003",x2003,y2003);
      this.sales_amount_monthly_chart.addSeries("2004",x2004,y2004);
      this.panel1Bb = new XChartPanel(this.sales_amount_monthly_chart);

    } catch(Exception e){
      throw new Exception(e);
    }
  }

  // Part 1Bc setter: XY graph example
  public void set_sales_amount_weekly(List<Integer>x2001, List<Double>y2001,List<Integer>x2002, List<Double>y2002,List<Integer>x2003, List<Double>y2003,List<Integer>x2004, List<Double>y2004) throws Exception {
    try {
      this.sales_amount_weekly_chart = new XYChartBuilder().width(400).height(400).title("sales amount each week").build();
      this.sales_amount_weekly_chart.addSeries("2001",x2001,y2001);
      this.sales_amount_weekly_chart.addSeries("2002",x2002,y2002);
      this.sales_amount_weekly_chart.addSeries("2003",x2003,y2003);
      this.sales_amount_weekly_chart.addSeries("2004",x2004,y2004);
      this.panel1Bc = new XChartPanel(this.sales_amount_weekly_chart);

    } catch(Exception e){
      throw new Exception(e);
    }
  }

  // Part 1Ca setter: XY graph example
  public void set_sales_count_yearly(List<Integer>x, List<Integer>y) throws Exception {
    try {
      this.sales_count_yearly_chart = new XYChartBuilder().width(400).height(400).title("sales count each year").build();
      this.sales_count_yearly_chart.addSeries("sales_count_yearly",x,y);

      this.panel1Ca = new XChartPanel(this.sales_count_yearly_chart);
      // hide the legend
      this.sales_count_yearly_chart.getStyler().setLegendVisible(false);


    } catch(Exception e){
      throw new Exception(e);
    }
  }
  // Part 1Cb setter: XY graph example
  public void set_sales_count_monthly(List<Integer>x2001, List<Integer>y2001,List<Integer>x2002, List<Integer>y2002,List<Integer>x2003, List<Integer>y2003,List<Integer>x2004, List<Integer>y2004) throws Exception {
    try {
      this.sales_count_monthly_chart = new XYChartBuilder().width(400).height(400).title("sales count each month").build();
      this.sales_count_monthly_chart.addSeries("2001",x2001,y2001);
      this.sales_count_monthly_chart.addSeries("2002",x2002,y2002);
      this.sales_count_monthly_chart.addSeries("2003",x2003,y2003);
      this.sales_count_monthly_chart.addSeries("2004",x2004,y2004);
      this.panel1Cb = new XChartPanel(this.sales_count_monthly_chart);

    } catch(Exception e){
      throw new Exception(e);
    }
  }

  // Part 1Cc setter: XY graph example
  public void set_sales_count_weekly(List<Integer>x2001, List<Integer>y2001,List<Integer>x2002, List<Integer>y2002,List<Integer>x2003, List<Integer>y2003,List<Integer>x2004, List<Integer>y2004) throws Exception {
    try {
      this.sales_count_weekly_chart = new XYChartBuilder().width(400).height(400).title("sales count each week").build();
      this.sales_count_weekly_chart.addSeries("2001",x2001,y2001);
      this.sales_count_weekly_chart.addSeries("2002",x2002,y2002);
      this.sales_count_weekly_chart.addSeries("2003",x2003,y2003);
      this.sales_count_weekly_chart.addSeries("2004",x2004,y2004);
      this.panel1Cc = new XChartPanel(this.sales_count_weekly_chart);

    } catch(Exception e){
      throw new Exception(e);
    }
  }

  // Part 5A setter: pie chart example
  public void set_customer_birth_year(List<String> x, List<Integer> y) throws Exception{
    try{
      // add the pie chart
      this.customer_birth_year_chart = new PieChartBuilder().width(400).height(400).title("customer age year").build();
      // loop through to add piechart data to piechart
      for(int i = 0; i < x.size(); i++){
        this.customer_birth_year_chart.addSeries(x.get(i), y.get(i));
      }
      // define it here, since we have to pass the chart object when creating it, we can't use new JPanel(), but new XChartPanel(chartobject)
      this.panel5A = new XChartPanel(this.customer_birth_year_chart);
    } catch (Exception e){
      throw new Exception(e);
    }
  }

  // Part 5B setter -- pie chart example
  public void set_customer_gender(List<String> x, List<Integer>y) throws Exception{
    try{
      this.customer_gender_chart = new PieChartBuilder().width(400).height(400).title("customer gender").build();
      for(int i = 0; i < x.size(); i++){
        this.customer_gender_chart.addSeries(x.get(i),y.get(i));
      }
      this.panel5B = new XChartPanel(this.customer_gender_chart); // must be declared as JPanel type but create new XChartPanel

    } catch(Exception e){
      throw new Exception(e);
    }
  }

  // Part 5C setter --  also category chart example
  public void set_customer_education(List<String>x, List<Integer>y)throws Exception {
    try{
      this.customer_education_chart = new CategoryChartBuilder().width(400).height(400).title("customer education").build();
      // category chart can add data at once
      this.customer_education_chart.addSeries("education",x,y);
      this.panel5C = new XChartPanel(this.customer_education_chart);
      // label overlapping, tilt / rotate ticks
      this.customer_education_chart.getStyler().setXAxisLabelRotation(45);

    } catch (Exception e){
      throw new Exception(e);
    }
  }

  // part 5D: customer marrital status setter
  public void set_customer_marrital_status(List<String>x, List<Integer>y) throws Exception{
    try{
      this.customer_marrital_status_chart = new PieChartBuilder().width(400).height(400).title("customer marrital status").build();
      for(int i = 0; i < x.size(); i++){
        this.customer_marrital_status_chart.addSeries(x.get(i),y.get(i));
      }
      this.panel5D = new XChartPanel(this.customer_marrital_status_chart);

    } catch(Exception e){
      throw new Exception(e);
    }
  }

  // part 5E: customer yearly income setter
  public void set_customer_yearly_income(List<String>x,List<Integer>y)throws Exception{
    try{
      this.customer_yearly_income_chart = new CategoryChartBuilder().width(400).height(400).title("customer yearly income").build();
      // CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title("Stick").build();
      // Customize Chart
      this.customer_yearly_income_chart.getStyler().setChartTitleVisible(true);
      this.customer_yearly_income_chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
      this.customer_yearly_income_chart.getStyler().setDefaultSeriesRenderStyle(CategorySeriesRenderStyle.Stick);
      this.customer_yearly_income_chart.addSeries("yearlyIncome",x,y);

      this.panel5E = new XChartPanel(this.customer_yearly_income_chart);
      this.customer_yearly_income_chart.getStyler().setXAxisLabelRotation(70);
    } catch(Exception e){
      throw new Exception(e);
    }
  }

  // Part 2A setter (age) : Histogram
  public void set_employee_age(List<Double>x)throws Exception{
    try{

      this.employee_age_histogram = new Histogram(x, 9, 20, 65);
      this.employee_age_chart = new CategoryChartBuilder().width(400).height(400).title("employee age").
      		xAxisTitle("Age Range").yAxisTitle("Count").build();

      List<String> xAgeRange = new ArrayList<>(); // labeling x axis age range values in histograms
      for(int i =0; i <9; i++) {
      	xAgeRange.add(Integer.toString(20+ (5*i)) + " - " + Integer.toString(24+ (5*i)));
      }

      this.employee_age_chart.addSeries("employee age", xAgeRange, this.employee_age_histogram.getyAxisData());
      this.panel2A = new XChartPanel(this.employee_age_chart);
      // rotate/ tilt the captions of the x axis
      this.employee_age_chart.getStyler().setXAxisLabelRotation(70);
    } catch(Exception e){
      throw new Exception(e);
    }
  }


  public void set_product_top_10_Jan_to_Mar(List<String>x, List<Integer>y)throws Exception{
    try{
      this.product_top_10_Jan_to_Mar_chart = new PieChartBuilder().width(400).height(400).title("top 10 products Jan to Mar").build();
      for(int i = 0; i < x.size(); i++){
        this.product_top_10_Jan_to_Mar_chart.addSeries(x.get(i),y.get(i));
      }
      this.panel4A = new XChartPanel(this.product_top_10_Jan_to_Mar_chart);


      // this.product_top_10_Jan_to_Mar_chart = new CategoryChartBuilder().width(400).height(400).title("top 10 products Jan to Mar").build();
      // // category chart can add data at once
      // this.product_top_10_Jan_to_Mar_chart.addSeries("Jan to Mar",x,y);
      // this.panel4A = new XChartPanel(this.product_top_10_Jan_to_Mar_chart);
      // // label overlapping, tilt / rotate ticks
      // this.product_top_10_Jan_to_Mar_chart.getStyler().setXAxisLabelRotation(45);

    } catch(Exception e){
      throw new Exception(e);
    }
  }

  public void set_product_top_10_Apr_to_Jun(List<String>x, List<Integer>y)throws Exception{
    try{
      this.product_top_10_Apr_to_Jun_chart = new PieChartBuilder().width(400).height(400).title("top 10 products Apr to Jun").build();
      for(int i = 0; i < x.size(); i++){
        this.product_top_10_Apr_to_Jun_chart.addSeries(x.get(i),y.get(i));
      }
      this.panel4B = new XChartPanel(this.product_top_10_Apr_to_Jun_chart);
    } catch(Exception e){
      throw new Exception(e);
    }
  }

  public void set_product_top_10_Jul_to_Sep(List<String>x, List<Integer>y)throws Exception{
    try{
      this.product_top_10_Jul_to_Sep_chart = new PieChartBuilder().width(400).height(400).title("top 10 products Jul to Sep").build();
      for(int i = 0; i < x.size(); i++){
        this.product_top_10_Jul_to_Sep_chart.addSeries(x.get(i),y.get(i));
      }
      this.panel4C = new XChartPanel(this.product_top_10_Jul_to_Sep_chart);
    } catch(Exception e){
      throw new Exception(e);
    }
  }

  public void set_product_top_10_Oct_to_Dec(List<String>x, List<Integer>y)throws Exception{
    try{
      this.product_top_10_Oct_to_Dec_chart = new PieChartBuilder().width(400).height(400).title("top 10 products Oct to Dec").build();
      for(int i = 0; i < x.size(); i++){
        this.product_top_10_Oct_to_Dec_chart.addSeries(x.get(i),y.get(i));
      }
      this.panel4D = new XChartPanel(this.product_top_10_Oct_to_Dec_chart);
    } catch(Exception e){
      throw new Exception(e);
    }
  }

  // Part 2B setter (total salary): Histogram
  public void set_employee_salary(List<Double>x)throws Exception{
      try{
      	int minHistogram = (int)(Collections.min(x)/500) * 500;
      	int maxHistogram = (int)(Collections.max(x)/500) * 500 + 500;
      	int numBins = (maxHistogram - minHistogram)/500;

        this.employee_salary_histogram = new Histogram(x, numBins, minHistogram, maxHistogram);
        this.employee_salary_chart = new CategoryChartBuilder().width(400).height(400).title("Employee Total Salary (Median:560, Mean:767)").
      		  xAxisTitle("Salary Range").yAxisTitle("Count").build();

        List<String> xSalaryRange = new ArrayList<>();
        for(int i =0; i <numBins; i++) {
        	xSalaryRange.add(Integer.toString(minHistogram+ (500*i)) + " - " + Integer.toString((minHistogram + 499)+ (500*i)));
        }


        this.employee_salary_chart.addSeries("Total Salary", xSalaryRange, employee_salary_histogram.getyAxisData());
        this.panel2B = new XChartPanel(this.employee_salary_chart);
        // rotate/ tilt the captions of the x axis
        this.employee_salary_chart.getStyler().setXAxisLabelRotation(70);
      } catch(Exception e){
        throw new Exception(e);
      }
    }

    // Part 3A setter
    public void set_regional_sales_count(List<String> states, List<Integer> counts) throws Exception {
    	try {
    		regional_sales_count_chart = new CategoryChartBuilder().width(2500).height(400).title("Sales Count by Region").xAxisTitle("Region").yAxisTitle("Sales count").build();
    		regional_sales_count_chart.addSeries("regional_sales_count", states, counts);
    		regional_sales_count_chart.getStyler().setXAxisLabelRotation(90);
    		panel3A = new XChartPanel(regional_sales_count_chart);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    // Part 3B setter
    public void set_regional_sales_amount(List<String> states, List<Double> amounts) throws Exception {
    	try {
    		regional_sales_amount_chart = new CategoryChartBuilder().width(2500).height(400).title("Sales Amount by Region").xAxisTitle("Region").yAxisTitle("Sales amount").build();
    		regional_sales_amount_chart.addSeries("regional_sales_amount", states, amounts);
    		regional_sales_amount_chart.getStyler().setXAxisLabelRotation(90);
    		panel3B = new XChartPanel(regional_sales_amount_chart);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    // Part 3C setter
    public void set_regional_sales_customer(List<String> states, List<Integer> customers) throws Exception {
    	try {
    		regional_sales_customer_chart = new CategoryChartBuilder().width(2500).height(400).title("Customers by Region").xAxisTitle("Region").yAxisTitle("Number of customers").build();
    		regional_sales_customer_chart.addSeries("regional_sales_customers", states, customers);
    		regional_sales_customer_chart.getStyler().setXAxisLabelRotation(90);
    		panel3C = new XChartPanel(regional_sales_customer_chart);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
     }

    // Part 3D setter
    public void set_regional_aggregate_sums(List<String> states, List<Double> sums) throws Exception {
    	try {
    		regional_aggregate_sum_chart = new CategoryChartBuilder().width(700).height(400).title("Aggregate sum by region").xAxisTitle("Region").yAxisTitle("Sums").build();
    		regional_aggregate_sum_chart.addSeries("regional_aggregate_sums", states, sums);
    		regional_aggregate_sum_chart.getStyler().setXAxisLabelRotation(90);
    		panel3D = new XChartPanel(regional_aggregate_sum_chart);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }

  // when finish adding all the plots, call the function below to show it
  // add all intermediate panels to the final panel and add final panel to jframe
  public void show_dashboard() throws Exception{
    try{
      this.finalPanel = new JPanel();
      // this.scroll = new JScrollPane(finalPanel);
      // add sub panels to final panel
      this.finalPanel.add(this.panel1Aa, BorderLayout.NORTH);
      this.finalPanel.add(this.panel1Ab, BorderLayout.NORTH);
      this.finalPanel.add(this.panel1Ac, BorderLayout.NORTH);

      this.finalPanel.add(this.panel1Ba, BorderLayout.NORTH);
      this.finalPanel.add(this.panel1Bb, BorderLayout.NORTH);
      this.finalPanel.add(this.panel1Bc, BorderLayout.NORTH);

      this.finalPanel.add(this.panel1Ca, BorderLayout.NORTH);
      this.finalPanel.add(this.panel1Cb, BorderLayout.NORTH);
      this.finalPanel.add(this.panel1Cc, BorderLayout.NORTH);
      
      this.finalPanel.add(this.panel3A, BorderLayout.NORTH);
      this.finalPanel.add(this.panel3B, BorderLayout.NORTH);
      this.finalPanel.add(this.panel3C, BorderLayout.NORTH);
      this.finalPanel.add(this.panel3D, BorderLayout.NORTH);

      this.finalPanel.add(this.panel4A,BorderLayout.NORTH);
      this.finalPanel.add(this.panel4B,BorderLayout.NORTH);
      this.finalPanel.add(this.panel4C,BorderLayout.NORTH);
      this.finalPanel.add(this.panel4D,BorderLayout.NORTH);

      this.finalPanel.add(this.panel5A,BorderLayout.NORTH);
      this.finalPanel.add(this.panel5B,BorderLayout.SOUTH);
      this.finalPanel.add(this.panel5C,BorderLayout.SOUTH);
      this.finalPanel.add(this.panel5D,BorderLayout.SOUTH);
      this.finalPanel.add(this.panel5E,BorderLayout.EAST);


      this.finalPanel.add(this.panel2A,BorderLayout.SOUTH);
      this.finalPanel.add(this.panel2B,BorderLayout.SOUTH);
      //..


      this.frame.add(this.finalPanel);
      // this.frame.add(this.scroll);
      this.frame.pack();

      // add scroll bar
      this.frame.setLayout(new BorderLayout());
      this.scroll = new JScrollPane(this.finalPanel);
      this.scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      this.scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

      this.frame.add(this.scroll);
      this.frame.setVisible(true);

    } catch(Exception e){
      throw new Exception(e);
    }
  }

  // function 1 to add chart

};
