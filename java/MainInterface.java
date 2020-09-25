/*
    Phase 1 JDBC Interfacing, Group 5

    Written by Noah Miner, Allen Yang,
    Evelyn Tang, Julie Herrick, George Lan
*/

import java.sql.*;
import java.util.*;
import java.lang.*;


class Edge{
  public String s1;
  public String s2;
  public Integer edgelabel = 0;
  public Edge(){
    s1 = "";
    s2 = "";
  }
  public Edge(String t1, String t2){
    s1 = t1;
    s2 = t2;
  }

  public Edge(Edge e){
    this.s1 = e.s1;
    this.s2 = e.s2;
    this.edgelabel = e.edgelabel;
  }
  public boolean equaledge(Edge e){
    if((e.s1).equals(this.s1) && (e.s2).equals(this.s2)){
      return true;
    }
    return false;
  }

  public void printEdge(){
    System.out.println("s1:"+s1+" s2:"+s2+" "+"edgelabel:"+edgelabel);
  }
};


public class MainInterface {

    // driver and docker url
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/?serverTimezone=UTC#/";

    // Database credentials
    // going to be static for now, maybe we can implement a login later
    static final String USER = "root";
    static final String PASS = "password";


    static Map<String,ArrayList<String>> adj_list_by_column = new HashMap<String,ArrayList<String>>();
    static Map<String,ArrayList<String>> adj_list_by_table = new HashMap<String,ArrayList<String>>();
    static ArrayList<Edge> column_name = new ArrayList<Edge>(); // store visied info
    static ArrayList<String> table_name = new ArrayList<String>();
    static Map<String,Integer> visitedNode = new HashMap<String,Integer>();
    // static Map<Edge,Integer> visitedEdge = new HashMap<String,Integer>();
    static Stack<String> stk = new Stack<String>();
    // main loop to log into and interact with database

    public static ArrayList<Edge> reset_edge(ArrayList<Edge> edgelist){
      for(int i = 0; i < edgelist.size();i++){
        edgelist.get(i).edgelabel = 0; // set to unexplored
      }
      return edgelist;
    }

    public static boolean find_edge(ArrayList<Edge> edgelist, Edge e){
      boolean found = false;
      for(int i = 0; i < edgelist.size();i++){
        if(edgelist.get(i).equaledge(e)){
          found = true;
          break;
        }
      }
      return found;
    }

    // public static ArrayList<Edge> remove_edge(ArrayList<Edge> edgelist, Edge e){
    //   for(int i = 0; i < edgelist.size();i++){
    //     if(edgelist.get(i).equaledge(e)){
    //       edgelist.remove(e);
    //       break;
    //     }
    //   }
    //   return edgelist;
    // }

    public static ArrayList<Edge> set_edge_label(ArrayList<Edge> edgelist, Edge e, Integer num){
      for(int i = 0; i < edgelist.size();i++){
        if(edgelist.get(i).equaledge(e)){
          edgelist.get(i).edgelabel = num;
          break;
        }
      }
      return edgelist;
    }

    public static Edge get_edge(ArrayList<Edge> edgelist, Edge e){
      for(int i = 0; i < edgelist.size();i++){
        if(edgelist.get(i).equaledge(e)){
          return edgelist.get(i);
        }
      }
      return null;
    }


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


// ------------ DO NOT DELETE THE CODE BELOW -------------------------

            // test cases passed find_edge and reset_edge
            Edge e1 = new Edge("n1","n2");
            Edge e2 = new Edge("n3","n2");
            Edge e3 = new Edge("n2","n4");
            Edge e4 = new Edge("n3","n5");
            Edge e5 = new Edge(e4);
            ArrayList<Edge> sampleEdgelist = new ArrayList<Edge>(Arrays.asList(e1,e2,e3,e4,e5));


            set_edge_label(sampleEdgelist,e2,1);
            set_edge_label(sampleEdgelist,e5,1);
            for(int i =0; i < sampleEdgelist.size();i++){
              sampleEdgelist.get(i).printEdge();
            }

            System.out.println(find_edge(sampleEdgelist,e2));

            sampleEdgelist = reset_edge(sampleEdgelist);
            for(int i =0; i < sampleEdgelist.size();i++){
              sampleEdgelist.get(i).printEdge();
            }








            // database_meta(conn,stmt,adj_list_by_column,adj_list_by_table,table_name,column_name);

            // intialized , none of them is visited ------------ move this into while loop later
            for(int i = 0; i < table_name.size();i++){
              visitedNode.put(table_name.get(i),0);
            }


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
    public static void database_meta(Connection conn, Statement stmt,Map<String,ArrayList<String>> adj_list_by_column, Map<String,ArrayList<String>> adj_list_by_table, ArrayList<String> table_name, ArrayList<Edge> column_name){
      try{
        // ArrayList<ArrayList<String>> adjmatx = new ArrayList<ArrayList<String>>();

        ArrayList<ArrayList<String>> tbl_col = new ArrayList<ArrayList<String>>();
        // Map<String,Map<String,Integer>> matrix = new HashMap<String,HashMap<String,Integer>>();
        // Integer count = 0;
        String tablename;

        DatabaseMetaData metaData = conn.getMetaData();
        String[] types = {"TABLE"};
        ResultSet tables = metaData.getTables(null, null, "%", types);

        // O(n)
        while (tables.next()) {
         tablename = tables.getString("TABLE_NAME");
         // table_name_map.put(tablename,count);
         table_name.add(tablename);
        }

        // checking
        // for(int i = 0; i < column_name.size();i++){
        //   System.out.println(column_name.get(i));
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

        // // print for checking
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

          if(!colstr.toLowerCase().endsWith("id")){
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

        // // initialize matrix
        // for(int i=0; i < column_name.size();i++){
        //   String row_table = column_name.get(i);
        //   for(int j = 0; j < column_name.size();j++){
        //     String table_temp = column_name.get(j);
        //     Map<String,Integer> temp3 = new HashMap<String,Integer>()
        //     temp3.put(table_temp,0);
        //     matrix.put(row_table,temp3);
        //   }
        // }


        // get edges
        for(String k : adj_list_by_column.keySet()){
          ArrayList<String> temp5 = adj_list_by_column.get(k);
          for(int j = 0; j < temp5.size();j++){
            column_name.add(new Edge(k,temp5.get(j)));
          }
        }






      } catch(Exception e){
        System.out.println(e);
        e.printStackTrace();
      }
    }



    // search path, print out path
    public void path_finding_DFS(Map<String,ArrayList<String>> adj_list_by_column, Map<String,ArrayList<String>> adj_list_by_table,String tb_start, String tb_end,Map<String,Integer> visitedNode,ArrayList<Edge> column_name,Stack<String> stk){
      try{
        // set viisted
        visitedNode.replace(tb_start,1);
        stk.push(tb_start);

        if((tb_start.toLowerCase()).equals((tb_end.toLowerCase()))){
          return; // return to previous level
        }

        ArrayList<String> node_s_columes = adj_list_by_table.get(tb_start);
        for(int j=0;j<node_s_columes.size();j++){
          Edge curr_edge = new Edge(tb_start,node_s_columes.get(j));
          if(find_edge(column_name,curr_edge)){
            Edge tempEdge = new Edge(get_edge(column_name,curr_edge));
            if(tempEdge.edgelabel == 0){ // unexplored
              // String other_side_node = tempEdge.s2;
            }
          }else {
            System.out.println("wrong in finding dfs edge line 420");
          }



        }




      } catch(Exception e){
        System.out.println("Something wrong in search_path() function");
        e.printStackTrace();
      }

      return;
    }// end search_path

    // search and join tables
    public ArrayList<String> get_views(String view_name, String view_content){

      return new ArrayList<String>();
    }



}
