package POMA;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.OA;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.O;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.PC;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.UA;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.U;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JApplet;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import POMA.Exceptions.NoTypeProvidedException;
import POMA.GUI.GraphVisualization.GraphVisualizer;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.GraphSerializer;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.dag.searcher.DepthFirstSearcher;
import gov.nist.csd.pm.pip.graph.dag.searcher.Direction;
import gov.nist.csd.pm.pip.graph.dag.visitor.Visitor;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.prohibitions.MemProhibitions;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.ProhibitionsSerializer;

public class Utils {

	public static Boolean isContainedBy(Graph graph, String descendant, String container) throws PMException {
		Node containerNode = graph.getNode(container);
		Node descendantNode = graph.getNode(descendant);
		DepthFirstSearcher dfs = new DepthFirstSearcher(graph);
		Set<String> nodes = new HashSet<>();
		Visitor visitor = node -> {
			if (node.getName().equals(containerNode.getName())) {
				nodes.add(node.getName());
			}
		};
		dfs.traverse(descendantNode, (c, p) -> {
		}, visitor, Direction.PARENTS);

		return nodes.contains(containerNode.getName());
	}

	public static String getPCOf(Graph graph, String node) throws PMException {
		for (String pc : graph.getPolicyClasses()) {
			if (isContainedBy(graph, node, pc)) {
				return pc;
			}
		}
		return "";
	}

	public static OperationSet getAllAccessRights(Graph graph) throws PMException, NoTypeProvidedException {
		OperationSet ARSet = new OperationSet();
		for (String SourceNode : getNodesByTypes(graph, "UA")) {
			if (graph.getSourceAssociations(SourceNode) == null) {
				continue;
			}
			Map<String, OperationSet> sources = graph.getSourceAssociations(SourceNode);
			List<String> targetNodes = new ArrayList<String>(sources.keySet());
			for (String targetNode : targetNodes) {
				Set<String> operateSet = sources.get(targetNode);
				OperationSet accessRights = new OperationSet(operateSet);
				ARSet.addAll(accessRights);
			}
		}
		return ARSet;
	}

	public static List<String> getUAsInGraph(Graph graph) throws PMException {
		List<String> UAs = new ArrayList<String>();

		Node[] nodes = graph.getNodes().toArray(new Node[graph.getNodes().size()]);
		for (Node node : nodes) {
			if (node.getType() == UA) {
				UAs.add(node.getName());
			}
		}
		return UAs;
	}

	public static List<String> getOAsInGraph(Graph graph) throws PMException {
		List<String> OAs = new ArrayList<String>();

		Node[] nodes = graph.getNodes().toArray(new Node[graph.getNodes().size()]);
		for (Node node : nodes) {
			if (node.getType() == OA) {
				OAs.add(node.getName());
			}
		}
		return OAs;
	}

	public static List<String> getOsInGraph(Graph graph) throws PMException {
		List<String> Os = new ArrayList<String>();

		Node[] nodes = graph.getNodes().toArray(new Node[graph.getNodes().size()]);
		for (Node node : nodes) {
			if (node.getType() == O) {
				Os.add(node.getName());
			}
		}
		return Os;
	}

	public static List<String> getUsInGraph(Graph graph) throws PMException {
		List<String> Us = new ArrayList<String>();

		Node[] nodes = graph.getNodes().toArray(new Node[graph.getNodes().size()]);
		for (Node node : nodes) {
			if (node.getType() == U) {
				Us.add(node.getName());
			}
		}
		return Us;
	}

	public static List<String> getPCsInGraph(Graph graph) throws PMException {
		List<String> PCs = new ArrayList<String>();

		Node[] nodes = graph.getNodes().toArray(new Node[graph.getNodes().size()]);
		for (Node node : nodes) {
			if (node.getType() == PC) {
				PCs.add(node.getName());
			}
		}
		return PCs;
	}

	
	private static void saveDataToFile(String data, String path) throws PMException, IOException {
		File file = new File(path);
		FileWriter myWriter = new FileWriter(file);
		myWriter.write(data);
		myWriter.close();

	}

	public String readTextFile(String path) {
		StringBuilder sb = new StringBuilder();
		try {
		      File myObj = new File(path);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		    	  sb.append(myReader.nextLine());
		    	  sb.append(System.lineSeparator());
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		    //  e.printStackTrace();
		    }
		return sb.toString();
	}
	
public static Graph combineGraphs() throws PMException, IOException {
    File file1 = new File("Policies/GPMS/AcademicUnitsPolicyClass.json");
    File file2 = new File("Policies/GPMS/AdministrationUnitsPolicyClass.json");
    File file3 = new File("Policies/GPMS/EditingPolicyClass.json");
    File file4 = new File("Policies/GPMS/EligibilityPolicyClass.json");
    String policy1 = new String(Files.readAllBytes(Paths.get(file1.getAbsolutePath())));
    String policy2 = new String(Files.readAllBytes(Paths.get(file2.getAbsolutePath())));
    String policy3 = new String(Files.readAllBytes(Paths.get(file3.getAbsolutePath())));
    String policy4 = new String(Files.readAllBytes(Paths.get(file4.getAbsolutePath())));
    Graph ngacGraph = new MemGraph();
    GraphSerializer.fromJson(ngacGraph, policy1);
    GraphSerializer.fromJson(ngacGraph, policy2);
    GraphSerializer.fromJson(ngacGraph, policy3);
    GraphSerializer.fromJson(ngacGraph, policy4);
    
//    File file1 = new File("Policies/LawUseCase/CasePolicy.json");
//    File file2 = new File("Policies/LawUseCase/LawFirmPolicy.json");
//    File file3 = new File("Policies/LawUseCase/ValueTypePolicy.json");
//    String policy1 = new String(Files.readAllBytes(Paths.get(file1.getAbsolutePath())));
//    String policy2 = new String(Files.readAllBytes(Paths.get(file2.getAbsolutePath())));
//    String policy3 = new String(Files.readAllBytes(Paths.get(file3.getAbsolutePath())));
//    Graph ngacGraph = new MemGraph();
//    GraphSerializer.fromJson(ngacGraph, policy1);
//    GraphSerializer.fromJson(ngacGraph, policy2);
//    GraphSerializer.fromJson(ngacGraph, policy3);
    
    System.out.println(GraphSerializer.toJson(ngacGraph));
    return ngacGraph;
}
	
	public static Graph readAnyGraph(String path) throws PMException, IOException {
		File graphFile = new File(path);

		String graphJSON = new String(Files.readAllBytes(Paths.get(graphFile.getAbsolutePath())));

		Graph ngacGraph = new MemGraph();

		GraphSerializer.fromJson(ngacGraph, graphJSON);

		return ngacGraph;
	}

	public static Prohibitions readProhibitions(String path) throws PMException, IOException {
		File prohibitionsFile = new File(path);

		String prohibitionsJSON = new String(Files.readAllBytes(Paths.get(prohibitionsFile.getAbsolutePath())));

		Prohibitions prohibitions = new MemProhibitions();

		ProhibitionsSerializer.fromJson(prohibitions, prohibitionsJSON);
		

		return prohibitions;
	}
	
	
	public MemGraph readAnyMemGraph(String path) throws PMException, IOException {
		File graphFile = new File(path);
		if(graphFile.isDirectory()) {
			MemGraph ngacGraph = readAllFilesInFolderToGraph(graphFile);
			return ngacGraph;
		}
		String graphJSON = new String(Files.readAllBytes(Paths.get(graphFile.getAbsolutePath())));

		MemGraph ngacGraph = new MemGraph();

		GraphSerializer.fromJson(ngacGraph, graphJSON);

		return ngacGraph;
	}
	
	public static Graph readGPMSGraph() throws PMException, IOException {
		File file_eligibility_policy = new File("Policies/GPMS/EligibilityPolicyClass.json");
		File file_org = new File("Policies/GPMS/AcademicUnitsPolicyClass.json");
		File file_adm = new File("Policies/GPMS/AdministrationUnitsPolicyClass.json");

		File editingFile = new File("Policies/GPMS/EditingPolicyClass.json");

		String eligibility_policy = new String(
				Files.readAllBytes(Paths.get(file_eligibility_policy.getAbsolutePath())));

		String org_policy = new String(Files.readAllBytes(Paths.get(file_org.getAbsolutePath())));
		String adm_policy = new String(Files.readAllBytes(Paths.get(file_adm.getAbsolutePath())));
		String editing_policy = new String(Files.readAllBytes(Paths.get(editingFile.getAbsolutePath())));
		Graph ngacGraph = new MemGraph();

		GraphSerializer.fromJson(ngacGraph, eligibility_policy);

		GraphSerializer.fromJson(ngacGraph, org_policy);
		GraphSerializer.fromJson(ngacGraph, adm_policy);

		GraphSerializer.fromJson(ngacGraph, editing_policy);

		return ngacGraph;
	}

	private static void saveCSV(List<String[]> data, String testMethod) throws PMException, IOException {
		boolean bool = true;
		String folderCSV = "CSV";
		File file = new File(folderCSV);
		if (!file.exists()) {
			bool = file.mkdir();
		}
		String folderTestSuits = "CSV/testSuits";
		File file2 = new File(folderTestSuits);
		if (!file2.exists() && bool) {
			bool = file2.mkdir();
		}
		if (bool) {
			// System.out.println("The directory was created or was already there");
		} else {
			// System.out.println("Failure with creating the directory");
			return;
		}
		String testSuiteFile = "CSV/testSuits/" + testMethod + "testSuite.csv";
		file = new File(testSuiteFile);
		if (file.createNewFile()) {
			// System.out.println("File has been created.");
		} else {

			// System.out.println("File already exists.");
		}
		BufferedWriter writer = null;
		writer = new BufferedWriter(new FileWriter(file));
		CSVWriter CSVwriter = new CSVWriter(writer);
		CSVwriter.writeAll(data);
		writer.flush();
		CSVwriter.close();

		if (writer != null)
			writer.close();

	}

	public static List<String[]> loadCSV(File csv) throws IOException {

		Reader reader = Files.newBufferedReader(Paths.get(csv.getAbsolutePath()));
		CSVReader csvReader = new CSVReader(reader);
		List<String[]> csvList = csvReader.readAll();
		reader.close();
		csvReader.close();
		return csvList;
	}

	public static List<String> getNodesByTypes(Graph graph, String... types)
			throws PMException, NoTypeProvidedException {
		if (types.length == 0) {
			throw new NoTypeProvidedException("Please provide at least one type to search for in graph");
		}
		List<String> listOfTypes = Arrays.asList(types);
		List<String> nodesToReturn = new ArrayList<String>();
		Node[] nodes = graph.getNodes().toArray(new Node[graph.getNodes().size()]);
		for (Node node : nodes) {
			if (listOfTypes.contains(node.getType().toString())) {
				nodesToReturn.add(node.getName());
			}
		}
		return nodesToReturn;
	}

	public static List<String> mergeTwoLists(List<String> list1, List<String> list2) {
		return Stream.of(list1, list2).flatMap(x -> x.stream()).collect(Collectors.toList());
	}

	public static Boolean verifyTestSuitIsForGraph(Graph graph, String testSuitPath) throws IOException, PMException {
		List<String[]> testSuite = loadCSV(new File(testSuitPath));
		for (String[] sArray : testSuite) {
			String UorUAname = sArray[1];
			String OorOAname = sArray[2];
			String ar = sArray[3];

			try {
				if (!graph.exists(UorUAname) || !graph.exists(OorOAname) || !getAllAccessRights(graph).contains(ar)) {
					return false;
				}
			} catch (NoTypeProvidedException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	private static void addJSONToGraph(MemGraph graph, String path) {
		File file = new File(path);
		String JSON = "";
		try {
			JSON = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			GraphSerializer.fromJson(graph, JSON);
		} catch (Exception e) {
			//System.out.println(e);
			//e.printStackTrace();
		}

	}

	public MemGraph readAllFilesInFolderToGraph(final File folder) {
		MemGraph graph = new MemGraph();
		for (final File fileEntry : folder.listFiles()) {
			if (!fileEntry.toString().endsWith(".json")) {
				continue;
			}
			try {
				 Prohibitions prohibition = readProhibitions(fileEntry.getPath());
				 continue;
			}
			catch(Exception ex) {
			}
			addJSONToGraph(graph, fileEntry.getAbsolutePath());
		}
		return graph;
	}
	
	public static JApplet getGraphVisualization(MemGraph graph) {
		GraphVisualizer gui = new GraphVisualizer(graph);
		gui.init();
		return gui.returnPane();	
	}
}
