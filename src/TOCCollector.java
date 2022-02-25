import java.io.*;
import java.util.*;

public class TOCCollector{
	
	
	public static void main(String [] args) throws Exception {
		
		// create a DriverLicenseExam object to dereference the main function to start an exam
		TOC toc = new TOC();
		toc.generate();
	}
	
}

class TOC {
	// instance variable to store file path
	String filepath;
	String neededSheetNumFileName;
	HashMap<String, String> map = new HashMap();
	String tocfilename = "TOC.txt";
	
	public void generate() {
		readAllFilesPath();
		collectNeededSheetNums();
		buildMap();
		String[][] toc = generateTOC();
		writeToFile(toc);
	}
	
	private void writeToFile(String[][] toc) {
		try {
			PrintWriter pw = new PrintWriter(tocfilename);
			for(int i = 0; i < toc.length; ++i) {
				pw.println(toc[i][0] + " " + toc[i][1].substring(8));
			}
			pw.close();
			System.out.println("TOC is generated and saved as: " + tocfilename);
		}
		catch(FileNotFoundException e) {
			System.out.println("error while generating report" + e.getMessage());
		}
		catch (IllegalArgumentException e) {
			System.out.println("error while generating report" + e.getMessage());
		}
	}
	
	/**
	 * create a 2D array from Hashmap, and sort the array by sheet index
	 * @return a sorted 2D array of Strings of sheet number and sheet index, sorted by sheet number in ascending order
	 */
	private String[][] generateTOC() {
		String[][] toc = new String[map.size()][2];
		int i = 0;
		for(String sheetIdx : map.keySet()) {
			toc[i][0] = sheetIdx;
			toc[i++][1] = map.get(sheetIdx);
		}
		// sort toc by sheet index in ascending order
		Arrays.sort(toc, new Comparator<String[]>() {
			@Override
			public int compare(String[] first, String[] second) {
				return first[0].compareTo(second[0]);
			}
		});
		return toc;
	}
	
	/**
	 * each file name consists of sheet index, sheet name, revision dates and name, with the first 5 char being sheet index
	 * mapping needed sheet index as string to String of file names containing needed sheet index
	 */
	private void buildMap() {
		List<String> fileNames = readFileNames();
		for(String fileName : fileNames) {
			String sheetIdx = fileName.substring(0, 5);
			/* build a HashMap mapping sheet index to file name
			 * if there are multiple files with the same index, the longest file name will be put (meaning latest file) into the map
			 */
			if(map.containsKey(sheetIdx)) map.put(sheetIdx, fileName);
		}
	}
	
	/**
	 * build a HashMap with needed String of need sheet indexes as key, and empty string as values
	 */
	private void collectNeededSheetNums() {
		try {
			Scanner sc = new Scanner(new FileInputStream(neededSheetNumFileName));
			while(sc.hasNext()) {
				String s = sc.nextLine();
				System.out.println(s);
				map.put(s, "x");
			}
			sc.close();
		}
		catch(IllegalStateException e) {
			System.out.println("error while displaying report" + e.getMessage());
		}
		catch(FileNotFoundException e) {
			System.out.println("error while displaying report" + e.getMessage());
		}

	}

	/**
	 * read filepath from user
	 */
	private void readAllFilesPath() {
		try {
			Scanner input = new Scanner(System.in);
			System.out.println("Type in directory of all the project files for filing: ");
			filepath = input.nextLine();	
			System.out.println("Type in filename of the txt file with all sheet numbers needed for filing: ");
			neededSheetNumFileName = input.nextLine();
		}
		
		catch (InputMismatchException e) {
			System.out.println("error while reading input" + e.getMessage());
		}
	}
	
	/**
	 * build a list of strings to store file names
	 */
	private List<String> readFileNames() {
		List<String> filenames = new ArrayList();
		File[] files = new File(filepath).listFiles();
		
		for(File file : files) {
			if(file.isFile()) filenames.add(file.getName());
		}
		return filenames;
	}

}