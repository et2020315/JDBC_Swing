/*
    Phase 1 JDBC Interfacing, Group 5

    Written by Noah Miner, Allen Yang,
    Evelyn Tang, Julie Herrick, George Lan
 */

import java.sql.*;
import java.util.Scanner;
import java.util.regex.*;
import java.util.*;

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
			Class.forName("com.mysql.cj.jdbc.Driver");

			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.createStatement();

			ResultSet rs;
			rs = stmt.executeQuery("USE adventureworks;");

			Pattern spaceSplit = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'"); // splits by space, but not inside quotes

			// for basic SQL commands and calling custom functions
			Scanner sc = new Scanner(System.in);
			boolean loop = true;
			while (loop) {
				System.out.print("jdb> ");
				String command = sc.nextLine();
				if (!validateCommand(command)) {
					System.out.println("Cannot edit database.");
					continue;
				}

				Matcher m = spaceSplit.matcher(command);

				List<String> matches = new ArrayList<String>();
				String query;
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
					if (parsed_command.length != 3) {
						System.out.println("Incorrect amount of arguments");
						break;
					}
					
					// get index of the column chosen in command
					rs = stmt.executeQuery("SELECT column_name FROM information_schema.columns where table_name='" + parsed_command[1] + "'");
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
						System.out.println("Table or Column name not found.");
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
						System.out.println("Column must contain a numeric data type.");
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
					
					System.out.format("Min value: %.2f\n", min);
					System.out.format("Max value: %.2f\n", max);
					System.out.format("Average: %.2f\n", avg);
					System.out.format("Median: %.2f\n", median);
					
					// plotting histogram
					// first get number of bins and bin width
					int num_bins = (int)Math.ceil(Math.sqrt(list.size()));
					double bin_width = (max - min)/num_bins;
					int[] bins = new int[num_bins];
					
					// bins[i] = (min + i*bin_width) - (min + (i+1)*bin_width)
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
					
					// displaying bins
					// printing y-axis
					int offset = String.format("%.2f", max).length() * 2 + 3; // how long the header for each bin should be when displaying
					System.out.println();
					System.out.format("%-" + (offset + 1) + "s", "");
					int max_count_scaled = max_count/y_scale;
					for (int count = 0; count <= max_count_scaled; count++) // printing y-axis labels
						System.out.print(count + "___");
					System.out.format("(Each star and each y-axis label represents %d counts)\n", y_scale);
					
					// printing each bin
					for (int i = 0; i < bins.length; i++) {
						String range = String.format("%.2f - %.2f", min + i*bin_width, min + (i+1)*bin_width);
						System.out.format("%-" + offset + "s |", range);
						for (int j = 0; j < bins[i]; j++) {
							System.out.print("*");
						}
						System.out.println();
					}
					
					break;
				case "jdb-customer-info": // jdb-customer-info "conditions" groupby? columnName // displays individual customers line by line that match "conditions", can group by column name, e.g. how many customers in this state
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
					//query += "LIMIT 1000";
					
					rs = stmt.executeQuery(query);
					printResults(rs);
					break;
				case "jdb-customer-orders": //jdb-customer-orders <condition> <aggregate-by-sales|aggregate>
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
					
					System.out.println(query);
					rs = stmt.executeQuery(query);
					printResults(rs);
					break;
				default: // basic sql commands 
					rs = stmt.executeQuery(command);
					printResults(rs);
					break;
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

	public static boolean validateCommand(String command) {
		command = command.toUpperCase();
		return !(command.contains("CREATE") || command.contains("DROP") ||
				command.contains("ALTER") || command.contains("DELETE") || 
				command.contains("INSERT"));
	}
	
	public static void printResults(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();

		// Printing column names
		for (int i = 1; i <= cols; i++) {
			if (i > 1) 
				System.out.print(", ");
			System.out.print(rsmd.getColumnLabel(i));
		}
		System.out.println("");

		// Printing query contents
		while (rs.next()) {
			for (int i = 1; i <= cols; i++) {
				if (i > 1) 
					System.out.print(", ");
				String colVal = rs.getString(i);
				if (colVal != null && colVal.contains(","))
					System.out.print('"' + colVal + '"');
				else
					System.out.print(colVal);
			}
			System.out.println("");
		}
	}
	
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
}
