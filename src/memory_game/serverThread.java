package memory_game;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Platform;


public class ServerThread extends Thread {

	private Socket prviIgrac;
	private Socket drugiIgrac;
	private Klijent igrac1;
	private Klijent igrac2;

	public ServerThread(Socket prviIgrac, Socket drugiIgrac) {
		this.prviIgrac = prviIgrac;
		this.drugiIgrac = drugiIgrac;

	}

	public String postaviRandomPozicije() {
		ArrayList<String> pozicije = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			pozicije.add(i + "");
			pozicije.add(i + "");
		}
		Collections.shuffle(pozicije);
		return pozicije.toString();
	}

	@Override
	public void run() {
		try {

			BufferedReader prviIgracInput = new BufferedReader(new InputStreamReader(prviIgrac.getInputStream()));
			PrintWriter prviIgracOutput = new PrintWriter(prviIgrac.getOutputStream(), true);
			String imePrvog = prviIgracInput.readLine();
			System.out.println("Prvi igrač: " + imePrvog);
			igrac1 = new Klijent(imePrvog);

			BufferedReader drugiIgracInput = new BufferedReader(new InputStreamReader(drugiIgrac.getInputStream()));
			PrintWriter drugiIgracOutput = new PrintWriter(drugiIgrac.getOutputStream(), true);
			String imeDrugog = drugiIgracInput.readLine();
			System.out.println("Drugi igrač: " + imeDrugog);
			igrac2 = new Klijent(imeDrugog);

			String randomPozicije = postaviRandomPozicije();

			prviIgracOutput.println("start");
			prviIgracOutput.println(randomPozicije);

			drugiIgracOutput.println("start");
			drugiIgracOutput.println(randomPozicije);

			drugiIgracOutput.println("pauza");
			prviIgracOutput.println("igraj");

			String linija = "";

			while (Panel.preostaleKarte > 0) {
				linija = prviIgracInput.readLine();
				while (!"pauza".equals(linija) && !"kraj".equals(linija)) {
					drugiIgracOutput.println(linija);

				
					if (linija.contains("true")) {
						igrac1.bodovi++;
						Panel.preostaleKarte--;
					}

					linija = prviIgracInput.readLine();
				}
				drugiIgracOutput.println(linija);
				if ("kraj".equals(linija)) {
					break;
				}
				linija = drugiIgracInput.readLine();
				while (!"pauza".equals(linija) && !"kraj".equals(linija)) {
					prviIgracOutput.println(linija);

				
					if (linija.contains("true")) {
						igrac2.bodovi++;
						Panel.preostaleKarte--;
					}

					linija = drugiIgracInput.readLine();
				}
				prviIgracOutput.println(linija);
				if ("kraj".equals(linija)) {
					break;
				}
			}

			System.out.println("--------------------------------------");
			vratiPobjednika();

			Panel.preostaleKarte = 10;
			prviIgrac.close();
			prviIgracInput.close();
			prviIgracOutput.close();

			drugiIgrac.close();
			drugiIgracInput.close();
			drugiIgracOutput.close();
			
		} catch (SocketException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void vratiPobjednika() {
		if (igrac1.bodovi > igrac2.bodovi) {
			System.out.println("Pobjednik je: " + igrac1.getUsername());
		} else if (igrac1.bodovi < igrac2.bodovi) {
			System.out.println("Pobjednik je: " + igrac2.getUsername());
		} else {
			System.out.println("Nerijeseno je!");
		}

	}

}
