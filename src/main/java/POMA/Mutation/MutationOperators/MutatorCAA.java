package POMA.Mutation.MutationOperators;

import java.io.File;
import java.io.IOException;

import POMA.Utils;
import POMA.Exceptions.GraphDoesNotMatchTestSuitException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
//change assignment ascendent
public class MutatorCAA extends MutantTester {
	public MutatorCAA(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException {
		super(testMethod, graph, prohibitions);
	}

	public void init() throws PMException, IOException {
		this.mutationMethod = "CAA";
		String testResults = "CSV/" + testMethod + "/" + testMethod + "testResultsCAA.csv";
		String testSuitePath = getTestSuitPathByMethod(testMethod);
//		getGraphLoaded("GPMSPolicies/gpms_testing_config.json");
		// getGraphLoaded("GPMSPolicies/bank_policy_config.json");
		// getGraphLoaded(initialGraphConfig);

		// readGPMSGraph();

		Node nodeB, nodePc;

		for (Node nodeA : UAsOAs) {
			for (String obName : graph.getParents(nodeA.getName())) {
				nodeB = graph.getNode(obName);
				if (nodeB.getType().toString().equals("PC")) {
//					System.out.println("a is "+ua.toString()+"| b is "+ub.toString());
					continue;
				}
				String pcName = Utils.getPCOf(graph, nodeB.getName());
				if (pcName.isEmpty()) {
					System.out.println("PC not found.");
					continue;
				}
				nodePc = graph.getNode(pcName);
				for (Node nodeC : UAsOAs) {
					if (nodeC.getType() != nodeA.getType()) {
						continue;
					}
					if (nodeA.toString().equals(nodeC.toString()) || nodeB.toString().equals(nodeC.toString())) {
						continue;
					}
					if (graph.isAssigned(nodeC.getName(), nodeB.getName())) {
						// assignment <c,b> already exists
						continue;
					}
					if (GraphUtils.isContained(nodeB, nodeC)) {
						continue;
					}
					if (graph.isAssigned(nodeA.getName(), nodeC.getName())) {
						continue;
					}
					try {
						performMutation(nodeA, nodeB, nodeC, nodePc, testMethod, testSuitePath);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						continue;
					}
				}
			}

		}
		saveCSV(data, new File(testResults), testMethod);

	}

	private void performMutation(Node nodeA, Node nodeB, Node nodeC, Node nodePc, String testMethod,
			String testSuitePath) throws PMException, IOException {
		File testSuite = new File(testSuitePath);
		double before, after;

		Graph mutant = createCopy();

		mutant = changeAssignmentDescendent(mutant, nodeA, nodeB, nodeC);

		// if a cycle, here will throw e, below will not be conducted

		if (GraphUtils.isContained(nodeA, nodePc) != true) {
			// add assignment if node a is not PC-connected
			mutant.assign(nodeA.getName(), nodePc.getName());
		}

		before = getNumberOfKilledMutants();
		testMutant(mutant, testSuite, testMethod, getNumberOfMutants(), mutationMethod);
		after = getNumberOfKilledMutants();

		if (before == after)
			System.out.println("Unkilled mutant:" + "CAA:" + "a:" + nodeA.toString() + " || " + "b:" + nodeB.toString()
					+ " || " + "c:" + nodeC.toString());
		setNumberOfMutants(getNumberOfMutants() + 1);
	}

	private Graph changeAssignmentDescendent(Graph mutant, Node nodeA, Node nodeB, Node nodeC)
			throws PMException, IOException {
		mutant.deassign(nodeA.getName(), nodeB.getName());
		mutant.assign(nodeC.getName(), nodeB.getName());
		return mutant;
	}
}
