import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tester class that thoroughly tests a class implementing BackendInterface through JUnit tests
 */
public class BackendDeveloperTests {

  /**
   * This method tests the important features of the readFile method in a class implementing
   * BackendInterface. Specifically, it tests that a FileNotFoundException is thrown when a
   * non-existing file is passed as an argument, and that readFile gives the correct output with a
   * tester CSV file.
   */
  @Test
  public void testReadFile() {
    IterableMultiKeyRBT<MeteoriteObjectInterface> redBlackTree =
        new IterableMultiKeyRBT<>();
    Backend backend = new Backend(redBlackTree);
    // case where the given file doesn't actually exist
    try {
      backend.readFile("&^&"); // assumption is that this file does not exist
      fail(); // if no exception thrown, fail
    } catch (IOException e) {
      assertTrue(true); // expect it to throw because file does not exist
    } catch (Exception e) {
      fail(); // if a different exception thrown than expected, then fail
    }
    // valid file given; check to ensure RBT contains the correct info
    try {
      backend.readFile("meteoritesTester.csv");
      // meteorite objects in the file
      MeteoriteObjectInterface m1 = new MeteoriteObject(
          "Aachen", 50.775000, 6.083330, "Fell", 21);
      MeteoriteObjectInterface m2 = new MeteoriteObject(
          "Aarhus", 56.183330, 10.233330, "Fell", 720);
      MeteoriteObjectInterface m3 = new MeteoriteObject(
          "Abee", 54.216670, -113.000000, "Fell", 107000);
      MeteoriteObjectInterface m4 = new MeteoriteObject(
          "Acapulco", 16.883330, -99.900000, "Fell", 1914);
      MeteoriteObjectInterface m5 = new MeteoriteObject(
          "Achiras", -33.166670, -64.950000, "Fell", 780);
      MeteoriteObjectInterface m6 = new MeteoriteObject(
          "Adhi Kot", 32.100000, 71.800000, "Fell", 4239);
      MeteoriteObjectInterface m7 = new MeteoriteObject(
          "Akyumak", 39.916670, 42.816670, "Fell", 50000);
      int expectedSize = 7;
      assertEquals(expectedSize, redBlackTree.size()); // ensure the size of the tree is correct
      // use contains() to check is these are the size meteorites read from the file
      ArrayList<MeteoriteObjectInterface> expected = new ArrayList<>();
      expected.add(m1);
      expected.add(m2);
      expected.add(m5);
      expected.add(m4);
      expected.add(m6);
      expected.add(m7);
      expected.add(m3);
      int i = 0;
      for (MeteoriteObjectInterface meteorite : redBlackTree) {
        assertEquals(meteorite, expected.get(i++));
      }
    } catch (Exception e) {
      fail(); // shouldn't throw exception
    }
  }

  /**
   * This test tests two cases for the getMaxMassMeteorites() in a class that implements
   * BackendInterface.
   *   Case 1:
   *     A red black tree contains meteorites with duplicate masses, so it is expected that
   *     getMaxMassMeteorites() accounts for all of them.
   *   Case 2:
   *     Just an average case where a red black tree has multiple meteorites but only has the max
   *     mass
   */
  @Test
  public void testGetMaxMassMeteorites() {
    // case 1
    {
      IterableMultiKeyRBT<MeteoriteObjectInterface> redBlackTree =
          new IterableMultiKeyRBT<>();
      Backend backend = new Backend(redBlackTree);
      // inserting different meteorites with equal mass
      MeteoriteObjectInterface m1 = new MeteoriteObject("m1", 1.0, 1.0, "fall", 2.0);
      MeteoriteObjectInterface m2 = new MeteoriteObject(
          "m2", 2.0, 2.0, "fall", 2.0);
      MeteoriteObjectInterface m3 = new MeteoriteObject(
          "m3", 3.0, 3.0, "fall", 2.0);
      MeteoriteObjectInterface m4 = new MeteoriteObject(
          "m4", 4.0, 4.0, "fall", 2.0);
      redBlackTree.insertSingleKey(m1);
      redBlackTree.insertSingleKey(m2);
      redBlackTree.insertSingleKey(m3);
      redBlackTree.insertSingleKey(m4);
      // list of the expected meteorites that should be returned by the method
      ArrayList<MeteoriteObjectInterface> expected = new ArrayList<>();
      expected.add(m1);
      expected.add(m2);
      expected.add(m3);
      expected.add(m4);
      // actual list returned from the method
      ArrayList<MeteoriteObjectInterface> actual =
          (ArrayList<MeteoriteObjectInterface>) backend.getMaxMassMeteorites();

      // the number of elements in the actual list should be the same as what the expected list has
      assertEquals(expected.size(), actual.size());
      // checking that all items in the expected list show up somewhere in the actual list
      for (int i = 0; i < expected.size(); i++) {
        for (int j = 0; j < expected.size(); j++) {
          if (expected.get(i).getName().equals(actual.get(j).getName())) {
            assertTrue(expected.get(i).equals(actual.get(j)));
          }
        }
      }
    }
    // case 2
    {
      IterableMultiKeyRBT<MeteoriteObjectInterface> redBlackTree =
          new IterableMultiKeyRBT<>();
      Backend backend = new Backend(redBlackTree);
      // inserting different meteorites with different masses
      MeteoriteObjectInterface m1 = new MeteoriteObject(
          "me1", 1.0, 1.0, "fall", 2.0);
      MeteoriteObjectInterface m2 = new MeteoriteObject(
          "me2", 2.0, 2.0, "fall", 4.0);
      MeteoriteObjectInterface m3 = new MeteoriteObject(
          "me3", 3.0, 3.0, "fall", 5.0);
      MeteoriteObjectInterface m4 = new MeteoriteObject(
          "me4", 4.0, 4.0, "fall", 6.0);
      redBlackTree.insertSingleKey(m1);
      redBlackTree.insertSingleKey(m2);
      redBlackTree.insertSingleKey(m3);
      redBlackTree.insertSingleKey(m4);
      // in this case, the expected should only have m4 since it has the largest mass
      ArrayList<MeteoriteObjectInterface> expected = new ArrayList<>();
      expected.add(m4);
      ArrayList<MeteoriteObjectInterface> actual =
          (ArrayList<MeteoriteObjectInterface>) backend.getMaxMassMeteorites();
      // the number of elements in the actual list should be the same as what the expected list has
      assertEquals(expected.size(), actual.size());
      // checking that all items in the expected list show up somewhere in the actual list
      for (int i = 0; i < expected.size(); i++) {
        for (int j = 0; j < expected.size(); j++) {
          if (expected.get(i).getName().equals(actual.get(j).getName())) {
            assertTrue(expected.get(i).equals(actual.get(j)));
          }
        }
      }
    }
  }

  /**
   * This test checks all the invalid inputs that can be given to getMeteoritesBetween() from a
   * class that implements BackendInterface and ensures that these invalid inputs are handled
   * properly
   */
  @Test
  public void testInvalidGetMeteoritesBetween() {
    /* case 1:
     *   The lowerBound provided as an argument is actually less than the higherBound. We would
     *   expect an IllegalArgumentException to be thrown.
     */
    {
      IterableMultiKeyRBT<MeteoriteObjectInterface> redBlackTree =
          new IterableMultiKeyRBT<>();
      Backend backend = new Backend(redBlackTree);
      try {
        ArrayList<MeteoriteObjectInterface> actual =
            (ArrayList<MeteoriteObjectInterface>) backend.getMeteoritesBetween(5, 1);
        fail(); // no exception thrown: fail
      } catch (IllegalArgumentException e) {
        assertTrue(true); // Correct exception thrown: pass
      } catch (Exception e) {
        fail(); // Incorrect or some other exception thrown: fail
      }
    }
    /* case 2:
     *   Testing to check if the method correctly handles when the bounds passed as an argument are
     *   negative. Since mass cannot be negative, we would expect IllegalArgumentException to be
     *   thrown
     */
    {
      IterableMultiKeyRBT<MeteoriteObjectInterface> redBlackTree =
          new IterableMultiKeyRBT<>();
      Backend backend = new Backend(redBlackTree);
      try {
        ArrayList<MeteoriteObjectInterface> actual =
            (ArrayList<MeteoriteObjectInterface>) backend.getMeteoritesBetween(-5, -1);
        fail(); // no exception thrown: fail
      } catch (IllegalArgumentException e) {
        assertTrue(true); // Correct exception thrown: pass
      } catch (Exception e) {
        fail(); // Incorrect or some other exception thrown: fail
      }
    }
  }

  /**
   * This test checks to see that the getMeteoritesBetween() method functions correctly with valid
   * inputs. The edge case it tries to get at is when the given range contains duplicates (while I
   * would also like to test with an empty tree, since our interface does not give one distinct way
   * of handling that, it wouldn't be opaque)
   */
  @Test
  public void testValidGetMeteoritesBetween() {
    IterableMultiKeyRBT<MeteoriteObjectInterface> redBlackTree =
        new IterableMultiKeyRBT<>();
    Backend backend = new Backend(redBlackTree);
    // inserting different meteorites with various masses
    MeteoriteObjectInterface m1 = new MeteoriteObject(
        "m1", 1.0, 1.0, "fall", 21.2);
    MeteoriteObjectInterface m2 = new MeteoriteObject(
        "m2", 2.0, 2.0, "fall", 41.1);
    MeteoriteObjectInterface m3 = new MeteoriteObject(
        "m3", 3.0, 3.0, "fall", 56.1);
    MeteoriteObjectInterface m4 = new MeteoriteObject(
        "m4", 4.0, 4.0, "fall", 12.5);
    MeteoriteObjectInterface m5 = new MeteoriteObject(
        "m5", 4.0, 4.0, "fall", 12.5);
    redBlackTree.insertSingleKey(m1);
    redBlackTree.insertSingleKey(m2);
    redBlackTree.insertSingleKey(m3);
    redBlackTree.insertSingleKey(m4);
    redBlackTree.insertSingleKey(m5);

    ArrayList<MeteoriteObjectInterface> actual =
        (ArrayList<MeteoriteObjectInterface>) backend.getMeteoritesBetween(0, 41);
    // we expect that the returned list will contain m1, m4, and m5
    ArrayList<MeteoriteObjectInterface> expected = new ArrayList<>();
    expected.add(m1);
    expected.add(m4);
    expected.add(m5);
    // the number of elements in the actual list should be the same as what the expected list has
    assertEquals(expected.size(), actual.size());
    // checking that all items in the expected list show up somewhere in the actual list
    for (int i = 0; i < expected.size(); i++) {
      for (int j = 0; j < expected.size(); j++) {
        if (expected.get(i).getName().equals(actual.get(j).getName())) {
          assertTrue(expected.get(i).equals(actual.get(j)));
        }
      }
    }
  }

  /**
   * This test ensures that the compareTo method in a class implementing MeteoriteObjectInterface
   * functions correctly
   */
  @Test
  public void testCompareToMeteorite() {
    MeteoriteObjectInterface m1 = new MeteoriteObject(
        "m1", 1.0, 1.0, "fall", 21.2);
    MeteoriteObjectInterface m2 = new MeteoriteObject(
        "m2", 2.0, 2.0, "fall", 41.1);
    MeteoriteObjectInterface m3 = new MeteoriteObject(
        "m3", 3.0, 3.0, "fall", 41.2);
    MeteoriteObjectInterface m4 = new MeteoriteObject(
        "m4", 4.0, 4.0, "fall", 12.5);
    MeteoriteObjectInterface m5 = new MeteoriteObject(
        "m5", 4.0, 4.0, "fall", 12.5);

    // checking basic functionality
    assertTrue(m1.compareTo(m2) < 0);
    assertTrue(m1.compareTo(m4) > 0);
    // checking that two meteorites with masses that are almost the same (double type) value aren't
    // mistaken to be the same mass
    assertTrue(m2.compareTo(m3) < 0);
    // checking to see that two meteorites with equal (double type) masses are indeed handled as
    // equal
    assertEquals(0, m4.compareTo(m5));
  }

  /**
   * This is a tester method that tests the integration of the frontend class with the backend
   * classes, specifically in the case of reading/loading a file. It calls the load file method from
   * frontend (and checks that forntend prints out expected things), and ensures that this is
   * communicated to the backend by checking if the backend command was run as expected and that the
   * RBT was updated.
   */
  @Test
  public void readFileIntegration() {
    // input that is a file name
    TextUITester tester = new TextUITester("meteoritesTester.csv");
    // initializes backend and frontend objects
    IterableMultiKeyRBT<MeteoriteObjectInterface> rbt = new IterableMultiKeyRBT<>();
    Scanner scanner = new Scanner(System.in);
    BackendInterface backend = new Backend(rbt);
    Frontend frontend = new Frontend(backend, scanner);
    // run option to read in file
    frontend.loadDataFileMenu();
    // Check if the main menu was printed
    String output = tester.checkOutput();
    // checks that the frontend requests the user to enter a filename, and that the file loaded 
    // successfully
    boolean containsLoadData = output.contains("Enter a filename") && output.contains("success");
    assertTrue(containsLoadData);
    // this tests that the frontend successfully communicated to the backend, and that the backend
    // RBT was updated
    try {
      // meteorite objects in the file
      MeteoriteObjectInterface m1 = new MeteoriteObject("Aachen", 50.775000, 6.083330, "Fell", 21);
      MeteoriteObjectInterface m2 = new MeteoriteObject("Aarhus", 56.183330, 10.233330, "Fell", 720);
      MeteoriteObjectInterface m3 = new MeteoriteObject("Abee", 54.216670, -113.000000, "Fell", 107000);
      MeteoriteObjectInterface m4 = new MeteoriteObject("Acapulco", 16.883330, -99.900000, "Fell", 1914);
      MeteoriteObjectInterface m5 = new MeteoriteObject("Achiras", -33.166670, -64.950000, "Fell", 780);
      MeteoriteObjectInterface m6 = new MeteoriteObject("Adhi Kot", 32.100000, 71.800000, "Fell", 4239);
      MeteoriteObjectInterface m7 = new MeteoriteObject("Akyumak", 39.916670, 42.816670, "Fell", 50000);
      int expectedSize = 7;
      assertEquals(expectedSize, rbt.size()); // ensure the size of the tree is correct
      // loading the correct meteorite objects into a list that represents the expected meteorites
      ArrayList<MeteoriteObjectInterface> expected = new ArrayList<>();
      expected.add(m1);
      expected.add(m2);
      expected.add(m5);
      expected.add(m4);
      expected.add(m6);
      expected.add(m7);
      expected.add(m3);
      int i = 0; // index that is used to compare the expected list to the actual RBT
      // ensures that all 7 of the expected meteorites are in the actual RBT
      for (MeteoriteObjectInterface meteorite : rbt) {
        assertEquals(meteorite, expected.get(i++));
      }
      // Expect to have checked through all 7 elements
      assertEquals(7, i);
    } catch (Exception e) {
      fail(); // shouldn't throw an exception if everything ran as expected
    }
  }

  /**
   * This is a tester method that tests the integration of the frontend class with the backend
   * classes, specifically in the case of loading specific data from a file that was read in. After
   * the file is read in through frontend command, the test calls load max meteorites and load
   * meteorites between a certain range from the frontend class, and ensures that both of these
   * calls output what is expected to.
   */
  @Test
  public void loadMaxAndBetweenMeteoritesIntegration() {
    // test containsLoadBiggestMeteorData
    {
      // this loads the option of loading a file (=1), specifies the file name (meteorites.csv),
      // chooses the option of getting max meteorite (=2), loading a second file, specifies a second
      // file name, gets a max meteorite again, and exits the run (=4)
      TextUITester tester = new TextUITester("meteoritesTester.csv\n1000\n100000");
      // initializes the backend and frontend objects
      IterableMultiKeyRBT<MeteoriteObjectInterface> rbt = new IterableMultiKeyRBT<>();
      Scanner scanner = new Scanner(System.in);
      BackendInterface backend = new Backend(rbt);
      Frontend frontend = new Frontend(backend, scanner);
      // loads the data from file name provided
      frontend.loadDataFileMenu();
      // calls method to get max meteorite
      frontend.loadHighestMassMeteoritesMenu();
      String output = tester.checkOutput();
      // checks to ensure that the frontend communicated with the backend and received the correct
      // meteorite from the backend
      boolean containsLoadBiggestMeteorData = output.contains("Abee has a mass of: 107000");

      assertTrue(containsLoadBiggestMeteorData,
          "Calling maximum meteor function does not print the maximum mass meteor");
    }
    // test loadMeteoritesInRangeMenu
    {
      // first input option is the file to load (meteoritesTester.csv), the second input option is
      // the lowerBound (1000), and the third input option is the higherBound (100000)
      TextUITester tester = new TextUITester("meteoritesTester.csv\n1000\n100000");
      // initializes the backend and frontend objects
      IterableMultiKeyRBT<MeteoriteObjectInterface> rbt = new IterableMultiKeyRBT<>();
      Scanner scanner = new Scanner(System.in);
      BackendInterface backend = new Backend(rbt);
      Frontend frontend = new Frontend(backend, scanner);
      // run option the load a data file
      frontend.loadDataFileMenu();
      // run option to get meteorites between an inputted range
      frontend.loadMeteoritesInRangeMenu();

      String output = tester.checkOutput();
      // checks that the output contains only the meteorites with a mass in the range of
      // [1000, 100000], and the other meteorites that are outside the range are excluded
      boolean containsCorrectMeteorsBetween =
          output.contains("Acapulco has a mass of: 1914") && output.contains(
              "Adhi Kot has a mass of: 4239") && output.contains(
              "Akyumak has a mass of: 50000") && !output.contains("Aachen") && !output.contains(
              "Aarhus") && !output.contains("Abee") && !output.contains("Achiras");

      assertTrue(containsCorrectMeteorsBetween,
          "Calling meteors in between function does not print the meteors in between the " +
              "given arguments");
    }
  }

  /**
   * This is a third integration that tests the ability to load multiple files in a single run. This
   * is accomplished by loading a file, getting the max meteorite from that file (and checking it is
   * correct), and then loading a second file and ensuring that getting the max meteorite now
   * outputs the correct new max meteorite)
   */
  @Test
  public void loadMultipleFilesIntegration() {
    // this loads the option of loading a file (=1), specifies the file name (meteorites.csv),
    // chooses the option of getting max meteorite (=2), loading a second file, specifies a second
    // file name, gets a max meteorite again, and exits the run (=4)
    TextUITester tester =
        new TextUITester("1\nmeteorites.csv\n2\n1\nmeteoritesTester.csv\n2\n4");
    // initializes a backend and frontend object
    IterableMultiKeyRBT<MeteoriteObjectInterface> rbt = new IterableMultiKeyRBT<>();
    Scanner scanner = new Scanner(System.in);
    BackendInterface backend = new Backend(rbt);
    Frontend frontend = new Frontend(backend, scanner);
    // runs the main program, and the input is grabbed from TextUITester described above
    frontend.startCommandLoop();
    String output = tester.checkOutput();
    // checks that the first file was loaded successfully and the get max option outputs the correct
    // meteorite
    boolean containsFirstFileMax = output.contains("Hoba") && output.contains("6.0E7");
    // checks that the second file was loaded successfully and the get max option outputs the
    // correct meteorite
    boolean containsSecondFileMax = output.contains("Abee") && output.contains("107000");
    // ensures that both files loaded successfully by checking that the correct max meteorite was
    // outputted
    assertTrue(containsFirstFileMax && containsSecondFileMax);
  }

  /**
   * This tests my partners code, specifically the instance where loadMeteoritesInRangeMenu (the
   * method that gets the meteorites in a given range and outputs them) is called before a file is
   * loaded. We expect that some error statement is outputted describing that no file has been
   * loaded yet.
   */
  @Test
  public void testInvalidMenuCall() {
    // inputs valid bounds, lowerBound/leftBound = 1 and higherBounder/rightBound = 100
    TextUITester tester = new TextUITester("1\n100");
    // initializes the backend and frontend objects
    IterableMultiKeyRBT<MeteoriteObjectInterface> rbt = new IterableMultiKeyRBT<>();
    Scanner scanner = new Scanner(System.in);
    BackendInterface backend = new Backend(rbt);
    Frontend frontend = new Frontend(backend, scanner);
    // calls method to get meteorites between a provided range before a file was loaded
    frontend.loadMeteoritesInRangeMenu();
    String output = tester.checkOutput();
    // checks to ensure that some form of error statement was outputted because this call is invalid
    boolean isInvalid = output.contains("no meteorites loaded") || output.contains("invalid");
    assertTrue(isInvalid);
  }

  /**
   * This test tests the case where a user enters a menu option that is not listed in their given
   * options. We expect for some error checking to occur here, so some form of statement indicating
   * invalid option should be outputted to the user
   */
  @Test
  public void testOutOfRangeSelection() {
    // inputs an invalid menu choice, 0, which isn't listed as an option to the user, and then exits
    // the program (=4)
    TextUITester tester = new TextUITester("0\n4");
    // initializes backend and frontend objects
    IterableMultiKeyRBT<MeteoriteObjectInterface> rbt = new IterableMultiKeyRBT<>();
    Scanner scanner = new Scanner(System.in);
    BackendInterface backend = new Backend(rbt);
    Frontend frontend = new Frontend(backend, scanner);
    // load main menu options
    frontend.mainMenu();
    String output = tester.checkOutput();
    // checks that some statement that indicates that this out of range value isn't valid is
    // outputted
    boolean isInvalid = output.contains("not in the range");
    assertTrue(isInvalid);
  }
}
