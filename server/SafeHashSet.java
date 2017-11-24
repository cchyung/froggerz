package froggerz.server;

import java.util.HashSet;

/**
 * Provides a thread safe interface for a HashSet
 */
public class SafeHashSet<E> {
	private HashSet<E> hashset;
	
	/**
	 * Constructor
	 * @param initialCapacity The initial capacity of the SafeHashSet
	 */
	public SafeHashSet(int initialCapacity) {
		hashset = new HashSet<E>(initialCapacity);
	}
	
	/**
	 * Constructor with default capacity
	 */
	public SafeHashSet() {
		hashset = new HashSet<E>();
	}
	
	/**
	 * @param e Variable to add to the SafeHashSet
	 */
	public synchronized void add(E e) {
		hashset.add(e);
	}
	
	/**
	 * @param e Variable to remove from the SafeHashSet
	 */
	public synchronized void remove(E e) {
		hashset.remove(e);
	}
	
	/**
	 * @param e Variable to check for in the SafeHashSet
	 * @return true if this SafeHashSet contains e, false otherwise
	 */
	public synchronized boolean contains(E e) {
		return hashset.contains(e);
	}
}
