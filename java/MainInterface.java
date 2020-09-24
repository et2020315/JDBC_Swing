/*
    Phase 1 JDBC Interfacing, Group 5

    Written by Noah Miner, Allen Yang,
    Evelyn Tang, Julie Herrick, George Lan
*/

import java.sql.*;
import java.util.*;
import java.lang.*;

public class MainInterface {

    // driver and docker url
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/?serverTimezone=UTC#/";

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

            database_meta(conn,stmt);


            //
            // while (loop) {
            //     System.out.print("jdb> ");
            //     String command = sc.nextLine();
            //     String parsed_command[] = command.split(" ");
            //
            //     if (!command.contains("create")) {
            //         switch (parsed_command[0]) {
            //             case "q":
            //                 loop = false;
            //                 break;
            //             case "quit":
            //                 loop = false;
            //                 break;
            //             case "jdb-show-related-tables":
            //                 System.out.println("jdb-show-related-tables");
            //                 break;
            //             case "jdb-show-all-primary-keys":
            //                 System.out.println("Show primary keys");
            //                 break;
            //             case "jdb-find-column":
            //                 System.out.println("Find column");
            //                 break;
            //             case "jdb-search-path":
            //                 System.out.println("Search path");
            //                 break;
            //             case "jdb-search-and-join":
            //                 System.out.println("Search and join");
            //                 break;
            //             case "jdb-get-view":
            //                 System.out.println("Get view");
            //                 break;
            //             case "jdb-stat":
            //                 System.out.println("jdb-stat");
            //                 break;
            //             default: // basic sql commands
            //                 rs = stmt.executeQuery(command);
            //                 rsmd = rs.getMetaData();
            //                 int cols = rsmd.getColumnCount();
            //
            //                 // Printing column names
            //                 for (int i = 1; i <= cols; i++) {
            //                     if (i > 1)
            //                         System.out.print(", ");
            //                     System.out.print(rsmd.getColumnName(i));
            //                 }
            //                 System.out.println("");
            //
            //                 // Printing query contents
            //                 while (rs.next()) {
            //                     for (int i = 1; i <= cols; i++) {
            //                         if (i > 1)
            //                             System.out.print(", ");
            //                         String colVal = rs.getString(i);
            //                         System.out.print(colVal);
            //                     }
            //                     System.out.println("");
            //                 }
            //                 break;
            //         }
            //     } else {
            //         System.out.println("Cannot edit database.");
            //     }
            // }

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
    }// end main

    // helper function build a map
    public static void database_meta(Connection conn, Statement stmt){
      try{
        // ArrayList<ArrayList<String>> adjmatx = new ArrayList<ArrayList<String>>();
        ArrayList<String> column_name = new ArrayList<String>(); // if not convinient, change to map
        ArrayList<ArrayList<String>> tbl_col = new ArrayList<ArrayList<String>>();
        Map<String,ArrayList<String>> adj_list_by_column = new HashMap<String,ArrayList<String>>();
        // Integer count = 0;
        String tablename;

        DatabaseMetaData metaData = conn.getMetaData();
        String[] types = {"TABLE"};
        ResultSet tables = metaData.getTables(null, null, "%", types);

        // O(n)
        while (tables.next()) {
         tablename = tables.getString("TABLE_NAME");
         // table_name_map.put(tablename,count);
         column_name.add(tablename);
        }

        // checking
        // for(int i = 0; i < column_name.size();i++){
        //   System.out.println(column_name.get(i));
        // }

        System.out.println("***************************************");
        System.out.println("***************************************");

        String query1 = "select table_name,column_name from information_schema.columns where table_schema = 'adventureworks' order by table_name,column_name";

        stmt = conn.createStatement();
        ResultSet rs1;
        ResultSetMetaData rsmd1;
        rs1 = stmt.executeQuery(query1);
        rsmd1 = rs1.getMetaData();
        while(rs1.next()){
          // System.out.println("---------");
          ArrayList<String> temp1 = new ArrayList<String>();
          for(int j=1;j <= rsmd1.getColumnCount();j++){
            temp1.add(rs1.getString(j));
          }
          tbl_col.add(temp1);
        }

        // // prinout to check built table
        // for(int i = 0; i < tbl_col.size();i++){
        //   for(int k=0;k < tbl_col.get(i).size();k++){
        //     System.out.print(tbl_col.get(i).get(k) + " ");
        //   }
        //   System.out.println();
        // }



        // building adj list
        for(int i=0;i<tbl_col.size();i++){
          // ith item's 2nd column
          String colstr = tbl_col.get(i).get(1);
          String curr_tbl = tbl_col.get(i).get(0);
          // not being added
          if(adj_list_by_column.get(colstr)==null){
            // ith item's 1st column, make a new array
            ArrayList<String> temp2 = new ArrayList<String>();
            temp2.add(curr_tbl);
            adj_list_by_column.put(colstr,temp2);
            temp2 = null;
          } else {
            // System.out.println(adj_list.get(colstr).getClass
            // System.out.println(curr_tbl);
            ArrayList<String> temp2 = adj_list_by_column.get(colstr);
            temp2.add(curr_tbl); // append
            adj_list_by_column.replace(colstr,temp2); // update
            // System.out.println(colstr + temp2.size());

          }
        }


        // print for checking
        for(String k: adj_list_by_column.keySet()){
          System.out.println("******************* "+ k +" ******************");
          for(int j=0;j<adj_list_by_column.get(k).size();j++){
            System.out.println(adj_list_by_column.get(k).get(j));
          }
        }






      } catch(Exception e){
        System.out.println(e);
        e.printStackTrace();
      }
    }



    // search path, print out path
    public ArrayList<String> search_path(String tb1, String tb2){
      try{



      } catch(Exception e){

      }
      return new ArrayList<String>(Arrays.asList("howdy"));
    }// end search_path

    // search and join tables
    public ArrayList<String> get_views(String view_name, String view_content){

      return new ArrayList<String>(Arrays.asList("howdy"));
    }



}
