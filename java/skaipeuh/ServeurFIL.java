package skaipeuh;

import java.net.*;
import java.io.*;

class ServeurFIC implements Runnable {

	int port;
	static boolean running = true;
	int fileSize;
	String filename;
	Socket s;

	public ServeurFIC(int p, int fileSize, String filename, Socket s) {
		this.port = p;
		this.fileSize = 100;
		this.filename = filename;
		this.s=s;
	}

	synchronized public void run() {
		try {
			char[] buff = new char[512];

			File f	= createFic("./downloads/"+filename);
			
			System.out.println("Debut du telechargement ...");
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			br.read(buff);
			
			FileWriter ecrivain = new  FileWriter(f);

			ecrivain.write("salut");
			ecrivain.flush();

			System.out.println("Telechargement termine");


		} catch (IOException e) {
			System.out.println("Erreur ServeurFIC\n" + e);
			System.exit(-1);
		}
	}

	public static File createFic(String chemin){
		File f=null;	
		try{
			String basename[]=chemin.split("/");
			String path="";
			String	filename;
			String mot[];
			String racine[];

			for(int i=0; i<basename.length-1; i++)
				path+=basename[i]+"/";

			filename=basename[basename.length-1];
			int j=1;

			while(new File(path+filename).exists()){

				mot=filename.split("\\.");		
				racine= mot[0].split("\\(\\d{0,}\\)");

				filename=racine[0]+"("+j+").";
				for(int k=1; k<mot.length; k++)
					filename+=mot[k]+".";

				j++;
				filename =filename.substring(0, filename.length()-1);
				System.out.println(filename);


			}

			f=new File(path+filename);
			f.createNewFile();

		}catch(Exception e){
			System.out.println(e);
		}
		return f;

	}	
}
