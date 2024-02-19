import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Drives an interactive loop of prompting the user to select a command, then requests any required
 * details about that command from the user, and then displays the results of the command.
 */
public class Frontend implements FrontendInterface {
  BackendInterface backend;
  Scanner scnr;
  boolean isRunning; // allows program to know when to stop

  /**
   * Constructor that accepts a reference to the backend and a java.util.Scanner instance to read
   * user input
   * @param backend reference to object that implements BackendInterface
   * @param scnr reference to java.util.Scanner instance to read user input
   */
  public Frontend (BackendInterface backend, Scanner scnr) {
    this.backend = backend;
    this.scnr = scnr;
    this.isRunning = true;
  }

  /**
   * Starts the main command loop for the user
   */
  @Override
  public void startCommandLoop() {
    while(this.isRunning) {
      mainMenu();
    }
  }

  /**
   * Creates and outputs the main menu of the command loop to the user, and based on their
   * selection, it calls the correct function
   */
  @Override
  public void mainMenu() {
    // string holds main menu options
    String options = "\nMain Menu:\n" +
                     "\n1: Load Data File" +
                     "\n2: Load Statistics" +
                     "\n3: Compute Shortest Route" +
                     "\n4: Exit App\n";
    System.out.println(options);
    // get user input, check if it is a valid integer and option
    int input = 0;
    if (scnr.hasNextInt()) { // check if input is integer
      input = scnr.nextInt();
      if (input < 1 || input > 4) { // if integer is not a valid options, print error message
        System.out.println("\"" + input + "\"" + " is not a valid option, try again.");
      }
    } else { // if input isn't an integer, print error message
      System.out.println("\"" + scnr.next() + "\"" + " is not an integer, try again.");
    }
    // execute user desired function
    switch (input) {
      case 1:
        this.loadDataFileMenu();
        break;
      case 2:
        this.loadStatisticsMenu();
        break;
      case 3:
        this.loadShortestRouteMenu();
        break;
      case 4:
        this.exitApp();
        break;
      default:
        break;
    }
  }

  /**
   * loads sub-menu for a command to specify (and load) a data file
   *
   * if any errors are thrown (from backend) when reading through the file, this should print out
   * some error message describing that the file could not be load, and exit out of this sub-menu
   * (so it shouldn't continue to prompt the user until they've entered a valid file name, but
   * instead go back to the main menu, and if the user wants to attempt to load a file again, they
   * should choose that option from the main menu
   */
  @Override
  public void loadDataFileMenu() {
    // prompt user to enter a file name
    System.out.println("Enter a file name:\n");
    boolean isValidFile = false;
    while (!isValidFile) { // prompt user for file name until a valid file name isn't inputted
      // option to exit back to main menu
      if (scnr.hasNextInt() && scnr.nextInt() == 4) {
        System.out.println("No data was loaded, returning to main menu...");
        return;
      }

      try {
        backend.loadData(new File(scnr.next()));
        isValidFile = true;
      } catch (IOException e) { // exception means invalid file, prompt user to try again
        System.out.println("The provided file is not a valid dot file, enter \"4\" to return to main" +
            " menu, or enter another file name:");
      }
    }

    System.out.println("File Loaded Successfully!");
  }

  /**
   * a command to show statistics about the dataset that includes the number of airports (nodes),
   * the number of edges (flights), and the total number of miles (sum of all edge weights) in the
   * graph
   *
   * if no file has been loaded yet, this should print out some error message to the user
   * instructing them to first load data
   */
  @Override
  public void loadStatisticsMenu() {
    System.out.println("\nData Statistics Summary:");
    try { // command backend to provide statistics
      String statistics = backend.getDatasetStatistics();
      System.out.println(statistics);
    } catch (IllegalStateException e) { // inform user if no data file has been loaded yet
      System.out.println("Load a file first to generate statistics.");
    }
  }

  /**
   * load sub-menu for a a command that asks the user for a start and destination airport, then
   * lists the shortest route between those airports, including all airports on the way, the
   * distance for each segment, and the total number of miles from start to destination airport
   *
   * if no file has been loaded yet, this should print out some error message to the user
   * instructing them to first load data
   */
  @Override
  public void loadShortestRouteMenu() {
    String startAirport = null;
    String destinationAirport = null;
    // get user input (start and destination airports)
    System.out.println("Enter a start airport:");
    startAirport = scnr.next();
    System.out.println("Enter a destination airport:");
    destinationAirport = scnr.next();
    // get shortest route through backend
    try {
      ShortestPathResult shortestPath = backend.getShortestRoute(startAirport, destinationAirport);
      // start statement
      String output = "\nThe shortest route between " + startAirport + " and " + destinationAirport +
          " is:\n";
      // formats data to be printed in the following format: "ORD--RNO: 1671 miles\n..."; so it
      // pairs two adjacent airports and gives their corresponding distance, and repeats this
      for (int i = 0; i < shortestPath.getSegmentMiles().size(); i++) {
        output += shortestPath.getRoute().get(i) + "->" + shortestPath.getRoute().get(i + 1) + ": "+
            shortestPath.getSegmentMiles().get(i) + " miles\n";
      }
      // closing statement: total number of miles
      output += "The total number of miles between " + startAirport + " and " + destinationAirport +
          " is: " + shortestPath.getTotalMiles();
      System.out.println(output + "\n");
    } catch (IllegalStateException e) {
      // inform user if no data file has been loaded yet, or if no path was found between the
      // provided airports
      if (e.getMessage().contains("Data must be loaded")) {
        System.out.println("Load a file first to compute shortest path.");
      } else {
        System.out.println("No path was found between " + startAirport + " and " +
            destinationAirport);
      }
    } catch (IllegalArgumentException e) { // inform user if the provided airports were invalid
      System.out.println("The provided airport(s) are invalid, try again with valid airport names.");
    } catch (Exception e) { // handle any unexpected errors
      System.out.println("Unexpected Error... Exiting App.");
      this.exitApp();
    }

  }

  /**
   * a command to exit the app
   */
  @Override
  public void exitApp() {
    this.isRunning = false;
    this.scnr.close();
    System.out.println("Exiting App... Goodbye!");
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
    BackendInterface backend = new Backend(graph);
    Frontend frontend = new Frontend(backend, scanner);

    frontend.startCommandLoop();
  }
}
