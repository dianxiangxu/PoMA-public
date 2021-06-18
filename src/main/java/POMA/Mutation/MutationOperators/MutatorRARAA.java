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
//remove an access right from all accociations
public class MutatorRARAA extends MutantTester {
	OperationSet allAccessRightSet;
	public MutatorRARAA(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException {
		super(testMethod, graph, prohibitions);
	}
	public void init() throws PMException, IOException {
		this.mutationMethod = "RARAA";
		String testResults = "CSV/"+testMethod+"/"+testMethod+"testResultsRARAA.csv";
		String testSuitePath = getTestSuitPathByMethod(testMethod);
		//getGraphLoaded("GPMSPolicies/gpms_testing_config.json");
//		getGraphLoaded("GPMSPolicies/bank_policy_config.json");
	//	getGraphLoaded(initialGraphConfig);

		//readGPMSGraph();

		allAccessRightSet = GraphUtils.getAllAccessRights();
		if (allAccessRightSet == null)
			return;
		for (String deleteAR : allAccessRightSet) {
			System.out.println("targeted deleteAR is :" + deleteAR);
			performMutation(deleteAR, testMethod, testSuitePath);
		}
		saveCSV(data, new File(testResults), testMethod);
	}

	private void performMutation(String deleteAR, String testMethod, String testSuitePath) throws PMException, IOException {
		File testSuite = new File(testSuitePath);
		double before, after;

		for (Node sourceNode : UAs) {
			if (graph.getSourceAssociations(sourceNode.getName()) == null) {
				continue;
			}
			Map<String, OperationSet> sources = graph.getSourceAssociations(sourceNode.getName());
			List<String> targetNodes = new ArrayList<String>(sources.keySet());
			for (String targetNode : targetNodes) {
				Set<String> operateSet = sources.get(targetNode);
				OperationSet accessRights = new OperationSet(operateSet);
			
				if (!accessRights.contains(deleteAR))
					continue;
				
				Graph mutant = createCopy();
			
				removeAccessRightFromAssociate(mutant, sourceNode.getName(), targetNode, accessRights, deleteAR);
				before = getNumberOfKilledMutants();
				try {
					testMutant(mutant, testSuite, testMethod, getNumberOfMutants(), mutationMethod);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					continue;
				}
				after = getNumberOfKilledMutants();
				if (before == after) {
					System.out.println("Unkilled mutant:" + "RARAA:" 
									+ "sourceNode:" + sourceNode.getName() + " || " 
									+ "targetNode:" + targetNode + " || " 
									+ "accessRights:" + accessRights.toString() + " || " 
									+ "removedAR:" + deleteAR);
				}
				setNumberOfMutants(getNumberOfMutants() + 1);
			}
		}
	}

	private Graph removeAccessRightFromAssociate(Graph mutant, String SourceName, String targetName, OperationSet accessRights, String accessRight) throws PMException, IOException {
		OperationSet tmpAccessRights = new OperationSet();
		tmpAccessRights.addAll(accessRights);
		tmpAccessRights.remove(accessRight);
		mutant.dissociate(SourceName, targetName);
		mutant.associate(SourceName, targetName, tmpAccessRights);
		return mutant;
	}
}