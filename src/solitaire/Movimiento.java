package solitaire;

import DeckOfCards.CartaInglesa;
import java.util.ArrayList;

//Undo
public class Movimiento {
    // Copias de cada estructura
    public Pila<CartaInglesa> copiaDraw;
    public Pila<CartaInglesa> copiaWaste;
    public ArrayList<CartaInglesa>[] copiaTableaux;
    public ArrayList<CartaInglesa>[] copiaFoundations;

    @SuppressWarnings("unchecked")
    public Movimiento(DrawPile dp, WastePile wp, ArrayList<TableauDeck> td, ArrayList<FoundationDeck> fd) {
        // Clonar Pila de DrawPile
        this.copiaDraw = clonarPila(dp.getCartasInternas());

        // Clonar Pila de WastePile
        this.copiaWaste = clonarPila(wp.getCartasInternas());

        // Clonar las 7 columnas del Tableau
        this.copiaTableaux = new ArrayList[7];
        for (int i = 0; i < 7; i++) {

            this.copiaTableaux[i] = new ArrayList<>(td.get(i).getCards());
        }

        // Clonar las 4 Fundaciones
        this.copiaFoundations = new ArrayList[4];
        for (int i = 0; i < 4; i++) {
            this.copiaFoundations[i] = new ArrayList<>(fd.get(i).getCardsInternas());
        }
    }

    private Pila<CartaInglesa> clonarPila(Pila<CartaInglesa> original) {
        if (original == null) return null;

        Pila<CartaInglesa> auxiliar = new Pila<>(52);
        Pila<CartaInglesa> copia = new Pila<>(52);

        // Mover de original a auxiliar (invierte el orden)
        while (!original.pilaVacia()) {
            auxiliar.push(original.pop());
        }

        // Regresar a original y llenar la copia (restaura el orden)
        while (!auxiliar.pilaVacia()) {
            CartaInglesa carta = auxiliar.pop();
            original.push(carta);
            copia.push(carta);
        }

        return copia;
    }
}
