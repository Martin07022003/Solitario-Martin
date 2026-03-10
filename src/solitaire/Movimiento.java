package solitaire;

import DeckOfCards.CartaInglesa;
import java.util.ArrayList;

public class Movimiento {
    // Estructuras para restaurar el estado
    public Pila<CartaInglesa> copiaDraw;
    public Pila<CartaInglesa> copiaWaste;
    public ArrayList<CartaInglesa>[] copiaTableaux;
    public ArrayList<CartaInglesa>[] copiaFoundations;

    @SuppressWarnings("unchecked")
    public Movimiento(DrawPile dp, WastePile wp, ArrayList<TableauDeck> td, ArrayList<FoundationDeck> fd) {
        // Clonar Pilas usando el metodo auxiliar
        this.copiaDraw = clonarPila(dp.getCartasInternas());
        this.copiaWaste = clonarPila(wp.getCartasInternas());

        this.copiaTableaux = (ArrayList<CartaInglesa>[]) new ArrayList[7];
        this.copiaFoundations = (ArrayList<CartaInglesa>[]) new ArrayList[4];

        // Clonar las 7 columnas del Tableau
        for (int i = 0; i < 7; i++) {
            // Especificamos el tipo <CartaInglesa> para que Java no tenga dudas
            this.copiaTableaux[i] = new ArrayList<CartaInglesa>(td.get(i).getCards());
        }

        // Clonar las 4 Fundaciones
        for (int i = 0; i < 4; i++) {
            // Usamos el metodo que devuelve la lista de la pila interna
            this.copiaFoundations[i] = new ArrayList<CartaInglesa>(fd.get(i).getCardsInternasParaCopia());
        }
    }

    /**
     * Algoritmo de clonación de Pilas:
     * Usa una pila auxiliar para duplicar los datos sin perder el orden LIFO.
     */
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
