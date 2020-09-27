/*
    Phase 1 JDBC Interfacing, Group 5

    Written by Noah Miner, Allen Yang,
    Evelyn Tang, Julie Herrick, George Lan
*/

import java.sql.*;
import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.nio.*;
import org.jgrapht.nio.dot.*;
import org.jgrapht.traverse.*;
import org.jgrapht.alg.connectivity.*;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.graph.*;


public class MainInterface {

    // driver and docker url
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/?serverTimezone=UTC#/adventureworks";

    // Database credentials
    // going to be static for now, maybe we can implement a login later
    static final String USER = "root";
    static final String PASS = "password";


    static Map<String,ArrayList<String>> adj_list_by_column = new HashMap<String,ArrayList<String>>();
    static Map<String,ArrayList<String>> adj_list_by_table = new HashMap<String,ArrayList<String>>();

    static ArrayList<String> table_name = new ArrayList<String>();
    static Graph<String, DefaultWeightedEdge> table_matrix =
      new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);

    static Double ind = 0.0;
    static ArrayList<String> edgeName = new ArrayList<String>(); // weight == index
    static Map<String,String> view_def_map = new HashMap<String,String>();


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


            database_meta(conn,stmt,adj_list_by_column,adj_list_by_table,table_matrix);



            // test
            print_shortest_path(table_matrix,"employee","vendorcontact");
            get_view_for_user(conn,stmt,"howdy1","select employee.EmployeeID,purchaseorderheader.TotalDue from employee INNER JOIN purchaseorderheader ON (employee.EmployeeID=purchaseorderheader.EmployeeID) INNER JOIN vendorcontact ON (purchaseorderheader.VendorID=vendorcontact.VendorID) where TotalDue < 581",view_def_map);

            print_join_table(conn,stmt,table_matrix,"employee","vendorcontact",edgeName);


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
    public static void database_meta(Connection conn, Statement stmt,Map<String,ArrayList<String>> adj_list_by_column, Map<String,ArrayList<String>> adj_list_by_table,Graph<String, DefaultWeightedEdge> table_matrix){
      try{

        ArrayList<ArrayList<String>> tbl_col = new ArrayList<ArrayList<String>>();

        String tablename;

        DatabaseMetaData metaData = conn.getMetaData();
        String[] types = {"TABLE"};
        ResultSet tables = metaData.getTables(null, null, "%", types);

        // O(n)
        while (tables.next()) {
         tablename = tables.getString("TABLE_NAME");
         // System.out.println("table:"+tablename);
         // table_name.add(tablename);
         table_matrix.addVertex(tablename); // add table node to graph
        }

        // checking
        // for(int i = 0; i < table_name.size();i++){
        //   System.out.println(table_name.get(i));
        // }

        // System.out.println("***************************************");
        // System.out.println("***************************************");

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
        // for(String k: adj_list_by_column.keySet()){
        //   System.out.println("******************* "+ k +" ******************");
        //   for(int j=0;j<adj_list_by_column.get(k).size();j++){
        //     System.out.println(adj_list_by_column.get(k).get(j));
        //   }
        // }

        // building map for table: adj_list_by_table
        for(int i=0;i<tbl_col.size();i++){
          // ith item's 2nd column
          String colstr = tbl_col.get(i).get(1);
          String curr_tbl = tbl_col.get(i).get(0);

          if(!colstr.endsWith("ID")){
            // System.out.println("Does not contain");
            continue;
          }

          // not being added
          if(adj_list_by_table.get(curr_tbl)==null){
            // ith item's 1st column, make a new array
            ArrayList<String> temp2 = new ArrayList<String>();
            temp2.add(colstr);
            adj_list_by_table.put(curr_tbl,temp2);
            temp2 = null;
          } else {
            // System.out.println(adj_list.get(colstr).getClass
            // System.out.println(curr_tbl);
            ArrayList<String> temp2 = adj_list_by_table.get(curr_tbl);
            temp2.add(colstr); // append
            adj_list_by_table.replace(curr_tbl,temp2); // update
            // System.out.println(curr_tbl +temp2.size());
            temp2 = null;
          }
        }

        // System.out.println("-------------------------------------------------------------");
        // System.out.println("-----------------------------then table--------------------------------");
        //
        // for(String k: adj_list_by_table.keySet()){
        //   System.out.println("******************* "+ k +" ******************");
        //   for(int j=0;j<adj_list_by_table.get(k).size();j++){
        //     System.out.println(adj_list_by_table.get(k).get(j));
        //   }
        // }

        for(String k: adj_list_by_column.keySet()){
          // currently , id only
          if(!k.contains("ID")){
            continue;
          }
          ArrayList<String> temp3 = adj_list_by_column.get(k);
          for(int j =0;j< temp3.size();j++){
            for(int q=0;q<temp3.size();q++){
              if(q != j){
                DefaultWeightedEdge ee =  table_matrix.addEdge(temp3.get(j),temp3.get(q));
                table_matrix.setEdgeWeight(table_matrix.getEdge(temp3.get(j),temp3.get(q)), ind); // nullptrException if ee to set
                // System.out.println("index at: " + ind.toString() + " col:" + k + " j:" + temp3.get(j) + " q:" + temp3.get(q));
                // edgeName add the string, this could be either map or arraylist
                edgeName.add(k);
                ind += 1.0;
              }
            }
          }
        }

        // // test:
        // int stopping = 0;
        // for(DefaultWeightedEdge e : table_matrix.edgeSet()){
        //   System.out.println(table_matrix.getEdgeSource(e) + " --> " + table_matrix.getEdgeTarget(e));
        //   System.out.println(edgeName.get((int)table_matrix.getEdgeWeight(e))); // Note: double cannot be dereferebced error --  canot use intValue()
        //   if(stopping == 10){break;}
        // }



      } catch(Exception e){
        System.out.println(e);
        e.printStackTrace();
      }
    }



    public static void print_shortest_path(Graph<String, DefaultWeightedEdge> table_matrix,String tb1, String tb2){
      System.out.println("Shortest path from tb1 to tb2:");
      DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraAlg =
          new DijkstraShortestPath<>(table_matrix);
      SingleSourcePaths<String, DefaultWeightedEdge> iPaths = dijkstraAlg.getPaths(tb1);

      System.out.println("shortest path from table \""+tb1+"\" to table \"" + tb2 + "\":");
      System.out.println(iPaths.getPath(tb2).getVertexList() + "\n");
    }



    public static void print_join_table(Connection conn, Statement stmt,Graph<String, DefaultWeightedEdge> table_matrix,String tb1, String tb2, ArrayList<String>edgeName){
      try{
      System.out.println("Shortest path from tb1 to tb2:");
      DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraAlg =
          new DijkstraShortestPath<>(table_matrix);
      SingleSourcePaths<String, DefaultWeightedEdge> iPaths = dijkstraAlg.getPaths(tb1);

      System.out.println("shortest path from table \""+tb1+"\" to table \"" + tb2 + "\":");
      System.out.println(iPaths.getPath(tb2).getVertexList() + "\n");
      List<String> tablelist = iPaths.getPath(tb2).getVertexList();


      // retrieve on what columns into arraylist
      ArrayList<String> onclauseEdge = new ArrayList<String>();
      // retrieve edge string by weight of edge
      for(int j = 0; j < tablelist.size()-1;j++){
        // get edge name weight in graph
        double wt = table_matrix.getEdgeWeight(table_matrix.getEdge(tablelist.get(j),tablelist.get(j+1)));
        // get the string by weight/index in edgename
        int index = (int)wt;
        String columnName1 = edgeName.get(index);
        System.out.println("index:"+index);
        System.out.println("columnName1:"+columnName1);
        // append it to oncluaseEdge
        onclauseEdge.add(columnName1);
      }


      String query = "select * from ";
      System.out.println("tblistsize:"+tablelist.size());
      System.out.println("oncluasesize:"+onclauseEdge.size());
      // contruct tables names and inner join strings
      String middle = "";
      for(int j=0;j<tablelist.size();j++){

        if(j==0){
          query = query + tablelist.get(j);
        } else{
          String currT = tablelist.get(j);
          String prevT = tablelist.get(j-1);
          String col = onclauseEdge.get(j-1);
          query = query+" INNER JOIN "+currT+" ON ("+prevT+"."+col+"="+currT+"."+col+")";
        }
      }


      System.out.println(query);
      stmt = conn.createStatement();
      ResultSet rs2;
      ResultSetMetaData rsmd2;
      rs2 = stmt.executeQuery(query);
      rsmd2 = rs2.getMetaData();
      System.out.println("hellow");
      int countloop = 0;
      while(rs2.next()){

        for(int j=1;j <= rsmd2.getColumnCount();j++){
          String type = rsmd2.getColumnTypeName(j);

          // System.out.println("type:"+type);
          if(type.toLowerCase().contains("binary")){
            System.out.println(rsmd2.getColumnName(j)+": "+"some binary, print out make a noise");
          }else {
            System.out.println(rsmd2.getColumnName(j)+": "+rs2.getString(j));
          }

        }// end forloop
        System.out.println("*******************************************************");

      }


    }catch(Exception e){
      System.out.println("Something wrong in join table.");
      e.printStackTrace();
    }

    }// print join table


    public static void get_view_for_user(Connection conn, Statement stmt,String view_name,String view_def,Map<String,String> view_def_map){
      try{

        view_name = view_name.trim();
        String qry = "";

        // if esist in map
        if(view_def_map.containsKey(view_name) && view_def.equals("")){
          qry = view_def_map.get(view_name);
        }
        // if does not exist in map, a new qry comes in
        else if(!view_def_map.containsKey(view_name) && (!view_def.equals(""))) {
          qry = view_def;
          create_or_update_view(view_name,view_def,0);
        }
        // if exist and also new query, update
        else if(view_def_map.containsKey(view_name) && (!view_def.equals(""))) {
          qry = view_def;
          create_or_update_view(view_name,view_def,1);
        }
        else {
          System.out.println("third 489");
        }

        stmt = conn.createStatement();
        ResultSet rs;
        ResultSetMetaData rsmd;
        rs = stmt.executeQuery(qry);
        rsmd = rs.getMetaData();

        while(rs.next()){

          for(int j=1;j <= rsmd.getColumnCount();j++){
            String type = rsmd.getColumnTypeName(j);

            if(type.toLowerCase().contains("binary")){
              System.out.println(rsmd.getColumnName(j)+": "+"some binary, print out make a noise");
            }else {
              System.out.println(rsmd.getColumnName(j)+": "+rs.getString(j));
            }
          }
          System.out.println("*******************************************************");
        }// end while




      }catch(Exception e){
        System.out.println("get-view-for-user");
        e.printStackTrace();
      }
    }


    // update or create view locally

    public static void create_or_update_view(String view_name,String view_def,int choice){
      try{

        if(choice == 0){
          view_def_map.put(view_name,view_def);
          System.out.println("create locally");
        } else {
          view_def_map.replace(view_name,view_def);
          System.out.println("update locally");
        }

        System.out.println("create/update view successfully");

      }catch(Exception e){
        System.out.println("in create_update_view");
        e.printStackTrace();
      }
    }






}
