package datascraping.scraping;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NftPlazasScraper implements Scraper{
    @Override
    public Map<String, JSONObject> scrape() {
        final String url = "https://nftplazas.com/nft-collectibles-news/";
        Map<String, JSONObject> pageDataMap = null;
        try {
            pageDataMap = new HashMap();

            Document doc = Jsoup.connect(url).userAgent("Jsoup client").get();
            Elements links = doc.select("article > a");

            for (Element e : links) {
                String link = e.attr("href");
                Document doc1 = Jsoup.connect(link).userAgent("Jsoup client").get();
                Element divElement = doc1.select("div.content").first();

                if (divElement != null) {
                    JSONObject pageElementsMap = new JSONObject();
                    Set<String> h1Set = new HashSet();
                    Set<String> h2Set = new HashSet();
                    Set<String> h3Set = new HashSet();
                    Set<String> h4Set = new HashSet();
                    Set<String> pSet = new HashSet();
                    Set<String> liSpanSet = new HashSet();
                    Set<String> imgSrcSet = new HashSet();
                    Set<String> aHrefSet = new HashSet();
                    Set<String> iframeSrcSet = new HashSet();

                    // Select 'h1', 'h2', 'h3', 'p', 'span', 'img', 'a', and 'iframe' elements inside the 'div'
                    Elements elements = divElement.select("h1, h2, h3, h4, p, span, img, a, iframe");

                    for (Element element : elements) {
                        String tagName = element.tagName();
                        String text = element.text();
                        switch (tagName) {
                            case "h1":
                                h1Set.add(text);
                                break;
                            case "h2":
                                h2Set.add(text);
                                break;
                            case "h3":
                                h3Set.add(text);
                                break;
                            case "h4":
                                h4Set.add(text);
                                break;
                            case "p":
                                pSet.add(text);
                                break;
                            case "span":
                                liSpanSet.add(text);
                                break;
                            case "img":
                                String src = element.attr("src");
                                imgSrcSet.add(src);
                                break;
                            case "a":
                                String aHref = element.attr("href");
                                aHrefSet.add(aHref);
                                break;
                            case "iframe":
                                String iframeSrc = element.attr("src");
                                iframeSrcSet.add(iframeSrc);
                                break;
                        }
                    }

                    // Add the sets to the JSON object
                    pageElementsMap.put("h1", new JSONArray(h1Set));
                    pageElementsMap.put("h2", new JSONArray(h2Set));
                    pageElementsMap.put("h3", new JSONArray(h3Set));
                    pageElementsMap.put("h4", new JSONArray(h4Set));
                    pageElementsMap.put("p", new JSONArray(pSet));
                    pageElementsMap.put("span", new JSONArray(liSpanSet));
                    pageElementsMap.put("imgSrc", new JSONArray(imgSrcSet));
                    pageElementsMap.put("aHref", new JSONArray(aHrefSet));
                    pageElementsMap.put("iframeSrc", new JSONArray(iframeSrcSet));

                    // Put the JSON object in the pageDataMap
                    pageDataMap.put(link, pageElementsMap);
                }
            }

            // Print or use the pageDataMap as needed
            for (Map.Entry<String, JSONObject> entry : pageDataMap.entrySet()) {
                String link = entry.getKey();
                JSONObject elementsJson = entry.getValue();

                System.out.println("Link: " + link);
                System.out.println("Data: " + elementsJson.toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return pageDataMap;
    }
}