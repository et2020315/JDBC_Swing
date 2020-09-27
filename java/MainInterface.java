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


    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;

        try {

        //   Graph<String, DefaultEdge> directedGraph =
        //     new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
        // directedGraph.addVertex("a");
        // directedGraph.addVertex("b");
        // directedGraph.addVertex("c");
        // directedGraph.addVertex("d");
        // directedGraph.addVertex("e");
        // directedGraph.addVertex("f");
        // directedGraph.addVertex("g");
        // directedGraph.addVertex("h");
        // directedGraph.addVertex("i");
        // directedGraph.addEdge("a", "b");
        // directedGraph.addEdge("b", "d");
        // directedGraph.addEdge("d", "c");
        // directedGraph.addEdge("c", "a");
        // directedGraph.addEdge("e", "d");
        // directedGraph.addEdge("e", "f");
        // directedGraph.addEdge("f", "g");
        // directedGraph.addEdge("g", "e");
        // directedGraph.addEdge("h", "e");
        // directedGraph.addEdge("i", "h");
        //
        //
        // // computes all the strongly connected components of the directed graph
        // StrongConnectivityAlgorithm<String, DefaultEdge> scAlg =
        //     new KosarajuStrongConnectivityInspector<>(directedGraph);
        // List<Graph<String, DefaultEdge>> stronglyConnectedSubgraphs =
        //     scAlg.getStronglyConnectedComponents();
        //
        // // prints the strongly connected components
        // System.out.println("Strongly connected components:");
        // for (int i = 0; i < stronglyConnectedSubgraphs.size(); i++) {
        //     System.out.println(stronglyConnectedSubgraphs.get(i));
        // }
        // System.out.println();
        //
        // // Prints the shortest path from vertex i to vertex c. This certainly
        // // exists for our particular directed graph.
        // System.out.println("Shortest path from i to c:");
        // DijkstraShortestPath<String, DefaultEdge> dijkstraAlg =
        //     new DijkstraShortestPath<>(directedGraph);
        // SingleSourcePaths<String, DefaultEdge> iPaths = dijkstraAlg.getPaths("i");
        // System.out.println(iPaths.getPath("c") + "\n");
        //
        //
        // System.out.println("Shortest path from c to i:");
        // SingleSourcePaths<String, DefaultEdge> cPaths = dijkstraAlg.getPaths("c");
        // System.out.println(cPaths.getPath("i"));
        //



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

            print_shortest_path(table_matrix,"employee","vendorcontact");

            print_join_table(conn,stmt,table_matrix,"employee","vendorcontact",edgeName);


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
        if(countloop == 100){
          break;
        }
      }


    }catch(Exception e){
      System.out.println("Something wrong in join table.");
      e.printStackTrace();
    }

    }// print join table







    // search path, print out path
    // public void path_finding_DFS(Map<String,ArrayList<String>> adj_list_by_column, Map<String,ArrayList<String>> adj_list_by_table,String tb_start, String tb_end,Map<String,Integer> visitedNode,ArrayList<Edge> column_name,Stack<String> stk){
    //   try{
    //     // set viisted
    //     visitedNode.replace(tb_start,1);
    //     stk.push(tb_start);
    //
    //     if((tb_start.toLowerCase()).equals((tb_end.toLowerCase()))){
    //       return; // return to previous level
    //     }
    //
    //     ArrayList<String> node_s_columes = adj_list_by_table.get(tb_start);
    //     for(int j=0;j<node_s_columes.size();j++){
    //       Edge curr_edge = new Edge(tb_start,node_s_columes.get(j));
    //       if(find_edge(column_name,curr_edge)){
    //         Edge tempEdge = new Edge(get_edge(column_name,curr_edge));
    //         if(tempEdge.edgelabel == 0){ // unexplored
    //           // String other_side_node = tempEdge.s2;
    //         }
    //       }else {
    //         System.out.println("wrong in finding dfs edge line 420");
    //       }
    //     }
    //
    //   } catch(Exception e){
    //     System.out.println("Something wrong in search_path() function");
    //     e.printStackTrace();
    //   }
    //
    //   return;
    // }// end search_path

    // search and join tables
    public ArrayList<String> get_views(String view_name, String view_content){

      return new ArrayList<String>();
    }



}
