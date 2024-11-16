package edu.vt.ece.hw5.sets;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FineSet<T> implements Set<T> {
    private final Node<T> head;

    public FineSet() {
        head = new Node<>(Integer.MIN_VALUE);
        head.next = new Node<>(Integer.MAX_VALUE);
    }

    @Override
    public boolean add(T item) {
        int key = item.hashCode();
        head.lock();
        Node<T> pred = head;
        try {
            Node<T> curr = pred.next;
            curr.lock();
            try {
                while (curr.key < key) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }
                if (curr.key == key) {
                    return false;
                } else {
                    Node<T> newNode = new Node<>(item, curr);
                    pred.next = newNode;
                    return true;
                }
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    @Override
    public boolean remove(T item) {
        int key = item.hashCode();
        head.lock();
        Node<T> pred = head;
        try {
            Node<T> curr = pred.next;
            curr.lock();
            try {
                while (curr.key < key) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }
                if (curr.key == key) {
                    pred.next = curr.next;
                    return true;
                } else {
                    return false;
                }
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    @Override
    public boolean contains(T item) {
        int key = item.hashCode();
        head.lock();
        Node<T> curr = head;
        try {
            while (curr.key < key) {
                curr.unlock();
                curr = curr.next;
                curr.lock();
            }
            return (curr.key == key);
        } finally {
            curr.unlock();
        }
    }

    private static class Node<U> {
        final Lock lock = new ReentrantLock();
        final int key;
        Node<U> next;
        final U item;

        public Node(U item, Node<U> next) {
            this.item = item;
            this.key = item.hashCode();
            this.next = next;
        }

        public Node(int key) {
            this.item = null;
            this.key = key;
            this.next = null;
        }

        void lock() { lock.lock(); }
        void unlock() { lock.unlock(); }
    }
}