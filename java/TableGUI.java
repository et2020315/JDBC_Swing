

import javax.swing.*;
import java.awt.*;
// import java.awt.event.*;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.io.*;
import java.util.*;
import java.sql.*;



// this class could be used to show tables--- first 3 GUI tasks in phase 3

// https://stackoverflow.com/questions/19373186/copying-a-2d-string-arraylist


public class TableGUI extends JFrame{


	// must use 2D primitive String array here
	private String [][] table;
	private ImageIcon image;
	private final int width_base = 800;
	private final int height_base = 600;
	private JTable jtable;
	private String [] cols;
	private JScrollPane scroll;


	// constructor of the TableGUI class. The constructor will pop up the image
	public TableGUI(ResultSet rs){
		try{
			this.setSize(width_base,height_base);
			this.setTitle("Present table in GUI");
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			ResultSetMetaData rsmd = rs.getMetaData();
			int numCols = rsmd.getColumnCount();
			
			ArrayList<String[]> table_al = new ArrayList<String[]>();
			while (rs.next()) {
				String[] row = new String[numCols];
				for (int i = 0; i < numCols; i++)
					row[i] = rs.getString(i);
				table_al.add(row);
			}
			table = table_al.toArray(new String[table_al.size()][]);
			
			// convert array list of column names to string array to pass into JTable
			cols = new String[numCols];
			for(int i = 0; i < numCols; i++){
				cols[i] = rsmd.getColumnLabel(i);
			}

			// JTable element requires Object [][] for the table you want to show and Object[] for column row
			jtable = new JTable(table, cols);
			jtable.setShowGrid(true);
			// use the JTable to instantiate ScrollPane, required
			// add JScrollPane to JPanel
			add(new JScrollPane(jtable));
			// set the panel to be visible
			setVisible(true);


		} catch(Exception e){
			System.out.println("Something wrong in constructor");
			e.printStackTrace();
		}
	}// end constructor


}
