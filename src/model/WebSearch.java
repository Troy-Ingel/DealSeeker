package model;

import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.UserAgent;

import java.util.ArrayList;
import java.util.List;

public class WebSearch
{
    private final String baseUrl = "https://dealsea.com/search?n=";
    private final String paramUrl = "&q=";
    private String keyword = "";
    private final List<String> deals = new ArrayList<>();
    private final List<Integer> dealNumber = new ArrayList<>();
    private final List<String> images = new ArrayList<>();
    private final List<Item> items = new ArrayList<>();
    private int totalCount = 0;

    public WebSearch(String keyword)
    {
        this.keyword = keyword;

        try
        {
            webScrapeItems();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public List<Item> getItems()
    {

        return items;
    }

    /*
    * This method creates headless browser, and searches for all the specified HTML tags.
    */
    private void webScrapeItems() throws Exception
    {
        UserAgent userAgent = new UserAgent();  //create new headless browser
        userAgent.visit(baseUrl + totalCount + paramUrl + keyword);
        Element itemsHeader = userAgent.doc.findFirst("<td align=\"center\"");
        String[] itemCountText = itemsHeader.getText().split(" ");
        totalCount = Integer.parseInt(itemCountText[itemCountText.length - 1]);


        int currCount = 0;
        while (currCount < totalCount)
        {
            userAgent.visit(baseUrl + currCount + paramUrl + keyword);
            Elements dealBoxes = userAgent.doc.findEvery("<div class=\"dealbox\">");

            if (dealBoxes.size() == 0)
            {
                break;
            }

            for (int i = 0; i < dealBoxes.size(); i++)
            {
                if (dealBoxes.getElement(i).findEvery("<span class=\"colr_red xxsmall\">").size() == 0)
                {
                    deals.add(dealBoxes.getElement(i)
                                       .findFirst("<div class=\"dealcontent\">")
                                       .findFirst("<a>")
                                       .getText());

                    images.add(dealBoxes.getElement(i)
                                        .findFirst("<div class=\"prodimage\">")
                                        .findFirst("<img>")
                                        .getAt("src"));

                    dealNumber.add(currCount + i);
                }
            }

            currCount += dealBoxes.size();
        }

        createItems();
    }

    /*
    * This method parses the text that was scraped from a webpage,
    * and passes it to a new model.Item object.
    */
    private void createItems()
    {
        for (int i = 0; i < deals.size(); i++)
        {
            String location = baseUrl + dealNumber.get(i) + paramUrl + keyword;
            String description = deals.get(i).split("\n")[0];
            String img = images.get(i);
            items.add(new Item(location, description, img));
        }
    }
}
