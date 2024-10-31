package edu.vt.ece.hw5.sets;

public class LazySet<T> implements Set<T> {
    private final Node<T> head;

    public LazySet() {
        head = new Node<>(Integer.MIN_VALUE);
        head.next = new Node<>(Integer.MAX_VALUE);
    }

    private boolean validate(Node<T> pred, Node<T> curr) {
        /* YOUR IMPLEMENTATION HERE */
    }

    @Override
    public boolean add(T item) {
        /* YOUR IMPLEMENTATION HERE */
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
        private boolean marked;

        public Node(U item, Node<U> next) {
            this.key = item.hashCode();
            this.next = next;
            this.marked = false;
        }

        public Node(int key) {
            this.key = key;
            next = null;
            this.marked = false;
        }

        public void mark() {
            marked = true;
        }

        public boolean isMarked() {
            return marked;
        }
    }
}
