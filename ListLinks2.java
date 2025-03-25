// Compile: javac -cp ".:libs/jsoup-1.19.1.jar" ListLinks2.java
// Run: java -cp ".:libs/jsoup-1.19.1.jar" ListLinks2 "http://www.hunter.cuny.edu/"

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

public class ListLinks2 {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("usage: supply url to fetch");
            return;
        }
        
        String url = args[0];
        System.out.println("Fetching " + url + "...");

        // fetch the document
        Document doc = Jsoup.connect(url).get();

        // print document title
        String pageTitle = doc.title();
        System.out.println("Page Title: " + pageTitle);

        // select all links
        Elements links = doc.select("a[href]");

        System.out.println("\nLinks: (" + links.size() + ")");
        
        // filter links for hunter.cuny.edu domain and relative links
        for (Element link : links) {
            String absHref = link.attr("abs:href");

            // Check if the link contains hunter.cuny.edu and is not a deeper subdomain
            if (isValidHunterLink(absHref)) {
                // Make sure the URL is relative to the base URL
                String relativeUrl = getRelativeUrl(absHref, url);
                System.out.println(" * a: <" + relativeUrl + ">  (" + trim(link.text(), 35) + ")");
            }
        }
    }

    // Check if the URL contains "hunter.cuny.edu" but is not a subdomain
    private static boolean isValidHunterLink(String link) {
        try {
            URI uri = new URI(link);
            String host = uri.getHost().toLowerCase();

            // Allow "hunter.cuny.edu" and "www.hunter.cuny.edu"
            if (host.equals("hunter.cuny.edu") || host.equals("www.hunter.cuny.edu")) {
                return true;
            }

            // Block deeper subdomains (e.g., roosevelthouse.hunter.cuny.edu)
            String[] hostParts = host.split("\\.");
            return hostParts.length == 3 && hostParts[0].equals("hunter") && hostParts[1].equals("cuny") && hostParts[2].equals("edu");

        } catch (Exception e) {
            return false;
        }
    }

    // Get relative URL based on the base URL
    private static String getRelativeUrl(String absHref, String baseUrl) {
        try {
            // If the link is already relative, return it as it is
            if (!absHref.startsWith("http")) {
                return absHref;
            }

            URI baseUri = new URI(baseUrl);
            URI linkUri = new URI(absHref);
            
            // If the domain matches, return the path relative to the base
            if (baseUri.getHost().equals(linkUri.getHost())) {
                // Build the relative URL (path part only, excluding the domain)
                return linkUri.getPath();
            }
            
            // Otherwise, return the original absolute URL
            return absHref;
        } catch (Exception e) {
            return absHref; // Return original if something goes wrong
        }
    }

    // Helper function to trim text for display
    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width - 1) + ".";
        else
            return s;
    }
}
