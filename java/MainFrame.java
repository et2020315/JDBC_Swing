/*
* MainFrame.java - a frame class to accept queries from user using jtextfield and create a button to submit
*
*/


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

public class MainFrame extends JFrame{
    // JPanel components
    private JButton submitQuery = new JButton("Submit Query");
    private JTextField console = new JTextField("Enter query here");
    private JTextArea display = new JTextArea();

    // Labels for GUI clarity
    private JLabel consoleLabel = new JLabel();
    private JLabel displayLabel = new JLabel();

    // These can be changed
    private int WIDTH = 700;
    private int HEIGHT = 800;

    // Constructor for JPanel
    MainFrame() {
        this.setLayout(null);

        // JButtons, add actionlistener here
        submitQuery.setBounds(10, 100 + HEIGHT / 3, 120, 30);
        this.add(submitQuery);

        // This is where query results are displayed
        display.setEditable(false);
        display.setText("Results");
        display.setBounds (10, 50, WIDTH - 2*10, HEIGHT / 3);
        this.add(display);

        customFunctionsDropDown.add(showRelatedTables);
        customFunctionsDropDown.setBounds(140, 30, 50, 30);
        this.add(customFunctionsDropDown);

        console.setBounds(10, 60 + HEIGHT / 3, WIDTH - 2*10, 40);
        this.add(console);
    }

    /* Uncomment this and run this file alone to test
    public static void main (String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.add(new MainFrame());
        testFrame.setPreferredSize(new Dimension(700, 800));
        testFrame.pack();
        testFrame.setVisible(true);
    }*/
}
