# SortingScript
Java program that can load directories with text files, lets you step through them one by one, and copy them to either a benign or extremist folder.

Also, taking suggestions for a better name.

For Windows users! I have noticed that Arabic text comes up as gibberish within Java applications. To fix this, do the following steps:

  - Open the Control Panel and search for environment variables at the top. 
  - Click 'Edit the system environment variables" from the search results
  - Click "Environment variables" at the bottom of the new window
  - Click "new" under the system variables list
  - Set the variable name as 'JAVA_TOOL_OPTIONS' (minus the quotes)
  - Set the variable value as '-Dfile.encoding=UTF8'  (minus the quotes)
  - Click OK on every window
  
To create an executable jar file:
  - Make sure to have the Java JDK installed: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
  - Open a terminal window and go into the directory with the SortingScript.java file
  - Type into the terminal 'javac SortingScript.java' (minus the quotes)
  - Type into the terminal 'jar cvfe SortingScript.jar SortingScript \*.class' (minus the quotes)
  
For Mac users, the jar file will not open unless you right click it and click open (it's a MacOS security thing).

Email me with any questions!
  

Contact Email: amotto@fordham.edu

# How to use

- When the application opens, select the folder containing your text files on the left using the file browser
- If a valid folder is selected, a list of files will appear on the left, and the contents of the first file will appear on the right
- The arrow buttons can be used to step back and forth through the files. The current file is highlighted in red.
- To set the benign and extremist directory paths, click on their respective buttons first and follow the prompts. If you already have a file open, the file will be moved immediately after you set the directory. If you don't want to move files just yet, set these paths before you open a file.
- Clicking the benign or extremist buttons once the directories are set will copy the file to whichever you clicked, and then move to the next file. Note that files are copied, not moved, and that if the same file is copied twice, the existing version will be replaced.
- Clicking the back button brings you back to the file browser and clears the text area on the right
