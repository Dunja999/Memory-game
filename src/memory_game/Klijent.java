package memory_game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Klijent {
	private static int PORT = 8001;
	private String username;
	private String HOST = "localhost";
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	private gui g;
	public int bodovi = 0;

	public Klijent(String username) {
		this.username = username;
	}

	public Klijent(gui g) {
		this.g = g;
		try {
			socket = new Socket(HOST, PORT);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			System.out.println("Klijent je povezan!");

		} catch (IOException e) {
			e.printStackTrace();
			closeResourses();

		}
	}

	public String run() throws IOException {
		String start = this.input.readLine();
		if (start.equals("start"))
			System.out.println("Igra pocinje!");
		String niz = input.readLine();

		return niz.substring(1, niz.length() - 1);
	}

	public void sendUsername() {
		output.println(username);
	}

	public void setUsername(String name) {
		username = name;
	}

	public String getUsername() {
		return username;
	}

	public void closeResourses() {
		try {
			socket.close();
			input.close();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    //klijent salje serveru poruku
	public void posaljiPoruku(String poruka) {
		output.println(poruka);// saljemo poruku serveru
	}
    
	public void posaljiPozicije(int prvaPozicija, int drugaPozicija, String bool) {

		System.out.println(prvaPozicija + ", " + drugaPozicija);
		output.println(prvaPozicija + ", " + drugaPozicija + ", " + bool);

	}
    //klijent cita sta mu je server poslao
	public String readString() throws IOException {

		String poruka = input.readLine();
		System.out.println(poruka);
		return poruka;
	}

}

