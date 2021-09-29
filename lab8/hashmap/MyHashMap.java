package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author Michael Jia
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */

    //the backing array for the hashMap
    private Collection<Node>[] buckets;
    //the size of the hashmap (# of Nodes)
    private int numNodes = 0;
    //the loadfactor #of nodes/ # of buckets
    private double loadFactor = 0.75;
    //a HashSet to keep track of the keys of the nodes
    //allows for constant time access to key
    //able to use .contains
    private HashSet<K> keySet = new HashSet<>();



    /** Constructors */
    public MyHashMap() {
        buckets = createTable(16);
    }

    public MyHashMap(int size) {
        buckets = createTable(size);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param size initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int size, double maxLoad) {
        buckets = createTable(size);
        loadFactor = maxLoad;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key,value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        Collection<Node> data = new ArrayList<>();
        return data;
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = createBucket();
        }
        return table;
    }



    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    //clears the table, the keys, and the number of nodes
    @Override
    public void clear() {
        buckets = createTable(16);
        keySet = new HashSet<K>();
        numNodes = 0;
    }

    //checks the set of keys to see if the wanted key is in there
    @Override
    public boolean containsKey(K key) {
        return keySet.contains(key);
    }

    //Associates the specified value with the specified key in this map.
    //If the map previously contained a mapping for the key,
    //the old value is replaced.
    @Override
    public void put(K key, V value) {
        Node n = getNode(key);
        //if the same node with the key exists, then we just want to update the value
        if (n != null) {
            n.value = value;
        } else {
            //check resize condition
            if(numNodes > loadFactor * buckets.length) {
                resize(buckets.length * 2);
            }
            Node insert = createNode(key, value);
            int index = getIndex(key);
            buckets[index].add(insert);
            numNodes += 1;
        }
        keySet.add(key);
    }

    //resize the table that represents the buckets
    private void resize(int newLength) {
        Collection<Node>[] newBucket = createTable(newLength);
        for(K key : keySet) {
            int index = getIndex(key);
            if(getNode(key) != null) {
                newBucket[index].add(getNode(key));
            }
        }
        buckets = newBucket;
    }

    @Override
    //Returns the value to which the specified key is mapped, or null if this
    //map contains no mapping for the key.
    public V get(K key) {
        if(containsKey(key) && getNode(key) != null) {
            return getNode(key).value;
        }
        return null;
    }

    //grabs the node of the specific key
    private Node getNode(K key) {
        int index = getIndex(key);
        for (Node n:buckets[index]) {
            if(n.key.equals(key)) {
                return n;
            }
        }
        return null;
    }

    //converts the key into an index in the table/array that represents the hashMap
    private int getIndex(K key) {
        return Math.floorMod(key.hashCode(), buckets.length);
    }


    //returns the number of nodes in the hashMap
    @Override
    public int size() {
        return numNodes;
    }

    //returns the set of keys
    @Override
    public Set<K> keySet() {
        return keySet;
    }

    //allows for the .contains method
    //allows the user to iterate through the set of keys to find the one that they need
    @Override
    public Iterator<K> iterator() {
        return keySet.iterator();
    }




    @Override
    public V remove (K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

}
