package memory_game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class Panel {

	private static final int broj_parova = 10;
	private static final int broj_po_redovima = 5;

	private Klijent klijent;
	private Pane root;
	private Label bodovi;
	private Label pobjednik;
	private List<Plocica> plocice;
	private boolean endTurn = false;
	private int brojKlikova = 2;
	Plocica selected = null;
	public static int preostaleKarte = broj_parova;

	public Panel(Klijent klijent) {
		this.klijent = klijent;
	}

	public Parent createContent(String pozicije) {

		root = new Pane();
		root.setMinSize(625, 610);
		root.setStyle("-fx-text-box-border: black ");

		bodovi = new Label();
		plocice = new ArrayList<>();
		pobjednik = new Label();

		Image[] array = new Image[broj_parova];

		array[0] = new Image(getClass().getResourceAsStream("brokuli.jpg"));
		array[1] = new Image(getClass().getResourceAsStream("cuko.jpg"));
		array[2] = new Image(getClass().getResourceAsStream("jagoda.jpg"));
		array[3] = new Image(getClass().getResourceAsStream("kafa.jpg"));
		array[4] = new Image(getClass().getResourceAsStream("kaktus.jpg"));
		array[5] = new Image(getClass().getResourceAsStream("pingvin.png"));
		array[6] = new Image(getClass().getResourceAsStream("koala.jpg"));
		array[7] = new Image(getClass().getResourceAsStream("leptir.jpg"));
		array[8] = new Image(getClass().getResourceAsStream("maca.jpg"));
		array[9] = new Image(getClass().getResourceAsStream("pecurka.jpg"));

		for (String pozicija : pozicije.split(",")) {
			int i = Integer.parseInt(pozicija.trim());

			ImageView slika = new ImageView(array[i]);
			slika.setFitHeight(120);
			slika.setFitWidth(120);
			plocice.add(new Plocica(slika));

		}

		int razmak = 5;

		for (int i = 0; i < plocice.size(); i++) {
			Plocica plocica = plocice.get(i);
			plocica.pozicija = i;

			// Izračunavanje pomaka pomoću širine (ili visine) pločice plus razmak
			int offsetX = (int) ((plocica.getBoundsInLocal().getWidth() + razmak) * (i % broj_po_redovima));
			int offsetY = (int) ((plocica.getBoundsInLocal().getHeight() + razmak) * (i / broj_po_redovima));

			plocica.setTranslateX(offsetX);
			plocica.setTranslateY(offsetY);

			root.getChildren().add(plocica);

		}

		bodovi.setTextFill(Color.MISTYROSE);
		bodovi.setFont(Font.font("Pristina", FontWeight.EXTRA_BOLD, 40));
		bodovi.setVisible(true);
		bodovi.setText(klijent.getUsername() + ": " + klijent.bodovi);
		bodovi.setTranslateY(125 * (plocice.size() / broj_po_redovima));
		

		pobjednik.setTextFill(Color.BLACK);
		pobjednik.setFont(Font.font("Pristina", FontWeight.EXTRA_BOLD, 100));
		pobjednik.setVisible(true);
		pobjednik.setTranslateY(200);
		pobjednik.setTranslateX(100);
	   
		
		
		root.getChildren().addAll(bodovi, pobjednik);
		return root;

	}

	public void ukloniKarte(String pozicije) {
		String[] dijelovi = pozicije.split(",");
		int prvaPozicija = Integer.parseInt(dijelovi[0].trim());
		int drugaPozicija = Integer.parseInt(dijelovi[1].trim());
		boolean ukloni = Boolean.parseBoolean(dijelovi[2].trim());
		if (ukloni) {
			root.getChildren().remove(prvaPozicija);
			root.getChildren().add(prvaPozicija, new Rectangle());
																	
			root.getChildren().remove(drugaPozicija);
			root.getChildren().add(drugaPozicija, new Rectangle());

			preostaleKarte--;

		}

	}

	public void prikaziProtivnikovePoteze(String pozicije) {
		String[] dijelovi = pozicije.split(",");

		if (dijelovi.length < 3) {
			klijent.posaljiPoruku("kraj");
			return; 
		}

		try {
			int prvaPozicija = Integer.parseInt(dijelovi[0].trim());
			int drugaPozicija = Integer.parseInt(dijelovi[1].trim());
			boolean ukloni = Boolean.parseBoolean(dijelovi[2].trim());

			if (prvaPozicija < 0 || drugaPozicija < 0) {
				System.err.println("Pozicije ne smiju biti negativne.");
				return;
			}

			if (prvaPozicija >= plocice.size() || drugaPozicija >= plocice.size()) {
				System.err.println("Pozicije premašuju broj pločica.");
				return; 
			}

			plocice.get(prvaPozicija).show(() -> {
				root.setDisable(false);
				if (ukloni) {
					root.setDisable(true);
					ukloniKarte(pozicije);
					if (preostaleKarte == 0) {

						if (klijent.bodovi == 5) {
							pobjednik.setText("Nerijeseno je!");
						} else if (klijent.bodovi > 5) {
							pobjednik.setText("Pobijedili ste!");
						} else if (klijent.bodovi < 5) {
							pobjednik.setText("Izgubili ste!");
						}

						klijent.posaljiPoruku("kraj");
						klijent.closeResourses();

					}

				}
			});
			
			plocice.get(drugaPozicija).show(() -> {
			});
			
		} catch (NumberFormatException e) {
			System.err.println("Neuspela konverzija stringa u broj ili boolean.");
			return;
		}
	}

	private class Plocica extends StackPane {

		int pozicija;
		ImageView slika;

		public Plocica(ImageView slika) {
			this.slika = slika;

			Rectangle border = new Rectangle(120, 120, Color.BLACK);
			border.setFill(null);
			border.setStroke(Color.WHITE);

			setAlignment(Pos.CENTER);
			getChildren().addAll(this.slika, border);

			setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					handleMouseClick(event);
				}
			});
			slika.setOpacity(0);

		}

		public void handleMouseClick(MouseEvent event) {

			if (isOpen() || brojKlikova == 0) {
				return;
			}
			brojKlikova--;

			if (selected == null) {
									
				selected = this;
				root.setDisable(true);
				open(new Runnable() {
					@Override
					public void run() {
						root.setDisable(false);
												
					}
				});

			} else {
				open(() -> {

					if (!imaIstuVrijednost(selected)) {
						selected.close(0.5);
						this.close(0.5);
						klijent.posaljiPozicije(this.pozicija, selected.pozicija, "false");
						klijent.posaljiPoruku("pauza");
						endTurn = true;
									

					} else {
					
						klijent.bodovi++;
						bodovi.setText(klijent.getUsername() + ": " + klijent.bodovi);
						System.out.println(this.pozicija);
						System.out.println(selected.pozicija);
						klijent.posaljiPozicije(this.pozicija, selected.pozicija, "true");											
						ukloniKarte(pozicija + "," + selected.pozicija + "," + "true");
																																		
						if (preostaleKarte == 0) {

							if (klijent.bodovi == 5) {
								pobjednik.setText("Nerijeseno je!");
							} else if (klijent.bodovi > 5) {
								pobjednik.setText("Pobijedili ste!");
							} else if (klijent.bodovi < 5) {
								pobjednik.setText("Izgubili ste!");
							}

							klijent.posaljiPoruku("kraj");
						    klijent.closeResourses();
							return;
						}

					}

					selected = null;
					brojKlikova = 2;

					if (endTurn) {

						root.setDisable(true);
						new Thread(() -> {
							
							try {
								String linija = klijent.readString();
								
								while (!"pauza".equals(linija) && !"kraj".equals(linija)) {
									String selektovaneKarte = linija;
									Platform.runLater(() -> prikaziProtivnikovePoteze(selektovaneKarte));															
									linija = klijent.readString();
																	
								}
								endTurn = false;
								
							} catch (IOException e) {
								e.printStackTrace();
							}
						}).start();
					}

				});

			}
		}

		public boolean imaIstuVrijednost(Plocica druga) {

			return slika.getImage().equals(druga.slika.getImage());// uporedjuje dvije slike
		}

		public boolean isOpen() {

			return slika.getOpacity() == 1;
		}

		public void open(Runnable action) {

			FadeTransition ft = new FadeTransition(Duration.seconds(0.5), slika);
			ft.setToValue(1);
			ft.setOnFinished(e -> action.run());
			ft.play();

		}

		public void close(double seconds) {

			FadeTransition ft = new FadeTransition(Duration.seconds(seconds), slika);
			ft.setToValue(0);
			ft.play();

		}

		public void show(Runnable action) {
			FadeTransition ft = new FadeTransition(Duration.seconds(1), slika);
			ft.setToValue(1);
			ft.setAutoReverse(true);
			ft.setCycleCount(2);
			ft.setOnFinished(e -> action.run());
			ft.play();

		}

	}
}

