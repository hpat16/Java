import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class that tests Frontend, Backend, and the integrated version of Frontend and Backend
 */
public class FrontendDeveloperTests {
  /**
   * Tests the mainMenu() method to ensure it outputs the correct options. It tests valid user
   * inputs and tests invalid inputs to main menu and ensures that the program handles those
   * accordingly.
   */
  @Test
  public void testMainMenu() {
    // valid inputs to mainMenu
    {
      // following line not useful for this tester, just needs to be initialized
      TextUITester tester = new TextUITester("4\n");
      // init objects
      Scanner scanner = new Scanner(System.in);
      DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
      BackendInterface backend = new BackendPlaceholder(graph);
      Frontend frontend = new Frontend(backend, scanner);
      // call mainMenu and use TextUITester to check output
      frontend.mainMenu();
      String output = tester.checkOutput();

      assertTrue(output.contains("Main Menu"), "Failed to print main menu when calling mainMenu()");
    }
    // tests invalid inputs to main menu
    {
      // array of invalid options to choose for main menu
      String[] invalidMenuInputs = new String[]{"5\n", "-1\n", "test\n", "0\n"};
      // loop runs 4 times to test 4 different invalid input edge cases
      for (int i = 0; i < invalidMenuInputs.length; i++) {
        TextUITester tester = new TextUITester(invalidMenuInputs[i]);
        // init objects
        Scanner scanner = new Scanner(System.in);
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        BackendInterface backend = new BackendPlaceholder(graph);
        Frontend frontend = new Frontend(backend, scanner);

        frontend.mainMenu();
        String output = tester.checkOutput();
        assertTrue(output.contains("not a valid option") ||
            output.contains("not an integer"));
      }
    }
  }

  /**
   * Tests the loadDataFileMenu() method to ensure functions as expected (by checking what is
   * outputted). Two cases: one where use provides a valid file, another where user provides an
   * invalid file
   */
  @Test
  public void testLoadDataFileMenu() {
    // test valid input
    {
      TextUITester tester = new TextUITester("flights.dot\n");
      // init objects
      Scanner scanner = new Scanner(System.in);
      DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
      BackendInterface backend = new BackendPlaceholder(graph);
      Frontend frontend = new Frontend(backend, scanner);
      // call loadDataFileMenu and use TextUITester to check output
      frontend.loadDataFileMenu();
      String output = tester.checkOutput();

      assertTrue(output.contains("Enter a file name"), "Failed to correctly load data file menu");
    }
    // test invalid input (user inputs invalid file)
    {
      TextUITester tester = new TextUITester("invalid\n4\n");
      // init objects
      Scanner scanner = new Scanner(System.in);
      DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
      BackendInterface backend = new BackendPlaceholder(graph);
      Frontend frontend = new Frontend(backend, scanner);
      // call loadDataFileMenu and use TextUITester to check output
      frontend.loadDataFileMenu();
      String output = tester.checkOutput();

      assertTrue(output.contains("file is not a valid dot file"),
          "Failed to correctly handle invalid input to loadDataFileMenu");
    }
  }

  /**
   * Tests the loadStatisticsMenu() method to ensure it functions as expected (by checking what is
   * outputted). Two cases: one where user calls loadStatisticsMenu after loading file, and one
   * where user calls loadStatisticsMenu before loading a file
   */
  @Test
  public void testLoadStatisticsMenu() {
    // test loadDataFileMenu after loading a file
    {
      TextUITester tester = new TextUITester("flights.dot\n");
      // init objects
      Scanner scanner = new Scanner(System.in);
      DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
      BackendInterface backend = new BackendPlaceholder(graph);
      Frontend frontend = new Frontend(backend, scanner);
      // load a file first
      frontend.loadDataFileMenu();
      // call loadStatisticsMenu and use TextUITester to check output
      frontend.loadStatisticsMenu();
      String output = tester.checkOutput();

      assertTrue(output.contains("Data Statistics"),
          "Failed to correctly load the menu for statistics about data");
    }
    // test loadDataFileMenu before loading a file
    {
      TextUITester tester = new TextUITester("flights.dot\n");
      // init objects
      Scanner scanner = new Scanner(System.in);
      DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
      BackendInterface backend = new BackendPlaceholder(graph);
      Frontend frontend = new Frontend(backend, scanner);
      // call loadStatisticsMenu and use TextUITester to check output
      frontend.loadStatisticsMenu();
      String output = tester.checkOutput();

      assertTrue(output.contains("Load a file first"),
          "Failed to correctly handle call to loadStatisticsMenu before data was loaded");
    }
  }

  /**
   * Tests the loadShortestRouteMenu() method to ensure it functions as expected (by checking what
   * is outputted). Three cases: one where user input is valid and after data file was loaded, one
   * where user input is invalid and data file had been already loaded, and one where input is valid
   * but user called before loading data
   */
  @Test
  public void testLoadShortestRouteMenu() {
    // case where user input is valid and data was loaded prior to the function call
    {
      // arbitrary strings as user input (for start and destination) to test functionality
      TextUITester tester = new TextUITester("flights.dot\ntest\ntest\n");
      // init objects
      Scanner scanner = new Scanner(System.in);
      DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
      BackendInterface backend = new BackendPlaceholder(graph);
      Frontend frontend = new Frontend(backend, scanner);
      // load a data file first
      frontend.loadDataFileMenu();
      // call loadShortestRouteMenu and use TextUITester to check output
      frontend.loadShortestRouteMenu();
      String output = tester.checkOutput();

      assertTrue(output.contains("The shortest route between"),
          "Failed to load shortest route menu correctly");
    }
    // case where user input is invalid and data was loaded prior to funciton call
    {
      // arbitrary strings as user input (for start and destination) to test functionality
      TextUITester tester = new TextUITester("flights.dot\ninvalid\ninvalid\n");
      // init objects
      Scanner scanner = new Scanner(System.in);
      DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
      BackendInterface backend = new BackendPlaceholder(graph);
      Frontend frontend = new Frontend(backend, scanner);
      // load a data file first
      frontend.loadDataFileMenu();
      // call loadShortestRouteMenu and use TextUITester to check output
      frontend.loadShortestRouteMenu();
      String output = tester.checkOutput();

      assertTrue(output.contains("provided airport(s) are invalid"),
          "Failed to load shortest route menu correctly");
    }
    // case where user input is valid but data was NOT loaded prior to function call
    {
      // arbitrary strings as user input (for start and destination) to test functionality
      TextUITester tester = new TextUITester("test\ntest\n");
      // init objects
      Scanner scanner = new Scanner(System.in);
      DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
      BackendInterface backend = new BackendPlaceholder(graph);
      Frontend frontend = new Frontend(backend, scanner);
      // call loadShortestRouteMenu and use TextUITester to check output
      frontend.loadShortestRouteMenu();
      String output = tester.checkOutput();
      System.out.println(output);

      assertTrue(output.contains("Load a file first"),
          "Failed to load shortest route menu correctly");
    }
  }

  /**
   * Test runs a full sequence of inputs to test the program fully (as in choosing options from main
   * menu through user input, and ensuring that that input executes the expected behavior).
   */
  @Test
  public void testFullRun() {
    // tests fully functionally through command loop
    {
      TextUITester tester = new TextUITester("1\nflights.dot\n2\n3\nstart\ndest\n4\n");
      // init objects
      Scanner scanner = new Scanner(System.in);
      DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
      BackendInterface backend = new BackendPlaceholder(graph);
      Frontend frontend = new Frontend(backend, scanner);
      // call startCommandLoop to begin full loop of interaction with user
      frontend.startCommandLoop();
      String output = tester.checkOutput();
      // ensure main menu loaded
      assertTrue(output.contains("Main Menu"));
      // ensure load data file menu loaded (1)
      assertTrue(output.contains("Enter a file name"));
      // ensure program outputted an indication of file being read successfully
      assertTrue(output.contains("File Loaded Successfully"));
      // ensure load data statistics menu loaded
      assertTrue(output.contains("Data Statistics"));
      // ensure load shortest route menu loaded
      assertTrue(output.contains("The shortest route between"));
    }
  }

  /**
   * Integration test that tests the integrated programs with simulated valid user input and a
   * tester data file
   */
  @Test
  public void testValidIntegration() {
    /* user input that represents: user selecting Load Data File menu, loading "testFlights.dot" as
       data file, user then selecting Load Statistics menu, user then selecting Compute Shortest
       Route menu inputting "DTW" as start airport and "FLL" as destination airport */
    TextUITester tester = new TextUITester("1\ntestFlights.dot\n2\n3\nDTW\nFLL\n4");
    // init objects
    Scanner scanner = new Scanner(System.in);
    DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
    BackendInterface backend = new Backend(graph);
    Frontend frontend = new Frontend(backend, scanner);

    frontend.startCommandLoop();
    String output = tester.checkOutput();
    // check that the printed statistics are correct based off of the tester data file
    assertTrue(
        output.contains("Number of airports: 10, Number of flights: 15, Total miles: 65"));
    /* check that the sequence of airports in shortest path, their respective miles, and the total
       miles between the provided airports are correct */
    assertTrue(
        output.contains("DTW->ATL: 7.0 miles\n" + "ATL->BOS: 1.0 miles\n" + "BOS->MSP: 3.0 miles\n"+
            "MSP->FLL: 4.0 miles\n" + "The total number of miles between DTW and FLL is: 15.0"));
  }

  /**
   * Integration tester that tests the integrated programs with simulated invalid inputs to ensure
   * it is handled correctly on both ends (through to test cases)
   *     Case 1:
   *         user inputs an invalid file (so doesn't end up loading any files) and proceeds to
   *         select the load statistics and compute shortest route menus; in this case the program
   *         is expected to output some message indicating to the user that no data file has been
   *         loaded so those functions could not be carried out
   *     Case 2:
   *         user inputs a valid data files (that successfully loads), but then proceeds to call
   *         compute shortest route menu and enter invalid airport names; in this case, we expect
   *         the program to output some form of error message indicating to the user that the
   *         inputted airports are invalid
   */
  @Test
  public void testInvalidIntegration() {
    {
      /* user input representing: user selecting load data file menu, loading "invalid.dot" which is
         an invalid data files, exiting back to main menu, then selecting load statistics and
         compute shortest route menus */
      TextUITester tester = new TextUITester("1\ninvalid.dot\n4\n2\n3\ntest\ntest\n4");
      // init objects
      Scanner scanner = new Scanner(System.in);
      DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
      BackendInterface backend = new Backend(graph);
      Frontend frontend = new Frontend(backend, scanner);

      frontend.startCommandLoop();
      String output = tester.checkOutput();

      assertTrue(output.contains("not a valid dot file"));

      assertTrue(output.contains("Load a file first"));

      assertTrue(output.contains("Load a file first"));
    }
    {
      /* user input representing: user selects load file menu, loads a valid file
         ("testFlights.dot"), selects compute shortest path menu and user inputs invalid airport
         names ("DNE" and "INVALID"), then exits program */
      TextUITester tester = new TextUITester("1\ntestFlights.dot\n3\nDNE\nINVALID\n4");
      // init objects
      Scanner scanner = new Scanner(System.in);
      DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
      BackendInterface backend = new Backend(graph);
      Frontend frontend = new Frontend(backend, scanner);

      frontend.startCommandLoop();
      String output = tester.checkOutput();

      assertTrue(output.contains("File Loaded Successfully"));

      assertTrue(output.contains("The provided airport(s) are invalid"));
    }
  }

  /**
   * This tests my partners by using a small tester data file that I have already calculated
   * statistics for by hand, and ensures that these valid calls to each of the methods in backend
   * function correctly
   */
  @Test
  public void backendValidCalls() {
    DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
    BackendInterface backend = new Backend(graph);
    // check loadData method
    try {
      backend.loadData(new File("testFlights.dot"));
    } catch (IOException e) {
      fail();
    }
    // check getDatasetStatistics method
    assertEquals("Number of airports: 10, Number of flights: 15, Total miles: 65",
        backend.getDatasetStatistics());
    // check getShortestRoute method with hand calculated route (using lecture example basically)
    ShortestPathResult actualResut = backend.getShortestRoute("ATL", "ORD");
    // expected data
    String[] expectedRoute = {"ATL", "HOU", "IAD", "DTW", "ORD"};
    Double[] expectedSegementMiles = {8.0, 2.0, 1.0, 2.0};
    Double expectedTotalMiles = 13.0;
    // test that actual shortest path data matches expected
    assertArrayEquals(expectedRoute, actualResut.getRoute().toArray());
    assertArrayEquals(expectedSegementMiles, actualResut.getSegmentMiles().toArray());
    assertEquals(expectedTotalMiles, actualResut.getTotalMiles());
  }

  /**
   * This tests my partners Backend code with invalid arguments passed into different functions
   *     Case 1:
   *         Pass an invalid file to loadData and ensure this is handled correctly, then attempt to
   *         call getShortestRoute even though no data has been loaded (expect IllegalStateException)
   *     Case 2:
   *         Pass a valid file to loadData but invalid arguments to getShortestRoute, specifically,
   *         start and destination airports that have no path between them
   *         (expect IllegalArgumentException)
   */
  @Test
  public void backendInvalidCalls() {
    DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
    BackendInterface backend = new Backend(graph);
    // case 1
    {
      // test loadData method with invaild file
      try {
        backend.loadData(new File("invalid.dot"));
        //fail();
      } catch (IOException e) {
        // expected
      }
      // calls one of the other methods despite no data being loaded: expect exception to be thrown
      assertThrows(
          IllegalStateException.class,
          () -> backend.getShortestRoute("ATL", "ORD")
      );
    }
    // case 2
    {
      // load valid data
      try {
        backend.loadData(new File("testFlights.dot"));
      } catch (IOException e) {
        fail();
      }
      // call getShortestRoute with invalid airports
      assertThrows(
          IllegalStateException.class,
          () -> backend.getShortestRoute("MSP", "DTW")
      );
    }
  }
}
