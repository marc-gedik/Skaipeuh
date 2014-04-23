import  java.net.*;
import java.io.*;
import java.util.Scanner;



public class ThreadLIS implements Runnable{
	
    int PORT = 0;

    public ThreadLIS(int  a){
	this.PORT=a;
    }

    public void run(){
	try{	
	    Socket s = new Socket( "localhost" , PORT);
	    BufferedReader bf = new BufferedReader (new InputStreamReader(s.getInputStream()));

	    while(true){
		System.out.println("j'écoute");		
		System.out.println(bf.readLine());
		System.out.println("je fais autre chose");
	    }
	}
	catch (IOException e) {
	    System.out.println("theadlis" + e);
	}
    }
}
