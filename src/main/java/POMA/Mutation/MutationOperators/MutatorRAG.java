package POMA.Mutation.MutationOperators;

import java.io.File;
import java.io.IOException;

import POMA.Utils;
import POMA.Exceptions.GraphDoesNotMatchTestSuitException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
//remove one assignment
public class MutatorRAG extends MutantTester {
	public MutatorRAG(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException {
		super(testMethod, graph, prohibitions);
	}
	public void init() throws PMException, IOException {
		this.mutationMethod = "RAGR";
		String testResults = "CSV/"+testMethod+"/"+testMethod+"testResultsRAGR.csv";
		String testSuitePath = getTestSuitPathByMethod(testMethod);
//		getGraphLoaded("GPMSPolicies/gpms_testing_config.json");
		//getGraphLoaded("GPMSPolicies/bank_policy_config.json");
	//	getGraphLoaded(initialGraphConfig);

		//readGPMSGraph();

		Node nodeB, nodePc;
		
		for (Node nodeA : UAsOAs) {
			for (String nodeBName : graph.getParents(nodeA.getName())) {
				nodeB = graph.getNode(nodeBName);
				if (nodeB.getType().toString().equals("PC")) {
//					System.out.println("cannot reverse assignment of PC.");
//					System.out.println("a is "+ua.toString()+"| b is "+ub.toString());
					continue;
				}
//				System.out.println("Debug:" + "RAD:" + "a:" + ua.toString() + " || " + "b:" + ub.toString() + " || " + "pc:" + pc.toString());
				
				String pcName = Utils.getPCOf(graph, nodeB.getName());
				if (pcName.isEmpty()) {
					System.out.println("PC not found.");
					continue;
				}
				nodePc = graph.getNode(pcName);
				try {
					performMutation(nodeA, nodeB, nodePc, testMethod, testSuitePath);
				}
				catch (IllegalArgumentException e) {
					e.printStackTrace();
					continue;
				}
			}
			
		}
		saveCSV(data, new File(testResults), testMethod);
		

	}

	private void performMutation(Node nodeA, Node nodeB, Node nodePc, String testMethod, String testSuitePath) throws PMException, IOException {
		File testSuite = new File(testSuitePath);
		double before, after;
		
		try {
			Graph mutant = createCopy();		
			mutant = RemoveAssignment(mutant, nodeA, nodeB);
			if (GraphUtils.isContained(nodeA, nodePc) != true) {
				//add assignment if node a is not PC-connected
				mutant.assign(nodeA.getName(), nodePc.getName());
			}
			
			before = getNumberOfKilledMutants();
			testMutant(mutant, testSuite,testMethod , getNumberOfMutants(), mutationMethod);
			after = getNumberOfKilledMutants();
			
			if (before == after)
				System.out.println("Unkilled mutant:" + "RAGR:" 
							+ "NodeA:" + nodeA.toString() + " || " 
							+ "NodeB:" + nodeB.toString() + " || " 
							+ "PC:" + nodePc.toString());
			setNumberOfMutants(getNumberOfMutants() + 1);
		}
		catch (PMException e) {
			//throw an error when detecting cycle after reverse assignment
			e.printStackTrace();
			System.out.println("RAGR_" + nodeA.toString() + "_" + nodeB.toString() + "_" + nodePc.toString());
		}
	}

	private Graph RemoveAssignment(Graph mutant, Node nodeA, Node nodeB) throws PMException, IOException {
		mutant.deassign(nodeA.getName(), nodeB.getName());
		return mutant;
	}
}
