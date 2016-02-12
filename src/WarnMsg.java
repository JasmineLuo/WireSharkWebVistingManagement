
import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;

//import java.util.Properties;
//import javax.mail.*;
import javax.mail.internet.*;

public class WarnMsg extends Setplace implements ActionListener, ItemListener {
	 private JFrame frame;
	   JTextField Email;
	   JTextField warnmsg;
	   private JButton Confirm;
	   private JButton Cancel;
	   
	   Connection conn = null;
       ResultSet rs;
       String[] email;
       String[] name;
	   
	   Choice ch;
	   int x=0;
	   int y;

	   
	   
	   String sql;
	   String url = "jdbc:mysql://localhost:3306/dbms?"+"user=root&useUnicode=true&characterEncoding=UTF8";
	   
	   public WarnMsg(int rows){
		   y=rows;
		   try{
	        	//Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver
	        	conn = DriverManager.getConnection(url);
	        	Statement stmt = conn.createStatement();
	        	
	        	email = new String[rows];
	        	name = new String[rows];
	        	
	        	sql = "select * from account";
	        	rs= stmt.executeQuery(sql); 
	        	/**
	        	 * Store the current Account info into Administrator GUI
	        	 */
	        	while(rs.next()){
	        	email[x]= rs.getString(5);
	        	name[x]= rs.getString(3);
	        	x++;
	        	}
		   }catch (SQLException e1){
		        	System.out.println("MySQL Operation Error");
		        	e1.printStackTrace();
		        }finally{
		        	//conn.close();
		        }
		   
		   Email = new JTextField(6);
		   warnmsg = new JTextField(6);
		   ch = new Choice();
		   ch.add("Select User");
		   for(int i=0; i<rows;i++){		  
			   ch.add(name[i]);
			   ch.addItemListener(this);
		   }
		  
		   
		   JLabel Confirmlabel = new JLabel("Choose User You Wanna Warn");
		   
		   Confirm = new JButton("Confirm");
		   Cancel = new JButton("Cancel");
		   
		   Confirm.addActionListener(this);
		   Cancel.addActionListener(this);
		   
		   
		   
		   JPanel center = new JPanel(new GridLayout(3,2));
		   center.add(new JLabel("username: "));
		   for(int i=0;i<rows;i++)
		   center.add(ch);
		   center.add(new JLabel("Email: "));
		   center.add(Email);
		   center.add(new JLabel("Violation Warning: "));
		   center.add(warnmsg);
		   
		   JPanel south = new JPanel(new FlowLayout());
		   south.add(Confirm);
		   south.add(Cancel);
		   
		   frame = new JFrame("Network DBMS Confirm");
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
		   frame.add(center, BorderLayout.CENTER);
		   frame.add(Confirmlabel, BorderLayout.NORTH);
		   frame.add(south, BorderLayout.SOUTH);
		   frame.setSize(new Dimension(300,180));
		   frame.setLocation(width/2-200, height/2-150);
		   frame.setVisible(true);
	   }

	   @Override
	   public void itemStateChanged(ItemEvent e) {
		for(int i=0;i<y;i++){
	   	if(ch.getSelectedItem().equals(name[i])){
	   		Email.setText(email[i]);
	   	}
		}
	   }
	   
	@Override
	public void actionPerformed(ActionEvent e) {  // React for clicking on the Confirm Button

        String content = warnmsg.getText();
        String address = Email.getText();
        String host = "smtp.163.com";
        String from = "luoxuanmingren1@163.com";
        String to = address;
        
		if (e.getSource()==Confirm){
			Properties props = System.getProperties();
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.auth", "true");
			MyAuthenticator myauth = new MyAuthenticator("luoxuanmingren1@163.com", "Woaichutian123");
			Session session = Session.getDefaultInstance(props, myauth);
			//session.setDebug(true);
			MimeMessage message = new MimeMessage(session);
			
			try {
				message.setFrom(new InternetAddress(from));
				message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
				message.setSubject("Hot Spot Warning Message--Important!");
				message.setText(content);
				message.saveChanges();

			    Transport.send(message);
			    JOptionPane.showMessageDialog(null, "Warning Message Sent Successfully!");
			} catch (AddressException e1) {
				e1.printStackTrace();
			} catch (MessagingException e1) {
				e1.printStackTrace();
			}	
	        }

		else if (e.getSource()==Cancel){
			frame.dispose();
		}
		else {}
	}

	}
