package memory_game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Klijent {
	private static final int PORT = 11111;
	private String username;
	private String HOST = "localhost";
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	private int points;
	
	public Klijent() {
		try {
			socket = new Socket(HOST, PORT);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			System.out.println("Klijent je povezan!");
		}
		catch(IOException e) {
			e.printStackTrace();
			closeResourses();
		}
	}
	
	public String run() throws IOException {
		String start = this.input.readLine();
		System.out.println(start);
		if(start.equals("start"))
			System.out.println("Igra pocinje!");
		String niz = input.readLine();
		System.out.println("Raspored: " + niz);
		
		
		return niz.substring(1, niz.length() - 1);
	}
	
	public void closeResourses() {
		try {
			socket.close();
			input.close();
			output.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void posaljiPoruku(String poruka) {
		output.println(poruka);
	}
	
	public void posaljiPozicije(int prvaPozicija, int drugaPozicija) {

		System.out.println(prvaPozicija + ", " + drugaPozicija);
		output.println(prvaPozicija + "," + drugaPozicija );
		
	}
	

}
