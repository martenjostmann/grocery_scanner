package de.grocery_scanner.helper.scraper;



import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
public class webScraper implements Runnable{

    private String url;
    private Document doc;


    public webScraper(String url){
        this.url = url;
    }

    public void run() {
        scrape();
    }

    public void scrape(){
        try {
            doc = Jsoup.connect(this.url).get();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Document returnDocument(){
        return doc;
    }
}
