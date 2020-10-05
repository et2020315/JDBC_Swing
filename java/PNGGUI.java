/*
* PNGGUI.java
*/


import javax.swing.*;
import java.awt.*;
// import java.awt.event.*;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.File;
// import java.net.MalformedURLException;
import java.awt.Desktop;
// import java.net.URI;
import java.io.*;
import java.util.*;


public class PNGGUI extends JFrame{

  private ImageIcon img_icon1;
  // private JPanel panel1;
  private JLabel img_label;
  private BufferedImage img_buf;
  private final int width_base = 1200;
  private final int height_base = 1000;

  public PNGGUI(String img){
    try{

      this.setSize(width_base,height_base);
      this.setTitle("Graph Schema - jdb-get-schema");
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      img_icon1 = new ImageIcon(img);
      img_label = new JLabel(img_icon1);
      add(img_label);

      this.setVisible(true);

    }
    catch(Exception e){
      System.out.println("******** Something wrong in constructor PNGGUI **********");
      e.printStackTrace();
    }
  }
}
