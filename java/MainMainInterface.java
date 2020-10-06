/*
*
*
* MainInterface --- name might be changed later
*/

import java.sql.*;
// import java.util.regex.*;
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

import javax.imageio.ImageIO;
import org.jgrapht.ext.JGraphXAdapter;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.swing.*;

public class MainMainInterface{

    private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://localhost:3306/?serverTimezone=UTC#/adventureworks";

    // Database credentials
    private final String USER = "root";
    private final String PASS = "password";
    private Map<String,ArrayList<String>> adj_list_by_column = new HashMap<String,ArrayList<String>>();
    private Map<String,ArrayList<String>> adj_list_by_table = new HashMap<String,ArrayList<String>>();
    private ArrayList<String> table_name = new ArrayList<String>();
    private Graph<String, ColumnEdge> table_matrix =
      new SimpleWeightedGraph<String, ColumnEdge>(ColumnEdge.class);

    private Double ind = 0.0;
    private ArrayList<String> edgeName = new ArrayList<String>(); // weight == index
    private Map<String,String> view_def_map = new HashMap<String,String>();
    private ArrayList<ArrayList<String>> GUI_display_table = new ArrayList<ArrayList<String>>();
    public Connection conn = null;
		public Statement stmt = null;

    private String query_string;


    MainMainInterface(String input_string){
      try{

        Class.forName("com.mysql.cj.jdbc.Driver");
  			System.out.println("Connecting to database...");
  			conn = DriverManager.getConnection(DB_URL,USER,PASS);
  			stmt = conn.createStatement();
  			ResultSet rs;
  			rs = stmt.executeQuery("USE adventureworks;");

        // initialize graph
        database_meta(conn,stmt,adj_list_by_column,adj_list_by_table,table_matrix,table_name);
        // generate the image
        graphPNG(table_matrix);


      } catch(Exception e1){
        JOptionPane.showMessageDialog(null,"error in MainMainInterface constructor, details in terminal");
        System.out.println("********** Something wrong in MainMainInterface constructor******");
        e.printStackTrace();
      }

    }// end constructor


    // member functions that we won't use in MainMainInterface - either print functions or functions being moved to MainFrame


    	// public static boolean validateCommand(String command) {
    	// 	command = command.toUpperCase();
    	// 	return !(command.contains("CREATE") || command.contains("DROP") ||
    	// 			command.contains("ALTER") || command.contains("DELETE") ||
    	// 			command.contains("INSERT")) || command.contains("CREATE VIEW")||
      //       command.contains("UPDATE") ||
      //       command.contains("REPLACE VIEW") || command.contains("DROP VIEW");
    	// }



      public static void createTempAggregateSalesTables(Statement stmt) throws SQLException {
    		stmt.executeUpdate("CREATE TEMPORARY TABLE IF NOT EXISTS subtotals "
    						 + "SELECT salesorderheader.CustomerID, SUM(salesorderheader.SubTotal) AS TotalSpent "
    						 + "FROM salesorderheader "
    						 + "GROUP BY salesorderheader.CustomerID");
    		stmt.executeUpdate("CREATE TEMPORARY TABLE IF NOT EXISTS customeraggregate "
    						 + "SELECT salesorderheader.CustomerID,"
    						 + "COUNT(DISTINCT(salesorderheader.SalesOrderID)) AS NumSales, "
    						 + "COUNT(DISTINCT(salesorderdetail.ProductID)) AS NumProducts,"
    						 + "SUM(salesorderdetail.OrderQty) AS TotalProducts "
    						 + "FROM salesorderheader INNER JOIN salesorderdetail ON (salesorderheader.SalesOrderID=salesorderdetail.SalesOrderID) "
    						 + "GROUP BY salesorderheader.CustomerID"); // a list of customer IDs and their aggregate sales info
    		stmt.executeUpdate("CREATE TEMPORARY TABLE IF NOT EXISTS salesaggregate "
    						 + "SELECT salesorderheader.SalesOrderID, "
    						 + "COUNT(DISTINCT(salesorderdetail.ProductID)) AS NumDistinctProducts, "
    						 + "SUM(salesorderdetail.OrderQty) AS TotalProductCount "
    						 + "FROM salesorderheader INNER JOIN salesorderdetail ON (salesorderheader.SalesOrderID=salesorderdetail.SalesOrderID) "
    						 + "GROUP BY salesorderheader.SalesOrderID");
    	}



      public static void database_meta(Connection conn, Statement stmt,Map<String,ArrayList<String>> adj_list_by_column, Map<String,ArrayList<String>> adj_list_by_table,Graph<String, ColumnEdge> table_matrix,ArrayList<String>table_name){

        try{
          ArrayList<ArrayList<String>> tbl_col = new ArrayList<ArrayList<String>>();
          String tablename;
          DatabaseMetaData metaData = conn.getMetaData();
          String[] types = {"TABLE"};
          ResultSet tables = metaData.getTables(null, null, "%", types);

          // list of table name and add vertex to graph
          while (tables.next()) {
           tablename = tables.getString("TABLE_NAME");
           table_name.add(tablename);
           table_matrix.addVertex(tablename); // add table node to graph
         }// end while

          String query1 = "select table_name,column_name from information_schema.columns where table_schema = 'adventureworks' order by table_name,column_name";
          stmt = conn.createStatement();
          ResultSet rs1;
          ResultSetMetaData rsmd1;
          rs1 = stmt.executeQuery(query1);
          rsmd1 = rs1.getMetaData();

          while(rs1.next()){
            ArrayList<String> temp1 = new ArrayList<String>();
            for(int j=1;j <= rsmd1.getColumnCount();j++){
              temp1.add(rs1.getString(j));
            }
            tbl_col.add(temp1);
          }// end building 2d structure, while

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
              ArrayList<String> temp2 = adj_list_by_column.get(colstr);
              temp2.add(curr_tbl); // append
              adj_list_by_column.replace(colstr,temp2); // update
            }
          }// end building map


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
              ArrayList<String> temp2 = adj_list_by_table.get(curr_tbl);
              temp2.add(colstr); // append
              adj_list_by_table.replace(curr_tbl,temp2); // update
              temp2 = null;
            }
          } // end building map


          for(String k: adj_list_by_column.keySet()){
            // currently , id only
            if(!k.contains("ID")){
              continue;
            }
            ArrayList<String> temp3 = adj_list_by_column.get(k);
            for(int j =0;j< temp3.size();j++){
              for(int q=0;q<temp3.size();q++){
                if(q != j){
                  table_matrix.addEdge(temp3.get(j),temp3.get(q),new ColumnEdge(k));
                  table_matrix.setEdgeWeight(table_matrix.getEdge(temp3.get(j),temp3.get(q)), ind); // nullptrException if create new edge and pass it down
                  edgeName.add(k);
                  ind += 1.0;
                }
              }
            }
          }// end for loop
        } catch(Exception e){
          System.out.println(e);
          e.printStackTrace();
        }
      }// end database meta

      // generate a graph of shortest path
      public static void plot_shortest_path(Graph<String, ColumnEdge> table_matrix,String tb1, String tb2){
        System.out.println("Shortest path from tb1 to tb2:");
        DijkstraShortestPath<String, ColumnEdge> dijkstraAlg =
            new DijkstraShortestPath<>(table_matrix);
        SingleSourcePaths<String, ColumnEdge> iPaths = dijkstraAlg.getPaths(tb1);

        System.out.println("shortest path from table \""+tb1+"\" to table \"" + tb2 + "\":");
        System.out.println(iPaths.getPath(tb2).getVertexList() + "\n");
      }



      public static void print_join_table(Connection conn, Statement stmt,Graph<String, ColumnEdge> table_matrix,String tb1, String tb2, ArrayList<String>edgeName){
        try{
        System.out.println("Shortest path from tb1 to tb2:");
        DijkstraShortestPath<String, ColumnEdge> dijkstraAlg =
            new DijkstraShortestPath<>(table_matrix);
        SingleSourcePaths<String, ColumnEdge> iPaths = dijkstraAlg.getPaths(tb1);

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
      }// end catch
      }// print join table




      public static void get_view_for_user(Connection conn, Statement stmt,String view_name,String view_def,Map<String,String> view_def_map){
        try{
          view_name = view_name.trim();
          String qry = "";
          // if esist in map
          if(view_def_map.containsKey(view_name) && view_def.equals("")){
            System.out.println("view exist");
            qry = view_def_map.get(view_name);
          }
          // if does not exist in map, a new qry comes in
          else if(!view_def_map.containsKey(view_name)) {
            System.out.println("view created");
            qry = view_def;
            create_or_update_view(view_name,view_def,0);
          }
          // if exist and also new query, update
          else if(view_def_map.containsKey(view_name) && (!view_def.equals(""))) {
            System.out.println("view update");
            qry = view_def;
            create_or_update_view(view_name,view_def,1);
          }
          else {
            System.out.println("something wrong");
            return;
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
      }// end function

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
      }// end function



      public static void graphPNG(Graph<String, ColumnEdge> table_matrix){
        JGraphXAdapter<String, ColumnEdge> graphAdapter = new JGraphXAdapter<>(table_matrix);
            mxIGraphLayout layout = new mxHierarchicalLayout(graphAdapter);
            layout.execute(graphAdapter.getDefaultParent());
            BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
            File imgFile = new File("DB.png");
            try{
                ImageIO.write(image, "PNG", imgFile);
                System.out.println("Image created successfully!");
                Desktop.getDesktop().open(imgFile);
            } catch (IOException e){
              System.out.println("Something Wrong in graphPNG function*********");
              e.printStackTrace();
            }
      }// end function


      	public static void jdbFindColumn (String columnName, Connection conn) throws SQLException {

      		String sql="select TABLE_NAME from information_schema.columns where column_name like ?";
      		PreparedStatement statement =conn.prepareStatement(sql);
      		statement.setString(1, columnName );
      		ResultSet resultSet=statement.executeQuery();
      		displayResultSet(resultSet,'-',150);
      		System.out.println();
      		resultSet.close();
      		statement.close();
      	}// end function

      	private static void displayResultSet(ResultSet resultSet, char symbol, int width) throws SQLException
      	{
      	    ResultSetMetaData rsmd = resultSet.getMetaData();
      	    int columnsNumber = rsmd.getColumnCount();
      	    for(int i = 1; i <= columnsNumber; i++)
      	    {
      	    	System.out.printf("| %-20.20s",rsmd.getColumnLabel(i));
      	    }
      	    System.out.println();
      	    for(int i = 0; i < width; ++i)
      	        System.out.printf("%c", symbol);
      	    System.out.println();
      	    while (resultSet.next()){
      			for (int i = 1; i <= columnsNumber; i++) {
      				System.out.printf("| %-20.20s",resultSet.getString(i));
      			}
      			System.out.println();// Move to the next line to print the next row.
      		}
      	}// end function


      	public static void jdbShowBestSalesperson(int num, Connection conn) throws SQLException {
      		String sql="SELECT c.FirstName, c.LastName, bestEmployeeYTD.bestYTD " +
      				"from (select SalesPersonID, SalesYTD as bestYTD from salesperson order by SalesYTD desc limit ?) " +
      				"as bestEmployeeYTD inner join " +
      				"employee e on bestEmployeeYTD.SalesPersonID = e.EmployeeID inner join " +
      				"contact c on e.ContactID = c.ContactID;" ;

      				PreparedStatement statement =conn.prepareStatement(sql);
      				statement.setInt(1, num);

      				ResultSet resultSet=statement.executeQuery();
      				displayResultSet(resultSet,'-',150);
      				System.out.println();
      				resultSet.close();
      				statement.close();
      	}// end function




        	public static void jdbShowReasonCount (Connection conn) throws SQLException {

        		String sql="select sr.Name as reason, count(*) as orderCount  from " +
        				"salesorderheader sh inner join\r\n" +
        				"salesorderheadersalesreason shr using(SalesOrderID) inner join " +
        				"salesreason sr using(SalesReasonID) " +
        				"group by sr.Name " +
        				"order by count(*) desc;";
        		Statement statement =conn.createStatement();
        		ResultSet resultSet=statement.executeQuery(sql);
        		displayResultSet(resultSet,'-',150);
        		System.out.println();
        		resultSet.close();
        		statement.close();
        	}// end function

        	public static void jdbShowSalesMonthly(int year, Connection conn) throws SQLException {
        		String sql="select  month(OrderDate) as month, sum(SubTotal) as sales  from " +
        				"salesorderheader " +
        				"where year(OrderDate)= ? " +
        				"group by year(OrderDate) ,month(OrderDate) " +
        				"order by month(OrderDate)";

        				PreparedStatement statement =conn.prepareStatement(sql);
        				statement.setInt(1, year);

        				ResultSet resultSet=statement.executeQuery();
        				displayResultSet(resultSet,'-',150);
        				System.out.println();
        				resultSet.close();
        				statement.close();
        	}// end function



        	private static String removeSemicolon(String str) {
        		String result = str;
        		if (str.charAt(str.length() - 1) == ';') {
        			result = str.substring(0, str.length() - 1);
        		}
        		return result;
        	}








}// end MainMainInterface class
