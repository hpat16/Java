Java Projects Repository

This repository contains multiple Java projects focusing on different aspects of software development. Each project is described briefly below:

Meteorite Catcher App

The Meteorite Catcher app helps users quickly search through and list known meteorites by their mass. It uses a Red-Black Tree for efficient insertion and retrieval of meteorite data. The data is loaded from a local CSV file specified by the user.

Representative Tasks:

Find the meteorite with the highest mass in the dataset.
Search for and list all meteorites between a specified mass range.
Backend Developer (BD) Role:

Reads meteorite data from a CSV file.
Inserts data into a Red-Black Tree.
Exposes functionality to the frontend for data retrieval.
Frontend Developer (FD) Role:

Drives an interactive loop for user commands.
Requests details from the user for each command.
Displays the results of each command.
Flight Router App

The Flight Router app enables users to find the shortest sequence of flights between a start and destination airport. It uses Dijkstra's shortest path algorithm to find the route in a graph where airports are nodes and flights are edges. Data is loaded from a local file specified by the user.

Representative Tasks:

Search for and list flights between two specified airports.
Show statistics about the dataset, including the number of airports, flights, and total miles.
Backend Developer (BD) Role:

Reads graph data from a DOT file.
Inserts data into a graph data structure.
Exposes functionality to the frontend for route finding and dataset statistics.
Frontend Developer (FD) Role:

Drives an interactive loop for user commands.
Requests details from the user for each command.
Displays the results of each command.
WebServer Activity

The WebServer activity implements a basic web server that reads HTTP request parameters and generates a response. It demonstrates handling HTTP requests and responses using Java.

Setup Instructions:

Clone the repository and navigate to the WebServer folder.
Compile and run the HelloWebServer code.
Open a new terminal window and make client requests using the curl command.
For detailed installation and usage instructions for each project, refer to their respective folders.
