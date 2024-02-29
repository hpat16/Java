import java.io.File;
import processing.core.PImage;

/**
 * This class models a graphic Thing which can be drawn at a give (x,y) position within the display
 * window of a graphic application.
 */
public class Thing {
  // PApplet object that represents the display window of this graphic application
  protected static processing.core.PApplet processing;
  // image of this graphic thing of type PImage
  private processing.core.PImage image;
  // x-position of this thing in the display window
  protected float x;
  // y-position of this thing in the display window
  protected float y;

  /**
   * Creates a new graphic Thing located at a specific (x, y) position of the display window
   * @param x x-position of this thing in the display window
   * @param y y-position of this thing in the display window
   * @param imageFilename filename of the image of this thing, for instance "name.png"
   */
  public Thing(float x, float y, String imageFilename) {
    this.image = processing.loadImage("images" + File.separator + imageFilename);
    this.x = x;
    this.y = y;
  }

  /**
   * Draws this thing to the display window at its current (x,y) position
   */
  public void draw() {
    // draw the thing at its current position
    processing.image(this.image, this.x, this.y);
  }

  /**
   * Sets the PApplet object display window where this Thing object will be drawn
   * @param processing processing - PApplet object that represents the display window
   */
  public static void setProcessing(processing.core.PApplet processing) {
    Thing.processing = processing;
  }

  /**
   * Returns a reference to the image of this thing
   * @return the image of type PImage of the thing object
   */
  public processing.core.PImage image() { return this.image; }

  /**
   * Checks if the mouse is over this Thing object
   * @return true if the mouse is over this Thing, otherwise returns false
   */
  public boolean isMouseOver() {
    float thingWidth = this.image.width;
    float thingHeight = this.image.height;

    // checks if the mouse is over this Thing
    return processing.mouseX >= this.x - (thingWidth / 2)
        && processing.mouseX <= this.x + (thingWidth / 2)
        && processing.mouseY >= this.y - (thingHeight / 2)
        && processing.mouseY <= this.y + (thingHeight / 2);
  }
}
