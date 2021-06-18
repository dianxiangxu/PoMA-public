package POMA.TestSuitGeneration;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.OA;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.PC;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.U;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.UA;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import POMA.Utils;
import POMA.Exceptions.NoTypeProvidedException;
import POMA.Mutation.ProhibitionMutationOperators.ProhibitionMutation;

public class PairwiseTestSuitGenerator {
	Graph graph;
	Prohibitions prohibitions;
	List<String> UAs;
	List<String> OAs;
	List<String> Us;
	List<String> Os;
	List<String> PCs;
	OperationSet operationSet;
	String[] allAccessRights;
	List<String> UAsOAs;
	List<String> UsOs;
	List<String> UsUAs;
	List<String> UAsOAsUsOs;

	public static void main(String[] args) throws PMException, InterruptedException, IOException {
		String simpleGraphPath = "GPMSPolicies/simpleGraphToSMT.json";
		String ProhibitionPath = "";

		PairwiseTestSuitGenerator gt;
		try {
			gt = new PairwiseTestSuitGenerator(simpleGraphPath, ProhibitionPath);
			List<String[]> testSuit = gt.generatPairwiseTests();
		} catch (NoTypeProvidedException e) {
			e.printStackTrace();
		}
	}

	public PairwiseTestSuitGenerator(String path, String pathProhibitions)
			throws PMException, InterruptedException, IOException, NoTypeProvidedException {
		File file = new File(path);
		if (!file.isDirectory()) {
			graph = Utils.readAnyGraph(path);
			if (!pathProhibitions.equals(""))
				prohibitions = ProhibitionMutation.readProhibition(pathProhibitions);
		} else {
			Utils utils = new Utils();
			graph = utils.readAllFilesInFolderToGraph(file);
			if (!pathProhibitions.equals(""))
				prohibitions = ProhibitionMutation.readProhibition(pathProhibitions);
		}
		populateLists(graph);

//		for (String s : Utils.getNodesByTypes(graph, "U", "UA", "PC", "OA")) {
//			System.out.println(graph.getNode(s).toString());
//		}
	}

	public PairwiseTestSuitGenerator() throws PMException, InterruptedException, IOException, NoTypeProvidedException {
		graph = Utils.readGPMSGraph();
		populateLists(graph);


	}

	private void populateLists(Graph graph) throws PMException, NoTypeProvidedException, IOException {
		UAs = Utils.getNodesByTypes(graph, "UA");
		UsUAs = Utils.getNodesByTypes(graph, "UA", "U");
		UAsOAsUsOs = Utils.getNodesByTypes(graph, "UA", "OA", "U", "O");
		operationSet = Utils.getAllAccessRights(graph);
		allAccessRights = operationSet.toArray(new String[operationSet.size()]);
	}

	public List<String[]> generatPairwiseTests() throws PMException {

		List<String[]> data = new ArrayList<String[]>();

//		PReviewDecider decider = new PReviewDecider(graph);
		PReviewDecider decider = new PReviewDecider(graph, prohibitions);

		int i = 1;

		int numberOfTrue = 0;
		int numberOfFalse = 0;

		for (int[] pair : getListOfPairs()) {
			String subject = UsUAs.get(pair[0]);
			String ar = allAccessRights[pair[1]];
			String target = UAsOAsUsOs.get(pair[2]);

			boolean result = decider.check(subject, "", target, ar);
			data.add(new String[] { Integer.toString(i), subject, target, ar, Boolean.toString(result) });
			i++;
			if (result == true) {
				numberOfTrue++;
			} else {
				numberOfFalse++;
			}

		}
		//System.out.println("True: " + numberOfTrue + " False: " + numberOfFalse);
		return data;
	}

	private ArrayList<int[]> getListOfPairs() {
		// int[] choices = { UsUAs.size(), operationSet.size(), UAsOAsUsOs.size(),
		// PCs.size() };
		int[] choices = { UsUAs.size(), operationSet.size(), UAsOAsUsOs.size() };
		boolean noShuffle = true;
		int maxGoes = 100;
		long seed = 42;
		return AllPairs.generatePairs(choices, seed, maxGoes, !noShuffle, null, false);
	}
}
