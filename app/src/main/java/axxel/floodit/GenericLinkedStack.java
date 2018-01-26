package axxel.floodit;

import java.util.EmptyStackException;

/**
 * Implementation of GenericLinkedStack
 * @author Yassine Zahoui
 */
 
public class GenericLinkedStack<E> implements Stack<E> {

    private static class Elem<T> {
        private T value;
        private Elem<T> next;

        private Elem(T value, Elem<T> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Elem<E> top;

    public boolean isEmpty() {
        return top == null;
    }

    public void push(E value) throws NullPointerException {
		if (value==null) { throw new NullPointerException(); }
        top = new Elem<E>(value, top);
    }

    public E peek() throws EmptyStackException {
		if (isEmpty()) {throw new EmptyStackException();}
        return top.value;
    }

    public E pop() throws EmptyStackException {
		if (isEmpty()) {throw new EmptyStackException();}
        E saved = top.value;
        top = top.next;
        return saved;
    }
}