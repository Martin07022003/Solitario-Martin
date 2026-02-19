package solitairegui;

import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import DeckOfCards.CartaInglesa;

public class VistaCarta extends StackPane {
    private CartaInglesa modelo;

    public VistaCarta(CartaInglesa modelo) {
        this.modelo = modelo;
        dibujar();
    }

    private void dibujar() {
        this.getChildren().clear();

        // Fondo blanco de la carta
        Rectangle fondoBlanco = new Rectangle(110, 150);
        fondoBlanco.setFill(Color.WHITE);
        fondoBlanco.setArcWidth(10);
        fondoBlanco.setArcHeight(10);
        this.getChildren().add(fondoBlanco);

        String nombreArchivo;
        if (modelo.isFaceup()) {
            String valorTexto;
            switch (modelo.getValor()) {
                case 14:  valorTexto = "ace"; break;
                case 11: valorTexto = "jack"; break;
                case 12: valorTexto = "queen"; break;
                case 13: valorTexto = "king"; break;
                default: valorTexto = String.valueOf(modelo.getValor());
            }

            // Conversión de palo a inglés
            String paloOriginal = modelo.getPalo().toString().toLowerCase();
            String paloIngles = "";

            if (paloOriginal.contains("pica") || paloOriginal.contains("spade")) paloIngles = "spades";
            else if (paloOriginal.contains("corazon") || paloOriginal.contains("heart")) paloIngles = "hearts";
            else if (paloOriginal.contains("diamante") || paloOriginal.contains("diamond")) paloIngles = "diamonds";
            else if (paloOriginal.contains("trebol") || paloOriginal.contains("club")) paloIngles = "clubs";

            // Nombre: /imagenes/ace_of_spades.png
            nombreArchivo = "/imagenes/" + valorTexto + "_of_" + paloIngles + ".png";
        } else {
            // Ruta para la carta volteada
            nombreArchivo = "/imagenes/reverso.png";
        }

        try {
            java.io.InputStream stream = getClass().getResourceAsStream(nombreArchivo);

            if (stream == null) {
                throw new Exception("Archivo no encontrado en la ruta: " + nombreArchivo);
            }

            Image img = new Image(stream);
            ImageView vistaImg = new ImageView(img);

            vistaImg.setFitWidth(110);
            vistaImg.setFitHeight(150);
            vistaImg.setPreserveRatio(false);

            this.getChildren().add(vistaImg);
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo cargar la imagen: " + nombreArchivo);

            javafx.scene.control.Label etiqueta = new javafx.scene.control.Label(modelo.toString());
            if (modelo.getColor().equals("rojo")) etiqueta.setTextFill(Color.RED);
            this.getChildren().add(etiqueta);
        }
    }
}
