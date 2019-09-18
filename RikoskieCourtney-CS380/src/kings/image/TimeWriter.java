package kings.image;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Writes to the ReadMe file.
 * 
 * @author CourtneyRikoskie
 * @version 09-03-19
 */
public class TimeWriter {
	/**
	 * Writes the time spent on an algorithm on the first line of the ReadMe file.
	 * 
	 * @param algorithm
	 *            The algorithm.
	 * @param timeSpent
	 *            The time spent.
	 * @throws IOException
	 *             Thrown when the ReadMe file cannot be written to.
	 */
	public static void writeToReadMe(String algorithm, long timeSpent) throws IOException {
		Scanner read = new Scanner(new File("README.md"));
		ArrayList<String> list = new ArrayList<String>();
		
		while (read.hasNextLine()) {
			list.add(read.nextLine());
		}
		
		FileWriter fw = new FileWriter("README.md", false);
		fw.write("Time spent on " + algorithm.toLowerCase() + " algorithm: " + Math.round(timeSpent) + " ms\n");
		
		for (int line = 1; line < list.size(); line += 1) {
			fw.write(list.get(line) + "\n");
		}

		fw.close();
		read.close();
	}
}
