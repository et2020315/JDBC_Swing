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

			String testQuery1 = "jdb-get-view howdy (select employeeID, productID from employee,product);";
			String testQuery2 = "jdb-show-related-tables employeeaddress;";
			String testQuery3 = "jdb-show-all-primary-keys;";
			String testQuery4 = "jdb-find-column ScrapReasonID;";
			String testQuery5 = "jdb-search-path employee product;";
			String testQuery6 = "jdb-search-and-join employee product;";
			String testQuery7 = "jdb-stat salesview salesstat;";
 			MainMainInterface testMMI = new MainMainInterface();
			testMMI.setQueryString(testQuery7);
			testMMI.switchOnFirstWord();

		} catch (Exception e){
			System.out.println("********************");
			e.printStackTrace();
		}// end catch



	}
}
