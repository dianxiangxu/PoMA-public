package POMA.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import POMA.GlobalVariables;
import POMA.Utils;
import POMA.GUI.GraphVisualization.GraphVisualizer;
import POMA.GUI.editor.AbstractPolicyEditor;
import POMA.GUI.editor.DebugPanel;
import POMA.GUI.editor.MutationPanel;
import POMA.GUI.editor.PolicyEditorPanelDemo;
import POMA.GUI.editor.TestPanel;
import gov.nist.csd.pm.pip.graph.Graph;


public class POMA extends JFrame implements ItemListener, ActionListener {

	private static final long serialVersionUID = 1L;
	public int totalWidth;
	public int totalheight;
	GraphVisualizer gui;
	protected Action newAction, openAction, saveAction, saveAsAction, checkSchemaAction;
	protected Action openTestsAction, generateAllTestsAction, generateCoverageTestsAction, generateMutationTestsAction,
			generatePNOMutationTestsAction, runTestsAction, evaluateCoverageAction;
	protected Action openMutantsAction, generateMutantsAction, generateSecondOrderMutantsAction, testMutantsAction;
	protected Action localizeFaultAction, fixFaultAction;

	protected JCheckBoxMenuItem[] items;
	protected Action saveOracleValuesAction;
	boolean showVersionWarning = true;
	JSplitPane jSplitPanelMutationResult;
	protected JTabbedPane mainTabbedPane;
	protected AbstractPolicyEditor editorPanel;
	protected TestPanel testPanel;
	protected MutationPanel mutationPanel;
	protected DebugPanel debugPanel;

	public POMA() {
		try {
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			init();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	protected ImageIcon createNavigationIcon(String imageName) {
		String imgLocation = "org/umu/icons/" + imageName + ".gif";
		java.net.URL imageURL = this.getClass().getClassLoader().getResource(imgLocation);
		if (imageURL == null) {
			return null;
		} else {
			return new ImageIcon(imageURL);
		}
	}

	public JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(createPolicyMenu());
		menuBar.add(createTestMenu());
	//	menuBar.add(createDebuggingMenu());
		menuBar.add(createMutationMenu());
		menuBar.add(createHelpMenu());
		return menuBar;
	}

	
	

	private void createFileActions() {
		newAction = new NewAction("New", createNavigationIcon("new"), "New", new Integer(KeyEvent.VK_N));
		openAction = new OpenAction("Open...", createNavigationIcon("open"), "Open", new Integer(KeyEvent.VK_O));
		saveAction = new SaveAction("Save", createNavigationIcon("save"), "Save", new Integer(KeyEvent.VK_S));
		saveAsAction = new SaveAsAction("Save As...", createNavigationIcon("saveas"), "SaveAs",
				new Integer(KeyEvent.VK_A));
	}

	private void createActions() {
		createFileActions();

		checkSchemaAction = new CheckSchemaAction("Check Schema...", createNavigationIcon("CheckSchema"), "CheckSchema",
				new Integer(KeyEvent.VK_C));

		openTestsAction = new OpenTestsAction("Open Tests...", createNavigationIcon("opentests"), "OpenTests",
				new Integer(KeyEvent.VK_O));
		generateAllTestsAction = new GenerateAllTestsAction("Generate All Tests...",
				createNavigationIcon("generatealltests"), "GenerateAllTests", new Integer(KeyEvent.VK_G));

		generateCoverageTestsAction = new GenerateCoverageBasedTestsAction("Generate Coverage-Based Tests...",
				createNavigationIcon("generatecoveragetests"), "GenerateCoverageBasedTests",
				new Integer(KeyEvent.VK_G));

		generateMutationTestsAction = new GenerateMutationBasedTestsAction("Generate Mutation-Based Tests...",
				createNavigationIcon("generatemutationtests"), "GenerateMutationBasedTests",
				new Integer(KeyEvent.VK_M));

		generatePNOMutationTestsAction = new GeneratePNOMutationBasedTestsAction("Generate PNO Mutation-Based Tests...",
				createNavigationIcon("generatemutationtests"), "GenerateMutationBasedTests",
				new Integer(KeyEvent.VK_M));

		runTestsAction = new RunTestsAction("Run Tests", createNavigationIcon("runtests"), "RunTests",
				new Integer(KeyEvent.VK_R));
		evaluateCoverageAction = new EvaluateCoverageAction("Evaluate Coverage",
				createNavigationIcon("evaluateCoverage"), "EvaluateCoverage", new Integer(KeyEvent.VK_R));

		openMutantsAction = new OpenMutantsAction("Open Mutants...", createNavigationIcon("openmutants"), "OpenMutants",
				new Integer(KeyEvent.VK_P));

		generateMutantsAction = new GenerateMutantsAction("Generate/Run Mutants...",
				createNavigationIcon("generatemutants"), "GenerateMutants", new Integer(KeyEvent.VK_T));

		generateSecondOrderMutantsAction = new GenerateSecondOrderMutantsAction("Generate Second-Order Mutants...",
				createNavigationIcon("generatemutants"), "GenerateSecondOrderMutants", new Integer(KeyEvent.VK_B));

		testMutantsAction = new RunMutantsAction("Test Mutants", createNavigationIcon("runmutants"), "TestMutants",
				new Integer(KeyEvent.VK_U));

		saveOracleValuesAction = new SaveOraclesAction("Save as Oracles", createNavigationIcon(""), "SaveResults",
				new Integer(KeyEvent.VK_A));

		localizeFaultAction = new LocalizeFaultAction("Localize Fault", createNavigationIcon(""), "LocalizeFault",
				new Integer(KeyEvent.VK_L));

		fixFaultAction = new FixFaultAction("Repair", createNavigationIcon(""), "Repair", new Integer(KeyEvent.VK_F));

	}

	public void createToolBar() {
		Insets margins = new Insets(1, 1, 1, 1);

		JButton button = null;
		JToolBar toolBar = new JToolBar();
		add(toolBar, BorderLayout.PAGE_START);

		// new button
		button = new JButton(newAction);
		button.setMargin(margins);
		button.setBorderPainted(false);

		if (button.getIcon() != null) {
			button.setText(""); // an icon-only button
		}
		toolBar.add(button);

		// open button
		button = new JButton(openAction);
		button.setMargin(margins);
		button.setBorderPainted(false);
		if (button.getIcon() != null) {
			button.setText(""); // an icon-only button
		}
		toolBar.add(button);

		// save button
		button = new JButton(saveAction);
		button.setMargin(margins);
		button.setBorderPainted(false);
		if (button.getIcon() != null) {
			button.setText(""); // an icon-only button
		}
		toolBar.add(button);

	}

	protected JMenu createPolicyMenu() {
		JMenuItem menuItem = null;
		JMenu fileMenu = new JMenu("Policy");

		Action[] actions = { openAction };

		for (int i = 0; i < actions.length; i++) {
			menuItem = new JMenuItem(actions[i]);
			menuItem.setIcon(null); // arbitrarily chose not to use icon
			fileMenu.add(menuItem);
		}
		fileMenu.addSeparator();//
		fileMenu.add(createMenuItem("Exit"));
		return fileMenu;
	}

	protected JMenuItem createMenuItem(String menuName) {
		JMenuItem menuItem = new JMenuItem(menuName);
		menuItem.setActionCommand(menuName);
		menuItem.addActionListener(this);
		return menuItem;
	}

	protected JMenu createEditMenu() {
		JMenu editMenu = new JMenu("Edit");
		JMenuItem[] editItems = new JMenuItem[5];

		editItems[0] = new JMenuItem("Cut");
		editItems[1] = new JMenuItem("Copy");
		editItems[2] = new JMenuItem("Paste");
		editItems[3] = new JMenuItem("Find");
		editItems[4] = new JMenuItem("Replace");

		for (int i = 0; i < editItems.length; i++) {
			editItems[i].addItemListener(this);
			editMenu.add(editItems[i]);
		}

		return editMenu;
	}

	protected JMenu createTestMenu() {
		JMenu testMenu = new JMenu("Test");
		// Action[] actions = { openTestsAction, generateAllTestsAction, generateCoverageTestsAction,
		// 		generateMutationTestsAction, generatePNOMutationTestsAction, runTestsAction, saveOracleValuesAction,
		// 		evaluateCoverageAction };
		Action[] actions = {  generateAllTestsAction,
		 };
		for (int i = 0; i < actions.length; i++) {
			JMenuItem menuItem = new JMenuItem(actions[i]);
			menuItem.setIcon(null); // arbitrarily chose not to use icon
			testMenu.add(menuItem);
		}

		return testMenu;
	}

	

	protected JMenu createMutationMenu() {
		JMenu mutationMenu = new JMenu("Mutate");
		Action[] actions = { openMutantsAction, generateMutantsAction };
		for (int i = 0; i < actions.length; i++) {
			JMenuItem menuItem = new JMenuItem(actions[i]);
			menuItem.setIcon(null);
			mutationMenu.add(menuItem);
		}
		return mutationMenu;
	}

	protected JMenu createDebuggingMenu() {
		JMenu debuggingMenu = new JMenu("Debug");
		Action[] actions = { localizeFaultAction, fixFaultAction };
		for (int i = 0; i < actions.length; i++) {
			JMenuItem menuItem = new JMenuItem(actions[i]);
			menuItem.setIcon(null);
			debuggingMenu.add(menuItem);
		}
		return debuggingMenu;
	}

	protected JMenu createHelpMenu() {
		JMenu caMenu = new JMenu("Help");
		return caMenu;
	}

	public void itemStateChanged(ItemEvent e) {
		JCheckBoxMenuItem mi = (JCheckBoxMenuItem) (e.getSource());
		boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
		if (mi == items[0]) {
			openAction.setEnabled(selected);
		} else if (mi == items[1]) {
			saveAction.setEnabled(selected);
		}
	}

	public class NewAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public NewAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {

			editorPanel.newFile();
		}
	}

	public class OpenAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public OpenAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				editorPanel.openFile();
			} catch (Exception ex) {
				return;
			}
			if (getWorkingPolicyFile() != null) {
				setTitle("NGAC Policy Machine Analyzer - " + getWorkingPolicyFile().getName());
			} else {
				setTitle("NGAC Policy Machine Analyzer - Unnamed");
			}
			try {
				GlobalVariables.currentPath = getWorkingPolicyFile().getAbsolutePath();
			} catch (Exception ex1) {
				JOptionPane.showMessageDialog(editorPanel, "No file was selected, action aborted", "Error of Selection",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			int tabCount = mainTabbedPane.getTabCount();
			for (int i = tabCount - 1; i > 0; i--) {
				mainTabbedPane.removeTabAt(i);
			}
		}
	}

	public class SaveAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public SaveAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			editorPanel.saveFile();
		}
	}

	public class SaveAsAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public SaveAsAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	public class CheckSchemaAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public CheckSchemaAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	public class OpenTestsAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public OpenTestsAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			testPanel.openTests();
		}
	}

	public class GenerateAllTestsAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public GenerateAllTestsAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				testPanel.generateAllTestSuits();
				((PolicyEditorPanelDemo) editorPanel).updateFileTree();
				JOptionPane.showMessageDialog(editorPanel, "Test Suits Generated", "Success",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (IllegalArgumentException iae) {
				JOptionPane.showMessageDialog(editorPanel, "No file selected or policy has no associations",
						"Error of Selection", JOptionPane.WARNING_MESSAGE);
				return;
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(editorPanel, "No file selected", "Error of Selection",
						JOptionPane.WARNING_MESSAGE);
				e1.printStackTrace();
				return;
			}
		}
	}

	public class GenerateCoverageBasedTestsAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public GenerateCoverageBasedTestsAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			testPanel.generateCoverageBasedTests();
		}
	}

	public class GenerateMutationBasedTestsAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public GenerateMutationBasedTestsAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			testPanel.generateMutationBasedTests();

		}
	}

	public class GeneratePNOMutationBasedTestsAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public GeneratePNOMutationBasedTestsAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			testPanel.generatePNOMutationBasedTests();
		}
	}

	public class RunTestsAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public RunTestsAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			testPanel.runTests();
		}
	}

	public class EvaluateCoverageAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public EvaluateCoverageAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			testPanel.evaluateCoverage();
		}
	}

	public class OpenMutantsAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public OpenMutantsAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			mutationPanel.openMutants();
		}
	}

	public class GenerateMutantsAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public GenerateMutantsAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				if (GlobalVariables.currentPath.equals("")) {
					GlobalVariables.currentPath = editorPanel.getCurrentFile().getPath();
				}
			} catch (Exception e2) {
				JOptionPane.showMessageDialog(editorPanel, "No file selected", "Error of Selection",
						JOptionPane.WARNING_MESSAGE);
				if (mutationPanel.timerTask != null) {
					mutationPanel.stopProgressStatus();
				}
				return;
			}			
			jSplitPanelMutationResult = mutationPanel.generateMutants(editorPanel);
			if (mainTabbedPane.indexOfTab("Mutation Results") != -1) {
				mainTabbedPane.removeTabAt(mainTabbedPane.indexOfTab("Mutation Results"));
			}
	
			mainTabbedPane.addTab("Mutation Results", createNavigationIcon("images/policy.gif"),
					jSplitPanelMutationResult);
			mainTabbedPane.setSelectedIndex(mainTabbedPane.indexOfTab("Mutation Results"));
		}
	}

	public class GenerateSecondOrderMutantsAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public GenerateSecondOrderMutantsAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			mutationPanel.generateSecondOrderMutants();
		}
	}

	public class RunMutantsAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public RunMutantsAction(String text, ImageIcon icon, String desc, Integer mnemonic) {//
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			mutationPanel.testMutants();
		}
	}

	public class SaveOraclesAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public SaveOraclesAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			testPanel.saveActualResponsesAsOracleValues();
		}
	}

	public class LocalizeFaultAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public LocalizeFaultAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			debugPanel.localizeFault();
		}
	}

	public class FixFaultAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public FixFaultAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			debugPanel.fixFault();
		}
	}

	public void updateMainTabbedPane() {
		mainTabbedPane.validate();
		mainTabbedPane.updateUI();
	}

	public void setEditorPanel(AbstractPolicyEditor editorPanel) {
		this.editorPanel = editorPanel;
	}

	private void createMainTabbedPane() {
		editorPanel = new PolicyEditorPanelDemo();
		testPanel = new TestPanel(this);
		mutationPanel = new MutationPanel(this);
		mainTabbedPane = new JTabbedPane();
		mainTabbedPane.setBorder(BorderFactory.createEtchedBorder(0));
		mainTabbedPane.addTab("Policy", createNavigationIcon("images/policy.gif"), editorPanel);
		mainTabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {			
				tabsHandler();
			}
		});
		mainTabbedPane.setSelectedComponent(editorPanel);
	}

	private void tabsHandler() {
		int index = mainTabbedPane.getSelectedIndex();
		if (index == 0) {
			JScrollPane scrollTreePane = new JScrollPane(((PolicyEditorPanelDemo) editorPanel).getFileTree(),
					JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollTreePane.setPreferredSize(new Dimension(617, 600));
			((PolicyEditorPanelDemo) editorPanel).getPolicyjSplitPanel().setLeftComponent(scrollTreePane);
		} else if (mainTabbedPane.getTitleAt(index).equals("Mutation Results")) {
			JScrollPane scrollMutationResultsTree = new JScrollPane(
					((PolicyEditorPanelDemo) editorPanel).getFileTree(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollMutationResultsTree.setPreferredSize(new Dimension(617, 600));
			jSplitPanelMutationResult.setLeftComponent(scrollMutationResultsTree);
		}
	}
	
	public void setToPolicyPane() {
		mainTabbedPane.setSelectedComponent(editorPanel);
	}

	public void setToTestPane() {

		if (mainTabbedPane.indexOfTab("Test") == -1) {
			mainTabbedPane.addTab("Tests", createNavigationIcon("images/test.gif"), testPanel);
		}
		mainTabbedPane.setSelectedComponent(testPanel);
	}

	public void setToMutantPane() {
		if (mainTabbedPane.indexOfTab("Mutant") == -1) {
			mainTabbedPane.addTab("Mutants", createNavigationIcon("images/mutation.gif"), mutationPanel);
		}
		mainTabbedPane.setSelectedComponent(mutationPanel);
	}

	public void setToDebugPane() {
		if (mainTabbedPane.indexOfTab("Debud") == -1) {
			mainTabbedPane.addTab("Debugging", createNavigationIcon("images/mutation.gif"), debugPanel);
		}
		mainTabbedPane.setSelectedComponent(debugPanel);
	}

	private void init() throws Exception {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.totalWidth = (int) (screenSize.getWidth() * 0.8);
		this.totalheight = (int) (screenSize.getHeight() * 0.8);
		setPreferredSize(new Dimension(totalWidth, totalheight));
		createMainTabbedPane();
		createActions();
		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(mainTabbedPane, BorderLayout.CENTER);
		setJMenuBar(createMenuBar());
		createToolBar();
		handleProgramTitle();
		((PolicyEditorPanelDemo) editorPanel).getPolicyjSplitPanel().setResizeWeight(0.4);
	}
	
	private void handleProgramTitle() {
		try {
			if (!((PolicyEditorPanelDemo) editorPanel).openDefaultFile()) {
				JOptionPane.showMessageDialog(this, "Default file does not exist, please open another file/folder",
						"Error of Selection", JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception ex) {
			return;
		}
		if (getWorkingPolicyFile() != null) {
			setTitle("NGAC Policy Machine Analyzer - " + getWorkingPolicyFile().getName());
		} else {
			setTitle("NGAC Policy Machine Analyzer - Unnamed");
		}
		try {
			GlobalVariables.currentPath = getWorkingPolicyFile().getAbsolutePath();
		} catch (Exception ex1) {
			JOptionPane.showMessageDialog(editorPanel, "No file was selected, action aborted", "Error of Selection",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
	}

	protected void exit() {
		this.dispose();
	}



	public boolean hasWorkingPolicy() {
		return editorPanel.getWorkingPolicyFile() != null;
	}

	public File getWorkingPolicyFile() {
		return editorPanel.getWorkingPolicyFile();
	}

	public String getWorkingPolicyFilePath() {
		String path = null;
		if (editorPanel != null) {
			File file = editorPanel.getWorkingPolicyFile();
			if (file != null) {
				path = file.getAbsolutePath();
			}
		}
		return path;
	}


	public String getWorkingTestSuiteFileName() {
		return testPanel.getWorkingTestSuiteFileName();
	}

	public boolean hasTests() {
		return testPanel.hasTests();
	}

	public TestPanel getTestPanel() {
		return testPanel;
	}

	public boolean hasTestFailure() {
		return testPanel.hasTestFailure();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equalsIgnoreCase("Exit")) {
			windowClosing();
		}
	}

	public void windowClosing() {
		this.dispose();
	}

	public static String getResourcesPath() {
		String path = null;
		try {
			path = (new File(".")).getCanonicalPath() + File.separator + "resources";

		} catch (IOException e) {
			System.err.println("Can not locate policy repository");
			e.printStackTrace();
		}
		return path;

	}

	public static String getRootPath() {
		File rootDir = new File(".");
		String rootPath = null;
		try {
			rootPath = rootDir.getCanonicalPath();
		} catch (Exception e) {
			POMA.log(e);
		}
		return rootPath;
	}

	public static void log(Exception e) {
		e.printStackTrace();
	}

	public static void main(String[] args) {
		try {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					POMA frame = new POMA();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.pack();
					frame.setVisible(true);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}



class MiWindowAdapter extends WindowAdapter {
	private POMA adaptee;

	MiWindowAdapter(POMA adaptee) {
		this.adaptee = adaptee;
	}

	public void windowClosing(WindowEvent e) {
		adaptee.windowClosing();
	}
}
