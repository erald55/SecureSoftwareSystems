import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class webCrawler {
    private HashSet<String> links;
    //static String mainUrl="https://www.politico.com/";
    static String mainUrl="https://www.newegg.com/";
    //static String mainUrl="https://old.reddit.com/r/leagueoflegends/";


    public webCrawler() {
        links = new HashSet<String>();
    }

    public void getPageLinks(String URL) {
        //4. Check if you have already crawled the URLs
        //(we are intentionally not checking for duplicate content in this example)
        if (!links.contains(URL) && URL.contains(mainUrl)) {
            try {
                //4. (i) If not add it to the index
                if (links.add(URL) && (URL.contains("Product") || URL.contains("Item"))) {
                    System.out.println(URL);
                }

                //2. Fetch the HTML code
                Document document = Jsoup.connect(URL).get();
                //3. Parse the HTML to extract links to other URLs
                Elements linksOnPage = document.select("a[href]");

                //5. For each extracted URL... go back to Step 4.
                for (Element page : linksOnPage) {
                    getPageLinks(page.attr("abs:href"));
                }
            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        //Adds robots.txt to the site url in order to access the robots.txt file
        String robotsUrl=mainUrl +"robots.txt";

        //Allows us to access the robots.txt url
        Document robotDocument = Jsoup.connect(robotsUrl).get();

        //searching the robots.txt file in order to make the crawler follow the rules
        Elements robotsOnPage = robotDocument.body().getElementsMatchingText("User-agent: \\* Disallow: \\/\\w");
        if (robotsOnPage.size()!=0){
            System.out.println("Yeahh boy");
            new webCrawler().getPageLinks("https://www.newegg.com/p/pl?d=monitor#");
        }



    }
}
