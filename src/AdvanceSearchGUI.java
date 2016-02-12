import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;


public class AdvanceSearchGUI extends Setplace implements ActionListener, ItemListener {
  
   private JFrame frame;
   JTextField input;
   JCheckBox [] check;
   private JButton Search;
   private JButton Cancel;
   Choice ch;
   String a;
   String[] content;
   int x=0;
   String sql;
   int searchrows;
   int box;
   String url = "jdbc:mysql://localhost:3306/dbms?"+"user=root&useUnicode=true&characterEncoding=UTF8";
   
   public AdvanceSearchGUI(int rows){
	   
	   box=rows;
	   input = new JTextField(6);
	   ch = new Choice();
	   ch.add("Please select a search type");
	   ch.add("Username");
	   ch.add("Device Type");
	   //ch.add("Protocol Type");
	   
	   ch.addItemListener(this);
	   
	   
	   JLabel Loginlabel = new JLabel("Please Enter Information for Advanced Search");
	   
	   Search = new JButton("Search");
	   Cancel = new JButton("Cancel");
	   
	   Search.addActionListener(this);
	   Cancel.addActionListener(this);
	   
	   
	   
	   JPanel center = new JPanel(new GridLayout(3,2));
	   center.add(new JLabel("Search by: "));
	   center.add(ch);
	   center.add(input);
	 
	   JScrollPane scrollPane = new JScrollPane(center);
	   
	   JPanel south = new JPanel(new FlowLayout());
	   south.add(Search);
	   south.add(Cancel);
	   
	   frame = new JFrame("Advanced Search");
	   ///frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   ///
		  frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
     	  frame.addWindowListener( new WindowAdapter() {
   	           @Override
   	           public void windowClosing(WindowEvent we) {
   	        	   //new UserEntryGUI(activename);
   				   //new EntryGUI(box);
   	   			   frame.dispose();
   	               //System.exit(0);
   	   			try{
   	   	        	Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver	
   	   	        	Connection conn = DriverManager.getConnection(url); // Establish the connection       	
   	   	        	Statement stmt = conn.createStatement();
   	   	        	stmt.executeUpdate("truncate table search");

   	   		  } catch (ClassNotFoundException e1) {
   	   			e1.printStackTrace();
   	   		} catch (SQLException e1) {
   	   			e1.printStackTrace();
   	   		}
   	           }
   	       } );	 
	   ///
	   frame.setLayout(new BorderLayout());
	   frame.add(scrollPane, BorderLayout.CENTER);
	   frame.add(Loginlabel, BorderLayout.NORTH);
	   frame.add(south, BorderLayout.SOUTH);
	   frame.setSize(new Dimension(300,180));
	   frame.setLocation(width/2-200, height/2-150);
	   frame.setVisible(true);
   }

   @Override
   public void itemStateChanged(ItemEvent e){
   	if(ch.getSelectedItem().equals("Username")){
   		a="username";
   	}
   	if(ch.getSelectedItem().equals("Device Type")){
   		a="device";
   	}
   	/*if(ch.getSelectedItem().equals("Protocol Type")){
   		a="protocol";
   	}*/
   }
   
 @Override
public void actionPerformed(ActionEvent e) {  // React for clicking on the Login Button
	Connection conn = null;
	String text = input.getText();

	if (e.getSource()==Search){

		if(text.equals("")){
			JOptionPane.showMessageDialog(null, "Please Enter the Required Information");
		}
		else {
		// compare the database account and current input JDBC
		
        
        	if(a.equals("username")){
        		try{
    	        	Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver	
    	        	conn = DriverManager.getConnection(url); // Establish the connection       	
    	        	Statement stmt = conn.createStatement();
    	        	searchrows = stmt.executeUpdate("insert into search select * from entry where uname='"+text+"'");
    		  } catch (ClassNotFoundException e1) {
    			e1.printStackTrace();
    		} catch (SQLException e1) {
    			e1.printStackTrace();
    		}
    		  finally{}
    		new  SearchResultGUI(searchrows);
    		searchrows=0;
    	}
        	
        		
            
        	   
        	   else if(a.equals("device")){
        		   try{
       	        	Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver	
       	        	conn = DriverManager.getConnection(url); // Establish the connection       	
       	        	Statement stmt = conn.createStatement();
       	        	searchrows = stmt.executeUpdate("insert into search select * from entry where device='"+text+"'");
       		  } catch (ClassNotFoundException e1) {
       			e1.printStackTrace();
       		} catch (SQLException e1) {
       			e1.printStackTrace();
       		}
       		  finally{}
       		new  SearchResultGUI(searchrows);
       		searchrows=0;
       	}
        	   
        		  
        	
        	   else {/*
        		   try{
          	        	Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver	
          	        	conn = DriverManager.getConnection(url); // Establish the connection       	
          	        	Statement stmt = conn.createStatement();
          	        	searchrows = stmt.executeUpdate("insert into search select * from entry where protocol=' "+text+"'");
          		  } catch (ClassNotFoundException e1) {
          			e1.printStackTrace();
          		} catch (SQLException e1) {
          			e1.printStackTrace();
          		}
          		  finally{}
          		new  SearchResultGUI(searchrows);
          		searchrows=0;*/
          	}
        	 
		}  
	}
		if(e.getSource()==Cancel){
			frame.dispose();
			
			try{
   	        	Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver	
   	        	conn = DriverManager.getConnection(url); // Establish the connection       	
   	        	Statement stmt = conn.createStatement();
   	        	stmt.executeUpdate("truncate table search");

   		  } catch (ClassNotFoundException e1) {
   			e1.printStackTrace();
   		} catch (SQLException e1) {
   			e1.printStackTrace();
   		}
		}}}
           