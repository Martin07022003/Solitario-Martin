package solitairegui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.ArrayList;

import solitaire.SolitaireGame;
import solitaire.TableauDeck;
import solitaire.FoundationDeck;

public class SolitarioGUI extends Application {

    private SolitaireGame modeloJuego;
    private VistaWaste vistaWaste;
    private ArrayList<VistaFundacion> listaVistasFundaciones = new ArrayList<>();
    private HBox contenedorCentro;
    private BorderPane raiz;
    private Stage escenario;

    // Lógica de selección: 0 = nada, 1 = columna, 2 = waste
    private int tipoSeleccion = 0;
    private int indiceSeleccionado = -1;

    @Override
    public void start(Stage escenarioPrincipal) {
        this.escenario = escenarioPrincipal;
        reiniciarJuego(); // Iniciar el juego
    }

    private void reiniciarJuego() {
        modeloJuego = new SolitaireGame();
        listaVistasFundaciones.clear();
        tipoSeleccion = 0;
        indiceSeleccionado = -1;

        raiz = new BorderPane();
        raiz.setPadding(new Insets(20));
        raiz.setStyle("-fx-background-color: #2e7d32;");

        HBox arriba = new HBox(30);
        arriba.setAlignment(Pos.CENTER);
        arriba.setPadding(new Insets(0, 0, 30, 0));

        // Botón de Reiniciar
        Button btnReiniciar = new Button("REINICIAR");
        btnReiniciar.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-weight: bold;");
        btnReiniciar.setOnAction(e -> reiniciarJuego());

        // Waste
        StackPane mazoRobo = crearVistaMazo();
        mazoRobo.setOnMouseClicked(e -> {
            if (modeloJuego.getDrawPile().hayCartas()) {
                modeloJuego.drawCards();
            } else {
                modeloJuego.reloadDrawPile();
            }
            limpiarSeleccion();
        });

        vistaWaste = new VistaWaste(modeloJuego.getWastePile());
        vistaWaste.setOnMouseClicked(e -> {
            if (modeloJuego.getWastePile().hayCartas()) {
                tipoSeleccion = 2;
                actualizarVisual();
            }
        });

        arriba.getChildren().addAll(btnReiniciar, mazoRobo, vistaWaste);

        // Crear Fundaciones
        for (int i = 0; i < 4; i++) {
            VistaFundacion vFund = new VistaFundacion(modeloJuego.getFoundation().get(i));
            vFund.setOnMouseClicked(e -> {
                if (tipoSeleccion == 2) modeloJuego.moveWasteToFoundation();
                if (tipoSeleccion == 1) modeloJuego.moveTableauToFoundation(indiceSeleccionado);
                limpiarSeleccion();
            });
            listaVistasFundaciones.add(vFund);
            arriba.getChildren().add(vFund);
        }
        raiz.setTop(arriba);

        // Columnas de cartas
        contenedorCentro = new HBox(15);
        contenedorCentro.setAlignment(Pos.TOP_CENTER);
        actualizarCentro();
        raiz.setCenter(contenedorCentro);

        Scene escena = new Scene(raiz, 1200, 900);
        escenario.setTitle("Solitario");
        escenario.setScene(escena);
        escenario.show();
    }

    private void manejarClicColumna(int numCol) {
        if (tipoSeleccion == 0) {
            tipoSeleccion = 1;
            indiceSeleccionado = numCol;
        } else if (tipoSeleccion == 1) {
            modeloJuego.moveTableauToTableau(indiceSeleccionado, numCol);
            tipoSeleccion = 0;
        } else if (tipoSeleccion == 2) {
            modeloJuego.moveWasteToTableau(numCol);
            tipoSeleccion = 0;
        }
        actualizarVisual();
    }

    private void actualizarVisual() {
        vistaWaste.refrescar();
        actualizarCentro();
        for (VistaFundacion vf : listaVistasFundaciones) vf.refrescar();

        System.out.println("\n--- CONSOLA ---");
        System.out.println(modeloJuego.toString());
        System.out.println("-----------------------------------------------\n");

        if (modeloJuego.isGameOver()) {
            mostrarPantallaGanaste();
        }
    }

    private void mostrarPantallaGanaste() {
        VBox pantallaVictoria = new VBox(20);
        pantallaVictoria.setAlignment(Pos.CENTER);
        pantallaVictoria.setStyle("-fx-background-color: rgba(255, 255, 0, 0.9);"); // Pantalla Amarilla

        Label textoGanaste = new Label("¡GANASTE!");
        textoGanaste.setFont(Font.font("Arial", FontWeight.BOLD, 120));
        textoGanaste.setTextFill(Color.BLACK);

        Button btnJugarDeNuevo = new Button("NUEVA PARTIDA");
        btnJugarDeNuevo.setStyle("-fx-font-size: 25px; -fx-padding: 15px;");
        btnJugarDeNuevo.setOnAction(e -> reiniciarJuego());

        pantallaVictoria.getChildren().addAll(textoGanaste, btnJugarDeNuevo);

        StackPane layoutFinal = new StackPane();
        layoutFinal.getChildren().addAll(raiz, pantallaVictoria);

        Scene escenaVictoria = new Scene(layoutFinal, 1200, 900);
        escenario.setScene(escenaVictoria);
    }

    private void actualizarCentro() {
        contenedorCentro.getChildren().clear();
        for (int i = 0; i < 7; i++) {
            final int numCol = i + 1;
            VistaColumna vCol = new VistaColumna(modeloJuego.getTableau().get(i));
            if (tipoSeleccion == 1 && indiceSeleccionado == numCol) {
                vCol.setStyle("-fx-border-color: yellow; -fx-border-width: 3; -fx-border-radius: 5;");
            }
            vCol.setOnMouseClicked(e -> manejarClicColumna(numCol));
            contenedorCentro.getChildren().add(vCol);
        }
    }

    private void limpiarSeleccion() {
        tipoSeleccion = 0;
        indiceSeleccionado = -1;
        actualizarVisual();
    }

    private StackPane crearVistaMazo() {
        StackPane sp = new StackPane();
        try {
            ImageView iv = new ImageView(new Image(getClass().getResourceAsStream("/imagenes/reverso.png")));
            iv.setFitWidth(100); iv.setFitHeight(140);
            sp.getChildren().add(iv);
        } catch (Exception e) {
            Rectangle r = new Rectangle(110, 150, Color.BLACK);
            r.setArcWidth(10); r.setArcHeight(10);
            sp.getChildren().add(r);
        }
        return sp;
    }

    public static void main(String[] args) { launch(args); }
}