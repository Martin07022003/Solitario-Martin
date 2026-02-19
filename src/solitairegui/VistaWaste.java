package solitairegui;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import solitaire.WastePile;
import DeckOfCards.CartaInglesa;

public class VistaWaste extends HBox {
    private WastePile modelo;

    public VistaWaste(WastePile modelo) {
        this.modelo = modelo;
        this.setSpacing(10); // Espacio entre el mazo y la carta sacada
        refrescar();
    }

    public void refrescar() {
        this.getChildren().clear();

        StackPane mazoOculto = new StackPane();
        Rectangle marcoMazo = new Rectangle(110, 150);
        marcoMazo.setArcWidth(10); marcoMazo.setArcHeight(10);
        marcoMazo.setFill(Color.TRANSPARENT);
        marcoMazo.setStroke(Color.WHITE);
        mazoOculto.getChildren().add(marcoMazo);

        if (modelo.hayCartas()) {
            try {
                Image imgRev = new Image(getClass().getResourceAsStream("/imagenes/reverso.png"));
                ImageView vistaRev = new ImageView(imgRev);
                vistaRev.setFitWidth(110); vistaRev.setFitHeight(150);
                mazoOculto.getChildren().add(vistaRev);
            } catch (Exception e) {
                marcoMazo.setFill(Color.DARKGREEN);
            }
        }

        StackPane cartaActual = new StackPane();
        if (modelo.hayCartas()) {
            CartaInglesa cartaLogica = modelo.verCarta();
            if (cartaLogica != null) {
                cartaLogica.makeFaceUp();
                cartaActual.getChildren().add(new VistaCarta(cartaLogica));
            }
        } else {
            Rectangle marcoVacio = new Rectangle(110, 150);
            marcoVacio.setArcWidth(10); marcoVacio.setArcHeight(10);
            marcoVacio.setFill(Color.TRANSPARENT);
            marcoVacio.setStroke(Color.GRAY);
            cartaActual.getChildren().add(marcoVacio);
        }

        this.getChildren().addAll(mazoOculto, cartaActual);
    }
}