import java.util.Scanner;

public class ReadFromWebMain {

	/*
	 * Take the user inputs and then run a call to search for and display items
	 */
	public static void main(String[] args) {
		
		Scanner s = new Scanner(System.in);
		
		System.out.println("Enter the item you are searching for, press enter then enter the time (in seconds)"
				+ "that you want to wait between searches");
		
		
		new DisplayProducts(s.nextLine(),s.nextInt());
		s.close();
		

	}
}
