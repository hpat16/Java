/**
 * Drives an interactive loop of prompting the user to select a command, then requests any required
 * details about that command from the user, and then displays the results of the command.
 */
public interface FrontendInterface {

  /**
   * Constructor for frontend
   * (accepts a reference to the backend and a java.util.Scanner instance to read user input)
   */
  // public Frontend (BackendInterface backend, Scanner scnr) {}

  /**
   * Starts the main command loop for the user
   */
  public void startCommandLoop();

  /**
   * Creates and outputs the main menu of the command loop to the user, and based on their
   * selection, it calls the correct function
   */
  public void mainMenu();

  /**
   * loads sub-menu for a command to specify (and load) a data file
   *
   * if any errors are thrown (from backend) when reading through the file, this should print out
   * some error message describing that the file could not be load, and exit out of this sub-menu
   * (so it shouldn't continue to prompt the user until they've entered a valid file name, but
   * instead go back to the main menu, and if the user wants to attempt to load a file again, they
   * should choose that option from the main menu
   */
  public void loadDataFileMenu();

  /**
   * a command to show statistics about the dataset that includes the number of airports (nodes),
   * the number of edges (flights), and the total number of miles (sum of all edge weights) in the
   * graph
   *
   * if no file has been loaded yet, this should print out some error message to the user
   * instructing them to first load data
   */
  public void loadStatisticsMenu();

  /**
   * load sub-menu for a a command that asks the user for a start and destination airport, then
   * lists the shortest route between those airports, including all airports on the way, the
   * distance for each segment, and the total number of miles from start to destination airport
   *
   * if no file has been loaded yet, this should print out some error message to the user
   * instructing them to first load data
   */
  public void loadShortestRouteMenu();

  /**
   * a command to exit the app
   */
  public void exitApp();

}
