import javax.swing.*;
import javax.swing.text.*;
import java.io.*;
import java.nio.file.*;
import java.awt.*;
import javax.swing.border.Border;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class SortingScript {

	private JFrame primaryWindow = new JFrame("SortingScript");
	private JPanel leftPanel = new JPanel(new BorderLayout());
	private	JSplitPane splitPane;
	//private JPanel leftPanelSub1 = new JPanel();
	private JPanel leftPanelSub2 = new JPanel();
	private JPanel rightPanel = new JPanel(new BorderLayout());
	//private JPanel rightPanelSub1 = new JPanel();
	private JPanel rightPanelSub2 = new JPanel();
	private JButton copyToClipboard = new JButton("Copy to clipboard");
	private JButton benign = new JButton("Benign");
	private JButton unknown = new JButton("Unknown");
	private JButton extremism = new JButton("Extremism");
	private JButton forward = new JButton(">");
	private JButton backward = new JButton("<");
	private JButton bigger = new JButton("+");
	private JButton smaller = new JButton("-");
	private JButton back = new JButton("Back");
	private JButton goToButton = new JButton("Go");
	private JTextField goToFile = new JTextField(12);
	private JLabel goToLabel = new JLabel("Go to file: ");
	private JTextArea textAreaRight = new JTextArea("", 36, 49);
	private JTextArea textAreaLeft = new JTextArea("", 25, 25);
	private	JFileChooser fileBrowser = new JFileChooser();
	private Highlighter.HighlightPainter highlighter = new DefaultHighlighter.DefaultHighlightPainter(Color.LIGHT_GRAY);
	private Object highlightTag;
	private boolean goToNext = true;
	private boolean directorySelected = false;
	private boolean extremistDirectorySet = false;
	private boolean unknownDirectorySet = false;
	private boolean benignDirectorySet = false;
	private boolean emptyDirectorySet = false;
	private File path;
	private	File [] files;
	private Path benignDirectory;
	private Path emptyDirectory;
	private Path unknownDirectory;
	private Path extremistDirectory;
	private String benignInput;
	private String emptyInput;
	private String unknownInput;
	private String extremistInput;
	private int counter = 0;
	private int scrollCounter = 5;



	public SortingScript() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}

		catch (Exception e) {
			JOptionPane.showMessageDialog(primaryWindow, "System look and feel could not be applied.");
		}
		
		setUpFileBrowser();
		assembleSplitPane();
		assembleLeftPanelFileBrowser();
		assembleRightPanel();
		setActions();
		primaryWindow.add(splitPane, BorderLayout.CENTER);
		primaryWindow.setResizable(true);
		primaryWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	private void init() {
		primaryWindow.pack();
		primaryWindow.setVisible(true);
	}

	private void assembleSplitPane() {
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
		splitPane.setContinuousLayout(true);
		splitPane.setDividerSize(2);
	}

	private void setUpFileBrowser() {
		javax.swing.filechooser.FileNameExtensionFilter filter = new javax.swing.filechooser.FileNameExtensionFilter("Text File", "txt");
		fileBrowser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileBrowser.setFileFilter(filter);
		fileBrowser.setAcceptAllFileFilterUsed(false);

		fileBrowser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("ApproveSelection")){
					System.out.println("File browser got an approve selection command");
					path = fileBrowser.getSelectedFile();
					files = path.listFiles();
					if (checkDirectory()){
						directorySelected = true;
						try{checkSubDirectories(path);}
						catch (IOException io){JOptionPane.showMessageDialog(primaryWindow, 
						"Folder requires subfolders but they couldn't be made. Please change folder permissions!");}
						assembleLeftPanelTextArea();
						readFile(files[counter], true);
					}
					else{
						if (directorySelected)
							directorySelected = false;
						JOptionPane.showMessageDialog(primaryWindow, "Directory does not contain text files");
					}

				}
			}
    		});
	}

	private void assembleLeftPanelFileBrowser() {
		leftPanel.removeAll();
		//setUpFileBrowser();
		leftPanel.add(fileBrowser);
		//leftPanel.setBorder(BorderFactory.createEmptyBorder(35,10,35,10));
		leftPanel.validate();
		leftPanel.repaint();
	}

	private void assembleLeftPanelTextArea() {
		leftPanel.removeAll();
		//leftPanelSub1.removeAll();
		leftPanelSub2.removeAll();
		assembleTextAreaLeft();
		JScrollPane scrollPane = new JScrollPane(textAreaLeft);
		//leftPanelSub1.add(scrollPane);
		leftPanelSub2.add(goToLabel);
		leftPanelSub2.add(goToFile);
		leftPanelSub2.add(goToButton);
		leftPanelSub2.add(back);
		leftPanel.add(scrollPane, BorderLayout.CENTER);
		leftPanel.add(leftPanelSub2, BorderLayout.SOUTH);
		leftPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		leftPanel.validate();
		leftPanel.repaint();
	}

	private void assembleTextAreaLeft(){
		textAreaLeft.setText("");
		textAreaLeft.setEditable(false);
		textAreaLeft.setLineWrap(false);
		textAreaLeft.setFont(textAreaLeft.getFont().deriveFont(14f));
		for (int i = 0; i < files.length; i++)
			textAreaLeft.append(files[i].toString() + "\n");
		textAreaLeft.setCaretPosition(0);
	}

	private void autoScroll(){
		JViewport textAreaViewport = (JViewport)textAreaLeft.getParent();
		JScrollPane textAreaScrollPane = (JScrollPane)textAreaViewport.getParent();
		if (counter - scrollCounter > 1){
			textAreaScrollPane.getVerticalScrollBar().setValue(textAreaScrollPane.getVerticalScrollBar().getValue() + 17);
			scrollCounter++;
		}
		else if (counter - scrollCounter < -5){
			textAreaScrollPane.getVerticalScrollBar().setValue(textAreaScrollPane.getVerticalScrollBar().getValue() - 17);
			scrollCounter--;
		}
	}

	private void highlightLeftAreaLines(){
		try {
			if (highlightTag != null)
				textAreaLeft.getHighlighter().removeHighlight(highlightTag);
			int start = textAreaLeft.getLineStartOffset(counter);
			int end = textAreaLeft.getLineEndOffset(counter);
			highlightTag = textAreaLeft.getHighlighter().addHighlight(start, end, highlighter);
			autoScroll();
			textAreaLeft.repaint();
		}
		catch (BadLocationException b) {System.out.println("Could not highlight");}
	}
	
	private void highlightLine(int line, Color color){
		try {
			Highlighter.HighlightPainter lineHighlighter = new DefaultHighlighter.DefaultHighlightPainter(color);
			int start = textAreaLeft.getLineStartOffset(line);
			int end = textAreaLeft.getLineEndOffset(line);
			textAreaLeft.getHighlighter().addHighlight(start, end, lineHighlighter);
			autoScroll();
			textAreaLeft.repaint();
		}
		catch (BadLocationException b) {System.out.println("Could not highlight");}
	}

	private void assembleRightPanel() {
		rightPanel.removeAll();
		//rightPanelSub1.removeAll();
		rightPanelSub2.removeAll();
		assembleTextAreaRight();
		JScrollPane scrollPane = new JScrollPane(textAreaRight);
		//rightPanelSub1.add(scrollPane);
		benign.setBackground(Color.GREEN);
		unknown.setBackground(Color.ORANGE);
		extremism.setBackground(Color.RED);
		rightPanelSub2.add(copyToClipboard);
		rightPanelSub2.add(smaller);
		rightPanelSub2.add(bigger);
		rightPanelSub2.add(backward);
		rightPanelSub2.add(forward);
		rightPanelSub2.add(benign);
		rightPanelSub2.add(unknown);
		rightPanelSub2.add(extremism);
		rightPanel.add(scrollPane, BorderLayout.CENTER);
		rightPanel.add(rightPanelSub2, BorderLayout.SOUTH);
		rightPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	}

	private void assembleTextAreaRight(){
		textAreaRight.setEditable(false);
		textAreaRight.setLineWrap(true);
		textAreaRight.setFont(textAreaRight.getFont().deriveFont(18f));
	}

	private void checkSubDirectories(File folder) throws IOException {
        	benignDirectory = Paths.get(folder.getAbsoluteFile().toString() + "/" + "benign/");
        	extremistDirectory = Paths.get(folder.getAbsoluteFile().toString() + "/" + "extremist/");
       		emptyDirectory = Paths.get(folder.getAbsoluteFile().toString() + "/" + "empty/");
       		unknownDirectory = Paths.get(folder.getAbsoluteFile().toString() + "/" + "unknown/");

		if (!benignDirectory.toFile().exists()) {
			JOptionPane.showMessageDialog(null, "No benign folder. Making one.");
			benignDirectory.toFile().mkdir();
			benignDirectorySet = true;
		}
		else
			benignDirectorySet = true;

		if (!extremistDirectory.toFile().exists()) {
			JOptionPane.showMessageDialog(null, "No extremist folder. Making one.");
			extremistDirectory.toFile().mkdir();
			extremistDirectorySet = true;
		}

		else
			extremistDirectorySet = true;

		if (!emptyDirectory.toFile().exists()) {
			JOptionPane.showMessageDialog(null, "No empty text folder. Making one.");
			emptyDirectory.toFile().mkdir();
			emptyDirectorySet = true;
		}
		
		else
			emptyDirectorySet = true;

		if (!unknownDirectory.toFile().exists()) {
			JOptionPane.showMessageDialog(null, "No unknown text folder. Making one.");
			unknownDirectory.toFile().mkdir();
			unknownDirectorySet = true;
		}

		else
			unknownDirectorySet = true;
	}

	private void moveToEmptyDirectory(){

		if (directorySelected && emptyDirectorySet){

			files[counter].renameTo(new File (emptyDirectory.toString() + "/" + files[counter].getName()));						
			files[counter] = null;
			highlightLine(counter, Color.PINK);
		}

	}
	private void setActions(){
		copyToClipboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StringSelection selection = new StringSelection(textAreaRight.getText());
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selection, selection);
			}
    	});
		
		smaller.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textAreaRight.setFont(textAreaRight.getFont().deriveFont(textAreaRight.getFont().getSize() - 1.0f));
			}
    	});

		bigger.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textAreaRight.setFont(textAreaRight.getFont().deriveFont(textAreaRight.getFont().getSize() + 1.0f));
			}
    	});

		forward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (directorySelected == true){
					counter++;
					try {
						readFile(files[counter], true);
					}
					catch (ArrayIndexOutOfBoundsException a) {
						JOptionPane.showMessageDialog(primaryWindow, "End of directory");
					}
				}
			}
    	});

		backward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (directorySelected == true){
					if (counter > 0)
						counter--;
					readFile(files[counter], false);
				}
			}
    	});




		benign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (benignDirectorySet == false){
					JOptionPane.showMessageDialog(primaryWindow, "Select a directory to sort through first");
				}
				
				if (directorySelected && benignDirectorySet){
					files[counter].renameTo(new File (benignDirectory.toString() + "/" + files[counter].getName()));						
					files[counter] = null;
					highlightLine(counter, Color.GREEN);

					counter++;
					try {
						readFile(files[counter], true);
					}
					catch (ArrayIndexOutOfBoundsException a) {
						JOptionPane.showMessageDialog(primaryWindow, "End of directory");
					}
				}

			}
    	});

		unknown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (unknownDirectorySet == false){
					JOptionPane.showMessageDialog(primaryWindow, "Select a directory to sort through first");
				}
				
				if (directorySelected && unknownDirectorySet){

					files[counter].renameTo(new File (unknownDirectory.toString() + "/" + files[counter].getName()));						
					files[counter] = null;
					highlightLine(counter, Color.ORANGE);


					counter++;
					try {
						readFile(files[counter], true);
					}
					catch (ArrayIndexOutOfBoundsException a) {
						JOptionPane.showMessageDialog(primaryWindow, "End of directory");
					}
				}

			}
    	});


		extremism.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (extremistDirectorySet == false){
					JOptionPane.showMessageDialog(primaryWindow, "Select a directory to sort through first");
				}
				
				if (directorySelected && extremistDirectorySet){

					files[counter].renameTo(new File (extremistDirectory.toString() + "/" + files[counter].getName()));						
					files[counter] = null;
					highlightLine(counter, Color.RED);

					counter++;
					try {
						readFile(files[counter], true);
					}
					catch (ArrayIndexOutOfBoundsException a) {
						JOptionPane.showMessageDialog(primaryWindow, "End of directory");
					}

				}
			}
    	});

		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				assembleLeftPanelFileBrowser();
				splitPane.resetToPreferredSizes();
				counter = 0;
				textAreaRight.setText("");
				benignDirectorySet = false;
				emptyDirectorySet = false;
				unknownDirectorySet = false;
				extremistDirectorySet = false;
			}
    	});

		Action jumpingToFile = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				jumpToFile(goToFile.getText());
			}
		};

		goToButton.addActionListener(jumpingToFile);

		goToFile.addActionListener(jumpingToFile);
	}

	private boolean checkDirectory(){

		for (int i = 0; i < files.length; i++){
			if (files[i].getPath().indexOf(".txt") > 0)
				return true;
		}

		return false;

	}

	private boolean checkIfFileIsEmpty(File textFile){
		boolean wasEmpty = false;		
		
		try {
			FileReader fr = new FileReader(textFile);
			BufferedReader br = new BufferedReader(fr);
			if (br.readLine() == null){
				br.close();
				fr.close();
				moveToEmptyDirectory();
				wasEmpty = true;
			}
			br.close();
			fr.close();
		}

		catch (Exception e){JOptionPane.showMessageDialog(primaryWindow, e.getMessage());}

		return wasEmpty;
	}

	private void readFile(File textFile, boolean forward){
		int fileTypeIsText = -1;
		boolean fileWasEmpty = false;

		if (textFile != null){
			try {fileTypeIsText = textFile.getCanonicalPath().indexOf(".txt");}

			catch (Exception e) {JOptionPane.showMessageDialog(primaryWindow, "Selected item not a text file");}

			try {
				fileWasEmpty = checkIfFileIsEmpty(textFile);
			}

			catch (Exception e){JOptionPane.showMessageDialog(primaryWindow, e.getMessage());}

			if (textFile.isFile() && fileTypeIsText > 0 && fileWasEmpty == false){
				try {
					writeToTextAreaRight(files[counter]);
				}
				catch (NullPointerException n){
					System.out.println("NullPointerException");
				}

			}
			
			else
				readForwardOrBackward(forward);
		}

		else {
			readForwardOrBackward(forward);
		}
	}

	private void readForwardOrBackward(boolean forward){

			try {
				if (forward){
					counter++;
					readFile(files[counter], true);
				}
				else{
					if (counter > 0){
						counter--;
						readFile(files[counter], false);
					}
					else{
						counter++;
						readFile(files[counter], true);
					}
				}
			}
			catch (ArrayIndexOutOfBoundsException a) {
				JOptionPane.showMessageDialog(primaryWindow, "End of directory");
			
			}
	}

	private void jumpToFile(String fileChoice){
		int i = 0;
		boolean fileFound = false;
		while (fileFound == false && i < files.length){
			if (files[i].getPath().indexOf(fileChoice) > 0)
				fileFound = true;
			else
				i++;
		}
		if (fileFound){		
			counter = i;
			if (counter > 1)
				scrollCounter = counter - 2;		
			readFile(files[counter], true);
		}
		else
			JOptionPane.showMessageDialog(primaryWindow, "File not found");
	}

	private void writeToTextAreaRight(File textFile){
		try {
			FileReader fr = new FileReader(textFile);
			BufferedReader br = new BufferedReader(fr);
			textAreaRight.setText("");
			textAreaRight.read(br, null);
			highlightLeftAreaLines();
			br.close();
			fr.close();
		}
		
		catch (Exception e2) {
			JOptionPane.showMessageDialog(primaryWindow, e2.getMessage());
		}
	}

	public static void main (String [] args){
		SortingScript s = new SortingScript();
		s.init();
	}
}
