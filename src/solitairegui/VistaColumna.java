package solitairegui;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import solitaire.TableauDeck;
import DeckOfCards.CartaInglesa;

public class VistaColumna extends VBox {
    private TableauDeck modelo;

    public VistaColumna(TableauDeck modelo) {
        this.modelo = modelo;
        this.setSpacing(-100);
        this.setAlignment(Pos.TOP_CENTER);
        refrescar();
    }

    public void refrescar() {
        this.getChildren().clear(); // Limpiamos lo viejo

        if (modelo.isEmpty()) {
            // Si no hay cartas, dibujar espacio vacío
            Rectangle fondo = new Rectangle(100, 140);
            fondo.setFill(Color.TRANSPARENT);
            fondo.setStroke(Color.LIGHTGRAY);
            this.getChildren().add(fondo);
        } else {
            // Dibujar carta
            for (CartaInglesa carta : modelo.getCards()) {
                this.getChildren().add(crearCartaVisual(carta));
            }
        }
    }

    private VistaCarta crearCartaVisual(CartaInglesa carta) {
        return new VistaCarta(carta);
    }
}