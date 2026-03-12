package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;

import java.util.ArrayList;

/**
 * Juego de solitario.
 *
 * @author (Cecilia Curlango Rosas)
 * @version (2025-2)
 *
 */
public class SolitaireGame {
    ArrayList<TableauDeck> tableau = new ArrayList<>();
    ArrayList<FoundationDeck> foundation = new ArrayList<>();
    FoundationDeck lastFoundationUpdated;
    DrawPile drawPile;
    WastePile wastePile;

    // Pila para el historial de movimientos (Undo)
    private Pila<Movimiento> historial = new Pila<>(100);

    public SolitaireGame() {
        drawPile = new DrawPile();
        wastePile = new WastePile();
        createTableaux();
        createFoundations();
        wastePile.addCartas(drawPile.retirarCartas());
    }

    /**
     * Captura el estado actual antes de un cambio y lo guarda en la pila de historial.
     */
    private void registrarEstado() {
        Movimiento instantanea = new Movimiento(drawPile, wastePile, tableau, foundation);
        historial.push(instantanea);
    }

    /**
     * Revierte el juego al estado anterior almacenado en la pila de historial.
     */
    /**
     * Revierte el juego al estado anterior almacenado en la pila de historial.
     */
    public void deshacer() {
        if (!historial.pilaVacia()) {
            Movimiento anterior = historial.pop();

            // Restaurar Draw y Waste
            this.drawPile.setCartasInternas(anterior.copiaDraw);
            this.wastePile.setCartasInternas(anterior.copiaWaste);

            // Restaurar Tableaus y la carta superior esté boca arriba
            for (int i = 0; i < 7; i++) {
                this.tableau.get(i).setCards(new ArrayList<CartaInglesa>(anterior.copiaTableaux[i]));
            }

            // Restaurar Foundations
            for (int i = 0; i < 4; i++) {

                this.foundation.get(i).setCardsInternas(anterior.copiaFoundations[i]);

                if (!this.foundation.get(i).estaVacio()) {
                    this.foundation.get(i).getUltimaCarta().makeFaceUp();
                }
            }
        }
    }

    /**
     * Move cards from Waste pile to Draw Pile.
     */
    public void reloadDrawPile() {
        registrarEstado(); // Registro antes de recargar
        ArrayList<CartaInglesa> cards = wastePile.emptyPile();
        drawPile.recargar(cards);
    }

    /**
     * Move cards from Draw pile to Waste Pile.
     */
    public void drawCards() {
        registrarEstado(); // Registro antes de robar
        ArrayList<CartaInglesa> cards = drawPile.retirarCartas();
        wastePile.addCartas(cards);
    }

    /**
     * Tomar la carta del Waste pile y ponerla en el tableau
     *
     * @param tableauDestino donde se coloca la carta
     * @return true si se pudo hacer el movimiento, false si no
     */
    public boolean moveWasteToTableau(int tableauDestino) {
        boolean movimientoRealizado = false;
        TableauDeck destino = tableau.get(tableauDestino - 1);
        if (moveWasteToTableau(destino)) {
            movimientoRealizado = true;
        }
        return movimientoRealizado;
    }

    /**
     * Tomar varias cartas del Tableau fuente y colocarlas en el
     * Tableau destino.
     *
     * @param tableauFuente  de donde se toma la carta (1-7)
     * @param tableauDestino donde se coloca la carta (1-7)
     * @return true si se pudo hacer el movimiento, false si no
     */
    public boolean moveTableauToTableau(int tableauFuente, int tableauDestino) {
        boolean movimientoRealizado = false;
        TableauDeck fuente = tableau.get(tableauFuente - 1);
        if (!fuente.isEmpty()) {
            TableauDeck destino = tableau.get(tableauDestino - 1);

            int valorQueDebeTenerLaCartaInicialDeLaFuente;
            CartaInglesa cartaUltimaDelDestino; // aqui se coloca la fuente
            if (!destino.isEmpty()) {
                // si hay cartas en el destino, la ultima y primer debe concordar
                cartaUltimaDelDestino = destino.verUltimaCarta();
                valorQueDebeTenerLaCartaInicialDeLaFuente = cartaUltimaDelDestino.getValor() - 1;
            } else {
                // si el destino está vacío, solo puede colocar rey
                valorQueDebeTenerLaCartaInicialDeLaFuente = 13;
            }
            // ver que carta es la del inicio del bloque
            CartaInglesa cartaInicialDePrueba = fuente.viewCardStartingAt(valorQueDebeTenerLaCartaInicialDeLaFuente);
            if (cartaInicialDePrueba != null && destino.sePuedeAgregarCarta(cartaInicialDePrueba)) {

                registrarEstado();

                ArrayList<CartaInglesa> cartas = fuente.removeStartingAt(valorQueDebeTenerLaCartaInicialDeLaFuente);
                if (destino.agregarBloqueDeCartas(cartas)) {
                    if (!fuente.isEmpty()) {
                        // Voltear la carta que se destapa en el Tableau
                        fuente.verUltimaCarta().makeFaceUp();
                    }
                    movimientoRealizado = true;
                } else {
                    historial.pop();
                }
            }
        }
        return movimientoRealizado;
    }

    /**
     * Tomar la carta de Tableau y colocarla en el Foundation.
     *
     * @param numero de tableau donde se moverá la carta (1-7)
     * @return true si se pudo move la carta, false si no
     */
    public boolean moveTableauToFoundation(int numero) {
        boolean movimientoRealizado = false;
        TableauDeck fuente = tableau.get(numero - 1);
        CartaInglesa carta = fuente.removerUltimaCarta();

        registrarEstado(); // Guardamos antes del movimiento

        if (moveCartaToFoundation(carta)) {
            movimientoRealizado = true;
        } else {
            // regresar la carta al tableau porque no se puede hacer el movimiento
            fuente.agregarCarta(carta);
            historial.pop(); // Borrar el estado porque no hubo movimiento
        }
        return movimientoRealizado;
    }

    /**
     * Tomar la carta de Waste y colocarla en el Tableau.
     *
     * @param tableau donde se moverá la carta
     * @return true si se pudo move la carta, false si no
     */
    public boolean moveWasteToTableau(TableauDeck tableau) {
        boolean movimientoRealizado = false;
        CartaInglesa carta = wastePile.verCarta();

        registrarEstado(); // Registro preventivo

        if (moveCartaToTableau(carta, tableau)) {
            // si es movimiento válido, elimina la carta de la pila
            carta = wastePile.getCarta();
            movimientoRealizado = true;
        } else {
            historial.pop(); // Movimiento inválido
        }
        return movimientoRealizado;
    }

    /**
     * Tomar una carta de Waste y ponerla en una de las Foundations.
     *
     * @return true si se pudo hacer el movimiento.
     */
    public boolean moveWasteToFoundation() {
        boolean movimientoRealizado = false;
        CartaInglesa carta = wastePile.verCarta();

        registrarEstado();

        if (moveCartaToFoundation(carta)) {
            // si es movimiento válido, elimina la carta de la pila
            carta = wastePile.getCarta();
            movimientoRealizado = true;
        } else {
            historial.pop();
        }
        return movimientoRealizado;
    }

    /**
     * Coloca la carta recibida en el Tableau recibido.
     *
     * @param carta   a colocar
     * @param destino Tableau que recibe la carta.
     * @return true si se pudo hacer el movimiento, false si no
     */
    private boolean moveCartaToTableau(CartaInglesa carta, TableauDeck destino) {
        return destino.agregarCarta(carta);
    }

    /**
     * Coloca la carta recibida en el Foundation correspondiente.
     *
     * @param carta a colocar
     * @return true si se pudo hacer el movimiento, false si no.
     */
    private boolean moveCartaToFoundation(CartaInglesa carta) {
        int cualFoundation = carta.getPalo().ordinal();
        FoundationDeck destino = foundation.get(cualFoundation);
        lastFoundationUpdated = destino;
        return destino.agregarCarta(carta);
    }

    /**
     * Determina si se terminó el juego. El juego se
     * termina cuando todas las cartas están en Foundation
     *
     * @return true si se terminó el juego
     */
    public boolean isGameOver() {
        boolean gameOver = true;
        for (FoundationDeck foundation : foundation) {
            if (foundation.estaVacio()) {
                gameOver = false;
            } else {
                CartaInglesa ultimaCarta = foundation.getUltimaCarta();
                // si la última carta no es rey, no se ha terminado
                if (ultimaCarta.getValor() != 13) {
                    gameOver = false;
                }
            }
        }
        return gameOver;
    }

    private void createFoundations() {
        for (Palo palo : Palo.values()) {
            foundation.add(new FoundationDeck(palo));
        }
    }

    private void createTableaux() {
        for (int i = 0; i < 7; i++) {
            TableauDeck tableauDeck = new TableauDeck();
            tableauDeck.inicializar(drawPile.getCartas(i + 1));
            tableau.add(tableauDeck);
        }
    }

    public DrawPile getDrawPile() {
        return drawPile;
    }

    public ArrayList<TableauDeck> getTableau() {
        return tableau;
    }

    public WastePile getWastePile() {
        return wastePile;
    }

    public FoundationDeck getLastFoundationUpdated() {
        return lastFoundationUpdated;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        // add foundations
        str.append("Foundation\n");
        for (FoundationDeck foundationDeck : foundation) {
            str.append(foundationDeck);
            str.append("\n");
        }

        // add tableaux
        str.append("\nTableaux\n");
        int tableauNumber = 1;
        for (TableauDeck tableauDeck : tableau) {
            str.append(tableauNumber + " ");
            str.append(tableauDeck);
            str.append("\n");
            tableauNumber++;
        }
        str.append("Waste\n");
        str.append(wastePile);
        str.append("\nDraw\n");
        str.append(drawPile);
        return str.toString();
    }

    public ArrayList<FoundationDeck> getFoundation() {
        return foundation;
    }
}
