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

public class ViolatorGUI extends Setplace implements ActionListener{//, ItemListener{
	
	   private JFrame frame;
	   private JButton Exit;
	   private JButton AccessControl;
	   private JButton Delete;
	   JCheckBox[] check;
       Connection conn = null;
       ResultSet rs;
       int x;
       int violatorrows;
       int deleterows;
       String sql;
       String[][] content;
       String[] name;
       
       private String[] tablet={"name","device","srcmac","violatetimes"};
       
       private DefaultTableModel table;       
   	   private JTable jtable;
	   String url = "jdbc:mysql://localhost:3306/dbms?"+"user=root&useUnicode=true&characterEncoding=UTF8";
	
	public ViolatorGUI(int box){
		
		violatorrows = box;
		   
		 try{
	        	//Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver
	        	conn = DriverManager.getConnection(url);
	        	Statement stmt = conn.createStatement();
	        	
	        	content = new String[box][4];
	        	name = new String[box];
	        	
	        	sql = "select * from violator";
	        	rs= stmt.executeQuery(sql); 
	        	/**
	        	 * Store the current Account info into Administrator GUI
	        	 */
	        	while(rs.next()){
	        	content[x][0]=	rs.getString(1);
	        	content[x][1]=	rs.getString(2);
	        	content[x][2]=	rs.getString(3);
	        	content[x][3]=	rs.getString(4);
	        	name[x]= rs.getString(2);
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
		   }
		   */
		 
		   table=new DefaultTableModel(content, tablet);
		   jtable=new JTable(table);
		 
		   JLabel Loginlabel = new JLabel("Check Violator List");
		   
		  
		  Exit = new JButton("Exit");
		  Delete = new JButton("Delete");
          AccessControl = new JButton("Perform AccessControl");
		   
		 
		   Exit.addActionListener(this);
		   Delete.addActionListener(this);
		   AccessControl.addActionListener(this);
		   
		   JPanel center = new JPanel(new GridLayout(box,1));
		   
		   center.add(jtable);
		   /*for(int j=0;j<box;j++)
		   center.add(check[j]);
		   */
		   JScrollPane scrollPane = new JScrollPane(center);
		   
		   JPanel south = new JPanel(new FlowLayout());
		   south.add(AccessControl);
		   south.add(Delete);
		   south.add(Exit);
		   
		   frame = new JFrame("Violator List Checking");
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
	   	   			try{
	   		        	Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver	
	   		        	conn = DriverManager.getConnection(url); // Establish the connection       	
	   		        	Statement stmt = conn.createStatement();
	   		        	deleterows = stmt.executeUpdate("truncate table violator");
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
	        	for(int i=0;i<violatorrows;i++){
	        		if(check[i].isSelected()){
	        			sql = "delete from violator where name= '"+name[i]+"'";
	        			stmt.executeUpdate(sql);
	        		}
	        	}
	        	JOptionPane.showMessageDialog(null, "You Have Deleted the Selected Violator Information!");
	        	
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
	        	deleterows = stmt.executeUpdate("truncate table violator");
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



