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
					System.out.println("jdb-stat");
					break;
				case "jdb-customer-info": // jdb-customer-info "conditions" count? groupBy
					String query = "SELECT ";
					boolean counting = (parsed_command.length == 3 && parsed_command[1].equalsIgnoreCase("groupby")) 
							|| parsed_command.length == 4 && (parsed_command[2].equalsIgnoreCase("groupby"));
					
					if (counting) {
						query += parsed_command[parsed_command.length - 1] + ",Count(*) ";
					} else {
						query += "AccountNumber, CustomerType, "
								+ "AddressLine1, AddressLine2, City, stateprovince.Name, PostalCode ";
					}
					
					query += "FROM customer "
							+ "INNER JOIN customeraddress ON (customer.CustomerID=customeraddress.CustomerID) "
							+ "INNER JOIN address ON (customeraddress.AddressID=address.AddressID) "
							+ "INNER JOIN stateprovince ON (address.StateProvinceID=stateprovince.StateProvinceID) ";
					
					if (parsed_command.length > 1 && !parsed_command[1].equalsIgnoreCase("groupby")) {
						query += "WHERE " + parsed_command[1] + " LIMIT 1000";
					}
					
					if (counting) {
						query += "GROUP BY " + parsed_command[parsed_command.length - 1];
					}
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
	}
}
