// --== CS400 Fall 2023 File Header Information ==--
// Name: Henish Patel
// Email: hpatel37@wisc.edu
// Group: F24
// TA: ZHUOMING LIU
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Implementation of Hashtable using (implementing) MapADT
 */
public class HashtableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {

  protected class Pair {

    public KeyType key;
    public ValueType value;

    public Pair(KeyType key, ValueType value) {
      this.key = key;
      this.value = value;
    }

  }

  private int size; // number of key-value pairs stored in the HashtableMap
  protected LinkedList<Pair>[] table;

  /**
   * constructor that instantiates array with argument capacity
   * @param capacity maximum number of key-value pairs that can be stored in this HashtableMap
   */
  @SuppressWarnings("unchecked")
  public HashtableMap(int capacity) {
    table = (LinkedList<Pair>[]) new LinkedList[capacity];
  }

  /**
   * Default constructor that instantiates array with default capacity of 32
   */
  public HashtableMap() { // with default capacity = 32
    this(32);
  }


  /**
   * Dynamically grows hashtable by doubling its capacity and rehashing (whenever load factor
   * becomes greater than or equal to 75%)
   */
  @SuppressWarnings("unchecked")
  private void resizeHelper() {
    // save reference to current table and replace table field with new table with double capacity
    LinkedList<Pair>[] oldTable = table;
    table = (LinkedList<Pair>[]) new LinkedList[table.length*2];
    size = 0; // reset size because it will be corrected when pairs are rehashed below
    // iterate through all existing pairs and rehash them
    for (LinkedList<Pair> pairs : oldTable) {
      if (pairs != null) {
        for (Pair pair : pairs) {
          this.put(pair.key, pair.value);
        }
      }
    }
  }

  /**
   * Adds a new key,value pair/mapping to this collection.
   *
   * @param key   the key of the key,value pair
   * @param value the value that key maps to
   * @throws IllegalArgumentException if key already maps to a value
   * @throws NullPointerException     if key is null
   */
  @Override
  public void put(KeyType key, ValueType value) throws IllegalArgumentException {
    // handle cases of duplicate or null keys
    if (key == null) {
      throw new NullPointerException("key is null");
    } else if (this.containsKey(key)) {
      throw new IllegalArgumentException("key already maps to a value");
    }

    int index = Math.abs(key.hashCode()) % table.length; // calculate index to put current key in
    // if this key is the first to be mapped to this index, create new linked list
    if (table[index] == null) {
      table[index] = new LinkedList<>();
    }

    table[index].add(new Pair(key, value)); // insert pair at calculated index
    size++;
    if (((double) size/table.length) >= 0.75) { // check load factor and resize if it is >= 75%
      this.resizeHelper();
    }
  }

  /**
   * Checks whether a key maps to a value in this collection.
   *
   * @param key the key to check
   * @return true if the key maps to a value, and false is the key doesn't map to a value
   */
  @Override
  public boolean containsKey(KeyType key) {
    int index = Math.abs(key.hashCode()) % table.length; // calculate index of current key
    if (table[index] != null) { // check if there is linked list at mapped index
      // iterate through all pairs at mapped index of key and check if any of them contain key
      for (Pair pair : table[index]) {
        if (pair.key.equals(key)) {
          return true;
        }
      }
    }
    // reaching this point means key wasn't found
    return false;
  }

  /**
   * Retrieves the specific value that a key maps to.
   *
   * @param key the key to look up
   * @return the value that key maps to
   * @throws NoSuchElementException when key is not stored in this collection
   */
  @Override
  public ValueType get(KeyType key) throws NoSuchElementException {
    int index = Math.abs(key.hashCode()) % table.length; // calculate index of current key
    if (table[index] != null) { // check if there is linked list at mapped index
      // iterate through all pairs at (hash) index of key until argument key is found
      for (Pair pair : table[index]) {
        if (pair.key.equals(key)) {
          return pair.value; // return value paired with key
        }
      }
    }
    // reaching this point means the argument key was not found in this collection
    throw new NoSuchElementException("key is not stored in this collection");
  }

  /**
   * Remove the mapping for a key from this collection.
   *
   * @param key the key whose mapping to remove
   * @return the value that the removed key mapped to
   * @throws NoSuchElementException when key is not stored in this collection
   */
  @Override
  public ValueType remove(KeyType key) throws NoSuchElementException {
    int index = Math.abs(key.hashCode()) % table.length; // calculate index of current key
    LinkedList<Pair> pairs = table[index];
    if (table[index] != null) { // check if there is linked list at mapped index
      // iterate through all pairs in linked list at (hash) index of key in table until key is found
      for (Pair pair : pairs) {
        if (pair.key.equals(key)) {
          // remove the pair, decrement size and return corresponding value
          size--;
          table[index].remove(pair);
          return pair.value;
        }
      }
    }
    // if we reach this point, key was not in this collection
    throw new NoSuchElementException("key is not stored in this collection");
  }

  /**
   * Removes all key,value pairs from this collection.
   */
  @Override
  public void clear() {
    size = 0;
    for (int i = 0; i < table.length; i++) {
      table[i] = null;
    }
  }

  /**
   * Retrieves the number of keys stored in this collection.
   *
   * @return the number of keys stored in this collection
   */
  @Override
  public int getSize() {
    return size;
  }

  /**
   * Retrieves this collection's capacity.
   *
   * @return the size of te underlying array for this collection
   */
  @Override
  public int getCapacity() {
    return table.length;
  }

  /**
   * Tests the basic getters (getSize and getCapacity) after insertion and removal of keys
   */
  @Test
  public void testSimpleGetters() {
    HashtableMap<String, Integer> map = new HashtableMap<>();
    map.put("1", 1);
    map.put("2", 2);
    map.put("3", 3);
    map.put("4", 4);

    Assertions.assertEquals(32, map.getCapacity());
    Assertions.assertEquals(4, map.getSize());
    // remove a key and ensure getCapacity and getSize function as expected
    map.remove("1");

    Assertions.assertEquals(32, map.getCapacity());
    Assertions.assertEquals(3, map.getSize());
  }

  /**
   * Tests the resizing of map and that it resize load factor is 75%
   */
  @Test
  public void testResizing() {
    HashtableMap<String, Integer> map = new HashtableMap<>(4);
    map.put("test", 1);
    map.put("hello", 2);

    // check capacity before resizing
    Assertions.assertEquals(4, map.getCapacity());

    map.put("test2", 9);
    map.put("test3", 8);

    // check capacity after resizing
    Assertions.assertEquals(8, map.getCapacity());
    // ensure they rehashed correctly
    Assertions.assertEquals(4, map.getSize());
    Assertions.assertEquals(1, map.get("test"));
    Assertions.assertEquals(2, map.get("hello"));
    Assertions.assertEquals(9, map.get("test2"));
    Assertions.assertEquals(8, map.get("test3"));
  }

  /**
   * Tests different cases with the put method:
   *   Case 1: duplicate key insertion (expect IllegalArgumentException to be thrown)
   *   Case 2: invalid key (null key, expect NullPointerException to be thrown)
   */
  @Test
  public void testPutCases() {
    // case 1:
    {
      HashtableMap<String, Integer> map = new HashtableMap<>();
      map.put("test", 1);
      // duplicate key value: expect IllegalArgumentException
      Assertions.assertThrows(
          IllegalArgumentException.class,
          () -> map.put("test", 2)
      );
      // add different key-value pair
      map.put("hello", 3);
      // duplicate key value: expect IllegalArgumentException
      Assertions.assertThrows(
          IllegalArgumentException.class,
          () -> map.put("hello", 3)
      );
    }
    // case 2:
    {
      HashtableMap<String, Integer> map = new HashtableMap<>();
      map.put("test", 1);
      Assertions.assertThrows(
          NullPointerException.class,
          () -> map.put(null, 2)
      );
    }
  }

  /**
   * Tests containsKey method in a map
   */
  @Test
  public void testContainsKey() {
    HashtableMap<Integer, String> map = new HashtableMap<>();
    map.put(1, "1");
    map.put(5, "5");
    map.put(15, "15");
    map.put(20, "20");
    map.put(30, "30");
    // test with keys that actually exist in the map
    Assertions.assertTrue(map.containsKey(5));
    Assertions.assertTrue(map.containsKey(30));
    // test with keys that do not exist in the map
    Assertions.assertFalse(map.containsKey(2));
    Assertions.assertFalse(map.containsKey(10));
  }

  /**
   * Tests get on a large map (testing get on values in the map and not in the map)
   */
  @Test
  public void testGetLargeMap() {
    HashtableMap<Integer, String> map = new HashtableMap<>();
    // add bunch of keys
    for (int i = 0; i < 1000; i++) {
      map.put(i, Integer.toString(i));
    }
    // test get on keys that ARE in the map
    for (int i = 0; i < 1000; i++) {
      Assertions.assertEquals(Integer.toString(i), map.get(i));
    }
    // test get on keys that are NOT in the map
    Assertions.assertThrows(
        NoSuchElementException.class,
        () -> map.get(-1)
    );
    Assertions.assertThrows(
        NoSuchElementException.class,
        () -> map.get(1000)
    );
  }

  /**
   * Tests remove on a large map (ensuring that it returns the correct value, that is gets removed,
   * and that removing keys that aren't in map throw the correct error)
   */
  @Test
  public void testRemoveLargeMap() {
    HashtableMap<Integer, String> map = new HashtableMap<>();
    // add bunch of keys
    for (int i = 0; i < 1000; i++) {
      map.put(i, Integer.toString(i));
    }
    // remove keys that are in map
    for (int i = 0; i < 1000; i++) {
      Assertions.assertEquals(Integer.toString(i), map.remove(i));
    }
    // check that the keys were indeed removed
    for (int i = 0; i < 1000; i++) {
      Assertions.assertFalse(map.containsKey(i));
    }
    // remove keys that are NOT in map
    Assertions.assertThrows(
        NoSuchElementException.class,
        () -> map.remove(-1)
    );
    Assertions.assertThrows(
        NoSuchElementException.class,
        () -> map.remove(1000)
    );
  }

  /**
   * Tests the clear method to ensure all fields are updated correctly
   */
  @Test
  public void testClear() {
    HashtableMap<String, Integer> map = new HashtableMap<>(4);
    map.put("1", 1);
    map.put("2", 2);
    map.put("3", 3);
    map.put("4", 4);
    // clear map
    map.clear();
    // ensure capacity remained the same
    Assertions.assertEquals(8, map.getCapacity());
    // ensure the key-value pairs were actually removed
    Assertions.assertEquals(0, map.getSize());
    Assertions.assertFalse(map.containsKey("1"));
    Assertions.assertFalse(map.containsKey("2"));
    Assertions.assertFalse(map.containsKey("3"));
    Assertions.assertFalse(map.containsKey("4"));
  }
}
