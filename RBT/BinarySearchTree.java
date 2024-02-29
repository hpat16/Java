import java.util.LinkedList;
import java.util.Stack;

/**
 * Binary Search Tree implementation with a Node inner class for representing
 * the nodes of the tree. We will turn this Binary Search Tree into a self-balancing
 * tree as part of project 1 by modifying its insert functionality.
 */
public class BinarySearchTree<T extends Comparable<T>> implements SortedCollectionInterface<T> {

    /**
     * This class represents a node holding a single value within a binary tree.
     */
    protected static class Node<T> {
        public T data;

        // up stores a reference to the node's parent
        public Node<T> up;
        // The down array stores references to the node's children:
        // - down[0] is the left child reference of the node,
        // - down[1] is the right child reference of the node.
        // The @SupressWarning("unchecked") annotation is use to supress an unchecked
        // cast warning. Java only allows us to instantiate arrays without generic
        // type parameters, so we use this cast here to avoid future casts of the
        // node type's data field.
        @SuppressWarnings("unchecked")
        public Node<T>[] down = (Node<T>[])new Node[2];
        public Node(T data) { this.data = data; }
        
        /**
         * @return true when this node has a parent and is the right child of
         * that parent, otherwise return false
         */
        public boolean isRightChild() {
            return this.up != null && this.up.down[1] == this;
        }

    }

    protected Node<T> root; // reference to root node of tree, null when empty
    protected int size = 0; // the number of values in the tree

    /**
     * Inserts a new data value into the tree.
     * This tree will not hold null references, nor duplicate data values.
     * @param data to be added into this binary search tree
     * @return true if the value was inserted, false if is was in the tree already
     * @throws NullPointerException when the provided data argument is null
     */
    public boolean insert(T data) throws NullPointerException {
        if (data == null)
			throw new NullPointerException("Cannot insert data value null into the tree.");
		return this.insertHelper(new Node<>(data));
    }

    /**
     * Performs a naive insertion into a binary search tree: adding the new node
     * in a leaf position within the tree. After this insertion, no attempt is made
     * to restructure or balance the tree.
     * @param newNode the new node to be inserted
     * @return true if the value was inserted, false if is was in the tree already
     * @throws NullPointerException when the provided node is null
     */
    protected boolean insertHelper(Node<T> newNode) throws NullPointerException {
        if(newNode == null) throw new NullPointerException("new node cannot be null");

        if (this.root == null) {
            // add first node to an empty tree
            root = newNode;
            size++;
            return true;
        } else {
            // insert into subtree
            Node<T> current = this.root;
            while (true) {
                int compare = newNode.data.compareTo(current.data);
                if (compare == 0) {
                	return false;
				} else if (compare < 0) {
                    // insert in left subtree
                    if (current.down[0] == null) {
                        // empty space to insert into
                        current.down[0] = newNode;
                        newNode.up = current;
                        this.size++;
                        return true;
                    } else {
                        // no empty space, keep moving down the tree
                        current = current.down[0];
                    }
                } else {
                    // insert in right subtree
                    if (current.down[1] == null) {
                        // empty space to insert into
                        current.down[1] = newNode;
                        newNode.up = current;
                        this.size++;
                        return true;
                    } else {
                        // no empty space, keep moving down the tree
                        current = current.down[1]; 
                    }
                }
            }
        }
    }

    /**
     * Performs the rotation operation on the provided nodes within this tree.
     * When the provided child is a left child of the provided parent, this
     * method will perform a right rotation. When the provided child is a
     * right child of the provided parent, this method will perform a left rotation.
     * When the provided nodes are not related in one of these ways, this method
     * will throw an IllegalArgumentException.
     * @param child is the node being rotated from child to parent position
     *      (between these two node arguments)
     * @param parent is the node being rotated from parent to child position
     *      (between these two node arguments)
     * @throws IllegalArgumentException when the provided child and parent
     *      node references are not initially (pre-rotation) related that way
     */
    protected void rotate(Node<T> child, Node<T> parent) throws IllegalArgumentException {
        if (child == null || parent == null) {
            throw new NullPointerException();
        }
        // throw IllegalArgumentException if provided nodes are not related
        if (child.up != parent) {
            throw new IllegalArgumentException("Nodes unrelated");
        }
        // this value is used later to determine if the rotation should be left or right
        boolean isChildRightChild = child.isRightChild();
        if (parent == root) { // if parent node is the root, we need to update the root data
            // field and the child's up data field accordingly
            root = child;
            child.up = null;
        }
        else { // if parent node is not the root, we need to update the parent node's parent's
            // data field (specifically the down data field)
            if (parent.isRightChild()) { // if the parent node is a right child, we need to
                // update its parent's right child node to the argument child node
                parent.up.down[1] = child;
            } else { // if parent node is a left child, update (assign) its parent's left child
                // to the argument child node
                parent.up.down[0] = child;
            }
            // updating argument child's up data field to argument parent's parent
            child.up = parent.up;
        }

        // left rotation: if the child is a right child, we need to perform left rotation
        if (isChildRightChild) {

            if (child.down[0] != null) { // if child node has a left child, we need to shift it
                // because after the rotation, the child node's left child will be the parent
                // argument, so in order to keep the bst valid, we must shift the left child to the
                // parent argument node's right child data field
                parent.down[1] = child.down[0];
                parent.down[1].up = parent; //update up data field of shifted node to new parent
            } else {
                // if child node does not have left child, then clear parent node's right child data
                // (because that is currently the child argument node, and after the rotation it
                // will be null)
                parent.down[1] = null;
            }
            child.down[0] = parent; // set argument child's left child as the passed parent node
            // (actual rotation occurring)
            parent.up = child; // update parent's up data field

        } else { // right rotation: if the child node is a left child, we perform right rotation

            if (child.down[1] != null) { // if child node has a right child, we need to shift it
                // (to not lose it) because after the rotation, the child node's right child will be
                // the parent node, so in order to keep the bst valid, the right child must be
                // shifted to being the left child of the parent node
                parent.down[0] = child.down[1];
                parent.down[0].up = parent; //update up data field of shifted node to new parent
            } else {
                // if child node does not have right child, then clear parent node's left child data
                // because that is currently just the argument child node and should be null after
                // the rotation
                parent.down[0] = null;
            }
            child.down[1] = parent; // set child's right child as the passed parent node (actual
            // rotation occurring)
            parent.up = child; // update parent's up data field
        }
    }

	/**
     * Get the size of the tree (its number of nodes).
     * @return the number of nodes in the tree
     */
    public int size() {
        return size;
    }

    /**
     * Method to check if the tree is empty (does not contain any node).
     * @return true of this.size() returns 0, false if this.size() != 0
     */
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Checks whether the tree contains the value *data*.
     * @param data a comparable for the data value to check for
     * @return true if *data* is in the tree, false if it is not in the tree
     */
    public boolean contains(Comparable<T> data) {
        // null references will not be stored within this tree
        if (data == null) {
            throw new NullPointerException("This tree cannot store null references.");
        } else {
            Node<T> nodeWithData = this.findNode(data);
            // return false if the node is null, true otherwise
            return (nodeWithData != null);
        }
    }

    /**
     * Removes all keys from the tree.
     */
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    /**
     * Helper method that will return the node in the tree that contains a specific
     * key. Returns null if there is no node that contains the key.
     * @param data the data value for which we want to find the node that contains it
     * @return the node that contains the data value or null if there is no such node
     */
    protected Node<T> findNode(Comparable<T> data) {
        Node<T> current = this.root;
        while (current != null) {
            int compare = data.compareTo(current.data);
            if (compare == 0) {
                // we found our value
                return current;
            } else if (compare < 0) {
                if (current.down[0] == null) {
                    // we have hit a null node and did not find our node
                    return null;
                }
                // keep looking in the left subtree
                current = current.down[0];
            } else {
                if (current.down[1] == null) {
                    // we have hit a null node and did not find our node
                    return null;
                }
                // keep looking in the right subtree
                current = current.down[1];
            }
        }
        return null;
    }

    /**
     * This method performs an inorder traversal of the tree. The string 
     * representations of each data value within this tree are assembled into a
     * comma separated string within brackets (similar to many implementations 
     * of java.util.Collection, like java.util.ArrayList, LinkedList, etc).
     * @return string containing the ordered values of this tree (in-order traversal)
     */
    public String toInOrderString() {
        // generate a string of all values of the tree in (ordered) in-order
        // traversal sequence
        StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        if (this.root != null) {
            Stack<Node<T>> nodeStack = new Stack<>();
            Node<T> current = this.root;
            while (!nodeStack.isEmpty() || current != null) {
                if (current == null) {
                    Node<T> popped = nodeStack.pop();
                    sb.append(popped.data.toString());
                    if(!nodeStack.isEmpty() || popped.down[1] != null) sb.append(", ");
                    current = popped.down[1];
                } else {
                    nodeStack.add(current);
                    current = current.down[0];
                }
            }
        }
        sb.append(" ]");
        return sb.toString();
    }

    /**
     * This method performs a level order traversal of the tree. The string
     * representations of each data value
     * within this tree are assembled into a comma separated string within
     * brackets (similar to many implementations of java.util.Collection).
     * This method will be helpful as a helper for the debugging and testing
     * of your rotation implementation.
     * @return string containing the values of this tree in level order
     */
    public String toLevelOrderString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        if (this.root != null) {
            LinkedList<Node<T>> q = new LinkedList<>();
            q.add(this.root);
            while(!q.isEmpty()) {
                Node<T> next = q.removeFirst();
                if(next.down[0] != null) q.add(next.down[0]);
                if(next.down[1] != null) q.add(next.down[1]);
                sb.append(next.data.toString());
                if(!q.isEmpty()) sb.append(", ");
            }
        }
        sb.append(" ]");
        return sb.toString();
    }

    public String toString() {
        return "level order: " + this.toLevelOrderString() +
                "\nin order: " + this.toInOrderString();
    }

    // Implement at least 3 tests using the methods below. You can
    // use your notes from lecture for ideas of rotation examples to test with.
    // Make sure to include rotations at the root of a tree in your test cases.
    // Give each of the methods a meaningful header comment that describes what is being
    // tested and make sure your tests have inline comments that help with reading your test code.
    // If you'd like to add additional tests, then name those methods similar to the ones given below.
    // Eg: public static boolean test4() {}
    // Do not change the method name or return type of the existing tests.
    // You can run your tests through the static main method of this class.

    /**
     * Test a left rotation not at the root node of the tree, but both the nodes being rotated have
     * children, so this test verifies that the BST conditions are maintained post-rotation (and
     * that the rotation is performed correctly)
     * @return true is check passes, false if it fails
     */
    public static boolean test1() {
        // insert keys into empty tree
        BinarySearchTree<Integer> testTree = new BinarySearchTree<>();
        testTree.insert(8);
        testTree.insert(4);
        testTree.insert(11);
        testTree.insert(2);
        testTree.insert(6);
        testTree.insert(9);
        testTree.insert(12);
        testTree.insert(1);
        testTree.insert(5);
        testTree.insert(7);

        // find nodes for 8, 4, and 6
        Node<Integer> eight = testTree.root;
        Node<Integer> four = eight.down[0];
        Node<Integer> six = four.down[1];
        // then rotate 6 and 4
        testTree.rotate(six, four);

        // check level order string of tree after rotation
        if (!testTree.toLevelOrderString().equals("[ 8, 6, 11, 4, 7, 9, 12, 2, 5, 1 ]")) {
            System.out.println("After inserting 8, 4, 11, 2, 6, 9, 12, 1, 5, 7 into an empty tree" +
                " and rotating 6 and 4, level order is not [ 8, 6, 11, 4, 7, 9, 12, 2, 5, 1 ]," +
                " but should be");
            return false;
        }

        //test passes
        return true;
    }
    /**
     * Test a slightly more complex left rotation at the root node of the tree.
     * @return true is check passes, false if it fails
     */
    public static boolean test2() {
        // insert keys into empty tree
        BinarySearchTree<Integer> testTree = new BinarySearchTree<>();
        testTree.insert(6);
        testTree.insert(8);
        testTree.insert(7);
        testTree.insert(9);
        testTree.insert(4);
        testTree.insert(5);
        testTree.insert(3);

        // find nodes for 6 and 8
        Node<Integer> six = testTree.root;
        Node<Integer> eight = six.down[1];
        // then rotate 6 and 8
        testTree.rotate(eight, six);

        // check level order string of tree after rotation
        if (!testTree.toLevelOrderString().equals("[ 8, 6, 9, 4, 7, 3, 5 ]")) {
            System.out.println("After inserting 6, 8, 7, 9, 4, 5, 3 into an empty tree and " +
                "rotating 8 and 6, level order is not [ 8, 6, 9, 4, 7, 3, 5 ], but should be");
            return false;
        }

        //test passes
        return true;
    }

    /**
     * Test a right rotation not at the root node of the tree, but both the nodes being rotated have
     * children, so this test verifies that the BST conditions are maintained post rotation (and
     * that the rotation was performed correctly)
     * @return true is check passes, false if it fails
     */
    public static boolean test3() {
        // insert keys into empty tree
        BinarySearchTree<Integer> testTree = new BinarySearchTree<>();
        testTree.insert(8);
        testTree.insert(4);
        testTree.insert(11);
        testTree.insert(2);
        testTree.insert(6);
        testTree.insert(9);
        testTree.insert(12);
        testTree.insert(1);
        testTree.insert(3);
        testTree.insert(5);
        testTree.insert(7);

        // find nodes for 8, 4, and 2
        Node<Integer> eight = testTree.root;
        Node<Integer> four = eight.down[0];
        Node<Integer> two = four.down[0];
        // then rotate 2 and 4
        testTree.rotate(two, four);

        // check level order string of tree after rotation
        if (!testTree.toLevelOrderString().equals("[ 8, 2, 11, 1, 4, 9, 12, 3, 6, 5, 7 ]")) {
            System.out.println("After inserting 8, 4, 11, 2, 6, 9, 12, 1, 3, 5, 7 into an empty " +
                "tree and rotating 4 and 2, level order is not " +
                "[ 8, 2, 11, 1, 4, 9, 12, 3, 6, 5, 7 ], but should be");
            return false;
        }

        //test passes
        return true;
    }

    /**
     * Test a slightly more complicated right rotation at the root node of the tree (ensures that
     * the rotation was performed correctly and that the BST conditions were maintained)
     * @return true is check passes, false if it fails
     */
    public static boolean test4() {
        // insert keys into empty tree
        BinarySearchTree<Integer> testTree = new BinarySearchTree<>();
        testTree.insert(8);
        testTree.insert(4);
        testTree.insert(11);
        testTree.insert(2);
        testTree.insert(6);
        testTree.insert(9);
        testTree.insert(12);
        testTree.insert(1);
        testTree.insert(3);
        testTree.insert(5);
        testTree.insert(7);

        // find nodes for 8, and 4
        Node<Integer> eight = testTree.root;
        Node<Integer> four = eight.down[0];

        // then rotate 8 and 4
        testTree.rotate(four, eight);

        // check level order string of tree after rotation
        if (!testTree.toLevelOrderString().equals("[ 4, 2, 8, 1, 3, 6, 11, 5, 7, 9, 12 ]")) {
            System.out.println("After inserting 8, 4, 11, 2, 6, 9, 12, 1, 3, 5, 7 into an empty " +
                "tree and rotating 8 and 4, level order is not " +
                "[ 4, 2, 8, 1, 3, 6, 11, 5, 7, 9, 12 ], but should be");
            return false;
        }

        //test passes
        return true;
    }

    /**
     * Tests to ensure that the rotate method throws IllegalArgumentException when unrelated nodes
     * are passed as arguments
     * @return true is check passes, false if it fails
     */
    public static boolean test5() {
        // insert keys into empty tree
        BinarySearchTree<Integer> testTree = new BinarySearchTree<>();
        testTree.insert(2);
        testTree.insert(1);
        testTree.insert(4);
        testTree.insert(3);
        testTree.insert(6);
        testTree.insert(5);
        testTree.insert(7);

        // find nodes for 2, and 7
        Node<Integer> two = testTree.root;
        Node<Integer> seven = two.down[1].down[1].down[1];

        // then rotate 2 and 7 (expecting an error)
        try {
            testTree.rotate(seven, two);
            return false; // if no exception was thrown
        } catch (IllegalArgumentException e) {
            // do nothing because this is expected
        } catch (Exception e) {
            return false; // return false if some other exception is thrown
        }

        //test passes
        return true;
    }
    
    /**
     * Main method to run tests. If you'd like to add additional test methods, add a line for each
     * of them.
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Test 1 passed: " + test1());
        System.out.println("Test 2 passed: " + test2());
        System.out.println("Test 3 passed: " + test3());
        System.out.println("Test 4 passed: " + test4());
        System.out.println("Test 5 passed: " + test5());
    }

}
