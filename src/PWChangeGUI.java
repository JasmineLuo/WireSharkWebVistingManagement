import java.awt.*;

import javax.swing.*;

import java.sql.PreparedStatement;
import java.awt.event.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

public class PWChangeGUI extends Setplace implements ActionListener{

	public static void main(){
		new PWChangeGUI("rose");
	}
	
	String url = "jdbc:mysql://localhost:3306/dbms?"+"user=root&useUnicode=true&characterEncoding=UTF8";
	String sql;
	Connection conn= null;
	String activename;
	String OPassWord;
	String TPassWord;
	String RPassWord;
	
	private JFrame frame;
	private JButton update;
	private JButton cancel;
	private JPasswordField newpw;
	private JPasswordField repeatpw;
	private JLabel Message;
	private ResultSet result;
	
    Statement stmt;
    PreparedStatement pst;
	PreparedStatement pst2;
	
	public PWChangeGUI(String name) {
		// TODO Auto-generated constructor stub
		activename=name;
		
		update=new JButton("Submit");
		cancel=new JButton("Cancel");
		newpw = new JPasswordField(6);
		repeatpw= new JPasswordField(6);
		//actual could be shorter than 100
		
		Message=new JLabel("Input your new Password and confirm");
		// The color and content can be 
		
		update.addActionListener(this);
		cancel.addActionListener(this);
		
		frame = new JFrame("Password Info Change");
		
		JPanel center=new JPanel(new GridLayout(4,2));
		center.add(new JLabel("Username:"));
		center.add(new JLabel(activename));
		center.add(new JLabel("New Password:"));
		center.add(newpw);
		center.add(new JLabel("Confirm:"));
		center.add(repeatpw);
		
		JPanel bottom=new JPanel(new FlowLayout());
		bottom.add(update);
		bottom.add(cancel);
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		/* to return to previous page when closing*/
		frame.addWindowListener( new WindowAdapter() {
	           @Override
	           public void windowClosing(WindowEvent we) {
	        	   new UserEntryGUI(activename);
	   			   frame.dispose();
	               //System.exit(0);               
	           }
	       } );
		
		frame.setLayout(new BorderLayout());
		frame.add(center, BorderLayout.CENTER);
		frame.add(Message, BorderLayout.NORTH);
		frame.add(bottom, BorderLayout.SOUTH);
		frame.setSize(new Dimension(300,180));
		frame.setLocation(width/2-200, height/2-150);
		frame.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try {
			Class.forName("com.mysql.jdbc.Driver");
        	System.out.println(" Drive SQL accomplished");
			conn = DriverManager.getConnection(url);
			sql="select username, password from account where username = ?";
			pst=conn.prepareStatement(sql);
			pst.setString(1, activename);
			result=pst.executeQuery();
			//assume only one email for one person
			//hence one result only
			
			if (result.next()){
			OPassWord = result.getString(2);
			pst.close();
			}
			else
				System.out.println("NO finding!");
			// set to general utf8
			
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
						
		if(e.getSource()==update){
			TPassWord= new String(newpw.getPassword());
			RPassWord= new String(repeatpw.getPassword());
			
			if (!TPassWord.equals(RPassWord)){
				Message.setText("Confirm mismatch!");
				Message.setForeground(Color.red);
			}
			else{
				//Update in database and return to UserEntry GUI
				sql="update account set password = ?, status =? where username = ?";
				try {
					pst2=conn.prepareStatement(sql);
					pst2.setString(1, TPassWord);
					pst2.setString(2, "checking");
					// 'undercheck right?'
					pst2.setString(3, activename);						
					pst2.executeUpdate();						
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}finally{
					try {
						conn.close();
						pst2.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				//return to UserGUI
				new UserEntryGUI(activename);
				frame.dispose();
				//close present	
		}
		}
		else if(e.getSource()==cancel){
			new UserEntryGUI(activename);
			frame.dispose();
		}
		else;
	}

}
