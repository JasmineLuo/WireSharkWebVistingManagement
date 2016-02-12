public class Webpack {	
		 int notify;
		 //String SequenceNumber;
		 String Time;
		 String Source;
		 String Destination;
		 String Protocol;
		 String Info;
		 //String MacAdd;
		 String DstAdd;
		 //String Device;
		 
		 public String toString() {
		        //return (SequenceNumber +"    "+ Time + "    "+ Source + "    "+ Destination +"    "+ Protocol +"    "+ Info +"    "+ MacAdd + "    "+ DstAdd + "    "+ Device+"\n");
		        //return (Time + "    "+ Source + "    "+ Destination +"    "+ Protocol +"    "+ Info +"    "+ MacAdd + "    "+ DstAdd + "    "+ Device+"\n");
		        return (Time + "    "+ Source + "    "+ Destination +"    "+ Protocol +"    "+ Info +"    "+ DstAdd + "\n");
		    }
}
// Webpack correspond to tuple;