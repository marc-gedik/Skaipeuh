import  java.net.*;
import java.io.*;
import java.util.Scanner;

class ServeurTCP{

    public static void main(String[] args){

	try{

	    ServerSocket ssRcv = new ServerSocket(60000);
	    ServerSocket ssSent = new ServerSocket(60001);
	    Socket sRcv, sSent;
	    sSent= ssSent.accept();
	    sRcv= ssRcv.accept();

	    BufferedReader br;
	    PrintWriter pw;
	    String msg;
	  
	    while(true){
		br = new BufferedReader(new InputStreamReader(sRcv.getInputStream()));
		System.out.println("attend");;

		msg = br.readLine();
		System.out.println(msg);	
        
		pw = new PrintWriter(new OutputStreamWriter(sSent.getOutputStream()));
		pw.println(msg);
		pw.flush();
	      		System.out.println("fini");;

	    }
	    //sSent.close();
	    //sRcv.close();
	}

	catch(java.io.IOException e){
	    System.out.println(e);
	}
    }
}













/*	text=msg.split(" ");
	if(text[0].equals("GET") && (text[1].charAt(0))=='/' && text[2].startsWith("HTTP/1.")){
	pw.print(message200);
	pw.flush();
			
	}
	       

	while(true){
	msg = br.readLine();

	if(msg.length()==0){
	System.out.println("Ligne vide");

	break;
	}
	}
*/
