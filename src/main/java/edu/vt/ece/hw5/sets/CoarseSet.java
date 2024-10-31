package edu.vt.ece.hw5.sets;

public class CoarseSet<T> implements Set<T>{
    private final Node<T> head;

    public CoarseSet() {
        head = new Node<>(Integer.MIN_VALUE);
        head.next = new Node<>(Integer.MAX_VALUE);
    }

    @Override
    public boolean add(T item) {
        /* YOUR IMPLEMENTATION HERE */
        
        /* Hint: Look into object type's hashCode() method. */
        /* Hint: Look into synchronized methods. */
    }

    @Override
    public boolean remove(T item) {
        /* YOUR IMPLEMENTATION HERE */
    }

    @Override
    public boolean contains(T item) {
        /* YOUR IMPLEMENTATION HERE */
    }

    private static class Node<U> {
        int key;
        Node<U> next;

        public Node(U item, Node<U> next) {
            this.key = item.hashCode();
            this.next = next;
        }

        public Node(int key) {
            this.key = key;
            next = null;
        }
    }
    }
}
