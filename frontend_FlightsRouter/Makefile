runFDTests:
	javac -cp ../junit5.jar: FrontendDeveloperTests.java
	java -jar ../junit5.jar -cp . -c FrontendDeveloperTests

runMain:
	javac *.java -cp ../junit5.jar
	java Frontend

runBDTests:
	javac -cp ../junit5.jar: BackendDeveloperTests.java
	java -jar ../junit5.jar -cp . -c BackendDeveloperTests

clean:
	rm *.class
