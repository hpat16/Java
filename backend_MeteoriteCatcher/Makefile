runBDTests:
	@ `javac -cp .:../junit5.jar BackendDeveloperTests.java` 
	@ java -jar ../junit5.jar -cp . -c BackendDeveloperTests

runMain:
	@ javac *.java -cp ../junit5.jar
	@ java Backend

runFDTests:
	@ javac *.java -cp ../junit5.jar
	@ javac -cp ../junit5.jar: FrontendDeveloperTests.java
	@ java -jar ../junit5.jar -cp . -c FrontendDeveloperTests	

clean:
	@ rm *.class
