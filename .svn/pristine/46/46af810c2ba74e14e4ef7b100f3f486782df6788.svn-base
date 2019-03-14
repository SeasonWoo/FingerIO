package com.example.monster.airgesture;


import java.util.LinkedList;

/**
 * SynchLinkedList 线程安全的 LinkedList
 */
@SuppressWarnings("all")
public class SynchLinkedList<T> {
    private LinkedList<T> list = null;
    private final Object lock = new Object();

    public SynchLinkedList() {
        list = new LinkedList<>();
    }

    public void push(T object) {
        synchronized (lock) {
            list.add(object);
            lock.notify();
        }
    }

    public void front(T object) {
        synchronized (lock) {
            list.addFirst(object);
            lock.notify();
        }
    }

    public T pop(boolean wait) {
        if (wait) {
            T item = null;
            synchronized (lock) {
                if (list.size() <= 0) {
                    try {
                        this.lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (list.size() > 0)
                    item = list.removeFirst();
            }
            return item;
        } else {
            synchronized (lock) {
                if (list.size() > 0)
                    return list.removeFirst();
            }
            return null;
        }
    }

    public LinkedList<T> removeAll() {
        synchronized (lock) {
            if (list.size() <= 0)
                return null;
            LinkedList<T> ret= (LinkedList<T>) list.clone();
            list.clear();
            return ret;
        }
    }

    public int size() {
        synchronized (lock) {
            return list.size();
        }
    }

    public void notifyA() {
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void clear() {
        synchronized (lock) {
            list.clear();
        }
    }
}
