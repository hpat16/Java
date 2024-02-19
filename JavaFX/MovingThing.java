//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title:    Moving Thing
// Course:   CS 300 Spring 2023
//
// Author:   Henish Patel
// Email:    hpatel37@wisc.edu
// Lecturer: Hobbes LeGault
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Partner Name:    (name of your pair programming partner)
// Partner Email:   (email address of your programming partner)
// Partner Lecturer's Name: (name of your partner's lecturer)
//
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
//   ___ Write-up states that pair programming is allowed for this assignment.
//   ___ We have both read and understand the course Pair Programming Policy.
//   ___ We have registered our team prior to the team registration deadline.
//
///////////////////////// ALWAYS CREDIT OUTSIDE HELP //////////////////////////
//
// Persons:         NONE
// Online Sources:  NONE
//
///////////////////////////////////////////////////////////////////////////////

/**
 * This class models moving thing objects. A moving thing is defined by its speed and to which
 * direction it is facing (right or left).
 * @author henishpatel
 */
public class MovingThing extends Thing implements Comparable<MovingThing> {
  // movement speed of this MovingThing
  protected int speed;
  // indicates whether this MovingThing is facing right or not
  protected boolean isFacingRight;

  /**
   * Creates a new MovingThing and sets its speed, image file, and initial x and y position.
   * A MovingThing object is initially facing right.
   * @param x starting x-position of this MovingThing
   * @param y starting y-position of this MovingThing
   * @param speed movement speed of this MovingThing
   * @param imageFileName filename of the image of this MovingThing, for instance "name.png"
   */
  public MovingThing(float x, float y, int speed, String imageFileName) {
    super(x, y, imageFileName);
    this.speed = speed;
    this.isFacingRight = true;
  }

  /**
   * Draws this MovingThing at its current position. The implementation details of this method is
   * fully provided in the write-up of p05.
   */
  @Override
  public void draw() {
    // draw this MovingThing at its current position
    processing.pushMatrix();
    processing.rotate(0.0f);
    processing.translate(x, y);
    if (!isFacingRight) {
      processing.scale(-1.0f, 1.0f);
    }
    processing.image(image(), 0.0f, 0.0f);
    processing.popMatrix();
  }

  /**
   * Compares this object with the specified MovingThing for order, in the increasing order of their
   * speeds.
   * @param other the MovingThing object to be compared
   * @return zero if this object and other have the same speed, a negative integer if the speed of
   * this moving object is less than the speed of other, and a positive integer otherwise.
   */
  @Override
  public int compareTo(MovingThing other) {
    return (this.speed - other.speed);
  }
}
