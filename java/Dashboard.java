import javax.swing.*;
import java.util.*;
// import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.*;
import java.awt.Borderlayout;
import java.sql.*; // resultset



public class Dashboard{

  JFrame frame;
  JPanel finalpanel;
  JPanel panel1; // small display window for 1st chart
  JPanel panel2; // small display area for 2nd chart
  JPanel panel3; // small display area for 3rd chart
  JScrollPane scroll; // added when needed


  // Part 1, Part A: customer data
  List<Integer> list_num_customer_yearly_x; // year from 2001 - 2004
  List<Integer> list_num_customer_yearly_y; // num of customer each year

  List<Integer> list_num_customer_monthly_2002_x; // month from 1 - 12
  List<Integer> list_num_customer_monthly_2002_y; // num of customer each month in 2002

  List<Integer> list_num_customer_weekly_2002_x; // week from 0 - 52
  List<Integer> list_num_customer_weekly_2002_y; // num of customer each week in 2002


  // Part 1, Part B: sales amount data
  List<Integer> list_sales_amount_yearly_x; // year from 2001 - 2004
  List<Double> list_sales_amount_yearly_y; // sum of total sales in dollor for each year

  List<Integer> list_sales_amount_monthly_2003_x; // month in 2003, 1- 12
  List<Double> list_sales_amount_monthly_2003_y; // sum of total sales in dollars for each month in 2003

  List<Integer> list_sales_amount_weekly_2003_x; // week in 2003, 0 - 52
  List<Double> list_sales_amount_weekly_2003_y; // sum of total sales in dollars for each month in 2003


  // Part 1, Part C: sales order count data
  List<Integer> list_sales_order_yearly_x; // year, 2001 - 2004
  List<Integer> list_sales_order_yearly_y; // num of sales for each year

  List<Integer> list_sales_count_monthly_2004_x; // month 1 - 7
  List<Integer> list_sales_count_monthly_2004_y; // num of sales for 2004, record only has data from jan to july

  List<Integer> list_sales_count_weekly_2004; // week , 0 - 31
  List<Integer> list_sales_count_weekly_2004; // num of sales for 2004



  // for employee stats' histogram bins and bin name

  // Part 2, Part A: birth year - histogram
  List<String> list_employee_birth_bin_name; // in mainmainInterface cast the element you get of getInteger function and make strings like "1940 - (1940 + 10)"
  List<Integer> list_employee_birth_bin_count;

  // Part 2, Part B: salary
  List<String> list_employee_salary_bin_name;
  List<Integer> list_employee_salary_bin_count;
  double employee_salary_median;
  double employee_salary_average;


  // Part 3, Part A: regional statistics for sales

  List<String> list_regional_sales_count_x; // statename for sales count, String, category chart
  List<Integer> list_regional_sales_count_y; // sales count data, numeric

  List<String> list_regional_sales_amount_x; // statenames, strings, for category chart
  List<Double> list_regional_sales_amount_y; // dollars of sales for each state

  List<String> list_regional_sales_customer_x; // statenames, strings, for category chart
  List<Integer> list_regional_sales_customer_y; // num of cutomer in each state

  // Part 3, Part B: regional statistics for employee stats
  List<String> list_regional_aggregate_sum_rate_x; // statename, strings
  List<Double> list_regional_aggregate_sum_rate_y; // sum of employee payrate for each state


  // Part 4: list of top 10 products by season of all 4 years
  List<String> list_product_top_10_Jan_to_Mar_x; // name of the top 10 product from January to March
  List<Integer> list_product_top_10_Jan_to_Mar_y; // counts of the top 10 product from January to March

  List<String> list_product_top_10_Apr_to_Jun_x;
  List<Integer> list_product_top_10_Apr_to_Jun_y;

  List<String> list_product_top_10_Jul_to_Sep;
  List<Integer> list_product_top_10_Jul_to_Sep;

  List<String> list_product_top_10_Oct_to_Dec;
  List<Integer> list_product_top_10_Oct_to_Dec;



  // Part 5: customer demographics
  // if we need age, change the x bin manually to "(2020 - 1930) -  (2020-1940)"
  List<String> list_customer_birth_year_x; // birth year bin name like 1930 - 1940, 1940 - 1950 ...
  List<Integer> list_customer_birth_year_y; // count for each bin

  List<String> list_customer_gender_x; // gender types
  List<Integer> list_customer_gender_y; // count for each type

  List<String> list_customer_education_x; // education types
  List<Integer> list_customer_education_y; // count for each type

  List<String> list_customer_marrital_status_x; // Maritalstatus type: Married or not married
  List<Integer> list_customer_marrital_status_y; // count for each type

  List<String> list_customer_yearly_income_x; // yearly income range type, strings already
  List<Integer> list_customer_yearly_income_y; // count for each type

  // constructor
  Dashboard(){
    frame = new JFrame("dashboard for adventureworks");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setSize(1000,1000);


    frame.pack();
    frame.setVisible(true);
  }

  // function to set resultset

  // function 1 to add chart

};
