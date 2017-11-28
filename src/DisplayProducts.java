import java.awt.Container;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.concurrent.TimeUnit;




public class DisplayProducts {

	
	DisplayProducts(String name,int time){
		RunSearch(name, time);
	}
	
	/*
	 * Call the search for a user input item and display the results in a window, run again every  user input time
	 */
	void RunSearch(String name,int time){
		
		JFrame frameA = new JFrame("Deals Found");
	
		ReadFromWebPage rfwp = new ReadFromWebPage();
		
		rfwp.setKeyword(name);
		try{
		rfwp.webScrapeItems();
		rfwp.createItems();
		} catch(Exception e){
			System.out.println("Coundent WebScrape");
		}
		List<Item> deals = rfwp.getItems();
		
		frameA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameA.setBounds(200, 100, 800, 800);
		Container container = frameA.getContentPane();
		
		container.setLayout(null);
		
		JLabel Title = new JLabel("Deals Found On "+ name+" At www.dealsea.com");
		Title.setBounds(60,5,250,30);
		container.add(Title);
		
		for(int x = 0; x < deals.size(); ++x){
		JLabel DealTitle = new JLabel(deals.get(x).getDes());
		DealTitle.setBounds(60,30+(x*40),1200,30);
			container.add (DealTitle);
		}
		
		frameA.setVisible(true);
		try{
		TimeUnit.SECONDS.sleep(time);
		}catch (Exception e){
			System.out.println("Coundent wait");
		}
		frameA.removeAll();
		RunSearch(name,time);
	}
}
