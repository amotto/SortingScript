import java.io.*;
import java.nio.file.*;
import java.lang.NullPointerException;
import java.util.Scanner;
import java.awt.Desktop;


public class StringSearch{
	
	private String pattern;
	private File path;
	private File [] files;
	private BufferedReader bf;
	private boolean openFiles = false;
	private boolean printFiles = false;
	private boolean continueFromHere = false;

	
	
	public static void main (String [] args){
		if (args.length < 2 && args[0].equals("-h"))
			printHelp();
		
		else if (args.length < 3 && args[1].equals("-s")){
			for (int i = 3; i < 5; i++){
				StringSearch strs = new StringSearch(i, args);
				strs.stepThroughFiles();
			}
		}
		
		else{
			for (int i = 1; i < Integer.parseInt(args[1]) + 1; i++){
				StringSearch strs = new StringSearch(args, i);
				strs.searchFiles();
			}
		}
	}
	
	public static void printHelp(){
		System.out.println("\nStringSearch for WISDM Lab");
		System.out.println("============================\n");
		System.out.println("Use -h as the first command to");
		System.out.println("open up this help prompt.");
		System.out.println("\nYour first command should be");
		System.out.println("a file path to the directory containing");
		System.out.println("the text files you want to search.");
		System.out.println("\nYour second command should be how");
		System.out.println("many folders you want to go through");
		System.out.println("(i.e., put 3 for the first 3, 1 for");
		System.out.println("just the first one, etc.");
		System.out.println("Your third command should simply be");
		System.out.println("your search term.");
		System.out.println("\nHere is a list of optional commands to");
		System.out.println("put after the first three:");
		System.out.println("-o ---- Open all search result finds");
		System.out.println("-p ---- Print lines with search term");
		System.out.println();

	}
	
	public StringSearch (int i, String args[]){
		path = new File(args[0] + i); 
		files = path.listFiles();
	}
	
	
	public StringSearch (String args[], int i){
		
		for (int j = 3; j < args.length; j++){
			if (args[j] != null){
				if (args[j].equals("-o"))
					openFiles = true;
				else if (args[j].equals("-p"))
					printFiles = true;
			}		
		}
		path = new File(args[0] + i); 
		files = path.listFiles();
		pattern = args[2];
	}
	
	public void searchFiles(){
		for (int i = 0; i < files.length; i++){
			if (files[i].isFile()){
				try {
					findLines(files[i]);
				}
				catch (NullPointerException n){
					System.out.println("NullPointerException");
				}
			}				
		}
	}

	public void stepThroughFiles(){
		int i;
		openFiles = true;
		
		// for (i = 0; i < files.length && continueFromHere == false; i++)
			// if ((files[i].toString().indexOf("128n7")) >= 0) {continueFromHere = true;}
		
		//i--;
		
		// if (continueFromHere){
			for (i = 0; i < files.length; i++){
				if (files[i].isFile()){
					try {
						moveFile(files[i]);
					}
					catch (IOException n){
						System.out.println("NullPointerException");
					}
				}				
			}
		// }
	}
	
	public void moveFile(File file) throws IOException{
		
		Path target = Paths.get(file.getCanonicalPath());
		Path terrorism = Paths.get("C:\\Users\\liljo\\Desktop\\107\\Extremism\\" + target.getFileName().toString());
		Path benign = Paths.get("C:\\Users\\liljo\\Desktop\\107\\Benign\\" + target.getFileName().toString());	
		Scanner input = new Scanner(System.in);
		
		
		System.out.println(file);
		if (Desktop.isDesktopSupported() && openFiles)
			Desktop.getDesktop().edit(file);
		
		System.out.print("Move file: ");
		int choice = input.nextInt();

		
		if (choice == 1)
			// System.out.println("Copying to " + terrorism.toString());
			Files.copy(target, terrorism, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		else if (choice == 0)
			 //System.out.println("Copying " + target + " to " + benign.toString());
			Files.copy(target, benign, java.nio.file.StandardCopyOption.REPLACE_EXISTING);	
		else
			System.out.println("Skipping");
		
	}
	
	public void findLines(File file){
		String found;	
		try{
			bf = new BufferedReader(new FileReader(file));
		}
		catch (FileNotFoundException e){
			System.out.println("File not found.");
		}

		try {
			while ((found = bf.readLine()) != null){
				if(found.toLowerCase().indexOf(pattern.toLowerCase()) >= 0){
					System.out.println(file);
					if (printFiles){
						System.out.println("\n===========================RESULT START===========================================");
						System.out.println(found);
						System.out.println("=============================RESULT END=================================================\n");
				}
					if (Desktop.isDesktopSupported() && openFiles)
						Desktop.getDesktop().edit(file);
				}
			}
		} catch (IOException i){
			System.out.println("IOException");
		}
	}
}
