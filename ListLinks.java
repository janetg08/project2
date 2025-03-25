// Compile: javac -cp ".:libs/jsoup-1.19.1.jar" ListLinks.java
// Run: java -cp ".:libs/jsoup-1.19.1.jar" ListLinks "http://www.hunter.cuny.edu/"
// Run the Jar: java -cp "ListLinks.jar:libs/jsoup-1.19.1.jar" ListLinks "http://www.hunter.cuny.edu/"

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
Used ListLinks.java with named modifications to complete the Level 1 Goal.
 */
public class ListLinks {
    public static void main(String[] args) throws IOException {
        Validate.isTrue(args.length == 1, "usage: supply url to fetch");
        String url = args[0];
        print("Fetching %s...", url);

        // fetch the document
        Document doc = Jsoup.connect(url).get();

        // print document title
        String pageTitle = doc.title();
        print("Page Title: %s", pageTitle);

        // select all links
        Elements links = doc.select("a[href]");

        print("\nLinks: (%d)", links.size());
        for (Element link : links) {
            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        }
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
}
