import javax.swing.*;
import java.util.*;
// import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.*;
import java.awt.BorderLayout;
import java.sql.*; // resultset
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.XYSeries.*;
import org.knowm.xchart.style.markers.*;
import org.knowm.xchart.DialSeries;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;


public class Dashboard{

  JFrame frame; // the pop up window for our dashboard
  // JPanel finalpanel; // the final panel that all the sub panels will be added to it in show_dashboard() function


  JPanel panel1Ca; // panel for part 1Ca: sales_count_yearly
  JPanel panel1Cb; // panel for part 1Cb: sales_count_monthly_2004
  JPanel panel1Cc; // panel for part 1Cc: sales_count_weekly_2004
  JPanel panel2A; // panel for part 2A: employee age histogram

  JPanel panel5A; // panel for part 5A : customer birth year
  JPanel panel5B; // panel for part 5B: customer gender
  JPanel panel5C; // panel for part 5C: customer education
  JPanel panel5D; // panel for part 5D: customer marrital status
  JPanel panel5E; // panel for part 5E: customer yearly income
  JPanel finalPanel;

  JScrollPane scroll; // added when needed, not being used now


  // Part 5 chart members
  PieChart customer_birth_year_chart;
  PieChart customer_gender_chart;
  CategoryChart customer_education_chart;
  PieChart customer_marrital_status_chart;
  CategoryChart customer_yearly_income_chart;

  // Part 2 chart members
  Histogram employee_age_histogram; // histogram needs to be added a category chart to display histogram
  CategoryChart employee_age_chart;

  // Part 1C chart member
  BubbleChart sales_count_yearly_chart;
  BubbleChart sales_count_monthly_2004_chart;
  XYChart sales_count_weekly_2004_chart;



  // constructor
  Dashboard(){
    this.frame = new JFrame("dashboard for adventureworks");
    this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.frame.setSize(2000,2000);


    this.frame.setVisible(false);
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

    // Part 2A setter: Histogram
    public void set_employee_age(List<Double>x)throws Exception{
      try{
        this.employee_age_histogram = new Histogram(x,7);
        this.employee_age_chart = new CategoryChartBuilder().width(400).height(400).title("employee age").build();
        this.employee_age_chart.addSeries("employee age", this.employee_age_histogram.getxAxisData(), this.employee_age_histogram.getyAxisData());
        this.panel2A = new XChartPanel(this.employee_age_chart);
        // rotate/ tilt the captions of the x axis
        this.employee_age_chart.getStyler().setXAxisLabelRotation(70);
      } catch(Exception e){
        throw new Exception(e);
      }
    }

    // Part 1Ca setter: sales_count_yearly
    public void set_sales_count_yearly(List<Integer>x,List<Double>y,List<Double>z) throws Exception{
      try{
        this.sales_count_yearly_chart = new BubbleChartBuilder().width(400).height(400).title("sales count yearly").build();
        System.out.println("length x:"+x.size() + " y length:"+y.size());
        this.sales_count_yearly_chart.addSeries("sales_count_yearly",x,y,z);
        this.panel1Ca = new XChartPanel(this.sales_count_yearly_chart);
        // hide the legend to make the graph area bigger
        this.sales_count_yearly_chart.getStyler().setLegendVisible(false);

      } catch(Exception e){
        throw new Exception(e);
      }
    }

    // Part 1Cb setter : sales_count_monthly_2004
    public void set_sales_count_monthly_2004(List<Integer>x,List<Double>y,List<Double>z) throws Exception {
      try{
        this.sales_count_monthly_2004_chart = new BubbleChartBuilder().width(400).height(400).title("sales count monthly 2004").build();
        System.out.println("length x:"+x.size() + " y length:"+y.size()+" Z length: " + z.size());
        this.sales_count_monthly_2004_chart.addSeries("sales_count_monthly",x,y,z);
        this.panel1Cb = new XChartPanel(this.sales_count_monthly_2004_chart);
        // hide the legend
        this.sales_count_monthly_2004_chart.getStyler().setLegendVisible(false);

      } catch(Exception e){
        throw new Exception(e);
      }
    }

    // Part 1Cc setter: sales_count_weekly_2004
    public void set_sales_count_weekly_2004(List<Integer>x,List<Integer>y) throws Exception{
      try{
        this.sales_count_weekly_2004_chart = new XYChartBuilder().width(400).height(400).title("sales count weekly 2004").build();
        this.sales_count_weekly_2004_chart.addSeries("sale_count_weekly_2004",x,y);
        this.panel1Cc = new XChartPanel(this.sales_count_weekly_2004_chart);
        // hide the legend
        this.sales_count_weekly_2004_chart.getStyler().setLegendVisible(false);

      } catch(Exception e){
        throw new Exception(e);
      }
    }



  // when finish adding all the plots, call the function below to show it
  // add all intermediate panels to the final panel and add final panel to jframe
  public void show_dashboard() throws Exception{
    try{
      this.finalPanel = new JPanel();
      // this.scroll = new JScrollPane(finalPanel);
      // add sub panels to final panel
      this.finalPanel.add(this.panel1Ca,BorderLayout.NORTH);
      this.finalPanel.add(this.panel1Cb,BorderLayout.NORTH);
      this.finalPanel.add(this.panel1Cc,BorderLayout.NORTH);

      this.finalPanel.add(this.panel5A,BorderLayout.NORTH);
      this.finalPanel.add(this.panel5B,BorderLayout.SOUTH);
      this.finalPanel.add(this.panel5C,BorderLayout.SOUTH);
      this.finalPanel.add(this.panel5D,BorderLayout.SOUTH);
      this.finalPanel.add(this.panel5E,BorderLayout.EAST);


      this.finalPanel.add(this.panel2A,BorderLayout.SOUTH);
      //..

      this.frame.add(this.finalPanel);
      // this.frame.add(this.scroll);
      this.frame.pack();
      this.frame.setVisible(true);

    } catch(Exception e){
      throw new Exception(e);
    }
  }

  // function 1 to add chart

};
