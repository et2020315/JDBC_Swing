/*
* MainFrame.java - a frame class to accept queries from user using jtextfield and create a button to submit
*
*/


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
// import java.awt.event.*;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
// import java.net.MalformedURLException;
// import java.awt.Desktop;
// import java.net.URI;
import java.io.*;
import java.util.*;

// MainFrame class as a JPanel,
public class MainFrame extends JFrame {
    private JFrame window = new JFrame("Main Window");

    // -- PHASE 3 GUI COMPONENTS -- //
    private JPanel phase3Panel = new JPanel();
    // JPanel components
    private JButton submitQuery = new JButton("Submit Query");
    private JTextField console = new JTextField("Enter query here");
    private JTextArea display = new JTextArea();

    // Labels for GUI clarity
    private JLabel consoleLabel = new JLabel();
    private JLabel displayLabel = new JLabel();
    // -- END PHASE 3 COMPONENTS -- //

    // These can be changed
    private int WIDTH = 700;
    private int HEIGHT = 800;

    // Constructor for MainFrame
    MainFrame() {
        phase3Panel.setLayout(null);

        // JButtons, add actionlistener here
        submitQuery.setBounds(10, 100 + HEIGHT / 3, 120, 30);
        submitQuery.addActionListener(new ButtonListener());
        phase3Panel.add(submitQuery);

        // This is where query results are displayed
        display.setEditable(false);
        display.setText("Results");
        display.setBounds (10, 50, WIDTH - 2*10, HEIGHT / 3);
        phase3Panel.add(display);

        console.setBounds(10, 60 + HEIGHT / 3, WIDTH - 2*10, 40);
        phase3Panel.add(console);

        // adding JPanel to MainFrame
        this.add(phase3Panel);
        this.setPreferredSize(new Dimension(700, 800));
        this.pack();
        this.setVisible(true);
    }

    // ButtonListener that handles logic for specific button presses
    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String action = e.getActionCommand();

            switch(action) {
                case "Submit Query":
                    String text = getConsoleInput();
                    System.out.println("Button pressed");
                    // send to main interface
                    // get results
                    // output to display, perhaps with another function
                    break;
                case "Join Tables":
                    // prompts for extra input, such as specific tables
                    break;
            }
        }
    }

    // for getting textfield contents
    public String getConsoleInput() {
        return console.getText();
    }

    // Uncomment this and run this file alone to test
    public static void main (String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.add(new MainFrame());
        testFrame.setPreferredSize(new Dimension(700, 800));
        testFrame.pack();
        testFrame.setVisible(true);
    }
}

