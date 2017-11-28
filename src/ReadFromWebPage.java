import com.jaunt.*;
import java.util.ArrayList;
import java.util.List;

public class ReadFromWebPage {
    private  final String baseurl = "https://dealsea.com/search?q=";
    private  String keyword = "router";
    private  final String endurl = "&search_mode=Deals";
    private  List<String> items = new ArrayList<>();
    private  List<String> prices = new ArrayList<>();
    private  List<Item> deals = new ArrayList<>();
    
    
    public  List<Item> getItems(){
    	
    	return deals;
    }
    
  
	public void setKeyword(String key){
		keyword = key;
	}
	
	

    /*
    * This method creates headless browser, and searches for all the specified HTML tags.
    */
    public void webScrapeItems() throws Exception
    {
        UserAgent userAgent = new UserAgent();  //create new headless browser
        userAgent.visit(baseurl + keyword + endurl);

        Elements itemsText = userAgent.doc.findEvery("<div class=\"posttext\">");

        for (Element item : itemsText)
        {

            items.add(item.findFirst("<a>").getText() + item.getText()); // first a tag has the full description
        }

        for (Element price : itemsText)
        {

            prices.add(price.findFirst("<b>").getText()); // first b tag has the price
        }
    }

    /*
    * This method parses the text that was scraped from a webpage,
    * and passes it to a new Item object.
    */
    public  void createItems()
    {
        for (int i = 0; i < items.size() - 1; i++)
        {
            String location = constructItemLocation();
            String description = items.get(i).split("\n")[0];
            String price = prices.get(i);
            deals.add(new Item(location, description, price));
        }
    }

    /*
    * For each deal in the list, print where link where the item can be found,
    * the item description, and the item price.
    */
    public void printDeals()
    {
        for (Item deal : deals)
        {
            System.out.println(deal.itemLocation + "\n" + deal.description + "\n" + deal.price);
            System.out.println("-----------------------------");
        }

    }



    /*
    * This method simply formats the data into a String that says where the item
    * can be found.
    */
    private  String constructItemLocation()
    {
        return "Found deals on " + keyword + "(s). Find them here: " + baseurl + keyword + endurl;
    }


}
