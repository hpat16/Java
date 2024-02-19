import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Backend class responsible for reading in meteorite data from a CSV file, inserting it into a
 * Red-Black Tree, and accessing it based on commands from the frontend. Exposes the required
 * functionality to the frontend: read data from a file, get a list of meteorites with the maximum
 * mass in the data set, get a list of meteorites with a mass between two specified thresholds.
 */
public class Backend implements BackendInterface {
  // red black tree that will be inserted to, read from, and accessed
  protected IterableMultiKeySortedCollectionInterface<MeteoriteObjectInterface> redBlackTree;
  // one constructor that takes an iterable red black tree of meteorite object as an argument
  public Backend(IterableMultiKeySortedCollectionInterface<MeteoriteObjectInterface> rbt) {
    if (rbt != null) {
      redBlackTree = rbt;
    }
  }

  /**
   * This method reads a file containing meteorite data and inserts it into a redBlackTree.
   *
   * @param fileName the name of the file containing the data (that needs to be read)
   * @throws IOException throws IOException if the file could not be opened, or read
   */
  @Override
  public void readFile(String fileName) throws IOException {
    /* if a file was previously loaded and the user wants to load another file, clear
       this tree to insert new data */ 
    if (!redBlackTree.isEmpty()) {
      redBlackTree.clear();
      redBlackTree.setIterationStartPoint(null);
    }
    
    File file = new File(fileName);
    Scanner scnr = new Scanner(file);
    String[] line; // stores one line of data from csv file
    // if this file is empty, there is nothing to read from so through IOException
    if (!scnr.hasNext()) {
	throw new IOException("The provided file is empty.");
    }
    // skip the first line if it is the names of columns
    if (scnr.next().contains("name,id")) {
      scnr.nextLine();
    }
    ArrayList<MeteoriteObjectInterface> test = new ArrayList<>();
    scnr.useDelimiter(",");
    // iterate through the file
    while(scnr.hasNextLine()) {
      int numQuotes = 0;
      // each line represents a new meteorite object
      line = new String[9];
      // read through the first 9 columns of each line because we don't need the 10th column
      for (int i = 0; i < 9; i++) {
        String next = scnr.next();
        if (next.contains("\"")) {
          // count the number of quotes in each item of each column to format properly
          for (int j = 0; j < next.length(); j++) {
            if (next.charAt(j) == '"') {
              numQuotes++;
            }
          }
          // if there are an odd number of quotes this comma is within a column and not dividing a
          // column, so we add this half but will decrement counter to catch the next half
          if (numQuotes % 2 == 1) {
            line[i] += next.replaceAll("\"", "") + ",";
            i--;
          } else { // if even number of quotes, this commas is dividing the column, so add it
            line[i] = next.replaceAll("\"", "");
          }
        } else { // if no quotes, just add the item because comma is dividng column
          line[i] = next;
        }
      }
      MeteoriteObject meteorite = new MeteoriteObject();
      // saving only the columns that are necessary into meteorite objects
      meteorite.setName(line[0]);
      meteorite.setMass(Double.parseDouble(line[4]));
      meteorite.setFall(line[5]);
      meteorite.setLatitude(Double.parseDouble(line[7]));
      meteorite.setLongitude(Double.parseDouble(line[8]));
      // insert meteorite object into the red black tree
      redBlackTree.insertSingleKey(meteorite);
      // skip colum 10 and refresh to the beginning of the next line
      scnr.nextLine();
    }
    scnr.close();
  }

  /**
   * This method iterates through the iterable redBlackTree  and returns a list of the meteorite
   * object(s) with the highest mass
   *
   * @return list of meteorite object(s) with the highest mass in the provided tree
   * @throws IllegalStateException if the red black tree is empty when this method is called
   */
  @Override
  public List<MeteoriteObjectInterface> getMaxMassMeteorites() {
    /* if a file was never read or was empty, and the RBT was left empty,
       then throw IllegalStateException */
    if (redBlackTree.isEmpty()) {
      throw new IllegalStateException("red black tree is empty");
    }
    ArrayList<MeteoriteObjectInterface> maxMeteoritesList = new ArrayList<>();
    /* maxMeteorite saves the max meteorite found through the first iteration to set a start point
     for the second iteration */
    MeteoriteObjectInterface maxMeteorite = null;
    for (MeteoriteObjectInterface meteorite : redBlackTree) {
      maxMeteorite = meteorite;
    }
    /* should function as expected assuming that this will set the iteration point to the first
     * meteorite with the same mass as the max, and not the max itself
     */
    redBlackTree.setIterationStartPoint(maxMeteorite);
    /* add all elements because the iteration start point should be at the very first element with
     max mass */
    for (MeteoriteObjectInterface meteorite : redBlackTree) {
      maxMeteoritesList.add(meteorite);
    }

    return maxMeteoritesList;
  }

  /**
   * This method iterates through the iterable redBlackTree and returns a list of meteorites objects
   * whose mass is within the range provided by the arguments
   *
   * @param lowerBound double representing the low threshold mass
   * @param higherBound double representing the high threshold mass
   * @return list of meteorite objects whose mass is within the range low-high
   * @throws IllegalArgumentException if the given arguments are invalid (higherBound < lowerBound,
   *     bounds are negative, etc)
   * @throws IllegalStateException if the red black tree is empty when this method is called
   */
  @Override
  public List<MeteoriteObjectInterface> getMeteoritesBetween(double lowerBound,
      double higherBound) throws IllegalArgumentException {
    // if the provided bounds were invalid, then throw IllegalArgumentException
    if (higherBound < lowerBound || lowerBound < 0 || higherBound < 0) {
      throw new IllegalArgumentException("invalid bounds");
    }
    /* if a file was never read or was empty, and the RBT was left empty,
       then throw IllegalStateException */
    else if (redBlackTree.isEmpty()) {
      throw new IllegalStateException("red black tree is empty");
    }

    ArrayList<MeteoriteObjectInterface> meteoritesList = new ArrayList<>();
    /* sets iteration start point to the first meteorite with the mass equal to or closest to
     lowerBound (at least I think it will do this if I am understanding this method correctly */
    redBlackTree.setIterationStartPoint(new MeteoriteObject("", 0, 0, "", lowerBound));
    for (MeteoriteObjectInterface meteorite : redBlackTree) {
      if ((meteorite.getMass() >= lowerBound) && (meteorite.getMass() <= higherBound)) {
        meteoritesList.add(meteorite);
      } else if (meteorite.getMass() > higherBound) {
        break;
      }
    }
    return meteoritesList;
  }
    
  /**
   * This main method instantiates all necessary objects to create a frontend and a backend object,
   * and runs the program by starting the main command loop of the frontend
   * @param args unused
   */
  public static void main(String args[]) {
    Scanner scanner = new Scanner(System.in);
    IterableMultiKeyRBT<MeteoriteObjectInterface> rbt = new IterableMultiKeyRBT<>();
    BackendInterface backend = new Backend(rbt);		
    Frontend frontend = new Frontend(backend, scanner);
    // start the main command loop of the frontend
    frontend.startCommandLoop();        
  }
}
