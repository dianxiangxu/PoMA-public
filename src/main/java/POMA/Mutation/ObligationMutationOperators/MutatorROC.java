package POMA.Mutation.ObligationMutationOperators;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import POMA.Exceptions.GraphDoesNotMatchTestSuitException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.obligations.model.Condition;
import gov.nist.csd.pm.pip.obligations.model.NegatedCondition;
import gov.nist.csd.pm.pip.obligations.model.Obligation;
import gov.nist.csd.pm.pip.obligations.model.ResponsePattern;
import gov.nist.csd.pm.pip.obligations.model.Rule;
import gov.nist.csd.pm.pip.obligations.model.actions.Action;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;

//negate one factor
public class MutatorROC extends MutantTester2 {
//	String testMethod = "P";

	public MutatorROC(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
		super(testMethod, graph, obligationPath);
	}

	public void init() throws PMException, IOException {
		String testResults = "CSV/" + testMethod + "/" + testMethod + "testResultsCEPC.csv";
		String testSuitePath = getTestSuitPathByMethod(testMethod);

		
		performMutation(testMethod, testSuitePath);
		saveCSV(data, new File(testResults), testMethod);
	}

	private void performMutation(String testMethod, String testSuitePath) throws PMException, IOException {		
		File testSuite = new File(testSuitePath);
		Graph graph = createCopy();
		Obligation obligation = createObligationCopy();
		String ruleLabel;
		Obligation mutant = createObligationCopy();
		int index = 0;
		
		//negate condition before actions
		List<Rule> rules = obligation.getRules();
		for (Rule rule : rules) {
			ruleLabel = rule.getLabel();
			if (isAbleToMutate(rule) == false)
				continue;
			mutant = createObligationCopy();
			List<Rule> newRules = mutant.getRules();
			for (Rule r : newRules) {
				if (r.getLabel().equals(ruleLabel)) {
					ResponsePattern responsePattern = r.getResponsePattern();
					if (responsePattern.getCondition() != null) {
						responsePattern.setCondition(null);
					} else {
						responsePattern.setNegatedCondition(null);
					}
					System.out.println("Running (ROC) " + ruleLabel);
					runMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), "NOF", ruleLabel);
				}
			}
		}
		
		//negate condition within actions
		for (Rule rule : rules) {
			ruleLabel = rule.getLabel();
			ResponsePattern responsePattern = rule.getResponsePattern();
			List<Action> actions = responsePattern.getActions();
			index = 0;
			for (Action action : actions) {
				if (isAbleToMutate(action) == false) {
					System.out.println("Not available for mutation.");
					index++;
					continue;
				}
				
				mutant = createObligationCopy();
				List<Rule> newRules = mutant.getRules();
				//locate action and perform mutation
				for (Rule r : newRules) {
					if (r.getLabel().equals(ruleLabel)) {
						ResponsePattern newResponsePattern = r.getResponsePattern();
						List<Action> newActions = newResponsePattern.getActions();
						Action newAction = getAction(newActions, index);
						if (newAction == null) {
							System.out.println("this is a bug");
							break;
						}
						if (newAction.getCondition() != null) {
							newAction.setCondition(null);
						} else {
							newAction.setNegatedCondition(null);
						}
						System.out.println("Running (ROC) " + ruleLabel + "|actionIndex:" + index);
						runMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), "ROC", ruleLabel);
					}
				}
				index++;
			}
		}
		
		
	}
	
	private boolean isAbleToMutate(Rule r) {
		ResponsePattern responsePattern = r.getResponsePattern();
		if ((responsePattern.getCondition() == null) && (responsePattern.getNegatedCondition() == null)) 
			return false;
		return true;
	}
	
	
	private boolean isAbleToMutate(Action action) {
		Condition condition = action.getCondition();
		NegatedCondition negatedCondition = action.getNegatedCondition();
		if (condition == null)
			if (negatedCondition == null)
				return false;
		return true;
	}
	
	//parameters: graph and obligation are redundant here
	public void runMutant (Graph graph, Obligation mutant, File testSuite, String testMethod, int mutantNumber, String mutationMethod, String ruleLabel) throws PMException, IOException {
		double before, after;
//		System.out.println("Running (ROC) " + ruleLabel);
		
		setObligationMutant(mutant);
		before = getNumberOfKilledMutants();
		//invoke junit to kill obligation_mutant
		testMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), "ROC");
		after = getNumberOfKilledMutants();
		if (before == after) {
			//unkilled mutant caught
			System.out.println("Unkilled mutant (ROC) " + ruleLabel);
		}
		setNumberOfMutants(getNumberOfMutants() + 1);
	}
	
	public List<Function> addAllExcept (List<Function> functions, Function f) {
		List<Function> newFunctions = new ArrayList<>();
		newFunctions.addAll(functions);
		newFunctions.remove(f);
		return newFunctions;
	}
	public List<Function> addFunction (List<Function> functions, Function f) {
		List<Function> newFunctions = new ArrayList<>();
		newFunctions.addAll(functions);
		newFunctions.add(f);
		return newFunctions;
	}
	public Action getAction (List<Action> actions, int index) {
		int i = 0;
		for (Action a : actions) {
			if (i == index)
				return a;
			i++;
		}
		return null;
	}
}