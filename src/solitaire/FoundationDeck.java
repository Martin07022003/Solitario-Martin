package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;

/**
 * Modela un monículo donde se ponen las cartas
 * de un solo palo.
 *
 * @author Cecilia M. Curlango
 * @version 2025
 */
public class FoundationDeck {
    Palo palo;
    Pila<CartaInglesa> cartas = new Pila<>(13); // Capacidad máxima de un palo

    public FoundationDeck(Palo palo) {
        this.palo = palo;
    }

    public FoundationDeck(CartaInglesa carta) {
        palo = carta.getPalo();
        if (carta.getValorBajo() == 1) {
            cartas.push(carta);
        }
    }

    /**
     * Agrega una carta al montículo. Sólo la agrega si
     * la carta es del palo del montículo y el la siguiente
     * carta en la secuencia.
     *
     * @param carta que se intenta almancenar
     * @return true si se pudo guardar la carta, false si no
     */
    public boolean agregarCarta(CartaInglesa carta) {
        boolean agregado = false;
        if (carta.tieneElMismoPalo(palo)) {
            if (cartas.pilaVacia()) {
                if (carta.getValorBajo() == 1) {
                    cartas.push(carta);
                    agregado = true;
                }
            } else {
                CartaInglesa ultimaCarta = cartas.verTope();
                if (ultimaCarta.getValorBajo() + 1 == carta.getValorBajo()) {
                    cartas.push(carta);
                    agregado = true;
                }
            }
        }
        return agregado;
    }

    /**
     * Remover la última carta del montículo.
     *
     * @return la carta que removió, null si estaba vacio
     */
    CartaInglesa removerUltimaCarta() {
        return cartas.pop();
    }

    @Override
    public String toString() {
        if (cartas.pilaVacia()) {
            return "---";
        }
        return cartas.verTope().toString(); // En fundaciones basta con ver la cima en el toString
    }

    public boolean estaVacio() {
        return cartas.pilaVacia();
    }

    public CartaInglesa getUltimaCarta() {
        return cartas.verTope();
    }
}
