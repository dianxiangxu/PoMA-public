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
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
//change object attribute
public class MutatorCOAA extends MutantTester {
	public MutatorCOAA(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException {
		super(testMethod, graph, prohibitions);
	}
	public void init() throws PMException, IOException {
		this.mutationMethod = "COAA";
		String testResults = "CSV/"+testMethod+"/"+testMethod+"testResultsCOAA.csv";
		String testSuitePath = getTestSuitPathByMethod(testMethod);
		//getGraphLoaded("GPMSPolicies/gpms_testing_config.json");
//		getGraphLoaded("GPMSPolicies/bank_policy_config.json");
	//	getGraphLoaded(initialGraphConfig);

		//readGPMSGraph();

		
		for (Node sourceNode : UAs) {
			performMutation(sourceNode, testMethod, testSuitePath);
		}
		saveCSV(data, new File(testResults), testMethod);
	}

	private void performMutation(Node sourceNode, String testMethod, String testSuitePath) throws PMException, IOException {
		File testSuite = new File(testSuitePath);
		double before, after;
		
		if (graph.getSourceAssociations(sourceNode.getName()) == null) {
			return;
		}
		
		Map<String, OperationSet> sources = graph.getSourceAssociations(sourceNode.getName());
		List<String> oldTargetNodes = new ArrayList<String>(sources.keySet());
		
		for (String oldTargetNode : oldTargetNodes) {
			Set<String> operateSet = sources.get(oldTargetNode);
			OperationSet accessRights = new OperationSet(operateSet);
			Graph mutant = createCopy();
			
			for (Node newTargetNode : UAsOAs) {
				if (newTargetNode.getName().equals(sourceNode.getName())) {
					continue;
				}
				if (newTargetNode.getName().equals(oldTargetNode)) {
					continue;
				}
				if (newTargetNode.getType().equals(NodeType.UA)) {
					if (GraphUtils.isContained(newTargetNode, sourceNode)) {
						continue;
					}
				}
				changeObjectAttributeOfAssociate(mutant, sourceNode.getName(), oldTargetNode, newTargetNode.getName(), accessRights);
				before = getNumberOfKilledMutants();
				try {
					testMutant(mutant, testSuite, testMethod, getNumberOfMutants(), mutationMethod);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					continue;
				}
				after = getNumberOfKilledMutants();
				if (before == after) {
					System.out.println("Unkilled mutant:" + "COAA:" 
									+ "sourceNode:" + sourceNode.getName() + " || " 
									+ "newTargetName:" + newTargetNode.getName() + " || " 
									+ "oldTargetNode:" + oldTargetNode + " || " 
									+ "accessRights:" + accessRights.toString());
				}
					setNumberOfMutants(getNumberOfMutants() + 1);
			}
		}
	}

	private Graph changeObjectAttributeOfAssociate(Graph mutant, String SourceName, String oldTargetName, String newTargetName, OperationSet accessRights) throws PMException, IOException {
		mutant.dissociate(SourceName, oldTargetName);
		mutant.associate(SourceName, newTargetName, accessRights);
		return mutant;
	}
}