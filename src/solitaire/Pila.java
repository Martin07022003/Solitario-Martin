package solitaire;

public class Pila<T> {
    private T[] elementos;
    private int tope;
    private int capacidad;

    // Constructor: T() Pila
    public Pila(int capacidad) {
        this.capacidad = capacidad;
        this.elementos = (T[]) new Object[capacidad];
        this.tope = -1;
    }

    // Insertar dato: push(t dato)
    public void push(T dato) {
        if (pilaLlena()) {
            System.out.println("Desbordamiento");
            return;
        }
        elementos[++tope] = dato;
    }

    // Sacar dato: t pop()
    public T pop() {
        if (pilaVacia()) {
            System.out.println("Subdesbordamiento");
            return null;
        }
        T dato = elementos[tope];
        elementos[tope--] = null;
        return dato;
    }

    // Regresa el elemento del tope sin eliminarlo
    public T verTope() {
        if (pilaVacia()) return null;
        return elementos[tope];
    }

    public boolean pilaLlena() {
        return tope == elementos.length - 1;
    }

    public boolean pilaVacia() {
        return tope == -1;
    }
}