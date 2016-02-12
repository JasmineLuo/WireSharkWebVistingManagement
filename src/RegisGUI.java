import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;


public class RegisGUI extends Setplace implements ActionListener, ItemListener {
   public static void main(String[] args){
	   new RegisGUI();
   }
   
   private JFrame frame;
   JTextField username;
   JPasswordField password1;
   JPasswordField password2;
   JTextField email;
   private JButton Register;
   private JButton Cancel;
   private JButton Check;
   private int devicenum;
   Choice device;
   
   String sql;
   
   
   public RegisGUI(){
	   password1 = new JPasswordField(6);
	   password2 = new JPasswordField(6);
	   email = new JTextField(6);
	   username  = new JTextField(6);
	   
	   JLabel Loginlabel = new JLabel("Please Input Your First as Username and Password");
	   
	   device = new Choice();
	   device.add("1");
	   device.add("2");
	   device.add("3");
	   device.addItemListener(this);
	   
	   Register = new JButton("Register");
	   Cancel = new JButton("Cancel");
	   Cancel.setBackground(Color.GRAY);
	   Cancel.setOpaque(true);
	   Cancel.setBorderPainted(false);
	   Check =  new JButton("Check");
	   
	   Register.addActionListener(this);
	   Cancel.addActionListener(this);
	   Check.addActionListener(this);
	   
	   JPanel center = new JPanel(new GridLayout(5,2));
	   center.add(new JLabel("Username: "));
	   center.add(username);
	   center.add(new JLabel("Email: "));
	   center.add(email);
	   center.add(new JLabel("Num of Devices:"));
	   center.add(device);
	   center.add(new JLabel("Password: "));
	   center.add(password1);
	   center.add(new JLabel("Reenter Password: "));
	   center.add(password2);
	   
	   JPanel south = new JPanel(new FlowLayout());
	   south.add(Register);
	   south.add(Check);
	   south.add(Cancel);
	   
	   frame = new JFrame("Hot Spot Registration");
	   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   frame.setLayout(new BorderLayout());
	   frame.add(center, BorderLayout.CENTER);
	   frame.add(Loginlabel, BorderLayout.NORTH);
	   frame.add(south, BorderLayout.SOUTH);
	   frame.setSize(new Dimension(300,180));
	   frame.setLocation(width/2-200, height/2-150);
	   frame.setVisible(true);
   }


public void actionPerformed(ActionEvent e) {  // React for clicking on the Login Button
	Connection conn = null;
	String name = username.getText();
	String mail = email.getText();
	String pass1 = new String(password1.getPassword());
	String pass2 = new String(password2.getPassword());

	if (e.getSource()==Register){

		if(name.equals("")||pass1.equals("")||pass2.equals("")||mail.equals("")){
			JOptionPane.showMessageDialog(null, "Please Enter the Correct Username, Email and Password");
		}
		else if(pass1.equals(pass2)==false){
			JOptionPane.showMessageDialog(null, "Two Passwords Do Not Match. Please Re-enter");
		}
		else {
		String url = "jdbc:mysql://localhost:3306/dbms?"+"user=root&useUnicode=true&characterEncoding=UTF8";// database address
		
        try{
        	Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver	
        	conn = DriverManager.getConnection(url); // Establish the connection       	
        	Statement stmt = conn.createStatement();// Declare stmt to update and query data from database.
        	
        	///
        	sql="select * from account where username = '"+name+"' ";
        	ResultSet result5 = stmt.executeQuery(sql);
        	///
        	if (!result5.next()){
        		int j = 999;
        		for(int i=0;i<devicenum;i++){
	        	sql = "insert into account(id,username,password,email) values('"+j+"','"+name+"','"+pass1+"','"+mail+"')"; // insert the username and password of application into account table
	        	stmt.executeUpdate(sql);
	        	j--;
        		}
	        	JOptionPane.showMessageDialog(null, "Application Submitted. Please Check later for Registration Status");
        	}        	
        	else{
	        	JOptionPane.showMessageDialog(null, "This Username is already taken. Please try another");
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
	else if(e.getSource()==Check){
		if(name.equals("")||pass1.equals("")||pass2.equals("")||mail.equals("")){
			JOptionPane.showMessageDialog(null, "Please Enter the Correct Username, Email and Password");
		}
		else{
        String url = "jdbc:mysql://localhost:3306/dbms?"+"user=root&useUnicode=true&characterEncoding=UTF8";// database address
		
        try{
        	Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver	
        	conn = DriverManager.getConnection(url); // Establish the connection       	
        	Statement stmt = conn.createStatement();// Declare stmt to update and query data from database.
        	
        	sql = "select account.username, account.password, account.status from account";
            ResultSet rs = stmt.executeQuery(sql); // ִ
            
        	while (rs.next()) {
        	   if(rs.getString(1).equals(name)&&rs.getString(2).equals(pass1)){
                if(rs.getString(3).equals("approved")){
                	JOptionPane.showMessageDialog(null, "Your Application Has been Approved");
                	frame.dispose();
                	new LoginGUI();	
                }
                else{
                	JOptionPane.showMessageDialog(null, "Please Wait for Application Checking");
                }
            	conn.close();
            	break;
        	   }
        	   continue;
            }
        	
        } catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
        finally{
        	
        }
	}
	}
	else if (e.getSource()==Cancel){
		System.exit(0);
	}
	else {}
}


@Override
public void itemStateChanged(ItemEvent e) {
	// TODO Auto-generated method stub
	
	if(device.getSelectedItem().equals("1")){
		devicenum=1;
	}
	else if(device.getSelectedItem().equals("2")){
		devicenum=2;
	}
	else{
		devicenum=3;
	}
}
}

