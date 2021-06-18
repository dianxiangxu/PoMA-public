package POMA.Mutation.ProhibitionMutationOperators;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.OA;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.PC;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.UA;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import POMA.GlobalVariables;
import POMA.Utils;
import POMA.Exceptions.GraphDoesNotMatchTestSuitException;
import POMA.Exceptions.NoTypeProvidedException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.GraphSerializer;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.prohibitions.MemProhibitions;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.ProhibitionsSerializer;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

public class ProhibitionMutation {
	String mutationMethod = "";
	private double numberOfKilledMutants = 0;
	private int numberOfMutants = 0;
	List<Set<String>> operations = new ArrayList<Set<String>>();

	List<String[]> data = new ArrayList<String[]>();
	List<Node> OAs;
	public static Graph graph;
	public static Prohibitions prohibitions;
	String testMethod;
	public String initialGraphConfig = "Policies/LawUseCase";
//	public String initialGraphConfig = "Policies/ProhibitionExample/ProhibitionsMedicalExampleOA";

	static List<Node> UAs;
	static List<Node> UAsOAs;
	static List<Node> UAsPCs;
	static List<Node> UAsPCsOAs;

	public ProhibitionMutation(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException {
		this.testMethod = testMethod;
		this.graph = graph;
		this.prohibitions = prohibitions;
		try {
			//graph = Utils.readAnyGraph(initialGraphConfig);// .readGPMSGraph();
//			if (!Utils.verifyTestSuitIsForGraph(graph, getTestSuitPathByMethod(testMethod))) {
//				throw new GraphDoesNotMatchTestSuitException("Please verify that the testing suit is for this graph");
//			}
			getGraphLoaded();

		} catch (PMException | IOException e) {
			e.printStackTrace();
		}
	}
	public void setGraph(Graph graph) {
		this.graph = graph;
	}
	public void testMutant(Graph graph, Prohibitions prohibitions, File testSuiteCSV, String testMethod, int mutantNumber, String mutationMethod)
			throws PMException, IOException {

		List<String[]> testSuite = loadCSV(testSuiteCSV);
		if (mutantNumber == 0) {
			String[] header = new String[testSuite.size() + 2];
			header[0] = "MutantName";

			for (int i = 1; i < header.length; i++) {
				header[i] = "Test" + i;
			}
			header[header.length - 1] = "MutantKilled?";
			data.add(header);
		}

		PReviewDecider decider = new PReviewDecider(graph, prohibitions);

		String[] mutantTest = new String[testSuite.size() + 2];
		mutantTest[0] = mutationMethod + (mutantNumber + 1);
		int counter = 1;

		for (String[] sArray : testSuite) {

			String UAname = sArray[1];
			String OAname = sArray[2];
			if (UAname == null || OAname == null || !graph.exists(UAname) || !graph.exists(OAname)) {
				mutantTest[counter] = "Fail";
				counter++;
				continue;
			}

			String[] AR = { sArray[3] };

			Boolean result = Boolean.parseBoolean(sArray[4]);
			if (decider.check(UAname, "", OAname, AR) != result) {

				mutantTest[counter] = "Fail";
			} else {

				mutantTest[counter] = "Pass";
			}

			counter++;
		}
		String mutantKilled = "";
		if (Arrays.stream(mutantTest).anyMatch("Fail"::equals)) {
			mutantKilled = "Yes";
			setNumberOfKilledMutants(getNumberOfKilledMutants() + 1);
		} else {
			mutantKilled = "No";
		}
		mutantTest[mutantTest.length - 1] = mutantKilled;
		data.add(mutantTest);

	}

	public double calculateMutationScore(double numberOfMutations, double numberOfKilledMutants) {
		if(numberOfMutations == 0) {
			return 0;
		}
		return (numberOfKilledMutants / numberOfMutations * 100);
	}

	public List<String[]> loadCSV(File csv) throws IOException {

		Reader reader = Files.newBufferedReader(Paths.get(csv.getAbsolutePath()));
		CSVReader csvReader = new CSVReader(reader);
		List<String[]> csvList = csvReader.readAll();
		reader.close();
		csvReader.close();
		return csvList;
	}

	public void saveCSV(List<String[]> data, File directoryForTestResults, String testMethod)
			throws PMException, IOException {
		boolean bool = true;
		String folderCSV = "CSV";
		File file = new File(folderCSV);
		if (!file.exists()) {
			bool = file.mkdir();
		}
		String folderTests = "CSV/" + testMethod;
		File file2 = new File(folderTests);
		if (!file2.exists() && bool) {
			bool = file2.mkdir();
		}
		if (bool) {
			System.out.println("The directory was created or was already there");
		} else {
			System.out.println("Failure with creating the directory");
			return;
		}
		if (directoryForTestResults.createNewFile()) {
			System.out.println("File has been created.");
		} else {

			System.out.println("File already exists.");
		}
		BufferedWriter writer = null;
		writer = new BufferedWriter(new FileWriter(directoryForTestResults));
		CSVWriter CSVwriter = new CSVWriter(writer);
		CSVwriter.writeAll(data);
		writer.flush();
		CSVwriter.close();

		if (writer != null)
			writer.close();

	}

	public static File getFileFromResources(String fileName) {
		File resource = new File(fileName);
		return resource;
	}

	public void getOAsInGraph() throws PMException {
		System.out.println(graph);
		Node[] nodes = graph.getNodes().toArray(new Node[graph.getNodes().size()]);
		OAs = new ArrayList<Node>();
		for (Node node : nodes) {
			if (node.getType() == OA) {
				OAs.add(node);
			}

		}
	}

	public void getGraphLoaded(String filepath) throws PMException, IOException {
		File file = getFileFromResources(filepath);
		String json = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
		graph = new MemGraph();
		GraphSerializer.fromJson(graph, json);
		System.out.println(graph);
		getOAsInGraph();
		if (OAs.size() == 0) {
			System.out.println("No OAs found");
			return;
		}
		getUAsInGraph();
		if (UAs.size() == 0) {
			System.out.println("No UAs found");
			return;
		}
		loadAssociations();

		getUAsPCsInGraph();
		if (UAsPCs.size() == 0) {
			System.out.println("No UAs and PCs found");
			return;
		}
		getUAsPCsOAsInGraph();
		if (UAsPCsOAs.size() == 0) {
			System.out.println("No UAs, PCs, and OAs found");
			return;
		}
		getUAsOAsInGraph();
	}

	public void getGraphLoaded() throws PMException, IOException {
		getOAsInGraph();
		if (OAs.size() == 0) {
			System.out.println("No OAs found");
			return;
		}
		getUAsInGraph();
		if (UAs.size() == 0) {
			System.out.println("No UAs found");
			return;
		}
		loadAssociations();

		getUAsPCsInGraph();
		if (UAsPCs.size() == 0) {
			System.out.println("No UAs and PCs found");
			return;
		}
		getUAsPCsOAsInGraph();
		if (UAsPCsOAs.size() == 0) {
			System.out.println("No UAs, PCs, and OAs found");
			return;
		}
		getUAsOAsInGraph();
	}

	private void getUAsInGraph() throws PMException {
		UAs = new ArrayList<Node>();

		Node[] nodes = graph.getNodes().toArray(new Node[graph.getNodes().size()]);
		for (Node node : nodes) {
			if (node.getType() == UA) {
				UAs.add(node);
			}
		}
	}

	private void getUAsOAsInGraph() throws PMException {
		UAsOAs = new ArrayList<Node>();

		Node[] nodes = graph.getNodes().toArray(new Node[graph.getNodes().size()]);
		for (Node node : nodes) {
			if (node.getType() == UA || node.getType() == OA) {
				UAsOAs.add(node);
			}
		}

	}

	private void getUAsPCsInGraph() throws PMException {
		UAsPCs = new ArrayList<Node>();

		Node[] nodes = graph.getNodes().toArray(new Node[graph.getNodes().size()]);
		for (Node node : nodes) {
			if (node.getType() == UA || node.getType() == PC) {
				UAsPCs.add(node);
			}
		}

	}

	private void getUAsPCsOAsInGraph() throws PMException {
		UAsPCsOAs = new ArrayList<Node>();

		Node[] nodes = graph.getNodes().toArray(new Node[graph.getNodes().size()]);
		for (Node node : nodes) {
			if (node.getType() == UA || node.getType() == PC || node.getType() == OA) {
				UAsPCsOAs.add(node);
			}
		}

	}

	public double getNumberOfKilledMutants() {
		return numberOfKilledMutants;
	}

	public int getNumberOfMutants() {
		return numberOfMutants;
	}

	public void setNumberOfKilledMutants(double numberOfKilledMutants) {
		this.numberOfKilledMutants = numberOfKilledMutants;
	}

	public void setNumberOfMutants(int numberOfMutants) {
		this.numberOfMutants = numberOfMutants;
	}

	private void loadAssociations() throws PMException {
		Map<String, Set<String>> associations = new HashMap<String, Set<String>>();
		for (Node oa : OAs) {
			if (graph.getTargetAssociations(oa.getName()) != null) {
				associations.putAll(graph.getTargetAssociations(oa.getName()));
			}
			List<String> list = new ArrayList<String>(associations.keySet());

			for (int i = 0; i < list.size(); i++) {
				String objectID = list.get(i);
				Node obj = graph.getNode(objectID);

				if (!operations.contains(associations.get(objectID))) {
					operations.add(associations.get(objectID));
				}
				// graph.dissociate(objectID, oa.getID());
			}
		}
	}

	public Graph createCopy() throws PMException {
		Graph mutant = new MemGraph();
		String json = GraphSerializer.toJson(graph);

		GraphSerializer.fromJson(mutant, json);
		return mutant;
	}

	public String getMutationMethod() {
		return mutationMethod;
	}

	public void readGPMSGraph() throws PMException, IOException {
		File file_eligibility_policy = new File("Policies/GPMS/EligibilityPolicyClass.json");
		File file_org = new File("Policies/GPMS/AcademicUnitsPolicyClass.json");
		File file_adm = new File("Policies/GPMS/AdministrationUnitsPolicyClass.json");

		File editingFile = new File("Policies/GPMS/EditingPolicyClass.json");

		String eligibility_policy = new String(
				Files.readAllBytes(Paths.get(file_eligibility_policy.getAbsolutePath())));

		String org_policy = new String(Files.readAllBytes(Paths.get(file_org.getAbsolutePath())));
		String adm_policy = new String(Files.readAllBytes(Paths.get(file_adm.getAbsolutePath())));
		String editing_policy = new String(Files.readAllBytes(Paths.get(editingFile.getAbsolutePath())));
		graph = new MemGraph();

		GraphSerializer.fromJson(graph, eligibility_policy);

		GraphSerializer.fromJson(graph, org_policy);
		GraphSerializer.fromJson(graph, adm_policy);

		GraphSerializer.fromJson(graph, editing_policy);
		getOAsInGraph();
		if (OAs.size() == 0) {
			System.out.println("No OAs found");
			return;
		}
		getUAsInGraph();
		if (UAs.size() == 0) {
			System.out.println("No OAs found");
			return;
		}
		loadAssociations();

		getUAsPCsInGraph();
		if (UAsPCs.size() == 0) {
			System.out.println("No UAs and PCs found");
			return;
		}
		getUAsPCsOAsInGraph();
		if (UAsPCsOAs.size() == 0) {
			System.out.println("No UAs, PCs, and OAs found");
			return;
		}
		getUAsOAsInGraph();
	}

	public String getTestSuitPathByMethod(String testMethod) {
		File file = new File(GlobalVariables.currentPath);
		if(!file.isDirectory()) {
			file = file.getParentFile();
		}
		if(file==null) {
			return this.initialGraphConfig + "/CSV/testSuits/" + testMethod + "testSuite.csv";
		}
		return file.getAbsolutePath() + "/CSV/testSuits/" + testMethod + "testSuite.csv";
	}
	public String getTestSuitPathByMethod(String testMethod, String path) {
		File file = new File(path);
		if(!file.isDirectory()) {
			file = file.getParentFile();
		}
		System.out.println(file.getAbsolutePath());
		System.out.println(file.getAbsolutePath() + "/CSV/testSuits/" + testMethod + "testSuite.csv");
		return file.getAbsolutePath() + "/CSV/testSuits/" + testMethod + "testSuite.csv";
	}
	
	public static Node[] getNodesInGraph() throws PMException {
		Node[] nodes = graph.getNodes().toArray(new Node[graph.getNodes().size()]);
		return nodes;
	}
	
	public static List<Prohibition> getProhibitionList () throws PMException {
		return prohibitions.getAll();
	}
	
	public static Prohibitions createProhibitionsCopy() throws PMException {
		Prohibitions mutant = new MemProhibitions();
		String json = ProhibitionsSerializer.toJson(prohibitions);

		ProhibitionsSerializer.fromJson(mutant, json);
		return mutant;
	}
	
	public static void saveDataToFile(String code, String path) throws IOException {
		File file = new File(path);
		FileWriter myWriter = new FileWriter(file);
		myWriter.write(code);
		myWriter.close();
	}
	
	public static OperationSet getAllAccessRights(Graph graph) throws PMException, IOException {
		OperationSet ARSet = new OperationSet();
		for (Node SourceNode : UAs) {
			if (graph.getSourceAssociations(SourceNode.getName()) == null) {
				continue;
			}
			Map<String, OperationSet> sources = graph.getSourceAssociations(SourceNode.getName());
			List<String> targetNodes = new ArrayList<String>(sources.keySet());
			for (String targetNode : targetNodes) {
				Set<String> operateSet = sources.get(targetNode);
				OperationSet accessRights = new OperationSet(operateSet);
				ARSet.addAll(accessRights);
			}
		}
		// System.out.println("allAccessRightSet is :" + ARSet);
		return ARSet;
	}
	
    public static Prohibitions readProhibition(String filepath) throws PMException, IOException {
		File file = getFileFromResources(filepath);
		String json = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
		prohibitions = new MemProhibitions();
		ProhibitionsSerializer.fromJson(prohibitions, json);
		
//		System.out.println(json);
//		System.out.println(prohibitions.getAll().get(0).isIntersection());
//		System.out.println(prohibitions);
		
		//get list of all prohibitions
//		prohibitionList = prohibitions.getAll();
		
		return prohibitions;
	}
}
