package POMA.TestSuitGeneration;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.GraphSerializer;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import POMA.GlobalVariables;
import POMA.Exceptions.NoTypeProvidedException;


import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestSuitGenerator {

	public static void main(String[] args) throws Exception {
		String initialGraphPath = "";
		String initialProhibitionConfig = "";
		// loader.init();

		// loader.setPolicy(gt.getGraph());
		// loader.savePolicy(gt.getGraph(),"C:/data/ngac_config_Vlad.json");

		TestSuitGenerator graphTester = new TestSuitGenerator();

		/*
		 * File file = new File("C:/Users/dubro/git/GPMS-NGAC/mutants/REMNODE"); for
		 * (final File fileEntry : file.listFiles()) {
		 * 
		 * String json = new
		 * String(Files.readAllBytes(Paths.get(fileEntry.getAbsolutePath())));
		 * 
		 * Graph graph = GraphSerializer.fromJson(new MemGraph(), json);
		 * //System.out.println("Sneak path and Reachability:");
		 * GraphTesterReachabilitySneakpath gt = new
		 * GraphTesterReachabilitySneakpath(graph); List<Long> usersTrueFalse =
		 * gt.getUsers(); List<String[]> data = gt.testGraphPC();
		 * 
		 * graphTester.saveCSV(data); graphTester.COUNTER++; }
		 */
//		GraphTesterReachabilitySneakpath gt1 = new GraphTesterReachabilitySneakpath();
//		List<String[]> data1 = gt1.testGraphPC();
//		graphTester.saveCSV(data1, "RS");
//
//		GraphTesterReachability gt2 = new GraphTesterReachability();
//		List<String[]> data2 = gt2.testGraphPC();
//		graphTester.saveCSV(data2, "R");

//		initialGraphPath = "Policies/simpleGraphToSMT.json";
//		initialGraphPath = "Policies/GPMS/Graph.json";
//		initialGraphPath = "Policies/LawUseCase/Graph.json";
//		initialProhibitionConfig = "Policies/LawUseCase/prohibitions.json";
//		initialGraphPath = "Policies/BankPolicy/Complex/bank_policy_config.json";
		initialGraphPath = "Policies/ProhibitionExample/ProhibitionsMedicalExampleOA/graph.json";
//		initialProhibitionConfig = "Policies/ProhibitionExample/ProhibitionsMedicalExampleOA/prohibitionsx1.json";
		PairwiseTestSuitGenerator pairwiseGenerator = new PairwiseTestSuitGenerator(initialGraphPath, initialProhibitionConfig);
		List<String[]> data1 = pairwiseGenerator.generatPairwiseTests();
		graphTester.saveCSV("", data1, "Pairwise");
		AllCombTestSuitGenerator allCombinationsGenerator = new AllCombTestSuitGenerator(initialGraphPath, initialProhibitionConfig);
		List<String[]> data2 = allCombinationsGenerator.generateAllCombinationsTests();
		graphTester.saveCSV("", data2, "AllCombinations");

		// savePolicy(gt1.getGraph(),"GPMSPolicies/gpms_testing_config.json");
		/*
		 * File folder = new File("C:/Users/dubro/git/GPMS-NGAC/CSV/RS"); int i = 0; for
		 * (final File fileEntry1 : folder.listFiles()) { if (i == 0) { i++; continue; }
		 * //System.out.println("Killed number " +i + " mutant: " +
		 * !graphTester.compareCSV(new
		 * File("C:/Users/dubro/git/GPMS-NGAC/CSV/RS/csv0.csv"), fileEntry1)); i++; }
		 * //System.out.println("Mutant death ratio:" + i/(folder.listFiles().length -
		 * 1) * 100 + "%"); //System.out.println("Number of mutants" + (i-1));
		 */
		/*
		 * //System.out.println("============================================");
		 * 
		 * //loader.setPolicy(gt.graph); //loader.savePolicy(null);
		 * //System.out.println(""); //System.out.println("Reachability:");
		 * 
		 * GraphTesterReachability gttrue = new GraphTesterReachability(); List<Long>
		 * usersTrue = gttrue.getUsers(); gttrue.testGraphPC();
		 * //System.out.println("============================================");
		 * 
		 * //System.out.println("");
		 * //System.out.println("Pairwise combinations Path:"); GraphTesterPairwisePath
		 * gtptrue = new GraphTesterPairwisePath(); gtptrue.testGraphPairwisePC();
		 * //System.out.println("============================================");
		 * 
		 * //System.out.println("");
		 * //System.out.println("Pairwise combinations Path:");
		 * GraphTesterPairwiseNoPath gtpfalse = new GraphTesterPairwiseNoPath();
		 * gtpfalse.testGraphPairwisePC();
		 * //System.out.println("============================================");
		 * 
		 * 
		 * //System.out.println("Mutation:");
		 * 
		 * //GraphTesterReachabilitySneakpathMutation gtRSMutation = new
		 * GraphTesterReachabilitySneakpathMutation(); //gtRSMutation.mutator();
		 * 
		 * 
		 * //loader.setPolicy(gttrue.graph); //loader.savePolicy(null); // Graph graph =
		 * loader.getPolicy(); //HashMap<String, String> map = new
		 * HashMap<String,String>();
		 * 
		 * // Set<Node> set = graph.search("ProposalCreation", "PC", map); // gh = new
		 * GraphTester(graph, set.toArray(new Node[set.size()])[0]); // boolean result =
		 * false; //result = gh.testValidUser("tenuredU"); //result =
		 * gh.testValidUser("researchU"); //result = gh.testValidUser("tenure-trackU");
		 * //result = gh.testValidUser("tenureTrackTenuredU"); //result =
		 * gh.testValidUser("PIElegibleU");
		 * 
		 * ////System.out.println(result);
		 * 
		 */
	}

//	private void handleFileAllTests(String filePath) throws Exception{
//		File folder = new File(new File(filePath).getParent());
//		PairwiseTestSuitGenerator pairwiseGenerator = new PairwiseTestSuitGenerator(filePath);
//		List<String[]> data1 = pairwiseGenerator.generatPairwiseTests();
//		saveCSV(folder.getAbsolutePath()+"//", data1, "Pairwise");
////		AllCombTestSuitGenerator allCombinationsGenerator = new AllCombTestSuitGenerator(filePath);
////		List<String[]> data2 = allCombinationsGenerator.generateAllCombinationsTests();
////		saveCSV(folder.getAbsolutePath()+"//", data2, "AllCombinations");
//	}
//
//	private void handleFolderAllTests(String folderPath) throws Exception {
//		PairwiseTestSuitGenerator pairwiseGenerator = new PairwiseTestSuitGenerator(folderPath);
//		List<String[]> data1 = pairwiseGenerator.generatPairwiseTests();
//		saveCSV(folderPath, data1, "Pairwise");
////		AllCombTestSuitGenerator allCombinationsGenerator = new AllCombTestSuitGenerator(folderPath);
////		List<String[]> data2 = allCombinationsGenerator.generateAllCombinationsTests();
////		saveCSV(folderPath, data2, "AllCombinations");
//	}

	public void runAllTestGeneration() throws Exception {
		// String graphPath = "Policies/simpleGraphToSMT.json";
		String globalPath = "";
		File file = new File(GlobalVariables.currentPath);
//		if (!file.isDirectory()) {
//			// globalPath = file.getParent() + "\\";
//			globalPath = file.getAbsolutePath();
//			handleFileAllTests(globalPath);			
//		} else {
//			globalPath = GlobalVariables.currentPath + "\\";
//			handleFolderAllTests(globalPath);
//		}
	}

	// TODO
	public void runPairwiseTestSuitGeneration() throws Exception {

	}

	// TODO
	public void runAllCombinationsTestSuitGeneration() throws Exception {

	}

	private void saveCSV(String folderPath, List<String[]> data, String testMethod) throws PMException, IOException {
		boolean bool = true;
		String folderCSV = folderPath + "CSV";
		File file = new File(folderCSV);
		if (!file.exists()) {
			bool = file.mkdir();
		}
		String folderTestSuits = folderPath + "CSV/testSuits";
		File file2 = new File(folderTestSuits);
		if (!file2.exists() && bool) {
			bool = file2.mkdir();
		}
		if (bool) {
			System.out.println("The directory was created or was already there");
		} else {
			System.out.println("Failure with creating the directory");
			return;
		}
		String testSuiteFile = folderPath + "CSV/testSuits/" + testMethod + "testSuite.csv";
		file = new File(testSuiteFile);
		if (file.createNewFile()) {
			System.out.println("File has been created.");
		} else {

			System.out.println("File already exists.");
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

	/**
	 * @param path is the location and name to save the json policy if path is
	 *             provided null or empty string it will be saved to a default
	 *             location
	 * @throws PMException
	 * @throws IOException
	 */
	public static void savePolicy(Graph policy, String path) throws PMException, IOException {

		String policyString = GraphSerializer.toJson(policy);

		File file;
		if (path == null || path.isEmpty()) {
			file = new File("C:/data/ngac_config_Vlad3.json");
		} else {
			file = new File(path);
		}

		if (file.createNewFile()) {
			System.out.println("File has been created.");
		} else {

			System.out.println("File already exists.");
		}

		BufferedWriter writer = null;
		writer = new BufferedWriter(new FileWriter(file));
		writer.write(policyString);
		writer.flush();

		if (writer != null)
			writer.close();

	}

}
