package POMA.Mutation.ObligationMutationOperators;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.OA;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.PC;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.UA;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import POMA.Utils;
import POMA.Exceptions.GraphDoesNotMatchTestSuitException;
import POMA.Exceptions.NoTypeProvidedException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.GraphSerializer;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.obligations.evr.EVRException;
import gov.nist.csd.pm.pip.obligations.evr.EVRParser;
import gov.nist.csd.pm.pip.obligations.model.EventPattern;
import gov.nist.csd.pm.pip.obligations.model.EvrNode;
import gov.nist.csd.pm.pip.obligations.model.Obligation;
import gov.nist.csd.pm.pip.obligations.model.Rule;
import gov.nist.csd.pm.pip.obligations.model.Target;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class MutantTester2 {
	String mutationMethod = "";
	private double numberOfKilledMutants = 0;
	private int numberOfMutants = 0;
	List<Set<String>> operations = new ArrayList<Set<String>>();

	List<String[]> data = new ArrayList<String[]>();
	List<Node> OAs;
	public static Graph graph;
	public static Obligation originObligation = new Obligation();
   final public static Obligation obligationMutant = new Obligation();
	String testMethod;
	// public String initialGraphConfig = "GPMSPolicies/SimpleGraphToSMT.json";
//	public String initialGraphConfig = "Policies/simpleGraphToSMT.json";
	public static String obligationFilePath = "";
	public static String obligationTestFilePath = "";

	static List<Node> UAs;
	static List<Node> UAsOAs;
	static List<Node> UAsPCs;
	static List<Node> UAsPCsOAs;
	
	static List<String> Us;
	static List<String> PCs;
	static OperationSet OPs;
	static List<EvrNode> EvrNodes;
	

	public MutantTester2(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
		this.testMethod = testMethod;
		this.graph = graph;
        this.obligationFilePath = obligationPath;
        this.obligationTestFilePath = "";

        String[] tmpString = obligationPath.split("/");
        for (int i = 0; i < tmpString.length - 1; i++) {
        	System.out.print(tmpString[i]);
            this.obligationTestFilePath += (tmpString[i] + "/");
        }

		try {
			//graph = Utils.readAnyGraph(initialGraphConfig);// .readGPMSGraph();
//			if (!Utils.verifyTestSuitIsForGraph(graph, getTestSuitPathByMethod(testMethod))) {
//				throw new GraphDoesNotMatchTestSuitException("Please verify that the testing suit is for this graph");
//			}
			getGraphLoaded();
			getObligationLoaded();
			

		} catch (PMException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setGraph(Graph graph) {
		this.graph = graph;
	}
	
	public static Graph getGraph() {
		return graph;
	}
	
	public static void setObligationMutant(Obligation obligation){
		obligationMutant.setEnabled(obligation.isEnabled());
		obligationMutant.setLabel(obligation.getLabel());
		obligationMutant.setRules(obligation.getRules());
		obligationMutant.setSource(obligation.getSource());
	}
	
	public static Obligation getObligationMutantCopy() { 
		Obligation obligation = new Obligation();
		obligation.setEnabled(obligationMutant.isEnabled());
		obligation.setLabel(obligationMutant.getLabel());
		obligation.setRules(obligationMutant.getRules());
		obligation.setSource(obligationMutant.getSource());
		return obligation;
	}
	//graph and obligation are redundant here
	public void testMutant(Graph graph, Obligation obligation, File testSuiteCSV, String testMethod, int mutantNumber, String mutationMethod)
			throws PMException, IOException {
		//invoke junit here
		//and collect mutation info
		JUnitCore junit = new JUnitCore();
		junit.addListener(new TextListener(System.out));
		Result result = null;
		
		result = junit.run(ObligationTestMutation.class);
//		result = junit.run(ObligationTestMutationConcise.class);
		if (result.getFailureCount() != 0) {
			//there exist error, killed mutant + 1
			setNumberOfKilledMutants(getNumberOfKilledMutants() + 1);
		}
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
//				Node obj = graph.getNode(objectID);

				if (!operations.contains(associations.get(objectID))) {
					operations.add(associations.get(objectID));
				}
				// graph.dissociate(objectID, oa.getID());
			}
		}
	}

	public static Graph createCopy() throws PMException {
		Graph mutant = new MemGraph();
		String json = GraphSerializer.toJson(graph);
		GraphSerializer.fromJson(mutant, json);
		return mutant;
	}
	
	public static Obligation createObligationCopy() throws FileNotFoundException, EVRException{
		return readGPMSObligation();
//		Obligation newObligation = new Obligation();
//		newObligation.setEnabled(originObligation.isEnabled());
//		newObligation.setLabel(originObligation.getLabel());
//		newObligation.setRules(originObligation.getRules());
//		newObligation.setSource(originObligation.getSource());
//		return newObligation;
	}

	public String getMutationMethod() {
		return mutationMethod;
	}
	
	public void readGPMSGraph() throws PMException, IOException {
		File file_eligibility_policy = new File("Policies/GPMS/EligibilityPolicyClass.json");
		File file_org = new File("Policies/GPMS/AcademicUnitsPolicyClass.json");
		File file_adm = new File("GPolicies/GPMS/AdministrationUnitsPolicyClass.json");

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
		return "CSV/testSuits/" + testMethod + "testSuite.csv";
	}
	
	public static Obligation readGPMSObligation() throws FileNotFoundException, EVRException {
		File obligationFile = getFileFromResources(obligationFilePath);
		InputStream inputStream = new FileInputStream(obligationFile);
		Obligation obligation = EVRParser.parse(inputStream);

		return obligation;
	}
	
	public void getObligationLoaded() throws FileNotFoundException, EVRException{
		File obligationFile = getFileFromResources(obligationFilePath);
		InputStream inputStream = new FileInputStream(obligationFile);
		originObligation = EVRParser.parse(inputStream);
	}
	public void getAllUserNames() throws PMException {
		Us = Utils.getUsInGraph(graph);
//		for (String user : Us)
//			System.out.println(user);
	}
	public void getAllPCNames() throws PMException {
		PCs = Utils.getPCsInGraph(graph);
//		for (String pc : PCs)
//			System.out.println(pc);
	}
	
	public void getAllOperationsInObligation() throws PMException, NoTypeProvidedException{
		OPs = Utils.getAllAccessRights(graph);
		for (String op : OPs)
			System.out.println(op);
	}
	
	//get all EvrNodes in obligation
	public void getAllEvrNodes() {
		EvrNodes = new ArrayList<>();
		List<Rule> rules = originObligation.getRules();
		for (Rule rule : rules) {
			EventPattern eventPattern = rule.getEventPattern();
			Target target = eventPattern.getTarget();
			List<EvrNode> policyElements = target.getPolicyElements();
			for (EvrNode node : policyElements) {
				if (EvrNodes.contains(node) == false)
					EvrNodes.add(node);
			}	
		}
		//print
//		for (EvrNode node : EvrNodes) {
//			System.out.println(node.getName());
//		}
	}
}
