import javax.swing.*;
import java.util.*;
// import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.*;
// import java.awt.*;
import java.sql.*; // resultset



public class Dashboard{

  JFrame frame;
  JPanel finalpanel;
  JPanel panel1Aa; // small display window for part 1 - A - a
  JPanel panel5A; // small display area for part 5 - A
  JPanel panel5B;
  JPanel finalPanel;
  JScrollPane scroll; // added when needed


  // Part 1, Part A: customer data
  // panel 1Aa
  // List<Integer> list_num_customer_yearly_x; // year from 2001 - 2004
  // List<Integer> list_num_customer_yearly_y; // num of customer each year
  // // panel 1Ab
  // List<Integer> list_num_customer_monthly_2002_x; // month from 1 - 12
  // List<Integer> list_num_customer_monthly_2002_y; // num of customer each month in 2002
  // // panel 1Ac
  // List<Integer> list_num_customer_weekly_2002_x; // week from 0 - 52
  // List<Integer> list_num_customer_weekly_2002_y; // num of customer each week in 2002

  //
  // Part 1, Part B: sales amount data
  // panel 1Ba
  // List<Integer> list_sales_amount_yearly_x; // year from 2001 - 2004
  // List<Double> list_sales_amount_yearly_y; // sum of total sales in dollor for each year
  // // panel aBb
  // List<Integer> list_sales_amount_monthly_2003_x; // month in 2003, 1- 12
  // List<Double> list_sales_amount_monthly_2003_y; // sum of total sales in dollars for each month in 2003
  // // panel 1Bc
  // List<Integer> list_sales_amount_weekly_2003_x; // week in 2003, 0 - 52
  // List<Double> list_sales_amount_weekly_2003_y; // sum of total sales in dollars for each month in 2003


  // // Part 1, Part C: sales order count data
  // // panel 1Ca
  // List<Integer> list_sales_order_yearly_x; // year, 2001 - 2004
  // List<Integer> list_sales_order_yearly_y; // num of sales for each year
  //
  // // panel 1Cb
  // List<Integer> list_sales_count_monthly_2004_x; // month 1 - 7
  // List<Integer> list_sales_count_monthly_2004_y; // num of sales for 2004, record only has data from jan to july
  //
  // // panel 1Cc
  // List<Integer> list_sales_count_weekly_2004_x; // week , 0 - 31
  // List<Integer> list_sales_count_weekly_2004_y; // num of sales for 2004


  //
  // // for employee stats' histogram bins and bin name
  //
  // // Part 2, Part A: birth year - histogram
  // // panel 2Aa
  // List<String> list_employee_birth_bin_name_x; // in mainmainInterface cast the element you get of getInteger function and make strings like "1940 - (1940 + 10)"
  // List<Integer> list_employee_birth_bin_count_y;
  //
  // // Part 2, Part B: salary
  // // panel 2Ba
  // List<String> list_employee_salary_bin_name_x;
  // List<Integer> list_employee_salary_bin_count_y;
  // double employee_salary_median;
  // double employee_salary_average;
  //
  //
  // // Part 3, Part A: regional statistics for sales
  // // panel 3Aa
  // List<String> list_regional_sales_count_x; // statename for sales count, String, category chart
  // List<Integer> list_regional_sales_count_y; // sales count data, numeric
  // // panel 3Ab
  // List<String> list_regional_sales_amount_x; // statenames, strings, for category chart
  // List<Double> list_regional_sales_amount_y; // dollars of sales for each state
  // // panel 3Ac
  // List<String> list_regional_sales_customer_x; // statenames, strings, for category chart
  // List<Integer> list_regional_sales_customer_y; // num of cutomer in each state
  //
  // // Part 3, Part B: regional statistics for employee stats
  // // panel 3Ba
  // List<String> list_regional_aggregate_sum_rate_x; // statename, strings
  // List<Double> list_regional_aggregate_sum_rate_y; // sum of employee payrate for each state

  //
  // // Part 4: list of top 10 products by season of all 4 years
  // // panel 4A
  // List<String> list_product_top_10_Jan_to_Mar_x; // name of the top 10 product from January to March
  // List<Integer> list_product_top_10_Jan_to_Mar_y; // counts of the top 10 product from January to March
  // // panel 4B
  // List<String> list_product_top_10_Apr_to_Jun_x;
  // List<Integer> list_product_top_10_Apr_to_Jun_y;
  // // panel 4C
  // List<String> list_product_top_10_Jul_to_Sep_x;
  // List<Integer> list_product_top_10_Jul_to_Sep_y;
  // // panel 4D
  // List<String> list_product_top_10_Oct_to_Dec_x;
  // List<Integer> list_product_top_10_Oct_to_Dec_y;


  //
  // // Part 5: customer demographics
  // // if we need age, change the x bin manually to "(2020 - 1930) -  (2020-1940)"
  // // panel 5A
  // List<String> list_customer_birth_year_x; // birth year bin name like 1930 - 1940, 1940 - 1950 ...
  // List<Integer> list_customer_birth_year_y; // count for each bin
  // // panel 5B
  // List<String> list_customer_gender_x; // gender types
  // List<Integer> list_customer_gender_y; // count for each type
  // // Panel 5C
  // List<String> list_customer_education_x; // education types
  // List<Integer> list_customer_education_y; // count for each type
  // // panel 5D
  // List<String> list_customer_marrital_status_x; // Maritalstatus type: Married or not married
  // List<Integer> list_customer_marrital_status_y; // count for each type
  // // panel 5E
  // List<String> list_customer_yearly_income_x; // yearly income range type, strings already
  // List<Integer> list_customer_yearly_income_y; // count for each type



  // chart members
  PieChart customer_birth_year_chart;
  PieChart customer_gender_chart;
  PieChart customer_education_chart;
  PieChart customer_marrital_status_chart;
  PieChart customer_yearly_incom_chart;


  // constructor
  Dashboard(){
    this.frame = new JFrame("dashboard for adventureworks");
    this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.frame.setSize(1000,1000);


    this.frame.setVisible(false);
  }

  // functions to initiate those member lists,
  public void set_customer_birth_year(List<String> x, List<Integer> y) throws Exception{
    try{
      // add the pie chart
      this.customer_birth_year_chart = new PieChartBuilder().width(200).height(200).title(getClass().getSimpleName()).build();
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

  public void set_customer_gender(List<String> x, List<Integer>y) throws Exception{
    try{
      this.customer_gender_chart = new PieChartBuilder().width(500).height(500).title(getClass().getSimpleName()).build();
      for(int i = 0; i < x.size(); i++){
        this.customer_gender_chart.addSeries(x.get(i),y.get(i));
      }
      this.panel5B = new XChartPanel(this.customer_gender_chart); // must be declared as JPanel type but create new XChartPanel

    } catch(Exception e){
      throw new Exception(e);
    }
  }

  public void set_customer_education(List<String>x, List<Integer>y)throws Exception {
    try{


    } catch (Exception e){
      throw new Exception(e);
    }
  }

  // when finish adding all the plots, call the function below to show it
  // add all intermediate panels to the final panel and add final panel to jframe
  public void show_dashboard() throws Exception{
    try{
      this.finalPanel = new JPanel();
      // add sub panels to final panel
      this.finalPanel.add(this.panel5A);
      this.finalPanel.add(this.panel5B);

      this.frame.add(this.finalPanel);
      this.frame.pack();
      this.frame.setVisible(true);

    } catch(Exception e){
      throw new Exception(e);
    }
  }

  // function 1 to add chart

};
