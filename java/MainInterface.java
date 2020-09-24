/*
    Phase 1 JDBC Interfacing, Group 5

    Written by Noah Miner, Allen Yang,
    Evelyn Tang, Julie Herrick, George Lan
*/

import java.sql.*;
import java.util.Scanner;

public class MainInterface {

    // driver and docker url 
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost:3306";

    // Database credentials
    // going to be static for now, maybe we can implement a login later
    static final String USER = "root";
    static final String PASS = "password";

    // main loop to log into and interact with database
    public static void main(String[] args) {
        Connection conn = null; 
        Statement stmt = null; 
        try {
            // Connection stuff
            Class.forName("com.mysql.jdbc.Driver");

            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();

            String query = "USE adventureworks;";
            ResultSet rs;
            ResultSetMetaData rsmd;
            rs = stmt.executeQuery(query);

            // for basic SQL commands and calling custom functions
            Scanner sc = new Scanner(System.in);
            boolean loop = true;
            while (loop) {
                System.out.print("jdb> ");
                String command = sc.nextLine();
                String parsed_command[] = command.split(" ");

                if (!command.contains("create")) {
                    switch (parsed_command[0]) {
                        case "q":
                            loop = false;
                            break;
                        case "quit":
                            loop = false;
                            break;
                        case "jdb-show-related-tables":
                            System.out.println("jdb-show-related-tables");
                            break;
                        case "jdb-show-all-primary-keys":
                            System.out.println("Show primary keys");
                            break;
                        case "jdb-find-column":
                            System.out.println("Find column");
                            break;
                        case "jdb-search-path":
                            System.out.println("Search path");
                            break;
                        case "jdb-search-and-join":
                            System.out.println("Search and join");
                            break;
                        case "jdb-get-view": 
                            System.out.println("Get view");
                            break;
                        case "jdb-stat":
                            System.out.println("jdb-stat");
                            break;
                        default: // basic sql commands 
                            rs = stmt.executeQuery(command);
                            rsmd = rs.getMetaData();
                            int cols = rsmd.getColumnCount();

                            // Printing column names
                            for (int i = 1; i <= cols; i++) {
                                if (i > 1) 
                                    System.out.print(", ");
                                System.out.print(rsmd.getColumnName(i));
                            }
                            System.out.println("");

                            // Printing query contents
                            while (rs.next()) {
                                for (int i = 1; i <= cols; i++) {
                                    if (i > 1) 
                                        System.out.print(", ");
                                    String colVal = rs.getString(i);
                                    System.out.print(colVal);
                                }
                                System.out.println("");
                            }
                            break;
                    }
                } else {
                    System.out.println("Cannot edit database.");
                }
            }

            // Closing scanner
            sc.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
         } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
         } finally {
            //finally block used to close resources
            try {
               if (stmt!=null)
                  stmt.close();
            } catch (SQLException se2) {}

            try {
               if (conn!=null)
                  conn.close();
            } catch (SQLException se) {
               se.printStackTrace();
            }
         }
    }
}
