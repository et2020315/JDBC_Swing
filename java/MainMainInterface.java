/*
*
*
* MainInterface --- name might be changed later
*/
import java.util.regex.*;
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

  private String query_string = "";


  MainMainInterface() throws Exception{
    try{

      Class.forName("com.mysql.cj.jdbc.Driver");
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);
      stmt = conn.createStatement();
      ResultSet rs;
      rs = stmt.executeQuery("USE adventureworks;");

      // initialize graph
      database_meta();
      // join_table3("employee","purchaseorderheader","vendorcontact","purchaseorderheader.EmployeeID = employee.EmployeeID","purchaseorderheader.VendorID=vendorcontact.VendorID");
      // join_table4("department","employeedepartmenthistory","employeepayhistory","jobcandidate","department.DepartmentID=employeepayhistory.DepartmentID","employeepayhistory.EmployeeID=employeedepartmenthistory.EmployeeID","employeepayhistory.EmployeeID=jobcandidate.EmployeeID");



    } catch(Exception e1){
      JOptionPane.showMessageDialog(null,"error in MainMainInterface constructor, details in terminal");
      throw new Exception(e1);
    }

  }// end constructor


  // member functions that we won't use in MainMainInterface - either print functions or functions being moved to MainFrame

  public void switchOnFirstWord() throws Exception{
    try{
      // testtestFunction();

      String command = this.query_string;
      if(query_string.isEmpty()){
        System.out.println("private member query string is empty");
        System.exit(1);
      }


      Pattern spaceSplit = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
      Matcher m = spaceSplit.matcher(command);
      List<String> matches = new ArrayList<String>();

      while (m.find()) {
        if (m.group(1) != null) {
          matches.add(m.group(1)); // content inside double quotes
        } else if (m.group(2) != null) {
          matches.add(m.group(2)); // content inside single quotes
        } else {
          matches.add(m.group()); // unquoted
        }
      }
      String parsed_command[] = matches.toArray(new String[matches.size()]);

      String query; // local string object to store query in switch cases
      // String first = command.split(" ",2)[0];
      // System.out.println("first: "+ first);
      switch (parsed_command[0]) {

        case "q":
        case "q;":{
          System.exit(0);
          break;
        }

        case "quit;":
        {
          System.exit(0);
          break;
        }

        case "quit":{
          System.exit(0);
          break;
        }
        case "show-tables;":
        case "show-tables":{
          List<String> ttbstr = table_name;
          TableGUI showtbGui = new TableGUI(ttbstr);

          break;
        }

        case "jdb-show-related-tables":{
          // SHOW KEYS FROM <table name> WHERE Key_name = 'PRIMARY'
          if (parsed_command.length != 2) {
            JOptionPane.showMessageDialog(null,"Incorrect amount of arguments. Re-enter");
            break;
          }
          String parT = parsed_command[1].replace(";","").trim().toLowerCase();
          // System.out.println("**** A list of table that is connected to " + parT + "****");
          // System.out.println(Graphs.neighborListOf(this.table_matrix, parT));
          List<String> relate_tb = Graphs.neighborListOf(this.table_matrix, parT);
          TableGUI relatedtablegui = new TableGUI(relate_tb);
        }
        break;

        case "jdb-show-all-primary-keys;":
        {
          String sql = "select TABLE_NAME,COLUMN_NAME FROM  INFORMATION_SCHEMA.COLUMNS where COLUMN_KEY='PRI' AND TABLE_SCHEMA = 'adventureworks'";

          conn = DriverManager.getConnection(DB_URL,USER,PASS);
          PreparedStatement statement = conn.prepareStatement(sql);
          ResultSet rs2 = statement.executeQuery();
          TableGUI tbprimary = new TableGUI(rs2);
        }// end case
        break;


        case "jdb-show-all-primary-keys":
        {
          String sql = "select TABLE_NAME,COLUMN_NAME FROM  INFORMATION_SCHEMA.COLUMNS where COLUMN_KEY='PRI' AND TABLE_SCHEMA = 'adventureworks'";

          conn = DriverManager.getConnection(DB_URL,USER,PASS);
          PreparedStatement statement = conn.prepareStatement(sql);
          ResultSet rs2 = statement.executeQuery();
          TableGUI tbprimary2 = new TableGUI(rs2);
        }// end case
        break;


        case "jdb-find-column":{
          if (parsed_command.length != 2) {
            JOptionPane.showMessageDialog(null,"Re-enter");
            break;
          }
          String parC = parsed_command[1].replace(";","").trim();
          List<String> arrfindcol = this.adj_list_by_column.get(parC);
          TableGUI tblcolGUI = new TableGUI(arrfindcol);
        }// end case
        break;


        case "jdb-search-path":{
          command = command.trim();
          command = command.replace(";","");
          String tbarr1[] = command.split(" ");
          if(tbarr1.length!= 3 || !tbarr1[0].equals("jdb-search-path")){
            JOptionPane.showMessageDialog(null,"Something wrong in syntax. try again");
            break;
          } else {
            String tb11 = tbarr1[1].trim().toLowerCase();
            String tb12 = tbarr1[2].trim().toLowerCase();
            // if table not in graph
            if(!table_name.contains(tb11) || !table_name.contains(tb12)){
              JOptionPane.showMessageDialog(null,"one of the table not in schema, use table in schema");
              break;
            } else{
              plot_shortest_path(table_matrix,tb11,tb12); // call TableGUI in function
            }
          }
        }// end case
        break;


        case "jdb-search-and-join":{
          command = command.trim();
          command = command.replace(";","");
          String tbarr[] = command.split(" ");
          if(tbarr.length!= 3 || !tbarr[0].equals("jdb-search-and-join")){
            System.out.println("Something wrong in syntax. try again");
            break;
          } else {
            String tb1 = tbarr[1].trim().toLowerCase();
            String tb2 = tbarr[2].trim().toLowerCase();
            // if table not in graph
            if(!table_name.contains(tb1) || !table_name.contains(tb2)){
              System.out.println("one of the table not in schema, enter table in schema");
              break;
            } else{
              print_join_table(conn,stmt,table_matrix,tb1,tb2,edgeName); // call tableGUI in function
            }
          }
        }
        break;

        case "join-tables":{
          if(parsed_command.length == 4){
            String tB1 = parsed_command[1].trim();
            String tB2 = parsed_command[3].replace(";","").trim();
            print_join_table(this.conn,this.stmt,this.table_matrix,tB1,tB2,this.edgeName);

          } else if(parsed_command.length == 6){
            String tB1 = parsed_command[1].trim();
            String tB2 = parsed_command[3].trim();
            String tB3 = parsed_command[5].replace(";","").trim();
            String cond1 = parsed_command[2].trim();
            String cond2 = parsed_command[4].trim();
            join_table3(tB1,tB2,tB3,cond1,cond2);

          } else if (parsed_command.length == 8){
            String tB1 = parsed_command[1].trim();
            String tB2 = parsed_command[3].trim();
            String tB3 = parsed_command[5].trim();
            String tB4 = parsed_command[7].replace(";","").trim();
            String cond1 = parsed_command[2].trim();
            String cond2 = parsed_command[4].trim();
            String cond3 = parsed_command[6].trim();
            join_table4(tB1,tB2,tB3,tB4,cond1,cond2,cond3);

          } else {
            JOptionPane.showMessageDialog(null,"incorrect num of arguments. Retry");
          }

          break;
        }


        case "jdb-get-view":{
          command = command.trim();
          String query11 = "";
          if(command.contains("(") && command.contains(")")){
            int indl = command.indexOf("(");
            int indr = command.indexOf(")");
            if(indl == indr -1){
              JOptionPane.showMessageDialog(null,"gdb-get-view, empty, re-enter please");
              break;
            }
            query11 = command.substring(indl+1,indr);
          }
          // make all view name keys lowercase
          String viewName1 = command.split(" ")[1].toLowerCase();
          get_view_for_user(conn,stmt,viewName1,query11,view_def_map); // call table GUI in function
        }
        break;

        case "jdb-show-best-salesperson;":{
          jdbShowBestSalesperson(1, conn);
          break;
        }


        case "jdb-show-best-salesperson": {
          String bsp[] = command.split(" ");
          if(bsp.length > 2){
            JOptionPane.showMessageDialog(null,"Re-enter. Not correct syntax");
            break;
          }
          // position 1 not a number
          try {
            Integer.parseInt(parsed_command[1].replace(";","").trim());
          }
          catch (NumberFormatException eee){
            JOptionPane.showMessageDialog(null,"position 1 not a number ");
            break;
          }

          int num = Integer.parseInt(parsed_command[1].replace(";","").trim()) ;
          jdbShowBestSalesperson(num, conn); // call tablegui in function
          break;
        }

        case "jdb-show-reason-count;":
        case "jdb-show-reason-count": {
          jdbShowReasonCount(conn); // call tablegui in function
          break;
        }

        case "jdb-show-sales-monthly;":{
          JOptionPane.showMessageDialog(null,"Did not enter a year, defualt set to year 2002,click ok");
          jdbShowSalesMonthly(2002, conn); // default year when you did not specify a year
          break;
        }

        case "jdb-show-sales-monthly": {
          String bsp[] = command.split(" ");
          if(bsp.length > 2){
            JOptionPane.showMessageDialog(null,"Re-enter. Not correct syntax");
            break;
          }
          try {
            Integer.parseInt(parsed_command[1].replace(";","").trim());
          }
          catch (NumberFormatException eee){
            JOptionPane.showMessageDialog(null,"position 1 not a number ");
            break;
          }


          int year = Integer.parseInt(parsed_command[1].replace(";","").trim());
          jdbShowSalesMonthly(year, conn); // call tablegui in function
          break;
        }


        case "jdb-stat": {
          if (parsed_command.length != 3) {
            JOptionPane.showMessageDialog(null,"Incorrect amount of arguments");
            break;
          }
          // get index of the column chosen in command
          ResultSet rs = stmt.executeQuery("SELECT column_name FROM information_schema.columns where table_name='" + parsed_command[1] + "'");
          int index_of_column = 0;
          boolean name_found = false;
          while (rs.next()) {
            String colName = rs.getString(1);
            if (colName.equalsIgnoreCase(parsed_command[2])) {
              name_found = true;
              break;
            }
            index_of_column++;
          }
          if (!name_found) {
            JOptionPane.showMessageDialog(null,"Table or Column name not found.");
            break;
          }

          // make sure data type is numeric
          rs = stmt.executeQuery("SELECT data_type FROM information_schema.columns where table_name='" + parsed_command[1] + "'");
          rs.next();
          for (int i = 0; i < index_of_column; i++) {
            rs.next();
          }
          String datatype = rs.getString(1);
          if (!(datatype.contains("int") || datatype.contains("dec") || datatype.contains("num") || datatype.contains("float") || datatype.contains("double"))) {
            JOptionPane.showMessageDialog(null,"Column must contain a numeric data type.");
            break;
          }

          // now that query is valid, generate a list of the values in that column
          rs = stmt.executeQuery("SELECT " + parsed_command[2] + " FROM " + parsed_command[1]);
          List<Double> list = new ArrayList<Double>();
          while (rs.next()) {
            list.add(rs.getDouble(1));
          }

          // going through list and getting stats
          double min = list.get(0);
          double max = list.get(0);
          double sum = 0;
          double median = 0;
          if (list.size()%2 == 0)
          median = (list.get(list.size()/2) + list.get(list.size()/2 - 1))/2;
          else
          median = list.get(list.size()/2);

          for (int i = 0; i < list.size(); i++) {
            if (list.get(i) < min)
            min = list.get(i);
            if (list.get(i) > max)
            max = list.get(i);
            sum += list.get(i);
          }
          double avg = sum/list.size();

          // set up 1 d arraylist
          ArrayList<String> statArr = new ArrayList<String>();

          statArr.add(String.format("Min value: %.2f\n", min));
          statArr.add(String.format("Max value: %.2f\n", max));
          statArr.add(String.format("Average: %.2f\n", avg));
          statArr.add(String.format("Median: %.2f\n", median));

          // plotting histogram
          // first get number of bins and bin width
          int num_bins = (int)Math.ceil(Math.sqrt(list.size()));
          double bin_width = (max - min)/num_bins;
          int[] bins = new int[num_bins];

          // populating each bin and scaling them properly
          int max_count = 0;
          for (int index = 0; index < list.size(); index++) { // populating each bin with quantity in that bin
            int i = (int)(Math.floor((list.get(index) - min)/bin_width));
            i = Math.min(i, bins.length - 1);
            bins[i]++;
            if (bins[i] > max_count)
            max_count = bins[i];
          }
          int y_scale = 1;
          while (max_count/y_scale > 50) // get scale for y axis
          y_scale *= 10;
          for (int i = 0; i < bins.length; i++) { // divide each quantity in bins by y_scale
            bins[i] = (int)Math.round(bins[i]/(double)y_scale);
          }
          String stst = "";
          // displaying bins
          // printing y-axis
          int offset = String.format("%.2f", max).length() * 2 + 3; // how long the header for each bin should be when displaying
          System.out.println();
          stst += String.format("%-" + (offset + 1) + "s", "");
          int max_count_scaled = max_count/y_scale;

          // printing y-axis labels
          for (int count = 0; count <= max_count_scaled; count++){
            stst += (count + "___");
          }

          stst += String.format("(Each star and each y-axis label represents %d counts)\n", y_scale);
          statArr.add(stst);

          stst = "";
          // printing each bin
          for (int i = 0; i < bins.length; i++) {
            String range = String.format("%.2f - %.2f", min + i*bin_width, min + (i+1)*bin_width);
            String stst2 = "";
            stst2 += String.format("%-" + offset + "s |", range);
            for (int j = 0; j < bins[i]; j++) {
              stst2 += "*";
            }
            // System.out.println();
            statArr.add(stst2);
            stst2 = "";
          }

          List<String> sstat = statArr;
          TableGUI tbstatss = new TableGUI(sstat);

          break;
        }// end jdb-get-stat




        case "jdb-customer-info": // displays individual customers line by line that match "conditions", can group by column name, e.g. how many customers in this state
        {
          createTempAggregateSalesTables(stmt);
          query = "SELECT ";
          boolean counting = (parsed_command.length == 3 && parsed_command[1].equalsIgnoreCase("groupby"))
          || parsed_command.length == 4 && (parsed_command[2].equalsIgnoreCase("groupby"));

          if (counting) {
            query += parsed_command[parsed_command.length - 1] + ",Count(DISTINCT(customer.CustomerID)) ";
          } else {
            query += "AccountNumber AS 'Account Number', CustomerType AS 'Customer Type', "
            + "customeraggregate.NumSales AS 'Total Number of Orders', customeraggregate.NumProducts AS 'Number of distinct products', "
            + "customeraggregate.TotalProducts AS 'Total number of products', subtotals.TotalSpent AS 'Total amount spent', "
            + "AddressLine1 AS 'Address Line 1', AddressLine2 AS 'Address Line 2', City, stateprovince.Name AS State, countryregion.Name AS Country, PostalCode AS 'Postal Code' ";
          }
          // Getting location info from customers
          query += "FROM customeraggregate "
          + "INNER JOIN subtotals ON (customeraggregate.CustomerID=subtotals.CustomerID) "
          + "INNER JOIN customer ON (customeraggregate.CustomerID=customer.customerID) "
          + "INNER JOIN customeraddress ON (customer.CustomerID=customeraddress.CustomerID) "
          + "INNER JOIN address ON (customeraddress.AddressID=address.AddressID) "
          + "INNER JOIN stateprovince ON (address.StateProvinceID=stateprovince.StateProvinceID) "
          + "INNER JOIN countryregion ON (stateprovince.CountryRegionCode=countryregion.CountryRegionCode) ";

          if (parsed_command.length > 1 && !parsed_command[1].equalsIgnoreCase("groupby")) {
            query += "WHERE " + parsed_command[1] + " ";
          }
          if (counting) {
            query += "GROUP BY " + parsed_command[parsed_command.length - 1] + " ";
          }
          ResultSet rs = stmt.executeQuery(query);
          TableGUI Cusgui = new TableGUI(rs);
          // Call TableGUI here
        }
        break;



        case "jdb-customer-orders": //jdb-customer-orders <condition> <aggregate-by-sales|aggregate>
        {
          query = "";
          String where_expr = "";
          String group_expr = "";
          String from_expr = "";
          if (parsed_command.length > 1 && !parsed_command[1].toLowerCase().contains("aggregate")) {
            where_expr = "WHERE " + parsed_command[1] + " ";
          }
          if ((parsed_command.length == 2 && parsed_command[1].equalsIgnoreCase("aggregate")) ||
          (parsed_command.length == 3 && parsed_command[2].equalsIgnoreCase("aggregate"))) {
            createTempAggregateSalesTables(stmt); // tables with aggregate sales data for a customer
            query = "SELECT customer.AccountNumber AS 'Account Number', "
            + "customer.CustomerType AS 'Customer Type', "
            + "customeraggregate.NumSales AS 'Total number of sales',"
            + "customeraggregate.NumProducts AS 'Distinct number of items bought',"
            + "customeraggregate.TotalProducts AS 'Total number of items bought',"
            + "subtotals.TotalSpent AS 'Total amount spent' ";
            from_expr = "FROM customer "
            + "INNER JOIN subtotals ON (customer.CustomerID=subtotals.CustomerID) "
            + "INNER JOIN customeraggregate ON (customer.CustomerID=customeraggregate.CustomerID) ";
          }
          else if ((parsed_command.length == 2 && parsed_command[1].equalsIgnoreCase("aggregate-by-sales")) ||
          (parsed_command.length == 3 && parsed_command[2].equalsIgnoreCase("aggregate-by-sales"))) {
            createTempAggregateSalesTables(stmt); // tables with aggregate sales data for a customer
            query = "SELECT customer.AccountNumber AS 'Account Number', "
            + "customer.CustomerType AS 'Customer Type', "
            + "salesorderheader.SalesOrderID AS 'Sales Order ID', "
            + "salesaggregate.NumDistinctProducts AS 'Distinct number of items bought', "
            + "salesaggregate.TotalProductCount AS 'Total number of items bought', "
            + "salesorderheader.SubTotal AS 'Total amount spent' ";
            from_expr = "FROM customer INNER JOIN salesorderheader ON (customer.CustomerID=salesorderheader.CustomerID) "
            + "INNER JOIN salesaggregate ON (salesorderheader.SalesOrderID=salesaggregate.SalesOrderID) ";
          }
          else {
            query = "SELECT customer.AccountNumber AS 'Account Number', "
            + "customer.CustomerType AS 'CustomerType', "
            + "salesorderheader.SalesOrderID AS 'Sales Order ID', "
            + "product.Name AS 'Product Name', "
            + "product.ListPrice AS 'List Price', "
            + "salesorderdetail.OrderQty AS 'Amount ordered' ";
            from_expr = "FROM customer INNER JOIN salesorderheader ON (customer.CustomerID=salesorderheader.CustomerID) "
            + "INNER JOIN salesorderdetail ON (salesorderheader.SalesOrderID=salesorderdetail.SalesOrderID) "
            + "INNER JOIN product ON (salesorderdetail.ProductID=product.ProductID) ";
          }
          query += from_expr + where_expr + group_expr + "";
          ResultSet rs = stmt.executeQuery(query);
          TableGUI cusOrdGUI = new TableGUI(rs);
        }// end case
        break;

        // Need this for phase 3 requirements
        case "show-specific-columns": {
          ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();

          if (parsed_command.length != 3) {
            JOptionPane.showMessageDialog(null, "Arguments invalid");
          }
          System.out.println("arg2-"+parsed_command[1]+"-args-"+parsed_command[2]+"-");
          if (!parsed_command[2].equalsIgnoreCase("ALL")) {
            String[] colsAsString = parsed_command[2].trim().split(":");
            // int[] columns = new int[colsAsString.length];
            // for (int i = 0; i < colsAsString.length; i++) {
            //   columns[i] = Integer.parseInt(colsAsString[i]);
            //   System.out.println("col:"+columns[i]);
            // }
            ArrayList<String> intStrlist = new ArrayList<String>();
            for(int i = 0; i < colsAsString.length;i++){
              intStrlist.add(colsAsString[i].trim());
            }
            // now intStrlist contains the index of column

            query = "SELECT * FROM " + parsed_command[1];
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            // columns contains the index of displaying columns

            ArrayList<String> displaySPEC = new ArrayList<String>();
            String temptemp = "";
            for(int i = 0; i < colsAsString.length;i++){
              temptemp += " |" + rsmd.getColumnName(Integer.parseInt(colsAsString[i]));
            }
            displaySPEC.add(temptemp);

            while (rs.next()) {
              String strrow = "";
              for (int i = 1; i <= colCount; i++){
                // if (columns[colIndex] == i+1) {
                //   results.get(colIndex).add(rs.getString(i+1));
                //   colIndex++;
                // }
                if(intStrlist.contains(Integer.toString(i))){
                  strrow = strrow + "\t|" + rs.getString(i);
                }
              }// end for
              displaySPEC.add(strrow);
            }
            TableGUI disSpecGUI = new TableGUI(displaySPEC);

            // send to tableGUI
          } else {
            query = "SELECT * FROM " + parsed_command[1].trim();
            ResultSet rs = stmt.executeQuery(query);
            TableGUI specColGui = new TableGUI(rs);
          }
        } break;


        case "CREATE":{
          // create view satement
          if(command.contains("CREATE VIEW") && command.contains("AS")){
            String cv1[] = command.split(" AS ");
            String viewName = cv1[0].replace("CREATE VIEW","").trim().toLowerCase();
            String qy = cv1[1].replace("(","").replace(")","").replace(";","").trim();

            if(qy.equals("")){
              JOptionPane.showMessageDialog(null,"empty view def, try again");
              break;
            }
            if(view_def_map.containsKey(viewName)){
              JOptionPane.showMessageDialog(null,"THE view "+viewName+" has already existed. use replace view command");
              break;
            }
            // call create view function
            else {
              create_or_update_view(viewName,qy,0);
              // for(String ttt: view_def_map.keySet()){
              //   System.out.println(ttt + "-> " + view_def_map.get(ttt));
              // }
            }
          }
          // missing as keyword
          else if (command.contains("CREATE VIEW") && (!command.contains("AS"))){
            JOptionPane.showMessageDialog(null,"something wrong, missing AS keyword");
          }
          // other create mysql statement
          else {}
          }// end case
          break;

          case "REPLACE":{
            if(command.contains("REPLACE VIEW") && command.contains("AS")){
              String cv1[] = command.split(" AS ");
              String viewName = cv1[0].replace("REPLACE VIEW","").trim().toLowerCase();
              String qy = cv1[1].replace("(","").replace(")","").replace(";","").trim();
              if(qy.equals("")){
                JOptionPane.showMessageDialog(null,"empty view def, try again");
                break;
              }
              // call create_update view
              if(view_def_map.containsKey(viewName)){
                create_or_update_view(viewName,qy,1); // add forloop below to debug
              }
              else{
                JOptionPane.showMessageDialog(null,"the view is not created yet, cannot replace.");
              }
            }// end if
            else {
              JOptionPane.showMessageDialog(null,"Something wrong, try again"); // error
            }
          }// end case
          break;


          case "DROP":{
            if (command.contains("DROP VIEW")) {
              String viewName = command.replace("DROP VIEW","").replace(";","").trim().toLowerCase();
              // remove from map
              if(view_def_map.containsKey(viewName)){
                view_def_map.remove(viewName);
                JOptionPane.showMessageDialog(null,viewName + " removed"); // add forlooop below to debug check view
              }
              else{
                JOptionPane.showMessageDialog(null,"the view is not created yet, cannot drop.");
              }
            }
            break;
          }// end case


          case "jdb-plot-schema":{
            graphPNG(table_matrix);
            PNGGUI png1 = new PNGGUI("DB.png");
            break;
          }

          // basic sql commands
          default:{
            command = command.replace(";","").trim();
            ResultSet rs = stmt.executeQuery(command);
            // printResults(rs);
            TableGUI tbdefault = new TableGUI(rs);
          }
          break;
        }// end switch

      }//end try
      catch(Exception e10){
        JOptionPane.showMessageDialog(null,"exception caught.");
        throw new Exception(e10);
      }
    }// end function


    public void setQueryString(String query1){
      this.query_string = query1;
    }

    public void createTempAggregateSalesTables(Statement stmt) throws SQLException {
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
    }// end function


    // // debugging purpose
  	// public static void printResults(ResultSet rs) throws SQLException {
  	// 	ResultSetMetaData rsmd = rs.getMetaData();
  	// 	int cols = rsmd.getColumnCount();
  	// 	// Printing column names
  	// 	for (int i = 1; i <= cols; i++) {
  	// 		if (i > 1)
  	// 			System.out.print(", ");
  	// 		System.out.print(rsmd.getColumnLabel(i));
  	// 	}
  	// 	System.out.println("");
  	// 	// Printing query contents
  	// 	while (rs.next()) {
  	// 		for (int i = 1; i <= cols; i++) {
  	// 			if (i > 1)
  	// 				System.out.print(", ");
  	// 			String colVal = rs.getString(i);
  	// 			if (colVal != null && colVal.contains(","))
  	// 				System.out.print('"' + colVal + '"');
  	// 			else
  	// 				System.out.print(colVal);
  	// 		}
  	// 		System.out.println("");
  	// 	}
  	// }// end function

    public void database_meta(){

      try{
        ArrayList<ArrayList<String>> tbl_col = new ArrayList<ArrayList<String>>();
        String tablename;
        DatabaseMetaData metaData = this.conn.getMetaData();
        String[] types = {"TABLE"};
        ResultSet tables = metaData.getTables(null, null, "%", types);

        // list of table name and add vertex to graph
        while (tables.next()) {
          tablename = tables.getString("TABLE_NAME");
          this.table_name.add(tablename);
          this.table_matrix.addVertex(tablename); // add table node to graph
        }// end while

        String query1 = "select table_name,column_name from information_schema.columns where table_schema = 'adventureworks' order by table_name,column_name";
        this.stmt = this.conn.createStatement();
        ResultSet rs1;
        ResultSetMetaData rsmd1;
        rs1 = this.stmt.executeQuery(query1);
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
          if(this.adj_list_by_column.get(colstr)==null){
            // ith item's 1st column, make a new array
            ArrayList<String> temp2 = new ArrayList<String>();
            temp2.add(curr_tbl);
            this.adj_list_by_column.put(colstr,temp2);
            temp2 = null;
          } else {
            ArrayList<String> temp2 = this.adj_list_by_column.get(colstr);
            temp2.add(curr_tbl); // append
            this.adj_list_by_column.replace(colstr,temp2); // update
          }
        }// end building map

        // building map for table: adj_list_by_table
        for(int i=0;i<tbl_col.size();i++){
          // ith item's 2nd column
          String colstr = tbl_col.get(i).get(1);
          String curr_tbl = tbl_col.get(i).get(0);

          if(!colstr.endsWith("ID")){
            continue;
          }

          if(this.adj_list_by_table.get(curr_tbl)==null){ // not being added
            ArrayList<String> temp2 = new ArrayList<String>(); // ith item's 1st column, make a new array
            temp2.add(colstr);
            this.adj_list_by_table.put(curr_tbl,temp2);
            temp2 = null;
          } else {
            ArrayList<String> temp2 = this.adj_list_by_table.get(curr_tbl);
            temp2.add(colstr); // append
            this.adj_list_by_table.replace(curr_tbl,temp2); // update
            temp2 = null;
          }
        } // end building map

        for(String k: this.adj_list_by_column.keySet()){
          // currently , id only
          if(!k.contains("ID")){ continue; }
          ArrayList<String> temp3 = this.adj_list_by_column.get(k);
          for(int j =0;j< temp3.size();j++){
            for(int q=0;q<temp3.size();q++){
              if(q != j){
                this.table_matrix.addEdge(temp3.get(j),temp3.get(q),new ColumnEdge(k));
                this.table_matrix.setEdgeWeight(this.table_matrix.getEdge(temp3.get(j),temp3.get(q)), this.ind); // nullptrException if create new edge and pass it down
                this.edgeName.add(k);
                this.ind += 1.0;
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
    public void plot_shortest_path(Graph<String, ColumnEdge> table_matrix,String tb1, String tb2){
      System.out.println("Shortest path from tb1 to tb2:");
      DijkstraShortestPath<String, ColumnEdge> dijkstraAlg =
      new DijkstraShortestPath<>(table_matrix);
      SingleSourcePaths<String, ColumnEdge> iPaths = dijkstraAlg.getPaths(tb1);

      System.out.println("shortest path from table \""+tb1+"\" to table \"" + tb2 + "\":");
      // System.out.println(iPaths.getPath(tb2).getVertexList() + "\n");
      List<String> pathstr1 = iPaths.getPath(tb2).getVertexList();

      TableGUI tbpath = new TableGUI(pathstr1);
    }

    public void join_table3(String tb1, String tb2, String tb3, String cond1, String cond2)throws Exception{
      try{
        String qry = "select * from " + tb1 +
        " INNER JOIN " + tb2 + " ON ( " + cond1 + ") " +
        " INNER JOIN " + tb3 + " ON ( " + cond2 + ") ";
        this.stmt = this.conn.createStatement();
        ResultSet rs2;
        rs2 = this.stmt.executeQuery(qry);
        TableGUI tbjoin3gui = new TableGUI(rs2);
      }catch(Exception e){
        // throw new Exception(ee);
        JOptionPane.showMessageDialog(null,"Wrong format or syntax for condition is not correct. Re-enter");
        throw new Exception(e);
      }
    }// end function

    public void join_table4(String tb1, String tb2, String tb3, String tb4, String cond1, String cond2, String cond3) throws Exception{
      try{
        String qry = "select * from " + tb1 +
        " INNER JOIN " + tb2 + " ON ( " + cond1 + " ) " +
        " INNER JOIN " + tb3 + " ON ( " + cond2 + " ) "  +
        " INNER JOIN " + tb4 + " ON ( " + cond3 + " ) ";

        this.stmt = this.conn.createStatement();
        ResultSet rs2;
        rs2 = this.stmt.executeQuery(qry);
        TableGUI tbjoin3gui = new TableGUI(rs2);

      } catch(Exception ee){
        JOptionPane.showMessageDialog(null,"Wrong format or syntax for condition is not correct.Re-enter");
        throw new Exception(ee);
      }
    }


    public void print_join_table(Connection conn, Statement stmt,Graph<String, ColumnEdge> table_matrix,String tb1, String tb2, ArrayList<String>edgeName)throws Exception{
      try{
        System.out.println("---------------- It will take a while to display ------------");
        DijkstraShortestPath<String, ColumnEdge> dijkstraAlg =
        new DijkstraShortestPath<>(table_matrix);
        SingleSourcePaths<String, ColumnEdge> iPaths = dijkstraAlg.getPaths(tb1);

        // System.out.println("shortest path from table \""+tb1+"\" to table \"" + tb2 + "\":");
        // System.out.println(iPaths.getPath(tb2).getVertexList() + "\n");
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
          // System.out.println("index:"+index);
          // System.out.println("columnName1:"+columnName1);
          // append it to oncluaseEdge
          onclauseEdge.add(columnName1);
        }

        // // if there are more than 3 edges. which means more than 4 tables joined, do not display
        // if(onclauseEdge.size()>3){
        //   JOptionPane.showMessageDialog(null,"More than 4 tables joined. we are not going to display it");
        //   return;
        // }


        String query = "select * from ";
        // System.out.println("tblistsize:"+tablelist.size());
        // System.out.println("oncluasesize:"+onclauseEdge.size());
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
        // System.out.println("hellow");
        int countloop = 0;
        JOptionPane.showMessageDialog(null,"The joined table will be displayed after you read 2 messages and clicking ok button for three times");
        JOptionPane.showMessageDialog(null,"If the joined table contains too many columns, it will show dots, please click ok to continue");
        JOptionPane.showMessageDialog(null,"You might need to change the width of the column to see the entries, please click ok to continue");
        TableGUI joingui = new TableGUI(rs2);

      }catch(Exception e){
        JOptionPane.showMessageDialog(null,"Error in joining");
        throw new Exception(e);
        // System.out.println("Something wrong in join table.");
        // e.printStackTrace();
      }// end catch
    }// print join table




    public void get_view_for_user(Connection conn, Statement stmt,String view_name,String view_def,Map<String,String> view_def_map){
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

        TableGUI tbviewgui = new TableGUI(rs);
        // while(rs.next()){
        //   for(int j=1;j <= rsmd.getColumnCount();j++){
        //     String type = rsmd.getColumnTypeName(j);
        //     if(type.toLowerCase().contains("binary")){
        //       System.out.println(rsmd.getColumnName(j)+": "+"some binary, print out make a noise");
        //     }else {
        //       System.out.println(rsmd.getColumnName(j)+": "+rs.getString(j));
        //     }
        //   }
        //   System.out.println("*******************************************************");
        // }// end while

      }catch(Exception e){
        System.out.println("get-view-for-user");
        e.printStackTrace();
      }
    }// end function

    public void create_or_update_view(String view_name,String view_def,int choice){
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



    public void graphPNG(Graph<String, ColumnEdge> table_matrix){
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


    public void jdbFindColumn (String columnName, Connection conn) throws SQLException {

      String sql="select TABLE_NAME from information_schema.columns where column_name like ?";
      PreparedStatement statement =conn.prepareStatement(sql);
      statement.setString(1, columnName );
      ResultSet resultSet=statement.executeQuery();
      displayResultSet(resultSet,'-',150);
      System.out.println();
      resultSet.close();
      statement.close();
    }// end function

    private void displayResultSet(ResultSet resultSet, char symbol, int width) throws SQLException
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


    public void jdbShowBestSalesperson(int num, Connection conn) throws SQLException {
      String sql="SELECT c.FirstName, c.LastName, bestEmployeeYTD.bestYTD " +
      "from (select SalesPersonID, SalesYTD as bestYTD from salesperson order by SalesYTD desc limit ?) " +
      "as bestEmployeeYTD inner join " +
      "employee e on bestEmployeeYTD.SalesPersonID = e.EmployeeID inner join " +
      "contact c on e.ContactID = c.ContactID;" ;

      PreparedStatement statement =conn.prepareStatement(sql);
      statement.setInt(1, num);

      ResultSet resultSet=statement.executeQuery();
      TableGUI salesGUI = new TableGUI(resultSet);
      // displayResultSet(resultSet,'-',150);
      // System.out.println();
      // resultSet.close();
      // statement.close();
    }// end function




    public void jdbShowReasonCount (Connection conn) throws SQLException {

      String sql="select sr.Name as reason, count(*) as orderCount  from " +
      "salesorderheader sh inner join\r\n" +
      "salesorderheadersalesreason shr using(SalesOrderID) inner join " +
      "salesreason sr using(SalesReasonID) " +
      "group by sr.Name " +
      "order by count(*) desc;";
      Statement statement =conn.createStatement();
      ResultSet resultSet=statement.executeQuery(sql);
      TableGUI reasongui = new TableGUI(resultSet);
      // displayResultSet(resultSet,'-',150);
      // System.out.println();
      // resultSet.close();
      // statement.close();
    }// end function

    public void jdbShowSalesMonthly(int year, Connection conn) throws SQLException {
      String sql="select  month(OrderDate) as month, sum(SubTotal) as sales  from " +
      "salesorderheader " +
      "where year(OrderDate)= ? " +
      "group by year(OrderDate) ,month(OrderDate) " +
      "order by month(OrderDate)";

      PreparedStatement statement =conn.prepareStatement(sql);
      statement.setInt(1, year);

      ResultSet resultSet=statement.executeQuery();
      TableGUI monthlyGUI  = new TableGUI(resultSet);
      // displayResultSet(resultSet,'-',150);
      // System.out.println();
      // resultSet.close();
      // statement.close();
    }// end function



    // private String removeSemicolon(String str) {
    //   String result = str;
    //   if (str.charAt(str.length() - 1) == ';') {
    //     result = str.substring(0, str.length() - 1);
    //   }
    //   return result;
    // }








  }// end MainMainInterface class
