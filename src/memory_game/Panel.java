package memory_game;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import memory_game.Klijent;


public class Panel {
	private Klijent klijent;
	private Pane root;
	private Label bodovi;
	private List<Plocica> plocice;
	private static final int broj_parova = 10;
	private static final int broj_po_redovima = 4;
	
	public Panel(Klijent klijent) {
		this.klijent = klijent;
	}
	
	public Parent createContent(String pozicije) {
		
		root = new Pane();
		root.setMinSize(600, 800);
		bodovi = new Label();
		plocice = new ArrayList<>();
		Image[] niz = new Image[broj_parova];

		niz[0]= new Image("slike/brokuli.jpg");
		niz[1]= new Image("slike/cuko.jpg");
		niz[2]= new Image("slike/jagoda.jpg");
		niz[3]= new Image("slike/kafa.jpg");
		niz[4]= new Image("slike/kaktus.jpg");
		niz[5]= new Image("slike/koala.jpg");
		niz[6]= new Image("slike/leptir.jpg");
		niz[7]= new Image("slike/maca.jpg"); 
		niz[8]= new Image("slike/pecurka.jpg");
		niz[9]= new Image("slike/pingvin.png");

		for (String pozicija : pozicije.split(",")) {
        	int i = Integer.parseInt(pozicija.trim());

            ImageView slika = new ImageView(niz[i]);
            slika.setFitHeight(100);
            slika.setFitWidth(100);
            plocice.add(new Plocica(slika));
            
		}
	}
}
