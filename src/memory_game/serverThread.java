package memory_game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;


public class serverThread extends Thread{
	
	private Socket prviIgrac;
	private Socket drugiIgrac;
	
	public serverThread(Socket prviIgrac, Socket drugiIgrac) {
		this.prviIgrac = prviIgrac;
		this.drugiIgrac = drugiIgrac;
	}
	
	public String postaviRandomPozicije() {
		
		ArrayList<String> pozicije = new ArrayList<> ();
		 for(int i = 0; i < 10; i++) {
			 pozicije.add(i + "");
			 pozicije.add(i + "");
		 }
		 
		 System.out.println(pozicije);
		 Collections.shuffle(pozicije);
		 System.out.println(pozicije);
		 
		 return pozicije.toString();
	}
	
	public void run() {
		try {
			
			BufferedReader prviIgracInput = new BufferedReader(new InputStreamReader(prviIgrac.getInputStream()));
			PrintWriter prviIgracOutput = new PrintWriter(prviIgrac.getOutputStream(), true);
			String ime = prviIgracInput.readLine();
			System.out.println("Prvi igrac: " + ime);
			
			BufferedReader drugiIgracInput = new BufferedReader(new InputStreamReader(drugiIgrac.getInputStream()));
			PrintWriter drugiIgracOutput = new PrintWriter(drugiIgrac.getOutputStream(), true);
			ime = drugiIgracInput.readLine();
			System.out.println("Drugi igrac: " + ime);
			
			String randomPozicije = postaviRandomPozicije();
			System.out.println(randomPozicije);
			
			prviIgracOutput.println("start");
			prviIgracOutput.println(randomPozicije);
			
			drugiIgracOutput.println("start");
			drugiIgracOutput.println(randomPozicije);
			
			drugiIgracOutput.println("pause");
			prviIgracOutput.println("play");
			
			prviIgrac.close();
			prviIgracInput.close();
			prviIgracOutput.close();
			
			drugiIgrac.close();
			drugiIgracInput.close();
			drugiIgracOutput.close();
			
			String line="";
			
			while(true) {
				
				line = prviIgracInput.readLine();
				while(!"pause".equals(line) && !"end".equals(line)) {
					drugiIgracOutput.println(line);
					line = prviIgracInput.readLine();
				}
				drugiIgracOutput.println(line);
				if("end".equals(line)) {
					break;
				}
				line = drugiIgracInput.readLine();
				while(!"pause".equals(line) && !"end".equals(line)) {
					prviIgracOutput.println(line);
					line = drugiIgracInput.readLine();
				}
				prviIgracOutput.println(line);
				if("end".equals(line)) {
					break;
				}
				
			}	
			
	} catch (SocketException e) {
		
	} catch (IOException e) {
		e.printStackTrace();
	
	}
	}
	

}
