import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;
import java.io.File;

public class MyWebServer {
    public static void main(String[] args) throws IOException {
    // Note that we changed this port number from 8000 to 80
	// to make this web server publicly accessible. And that
	// for A12 this should be left at 8000
	InetSocketAddress address = new InetSocketAddress(80);
	HttpServer server = HttpServer.create(address, 10);
	
	HttpContext context = server.createContext("/test");
	context.setHandler( exchange -> {
		String query = exchange.getRequestURI().getQuery();
		System.out.println("received: "+query);
		exchange.sendResponseHeaders(200,0);
		OutputStream out = exchange.getResponseBody();
		out.write("Hello Http Requester!".getBytes());
		out.close();
	    });
	
	context = server.createContext ("/tree.xml");
	context.setHandler( exchange -> {
	    System.out.println("received tree request");
	    exchange.sendResponseHeaders(200, 0);
	    exchange.getResponseHeaders().add("Content-type", "text/xml");
	    OutputStream out = exchange.getResponseBody();
	    String response = "";
	    Scanner fin = new Scanner(new File("tree.xml"));
	    while( fin.hasNextLine() ) response += fin.nextLine() + "\n";
	    out.write(response.getBytes());
	    out.close();
	});

        context = server.createContext ("/index.html");
        context.setHandler( exchange -> {
            System.out.println("received html request");
            exchange.sendResponseHeaders(200, 0);
            exchange.getResponseHeaders().add("Content-type", "text/html");
            OutputStream out = exchange.getResponseBody();
            String response = "";
            Scanner fin = new Scanner(new File("index.html"));
            while( fin.hasNextLine() ) response += fin.nextLine() + "\n";
            out.write(response.getBytes());
            out.close();
        });

        context = server.createContext ("/style.css");
        context.setHandler( exchange -> {
            System.out.println("received style request");
            exchange.sendResponseHeaders(200, 0);
            exchange.getResponseHeaders().add("Content-type", "text/css");
            OutputStream out = exchange.getResponseBody();
            String response = "";
            Scanner fin = new Scanner(new File("style.css"));
            while( fin.hasNextLine() ) response += fin.nextLine() + "\n";
            out.write(response.getBytes());
            out.close();
	});

	context = server.createContext ("/sumPage");
        context.setHandler( exchange -> {
	    String query = exchange.getRequestURI().getQuery();
            System.out.println("received sumPage request");
	    String[] parts = query.split("&|=");
	    int a = Integer.parseInt(parts[1]);
	    int b = Integer.parseInt(parts[3]);
	    int sum = a + b;
	    
            exchange.sendResponseHeaders(200, 0);
            exchange.getResponseHeaders().add("Content-type", "text/html");
            OutputStream out = exchange.getResponseBody();
            String response = "";
            Scanner fin = new Scanner(new File("index.html"));
            while( fin.hasNextLine() ) response += fin.nextLine() + "\n";

	    response = response.replace("???",a + " + " + b + " = " + sum);
	    
            out.write(response.getBytes());
            out.close();
        });

	context = server.createContext ("/sumNumber");
        context.setHandler( exchange -> {
            String query = exchange.getRequestURI().getQuery();
            System.out.println("received sumNumber request");
            String[] parts = query.split("&|=");
            int a = Integer.parseInt(parts[1]);
            int b = Integer.parseInt(parts[3]);
            int sum = a + b;

            exchange.sendResponseHeaders(200, 0);
            exchange.getResponseHeaders().add("Content-type", "text/html");
            OutputStream out = exchange.getResponseBody();
            String response = "";

            response = a + " + " + b + " = " + sum;

            out.write(response.getBytes());
            out.close();
        });

	server.start();
    }
}
