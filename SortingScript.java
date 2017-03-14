import javax.swing.*;
import javax.swing.text.*;
import java.io.*;
import java.nio.file.*;
import java.awt.*;
import javax.swing.border.Border;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SortingScript {

	private JFrame primaryWindow = new JFrame("SortingScript");
	private JPanel leftPanel = new JPanel(new BorderLayout());
	//private JPanel leftPanelSub1 = new JPanel();
	private JPanel leftPanelSub2 = new JPanel();
	private JPanel rightPanel = new JPanel(new BorderLayout());
	//private JPanel rightPanelSub1 = new JPanel();
	private JPanel rightPanelSub2 = new JPanel();
	private JButton benign = new JButton("Benign");
	private JButton empty = new JButton("Empty");
	private JButton extremism = new JButton("Extremism");
	private JButton forward = new JButton(">");
	private JButton backward = new JButton("<");
	private JButton back = new JButton("Back");
	private JTextArea textAreaRight = new JTextArea("", 49, 78);
	private JTextArea textAreaLeft = new JTextArea("", 49, 45);
	private	JFileChooser fileBrowser = new JFileChooser();
	private Highlighter.HighlightPainter highlighter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
	private boolean goToNext = true;
	private boolean directorySelected = false;
	private boolean extremistDirectorySet = false;
	private boolean benignDirectorySet = false;
	private boolean emptyDirectorySet = false;
	private File path;
	private	File [] files;
	private Path benignDirectory;
	private Path emptyDirectory;
	private Path extremistDirectory;
	private String benignInput;
	private String emptyInput;
	private String extremistInput;
	private int counter = 0;


	public SortingScript() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}

		catch (Exception e) {
			JOptionPane.showMessageDialog(primaryWindow, "System look and feel could not be applied.");
		}

		assembleLeftPanelFileBrowser();
		assembleRightPanel();
		setButtons();
		primaryWindow.add(leftPanel, BorderLayout.WEST);
		primaryWindow.add(rightPanel, BorderLayout.CENTER);
		primaryWindow.setResizable(true);
		primaryWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	private void init() {
		primaryWindow.pack();
		primaryWindow.setVisible(true);
	}

	private void setUpFileBrowser() {
		javax.swing.filechooser.FileNameExtensionFilter filter = new javax.swing.filechooser.FileNameExtensionFilter("Text File", "txt");
		fileBrowser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileBrowser.setFileFilter(filter);
		fileBrowser.setAcceptAllFileFilterUsed(false);

		fileBrowser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("ApproveSelection")){
					path = fileBrowser.getSelectedFile();
					files = path.listFiles();
					directorySelected = true;
					assembleLeftPanelTextArea();
					readFile(files[counter]);
				}
			}
    		});
	}

	private void assembleLeftPanelFileBrowser() {
		leftPanel.removeAll();
		setUpFileBrowser();
		leftPanel.add(fileBrowser);
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
		leftPanelSub2.add(back);
		leftPanel.add(scrollPane, BorderLayout.CENTER);
		leftPanel.add(leftPanelSub2, BorderLayout.SOUTH);
		leftPanel.validate();
		leftPanel.repaint();
	}

	private void assembleTextAreaLeft(){
		textAreaLeft.setText("");
		textAreaLeft.setEditable(false);
		textAreaLeft.setLineWrap(false);
		for (int i = 0; i < files.length; i++)
			textAreaLeft.append(files[i].toString() + "\n");
		textAreaLeft.setCaretPosition(0);
	}

	private void highlightLeftAreaLines(){
		try {
			textAreaLeft.getHighlighter().removeAllHighlights();
			int start = textAreaLeft.getLineStartOffset(counter);
			int end = textAreaLeft.getLineEndOffset(counter);
			textAreaLeft.getHighlighter().addHighlight(start, end, highlighter);
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
		rightPanelSub2.add(backward);
		rightPanelSub2.add(forward);
		rightPanelSub2.add(benign);
		rightPanelSub2.add(empty);
		rightPanelSub2.add(extremism);
		rightPanel.add(scrollPane, BorderLayout.CENTER);
		rightPanel.add(rightPanelSub2, BorderLayout.SOUTH);
	}

	private void assembleTextAreaRight(){
		textAreaRight.setEditable(false);
		textAreaRight.setLineWrap(true);
	}

	private void setButtons(){
		forward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (directorySelected == true){
					counter++;
					try {
						readFile(files[counter]);
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
					readFile(files[counter]);
				}
			}
    		});


		benign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (benignDirectorySet == false){
					benignInput = JOptionPane.showInputDialog("Enter a path to benign folder");
					if (benignInput != null)
						benignDirectorySet = true;
				}
				
				if (directorySelected && benignDirectorySet){
					try {
						Path target = Paths.get(files[counter].getCanonicalPath());
						benignDirectory = Paths.get(benignInput + target.getFileName().toString());
						Files.copy(target, benignDirectory, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

					}
					catch (IOException ioe) {
						JOptionPane.showMessageDialog(primaryWindow, "Benign directory does not exist. Click benign to reset.");
						benignDirectorySet = false;
					}
					counter++;
					try {
						readFile(files[counter]);
					}
					catch (ArrayIndexOutOfBoundsException a) {
						JOptionPane.showMessageDialog(primaryWindow, "End of directory");
					}
				}

			}
    		});

		empty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (emptyDirectorySet == false){
					emptyInput = JOptionPane.showInputDialog("Enter a path to empty folder");
					if (emptyInput != null)
						emptyDirectorySet = true;
				}
				
				if (directorySelected && emptyDirectorySet){
					try {
						Path target = Paths.get(files[counter].getCanonicalPath());
						emptyDirectory = Paths.get(emptyInput + target.getFileName().toString());
						Files.copy(target, emptyDirectory, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

					}
					catch (IOException ioe) {
						JOptionPane.showMessageDialog(primaryWindow, "Empty directory does not exist. Click empty to reset.");
						emptyDirectorySet = false;
					}
					counter++;
					try {
						readFile(files[counter]);
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
					extremistInput = JOptionPane.showInputDialog("Enter a path to extremist folder");
					if (extremistInput != null)
						extremistDirectorySet = true;
				}
				
				if (directorySelected && extremistDirectorySet){
					try {
						Path target = Paths.get(files[counter].getCanonicalPath());
						extremistDirectory = Paths.get(extremistInput + target.getFileName().toString());
						Files.copy(target, extremistDirectory, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

					}
					catch (IOException ioe) {
						JOptionPane.showMessageDialog(primaryWindow, "Extremist directory does not exist. Click extremist to reset.");
						extremistDirectorySet = false;
					}
					counter++;
					try {
						readFile(files[counter]);
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
				counter = 0;
				textAreaRight.setText("");
			}
    		});
	}

	private void readFile(File textFile){
		int fileTypeIsText = -1;

		try {fileTypeIsText = textFile.getCanonicalPath().indexOf(".txt");}
		catch (IOException io) {JOptionPane.showMessageDialog(primaryWindow, "Selected item not a text file");}

		if (textFile.isFile() && fileTypeIsText > 0){
			try {
				writeToTextAreaRight(files[counter]);
			}
			catch (NullPointerException n){
				System.out.println("NullPointerException");
			}

		}
			
		else {
			//JOptionPane.showMessageDialog(primaryWindow, "Current item not a text file.");
			try {
				counter++;
				readFile(files[counter]);
			}
			catch (ArrayIndexOutOfBoundsException a) {
				JOptionPane.showMessageDialog(primaryWindow, "End of directory");
				
			};

		}
	}

	private void writeToTextAreaRight(File textFile){
		try {
			FileReader fr = new FileReader(textFile);
			BufferedReader br = new BufferedReader(fr);
			textAreaRight.setText("");
			textAreaRight.read(br, null);
			highlightLeftAreaLines();
			br.close();
		}
		
		catch (Exception e2) {
			JOptionPane.showMessageDialog(primaryWindow, "File could not be opened");
		}
	}

	public static void main (String [] args){
		SortingScript s = new SortingScript();
		s.init();
	}
}
