
import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.io.File;
import java.io.IOException;


public class AdminGUI extends Setplace implements ActionListener{
   public static void main(String[] args){
	   new AdminGUI();
   }

   private JButton Logout;
   private JButton Check_Reg;
   private JButton Check_Entry;
   private JButton Send_Warning;
   private JButton Check_Violation;
   private JButton Access_Control;
   private JButton Network_Monitor;
   private JButton Import;
   private JButton Website;
   
   int rows; // For checking Box in Registration Box. Get from rs.
   
   String url = "jdbc:mysql://localhost:3306/dbms?"+"user=root&useUnicode=true&characterEncoding=UTF8";
   
   String sql;
   
   public AdminGUI(){

	   
	   JLabel Loginlabel = new JLabel("Welcome to WIFI DBMS: ");
	   
	   //Login = new JButton("Login");
	   Logout = new JButton("Logout");
	   Check_Reg = new JButton("Account Management");
	   Check_Entry = new JButton("Check Accesss Entry");
	   Send_Warning = new JButton("Send Warning Message");
	   Check_Violation = new JButton("Check Violation History");
	   Access_Control = new JButton("Perform Access Control");
	   Network_Monitor= new JButton("Monitor the network");
	   Import = new JButton("Import Wireshark Files");
	   Website = new JButton("Update Website List");
	   //Login.addActionListener(this);
	   Logout.addActionListener(this);
	   Check_Reg.addActionListener(this);
	   Check_Entry.addActionListener(this);
	   Check_Violation.addActionListener(this);
	   Send_Warning.addActionListener(this);
	   Access_Control.addActionListener(this);
	   Network_Monitor.addActionListener(this);
	   Import.addActionListener(this);
	   Website.addActionListener(this);
	   
	   JPanel north = new JPanel(new GridLayout(1,2,50,0));
	   north.add(Loginlabel);
	   
	   JPanel center = new JPanel(new GridLayout(3,3,10,30));
	   center.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
	   center.add(Check_Reg);
	   center.add(Check_Entry);
	   center.add(Check_Violation);
	   center.add(Send_Warning);
	   center.add(Access_Control);
	   center.add(Network_Monitor);
	   center.add(Website);
	   center.add(Import);
	   center.add(Logout);
	   
	   ///JPanel south = new JPanel(new FlowLayout());
	   ///south.add(Logout);
	   ///south.add(Import);
	   
	   JFrame frame = new JFrame("Administration Page");
	   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   frame.setLayout(new BorderLayout());
	   frame.add(center, BorderLayout.CENTER);
	   frame.add(north, BorderLayout.NORTH);
	   ///frame.add(south, BorderLayout.SOUTH);
	   frame.setSize(new Dimension(500,500));
	   frame.setLocation(width/2-300, height/2-100);
	   frame.pack();
	   frame.setVisible(true);
   }

@Override
public void actionPerformed(ActionEvent e) {  // React for clicking on the Login Button
	Connection conn = null;
	if (e.getSource()==Check_Entry){
		String url = "jdbc:mysql://localhost:3306/dbms?"+"user=root&useUnicode=true&characterEncoding=UTF8";
        try{
        	Class.forName("com.mysql.jdbc.Driver");  //Dynamic Load Mysql Driver
        	conn = DriverManager.getConnection(url);
        	Statement stmt = conn.createStatement();
        	
        	sql = "select * from entry";
        	ResultSet res = stmt.executeQuery(sql); 
        	while(res.next()){
        		rows++;
        	}
        	
        }catch (SQLException e1){
        	System.out.println("MySQL Operation Error");
        	e1.printStackTrace();
        }
        catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}finally{
        	//conn.close();
        }
        new EntryGUI(rows);
		rows=0;
	}
	else if (e.getSource()==Check_Reg){
		
		  try{
	        	Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver	
	        	conn = DriverManager.getConnection(url); // Establish the connection       	
	        	Statement stmt = conn.createStatement();
	        	
	        	sql = "select * from account";
	        	ResultSet rs = stmt.executeQuery(sql);
	        	while (rs.next()) {
	        	rows++;
	        	}
		  } catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		  finally{}
		new AccountCheckGUI(rows);
		rows=0;
	}
	else if (e.getSource()==Check_Violation){
		
		  try{
	        	Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver	
	        	conn = DriverManager.getConnection(url); // Establish the connection       	
	        	Statement stmt = conn.createStatement();
	        	rows = stmt.executeUpdate("insert into violationhistory(time,username,sitename,device,srcmac) select time,uname,sitename,device,srcmac from (entry join restrictedlist on dstName=sitename)");
		  } catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		  finally{}
		new  ViolationHistoryGUI(rows);
		rows=0;
	}
	else if(e.getSource()==Send_Warning){
		try{
        	Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver	
        	conn = DriverManager.getConnection(url); // Establish the connection       	
        	Statement stmt = conn.createStatement();
        	
        	sql = "select * from account";
        	ResultSet rs = stmt.executeQuery(sql);
        	while (rs.next()) {
        	rows++;
        	}
	  } catch (ClassNotFoundException e1) {
		e1.printStackTrace();
	} catch (SQLException e1) {
		e1.printStackTrace();
	}
	  finally{}
		new WarnMsg(rows);
		rows=0;
	}
	else if(e.getSource()==Access_Control){
		try{
			Runtime.getRuntime().exec("C:/Program Files (x86)/Connectify/Connectify.exe");
		}
		catch (IOException e1){
			e1.printStackTrace();
		}
	}
	else if(e.getSource()==Network_Monitor){
		try{
			//Runtime.getRuntime().exec("E:/StudyTool/Wireshark/Wireshark.exe");
			//Runtime.getRuntime().exec("/Applications/Wireshark.app");
			Desktop.getDesktop().open(new File ("/Applications/Wireshark.app"));
		}
		catch (IOException e1){
			e1.printStackTrace();
		}
	}
	
	else if(e.getSource()==Logout){
		System.exit(0);
	}
	
	else if(e.getSource()==Import){
		new FileChooser();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE); 
                FileChooser.createAndShowGUI();
            }
        });
		//System.out.println("activated");
	}
	
	else if(e.getSource()==Website){
		new ViowebImport("/Users/luozhongyi/Desktop/RestrictedList.txt");
	}
}
}