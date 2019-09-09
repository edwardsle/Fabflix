import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LogParser {

	public static void main(String[] args) {
		File file = new File("scaled-case4.txt");
		long tj = 0;
		long ts = 0;
		double count = 0;
		
	    Scanner scanner;
		try {
			scanner = new Scanner(file);
		 	while (scanner.hasNextLine())
		 	{
		 		count++;
		 		String line = scanner.nextLine();
		 		line = line.replaceAll("ts:", "");
		 		line = line.replaceAll("tj:", "");
		 		String[] tokens = line.split("-");
		 		
		 		long lineTs = Long.parseLong(tokens[0]);
		 		long lineTj = Long.parseLong(tokens[1]);
		 		
		 		tj += lineTj;
		 		ts += lineTs;
		 	}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		System.out.println(tj);
		System.out.println(ts);
		
		float avgTj = (float) ((tj / count) / 1000000.0); // converted to ms
		float avgTs = (float) ((ts / count) / 1000000.0);// converted to ms
		
		System.out.println(avgTj);
		System.out.println(avgTs);
		
	  }
}

