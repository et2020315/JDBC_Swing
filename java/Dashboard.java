import javax.swing.*;
import java.util.*;
// import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.*;
import java.awt.BorderLayout;
import java.sql.*; // resultset



public class Dashboard{

  JFrame frame;
  JPanel finalpanel;
  JPanel panel1Aa; // small display window for part 1 - A - a
  JPanel panel5A; // small display area for part 5 - A
  JPanel panel5B;
  JPanel finalPanel;

  // JLabel label5A;
  // JLabel label5B;
  JScrollPane scroll; // added when needed


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
      this.customer_birth_year_chart = new PieChartBuilder().width(300).height(300).title(getClass().getSimpleName()).build();
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
      this.customer_gender_chart = new PieChartBuilder().width(300).height(300).title(getClass().getSimpleName()).build();
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
      this.finalPanel.add(this.panel5A,BorderLayout.NORTH);
      this.finalPanel.add(this.panel5B,BorderLayout.SOUTH);

      this.frame.add(this.finalPanel);
      this.frame.pack();
      this.frame.setVisible(true);

    } catch(Exception e){
      throw new Exception(e);
    }
  }

  // function 1 to add chart

};
