/*
*
* ListGUI.java
*
*/



import javax.swing.*;
// import java.awt.*;
// import java.awt.event.*;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.io.*;
import java.util.*;



// this class could be used to show tables--- first 3 GUI tasks in phase 3

// https://stackoverflow.com/questions/19373186/copying-a-2d-string-arraylist


public class ListGUI extends JFrame{

  private final int width_base = 800;
  private final int height_base = 600;
  String passedList[];
  JList jlist;
  JPanel panel;


  public ListGUI(ArrayList<String> als){
    try{
      this.setSize(width_base,height_base);
      this.setTitle("Present table in GUI");
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // copy into String list
      passedList = new String[als.size()];
      for(int i = 0; i < als.size(); i++){
        passedList[i] = als.get(i);
      }

      jlist = new JList(passedList);

      panel.add(jlist);
      add(panel);

      this.setVisible(true);
    } catch(Exception e){
      System.out.println("Something wrong in constructor");
      e.printStackTrace();
    }
  }// end constructor

  public ListGUI(List ls){
    try{
      this.setSize(width_base,height_base);
      this.setTitle("Present table in GUI");
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // copy into String list
      passedList = new String[ls.size()];
      for(int i = 0; i < ls.size(); i++){
        passedList[i] = ls.get(i).toString();
      }

      jlist = new JList(passedList);

      panel.add(jlist);
      add(panel);


      this.setVisible(true);
    } catch(Exception e){
      System.out.println("SOmething wrong");
      e.printStackTrace();
    }
  }


}// end class
