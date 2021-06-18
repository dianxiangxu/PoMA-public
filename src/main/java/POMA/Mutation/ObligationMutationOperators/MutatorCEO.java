package POMA.Mutation.ObligationMutationOperators;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import POMA.Exceptions.GraphDoesNotMatchTestSuitException;
import POMA.Exceptions.NoTypeProvidedException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.obligations.model.EventPattern;
import gov.nist.csd.pm.pip.obligations.model.Obligation;
import gov.nist.csd.pm.pip.obligations.model.Rule;

//change event operation
public class MutatorCEO extends MutantTester2 {
//	String testMethod = "P";

	public MutatorCEO(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
		super(testMethod, graph, obligationPath);
	}

	public void init() throws PMException, IOException, NoTypeProvidedException {
		String testResults = "CSV/" + testMethod + "/" + testMethod + "testResultsCEU.csv";
		String testSuitePath = getTestSuitPathByMethod(testMethod);

		
		performMutation(testMethod, testSuitePath);
		saveCSV(data, new File(testResults), testMethod);
	}

	private void performMutation(String testMethod, String testSuitePath) throws PMException, IOException, NoTypeProvidedException {
		File testSuite = new File(testSuitePath);
		Graph graph = createCopy();
		Obligation obligation = createObligationCopy();
		String ruleLabel;
		Obligation mutant;
		double before, after;
		
		getAllOperationsInObligation();	
		List<Rule> rules = obligation.getRules();
		
		for (Rule rule : rules) {
			ruleLabel = rule.getLabel();
			EventPattern eventPattern = rule.getEventPattern();
			List<String> operations = eventPattern.getOperations();
			
			for (String operation : operations) {
				for (String changeToOperation : OPs) {
					mutant = createObligationCopy();
			
					//operation as input, to avoid change to same operation
					List<String> changeToOperationSet = getChangeToOpSet(operations, operation, changeToOperation);
					//no difference from mutant
					if (changeToOperationSet == null)
						continue;
					System.out.println("change operation:" + operations.toString() + " to " + changeToOperationSet);
					mutant = updateOperationSet(mutant, ruleLabel, changeToOperationSet);
				
					setObligationMutant(mutant);

					before = getNumberOfKilledMutants();
					//invoke junit to kill obligation_mutant
					testMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), "CEO");
					after = getNumberOfKilledMutants();
					if (before == after) {
						//unkilled mutant caught
						System.out.println("Unkilled mutant (CEO) " + ruleLabel + "|" 
								+ operations.toString() + "|" + changeToOperationSet.toString());
					}
					
					setNumberOfMutants(getNumberOfMutants() + 1);
					
				}
			}
			
		}
//		System.out.println("Total number of mutant is " + getNumberOfMutants());
	}
	
	//change operationset (in label: ruleLabel) to changeToOperationSet
	private Obligation updateOperationSet(Obligation obligation, String ruleLabel, List<String> changeToOperationSet) {
		if (ruleLabel == null)
			return null;
		List<Rule> rules = obligation.getRules();
		List<Rule> newRules = new ArrayList<>();
		
		for (Rule newRule : rules) {
			if (newRule.getLabel().equals(ruleLabel)) {
				EventPattern eventPattern = newRule.getEventPattern();
				
				eventPattern.setOperations(changeToOperationSet);				
				newRule.setEventPattern(eventPattern);
			}
				
			newRules.add(newRule);
		}

		obligation.setRules(newRules);
		return obligation;
	}

	public List<String> getChangeToOpSet(List<String> operations, String operation, String changeToOperation) {
		//test code
		if (changeToOperation.equals("delete-copi")) {
			return null;
		}
		if (changeToOperation.equals("add-copi")) {
			return null;
		}
		
		if (changeToOperation.equals(operation)) {
			return null;
		}
		if (operations.contains(changeToOperation)) {
			return null;
		}
		List<String> newOperations = new ArrayList<>();
		//changeToOperation != operation, and changeToOperation not in operations, but in OPs
		for (String newOp : operations) {
			if (newOp.equals(operation)) {
				newOperations.add(changeToOperation);
			} else {
				newOperations.add(newOp);
			}
		}
		return newOperations;
	}
}
