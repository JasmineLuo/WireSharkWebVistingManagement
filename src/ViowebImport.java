// This need only to go through once, unless change in the list
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.Connection;

public class ViowebImport {

	public static void main( String args[]){
		new ViowebImport("/Users/luozhongyi/Desktop/RestrictedList.txt");
	}
	
	private String dir;
	private File file;
	private Scanner con;
	private String currentline;
	private int count;
	
	private String url = "jdbc:mysql://localhost:3306/dbms?"+"user=root&useUnicode=true&characterEncoding=UTF8";
	private String sql;
	private String sql1;
	private Connection conn= null;
	private PreparedStatement pst = null;
	private Statement stmt;
	//private ResultSet result;
	private ResultSet result3;
	//private ResultSet result4;
	private int result2;
	
	public ViowebImport(String filename){
		

		try {
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();
			sql="set names 'utf8mb4'";
			System.out.println("Encode are set to UTF8MB4");
			stmt.executeQuery(sql);
			
			sql1 = "select no from restrictedlist";
			ResultSet ss = stmt.executeQuery(sql1);
			count =0;
			while(ss.next()){
				
				count++;
			}
			//System.out.print(count);
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		dir = filename;
		file = new File(dir);
		
		try {
						
			con= new Scanner(file);					
			while(con.hasNextLine()){
				
				currentline=con.nextLine();
				
				sql="select * from restrictedlist where sitename = ?";
				pst=conn.prepareStatement(sql);
				pst.setString(1, currentline.toString());
				result3=pst.executeQuery();
				
				if (!result3.next()){
					sql="insert into restrictedlist (no, sitename) values (?,?) ";
					pst = conn.prepareStatement(sql);
					//System.out.println(count);
					pst.setString(1, count+"");
					pst.setString(2, currentline.toString());
					result2 = pst.executeUpdate();
					count++;
					// add to list if not exsist
				}
				else;
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//con.close();
		try {
			pst.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
