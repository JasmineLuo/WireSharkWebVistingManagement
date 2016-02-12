import java.awt.*;
import javax.swing.*;

import com.mysql.jdbc.DatabaseMetaData;

import java.awt.event.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
//import java.sql.Statement;

public class UserEntryGUI extends Setplace implements ActionListener{

	public static void main( String[] args ){
		new UserEntryGUI( "ZhongyiLuo");
		// NOTICE: need to have the name of current active acount
	}
	
	public static JFrame UserFrame=new JFrame("User Entry Page");
	private JButton EntryHistory;
	private JButton ChangeEmail;
	private JButton ResetPassword;
	//private JButton	Addev;
	private JButton	Return;
	private String activename;
	int lines;
	
	String url = "jdbc:mysql://localhost:3306/dbms?"+"user=root&useUnicode=true&characterEncoding=UTF8";
	String sql;
	ResultSet result;
	
	Connection conn= null;
	PreparedStatement pst = null;
	private DatabaseMetaData dbm;
	
	public UserEntryGUI( String name){
		
		activename=name;
		
		JLabel entrylabel = new JLabel("Notice: your account will be under check if your info is changed.");
		
		EntryHistory = new JButton("Entry History");
		ChangeEmail = new JButton("Change Email");
		ResetPassword = new JButton("Reset Password");
		//Addev = new JButton("Add Device");
		Return=new JButton("Log Off");
		
		EntryHistory.addActionListener(this);
		ChangeEmail.addActionListener(this);
		ResetPassword.addActionListener(this);
		//Addev.addActionListener(this);
		Return.addActionListener(this);
		
	   JPanel title = new JPanel(new GridLayout(1,2,50,0));
		
	   title.add(entrylabel);
	   JPanel center = new JPanel(new GridLayout(2,2,0,50));
	   center.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
	   center.add(EntryHistory);
	   center.add(ChangeEmail);
	   center.add(ResetPassword);
	   center.add(Return);
	   
	   /*JPanel back = new JPanel(new FlowLayout());
	   back.add(Return);*/
	   
	   /* to drop table when closing*/
	   UserFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	   //UserFrame.dispatchEvent(new WindowEvent(UserFrame, WindowEvent.WINDOW_CLOSING));
       UserFrame.addWindowListener( new WindowAdapter() {
           @Override
           public void windowClosing(WindowEvent we) {
               droptable();
   			   UserFrame.dispose();
               //System.exit(0);               
           }
       } );
       
	   UserFrame.setLayout(new BorderLayout());
	   UserFrame.add(center, BorderLayout.CENTER);
	   UserFrame.add(title, BorderLayout.NORTH);
	   //UserFrame.add(back, BorderLayout.SOUTH);
	   UserFrame.setSize(new Dimension(500,500));
	   UserFrame.setLocation(width/2-200, height/2-250);
	   //frame.pack();
	   UserFrame.setVisible(true);
		   
	   //complete the layout setting and link to button
		
	}
	
	
	//actions accordingly
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//switch (e.getSource().toString()){
		
		if( e.getSource()==EntryHistory){
			try {			
	        	//Drive MySQL 
	        	Class.forName("com.mysql.jdbc.Driver");
	        	System.out.println(" Drive SQL accomplished");
	        	conn = DriverManager.getConnection(url);
	        	//Statement stmt = conn.createStatement();	        		        	
	        	sql="select time from entry where uname = ? ";
	        	pst=conn.prepareStatement(sql);
	        	pst.setString(1, activename);	        	
	        	result=pst.executeQuery();	        	
	        	while(result.next()){
	        		lines++;
	        	};
	        	UserFrame.dispose();
	        	new ShowEntryGUI(lines, activename);
	        	//GUI for User View Entry
	    		lines=0;
	    		
	        	//PreparedStatement pst = null;
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();				
			}			
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}finally{
				try {
					conn.close();
					pst.close();
					result.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		else if (e.getSource()== ChangeEmail){
			new EmailChangeGUI( activename );	
			UserFrame.dispose();
		}
		
		else if (e.getSource()==ResetPassword){
			new PWChangeGUI( activename );	
			UserFrame.dispose();
		}
		
		else if (e.getSource()==Return){
			JOptionPane.showMessageDialog(null, "Log off Success!");
			UserFrame.dispose();
		}
	/*	else if(e.getSource()==Addev){
			new AddDeviceGUI(activename);
			UserFrame.dispose();
		}*/
		/*case "ChangeEmail": {
			
		}
			break;
		case "ResetPassword": 
			break;
		case "ForgetPassword":
			break;
		case "Return":
			break;
		default:
			System.out.println(e.getSource());
			break;		
			
		}*/
		
	}	
	// a interface provide function as: changing password;
	//									email address;
	//									add device;
	//									check recent entry: according to time/ device/ dst
	private void droptable() {
		// TODO Auto-generated method stub
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url);
	    	//Statement stmt = conn.createStatement();	  
			
			dbm=(DatabaseMetaData) conn.getMetaData();
        	ResultSet tables1= dbm.getTables( null, null, "working", null);
        	ResultSet tables2= dbm.getTables( null, null, "temp", null);
			
	    	if (tables1.next()){
	        	sql="drop table working";
		    	pst=conn.prepareStatement(sql);        	
		    	pst.executeUpdate();	
	    	}  
	    	
	    	if (tables2.next()){
	        	sql="drop table temp";
		    	pst=conn.prepareStatement(sql);        	
		    	pst.executeUpdate();	
	    	} 
	    	
	    	pst.close();
	    	conn.close();
	    	
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println(" temp and working table are droped.");
				
	}
}
