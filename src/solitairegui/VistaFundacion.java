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

        // 1. Dibujamos el marco base (el cuadro con borde blanco)
        Rectangle base = new Rectangle(110, 150);
        base.setArcWidth(10);
        base.setArcHeight(10);
        base.setFill(Color.TRANSPARENT);
        base.setStroke(Color.WHITE);
        base.setStrokeWidth(2);
        this.getChildren().add(base);

        // 2. Si hay cartas en la fundación (como un As), usamos VistaCarta
        if (!modelo.estaVacio()) {
            VistaCarta cartaVisual = new VistaCarta(modelo.getUltimaCarta());
            this.getChildren().add(cartaVisual);
        }
    }
}
