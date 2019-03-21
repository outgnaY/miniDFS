package Util;

public class Pair<K, V> {
	public static void main(String[] args) {
		//Pair class test.
		Pair<Integer, Integer> p = new Pair<>(2, 3);
		System.out.println(p.getKey());
		System.out.println(p.getValue());
	}
	private K key;
	public K getKey() {
		return key;
	}
	private V value;
	public V getValue() {
		return value;
	}
	public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
