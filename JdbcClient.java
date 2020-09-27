import java.util.Scanner;
import java.sql.*;
import java.util.regex.*;

public class JdbcClient {

	static Scanner userInput = new Scanner(System.in);

	public static void main(String[] args) throws SQLException {
		Connection conn = null;
		try {
			conn = MysqlDBAccess.createConnection("jdbc:mysql://localhost/AdventureWorks", "george",
					"George1234");
		} catch (SQLException se) {
			// Handle errors for JDBC
			System.out.println(se.getMessage());
		}
		

		System.out.println("Note: jdb> is the prompt.");
		System.out.println("Note: all custom commands are prefixed by \"jdb-\".");
		System.out.println();

		while (true) {
			try {
				System.out.print("jdb> ");
				String userCommand = userInput.nextLine().trim();
				if (userCommand.equals("exit")) {
					System.out.println("Goodbye!");
					MysqlDBAccess.closeConnection(conn);
					break;
				}
				String arr[] = userCommand.split(" ", 2);
				String firstWord = removeSemicolon(arr[0]);

				switch (firstWord) {
				case "jdb-show-related-tables": {
					String secondPart = arr[1].trim();
					String tableName = removeSemicolon(secondPart);
					MysqlDBAccess.jdbShowRelatedTables(tableName, conn);
					break;
				}
				case "jdb-show-all-primary-keys": {
					MysqlDBAccess.jdbShowAllPrimaryKeys(conn);
					break;
				}
				case "jdb-find-column": {

					String secondPart = arr[1].trim();
					String columnName = removeSemicolon(secondPart);
					MysqlDBAccess.jdbFindColumn(columnName, conn);
					break;
				}
				case "jdb-search-path": {

					System.out.println("jdb-search-path function to be implemented");
					break;
				}
				case "jdb-search-and-join": {

					System.out.println("jdb-search-and-join function to be implemented");
					break;
				}
				case "jdb-get-view": {
					String viewName = "";
					String sql = "";
					String secondPart = arr[1].trim();
					String theRegex = "(\\S+?)\\s+\\((.*)\\)";
					Pattern checkRegex = Pattern.compile(theRegex);
					Matcher regexMatcher = checkRegex.matcher(secondPart);
					if (regexMatcher.find()) {
						viewName = regexMatcher.group(1);
						sql = regexMatcher.group(2).trim();
					} else {
						System.out.println("invalid command, please try again");
						break;
					}

					MysqlDBAccess.jdbGetView(viewName, sql, conn);
					break;
				}
				case "jdb-stat": {
					String tableViewName = arr[1].trim().split("\s+")[0];
					String columnName = arr[1].trim().split("\s+")[1];
					MysqlDBAccess.jdbStat(tableViewName, columnName, conn);
					break;
				}
				case "jdb-show-best-Salesperson": {
					int num =Integer.parseInt( removeSemicolon( arr[1].trim())) ;					
					MysqlDBAccess.jdbShowBestSalesperson(num, conn);
					break;
				}
				case "jdb-show-reason-count": {										
					MysqlDBAccess.jdbShowReasonCount(conn);
					break;
				}
				case "jdb-show-sales-monthly": {
					int year =Integer.parseInt( removeSemicolon( arr[1].trim())) ;					
					
					MysqlDBAccess.jdbShowSalesMonthly (year, conn);
					break;
				}
				

				default:
					MysqlDBAccess.getResult(userCommand, conn);
				} // end switch
			} // end try

			catch (Exception e) {
				// Handle errors for Class.forName
				System.out.println(e.getMessage());
				System.out.println();
			}

		} // end while
	}

	private static String removeSemicolon(String str) {
		String result = str;
		if (str.charAt(str.length() - 1) == ';') {
			result = str.substring(0, str.length() - 1);
		}
		
		return result;
	}
}
