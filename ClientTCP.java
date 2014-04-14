import  java.net.*;
import java.io.*;
import java.util.Scanner;

class ClientTCP{

    public static void main(String[] args){
	int PORT=0;

	if(args.length==1)
	    PORT=Integer.parseInt(args[0]);
	else{
	    System.out.println("manque le port ");
	    System.exit(0);
	}
	int i=1;
	while(i!=10){
	    i++;	
	    System.out.println("couc" + i);
	try{
	    Socket s = new Socket( "localhost" , PORT);
	    Scanner sc = new Scanner(System.in);
	    String st = sc.next();
	    PrintWriter pw = new PrintWriter (new OutputStreamWriter(s.getOutputStream()));
	    pw.println(st);
	    pw.flush();
	    BufferedReader bf = new BufferedReader (new InputStreamReader(s.getInputStream()));
	    System.out.println(bf.readLine());
	}
	catch(UnknownHostException e ) {
	    System.out.println("UnknownHostException");
	}
	catch(IOException e ) {
	    System.out.println("IOExecption");
	}
	}
    }
}
