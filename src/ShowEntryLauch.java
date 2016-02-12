import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
//import com.mysql.jdbc.DatabaseMetaData;
import java.awt.event.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Connection;

public class ShowEntryLauch extends Setplace implements ActionListener{
	
	public static void main( String[] args ){
		new ShowEntryLauch( "ZhongyiLuo",4,"Apple","baidu","1900", "1900",30);
		// activename, device, dst, time
		// NOTICE: need to have the name of current active acount
	}
	
	Connection conn= null;
	PreparedStatement pst = null;
	Statement stmt;
	String sql;
	Object[][] content;
	ResultSet result;	
	ResultSet result2;	
	ResultSet result3;
	String url = "jdbc:mysql://localhost:3306/dbms?"+"user=root&useUnicode=true&characterEncoding=UTF8";
	
	ArrayList<String> Temp1;
	ArrayList<String> Temp2;
	ArrayList<String> Temp3;
	ArrayList<String> Temp4;
	ArrayList<String> Temp5;
	ArrayList<String> Temp6;
	
	//private int mode; //to specify different cases of search
			  // 000 - 0 no specified search
			  // 001 - 1 time search
			  // 010 - 2 destname search
			  // 011 - 3 destname + time
			  // 100 - 4 device search
	  		  // 101 - 5 device + time
	          // 110 - 6 device + dst
	          // 111 - 7 device + dst + time
	
	//variable related to GUI
	private JButton Return1;
	private JButton Return2;
	private JFrame frame1;
	private JFrame frame2;
	private String act;
	private DefaultTableModel table;
	private String[] Tablecolum={"no","Visit Time", "Username", "Source IP", "Domain Name","Device" };
	private JTable jtable;
	protected int numx;
	
	public ShowEntryLauch( String activename, int mode, String device, String destname, String start, String end , int x){
		
		act = activename;
		numx=x;
		
		try {
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();
			sql="set names 'utf8mb4'";
			System.out.println("Encode are set to UTF8MB4");
			stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
						
		//search related entry
		switch (mode) {
		
		case 0:{ 
			sql="select no, time, uname, srcIP, dstName, device from working"
				+ " where uname = ?";
				
				try {
					pst=conn.prepareStatement(sql);
					pst.setString(1, activename);
					result3=pst.executeQuery();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}break;
				
		case 1:{
			sql="select no, time, uname, srcIP, dstName, device from working"
					+ " where time between ? and ?";					
					try {
						pst=conn.prepareStatement(sql);
						pst.setString(1, start);
						pst.setString(2, end);
						result3=pst.executeQuery();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}break;
		case 2:{
			sql="select no, time, uname, srcIP, dstName, device from working"
					+ " where dstName like ?";					
					try {
						pst=conn.prepareStatement(sql);
						pst.setString(1, "%"+destname+"%");
						result3=pst.executeQuery();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}break;
		case 3:{
			sql="select no, time, uname, srcIP, dstName, device from working"
					+ " where dstName like ? and time between ? and ? ";					
					try {
						pst=conn.prepareStatement(sql);
						pst.setString(1, "%"+destname+"%");
						pst.setString(2, start);
						pst.setString(3, end);
						result3=pst.executeQuery();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}break;
		case 4:{
			sql="select no, time, uname, srcIP, dstName, device from working"
					+ " where device = ?";					
					try {
						pst=conn.prepareStatement(sql);
						pst.setString(1, device);
						result3=pst.executeQuery();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}break;
		case 5:{
			sql="select no, time, uname, srcIP, dstName, device from working"
					+ " where device = ? and time between ? and ? ";					
					try {
						pst=conn.prepareStatement(sql);
						pst.setString(1, device);
						pst.setString(2, start);
						pst.setString(3, end);
						result3=pst.executeQuery();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}break;
		case 6:{
			sql="select no, time, uname, srcIP, dstName, device from working"
					+ " where device = ? and dstName like ?";					
					try {
						pst=conn.prepareStatement(sql);
						pst.setString(1, device);
						pst.setString(2, "%"+destname+"%");
						result3=pst.executeQuery();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}break;
		case 7:{
			sql="select no, time, uname, srcIP, dstName, device from working"
					+ " where device = ? and dstName like ? and time between ? and ?";					
					try {
						pst=conn.prepareStatement(sql);
						pst.setString(1, device);
						pst.setString(2, "%"+destname+"%");
						pst.setString(3, start);
						pst.setString(4, end);
						result3=pst.executeQuery();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}break;
		default:break;
		}
	
        	
        	Temp1=new ArrayList<String>();
        	Temp2=new ArrayList<String>();
        	Temp3=new ArrayList<String>();
        	Temp4=new ArrayList<String>();
        	Temp5=new ArrayList<String>();
        	Temp6=new ArrayList<String>();
        	
        	int num=0;
        	try {
				while(result3.next()){
					Temp1.add(result3.getString(1));
					Temp2.add(result3.getString(2));
					Temp3.add(result3.getString(3));
					Temp4.add(result3.getString(4));
					Temp5.add(result3.getString(5));
					Temp6.add(result3.getString(6));
					num++;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	try {
				result3.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	//close result3
        	if (num>0){
        		// more than 0 results are found;
				content = new String[num][6];
	        	for(int m=0;m<num;m++){ 
					content[m][0]=(String)Temp1.get(m);
					content[m][1]=(String)Temp2.get(m);
					content[m][2]=(String)Temp3.get(m);
					content[m][3]=(String)Temp4.get(m);
					content[m][4]=(String)Temp5.get(m);
					content[m][5]=(String)Temp6.get(m);
					// time, uname, srcIP, dstName and device will be shown
	        	}
					table= new DefaultTableModel(content, Tablecolum);
					jtable = new JTable(table);
		        	JScrollPane scrollPane = new JScrollPane(jtable);
		        	jtable.setFillsViewportHeight(true);
		        	// slide
		        	/*show result*/
		        	JPanel center = new JPanel(new GridLayout(1,100)); // gridlayout for query results
		        													// how 2 arrange them into rows and slide?		   
		     		center.add(scrollPane); // the total query result*/
					
				    JLabel Tolabel= new JLabel("Entry history according to search setting:");
		     		Return1 = new JButton("Return");
					Return1.addActionListener(this);
					JPanel bottom= new JPanel(new FlowLayout());
					bottom.add(Return1);
					
				   frame1 = new JFrame("Search Result");
				   /* to return to previous page when closing*/
	     		   frame1.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			     	  frame1.addWindowListener( new WindowAdapter() {
			   	           @Override
			   	           public void windowClosing(WindowEvent we) {
			   	        	   //new UserEntryGUI(activename);
			   				   new ShowEntryGUI(numx,act);
			   	   			   frame1.dispose();
			   	               //System.exit(0);               
			   	           }
			   	       } );	     		   
	     		   frame1.setLayout(new BorderLayout());
	     		   frame1.add(center, BorderLayout.CENTER);
	     		   frame1.add(Tolabel, BorderLayout.NORTH);
	     		   frame1.add(bottom, BorderLayout.SOUTH);
	     		   frame1.setSize(new Dimension(600,600));
	     		   frame1.setLocation(width/2-200, height/2-150);
	     		   frame1.pack();
	     		   frame1.setVisible(true);
        	}
        	else{
        		Return2 = new JButton("Return");
        		Return2.addActionListener(this);
        		
        		frame2= new JFrame("No finding");
        		JLabel message=new JLabel("Please reset your search.");
      		    
        		frame2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      		  /* to return to previous page when closing*/
			     	  frame2.addWindowListener( new WindowAdapter() {
			   	           @Override
			   	           public void windowClosing(WindowEvent we) {
			   	        	   //new UserEntryGUI(activename);
			   				   new ShowEntryGUI(numx,act);
			   	   			   frame2.dispose();
			   	               //System.exit(0);               
			   	           }
			   	       } );	 
			     	  
      		    frame2.setLayout(new BorderLayout());
      		    frame2.add(message, BorderLayout.CENTER);
      		    frame2.add(Return2, BorderLayout.SOUTH);
      		    frame2.setSize(new Dimension(600,600));
      		    frame2.setLocation(width/2-200, height/2-150);
      		    frame2.pack();
      		    frame2.setVisible(true);
        	}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==Return2){
			frame2.dispose();
			new ShowEntryGUI(numx,act);
		}
		else if (e.getSource()==Return1){
			frame1.dispose();
			new ShowEntryGUI(numx,act);
		}
		else;
	}
}
