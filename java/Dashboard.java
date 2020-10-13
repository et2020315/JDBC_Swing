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

  ResultSet rs_num_of_order_yearly;
  ResultSet rs_num_of_order_monthly;
  ResultSet rs_num_of_order_weekly;

  ResultSet rs_total_sales_yearly;
  ResultSet rs_total_sales_monthly;
  ResultSet rs_total_sales_weekly;

  ResultSet rs_num_of_customer_yearly;
  ResultSet rs_num_of_customer_monthly;
  ResultSet rs_num_of_order_weekly;

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
