package automata_classes;

public class Pair<T,E> {
	T key;
	E value;
	
	public Pair(T key, E value) {
		this.key = key;
		this.value = value;
	}
	
	public T getKey() { return this.key; }
	
	public E getValue() { return this.value; }
}