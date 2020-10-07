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
    private MainMainInterface mainInterface;

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
        try {
            mainInterface = new MainMainInterface();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            String query;

            // case strings are temporary, buttons aren't added yet
            switch(action) {
                case "Submit Query":
                    String text = getConsoleInput();
                    mainInterface.setQueryString(text);
                    mainInterface.switchOnFirstWord();
                    // send to main interface
                    break;
                case "Join Tables":
                    // prompts for extra input, such as specific tables
                    break;
                case "show list of tables":
                    query = "show tables";
                    mainInterface.setQueryString(query);
                    mainInterface.switchOnFirstWord();
                    break;
                case "show one or more columns of a specific table": {
                    String[] args = {"tableName", "colName"};
                    query = "show-specific-columns " + promptInput("Enter ALL for all columns, or list (e.g 1:3 for 1 and 3)", 2,  new String["tableName", "colName"]);
                    mainInterface.setQueryString(query);
                    mainInterface.switchOnFirstWord();
                } break;
                case "jdb-show-related-tables": {
                    String[] args = {"tableName"};
                    query = promptInput("jdb-show-related-tables", 1,  args);
                    mainInterface.setQueryString(query);
                    mainInterface.switchOnFirstWord();
                    // Send to main interface
                    break;
                }
                case "jdb-show-all-primary-keys":
                    // No command String[] args needed
                    query = "jdb-show-all-primary-keys;";
                    mainInterface.setQueryString(query);
                    mainInterface.switchOnFirstWord();
                    break;
                case "jdb-find-column": {
                    String[] args = {"columnName"};
                    query = promptInput("jdb-find-column", 1, args);
                    mainInterface.setQueryString(query);
                    mainInterface.switchOnFirstWord();
                    break;
                }
                case "jdb-search-path": {
                    String[] args = {"tableName1", "tableName2"};
                    query = promptInput("jdb-search-path", 2, args);
                    mainInterface.setQueryString(query);
                    mainInterface.switchOnFirstWord();
                    break;
                }
                case "jdb-search-and-join": {
                    String[] args = {"numToShow"};
                    query = promptInput("jdb-show-best-salesperson", 1, args);
                    mainInterface.setQueryString(query);
                    mainInterface.switchOnFirstWord();
                    break;
                }
                case "jdb-stat":
                    break;
                case "jdb-show-sales-monthly": {
                    String[] args = {"year"};
                    query = promptInput("jdb-show-sales-monthly", 1, args);
                    mainInterface.setQueryString(query);
                    mainInterface.switchOnFirstWord();
                    break;
                }
                case "jdb-customer-orders":
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

