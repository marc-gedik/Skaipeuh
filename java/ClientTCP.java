import  java.net.*;
import java.io.*;

class ClientTCP{

    public static void main(String[] args){
	int PORT = 0;
	if(args.length==1){
	    PORT=Integer.parseInt(args[0]);	   
	}
	else{
	    System.out.println("manque le port ");
	    System.exit(0);
	}

	try{
	    Socket s = new Socket("localhost" , PORT);
	    
	    ThreadWRI wri = new ThreadWRI(PORT,s);		
	    Thread t1 = new Thread(wri);
	    ThreadREAD read = new ThreadREAD(PORT, s);		
	    Thread t2 = new Thread(read);

	    t1.start();
	    t2.start();
	}
	catch(Exception e){
	    System.out.println("Erreur ClientTCP\n"+e);
	}
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

