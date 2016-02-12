
import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

public class ViolationHistoryGUI extends Setplace implements ActionListener{//, ItemListener{
	
	   private JFrame frame;
	   private JButton Exit;
	   private JButton Violator;
	   private JButton Delete;
	   JCheckBox[] check;
       Connection conn = null;
       ResultSet rs;
       int x;
       int rows;
       int violatorrows;
       int deleterows;
       String sql;
       String[][] content;
       String[] time;
       private String[] tablet={"time","username","sitename","device","mac","reason"};
       
       private DefaultTableModel table;
       
   	   private JTable jtable;
       
	   String url = "jdbc:mysql://localhost:3306/dbms?"+"user=root&useUnicode=true&characterEncoding=UTF8";
	
	public ViolationHistoryGUI(int box){
		
		rows = box;
		   
		 try{
	        	//Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver
	        	conn = DriverManager.getConnection(url);
	        	Statement stmt = conn.createStatement();
	        	
	        	content = new String[box][6];
	        	time = new String[box];
	        	
	        	sql = "select * from violationhistory";
	        	rs= stmt.executeQuery(sql); 
	        	/**
	        	 * Store the current Account info into Administrator GUI
	        	 */
	        	while(rs.next()){
	        	content[x][0]=	rs.getString(1);
	        	content[x][1]=	rs.getString(2);
	        	content[x][2]=	rs.getString(3);
	        	content[x][3]=	rs.getString(4);
	        	content[x][4]=	rs.getString(5);
	        	content[x][5]=	rs.getString(6);
	        	time[x]= rs.getString(3);
	        	x++;
	        	}
	        	
	        }catch (SQLException e1){
	        	System.out.println("MySQL Operation Error");
	        	e1.printStackTrace();
	        }finally{
	        	//conn.close();
	        }
    
		   /*check = new JCheckBox[box] ;
		   for(int i=0;i<box;i++){
			   check[i] = new JCheckBox(content[i]);
		   }*/
		 
		  table=new DefaultTableModel(content,tablet);
		  jtable=new JTable(table);
		  
		  
		   JLabel Loginlabel = new JLabel("Check Violation History");
		   
		  
		  Exit = new JButton("Exit");
		  Delete = new JButton("Delete");
          Violator = new JButton("Show Violators");
		   
		 
		   Exit.addActionListener(this);
		   Delete.addActionListener(this);
		   Violator.addActionListener(this);
		   
		   JPanel center = new JPanel(new GridLayout(box,1));
		   
		   /*for(int j=0;j<box;j++)
		   center.add(check[j]);
		   */
		   
		   center.add(jtable);
		   JScrollPane scrollPane = new JScrollPane(center);
		   
		   JPanel south = new JPanel(new FlowLayout());
		   south.add(Violator);
		   //south.add(Delete);
		   south.add(Exit);
		   
		   frame = new JFrame("Violation History Checking");
		   ///frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		   ///
 		   frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	     	  frame.addWindowListener( new WindowAdapter() {
	   	           @Override
	   	           public void windowClosing(WindowEvent we) {
	   	        	   //new UserEntryGUI(activename);
	   				   //new AdminGUI();
	   	   			   frame.dispose();
	   	               //System.exit(0);   
	   	   			try{
	   		        	Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver	
	   		        	conn = DriverManager.getConnection(url); // Establish the connection       	
	   		        	Statement stmt = conn.createStatement();
	   		        	deleterows = stmt.executeUpdate("truncate table violationhistory");
	   				}
	   				catch (ClassNotFoundException e1) {
	   					// TODO Auto-generated catch block
	   					e1.printStackTrace();
	   				} catch (SQLException e1) {
	   					// TODO Auto-generated catch block
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
		   frame.pack();
		   frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {

		
		if(e.getSource()==Delete){
	        try{
	        	Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver	
	        	conn = DriverManager.getConnection(url); // Establish the connection       	
	        	Statement stmt = conn.createStatement();
	        	for(int i=0;i<rows;i++){
	        		if(check[i].isSelected()){
	        			sql = "delete from violationhistory where time= '"+time[i]+"'";
	        			stmt.executeUpdate(sql);
	        		}
	        	}
	        	JOptionPane.showMessageDialog(null, "You Have Deleted the Selected Entries!");
	        	
	        } catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        finally{}
		}
		else if(e.getSource()==Exit){
			try{
	        	Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver	
	        	conn = DriverManager.getConnection(url); // Establish the connection       	
	        	Statement stmt = conn.createStatement();
	        	deleterows = stmt.executeUpdate("truncate table violationhistory");
			}
			catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			frame.dispose();
		}
		else if(e.getSource()==Violator){
			try{
	        	Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver	
	        	conn = DriverManager.getConnection(url); // Establish the connection       	
	        	Statement stmt = conn.createStatement();
	        	violatorrows = stmt.executeUpdate("insert into violator(name,device,srcmac,violatedtimes) select username, device, srcmac, count(*) from violationhistory group by username");
		  } catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		  finally{}
		new  ViolatorGUI(violatorrows);
		violatorrows=0;
	}
		}
	}


