


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


import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.nio.*;
import org.jgrapht.nio.dot.*;
import org.jgrapht.traverse.*;
import org.jgrapht.alg.connectivity.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.graph.*;


// this class could be used to show tables--- first 3 GUI tasks in phase 3

/******************************************

JAVA - PLOT - SCHEMA : PNG FILE WILL BE GENERATED IN MainInterface and pass the filename to be read in TableGui class

*******************************************/
// https://stackoverflow.com/questions/19373186/copying-a-2d-string-arraylist


public class TableGUI extends JFrame{

  // pass the output 2d arrays to display in TableGUI
  ArrayList<ArrayList<String>> table_to_show = new ArrayList<ArrayList<String>>();
  // mode = 0 : show list of table -- will be 2d since 1st column will be number 0,1,2...n
  // mode = 1 : show columns/one column of a table
  // mode = 2 : show result of joint-tables
  // mode = 4 : plot the schema
  private Integer mode = -1;
  private JPanel a_panel;
  private JLabel a_label;
  private ImageIcon image;
  private final int width_base = 800;
  private final int height_base = 600;
  // public TableGUI(){
  //   System.out.println("something wrong! Defulat constructor o TABLEGUI being called");
  //   System.exit(1);
  // }

  public TableGUI(ArrayList<ArrayList<String>> table_being_passed,Integer m){
    try{
      setSize(width_base,height_base);
      setTitle("Table shown");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLayout(new BorderLayout());


      // initialize mode
      mode = m;
      a_panel = null;
      a_label = null;
      image = null;

      // copy the 2d arraylist
      for(int i = 0; i < table_being_passed.size();i++){
        ArrayList<String> curr_list = table_being_passed.get(i);
        table_to_show.add(curr_list);
      }


      // print to check
      for(int i = 0; i < table_to_show.size();i++){
        for(int j = 0; j < table_to_show.get(i).size();j++){
          System.out.print(table_to_show.get(i).get(j) + " ");
        }
        System.out.println(" ");
      }






    } catch(Exception e){
      System.out.println("Something wrong in constructor");
      e.printStackTrace();
    }
  }// end constructor


  public void display_table(){


  }

}
