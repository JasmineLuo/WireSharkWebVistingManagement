import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

public class SearchResultGUI extends Setplace implements ActionListener{//, ItemListener{
	
	   private JFrame frame;
	   private JButton Exit;
	   private JButton AccessControl;
	  
	   JCheckBox[] check;
       Connection conn = null;
       ResultSet rs;
       int x;
       int searchrows;
       int deleterows;
       
       DefaultTableModel table;
       JTable jtable;
     
       String sql;
       String[][] content;
    
       String[] tablet={"id", "time","uname","srcIP","dstIP","protocol","dstName","device","srcmac","dstmac"};
	   String url = "jdbc:mysql://localhost:3306/dbms?"+"user=root&useUnicode=true&characterEncoding=UTF8";
	
	public SearchResultGUI(int box){
		
		searchrows = box;
		   
		 try{
	        	//Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver
	        	conn = DriverManager.getConnection(url);
	        	Statement stmt = conn.createStatement();
	        	
	        	content = new String[box][10];
	        	
	        	
	        	sql = "select * from search";
	        	rs= stmt.executeQuery(sql); 
	        	/**
	        	 * Store the current Account info into Administrator GUI
	        	 */
	        	while(rs.next()){
	        	content[x][0]=	rs.getString(2);
	        	content[x][1]=	rs.getString(3);
	        	content[x][2]=	rs.getString(4);
	        	content[x][3]=	rs.getString(5);
	        	content[x][4]=	rs.getString(6);
	        	content[x][5]=	rs.getString(7);
	        	content[x][6]=	rs.getString(8);
	        	content[x][7]=	rs.getString(9);
	        	content[x][8]=	rs.getString(10);
	        	content[x][9]=	rs.getString(11);
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
		   
		    table= new DefaultTableModel(content, tablet);
		   
		    jtable = new JTable(table);
       		JScrollPane scrollPane = new JScrollPane(jtable);
       		jtable.setFillsViewportHeight(true);
       	// slide
       	/*show result*/
       	    JPanel center = new JPanel(new GridLayout(1,100)); // gridlayout for query results
       													// how 2 arrange them into rows and slide?		   
    		center.add(scrollPane); // the total query result*/     		   
		 
		    JLabel Loginlabel = new JLabel("Advanced Search Result");
		   
		  
		  Exit = new JButton("Exit");
		  
          AccessControl = new JButton("Perform AccessControl");
		   
		 
		   Exit.addActionListener(this);
		   
		   AccessControl.addActionListener(this);
		   
		   //JPanel center = new JPanel(new GridLayout(box,1));
		   //for(int j=0;j<box;j++)
		   //center.add(check[j])  
		   //JScrollPane scrollPane = new JScrollPane(center);
		   
		   JPanel south = new JPanel(new FlowLayout());
		   south.add(AccessControl);
		  
		   south.add(Exit);
		   
		   frame = new JFrame("Advanced Search");
		  /// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  ///
 		   frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	     	  frame.addWindowListener( new WindowAdapter() {
	   	           @Override
	   	           public void windowClosing(WindowEvent we) {
	   	        	   //new UserEntryGUI(activename);
	   				   //new AdminGUI();
	   	   			   frame.dispose();
	   	               //System.exit(0);               
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

		
	   if(e.getSource()==Exit){
			try{
	        	Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver	
	        	conn = DriverManager.getConnection(url); // Establish the connection       	
	        	Statement stmt = conn.createStatement();
	        	deleterows = stmt.executeUpdate("truncate table search");
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
		else if(e.getSource()==AccessControl){
			try{
				Runtime.getRuntime().exec("C:/Program Files (x86)/Connectify/Connectify.exe");
			}
			catch (IOException e1){
				e1.printStackTrace();
			}
		  finally{}
		
	}
		}
	}



