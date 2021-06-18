/*
 * Copyright 2005, 2006 Alberto Jim?nez L?zaro
 *                      Pablo Galera Morcillo (umu-xacml-editor-admin@dif.um.es)
 *                      Dpto. de Ingenier?a de la Informaci?n y las Comunicaciones
 *                      (http://www.diic.um.es:8080/diic/index.jsp)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package POMA.GUI.editor;

import org.apache.commons.io.FilenameUtils;
import org.xml.sax.SAXException;

import com.jgraph.algebra.JGraphFibonacciHeap.Node;

import POMA.GlobalVariables;
import POMA.Utils;
import POMA.GUI.GraphVisualization.GraphVisualizer;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.GraphSerializer;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.ProhibitionsSerializer;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.stream.Collectors;

/**
 * 
 * This makes the PrincipalPolitica JFrame a JPanel so it can be embedded in
 * ArchEdit/Archipelego
 * 
 * @author Jie Ren
 */
public class PolicyEditorPanelDemo extends AbstractPolicyEditor {
	protected String policy;
	MemGraph g;

	File temporal;
	JTextArea policyText = new JTextArea();
	JApplet graphComponent;
	JSplitPane jsplitpanevertical;
	Utils utils = new Utils();
	JTree fileTree;

	JTree arbolPoliticas;

	File archivoActual;

	DefaultTreeModel miDTM = new DefaultTreeModel(new DefaultMutableTreeNode());

	JSplitPane policyjSplitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

	JScrollPane jScrollPane1;

	BorderLayout borderLayout1 = new BorderLayout();

	JMenuBar barra = new JMenuBar();

	JMenu mnuArchivo = new JMenu();

	JMenu mnuValidador = new JMenu();

	JMenu mnuAbout = new JMenu();

	JMenuItem mnuItNuevo = new JMenuItem();

	JMenuItem mnuItAbrir = new JMenuItem();

	JMenuItem mnuItGuardar = new JMenuItem();

	JMenuItem mnuItGuardarC = new JMenuItem();

	JMenuItem mnuItSalir = new JMenuItem();

	JMenuItem mnuItValidar = new JMenuItem();

	JMenuItem mnuItAbout = new JMenuItem();

	MiActionAdapter listener = new MiActionAdapter(this);

	public JTree getFileTree() {
		return this.fileTree;
	}

	public MemGraph getGraph() {
		return g;
	}

	public File getCurrentFile() {
		return temporal;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public String getPolicy() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		policy = os.toString();
		return policy;
	}

	public boolean isPolicyChanged() {
		return true;
	}
	
	public PolicyEditorPanelDemo(String f) throws SAXException, IOException {
		try {
			jbInit();
			if (new File(f).exists()) {
				arbolPoliticas.setModel(miDTM);
			} else {
				actionPerformed(new ActionEvent(mnuItNuevo, 0, "mnuFiles_New"));
			}
			archivoActual = new File(f);
		} catch (SAXException exc) {
			(new JOptionPane()).showMessageDialog(this, exc.toString());
			throw exc;
		} catch (IOException exc) {
			(new JOptionPane()).showMessageDialog(this, exc.toString());
			throw exc;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public PolicyEditorPanelDemo() {
		try {
			jbInit();
			actionPerformed(new ActionEvent(mnuItNuevo, 0, "mnuFiles_New"));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Component initialization.
	 *
	 * @throws java.lang.Exception
	 */
	private void jbInit() throws Exception {

		arbolPoliticas = new JTree(miDTM);

		setLayout(borderLayout1);
		setSize(new Dimension(800, 600));
		arbolPoliticas.setToolTipText("");
		arbolPoliticas.addMouseListener(new MiMouseAdapter(this));
		arbolPoliticas.addTreeSelectionListener(new MiTreeSelectionAdapter(this));
		jScrollPane1 = new JScrollPane(arbolPoliticas);
		policyjSplitPanel.setLeftComponent(jScrollPane1);
		policyjSplitPanel.setRightComponent(new JPanel());
		policyjSplitPanel.setResizeWeight(0.4);
		mnuArchivo.setText("File");
		mnuValidador.setText("Schema Validator");
		mnuAbout.setText("About");
		mnuItNuevo.setText("New");
		mnuItNuevo.addActionListener(new MiActionAdapter(this));
		mnuItAbrir.setText("Open...");
		mnuItAbrir.addActionListener(new MiActionAdapter(this));
		mnuItGuardar.setText("Save");
		mnuItGuardar.addActionListener(new MiActionAdapter(this));
		mnuItGuardarC.setText("Save As...");
		mnuItGuardarC.addActionListener(new MiActionAdapter(this));
		mnuItSalir.setText("Exit");
		mnuItSalir.addActionListener(new MiActionAdapter(this));
		mnuItValidar.setText("Checking...");
		mnuItValidar.addActionListener(new MiActionAdapter(this));
		mnuItAbout.setText("Credits...");
		mnuItAbout.addActionListener(new MiActionAdapter(this));
		add(policyjSplitPanel, borderLayout1.CENTER);
		mnuArchivo.add(mnuItNuevo);
		mnuArchivo.add(mnuItAbrir);
		mnuArchivo.add(mnuItGuardar);
		mnuArchivo.add(mnuItGuardarC);
		mnuArchivo.addSeparator();
		mnuArchivo.add(mnuItSalir);
		mnuValidador.add(mnuItValidar);
		mnuAbout.add(mnuItAbout);
		barra.add(mnuArchivo);
		barra.add(mnuValidador);
		barra.add(mnuAbout);
	}

	public File getWorkingPolicyFile() {
		return temporal;
	}

	public void newFile() {
		saveChanged();
		DefaultMutableTreeNode raiz = new DefaultMutableTreeNode(new String("Policy Document"));
		DefaultTreeModel auxdtm = new DefaultTreeModel(raiz);
		miDTM = auxdtm;
		arbolPoliticas.setModel(miDTM);
		archivoActual = null;
	}

	public boolean openDefaultFile() {
		temporal = new File(GlobalVariables.defaultPolicies);
		if (!temporal.exists()) {
			return false;
		}
		GlobalVariables.initialPath = temporal.getAbsolutePath();
		policyText.setEditable(false);
		JScrollPane scroll = new JScrollPane(createFileTree(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(617, 600));
		policyjSplitPanel.setLeftComponent(scroll);
		handlePolicyLoading();
		archivoActual = temporal;
		return true;
	}

	public void openFile() {
		saveChanged();
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setCurrentDirectory(getCurrentDirectory());
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			GlobalVariables.initialPath = fileChooser.getSelectedFile().getAbsolutePath();
			temporal = fileChooser.getSelectedFile();
			policyText.setEditable(true);
			JScrollPane scroll = new JScrollPane(createFileTree(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			scroll.setPreferredSize(new Dimension(617, 600));
			policyjSplitPanel.setLeftComponent(scroll);
			if (handlePolicyLoading())
				return;
			archivoActual = temporal;
		}
	}

	public JSplitPane getPolicyjSplitPanel() {
		return policyjSplitPanel;
	}

	private void emptyGraphComponent() {
		g = new MemGraph();
		GraphVisualizer gui = new GraphVisualizer(g);
		gui.init();
		graphComponent = gui.returnPane();
		jsplitpanevertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		JPanel p1 = new JPanel();

		JScrollPane scroll = new JScrollPane(policyText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(617, 600));
		p1.add(scroll);
		jsplitpanevertical.setBottomComponent(graphComponent);
		jsplitpanevertical.setTopComponent(p1);
		jsplitpanevertical.setResizeWeight(0.5);
		policyjSplitPanel.setRightComponent(jsplitpanevertical);
		policyjSplitPanel.setResizeWeight(0.4);
		scroll.setPreferredSize(new Dimension(617, 600));
		policyjSplitPanel.setRightComponent(scroll);
		policyjSplitPanel.setResizeWeight(0.4);

	}

	private void jTreeValueChanged(TreeSelectionEvent tse) {
		if (tse.getNewLeadSelectionPath() == null)
			return;
		String node = ((FileTreeNode) tse.getNewLeadSelectionPath().getLastPathComponent()).getFilePath();
		GlobalVariables.currentPath = node;
		temporal = new File(GlobalVariables.currentPath);
		String ext = FilenameUtils.getExtension(node);
		if (ext.equalsIgnoreCase("yml")) {
			emptyGraphComponent();
			policyText.setText(utils.readTextFile(node));
			policyjSplitPanel.setResizeWeight(0);
			policyText.setEditable(true);
			return;
		}
		try {
			updateGraphComonent();
			jsplitpanevertical.setBottomComponent(graphComponent);
			policyjSplitPanel.setResizeWeight(0.4);

		} catch (Exception e) {
			emptyGraphComponent();
			if (!temporal.isDirectory()) {
				policyText.setText(utils.readTextFile(node));
				policyText.setEditable(true);
				policyjSplitPanel.setResizeWeight(0);
			} else {
				policyText.setText("No JSON file found in this folder");
				policyText.setEditable(false);
				policyjSplitPanel.setResizeWeight(0.4);

			}
		}
		if (temporal.isDirectory()) {
			policyText.setEditable(false);
		}
	}

	private JTree createFileTree() {

		File initialFile = new File(GlobalVariables.initialPath);
		if (initialFile.isDirectory()) {
			fileTree = new JTree(new DefaultTreeModel(ListFiles(initialFile)));
		} else {
			DefaultMutableTreeNode fileTreeNode = new FileTreeNode(initialFile.getName(), initialFile.getPath());
			fileTree = new JTree(new DefaultTreeModel(fileTreeNode));
		}
		fileTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
			public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
				jTreeValueChanged(evt);
			}
		});
		return fileTree;
	}

	private DefaultMutableTreeNode ListFiles(File root) {
		DefaultMutableTreeNode treenode = new FileTreeNode(root.getName(), root.getPath());
		File[] files = root.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				treenode.add(ListFiles(f));
			} else {
				DefaultMutableTreeNode fileTreeNode = new FileTreeNode(f.getName(), f.getPath());
				treenode.add(fileTreeNode);
			}
		}
		return treenode;
	}

	public void updateFileTree() {
		TreePath treePath = fileTree.getSelectionPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
		File currentRootFile = new File(((FileTreeNode) node).getFilePath());
		if (!currentRootFile.isDirectory()) {
			currentRootFile = currentRootFile.getParentFile();
			node = (DefaultMutableTreeNode) node.getParent();
		}

		DefaultTreeModel model = ((DefaultTreeModel) fileTree.getModel());
		updateTreeModel(model, node);

	}

	private int getChildIndex(DefaultTreeModel model, DefaultMutableTreeNode root, String child) {
		int result = -1;
		int childCount = model.getChildCount(root);
		for (int i = 0; i < childCount; i++) {
			DefaultMutableTreeNode currentChild = (DefaultMutableTreeNode) model.getChild(root, i);
			if (child.equals(currentChild.toString())) {
				result = i;
			}
		}
		return result;
	}

	private void updateTreeModel(DefaultTreeModel model, DefaultMutableTreeNode root) {
		File currentRootFile = new File(((FileTreeNode) root).getFilePath());
		File[] files = currentRootFile.listFiles();
		for (File f : files) {
			int index = getChildIndex(model, root, f.getName());
			if (index == -1) {

				DefaultMutableTreeNode newChild = new FileTreeNode(f.getName(), f.getPath());
				model.insertNodeInto(newChild, root, root.getChildCount());
				if (f.isDirectory()) {
					updateTreeModel(model, newChild);
				}
			} else {
				DefaultMutableTreeNode newChild = (DefaultMutableTreeNode) model.getChild(root, index);
				if (f.isDirectory()) {
					updateTreeModel(model, newChild);
				}
			}
		}

	}

	private boolean handlePolicyLoading() {
		g = null;
		String ext = FilenameUtils.getExtension(temporal.getPath());
		if (ext.equalsIgnoreCase("yml")) {
			updateObligationTextComonent();
			policyText.setEditable(true);
			return true;
		}
		try {
			updateGraphComonent();
			return false;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "File cannot be opened", "Error of Selection",
					JOptionPane.WARNING_MESSAGE);
		} catch (Exception e) {
			try {
				updateProhibitionsTextComponent();
				policyText.setEditable(true);
				return true;
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "File cannot be opened", "Error of Selection",
						JOptionPane.WARNING_MESSAGE);
			}

		}
		return false;
	}

	private void updateObligationTextComonent() {
		policyText.setText(utils.readTextFile(temporal.getPath()));
		JScrollPane scroll = new JScrollPane(policyText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(617, 600));
		policyjSplitPanel.setRightComponent(scroll);
		policyjSplitPanel.setResizeWeight(0.4);

	}

	private void updateProhibitionsTextComponent() throws PMException, IOException {
		Prohibitions prohibition = null;
		prohibition = utils.readProhibitions(temporal.getPath());
		policyText.setText(ProhibitionsSerializer.toJson(prohibition));
		JScrollPane scroll = new JScrollPane(policyText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(617, 600));
		policyjSplitPanel.setRightComponent(scroll);
		policyjSplitPanel.setResizeWeight(0.4);

	}

	private void updateGraphComonent() throws IOException {
		try {
			g = utils.readAnyMemGraph(temporal.getPath());
			policyText.setText(GraphSerializer.toJson(g));
			GraphVisualizer gui = new GraphVisualizer(g);
			gui.init();
			graphComponent = gui.returnPane();
			jsplitpanevertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			JPanel p1 = new JPanel();
			JScrollPane scroll = new JScrollPane(policyText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			scroll.setPreferredSize(new Dimension(617, 600));
			p1.add(scroll);
			jsplitpanevertical.setBottomComponent(graphComponent);
			jsplitpanevertical.setTopComponent(scroll);
			jsplitpanevertical.setResizeWeight(0.5);
			policyjSplitPanel.setRightComponent(jsplitpanevertical);
			policyjSplitPanel.setResizeWeight(0.4);
			if (g.getNodes().size() == 0) {
				policyText.setText("No JSON file found in this folder");
			} else {
				if (!temporal.isDirectory())
				{
					policyText.setEditable(true);
				}
				else {
					policyText.setEditable(false);
				}
				policyText.setText(GraphSerializer.toJson(g));
			}
		} catch (PMException e) {
			policyText.setEditable(false);
		}
	}

	public void saveFile() {
		if (archivoActual == null) {
			saveAsFile();
		} else {

		}
	}

	public void saveAsFile() {
		JFileChooser cuadroGuardar = new JFileChooser();
		cuadroGuardar.setMultiSelectionEnabled(false);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mnuItNuevo) {
			newFile();
		} else if (e.getSource() == mnuItAbrir) {
			openFile();
		} else if (e.getSource() == mnuItGuardar) {
			saveFile();
		} else if (e.getSource() == mnuItGuardarC) {
			saveAsFile();
		} else if (e.getSource() == mnuItSalir) {
			saveChanged();
		} else if (e.getActionCommand().equalsIgnoreCase("copy")) {
			DefaultMutableTreeNode copia = (DefaultMutableTreeNode) arbolPoliticas.getLastSelectedPathComponent();
			if (copia != null && !copia.isRoot()) {
			}
		} else if (e.getActionCommand().equalsIgnoreCase("paste")) {
			DefaultMutableTreeNode selecto = (DefaultMutableTreeNode) arbolPoliticas.getLastSelectedPathComponent();
		}
	}


	private void saveChanged() {
		// JR if (miDTM.getChildCount(miDTM.getRoot()) > 0) {
		// JR use ModelChangeMonitor
//		if (mcm.isChanged()) {
//			int resp = JOptionPane.showConfirmDialog(this,
//					"Not Save. Do you wish to save the changes?", "Saving...",
//					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//			if (resp == JOptionPane.YES_OPTION) {
//				actionPerformed(new ActionEvent(mnuItGuardar, 0, "mnuFiles_Save"));
//			}
//		}

	}

	private DefaultMutableTreeNode copiarNodos(DefaultMutableTreeNode node) {

		Enumeration hijos = node.children();

		return null;
	}

	private void eliminarNodos(DefaultMutableTreeNode node) {
		DefaultMutableTreeNode padre = (DefaultMutableTreeNode) node.getParent();
		miDTM.removeNodeFromParent(node);
		miDTM.reload(padre);
	}

	private void crearNodos(String s, DefaultMutableTreeNode nodo) {
	}

	public void valueChanged(TreeSelectionEvent e) {
		int original = policyjSplitPanel.getDividerLocation();
		DefaultMutableTreeNode selecto = (DefaultMutableTreeNode) arbolPoliticas.getLastSelectedPathComponent();
		if (selecto != null) {
		} else {
			policyjSplitPanel.setRightComponent(new JPanel());
		}
		policyjSplitPanel.setDividerLocation(original);
	}

	public void mouseReleased(MouseEvent e) {
		DefaultMutableTreeNode nodo;
		if (SwingUtilities.isRightMouseButton(e)) {
			int xCoord = e.getX();
			int yCoord = e.getY();

			TreePath path = arbolPoliticas.getPathForLocation(xCoord, yCoord);
			if (path != null) {
				arbolPoliticas.setSelectionPath(path);
				nodo = (DefaultMutableTreeNode) path.getLastPathComponent();
				if (nodo != null) {
				}
			}
		}
	}

	public void windowClosing(WindowEvent e) {
	}

	private static class MiActionAdapter implements ActionListener {
		private PolicyEditorPanelDemo adaptee;

		MiActionAdapter(PolicyEditorPanelDemo adaptee) {
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e) {
			adaptee.actionPerformed(e);
		}
	}

	private static class MiTreeSelectionAdapter implements TreeSelectionListener {
		private PolicyEditorPanelDemo adaptee;

		MiTreeSelectionAdapter(PolicyEditorPanelDemo adaptee) {
			this.adaptee = adaptee;
		}

		public void valueChanged(TreeSelectionEvent e) {
			adaptee.valueChanged(e);
		}
	}

	private static class MiMouseAdapter extends MouseAdapter {
		private PolicyEditorPanelDemo adaptee;

		MiMouseAdapter(PolicyEditorPanelDemo adaptee) {
			this.adaptee = adaptee;
		}

		public void mouseReleased(MouseEvent e) {
			adaptee.mouseReleased(e);
		}
	}

	private static class MiWindowAdapter extends WindowAdapter {
		private PolicyEditorPanelDemo adaptee;

		MiWindowAdapter(PolicyEditorPanelDemo adaptee) {
			this.adaptee = adaptee;
		}

		public void windowClosing(WindowEvent e) {
			adaptee.windowClosing(e);
		}
	}

	private class FileTreeNode extends DefaultMutableTreeNode {
		public String filePath;

		public String getFilePath() {
			return this.filePath;
		}

		public FileTreeNode(String fileName, String filePath) {
			super(fileName);
			this.filePath = filePath;
		}
	}

	public static void main(String[] args) throws PMException, IOException {

		JFrame frame = new JFrame();
		PolicyEditorPanelDemo panel = new PolicyEditorPanelDemo();
		frame.getContentPane().add(panel);

		frame.pack();
		frame.setVisible(true);
	}

}
