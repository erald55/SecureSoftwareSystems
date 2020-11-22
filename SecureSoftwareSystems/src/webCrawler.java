import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class webCrawler {
    private HashSet<String> links;

    public webCrawler() {
        links = new HashSet<String>();
    }

    public void getPageLinks(String URL) {
        //4. Check if you have already crawled the URLs
        //(we are intentionally not checking for duplicate content in this example)
        if (!links.contains(URL)) {
            try {
                //4. (i) If not add it to the index
                if (links.add(URL)) {
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
        //1. Pick a URL from the frontier
        String mainUrl="https://old.reddit.com/r/leagueoflegends/";
        String robotsUrl=mainUrl +"robots.txt";

        Document robotDocument = Jsoup.connect(robotsUrl).get();
        String robotsBody=robotDocument.body().text();
        String robotDeny="User-agent: * Disallow: /";
        if (robotsBody.equalsIgnoreCase(robotDeny)){
            System.out.println("yeahh boyyy");
        }

        /*Elements robotsOnPage = robotDocument.body().getElementsContainingText(("User-agent: *" ));
        if (robotsOnPage.size()>0){
            System.out.println("Yeahh boy");

        }*/

        //new webCrawler().getPageLinks(mainUrl);

    }
}
