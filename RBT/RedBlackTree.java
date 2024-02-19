// --== CS400 Fall 2023 File Header Information ==--
// Name: Henish Patel
// Email: hpatel37@wisc.edu
// Group: B30
// TA: ROBERT NAGEL
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Red-Black Tree implementation with a RBTNode inner class for representing
 * the nodes of the tree. The implementation does not contain a remove function yet.
 */
public class RedBlackTree<T extends Comparable<T>> extends BinarySearchTree<T> {
  protected static class RBTNode<T> extends Node<T> {
    public int blackHeight = 0;
    public RBTNode(T data) { super(data); }
    public RBTNode<T> getUp() { return (RBTNode<T>)this.up; }
    public RBTNode<T> getDownLeft() { return (RBTNode<T>)this.down[0]; }
    public RBTNode<T> getDownRight() { return (RBTNode<T>)this.down[1]; }
  }

  /**
   * The purpose of this method is to resolve any red property violations that are introduced by
   * inserting a new node into the red-black tree.
   * @param redNode reference to a newly added red node
   */
  protected void enforceRBTreePropertiesAfterInsert(RBTNode<T> redNode) {
    // while the parent of redNode is red, RBT properties are violated: need to be fixed
    while (redNode != null && redNode.getUp() != null && redNode.getUp().blackHeight == 0) {
      RBTNode<T> parent = redNode.getUp(); // parent of redNode
      /* this represents what side the uncle is in the same manner the Nod<T> class does for the
      down data field; so 0 means the uncle is on the left side, and 1 means the uncle is on the
      right side (purpose of this variable is to simplify unnecessary chunks of if statements) */
      int uncleSide = parent.isRightChild() ? 0 : 1;
      RBTNode<T> uncle = (RBTNode<T>)parent.getUp().down[uncleSide];
      // case 1: uncle is red
      if (uncle != null && uncle.blackHeight == 0) {
        parent.blackHeight = 1; // change parent to black
        uncle.blackHeight = 1; // change uncle to black
        parent.getUp().blackHeight = 0; // change grandparent to red
        /* since grandparent is now red, there could be violation higher in tree, so update node to
        check through loop condition */
        redNode = parent.getUp();
      }
      // case 2: uncle is black and conflicting red nodes are on the same side
      else if (redNode.isRightChild() == parent.isRightChild()) {
        /* change colors first since it has no impact on rotation and after rotation,
           relationships change */
        parent.blackHeight = 1; // set parent to black
        parent.getUp().blackHeight = 0; // set grandparent to be red
        rotate(parent, parent.getUp()); // rotate parent and grandparent
      }
      // case 3: uncle is black and conflicting red nodes are on different sides
      else {
        // rotate redNode and parent as the first step to resolve this violation
        rotate(redNode, parent);
        /* after rotation, the parent will be the child of redNode, but the conflict remains, so we
           must update the redNode to now be the parent to accurately check it in next loop */
        redNode = parent;
      }
    }
  }

  /**
   * Inserts a new data value into the tree.
   * @param data to be added into this red black tree
   * @return true if the value was inserted, false if is was in the tree already
   * @throws NullPointerException when the provided data argument is null
   */
  @Override
  public boolean insert(T data) throws NullPointerException {
    if (data == null) {
      throw new NullPointerException("Cannot insert data value null into the tree.");
    }

    RBTNode<T> newNode = new RBTNode<>(data);
    // if insertHelper returns false, then insertion failed, so return false
    if (!insertHelper(newNode)) { return false; }
    enforceRBTreePropertiesAfterInsert(newNode);
    ((RBTNode<T>) root).blackHeight = 1;
    return true;
  }

  /**
   * This test tests some basic functionality of the insert method, ensuring that upon insertion,
   * the color of the added nodes are correct. The main purpose of this test is to test the case the
   * added node's parent is red, and it's aunt is red (which occurs at the insertion of node 62) (by
   * ensuring the level-order after the insertion is correct, and that the color of some specific
   * nodes that are affected are correct)
   */
  @Test
  public void caseRedAunt() {
    RedBlackTree<Integer> testTree = new RedBlackTree<>();
    testTree.insert(54);
    // the following line tests that the newly added node, which is now the root, is black
    assertEquals(1, ((RBTNode<Integer>)testTree.root).blackHeight);
    testTree.insert(40);
    // the following line tests that the node that was just added (left child of root) is red
    assertEquals(0, (((RBTNode<Integer>) testTree.root).getDownLeft()).blackHeight);
    testTree.insert(70);
    testTree.insert(32);
    // the following line tests that the left and right childs of the root are now changed to black
    assertEquals(1, (((RBTNode<Integer>) testTree.root).getDownLeft()).blackHeight);
    assertEquals(1, (((RBTNode<Integer>) testTree.root).getDownRight()).blackHeight);
    // this line tests that the very last node added (32) is red
    assertEquals(0,
        (((RBTNode<Integer>) testTree.root).getDownLeft().getDownLeft()).blackHeight);
    testTree.insert(49);
    testTree.insert(59);
    testTree.insert(78);
    testTree.insert(62);
    // this line tests that after insertion, the level-order traversal is as expected
    assertEquals("[ 54, 40, 70, 32, 49, 59, 78, 62 ]", testTree.toLevelOrderString());
    // tests to ensure that the root is still black
    assertEquals(1, ((RBTNode<Integer>)testTree.root).blackHeight);
    // following line ensures that the right child of the root was changed to red, as it should've
    assertEquals(0, ((RBTNode<Integer>)testTree.root).getDownRight().blackHeight);
    // ensures that node 59 was changed to black, as it should've been after the insertion of 62
    assertEquals(1,
        ((RBTNode<Integer>)testTree.root).getDownRight().getDownLeft().blackHeight);
  }

  /**
   * This test specifically tests the case where a red node is added and a violation occurs (because
   * its parent is red), and the new node and parent are on opposite sides while the parent's
   * sibling is black (this specifically occurs at the insertion of node 65). To ensure it is
   * tested to the fullest, the test checks the levelOrder after the insertions and it checks the
   * colors of some of the essential nodes that are rotated around.
   */
  @Test
  public void caseBlackAuntOppositeSide() {
    RedBlackTree<Integer> testTree = new RedBlackTree<>();
    testTree.insert(54);
    testTree.insert(40);
    testTree.insert(70);
    testTree.insert(32);
    testTree.insert(49);
    testTree.insert(59);
    testTree.insert(78);
    testTree.insert(62);
    testTree.insert(65);
    // this line tests that after insertion, the level-order traversal is as expected
    assertEquals("[ 54, 40, 70, 32, 49, 62, 78, 59, 65 ]",
        testTree.toLevelOrderString(), testTree.toLevelOrderString());
    /* the following lines ensure that the nodes affected by the insertion of node 65 are the
       correct colors (after the rotations/changes involved with this insertion) */
    assertEquals(1,
        ((RBTNode<Integer>)testTree.root).getDownRight().getDownLeft().blackHeight);
    assertEquals(0,
        ((RBTNode<Integer>)testTree.root).getDownRight().getDownLeft().getDownLeft().blackHeight);
    assertEquals(0,
        ((RBTNode<Integer>)testTree.root).getDownRight().getDownLeft().getDownRight().blackHeight);
  }

  /**
   * This test specifically tests the case where a red node is added and a violation occurs (parent
   * is red), and the new node and parent node are on the same side while the parent's sibling is
   * black (this occurs at the insertion of node 47). It also checks the heights of some specific
   * nodes that are rotated around and more vulnerable to bugs
   */
  @Test
  public void caseBlackAuntSameSide() {
    RedBlackTree<Integer> testTree = new RedBlackTree<>();
    testTree.insert(54);
    testTree.insert(40);
    testTree.insert(70);
    testTree.insert(32);
    testTree.insert(49);
    testTree.insert(59);
    testTree.insert(78);
    testTree.insert(45);
    testTree.insert(47);
    // this line tests that after insertion, the level-order traversal is as expected
    assertEquals("[ 54, 40, 70, 32, 47, 59, 78, 45, 49 ]",
        testTree.toLevelOrderString());
    /* the following lines ensure that the nodes affected by the insertion of node 47 are the
       correct colors (after the rotations/changes involved with this insertion) */
    assertEquals(1,
        ((RBTNode<Integer>)testTree.root).getDownLeft().getDownRight().blackHeight);
    assertEquals(0,
        ((RBTNode<Integer>)testTree.root).getDownLeft().getDownRight().getDownLeft().blackHeight);
    assertEquals(0,
        ((RBTNode<Integer>)testTree.root).getDownLeft().getDownRight().getDownRight().blackHeight);
  }

  /**
   * This test inserts the numbers 1 - 9 in order as this is notable for running into various
   * violations through the insertions. Specifically, at the insertion of 8, multiple steps are
   * necessary to uphold the RBT properties, and so after the insertion of 8, this test ensures that
   * specific nodes are at the correct positions (like the root) and that specific nodes are the
   * correct colors (those that are rotated around multiple times and affected the most). Finally,
   * after the insertion of 9, it ensures the levelOrder traversal of the resulting tree is correct
   * and the colors of some important nodes are correct
   */
  @Test
  public void testMultipleViolations() {
    RedBlackTree<Integer> testTree = new RedBlackTree<>();
    testTree.insert(1);
    testTree.insert(2);
    testTree.insert(3);
    testTree.insert(4);
    testTree.insert(5);
    testTree.insert(6);
    testTree.insert(7);
    testTree.insert(8);
    // ensures the root is no longer 2, but 4, and that after this change, it has the correct
    // childrens
    assertEquals(4, testTree.root.data);
    assertEquals(2, testTree.root.down[0].data);
    assertEquals(6, testTree.root.down[1].data);
    // checks the root's color and that the colors of the root's children are red and not black (as
    // they were before)
    assertEquals(1, ((RBTNode<Integer>)testTree.root).blackHeight);
    assertEquals(0, ((RBTNode<Integer>)testTree.root).getDownLeft().blackHeight);
    assertEquals(0, ((RBTNode<Integer>)testTree.root).getDownRight().blackHeight);
    // final
    testTree.insert(9);
    // this line tests that after insertion, the level-order traversal is as expected
    assertEquals("[ 4, 2, 6, 1, 3, 5, 8, 7, 9 ]",
        testTree.toLevelOrderString(), testTree.toLevelOrderString());
    /* the following lines ensure that the nodes affected by the insertion of node 9 are the
       correct colors (after the rotations/changes involved with this insertion) */
    assertEquals(1,
        ((RBTNode<Integer>)testTree.root).getDownRight().getDownLeft().blackHeight);
    assertEquals(1,
        ((RBTNode<Integer>)testTree.root).getDownRight().getDownRight().blackHeight);
    assertEquals(0,
        ((RBTNode<Integer>)testTree.root).getDownRight().getDownRight().getDownRight().blackHeight);
  }
}
