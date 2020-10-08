import java.util.regex.*;
import java.util.*;

public class Test {
	public static void main(String[] args) {
		try{
			// Scanner sc = new Scanner(System.in);
			// String subjectString = sc.nextLine();
			// List<String> matchList = new ArrayList<String>();
			// Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
			// Matcher regexMatcher = regex.matcher(subjectString);
			// while (regexMatcher.find()) {
			// 	if (regexMatcher.group(1) != null) {
			// 		// Add double-quoted string without the quotes
			// 		matchList.add(regexMatcher.group(1));
			// 	} else if (regexMatcher.group(2) != null) {
			// 		// Add single-quoted string without the quotes
			// 		matchList.add(regexMatcher.group(2));
			// 	} else {
			// 		// Add unquoted word
			// 		matchList.add(regexMatcher.group());
			// 	}
			// }
			// System.out.println(matchList);

			// ArrayList<ArrayList<String>> tbpass = new ArrayList<ArrayList<String>>();
			// tbpass.add(new ArrayList<String>(Arrays.asList("1","employee","A")));
			// tbpass.add(new ArrayList<String>(Arrays.asList("2","vendorcontact","B")));
			// tbpass.add(new ArrayList<String>(Arrays.asList("3","salesmen","C")));
			// tbpass.add(new ArrayList<String>(Arrays.asList("4","product","D")));
			//
			//
			// ArrayList<String> col = new ArrayList<String>();
			// col.add("c1");
			// col.add("c2");
			// col.add("c3");
			//
			// // int num = 1;
			//
			// TableGUI test1 = new TableGUI(tbpass,col);

			//
			// PNGGUI png1 = new PNGGUI("DB.png");
			// List<Integer> llist = new LinkedList<Integer>();
      // llist.add(2);
      // llist.add(4);
			// llist.add(6);
			// llist.add(10);
			// ListGUI lsgui1 = new ListGUI(llist);

			// ArrayList<String>

			String testQuery1 = "jdb-get-view howdy (select employeeID, productID from employee,product);";
			String testQuery2 = "jdb-show-related-tables employeeaddress;";
			String testQuery3 = "jdb-show-all-primary-keys;";
			String testQuery4 = "jdb-find-column ScrapReasonID;";
			String testQuery5 = "jdb-search-path employee product;";
			String testQuery6 = "jdb-search-and-join employee product;";
			String testQuery7 = "jdb-stat salesorderheader SubTotal";

			String testQuery8 = "jdb-show-best-salesperson 10;";
			String testQuery9 = "jdb-show-reason-count;";
			String testQuery10 = "jdb-show-sales-monthly 2002;";

			String testQuery11 = "jdb-show-best-salesperson;";
			String testQuery12 = "jdb-show-sales-monthly;";

			String testQuery13 = "jdb-customer-info state \"state.zipcode < 50000\" groupby state.statename;";

			String testQuery20 = "jdb-show-sales-monthly msodf;";



 			MainMainInterface testMMI = new MainMainInterface();
			testMMI.setQueryString(testQuery13);
			testMMI.switchOnFirstWord();

		} catch (Exception e){
			System.out.println("********************");
			e.printStackTrace();
		}// end catch



	}
}
