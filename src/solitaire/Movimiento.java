package solitaire;

import DeckOfCards.CartaInglesa;
import java.util.ArrayList;

public class Movimiento {
    public Pila<CartaInglesa> copiaDraw;
    public Pila<CartaInglesa> copiaWaste;
    public ArrayList<CartaInglesa>[] copiaTableaux;
    public ArrayList<CartaInglesa>[] copiaFoundations;

    @SuppressWarnings("unchecked")
    public Movimiento(DrawPile dp, WastePile wp, ArrayList<TableauDeck> td, ArrayList<FoundationDeck> fd) {
        // Clonar Pilas (las cartas dentro también)
        this.copiaDraw = clonarPilaConCartasNuevas(dp.getCartasInternas());
        this.copiaWaste = clonarPilaConCartasNuevas(wp.getCartasInternas());

        this.copiaTableaux = (ArrayList<CartaInglesa>[]) new ArrayList[7];
        this.copiaFoundations = (ArrayList<CartaInglesa>[]) new ArrayList[4];

        // Clonar Tableaus creando cartas nuevas para restaurar correctamente
        for (int i = 0; i < 7; i++) {
            this.copiaTableaux[i] = new ArrayList<CartaInglesa>();
            for (CartaInglesa c : td.get(i).getCards()) {
                this.copiaTableaux[i].add(crearCopiaDeCarta(c));
            }
        }

        // Clonar Fundaciones
        for (int i = 0; i < 4; i++) {
            this.copiaFoundations[i] = new ArrayList<CartaInglesa>();
            for (CartaInglesa c : fd.get(i).getCardsInternasParaCopia()) {
                this.copiaFoundations[i].add(crearCopiaDeCarta(c));
            }
        }
    }

     //Crea un objeto nuevo de CartaInglesa con el mismo estado de visibilidad.
    private CartaInglesa crearCopiaDeCarta(CartaInglesa original) {
        if (original == null) return null;

        int valor = original.getValor();
        DeckOfCards.Palo palo = original.getPalo();
        String representacion = original.toString();

        CartaInglesa copia = new CartaInglesa(valor, palo, representacion);

        // Restaurar visibilidad
        if (original.isFaceup()) {
            copia.makeFaceUp();
        } else {
            copia.makeFaceDown();
        }
        return copia;
    }

        //Pila Auxiliar
    private Pila<CartaInglesa> clonarPilaConCartasNuevas(Pila<CartaInglesa> original) {
        if (original == null) return null;

        Pila<CartaInglesa> auxiliar = new Pila<>(52);
        Pila<CartaInglesa> copia = new Pila<>(52);

        while (!original.pilaVacia()) {
            auxiliar.push(original.pop());
        }

        while (!auxiliar.pilaVacia()) {
            CartaInglesa cartaOriginal = auxiliar.pop();
            original.push(cartaOriginal); // Restaurar original
            copia.push(crearCopiaDeCarta(cartaOriginal)); // Guardar copia nueva
        }

        return copia;
    }
}