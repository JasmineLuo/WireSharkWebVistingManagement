import java.io.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import com.mysql.jdbc.DatabaseMetaData;
import java.sql.* ; 

public class UpdateWebPack {
    
	/*public static void main(String[] args ){
		new UpdateWebPack("/Users/luozhongyi/Desktop/capture12.txt");
	}
	*/
	PreparedStatement pst;
	DatabaseMetaData dbm;
	ResultSet tables;
	Statement stmt;
	
	Webpack[] Read1 = null;			
	// For sql
	Connection conn = null;
    String sql;
    int result;
    ResultSet result2;
    ResultSet result3;
    ResultSet result4;
    int s1;
    int i;
    int index;
    int lastindex;
    int index2;
    int countnum=0;
    String s2, s3, s4, s5, s6, s7, s8, s9, s10, s11; //s11 is for the id construction
	String[] s12;
    String ps7;
    String url= "jdbc:mysql://localhost:3306/dbms?"+"user=root&useUnicode=true&characterEncoding=UTF8"; // Need to be initialized        																									// past is UTF8
    String[] postfix={""};
    StringBuffer reccordedip= new StringBuffer(); // if not reccorded then reject the update
	
	public UpdateWebPack(String address){
	//public static void main(String[] args) {
		// TODO Auto-generated method stub
		new WireSharkTxt();               
        try {
			Read1 = WireSharkTxt.ReadSet(address);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			// import files
		                      
        try {			
        	//Drive MySQL 
        	Class.forName("com.mysql.jdbc.Driver");
        	System.out.println(" Drive SQL accomplished");
        	conn = DriverManager.getConnection(url);
        	stmt = conn.createStatement();
        	
			sql="set names 'utf8mb4'";
			stmt.executeQuery(sql);
        	sql="select * from entry where no=1 ";//if table is empty
        	result2=stmt.executeQuery(sql);
        	
			if (result2.next()) {
        		sql="select max(no) from entry";
        		stmt.executeQuery(sql);
        		result2=stmt.executeQuery(sql);
        		result2.next();
        		index=Integer.parseInt(result2.getString(1));
        		lastindex=index;
        		System.out.println(index+"");
        		}
        	else{
        		index=0;
        		lastindex=index;
        	}
        	result2.close();
        	index+=1;
        	// insert every new tuple in a for loop:

			dbm=(DatabaseMetaData) conn.getMetaData();    			
			tables= dbm.getTables( null, null, "temp", null);
          	
			if (!tables.next()){
			sql="create table temp like entry";// create temporary table
			pst=conn.prepareStatement(sql);
			pst.executeUpdate();
          	}
			
			/****/
			sql="select id from account";
			pst=conn.prepareStatement(sql);
			result4=pst.executeQuery();
			
			while(result4.next()){
				reccordedip.append(result4.getString(1));
				//System.out.println(result4.getString(1));
				reccordedip.append(" ");	
			}
			
			System.out.println(reccordedip.toString());
			//**** assuming the mac address of wireshark implemented mac also exsist in account table*/
			
        	
        	for (int m=0;m<=(Read1[0].notify-1);m++){
    			
        		//System.out.print(Read1[m]);
    			//s1=Read1[m].SequenceNumber;
        		s1=index;
    			s2=Read1[m].Time;
    			s3="Default";
    			s4=Read1[m].Source;
    			s5=Read1[m].Destination;
    			s6=Read1[m].Protocol;
    			s7=Read1[m].Info;
    			/****/
    			//System.out.println(s7);
    			/****/
    			//s8=Read1[m].Device;
    			//s9=Read1[m].MacAdd;
    			s10=Read1[m].DstAdd;
    			/****/
    			//System.out.println(s4);
    			index2=s4.lastIndexOf('.');
    			s11=s4.substring(index2+1);
    			// System.out.println(s11);
    			// the last part is id
    			//also need to filter out the returning packages by making sure the inserted mac already exsist
    			
    			sql="select * from temp where id=? and time=? and dstIP = ?";//skip duplicated tuples
    			pst=conn.prepareStatement(sql);
    			pst.setString(1, s11);
    			pst.setString(2, s2);
    			pst.setString(3, s5);// assuming these are the key of entry table
    			result2=pst.executeQuery();
    			             	
    			if (!result2.next() && s7.contains("www.") && s7.length()<400 && reccordedip.toString().contains(s11)){ 
    				// tuple is new
    				// info contains www
    				// info length <200
    				// mac is recorded, otherwise can be a returning package from destinaion
    				ps7=editinfo(s7);
	    			if (s7.length() <= 200 && !ps7.equals("Default")) {	    		   
	    			pst=conn.prepareStatement(" insert into temp values (?,?,?,?,?,?,?,?,?,?,?)");
	    			pst.setString(1, s1+"");
	    			pst.setString(2, s11);
	    			// id is in the second column
	    			pst.setString(3, s2);
	    			pst.setString(4, s3);
	    			pst.setString(5, s4);
	    			pst.setString(6, s5);
	    			pst.setString(7, s6);
	    			//need to process on s7 to get meaningful result	    			
	    			//here format apart from www is not considered
	    			pst.setString(8, ps7);
	    			pst.setString(9, "DEFAULT");
	    			pst.setString(10, "XX:XX:XX:XX");
	    			pst.setString(11, s10);	    			
	    			result=pst.executeUpdate();
	    			//result = stmt.executeUpdate(sql);
	                if(result!=-1){
	                	System.out.println(" Tuple ["+ m +""+ "] is inserted into temp table.");
	                	index+=1;
	                }
	                else;	                
	                }
	    			else; // delete the tuple with info length >=200	        	
	        	}
    			else; //skip insertion if duplicated        	
        	}// loop for m       	
        	/* Join to renew the final table*/
        	if (!result2.next()){
        	sql="insert into entry (no, id, time, uname, srcIP, dstIP,"
        			+ " protocol, dstName, device, srcmac, dstmac)"
        			+ " select no, account.id, time, account.username, srcIP, dstIP, "
        			+ " protocol, dstName, account.device, account.mac, dstmac"
        			+ " from temp, account"
        			+ " where temp.id = account.id";//
        	//NOTICE, no longer decide username by src account but by id
            pst=conn.prepareStatement(sql);
            result=pst.executeUpdate();
           
            sql="drop table temp";
            //NOTICE if broke in the middle, temp table won't be dropped
            pst=conn.prepareStatement(sql);
            result=pst.executeUpdate();
        	}
        	else;
        	// operation when content is duplicated
        	} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();				
		}                 
        try {
			conn.close();
			pst.close();			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // close connection		
	}

	private String editinfo(String s72) {
		int firstw = 0;
		int lastbench = 0;
		int dotcount=0;
		String temp=null;
		String current=s72;
		Boolean flag1 = false;
		Boolean flag2 = false;
		int i;
		while(!flag1 && current.length()>0){
			firstw=current.indexOf('w');
			if (current.charAt(firstw+1)=='w' && current.charAt(firstw+2)=='w'){
				flag1=true;
			}
			else{
				current=current.substring(firstw+1);
				//cut and search again
			}
		}		
		if(current.length()>0){
			//get the desired section
			i=firstw;
			while(i<current.length() && !flag2){
				if(Character.isLetter(current.charAt(i))  || 
						Character.isDigit(current.charAt(i)) || current.charAt(i) == '-' 
						|| current.charAt(i) == '.'){
					if(current.charAt(i) == '.'){
						dotcount++;
					} // just to avoid www.XXX/YYY format
					i++;
				}
				else{ 
					flag2=true;
					lastbench=i;
				}
			}
			//finally get the desired section
			if(dotcount>=2){
				if(flag2==true){
					//last index is found
					temp=current.substring(firstw, lastbench);
				}
				else{
					//the domain name is the last part of info
					temp=current.substring(firstw);
				}
				//get appropriate value for domain name
			}
			else{
				//do nothing in case of www.XXX/YYY format
				temp="Default";
			}
		}		
		else{
			System.out.println("finding domain name error!");
		}
		//for default 
		System.out.println(temp);
		return temp; //temp would be null if not 
	}
}
