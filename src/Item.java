public class Item {
	
	 	String itemLocation = "";
	    String description = "";
	    String price = "";
	    
	    public String getDes(){
	    	
	    	
	    	return description;
	    }

	    public Item(String itemLocation, String description, String price)
	    {

	        this.itemLocation = itemLocation;
	        this.description = description;
	        this.price = price;
	    }

}
