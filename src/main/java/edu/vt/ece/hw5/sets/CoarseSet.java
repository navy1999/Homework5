package edu.vt.ece.hw5.sets;

public class CoarseSet<T> implements Set<T> {
    private final Node<T> head;

    public CoarseSet() {
        head = new Node<>(Integer.MIN_VALUE);
        head.next = new Node<>(Integer.MAX_VALUE);
    }

    @Override
    public synchronized boolean add(T item) {
        int key = item.hashCode();
        Node<T> pred = head;
        Node<T> curr = pred.next;
        while (curr.key < key) {
            pred = curr;
            curr = curr.next;
        }
        if (key == curr.key) {
            return false;
        } else {
            Node<T> newNode = new Node<>(item, curr);
            pred.next = newNode;
            return true;
        }
    }

    @Override
    public synchronized boolean remove(T item) {
        int key = item.hashCode();
        Node<T> pred = head;
        Node<T> curr = pred.next;
        while (curr.key < key) {
            pred = curr;
            curr = curr.next;
        }
        if (key == curr.key) {
            pred.next = curr.next;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized boolean contains(T item) {
        int key = item.hashCode();
        Node<T> curr = head.next;
        while (curr.key < key) {
            curr = curr.next;
        }
        return (key == curr.key);
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