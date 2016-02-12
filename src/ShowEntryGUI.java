import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.DatabaseMetaData;

import java.awt.event.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Connection;
//import java.sql.Date;

public class ShowEntryGUI extends Setplace implements ActionListener{
	public static void main( String[] args ){
		new ShowEntryGUI( 30,"ZhongyiLuo");
		// NOTICE: need to have the name of current active acount
	}
	
	private JButton search;
	private JButton Return;
	private JButton Show;
	// to show activity pattern in a new GUI
	
	private JFrame frame;
	//private JTable table;
	private DefaultTableModel table;
	private JTable jtable;
	// for device
	private JComboBox deviceselect;
	// for destname
	private JTextField keyword;
	// for date
	DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
	DateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
	JFormattedTextField sdateTextField;
	JFormattedTextField stimeTextField;
	JFormattedTextField edateTextField;
	JFormattedTextField etimeTextField;
	
	// few options for query when the total will be shown at the beginning
	private String activename;
	private String[] Tablecolum={"Visit Time", "Username", "Source IP", "Domain Name","Device" };
	ArrayList<String> AvailDev = new ArrayList<String>();
	// for detailed search

	
    private String[] Dev;
	// few restrictions on searching
	private String devcon;
	private String startcon;
	private String endcon;
	private String destcon;
	
	Object[][] content;
	Object[][] devcontent;
	ResultSet result;	
	ResultSet result2;	
	String url = "jdbc:mysql://localhost:3306/dbms?"+"user=root&useUnicode=true&characterEncoding=UTF8";
	String sql;
	Connection conn= null;
	PreparedStatement pst = null;
	private DatabaseMetaData dbm;
	
	private int mode; //to specify different cases of search
	  // 000 - 0 no specified search
	  // 001 - 1 time search
	  // 010 - 2 destname search
	  // 011 - 3 destname + time
	  // 100 - 4 device search
	  // 101 - 5 device + time
	  // 110 - 6 device + dst
	  // 111 - 7 device + dst + time
    private int mode1;
    private int mode10;
    private int mode100;
    private int num;
	
	public ShowEntryGUI (int x, String name){
		
		activename = name;
		num=x;
		
		int i=0;				
		try {
			conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement();
			sql="set names 'utf8mb4'";
			System.out.println("Encode are set to UTF8MB4");
			stmt.executeQuery(sql);
				
			content= new Object[x][5];
			
			sql="select time, uname, srcIP, dstName, device from entry where uname = ?";
			pst=conn.prepareStatement(sql);
        	pst.setString(1, activename);
        	result=pst.executeQuery();
        	
        	while(result.next() && i<x){ 
        		content[i][0]=result.getString(1);
        		content[i][1]=result.getString(2);
        		content[i][2]=result.getString(3);
        		content[i][3]=result.getString(4);
        		content[i][4]=result.getString(5);
        		// time, uname, srcIP, dstName and device will be shown
        		i++;
        	}
        	//System.out.println(i+" ");
        	
        	//create corresponding working table for convenience
        	dbm=(DatabaseMetaData) conn.getMetaData();
        	ResultSet tables= dbm.getTables( null, null, "working", null);
        	
        	if (!tables.next()){
        	
        	sql="create table working (no VARCHAR(45) not NULL, time VARCHAR(90), uname VARCHAR(45), srcIP VARCHAR(90), "+
        	"dstName VARCHAR(200), device VARCHAR(45), PRIMARY KEY(no))";
        	pst=conn.prepareStatement(sql);
        	pst.executeUpdate();
        	
        	//insert all related rows
        	sql="insert into working (no, time, uname, srcIP, dstName,device)"
        			+ "values (?,?,?,?,?,?)";
        	pst=conn.prepareStatement(sql);
        	for(int l=0;l<x;l++){
        		pst.setString(1, l+"");
        		pst.setString(2, (String) content[l][0]);
        		pst.setString(3, (String) content[l][1]);
        		pst.setString(4, (String) content[l][2]);
        		pst.setString(5, (String) content[l][3]);
        		pst.setString(6, (String) content[l][4]);
        		pst.addBatch();
        	}
        	pst.executeBatch();
        	}
        	else;      	
        	
        	//get related device for current user
        	sql="select device from account where username = ?";
        	pst=conn.prepareStatement(sql);
        	pst.setString(1, activename);        	
        	result2=pst.executeQuery();

        	String s="all";
        	AvailDev.add(s);
        	while(result2.next()){
        		AvailDev.add(result2.getString(1));
        		//System.out.println(result2.getString(1));
        	}
        	
         	//create temp table is done
        	table= new DefaultTableModel(content, Tablecolum);
        	
        	//initialize
        	search = new JButton("Search");
        	Return = new JButton("Return");	
        	Show = new JButton("Show my activity pattern");
        	keyword = new JTextField("all",10); 
        	DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        	DateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
        	
        	sdateTextField = new JFormattedTextField(dateformat);
        	sdateTextField.setValue( new java.sql.Date((dateformat.parse("1900-01-01").getTime())));
        	sdateTextField.setColumns(10);
        	/*sdateTextField.addPropertyChangeListener("value", this);*/
        	
        	stimeTextField = new JFormattedTextField(timeformat);
        	stimeTextField.setValue( new java.sql.Date((timeformat.parse("00:00:00").getTime())));
        	stimeTextField.setColumns(8);

        	edateTextField = new JFormattedTextField(dateformat);
        	edateTextField.setValue( new java.sql.Date((dateformat.parse("1900-01-01").getTime())));
        	edateTextField.setColumns(10);

        	etimeTextField = new JFormattedTextField(timeformat);
        	etimeTextField.setValue(new java.sql.Date((timeformat.parse("00:00:00").getTime())));
        	etimeTextField.setColumns(8);

            //get variables for display in bar  	
        	int size=AvailDev.size();
        	Dev = new String[size];
        	for(int i2=0;i2<size;i2++){
        		Dev[i2]=(String)AvailDev.get(i2);
        		//System.out.println(Dev[i2]);
        	}
        	//initialize deviceselect
        	deviceselect = new JComboBox(Dev);
        	deviceselect.setSelectedIndex(0);
        	deviceselect.addActionListener(this);
        	//device select is ready
        	
        	search.addActionListener(this);
        	deviceselect.addActionListener(this);
        	Return.addActionListener(this);
        	
        	//System.out.println(table.getRowCount()+"");
        	jtable = new JTable(table);
        	JScrollPane scrollPane = new JScrollPane(jtable);
        	jtable.setFillsViewportHeight(true);
        	// slide
        	/*show result*/
        	JPanel center = new JPanel(new GridLayout(1,100)); // gridlayout for query results
        													// how 2 arrange them into rows and slide?		   
     		center.add(scrollPane); // the total query result*/     		   
        	JLabel Tolabel = new JLabel("Total data");   
        	JPanel east = new JPanel(new GridLayout(11,1));
        	JPanel bottom = new JPanel(new FlowLayout());
     		east.add(new JLabel("Start Date:"));
     		east.add(sdateTextField);
     		east.add(new JLabel("End Date:"));
     		east.add(edateTextField);
     		east.add(new JLabel("Start Time:"));
     		east.add(stimeTextField);
     		east.add(new JLabel("End Time:"));
     		east.add(etimeTextField);
     		east.add(new JLabel("Keyword:(<10)"));
     		east.add(keyword);
     		east.add(deviceselect);
     		bottom.add(search);
     		bottom.add(Return);
     		bottom.add(Show);
     		   
     		   frame = new JFrame("Visit History");
     		   frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
     		   //add operation on table
     		   /* to avoid closing without droping*/     		   
     		  frame.addWindowListener( new WindowAdapter() {
     	           @Override
     	           public void windowClosing(WindowEvent we) {
     	               droptable();
     	   			   frame.dispose();
     	   			   new UserEntryGUI(activename);
     	               //System.exit(0);               
     	           }
     	       } );

     		   frame.setLayout(new BorderLayout());
     		   frame.add(center, BorderLayout.CENTER);
     		   frame.add(Tolabel, BorderLayout.NORTH);
     		   frame.add(east, BorderLayout.EAST);
     		   frame.add(bottom, BorderLayout.SOUTH);
     		   frame.setSize(new Dimension(600,600));
     		   frame.setLocation(width/2-200, height/2-150);
     		   frame.pack();
     		   frame.setVisible(true);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==search){
			startcon=sdateTextField.getText().concat(stimeTextField.getText());
			String START=startcon.replaceAll(" ", "");
			devcon=(String)deviceselect.getSelectedItem();
			String DEV=devcon.replaceAll(" ", "");
			endcon=edateTextField.getText().concat(etimeTextField.getText());
			String END=endcon.replaceAll(" ", "");
			destcon=keyword.getText();
			String DEST=destcon.replaceAll(" ", "");
			
			// determine search mode
			if (startcon.contains("1900") || endcon.contains("1900"))
				mode1=0;
			else mode1=1;
			
			if (devcon.equals("all"))
				mode100=0;
			else mode100=1;
			
			if (destcon.equals("all"))
				mode10=0;
			else mode10=1;
			
			mode=4*mode100+2*mode10+mode1;
			// mode determined
			
			System.out.println(mode+"");
			new ShowEntryLauch( activename,mode, DEV,DEST,START,END, num);
			frame.dispose();
			//launch a new search window 
			
			try {
				//pst.executeUpdate();
				conn.close();
				pst.close();
				//close current connection
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getSource()==Return){
			//new LoginGUI();
			//drop temp table
			sql="drop table working";
				try {
					pst=conn.prepareStatement(sql);
					pst.executeUpdate();
					conn.close();
					pst.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}			
			//close window	
			frame.dispose();
			new UserEntryGUI(activename);
		}
		else if(e.getSource()==Show){
			new ShowPatternGUI(activename);
		}
		else;
	}

	/*to avoid accident close without dropping*/
	private void droptable() {
		// TODO Auto-generated method stub
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url);
	    	//Statement stmt = conn.createStatement();	  
			
			dbm=(DatabaseMetaData) conn.getMetaData();
        	ResultSet tables1= dbm.getTables( null, null, "working", null);			
	    	if (!tables1.next()){
	        	sql="drop table working";
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
    	System.out.println(" working table are droped.");
		
	}
}
