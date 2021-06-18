package POMA.Mutation.MutationOperators;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import POMA.Exceptions.GraphDoesNotMatchTestSuitException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
//change user attribute 
public class MutatorCUAA extends MutantTester {
	public MutatorCUAA(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException {
		super(testMethod, graph, prohibitions);
	}
	public void init() throws PMException, IOException {
		this.mutationMethod = "CUAA";
		String testResults = "CSV/"+testMethod+"/"+testMethod+"testResultsCUAA.csv";
		String testSuitePath = getTestSuitPathByMethod(testMethod);
//		getGraphLoaded("GPMSPolicies/gpms_testing_config.json");
	//	getGraphLoaded("GPMSPolicies/bank_policy_config.json");
	//	getGraphLoaded(initialGraphConfig);

		//readGPMSGraph();

		
		for (Node oldSourceNode : UAs) {
			performMutation(oldSourceNode, testMethod, testSuitePath);
		}
		saveCSV(data, new File(testResults), testMethod);
	}

	private void performMutation(Node oldSourceNode, String testMethod, String testSuitePath) throws PMException, IOException {
		File testSuite = new File(testSuitePath);
		double before, after;
		
		if (graph.getSourceAssociations(oldSourceNode.getName()) == null) {
			return;
		}
		
		Map<String, OperationSet> sources = graph.getSourceAssociations(oldSourceNode.getName());
		List<String> targetNodes = new ArrayList<String>(sources.keySet());
		
		for (String targetNode : targetNodes) {
			Set<String> operateSet = sources.get(targetNode);
			OperationSet accessRights = new OperationSet(operateSet);
				
			Graph mutant = createCopy();
			for (Node newSourceNode : UAs) {
				if (!newSourceNode.getName().equals(oldSourceNode.getName())) {
					changeUserAttributeOfAssociate(mutant, oldSourceNode.getName(), targetNode, newSourceNode.getName(), accessRights);
					before = getNumberOfKilledMutants();
					if(mutant==null) continue;
					try {
						testMutant(mutant, testSuite, testMethod, getNumberOfMutants(), mutationMethod);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						continue;
					}
					after = getNumberOfKilledMutants();
					if (before == after)
						System.out.println("Unkilled mutant:" + "CUAA:" 
									+ "oldSourceNode:" + oldSourceNode.getName() + " || " 
									+ "newSourceName:" + newSourceNode.getName() + " || " 
									+ "targetNode:" + targetNode + " || " 
									+ "accessRights:" + accessRights.toString());
					setNumberOfMutants(getNumberOfMutants() + 1);
				}
			}
		}
	}

	private void changeUserAttributeOfAssociate(Graph mutant, String oldSourceName, String targetName, String newSourceName, OperationSet accessRights) throws PMException, IOException {
		mutant.dissociate(oldSourceName, targetName);
		//Catch error detecting cycle after reverse assignment
		try {
		mutant.associate(newSourceName, targetName, accessRights);
		}
		catch(IllegalArgumentException e) {
			mutant = null;
		}
	}
}
