package solitaire;

import DeckOfCards.CartaInglesa;

import java.util.ArrayList;
/**
 * Modela el montículo donde se colocan las cartas
 * que se extraen de Draw pile.
 *
 * @author (Cecilia Curlango Rosas)
 * @version (2025-2)
 */

public class WastePile {
    private Pila<CartaInglesa> cartas;

    public WastePile() {
        cartas = new Pila<>(52); // Capacidad máxima de la baraja
    }

    public void addCartas(ArrayList<CartaInglesa> nuevas) {
        for (CartaInglesa c : nuevas) {
            cartas.push(c);
        }
    }

    public ArrayList<CartaInglesa> emptyPile() {
        ArrayList<CartaInglesa> pile = new ArrayList<>();
        while (!cartas.pilaVacia()) {
            pile.add(cartas.pop());
        }
        return pile;
    }

    /**
     * Obtener la última carta sin removerla.
     * @return Carta que está encima. Si está vacía, es null.
     */
    public CartaInglesa verCarta() {
        return cartas.verTope();
    }

    public CartaInglesa getCarta() {
        return cartas.pop();
    }

    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder();
        if (cartas.pilaVacia()) {
            stb.append("---");
        } else {
            CartaInglesa regresar = cartas.verTope();
            regresar.makeFaceUp();
            stb.append(regresar.toString());
        }
        return stb.toString();
    }

    public boolean hayCartas() {
        return !cartas.pilaVacia();
    }
}
