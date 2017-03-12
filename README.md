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
  

Contact Email: amotto@fordham.edu

# Known major issues
- GUI elements seem to break on high DPI screens (tested on Surface Pro 3)

# Known minor issues
- First file in the list is not highlighted unless you go forwards and then backwards
- The left text area has a minor graphical bug when closed and repainted.
- Error dialog for invalid directories sometimes appears more than once.

# Workarounds
- Resizing the window fixes most graphical issues so far

