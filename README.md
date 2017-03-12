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

# Known major issues
- GUI elements break when display is set to resolution below the default. [NO WORKAROUND AVAILABLE]

# Known minor issues
- First file in the list is not highlighted unless you go forwards and then backwards
- Left text area extends a little too far to the right on high dpi screens (tested on Surface Pro 3)
- Error dialog for invalid directories sometimes appears more than once.

# Workarounds
- Resizing the window fixes most graphical issues so far

