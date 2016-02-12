import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.PreparedStatement;

import java.awt.event.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

public class EntryGUI extends Setplace implements ActionListener{//, ItemListener{
	
	   private JFrame frame;
	   private JButton Exit;
	   private JButton Search;
	   private JButton Delete;
	   private JButton List;
	   JCheckBox[] check;
       Connection conn = null;
       ResultSet rs;
       int x;
       int rows;
       int userrows;
       String sql;
       String[] content;
       String[] no;
       
	   String url = "jdbc:mysql://localhost:3306/dbms?"+"user=root&useUnicode=true&characterEncoding=UTF8";
	
	   ///
		private DefaultTableModel table;
		private JTable jtable;
		private String[] Tablecolum={"no", "id", "Visit Time", "Username", "Source IP","master IP", "protocol", "Website",
				"device","source mac","master mac","Edit"};
		Object[][] tablecontent;
		int count=0;
		ResultSet rst;
		private java.sql.PreparedStatement pst = null;
	   ///
	public EntryGUI(int box){
		
		rows = box;
		   
		 try{
	        	//Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver
	        	conn = DriverManager.getConnection(url);
	        	Statement stmt = conn.createStatement();
	        	
	        	content = new String[box];
	        	no = new String[box];
	        	
	        	sql = "select * from entry";
	        	rs = stmt.executeQuery(sql); 
	        	/**
	        	 * Store the current Account info into Administrator GUI
	        	 */
	        	
	        	///
	        	tablecontent= new Object[box][12];
	        	
	        	while(rs.next()){
	        	content[x]=	rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+" "+rs.getString(5)+" "+rs.getString(6)+" "+rs.getString(7)+" "+rs.getString(8)+" "+rs.getString(9)+" "+rs.getString(10)+" "+rs.getString(11);
	        	//System.out.println(content[x]);
	        	no[x]= rs.getString(1);	        	
	        	tablecontent[x][0]=rs.getString(1);
        		tablecontent[x][1]=rs.getString(2);
        		tablecontent[x][2]=rs.getString(3);
        		tablecontent[x][3]=rs.getString(4);
        		tablecontent[x][4]=rs.getString(5);
        		tablecontent[x][5]=rs.getString(6);
        		tablecontent[x][6]=rs.getString(7);
        		tablecontent[x][7]=rs.getString(8);
        		tablecontent[x][8]=rs.getString(9);
        		tablecontent[x][9]=rs.getString(10);
        		tablecontent[x][10]=rs.getString(11);
        		tablecontent[x][11]=false;
	        	///
	        	x++;
	        	}
	        	
	        	///
	        	///jtable= new JTable(tablecontent, Tablecolum);
	        	///jtable.setModel(table);
	        	table = new DefaultTableModel(tablecontent, Tablecolum);
	            
	        	jtable = new JTable(table) {

	                private static final long serialVersionUID = 1L;
	                /*@Override
	                public Class getColumnClass(int column) {
	                return getValueAt(0, column).getClass();
	                }*/
	                /*public Object getValueAt(int rowIndex) {
	                    return tablecontent[rowIndex];
	                }*/

	                @Override
	                public Class getColumnClass(int column) {
	                    switch (column) {
	                        case 0:
	                            return String.class;
	                        case 1:
	                            return String.class;
	                        case 2:
	                            return String.class;
	                        case 3:
	                            return String.class;
	                        case 4:
	                            return String.class;
	                        case 5:
	                            return String.class;
	                        case 6:
	                            return String.class;
	                        case 7:
	                            return String.class;
	                        case 8:
	                            return String.class;
	                        case 9:
	                            return String.class;
	                        case 10:
	                            return String.class;
	                        default:
	                            return Boolean.class;
	                    }
	                }
	            };
	            jtable.setPreferredScrollableViewportSize(jtable.getPreferredSize());
	            ///JScrollPane scrollPane = new JScrollPane(jtable);
	            //getContentPane().add(scrollPane);
	        	///
	        	
	        }catch (SQLException e1){
	        	System.out.println("MySQL Operation Error");
	        	e1.printStackTrace();
	        }finally{
	        	//conn.close();
	        }
    
		   check = new JCheckBox[box] ;
		   for(int i=0;i<box;i++){
			   check[i] = new JCheckBox(content[i]);
		   }
		   JLabel Loginlabel = new JLabel("Check Access Entry");
		   
		  
		  Exit = new JButton("Exit");
		  Delete = new JButton("Delete");
          Search = new JButton("Advanced Search");
          List = new JButton("Hotspot Users");
		   
		 
		   Exit.addActionListener(this);
		   Delete.addActionListener(this);
		   Search.addActionListener(this);
		   List.addActionListener(this);
		   
		   ///JPanel center = new JPanel(new GridLayout(box,1));
		    
		   //for(int j=0;j<box;j++)
		   //center.add(check[j]);
		   JScrollPane scrollPane = new JScrollPane(jtable);
		   
		   JPanel south = new JPanel(new FlowLayout());
		   south.add(Search);
		   south.add(Delete);
		   south.add(Exit);
		   south.add(List);
		   
		   frame = new JFrame("Access Entry Checking");
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
		   frame.setSize(new Dimension(300,180));
		   frame.setLocation(width/2-200, height/2-150);
		   frame.pack();
		   frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {

		
		if(e.getSource()==Delete){
	        try{
	        	Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver	
	        	conn = DriverManager.getConnection(url); // Establish the connection       	
	        	Statement stmt = conn.createStatement();
	        	for(int i=0;i<rows;i++){
	        		//if(check[i].isSelected()){
	        		if((boolean) jtable.getValueAt(i,11)){
	        			sql = "delete from entry where no= '"+no[i]+"'";
	        			System.out.println(i+""+"row is checked!");
	        			stmt.executeUpdate(sql);
	        		}
	        	}
	        	JOptionPane.showMessageDialog(null, "You Have Deleted the Selected Entries!");
	        	
	        } catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        finally{}
		}
		else if(e.getSource()==List){ // HotspotUser
			try{
	        	Class.forName("com.mysql.jdbc.Driver"); // Dynamic Load Mysql Driver	
	        	conn = DriverManager.getConnection(url); // Establish the connection       	
	        	Statement stmt = conn.createStatement();
	        	userrows = stmt.executeUpdate("insert into hotspotuser(name,device,mac,entrynum) select uname, device, srcmac, count(*) from entry group by id");
		  } catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		  finally{}
		new HotSpotUserGUI(userrows,rows);
		userrows=0;
	}
		else if(e.getSource()==Search){
			new AdvanceSearchGUI(rows);
			
		}
		
		else if(e.getSource()==Exit){
			
			///
			String sql="delete from hotspotuser";
			try {
				pst=conn.prepareStatement(sql);
				pst.executeUpdate();	
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
			///
			frame.dispose();
		}
	}


}
