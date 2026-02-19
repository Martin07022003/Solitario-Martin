package solitairegui;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import solitaire.FoundationDeck;

public class VistaFundacion extends StackPane {
    private FoundationDeck modelo;

    public VistaFundacion(FoundationDeck modelo) {
        this.modelo = modelo;
        refrescar();
    }

    public void refrescar() {
        this.getChildren().clear();

        Rectangle base = new Rectangle(110, 150);
        base.setArcWidth(10);
        base.setArcHeight(10);
        base.setFill(Color.TRANSPARENT);
        base.setStroke(Color.WHITE);
        base.setStrokeWidth(2);
        this.getChildren().add(base);

        // Si hay cartas en la fundación las dibuja
        if (!modelo.estaVacio()) {
            VistaCarta cartaVisual = new VistaCarta(modelo.getUltimaCarta());
            this.getChildren().add(cartaVisual);
        }
    }
}
