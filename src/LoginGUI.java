import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;


public class LoginGUI extends Setplace implements ActionListener, ItemListener {
   public static void main(String[] args){
    new LoginGUI();
   }
   
   private JFrame frame;
   JTextField username;
   JPasswordField password;
   private JButton Login;
   private JButton Cancel;
   private JButton Register;
   Choice ch;

   
   Boolean b = false;
   
   String sql;
   
   
   public LoginGUI(){
	   password = new JPasswordField(6);
	   username = new JTextField(6);
	   ch = new Choice();	   
	   ch.add("Select Your ID");
	   ch.add("Administrator");
	   ch.add("HotSpotUser");
	   
	 
	   
	   ch.addItemListener(this);
	   
	   
	   JLabel Loginlabel = new JLabel("Please Input Your Username and Password");
	   
	   Login = new JButton("Login");
	   Cancel = new JButton("Cancel");
	   Register = new JButton(" Create Account");
	   
	   Login.addActionListener(this);
	   Cancel.addActionListener(this);
	   Register.addActionListener(this);
	   
	   
	   
	   JPanel center = new JPanel(new GridLayout(3,3));
	   center.add(new JLabel("Account Type: "));
	   center.add(ch);
	   center.add(new JLabel("Username: "));
	   center.add(username);
	   center.add(new JLabel("Password: "));
	   center.add(password);
	   
	   JPanel south = new JPanel(new FlowLayout());
	   south.add(Login);
	   south.add(Cancel);
	   south.add(Register);
	   
	   frame = new JFrame("Network DBMS Login");
	   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   frame.setLayout(new BorderLayout());
	   frame.add(center, BorderLayout.CENTER);
	   frame.add(Loginlabel, BorderLayout.NORTH);
	   frame.add(south, BorderLayout.SOUTH);
	   frame.setSize(new Dimension(500,180));
	   frame.setLocation(width/2-200, height/2-150);
	   frame.setVisible(true);
   }

   @Override
   public void itemStateChanged(ItemEvent e) {
   	if(ch.getSelectedItem().equals("Administrator")){
   		b=true;
   	}
   	else{
   		b=false;
   	}
   }
   
@Override
public void actionPerformed(ActionEvent e) {  // React for clicking on the Login Button
	Connection conn = null;
	String name = username.getText();
	String pass = new String(password.getPassword());

	if (e.getSource()==Login){

		if(name.equals("")||pass.equals("")){
			JOptionPane.showMessageDialog(null, "Please Enter the Username and Password");
		}
		else {
		String url = "jdbc:mysql://localhost:3306/dbms?"+"user=root&useUnicode=true&characterEncoding=UTF8";// compare the database account and current input JDBC
		
        try{
        	Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver	
        	conn = DriverManager.getConnection(url); // Establish the connection       	
        	Statement stmt = conn.createStatement();// Declare stmt to update and query data from database.
        	
        	// Decide which account to query:  Administrator or User account
        	// Case for Administrator
        	if(b){
            sql = "select adaccount.username, adaccount.password from adaccount";
            ResultSet rs = stmt.executeQuery(sql); // 
            
        	while (rs.next()) {
        	   if(rs.getString(1).equals(name)&&rs.getString(2).equals(pass)){
               	new AdminGUI();
            	frame.dispose();
            	conn.close();
            	break;
        	   }
        	   else if(rs.getString(1).equals(name)&& rs.getString(2)!=pass){
        		JOptionPane.showMessageDialog(null, "Password Error. Please Try Again");
        		break;
        	   }
        	   continue;
            }
        	}
        	
        	// Case for User
        	else{
            sql = "select account.username, account.password, account.status from account";
            ResultSet rs = stmt.executeQuery(sql); // ִ��query��������浽rs��
            
        	while (rs.next()) {
        	   if(rs.getString(1).equals(name)&&rs.getString(2).equals(pass)&&rs.getString(3).equals("approved")){
               	new UserEntryGUI(name);
            	frame.dispose();
            	conn.close();
            	break;
        	   }
        	   else if(rs.getString(1).equals(name)&& rs.getString(2)!=pass){
        		JOptionPane.showMessageDialog(null, "Password Error. Please Try Again");
        		break;
        	   }
        	   continue;
            }
        	
        	}

        }catch (SQLException e1){
        	System.out.println("MySQL Operation Error");
        	e1.printStackTrace();
        }catch(Exception e2){
        	 e2.printStackTrace();
        }
        finally{
        	//conn.close();
        }
	}
	}// END of LOGIN Conditoin
	else if (e.getSource()==Cancel){
		System.exit(0);
	}
	else if (e.getSource()==Register){
		new RegisGUI();
    	frame.dispose();
    	//conn.close();
	}
	else {}
}

}

