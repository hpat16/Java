/**
 * This class models a Badger object
 */
public class Badger extends MovingThing implements Clickable {
  // indicates whether this badger is being dragged or not
  private boolean isDragging;
  // indicates whether this badger is dancing or not
  private boolean isDancing;
  // old x-position of the mouse
  private static int oldMouseX;
  // old y-position of the mouse
  private static int oldMouseY;
  // array storing this Badger's dance show steps
  private DanceStep[] danceSteps;
  // index position of the current dance step of this badger
  private int stepIndex;
  /*
  stores the next dance (x, y) position of this Badger
  nextDancePosition[0]: x-position
  nextDancePosition[1]: y-position
  */
  private float[] nextDancePosition;

  /**
   * Creates a new Badger object positioned at a specific position of the display window and whose
   * moving speed is 2.
   * When created, a new badger is not dragging and is not dancing.
   * This constructor also sets the danceSteps of the created Badger to the one provided as input
   * and initializes stepIndex to 1.
   * @param x x-position of this Badger object within the display window
   * @param y y-position of this Badger object within the display window
   * @param danceSteps perfect-size array storing the dance steps of this badger
   */
  public Badger(float x, float y, DanceStep[] danceSteps) {
    super(x, y, 2, "badger.png");
    this.isDragging = false;
    this.isDancing = false;
    this.danceSteps = danceSteps;
    this.stepIndex = 1;
    this.nextDancePosition = new float[2];
  }

  /**
   * Draws this badger to the display window. Also, this method:
   * - calls the drag() behavior if this Badger is dragging
   * - calls the dance() behavior if this Badger is dancing
   * To draw the badger to the screen, think of using partial overriding (super.draw()) as the image
   * of the Badger is not directly visible here.
   */
  @Override
  public void draw() {
    // draw the badger at its current position
    super.draw();
    if (this.isDragging) {
      this.drag();
    }

    if (this.isDancing) {
      this.dance();
    }
  }

  /**
   * Checks whether this badger is being dragged
   * @return true if the badger is being dragged, false otherwise
   */
  public boolean isDragging() { return isDragging; }

  /**
   * Helper method to drag this Badger object to follow the mouse moves
   */
  private void drag() {
    int dx = processing.mouseX - oldMouseX;
    int dy = processing.mouseY - oldMouseY;
    x+=dx;
    y+=dy;

    if(x > 0)
      x = Math.min(x, processing.width);
    else
      x = 0;
    if(y > 0)
      y = Math.min(y, processing.height);
    else
      y = 0;
    oldMouseX = processing.mouseX;
    oldMouseY = processing.mouseY;
  }

  /**
   * Starts dragging this badger
   */
  public void startDragging() {
    oldMouseX = processing.mouseX;
    oldMouseY = processing.mouseY;
    this.isDragging = true;
    drag();
  }

  /**
   * Stops dragging this Badger object
   */
  public void stopDragging() {
    this.isDragging = false;
  }

  /**
   * Defines the behavior of this Badger when it is clicked. If the mouse is over this badger and
   * this badger is NOT dancing, this method starts dragging this badger.
   */
  @Override
  public void mousePressed() {
    if (this.isMouseOver() && !this.isDancing) {
      this.startDragging();
    }
  }

  /**
   * Defines the behavior of this Badger when the mouse is released.
   * If the mouse is released, this badger stops dragging.
   */
  @Override
  public void mouseReleased() {
    this.stopDragging();
  }

  /**
   * This helper method moves this badger one speed towards its nextDancePosition. Then, it checks
   * whether this Badger is facing right and updates the isFacingRight data field accordingly.
   * After making one move dance, a badger is facing right if the x-move towards its next dance
   * position is positive, otherwise, it is facing left.
   * @return true if this Badger almost reached its next dance position, meaning that the distance
   * to its next dance position is less than 2 times its speed. Otherwise, return false.
   */
  private boolean makeMoveDance() {
    float dx = nextDancePosition[0] - this.x; // x-move towards destination
    float dy = nextDancePosition[1] - this.y; // y-move towards destination
    int d = (int) Math.sqrt(dx * dx + dy * dy); // distance to destination
    if (d != 0) { // move!
      this.x += speed * dx / d;
      this.y += speed * dy / d;
    }

    if (dx > 0) {
      isFacingRight = true;
    } else {
      isFacingRight = false;
    }

    if (d < 2*speed) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Implements the dance behavior of this Badger. This method prompts the Badger to make one move
   * dance.
   * If the makeMoveDance method call returns true (meaning the badger almost reached its
   * nextDancePosition), this method MUST:
   * - update its next dance position (see DanceStep.getPositionAfter()),
   * - increment the stepIndex.
   * Note that the danceSteps array is a circular indexing array. The stepIndex should be
   * incremented by one and then wrapped around with respect to the length of the array.
   */
  private void dance() {
    if (makeMoveDance()) {
      nextDancePosition = danceSteps[stepIndex].getPositionAfter(x, y);
      stepIndex = ++stepIndex % danceSteps.length;
    }
  }

  /**
   * Prompts this badger to start dancing. This method:
   * - updates the isDancing data field
   * - stops dragging this badger
   * - sets stepIndex to zero
   * - Resets the nextDancePosition
   *
   * The order of the above steps is not important.
   */
  public void startDancing() {
    isDancing = true;
    stopDragging();
    stepIndex = 0;
    nextDancePosition = danceSteps[stepIndex].getPositionAfter(x, y);
  }

  /**
   * Prompts this badger to stop dancing. Sets the isDancing data field to false.
   */
  public void stopDancing() {
    isDancing = false;
  }
}
