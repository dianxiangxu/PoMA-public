package POMA.GUI.GraphVisualization;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import java.awt.Button;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JApplet;
import javax.swing.JFrame;

import org.jgrapht.DirectedGraph;
import org.jgrapht.ext.JGraphXAdapter;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxStylesheet;

import POMA.Utils;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.relationships.Relationship;

import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import java.awt.GridLayout;
import javax.swing.JPanel;

import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class GraphVisualizer extends JApplet {
	private static final long serialVersionUID = 1L;
	MemGraph ngacGraph;
	private static final Dimension DEFAULT_SIZE = new Dimension(1800, 1000);
	DirectedGraph<String, Relationship> graphToPlot;
	JGraphXAdapter<String, Relationship> jgxAdapter;
	private JTextField textField;

	public static void main(String[] args) throws PMException, IOException {
		String simpleGraphPath = "Graphs/simpleGraph.json";
		Utils utils = new Utils();

		MemGraph graph = utils.readAnyMemGraph("Graphs/NGACExample1.json");
		GraphVisualizer gui = new GraphVisualizer(graph);
		gui.init();
		gui.buildJFrame();
	}



	public GraphVisualizer(MemGraph ngacGraph) {
		this.ngacGraph = ngacGraph;
		graphToPlot = ngacGraph.graph;
	}

	private void buildJFrame() {
		JFrame frame = new JFrame();
		frame.getContentPane().add(this);
		frame.setTitle("NGAC Graph Visualization");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

	}
	public void buildJFrame(JFrame frame) {
		frame.getContentPane().add(this);
		frame.setTitle("NGAC Graph Visualization");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
        frame.setLocationRelativeTo(null);


	}
	
	public JApplet returnPane() {
		return this;
	}
	private List<mxICell> getCellsWithTypeInGraph(String type) {
		List<mxICell> List = new ArrayList<mxICell>();
		for (Map.Entry<String, mxICell> entry : jgxAdapter.getVertexToCellMap().entrySet()) {
 
			if (!entry.getKey().toString().equals("")) {
				try {
					if (ngacGraph.getNode(entry.getKey().toString()).getType().toString().equals(type)) {
						List.add(entry.getValue());
					}
				} catch (PMException e) {
					//e.printStackTrace();
					//System.out.println("Error retrieving nodes from the graph");
				}
			}
		}
		return List;
	}

	private void setNodeByType(List<mxICell> List, String type) {

		if (type.equals("U")) {
			jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR, "yellow", List.toArray());
			jgxAdapter.setCellStyles(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE, List.toArray());
		}
		if (type.equals("UA")) {
			jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR, "yellow", List.toArray());
			jgxAdapter.setCellStyles(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE, List.toArray());
		}
		if (type.equals("O")) {
			jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR, "red", List.toArray());
			jgxAdapter.setCellStyles(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE, List.toArray());
		}
		if (type.equals("OA")) {
			jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR, "red", List.toArray());
			jgxAdapter.setCellStyles(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE, List.toArray());
		}
		if (type.equals("PC")) {
			jgxAdapter.setCellStyles(mxConstants.STYLE_FILLCOLOR, "green", List.toArray());
			jgxAdapter.setCellStyles(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE, List.toArray());
		}

		jgxAdapter.setCellStyles(mxConstants.STYLE_GRADIENTCOLOR, "AFAFFF", List.toArray());
		jgxAdapter.setCellStyles(mxConstants.STYLE_OPACITY, "50", List.toArray());
		jgxAdapter.setCellStyles(mxConstants.STYLE_FONTCOLOR, "black", List.toArray());
	}

	private void setNodesStyleInGraph() {
		List<String> types = new ArrayList<String>();
		types.add("U");
		types.add("UA");
		types.add("O");
		types.add("OA");
		types.add("PC");
		for (String type : types) {
			List<mxICell> List = getCellsWithTypeInGraph(type);
			setNodeByType(List, type);
		}
	}

	private void setEdgesStyleInGraph() {
		for (Map.Entry<Relationship, mxICell> entry : jgxAdapter.getEdgeToCellMap().entrySet()) {
			if (!entry.getKey().toString().equals("")) {
				List<mxICell> itemList = new ArrayList<mxICell>();
				itemList.add(entry.getValue());
				jgxAdapter.setCellStyles(mxConstants.STYLE_FONTCOLOR, "red", itemList.toArray());
				jgxAdapter.setCellStyles(mxConstants.STYLE_DASHED, "true", itemList.toArray());
			}
		}
	}
	private void setCommonStyle() {
		mxStylesheet stylesheet = jgxAdapter.getStylesheet();
		Map<String, Object> edgeStyle = stylesheet.getDefaultEdgeStyle();
		edgeStyle.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ELBOW);
		edgeStyle.put(mxConstants.STYLE_STROKEWIDTH, 2.0);
		stylesheet.setDefaultEdgeStyle(edgeStyle);
		Map<String, Object> startStyle = new HashMap<>();
		startStyle.put(mxConstants.STYLE_STROKEWIDTH, 3.0);
		startStyle.put(mxConstants.STYLE_STROKECOLOR, "#0000F0");
		stylesheet.putCellStyle("StartStyle", startStyle);
	}
	private void setLayout() {
		mxHierarchicalLayout layout = new mxHierarchicalLayout(jgxAdapter);
		layout.setInterHierarchySpacing(100);
		layout.setIntraCellSpacing(100);
		layout.setInterRankCellSpacing(100);
		layout.setParallelEdgeSpacing(25);
		layout.execute(jgxAdapter.getDefaultParent());
		new mxParallelEdgeLayout(jgxAdapter, 20).execute(jgxAdapter.getDefaultParent());
	}
	
	private void setComponent() {
		
		
		mxGraphComponent graphComponent = new mxGraphComponent(jgxAdapter);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		JPanel controls = new JPanel();
		panel.add(controls, BorderLayout.SOUTH);
		controls.setLayout(new GridLayout(0,2));
		
		JButton zoomOut = new JButton("Zoom Out");
		zoomOut.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				graphComponent.zoomOut();
			}
		});
		controls.add(zoomOut);
		
		JButton zoomIn = new JButton("Zoom In");
		zoomIn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				graphComponent.zoomIn();
			}
		});
		controls.add(zoomIn);
		panel.add(graphComponent);
		graphComponent.setConnectable(false);
		graphComponent.getGraph().setAllowDanglingEdges(false);
		graphComponent.setSize(DEFAULT_SIZE);

	}
	@Override
	public void init() {
		jgxAdapter = new JGraphXAdapter<>(this.graphToPlot);
		setPreferredSize(DEFAULT_SIZE);
		setNodesStyleInGraph();
		setEdgesStyleInGraph();
		setCommonStyle();		
		resize(DEFAULT_SIZE);
		setComponent();
			//hello world
		jgxAdapter.getModel().setGeometry(jgxAdapter.getDefaultParent(),
				new mxGeometry((DEFAULT_SIZE.width) / 8, (30), DEFAULT_SIZE.width / 2.0, DEFAULT_SIZE.height / 2.0));
		setLayout();	
	}

}
