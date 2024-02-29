import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import processing.core.PApplet;

/**
 * This is the main class for the Dancing Bangers program
 */
public class DancingBadgers extends PApplet {

  // backgound image
  private static processing.core.PImage backgroundImage;
  // Maximum number of Badger objects allowed in the basketball court
  private static int badgersCountMax;
  // Tells whether the dance show is on
  private boolean danceShowOn;
  // Generator of random numbers
  private static Random randGen;
  // array storing badgers dance show steps
  private static DanceStep[] badgersDanceSteps = new DanceStep[] {DanceStep.LEFT,
      DanceStep.RIGHT, DanceStep.RIGHT, DanceStep.LEFT, DanceStep.DOWN,
      DanceStep.LEFT, DanceStep.RIGHT, DanceStep.RIGHT, DanceStep.LEFT, DanceStep.UP};
  // array storing the positions of the dancing badgers at the start of the dance show
  private static float[][] startDancePositions =
      new float[][] {{300, 250}, {364, 250}, {428, 250}, {492, 250}, {556, 250}};
  // arraylist storing Thing objects
  private static ArrayList<Thing> things;


  /**
   * Sets the size of the display window of this graphic application
   */
  @Override
  public void settings() {
    this.size(800, 600);
  }
  /**
   * Defines initial environment properties of this graphic application.
   * This method initializes all the data fields defined in this class.
   */
  @Override
  public void setup() {
    this.getSurface().setTitle("P5 Dancing Badgers"); // displays the title of the screen
    this.textAlign(3, 3); // sets the alignment of the text
    this.imageMode(3); // interprets the x and y position of an image to its center
    this.focused = true; // confirms that this screen is "focused", meaning
    // it is active and will accept mouse and keyboard input.
    // TODO complete the implementation of this method
    randGen = new Random();
    backgroundImage = loadImage("images" + File.separator + "background.png");
    badgersCountMax = 5;
    danceShowOn = false;
    things = new ArrayList<>();

    // set processing data fields for Thing class
    Thing.setProcessing(this);

    // create 4 Things and add them to the things list
    things.add(new Thing(50, 50, "target.png"));
    things.add(new Thing(750, 550, "target.png"));
    things.add(new Thing(750, 50, "shoppingCounter.png"));
    things.add(new Thing(50, 550, "shoppingCounter.png"));
    // create 2 startship robots and add them to the things list
    things.add(new StarshipRobot(things.get(0), things.get(2), 3));
    things.add(new StarshipRobot(things.get(1), things.get(3), 5));
    // create 2 basketball objects and adds them to the things list
    things.add(new Basketball(50, 300));
    things.add(new Basketball(750, 300));

  }

  /**
   * Callback method that draws and updates the application display window. This method runs in an
   * infinite loop until the program exits.
   */
  @Override
  public void draw() {
    background(color(255, 218, 185));
    image(backgroundImage,width / 2,height / 2);

    // draw things, robots, and then badgers
    for (Thing thing : things) {
      thing.draw();
    }
  }

  /**
   * Callback method called each time the user presses the mouse.
   * This method iterates through the list of things. If the mouse is over a Clickable object, it
   * calls its mousePressed method, then returns.
   */
  @Override
  public void mousePressed() {
    for (Thing thing: things) {
      if (thing instanceof Clickable && thing.isMouseOver()) {
        ((Clickable) thing).mousePressed();
        return;
      }
    }
  }

  /**
   * Callback method called each time the mouse is released.
   * This method calls the mouseReleased() method on every Clickable object stored in the things
   * list.
   */
  @Override
  public void mouseReleased() {
    for (Thing thing: things) {
      if (thing instanceof Clickable) {
        ((Clickable) thing).mouseReleased();
      }
    }
  }

  /**
   * Gets the number of Badger objects present in the basketball arena
   * @return the number of Badger objects present in the basketball arena
   */
  public int badgersCount() {
    int badgersCount = 0;
    for (Thing thing : things) {
      if (thing instanceof Badger) {
        badgersCount++;
      }
    }

    return badgersCount;
  }

  /**
   * Sets the badgers start dance positions. The start dance positions of the badgers are provided
   * in the startDancePositions array.
   * The array startDancePositions contains badgersCountMax dance positions. If there are fewer
   * Badger objects in the basketball arena, they will be assigned the first positions.
   */
  private void setStartDancePositions() {
    // a starting index for the nested for loop to ensure that it doesn't repeat over Badgers that
    // have already been assigned positions
    int j = 0;
    // outer loop loops through all the sets of [x,y] positions (so 5 times) to ensure that a unique
    // position is assigned to each of the Badger present while the inner loop ensures that a Badger
    // is assigned a position and that no Badger is reassigned the incorrect position
    for (float[] position : startDancePositions) {
      for (int i = j; i < things.size(); i++) {
        if (things.get(i) instanceof Badger) {
          things.get(i).x = position[0];
          things.get(i).y = position[1];
          // increment to start the next set of positions after this Badger(whose now been assigned)
          j = i+1;
          break;
        }
      }
    }
  }

  /**
   * Callback method called each time the user presses a key.
   * b-key: If the b-key is pressed and the danceShow is NOT on, a new badger is added to the list
   * of things. Up to badgersCountMax can be added to the basketball arena.
   *
   * c-key: If the c-key is pressed, the danceShow is terminated (danceShowOn set to false), and ALL
   * MovingThing objects are removed from the list of things. This key removes MovingThing objects
   * ONLY.
   *
   * d-key: This key starts the dance show if the danceShowOn was false, and there is at least one
   * Badger object in the basketball arena. Starting the dance show, sets the danceShowOn to true,
   * sets the start dance positions of the Badger objects, and calls the startDancing() method on
   * every Badger object present in the list of things. Pressing the d-key when the danceShowOn is
   * true or when there are no Badger objects present in the basketball arena has no effect.
   *
   * r-key: If the danceShow is NOT on and the r-key is pressed when the mouse is over a Badger
   * object, this badger is removed from the list of things. If the mouse is over more than one
   * badger, the badger at the lowest index position will be deleted.
   *
   * s-key: If the s-key is pressed, the danceShow is terminated and all the Badger objects stop
   * dancing. Pressing the s-key does not remove anything.
   */
  @Override
  public void keyPressed() {
    switch (Character.toUpperCase(this.key)) {
      case 'B': // b-key described in method header
        if (badgersCount() < badgersCountMax && !danceShowOn) {
          things.add(new Badger(randGen.nextInt(this.width), randGen.nextInt(this.height),
              badgersDanceSteps));
        }
        break;
      case 'C': // c-key described in method header
        danceShowOn = false;
        // boolean value representing whether things array list contains any MovingThing object
        boolean containsMovingThing = true;
        Thing thingToRemove;
        while (containsMovingThing) {
          thingToRemove = null;
          // finds the first MovingThing object, if any, present in things array list and saves the
          // references to use later
          for (Thing thing : things) {
            if (thing instanceof MovingThing) {
              thingToRemove = thing;
              break;
            }
          }
          // if a MovingThing object was found, the saved reference is used to remove that object
          // from things array list, and the loop restarts (until all MovingThing objects are
          // removed
          // if no MovingThing object was found (thingToRemove is null), containsMovingThing is set
          // to false to exit the loop
          if (thingToRemove != null) {
            things.remove(thingToRemove);
          } else {
            containsMovingThing = false;
          }
        }

        break;
      case 'D': // d-key described in method header
        if (badgersCount() > 0 && !danceShowOn) {
          danceShowOn = true;
          setStartDancePositions();
          for (Thing thing: things) {
            if (thing instanceof Badger) {
              ((Badger) thing).startDancing();
            }
          }
        }
        break;
      case 'R': // r-key described in method header
        if (!danceShowOn) {
          for (Thing thing : things) {
            if ((thing instanceof Badger) && thing.isMouseOver()) {
              things.remove(thing);
              break;
            }
          }
        }
        break;
      case 'S': // s-key described in method header
        danceShowOn = false;
        for (Thing thing: things) {
          if (thing instanceof Badger) {
            ((Badger) thing).stopDancing();
          }
        }
        break;
    }
  }


  /**
   * Driver method to run this graphic application
   * @param args list of input arguments if any
   */
  public static void main(String[] args) {
    PApplet.main("DancingBadgers");
  }
}
