


import javax.swing.*;
import java.awt.*;
// import java.awt.event.*;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
// import java.net.MalformedURLException;
// import java.awt.Desktop;
// import java.net.URI;
import java.io.*;
import java.util.*;

//
// import org.jgrapht.*;
// import org.jgrapht.graph.*;
// import org.jgrapht.nio.*;
// import org.jgrapht.nio.dot.*;
// import org.jgrapht.traverse.*;
// import org.jgrapht.alg.connectivity.*;
// import org.jgrapht.alg.interfaces.*;
// import org.jgrapht.graph.*;


// this class could be used to show tables--- first 3 GUI tasks in phase 3

/******************************************

JAVA - PLOT - SCHEMA : PNG FILE WILL BE GENERATED IN MainInterface and pass the filename to be read in TableGui class

*******************************************/
// https://stackoverflow.com/questions/19373186/copying-a-2d-string-arraylist


public class TableGUI extends JFrame{

  // pass the output 2d arrays to display in TableGUI
  // ArrayList<ArrayList<String>> table_to_show = new ArrayList<ArrayList<String>>();
  private String [][] table_to_show;
  // mode = 0 : show list of table -- will be 2d since 1st column will be number 0,1,2...n
  // mode = 1 : show columns/one column of a table
  // mode = 2 : show result of joint-tables
  // mode = 4 : plot the schema
  // private int mode = -1;
  // private JPanel main_panel;
  // private JLabel main_label;
  private ImageIcon image;
  private final int width_base = 800;
  private final int height_base = 600;
  private JTable jtable;
  private String [] cols;
  private JScrollPane scroll;
  // public TableGUI(){
  //   System.out.println("something wrong! Defulat constructor o TABLEGUI being called");
  //   System.exit(1);
  // }

  public TableGUI(ArrayList<ArrayList<String>> table_being_passed,ArrayList<String> columns){
    try{
      this.setSize(width_base,height_base);
      this.setTitle("Present table in GUI");
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      // this.setLayout(new BorderLayout());

      // initialize mode
      // mode = m;
      // main_panel = null;
      // main_label = null;
      // image = null;

      // // copy the 2d arraylist
      // for(int i = 0; i < table_being_passed.size();i++){
      //   ArrayList<String> curr_list = table_being_passed.get(i);
      //   table_to_show.add(curr_list);
      // }


      // convert 2d arraylist to array
      table_to_show = new String[table_being_passed.size()][table_being_passed.get(0).size()];
      // String[][] array = new String[arrayList.size()][];
      for (int i = 0; i < table_being_passed.size(); i++) {
        ArrayList<String> row = table_being_passed.get(i);
        table_to_show[i] = row.toArray(new String[row.size()]);
      }



      // print to check
      for(int i = 0; i < table_to_show.length;i++){
        for(int j = 0; j < table_to_show[i].length;j++){
          System.out.print(table_to_show[i][j] + " ");
        }
        System.out.println(" ");
      }


      // convert columns;
      cols = new String[columns.size()];
      for(int i=0; i<columns.size();i++){
        cols[i] = columns.get(i);
      }

      //print to check
      System.out.println("*********cols**************");
      for(int i = 0 ; i<cols.length;i++){
        System.out.print(cols[i]);
      }

      // main_panel = new JPanel();
      // scroll = new JScrollPane();
      jtable = new JTable(table_to_show,cols);
      jtable.setShowGrid(true);
      // scroll.add(jtable);
      // main_panel.add(jtable);
      // main_panel.add(scroll);


      // add(main_panel);
      add(new JScrollPane(jtable));
      // pack();
      setVisible(true);


    } catch(Exception e){
      System.out.println("Something wrong in constructor");
      e.printStackTrace();
    }
  }// end constructor


  // public void display_table(){
  //
  //
  // }

}
