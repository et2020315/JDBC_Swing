/*
* Test TableGUI
*
*
*/

import java.util.*;
import java.lang.*;

public class TestTableGUI{
  public static void main(String args[]){

    ArrayList<ArrayList<String>> tbpass = new ArrayList<ArrayList<String>>();
    tbpass.add(new ArrayList<String>(Arrays.asList("1","employee","A")));
    tbpass.add(new ArrayList<String>(Arrays.asList("2","vendorcontact","B")));
    tbpass.add(new ArrayList<String>(Arrays.asList("3","salesmen","C")));
    tbpass.add(new ArrayList<String>(Arrays.asList("4","product","D")));


    ArrayList<String> col = new ArrayList<String>();
    col.add("c1");
    col.add("c2");
    col.add("c3");

    int num = 1;

    TableGUI test1 = new TableGUI(tbpass,col,num);




  }
}
