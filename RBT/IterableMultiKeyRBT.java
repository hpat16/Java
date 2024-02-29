import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

/**
 * Iterable Red-Black Tree implementation that allows the storage of duplicate keys, and allows
 * users to iterate through the red-black tree with in-order traversal (smallest to largest)
 */
public class IterableMultiKeyRBT<T extends Comparable<T>> extends RedBlackTree<KeyListInterface<T>>
    implements IterableMultiKeySortedCollectionInterface<T>  {

  private Comparable<T> startPoint; // stores the iteration start point
  private int numKeys; // stores the number of keys in tree
  /**
   * Inserts value into tree that can store multiple objects per key by keeping lists of objects in
   * each node of the tree.
   *
   * @param key object to insert
   * @return true if a new node was inserted, false if the key was added into an existing node
   */
  @Override
  public boolean insertSingleKey(T key) {
    // creates a KeyList with the new key
    KeyList<T> keyList = new KeyList<>(key);
    /* holds the reference to (a node that contains) any duplicate KeyLists that already contain the
       key to insert */
    Node<KeyListInterface<T>> duplicate = this.findNode(keyList);
    // increment numKeys
    numKeys++;
    /* if duplicate is null, then no KeyLists exist with the same key, so insert a new KeyList with
       this new key */
    if (duplicate == null) {
      this.insert(keyList);
      return true;
    }
    // if duplicate holds a valid reference, then add key to that pre-existing KeyList
    else {
      duplicate.data.addKey(key);
      return false;
    }
  }

  /**
   * @return the number of values in the tree.
   */
  @Override
  public int numKeys() {
    return numKeys;
  }

  /**
   * Returns an instance of java.util.Stack containing nodes after initialization.
   * If no iteration start point is set (the field that stores the start point is set to null), the
   * stack is initialized with the nodes on the path from the root node to (and including) the node
   * with the smallest key in the tree. If the iteration start point is set, then the stack is
   * initialized with all the nodes with keys equal to or larger than the start point along the path
   * of the search for the start point.
   *
   * @return returns an instance of java.util.Stack containing nodes after initialization
   */
  protected Stack<Node<KeyListInterface<T>>> getStartStack() {
    Stack<Node<KeyListInterface<T>>> stack = new Stack<>();
    // reference to root node that is used for the purpose of iterating through the tree
    Node<KeyListInterface<T>> current = this.root;
    /* if no iteration start point is set, initialize stack with the nodes on the path from the root
       node to (and including) the node with the smallest key in the tree */
    if (startPoint == null) {
      while (current != null) {
        stack.push(current);
        current = current.down[0];
      }
    }
    /* If the iteration start point is set, then the stack is initialized with all the nodes with
       keys equal to or larger than the start point along the path of the search for the start
       point. */
    else {
      while(current != null) {
        int compare = startPoint.compareTo(current.data.iterator().next());
        /* if startPoint is less than or equal to the current item in the iteration (so current
           node's keys equal or are larger than the start point) then push item to stack and move to
           current node's left child */
        if (compare <= 0) {
          stack.push(current);
          current = current.down[0];
        }
        /* current node's keys are less than the start point, then don't push to stack and next node
           is the right child of current node */
        else {
          current = current.down[1];
        }
      }
    }

    return stack;
  }
  /**
   * Returns an iterator that does an in-order iteration over the tree.
   */
  @Override
  public Iterator<T> iterator() {
    // anonymous class to create a new iterator object
    Iterator<T> iterator = new Iterator<T>() {
      // creates and stores the initial stack for the iterator
      private Stack<Node<KeyListInterface<T>>> initialStack = getStartStack();
      // iterator that allows iteration through all duplicate keys (if any)
      private Iterator<T> currKeyListIterator = null;

      /**
       * Returns true if the iteration has more elements. (In other words, returns true if next()
       * would return an element rather than throwing an exception.)
       * @return true if the iteration has more elements
       */
      @Override
      public boolean hasNext() {
	  if (!initialStack.empty()) { // if stack isn't empty, clearly more elements left
          return true;
        }
	/* if stack is empty and the current key had no more duplicates left, then no more
	   items left to iterate through */
	else if (currKeyListIterator == null || !currKeyListIterator.hasNext()) {
          return false;
        }
	/* if stack is empty but currKeylist has more items, then more items left to iterate
	  through */  
	else {
          return true;
        }
      }

      /**
       * Returns the next element in the iteration.
       * @return the next element in the iteration
       * @throws NoSuchElementException if the iteration has no more elements
       */
      @Override
      public T next() throws NoSuchElementException {
        if (!this.hasNext()) {
          throw new NoSuchElementException();
        }
        /* if this is the start of the iterator or there are no more elements in the current keyList
           then and push all nodes on the path from the current node to (and including) the smallest
           key in current node's subtree to the stack */
        if (currKeyListIterator == null || !currKeyListIterator.hasNext()) {
          // saves reference to current node so it isn't lost after popping it off the stack
          Node<KeyListInterface<T>> currNode = initialStack.pop();
          // iterator that iterates through current node's KeyList
          currKeyListIterator = currNode.data.iterator();
          /* this finds and pushes all nodes on path from current node to (and including) the
             smallest node in the current node's subtree to the stack */
          currNode = currNode.down[1];
          while (currNode != null) {
            initialStack.push(currNode);
            currNode = currNode.down[0];
          }
        }

        return currKeyListIterator.next();
      }
    };

    return iterator;
  }

  /**
   * Sets the starting point for iterations. Future iterations will start at the starting point or
   * the key closest to it in the tree. This setting is remembered until it is reset. Passing in
   * null disables the starting point.
   *
   * @param startPoint the start point to set for iterations
   */
  @Override
  public void setIterationStartPoint(Comparable<T> startPoint) {
    this.startPoint = startPoint;
  }

  /**
   * Removes all keys from the tree.
   */
  public void clear() {
    super.clear();
    numKeys = 0;
  }

  /**
   * This test ensures that duplicate keys are handled in the correct manner and are stored as
   * expected. It compares the number of actual keys to the number of keylist nodes to ensure this.
   * Case 1: Multiple duplicate keys (tree size > 1)
   * Case 2: Single keylist with duplicates (tree size = 1)
   */
  @Test
  public void testDuplicateKeys() {
    // case 1: Multilple different duplicate keys (tree size > 1)
    {
      IterableMultiKeyRBT<Double> tree = new IterableMultiKeyRBT<>();
      // check to ensure that empty tree iterator does nothing
      for (Double key : tree) {
        Assertions.fail();
      }
      // insert items
      tree.insertSingleKey(14.4);
      tree.insertSingleKey(14.4);
      tree.insertSingleKey(29.9);
      tree.insertSingleKey(29.9);
      tree.insertSingleKey(72.2);
      tree.insertSingleKey(82.3);
      tree.insertSingleKey(82.4);
      tree.insertSingleKey(93.0);
      // ensures that duplicates were stored in the same keylist node, while the rest have their own
      // distinct nodes
      Assertions.assertEquals(6, tree.size());
      Assertions.assertEquals(8, tree.numKeys());
      // the correct keys that should be returned
      Double[] expected = new Double[] {14.4, 14.4, 29.9, 29.9, 72.2, 82.3, 82.4, 93.0};
      // loop to ensure that the correct values were stored and that the iterator handles duplicates
      // correctly
      int i = 0;
      for (Double key : tree) {
        Assertions.assertEquals(expected[i++], key);
      }
      // Expect to have gone through all elements
      Assertions.assertEquals(8, i);
    }
    // Single keylist with duplicate keys (tree size = 1)
    {
      IterableMultiKeyRBT<Double> tree = new IterableMultiKeyRBT<>();
      tree.insertSingleKey(14.4);
      tree.insertSingleKey(14.4);
      tree.insertSingleKey(14.4);
      tree.insertSingleKey(14.4);
      // ensures that duplicates were stored in the same keylist node, while the rest have their own
      // distinct nodes
      Assertions.assertEquals(1, tree.size());
      Assertions.assertEquals(4, tree.numKeys());
      // the correct keys that should be returned
      Double[] expected = new Double[] {14.4, 14.4, 14.4, 14.4};
      // loop to ensure that the correct values were stored and that the iterator handles duplicates
      // correctly
      int i = 0;
      for (Double key : tree) {
        Assertions.assertEquals(expected[i++], key);
      }
      // Expect to have gone through all elements
      Assertions.assertEquals(4, i);
    }
  }

  /**
   * This tests ensures that the iterator iterates over the tree in the correct order (in order
   * traversal) when the arguments are inserted in a random manner.
   */
  @Test
  public void testIteratorOrder() {
    IterableMultiKeyRBT<Double> tree = new IterableMultiKeyRBT<>();
    // inserting values in random order
    tree.insertSingleKey(14.4);
    tree.insertSingleKey(93.2);
    tree.insertSingleKey(29.9);
    tree.insertSingleKey(63.5);
    tree.insertSingleKey(14.4);
    tree.insertSingleKey(723.4);
    tree.insertSingleKey(1.0);
    tree.insertSingleKey(14.49);
    // the proper in order traversal of the inserted keys above
    Double[] expected = new Double[] {1.0, 14.4, 14.4, 14.49, 29.9, 63.5, 93.2, 723.4};
    Assertions.assertEquals(expected.length, tree.numKeys);
    int i = 0; // value to assist when comparing expected to actual in enhanced for loop
    // ensures the iterators iterate in the correct order when compared to the expected array
    for (Double key : tree) {
      Assertions.assertEquals(expected[i++], key);
    }
    // Expect to have gone through all elements
    Assertions.assertEquals(8, i);
  }

  /**
   * This test tests the setIterationStartPoint() method by ensuring that the following three cases
   * function as expected:
   *    Case 1: Passing a value that isn't in the tree
   *    Case 2: Passing a value that is in the tree
   *    Case 3: Passing null to reset the iteration point
   */
  @Test
  public void testIteratorStartPoint() {
    IterableMultiKeyRBT<Double> tree = new IterableMultiKeyRBT<>();
    tree.insertSingleKey(23.9);
    tree.insertSingleKey(942.3);
    tree.insertSingleKey(88.8);
    tree.insertSingleKey(53.4);
    tree.insertSingleKey(88.8);
    tree.insertSingleKey(53.4);
    tree.insertSingleKey(121.8);
    tree.insertSingleKey(831.1);
    // case 1: expect it to start at the next largest value (that is closest to the given argument)
    tree.setIterationStartPoint(24.0);
    // the proper in order traversal of the inserted keys above (accounting for setIteration)
    Double[] expected1 = new Double[] {53.4, 53.4, 88.8, 88.8, 121.8, 831.1, 942.3};
    int i = 0; // value to assist when comparing expected to actual in enhanced for loop
    // ensures the iterators iterate in the correct order when compared to the expected array
    for (Double key : tree) {
      Assertions.assertEquals(expected1[i++], key);
    }
    // Expect to have gone through all elements
    Assertions.assertEquals(7, i);
    // case 2: expect it to start exactly from 88.8
    tree.setIterationStartPoint(88.8);
    // the proper in order traversal of the inserted keys above (accounting for setIteration)
    Double[] expected2 = new Double[] {88.8, 88.8, 121.8, 831.1, 942.3};
    i = 0; // value to assist when comparing expected to actual in enhanced for loop
    // ensures the iterators iterate in the correct order when compared to the expected array
    for (Double key : tree) {
      Assertions.assertEquals(expected2[i++], key);
    }
    // Expect to have gone through all elements
    Assertions.assertEquals(5, i);
    // case 3: expect it to reset the iteration to the in order traversal (so smallest value first)
    tree.setIterationStartPoint(null);
    // the proper in order traversal of the inserted keys above (accounting for setIteration)
    Double[] expected3 = new Double[] {23.9, 53.4, 53.4, 88.8, 88.8, 121.8, 831.1, 942.3};
    i = 0; // value to assist when comparing expected to actual in enhanced for loop
    // ensures the iterators iterate in the correct order when compared to the expected array
    for (Double key : tree) {
      Assertions.assertEquals(expected3[i++], key);
    }
    // Expect to have gone through all elements
    Assertions.assertEquals(8, i);
  }

  @Test
  public void testRubric1() {
    IterableMultiKeyRBT<Double> tree = new IterableMultiKeyRBT<>();
    //
    tree.insertSingleKey(50.0);
    tree.insertSingleKey(50.0);
    tree.insertSingleKey(100.0);
    tree.insertSingleKey(100.0);
    tree.insertSingleKey(150.0);
    tree.insertSingleKey(150.0);
    // the proper in order traversal of the inserted keys above
    Double[] expected = new Double[] {50.0, 50.0, 50.0, 50.0, 100.0, 100.0, 100.0, 100.0, 150.0, 150.0, 150.0, 150.0};
    Iterator<Double> it = tree.iterator();
    Iterator<Double> it2 = tree.iterator();
    int i = 0;
    while(it.hasNext() && it2.hasNext()) {
      Assertions.assertEquals(expected[i++], it.next());
      Assertions.assertEquals(expected[i++], it2.next());
    }

    Assertions.assertFalse(it.hasNext());
    Assertions.assertFalse(it2.hasNext());
    Assertions.assertEquals(expected.length, i);
  }

  @Test
  public void testRubric2() {
    IterableMultiKeyRBT<Double> tree = new IterableMultiKeyRBT<>();
    //
    for (double i = 0; i < 1000; i++) {
      tree.insertSingleKey(i);
      tree.insertSingleKey(i);
      tree.insertSingleKey(i);
    }
    // the proper in order traversal of the inserted keys above
    ArrayList<Double> expected = new ArrayList<>();
    for (double i = 0; i < 1000; i++) {
      expected.add(i);
      expected.add(i);
      expected.add(i);
    }

    Assertions.assertEquals(expected.size(), tree.numKeys);
    int i = 0;
    for (Double key : tree) {
      Assertions.assertEquals(expected.get(i++), key);
    }
    // Expect to have gone through all elements
    Assertions.assertEquals(3000, i);
  }
}
