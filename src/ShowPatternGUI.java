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

public class ShowPatternGUI extends Setplace implements ActionListener{

	public static void main( String[] args ){
		new ShowPatternGUI("ZhongyiLuo");
		// NOTICE: need to have the name of current active acount
	}
	
	private JButton visible;
	private JButton hide;
	private JButton know;
	private String activename;
	private DefaultTableModel table;
	private JTable jtable;
	
	Object[][] timeactivity;
	Object[][] websiteactivity;
	
	ResultSet result;	
	ResultSet result2;	
	String url = "jdbc:mysql://localhost:3306/dbms?"+"user=root&useUnicode=true&characterEncoding=UTF8";
	String sql;
	Connection conn= null;
	PreparedStatement pst = null;
	
	private double[] rank;
	
	private String[] column={"Website", "frequncy"};
	
	public ShowPatternGUI (String name){
		
		activename=name;
		int rows1=0;
		int rows2=0;
		int i=0;
		
		try {
			
			conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement();
			sql="set names 'utf8mb4'";
			//System.out.println("Encode are set to UTF8MB4");
			stmt.executeQuery(sql);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sql="select dstName, count(*) from working group by dstName order by count(*)";			
		try {
			
			pst=conn.prepareStatement(sql);
	    	result=pst.executeQuery();
	    	
	    	if (result.getRow()>7)
	    		rows1=result.getRow();
	    	else
	    		JOptionPane.showMessageDialog(null, "You currently have no data to show.");
	    	
	    	while(result.next() && i<rows1){ 
	    		websiteactivity[i][0]=result.getString(1);
	    		websiteactivity[i][1]=result.getString(2);
        		// time, uname, srcIP, dstName and device will be shown
        		i++;
        		//notice that due to the feature is char value 
        	}    	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// select website vsiting history
			
			i=0;
			int sum=0;
			while(i<rows1){
				sum=sum+Integer.parseInt((String) websiteactivity[i][1]);
			}
		
			for(i=0;i<8;i++){
				rank[i]=Integer.parseInt((String) websiteactivity[i][1])/sum;
			}
			// find out the feature of activity
			
			sql="select * from userfeature where username = ?";
			try {
				pst=conn.prepareStatement(sql);
				result=pst.executeQuery();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
		
    	table= new DefaultTableModel(websiteactivity, column);
    	jtable = new JTable(table);
    	JScrollPane scrollPane = new JScrollPane(jtable);
    	jtable.setFillsViewportHeight(true);
    	// slide
    	/*show result*/
    	JPanel center = new JPanel(new GridLayout(1,100)); // gridlayout for query results
    													// how 2 arrange them into rows and slide?		   
 		center.add(scrollPane);
 		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
