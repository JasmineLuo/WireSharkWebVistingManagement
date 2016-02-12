import java.io.*;
import java.util.*;

public class WireSharkTxt {
		
	public WireSharkTxt(){
	}
	
	public static Webpack[] ReadSet( String s1) throws FileNotFoundException{
		
		int count;
		String[] list;
		StringBuffer temp;
		char ch;
		int stringnum;
		int i;
		
		//ArrayList<String> SequenceNumber = new ArrayList<String>();
		ArrayList<String> Time =new ArrayList<String>();
		ArrayList<String> Source =new ArrayList<String>();
		ArrayList<String> Destination =new ArrayList<String>();
		ArrayList<String> Protocol =new ArrayList<String>();
		ArrayList<String> Info =new ArrayList<String>();
		ArrayList<String> MacAdd =new ArrayList<String>();
		ArrayList<String> DstAdd =new ArrayList<String>();
		ArrayList<String> Device =new ArrayList<String>();
		
		File f = new File(s1);
		Scanner con = new Scanner(f);
		String currentline;
		
		count=0;
		while (con.hasNextLine()){
			currentline= con.nextLine();
			currentline= currentline.replaceAll("\\s+"," "); 
			while (currentline.length()<=1){
				currentline=con.nextLine();
				currentline= currentline.replaceAll("\\s+"," "); //delete duplicate " "
			}

			ch =currentline.charAt(0);
			if (ch==' ')
				currentline=currentline.replaceFirst(" ","");
			else; // delete first ""
			
		    ch = currentline.charAt(0);
		    
		    if (Character.isDigit(ch) && ch!='0'){ //the line with info1
		    	
		    	list= currentline.split(" "); 
		    	
		    	//SequenceNumber.add(list[0]));
		    	Time.add(list[1]+list[2]);
		    	Source.add(list[3]);
		    	Destination.add(list[4]);
		    	Protocol.add(list[5]);
		    	// length of list
		    	stringnum=list.length;
		    	
		    	temp = new StringBuffer(); 
		    	for (i=7;i<=(stringnum-1);i++){
		    		temp=temp.append(list[i]);
		    	}		    	
		    	Info.add(temp.toString()); 	    	
		    	count++; 
		    	while (!con.nextLine().startsWith("Frame")){} // filter the middle lines
		    	currentline=con.nextLine();
		    	//MacAdd.add(currentline.substring(currentline.indexOf('(')+1,currentline.indexOf(')')));
		    	DstAdd.add(currentline.substring(currentline.lastIndexOf('(')+1,currentline.lastIndexOf(')')));
		    	/* Fixing the problem when device name is not given*/
		    	//if (currentline.indexOf('_')>0)
		    	//Device.add((currentline.substring(currentline.indexOf(':')+1,currentline.indexOf('_'))).replaceAll(" ", ""));
		    	//else
		    	//Device.add((LookupDevice(currentline.substring(currentline.indexOf('(')+1,currentline.indexOf(')')))).replaceAll(" ", ""));
		    	/** device move to administration part**/
		    }
		    else;
		  	}
		con.close();
		// close scanner
		// start setting output
		Webpack[] webpackall = new Webpack[count];
		
		//System.out.print(SequenceNumber);
		//System.out.print(Info);
		
		for (int m=0; m<=(count-1); m++)
	        webpackall[m] = new Webpack();
		
		for(int m=0; m<=(count-1);m++ ){
			webpackall[m].notify=m;
			//webpackall[m].SequenceNumber=SequenceNumber.get(m);
			webpackall[m].Time=Time.get(m);
			webpackall[m].Source=Source.get(m);
			webpackall[m].Destination=Destination.get(m);
			webpackall[m].Protocol=Protocol.get(m);
			webpackall[m].Info=Info.get(m);
			//webpackall[m].MacAdd=MacAdd.get(m);
			webpackall[m].DstAdd=DstAdd.get(m);
			//webpackall[m].Device=Device.get(m);
		}		
		//record the length of webpackall
		webpackall[0].notify=count;
		return webpackall;
	}
	
	/* Following are the method for Lookup Device name*/
	private static String LookupDevice(String substring) {
		// TODO Auto-generated method stub
		File p = new File("/Users/luozhongyi/Desktop/2015 Fall course/CS 6400/MacPrefix2.txt");  // The path of MacPrefix.txt
		Scanner pcon = null;
		
		try {
			pcon = new Scanner(p);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String pcurrentline;
		String Device = null;
		char[] prefix = {'0','0','0','0','0','0','0','0'};
		//char px;
		int flag =0;
		
		//prefix=substring.substring(0, 8); // rember to transfer to uppercase
		//System.out.println(substring);
		for (int i=0; i<8; i++){
			//System.out.println(i+"");
			//System.out.println(Character.toUpperCase(substring.charAt(i)));
			if (Character.isLetter(substring.charAt(i)))
				prefix[i]=Character.toUpperCase(substring.charAt(i));
			else
				//System.out.println(i+"");
				//System.out.println(substring.charAt(i));
				prefix[i]=substring.charAt(i);
		}// get the uppercase of prefix

		/* Find in MacPrefix txt */
		while ( pcon.hasNextLine()){
			pcurrentline= pcon.nextLine().toString();
			if (pcurrentline.startsWith(new String(prefix))) {//find the first match and return 
				flag=1;
				pcurrentline= pcurrentline.replaceAll("\\s+"," "); //delete duplicate " ";
				//System.out.println(pcurrentline);
				Device= pcurrentline.substring(pcurrentline.indexOf(' ')+1,pcurrentline.indexOf(' ', pcurrentline.indexOf(' ')+1));
			}
			else
				;
		}
		if (flag==0) Device="DEFAULT";
		//System.out.println(Device);
		pcon.close();
		return Device;		
	}

}
