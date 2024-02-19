//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title:    Basketball
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

import processing.core.PApplet;

/**
 * This class models Basketball objects. When clicked, the basketball rotate.
 * @author henishpatel
 */
public class Basketball extends Thing implements Clickable {
  // Total number of rotations this Basketball object has made since it was created
  private int rotations;
  // Defines the rotation angle in radians that this Basketball object make when clicked
  public float rotation;

  /**
   * Creates a new Basketball object located at (x,y) position whose image filename is
   * "basketball.png", and sets its rotation angle to PApplet.PI/2.
   * Initially, when created, the basketball has made zero rotations
   * @param x x-position of this Basketball object in the display window
   * @param y y-position of this Basketball object in the display window
   */
  public Basketball(float x, float y) {
    super(x, y, "basketball.png");
    rotation = PApplet.PI/2;
    rotations = 0;
  }

  /**
   * Draws this rotating Basketball object to the display window. The implementation details of this
   * method is fully provided in the write-up of p05.
   */
  @Override
  public void draw() {
    // draw this rotating Basketball object at its current position
    processing.pushMatrix();
    processing.translate(x, y);
    processing.rotate(this.rotations * rotation);
    processing.image(image(), 0.0f, 0.0f);
    processing.popMatrix();
  }

  /**
   * Defines the behavior of this basketball when the mouse is pressed. The basketball rotates when
   * it is clicked (the mouse is over it when pressed).
   */
  @Override
  public void mousePressed() {
    if (this.isMouseOver()) {
      rotate();
    }
  }

  /**
   * Called when the mouse is released. A basketball object does nothing when the mouse is released.
   * This is a method with an empty body.
   */
  @Override
  public void mouseReleased() {}

  /**
   * This method rotates this basketball object by incrementing the number of its rotations by one.
   */
  public void rotate() {
    rotations++;
  }

}
