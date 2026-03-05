package solitaire;

import DeckOfCards.CartaInglesa;
import java.util.ArrayList;

/**
 * Modela un mazo de cartas de solitario.
 * @author Cecilia Curlango
 * @version 2025
 */
public class DrawPile {
    private Pila<CartaInglesa> cartas; // Sustitución de ArrayList por Pila
    private int cuantasCartasSeEntregan = 3;

    public DrawPile() {
        DeckOfCards.Mazo mazo = new DeckOfCards.Mazo();
        ArrayList<CartaInglesa> listaInicial = mazo.getCartas();
        cartas = new Pila<>(listaInicial.size());
        for (CartaInglesa c : listaInicial) {
            cartas.push(c);
        }
        setCuantasCartasSeEntregan(3);
    }

    /**
     * Establece cuantas cartas se sacan cada vez.
     * Puede ser 1 o 3 normalmente.
     * @param cuantasCartasSeEntregan
     */
    public void setCuantasCartasSeEntregan(int cuantasCartasSeEntregan) {
        this.cuantasCartasSeEntregan = cuantasCartasSeEntregan;
    }

    /**
     * Regresa la cantidad de cartas que se sacan cada vez.
     * @return cantidad de cartas que se entregan
     */
    public int getCuantasCartasSeEntregan() {
        return cuantasCartasSeEntregan;
    }

    /**
     * Retirar una cantidad de cartas. Este metodo se utiliza al inicio
     * de una partida para cargar las cartas de los tableaus.
     * Si se tratan de remover más cartas de las que hay,
     * se provocará un error.
     * @param cantidad de cartas que se quieren a retirar
     * @return cartas retiradas
     */
    public ArrayList<CartaInglesa> getCartas(int cantidad) {
        ArrayList<CartaInglesa> retiradas = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            retiradas.add(cartas.pop()); // Uso de pop()
        }
        return retiradas;
    }

    /**
     * Retira y entrega las cartas del monton. La cantidad que retira
     * depende de cuántas cartas quedan en el montón y serán hasta el máximo
     * que se configuró inicialmente.
     * @return Cartas retiradas.
     */
    public ArrayList<CartaInglesa> retirarCartas() {
        ArrayList<CartaInglesa> retiradas = new ArrayList<>();
        // Asumiendo que tu Pila tiene un metodo para conocer el tamaño o se controla con pilaVacia
        int contador = 0;
        while (!cartas.pilaVacia() && contador < cuantasCartasSeEntregan) {
            CartaInglesa retirada = cartas.pop();
            retirada.makeFaceUp();
            retiradas.add(retirada);
            contador++;
        }
        return retiradas;
    }

    /**
     * Indica si aún quedan cartas para entregar.
     * @return true si hay cartas, false si no.
     */
    public boolean hayCartas() {
        return !cartas.pilaVacia();
    }

    public CartaInglesa verCarta() {
        return cartas.verTope(); // Uso de verTope()
    }

    /**
     * Agrega las cartas recibidas al monton y las voltea
     * para que no se vean las caras.
     * @param cartasAgregar cartas que se agregan
     */
    public void recargar(ArrayList<CartaInglesa> cartasAgregar) {
        cartas = new Pila<>(cartasAgregar.size());
        for (CartaInglesa aCarta : cartasAgregar) {
            aCarta.makeFaceDown();
            cartas.push(aCarta);
        }
    }

    // Metodos para el undo
    public Pila<CartaInglesa> getCartasInternas() {
        return this.cartas;
    }

    public void setCartasInternas(Pila<CartaInglesa> nuevas) {
        this.cartas = nuevas;
    }

    @Override
    public String toString() {
        if (cartas.pilaVacia()) {
            return "-E-";
        }
        return "@";
    }
}
