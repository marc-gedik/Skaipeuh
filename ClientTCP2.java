import  java.net.*;
import java.io.*;
import java.util.Scanner;

class ClientTCP2{

    public static void main(String[] args)throws InterruptedException{
	int PORTLIS=0;
	int PORTWRI=0;

	if(args.length==2){
	    PORTLIS=Integer.parseInt(args[0]);
	    PORTWRI=Integer.parseInt(args[1]);
	}
	else{
	    System.out.println("manque les ports ");
	    System.exit(0);
	}






	BufferedInputStream text = new BufferedInputStream(System.in);
	String msg ;
	while(true){

	    try {
		if(text.available()>0){
		    Scanner sc = new Scanner(System.in);
		    msg = sc.next();
		    System.out.println("je lis :" + msg);
		    Socket s = new Socket( "localhost" , PORTWRI);
		    PrintWriter pw = new PrintWriter (new OutputStreamWriter(s.getOutputStream()));
		    pw.println(msg);
		    pw.flush();
		    System.out.println("j'envoie");

		}
		else{

		    //Socket s = new Socket( "localhost" , PORTLIS);
		    // BufferedReader bf = new BufferedReader (new InputStreamReader(s.getInputStream()));

		    // System.out.println(bf.readLine());
		    System.out.println("pb");		
	
		    Thread.sleep(1000);

		    System.out.println("je fais autre chose");
		}
	    
	} catch (IOException e) {
	    System.out.println("Probleme avec le Scanner");
	}
    }






    /*
      while(true){
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
      System.out.println("IOException Client");
	
      }
      }*/
}
}
