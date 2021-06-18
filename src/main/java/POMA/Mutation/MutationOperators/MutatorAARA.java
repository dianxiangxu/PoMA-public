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
//add access right to association
public class MutatorAARA extends MutantTester {
	OperationSet allAccessRightSet;

	public MutatorAARA(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException {
		super(testMethod, graph, prohibitions);
	}

	public void init() throws PMException, IOException {
		this.mutationMethod = "AARA";
		String testResults = "CSV/" + testMethod + "/" + testMethod + "testResultsAARA.csv";
		String testSuitePath = getTestSuitPathByMethod(testMethod);
		// getGraphLoaded("GPMSPolicies/gpms_testing_config.json");
//		getGraphLoaded("GPMSPolicies/bank_policy_config.json");
		// getGraphLoaded(initialGraphConfig);

		// readGPMSGraph();

		allAccessRightSet = GraphUtils.getAllAccessRights();
		for (Node sourceNode : UAs) {
			performMutation(sourceNode, testMethod, testSuitePath);
		}
		saveCSV(data, new File(testResults), testMethod);
	}

	private void performMutation(Node sourceNode, String testMethod, String testSuitePath)
			throws PMException, IOException {
		File testSuite = new File(testSuitePath);
		double before, after;

		if (graph.getSourceAssociations(sourceNode.getName()) == null) {
			return;
		}

		Map<String, OperationSet> sources = graph.getSourceAssociations(sourceNode.getName());
		List<String> targetNodes = new ArrayList<String>(sources.keySet());

		for (String targetNode : targetNodes) {
			Set<String> operateSet = sources.get(targetNode);
			OperationSet accessRights = new OperationSet(operateSet);

			for (String accessRight : allAccessRightSet) {
				if (accessRights.contains(accessRight)) {
					continue;
				}
				Graph mutant = createCopy();
				addAccessRightToAssociate(mutant, sourceNode.getName(), targetNode, accessRights, accessRight);
				before = getNumberOfKilledMutants();
				try {
					testMutant(mutant, testSuite, testMethod, getNumberOfMutants(), mutationMethod);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					continue;
				}
				after = getNumberOfKilledMutants();
				if (before == after) {
					System.out.println("Unkilled mutant:" + "AARA:" 
								+ "sourceNode:" + sourceNode.getName() + " || "
								+ "targetNode:" + targetNode + " || " 
								+ "accessRights:" + accessRights.toString() + " || " 
								+ "addedAR:" + accessRight);
				}
				setNumberOfMutants(getNumberOfMutants() + 1);
			}
		}
	}

	private Graph addAccessRightToAssociate(Graph mutant, String SourceName, String targetName,
			OperationSet accessRights, String accessRight) throws PMException, IOException {
		OperationSet tmpAccessRights = new OperationSet();
		tmpAccessRights.addAll(accessRights);
		tmpAccessRights.add(accessRight);
		mutant.dissociate(SourceName, targetName);
		mutant.associate(SourceName, targetName, tmpAccessRights);
		return mutant;
	}

}