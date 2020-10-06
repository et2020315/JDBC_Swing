/*
*
*
* MainInterface --- name might be changed later
*/


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


    MainMainInterface(){
      
    }

}
