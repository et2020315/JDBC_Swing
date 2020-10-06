


import javax.swing.*;
import java.awt.*;
// import java.awt.event.*;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.io.*;
import java.util.*;



// this class could be used to show tables--- first 3 GUI tasks in phase 3

// https://stackoverflow.com/questions/19373186/copying-a-2d-string-arraylist


public class TableGUI extends JFrame{


  // must use 2D primitive String array here
  private String [][] table_to_show;
  private ImageIcon image;
  private final int width_base = 800;
  private final int height_base = 600;
  private JTable jtable;
  private String [] cols;
  private JScrollPane scroll;


  // constructor of the TableGUI class. The constructor will pop up the image
  public TableGUI(ArrayList<ArrayList<String>> table_being_passed,ArrayList<String> columns){
    try{
      this.setSize(width_base,height_base);
      this.setTitle("Present table in GUI");
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


      // convert 2d arraylist to 2d primitive array
      table_to_show = new String[table_being_passed.size()][table_being_passed.get(0).size()];
      // String[][] array = new String[arrayList.size()][];
      for (int i = 0; i < table_being_passed.size(); i++) {
        ArrayList<String> row = table_being_passed.get(i);
        table_to_show[i] = row.toArray(new String[row.size()]);
      }

      // // print to check, save for debugging
      // for(int i = 0; i < table_to_show.length;i++){
      //   for(int j = 0; j < table_to_show[i].length;j++){
      //     System.out.print(table_to_show[i][j] + " ");
      //   }
      //   System.out.println(" ");
      // }

      // convert array list of column names to string array to pass into JTable
      cols = new String[columns.size()];
      for(int i=0; i<columns.size();i++){
        cols[i] = columns.get(i);
      }

      // //print to check, save for debugging
      // System.out.println("*********cols**************");
      // for(int i = 0 ; i<cols.length;i++){
      //   System.out.print(cols[i]);
      // }

      // JTable element requires Object [][] for the table you want to show and Object[] for column row
      jtable = new JTable(table_to_show,cols);
      jtable.setShowGrid(true);
      // use the JTable to instantiate ScrollPane, required
      // add JScrollPane to JPanel
      add(new JScrollPane(jtable));
      // set the panel to be visible
      setVisible(true);


    } catch(Exception e){
      System.out.println("Something wrong in constructor");
      e.printStackTrace();
    }
  }// end constructor


}
