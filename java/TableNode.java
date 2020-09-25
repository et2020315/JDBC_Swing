import java.sql.*;
import java.util.*;
import java.lang.*;


public class TableNode{
  public String t1;
  public Integer nodelabel = 0; // visited

  TableNode(){
    t1 = "";
    nodelabel = 0;
  }

  TableNode(String t, Integer l){
    t1 = t;
    nodelabel = l;
  }

  TableNode(TableNode n){
    this.t1 = n.t1;
    this.nodelabel = n.nodelabel;
  }

};
