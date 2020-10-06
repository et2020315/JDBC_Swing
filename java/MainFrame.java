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

            // case strings are temporary, buttons aren't added yet
            switch(action) {
                case "Submit Query":
                    String text = getConsoleInput();
                    promptInput("SubmitQuery", 2, args);
                    // send to main interface
                    break;
                case "Join Tables":
                    // prompts for extra input, such as specific tables
                    break;
                case "show list of tables":
                    String query = "show tables";
                    // send
                    break;
                case "show one or more columns of a specific table":

                    break;
                case "jdb-show-related-tables":
                    String[] args = {"tableName"};
                    String query = promptInput("jdb-show-related-tables", 1, args);
                    // Send to main interface
                    break;
                case "jdb-show-all-primary-keys":
                    // No command args needed
                    String query = "jdb-show-all-primary-keys;";
                    // send to MainInterface
                    break;
                case "jdb-find-column":
                    String[] args = {"columnName"};
                    String query = promptInput("jdb-find-column", 1, args);
                    // send to Main Interface
                    break;
                case "jdb-search-path":
                    break;
                case "jdb-search-and-join":
                    break;
                case "jdb-stat":
                    break;
            }
        }

        // creates JInputDialog to gather parameters for MainMainInterface functions
        // fields is how many parameters, and fieldStrings specifies parameter names
        public String promptInput(String function, int fields, String[] fieldStrings) {
            JPanel pane = new JPanel();
            pane.setLayout(new GridLayout(2, fields, 2, 2));

            JTextField[] textFields = new JTextField[fields];

            // Initiliaze text fields and add to pane with label
            for (int i = 0; i < fields; i++) {
                textFields[i] = new JTextField();
                pane.add(new JLabel("Enter " + fieldStrings[i] + ": "));
                pane.add(textFields[i]);
            }

            String[] output = new String[fields];
            String query = "";
            boolean missingVar = false;
            int option = JOptionPane.showConfirmDialog(window, pane, "Enter " + function + " parameters", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                for (int i = 0; i < fields; i++) {
                    output[i] = textFields[i].getText();
                    if (output[i] == null) {
                        // error catch
                        missingVar = true;
                    }
                    query += output[i] + " ";
                }
            }

            // we could go to mainframe from here, or do it in buttonlistener
            System.out.println(query);
            return function + " " + query + ";";
        }
    }

    // for getting textfield contents
    public String getConsoleInput() {
        return console.getText();
    }

    // Uncomment this and run this file alone to test
    public static void main (String[] args) {
        MainFrame testFrame = new MainFrame();
    }
}

