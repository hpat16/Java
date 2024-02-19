/**
 * This enumeration defines a Badger dance step in p05 Dancing Badgers program
 * 
 * @author Mouna
 *
 */
public enum DanceStep {
  /**
   * defines the one dance step Up
   */
  UP("Up", 0, 0.0f, -100.0f),

  /**
   * defines the one dance step DOWN
   */
  DOWN("Down", 1, 0.0f, 100.0f),

  /**
   * defines the one dance step LEFT
   */
  LEFT("Left", 2, -64.0f, 0.0f),

  /**
   * defines the one dance step RIGHT
   */
  RIGHT("Right", 3, 64.0f, 0.0f);

  /**
   * Defines one x-dance move of this DanceStep
   */
  private float dx;
  /**
   * Defines one y-dance move of this DanceStep
   */
  private float dy;

  /**
   * Private constructor to NOT be called from the outside of this enumeration.
   * 
   * @param name    name of this dance step
   * @param ordinal order of this dance step
   * @param dx      x-move
   * @param dy      y-move
   */
  private DanceStep(final String name, final int ordinal, final float dx, final float dy) {
    this.dx = dx;
    this.dy = dy;
  }

  /**
   * Gets the next position with respect to (x,y) coordinates provided as input.
   * 
   * @param x x-position of a given step
   * @param y y-position of a given step
   * @return a perfect size one dimensional array storing the x and y coordinates of the next
   *         position. (x-position stored at index 0 and y-position stored at index 1).
   */
  public float[] getPositionAfter(final float x, final float y) {
    return new float[] {x + this.dx, y + this.dy};
  }
}
