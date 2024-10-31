package edu.vt.ece.hw5.sets;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class LockFreeSet<T> implements Set<T> {
    private final Node<T> head;

    public LockFreeSet() {
        Node<T> tail = new Node<>(Integer.MAX_VALUE);
        head = new Node<>(Integer.MIN_VALUE, tail);
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
        AtomicMarkableReference<Node<U>> next;

        public Node(U item, Node<U> next) {
            this.key = item.hashCode();
            this.next = new AtomicMarkableReference<>(next, false);
        }

        public Node(int key, Node<U> next) {
            this.key = key;
            this.next = new AtomicMarkableReference<>(next, false);
        }

        public Node(int key) {
            this(key, null);
        }
    }
}