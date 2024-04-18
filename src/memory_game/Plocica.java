package memory_game;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Plocica extends StackPane {
	
private int pozicija;
private Klijent klijent;
private boolean endTurn = false;
private Pane root;
private ImageView slika;
private int brojKlikova = 2;
Plocica selected = null;

public Plocica(ImageView slika) {
	this.slika = slika;
	Rectangle border = new Rectangle(120, 120, Color.BLUE);
	setAlignment(Pos.CENTER);
	getChildren().addAll(this.slika, border);
	
	setOnMouseClicked(new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
	        handleMouseClick(event);
	    }
	});
}
	public void handleMouseClick(MouseEvent event) {
		
		if (isOpen() || brojKlikova == 0) {
			return;
		}
		brojKlikova--;
		if(selected == null) {
			selected = this;
			root.setDisable(true);
			open(new Runnable() {
			    @Override
			    public void run() {
			        root.setDisable(false);
			    }
			});
			
		}
		else {
			open(new Runnable() {

			    public void run() {
			        if (!imaIstuVrijednost(selected)) {
			            selected.close(0.5);
			            Plocica.this.close(0.5);
			            klijent.posaljiPozicije(Plocica.this.pozicija, selected.pozicija);
			            klijent.posaljiPoruku("pause");
			            endTurn = true;
			            
			        }
			    }
			});
		}
	}
	
	public boolean imaIstuVrijednost(Plocica druga) {
		
		return slika.getImage().equals(druga.slika.getImage());
	}
				
	public boolean isOpen() {
		
		return slika.getOpacity() == 1;
	}
	
	public void open(Runnable action) {
	
		
	}
	
	public void close(double seconds) {
		
	}
	

}
