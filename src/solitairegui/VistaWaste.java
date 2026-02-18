package solitairegui;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import solitaire.WastePile;
import DeckOfCards.CartaInglesa;

public class VistaWaste extends StackPane {
    private WastePile modelo;

    public VistaWaste(WastePile modelo) {
        this.modelo = modelo;
        refrescar();
    }

    public void refrescar() {
        this.getChildren().clear();

        if (!modelo.hayCartas()) {
            Rectangle fondo = new Rectangle(110, 150);
            fondo.setArcWidth(10);
            fondo.setArcHeight(10);
            fondo.setFill(Color.TRANSPARENT);
            fondo.setStroke(Color.WHITE);
            this.getChildren().add(fondo);
        } else {
            CartaInglesa cartaLogica = modelo.verCarta();

            if (cartaLogica != null) {
                cartaLogica.makeFaceUp();
                VistaCarta vistaCarta = new VistaCarta(cartaLogica);
                this.getChildren().add(vistaCarta);
            }
        }
    }
}