import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

public class AccountCheckGUI extends Setplace implements ActionListener{//, ItemListener{
	
	   private JFrame frame;
	   private JButton Approve;
	   private JButton Cancel;
	   private JButton Delete;
	   private JTextField[] Mac;
	   private JTextField[] ID;
	   JCheckBox[] check;
       Connection conn = null;
       ResultSet rs;
       int x;
       int rows;
       String sql;
       String sql1;
       String sql2;
       String sql3;
       String[] content;
       String[] id;
       String[] macaddr;
       String[] name;
       
	   String url = "jdbc:mysql://localhost:3306/dbms?"+"user=root&useUnicode=true&characterEncoding=UTF8";
	public AccountCheckGUI(int box){
		
		rows = box;
		   
		 try{
	        	//Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver
	        	conn = DriverManager.getConnection(url);
	        	Statement stmt = conn.createStatement();
	        	
	        	content = new String[box];
	        	id = new String[box];
	        	macaddr = new String[box];
	        	name = new String[box];
	        	
	        	sql = "select * from account";
	        	rs= stmt.executeQuery(sql); 
	        	/**
	        	 * Store the current Account info into Administrator GUI
	        	 */
	        	while(rs.next()){
	        	content[x]=	rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(5)+" "+rs.getString(6)+" "+rs.getString(7);
	        	id[x]= rs.getString(2);
	        	macaddr[x]=rs.getString(7);
	        	name[x]=rs.getString(3);
	        	x++;
	        	}
	        	
	        }catch (SQLException e1){
	        	System.out.println("MySQL Operation Error");
	        	e1.printStackTrace();
	        }finally{
	        	//conn.close();
	        }
    
		   check = new JCheckBox[box];
		   Mac = new JTextField[box];
		   ID = new JTextField[box];
		   
		   for(int i=0;i<box;i++){
			   check[i] = new JCheckBox(content[i]);
			   Mac[i] = new JTextField(12);
               Mac[i].setText(macaddr[i]);
               ID[i] = new JTextField(6);
               ID[i].setText(id[i]);
   
		   }
		   JLabel Loginlabel = new JLabel("Check Registration");
		   
		   Approve = new JButton("Approve");
		   Cancel = new JButton("Cancel");
		   Delete = new JButton("Delete");

		   
		   Approve.addActionListener(this);
		   Cancel.addActionListener(this);
		   Delete.addActionListener(this);
		   
		   JPanel center = new JPanel(new GridLayout(box,1));
		   
		   for(int j=0;j<box;j++){
		   center.add(check[j]);
		   center.add(Mac[j]);
		   }
		   JScrollPane scrollPane = new JScrollPane(center);
		   
		   JPanel east = new JPanel(new GridLayout(box,3));
		   for(int j=0;j<box;j++){

			   east.add(Mac[j]);
			   east.add(ID[j]);
			}
		   
		   JPanel south = new JPanel(new FlowLayout());
		   south.add(Approve);
		   south.add(Delete);
		   south.add(Cancel);
		   
		   frame = new JFrame("Account Application Checking");
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
	   	           }
	   	       } );	 
		   ///
		   frame.setLayout(new BorderLayout());
		   frame.add(scrollPane, BorderLayout.CENTER);
		   frame.add(Loginlabel, BorderLayout.NORTH);
		   frame.add(south, BorderLayout.SOUTH);
		   frame.add(east, BorderLayout.EAST);
		   frame.setSize(new Dimension(300,180));
		   frame.setLocation(width/2-200, height/2-150);
		   frame.pack();
		   frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
       
		if(e.getSource()==Approve){
	        try{
	        	Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver	
	        	conn = DriverManager.getConnection(url); // Establish the connection       	
	        	Statement stmt = conn.createStatement();
	        	
	        	
	        	for(int i=0;i<rows;i++){
	        		if(check[i].isSelected()){      			
	        			String mac = Mac[i].getText();
	        			String id = ID[i].getText();
	        			String Name = name[i];
	        			System.out.println(Name);
	        			sql = "update account set status='approved' where id= '"+id+"'";
	        			sql2 = "update account set mac = '"+mac+"' where id= '"+id+"'";
	        			
	        			stmt.executeUpdate(sql);
	        			stmt.executeUpdate(sql2);
	        			
	        			String j = content[i].substring(0, 3);
	        			System.out.println(j);
	        			sql3 = "update account set id = '"+id+"' where username = '"+Name+"' and id = '"+j+"'";
	        			stmt.executeUpdate(sql3);
	        		}
	        	}
	        	JOptionPane.showMessageDialog(null, "You Have Approved Checked Registration!");
	        	
	        } catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        finally{}
		}
		if(e.getSource()==Delete){
	        try{
	        	Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver	
	        	conn = DriverManager.getConnection(url); // Establish the connection       	
	        	Statement stmt = conn.createStatement();
	        	for(int i=0;i<rows;i++){
	        		if(check[i].isSelected()){
	        			sql = "delete from account where id= '"+id[i]+"'";
	        			stmt.executeUpdate(sql);
	        		}
	        	}
	        	JOptionPane.showMessageDialog(null, "You Have Deleted Checked Account!");
	        	
	        } catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        finally{}
		}
		else if(e.getSource()==Cancel){
			frame.dispose();
		}
	}


}
