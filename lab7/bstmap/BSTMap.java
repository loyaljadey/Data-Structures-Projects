package bstmap;

import java.util.Iterator;
import java.util.Set;

/** A data structure that uses a binary search tree map to store pairs of keys and values.
 *  Key operations are get(key), put(key, value), and contains(key) methods. The value
 *  associated to a key is the value in the last call to put with that key.
 * @author Michael Jia
 *  */

public class BSTMap<K extends Comparable<K>, V>  implements Map61B<K, V> {

    private Node root;
    private int size;

    private class Node {
        //each node has a key and a value associated with it
        //it also keeps track of the left and right nodes
        private K key;
        private V value;
        private Node left;
        private Node right;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    //empty bstmap at the beginning since there is nothing to put in it just yet
    public BSTMap() {
    }

    //setting the root to null means that the pointer to the old BSTMap is lost and
    //the rest of the BSTMap is cleaned up by the garbage collector
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    //checks if this node is in the tree
    @Override
    public boolean containsKey(K key) {
        return containsHelper(root, key);
    }
    //recursive helper for contains
    private boolean containsHelper(Node node, K key) {
        //base case
        if (node == null) {
            return false;
        }
        if (key.equals(node.key)) {
            return true;
        }
        else if (key.compareTo(node.key) < 0) {
            return containsHelper(node.left, key);
        }
        else {
            return containsHelper(node.right, key);
        }
    }

    //gets the value of the node with the specific key
    @Override
    public V get(K key) {
        if (containsKey(key)) {
            return getHelper(root,key);
        }
        return null;
    }
    //helper method for get that does the recursion
    private V getHelper(Node node, K key) {
        //base case
        if (node == null) {
            return null;
        }
        if (key.equals(node.key)) {
            return node.value;
        }
        else if (key.compareTo(node.key) < 0) {
            return getHelper(node.left, key);
        }
        else {
            return getHelper(node.right, key);
        }
    }

    //returns the size of the root node
    //size is size of all the nodes
    @Override
    public int size() {
        return size;
    }

    //putting a new node in
    @Override
    public void put(K key, V value) {
        root = putHelper(root, key, value);
    }
    //helper method for the put that does the recursion
    private Node putHelper(Node node, K key, V value) {
        //base case
        if (node == null) {
            size += 1;
            return new Node(key,value);
        }
        if (key.compareTo(node.key) < 0) {
            node.left = putHelper(node.left, key, value);
        }
        else if (key.compareTo(node.key) > 0){
            node.right = putHelper(node.right, key, value);
        }
        return node;
    }

    //prints the BSTMap in order
    public void printInOrder() {
        
    }

    //helper to find the smallest key

    //helper to find the largest key



    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}
