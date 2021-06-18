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

//remove one factor
public class MutatorROF extends MutantTester2 {
//	String testMethod = "P";

	public MutatorROF(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
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
					Condition condition = responsePattern.getCondition();
					NegatedCondition negatedCondition = responsePattern.getNegatedCondition();
					List<Function> newConditions = new ArrayList<>();
					List<Function> newNegatedConditions = new ArrayList<>();
					List<Function> functions = new ArrayList<>();
				
					if (condition != null && condition.getCondition().size() >= 2) {
						int i = 0;
						functions = condition.getCondition();
						for (Function f : functions) {
							newConditions = addAllExcept(functions, f);
							condition.setCondition(newConditions);
							System.out.println("Running (ROF) " + ruleLabel + "|factorIndex:" + i);
							runMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), "ROF", ruleLabel);
							i++;
						}
					}
				}
			}
			
			mutant = createObligationCopy();
			newRules = mutant.getRules();
			for (Rule r : newRules) {
				if (r.getLabel().equals(ruleLabel)) {
					ResponsePattern responsePattern = r.getResponsePattern();
					Condition condition = responsePattern.getCondition();
					NegatedCondition negatedCondition = responsePattern.getNegatedCondition();
					List<Function> newConditions = new ArrayList<>();
					List<Function> newNegatedConditions = new ArrayList<>();
					List<Function> functions = new ArrayList<>();
					if (negatedCondition != null && negatedCondition.getCondition().size() >= 2) {
						int i = 0;
						functions = negatedCondition.getCondition();
						for (Function f : functions) {
							newNegatedConditions = addAllExcept(functions, f);
							negatedCondition.setCondition(newNegatedConditions);
							System.out.println("Running (ROF negated) " + ruleLabel + "|factorIndex:" + i);
							runMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), "ROF", ruleLabel);
							i++;
						}
					}
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
								
						Condition condition = newAction.getCondition();
						NegatedCondition negatedCondition = newAction.getNegatedCondition();
						List<Function> newConditions = new ArrayList<>();
						List<Function> newNegatedConditions = new ArrayList<>();
						List<Function> functions = new ArrayList<>();
						
						if (condition != null && condition.getCondition().size() >= 2) {
							int i = 0;
							functions = condition.getCondition();
							for (Function f : functions) {
								newConditions = addAllExcept(functions, f);
								condition.setCondition(newConditions);
								System.out.println("Running (ROF) " + ruleLabel + "|actionIndex:" + index + "|factorIndex:" + i);
								runMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), "ROF", ruleLabel);
								i++;
							}
						}
					}
				}
				
				mutant = createObligationCopy();
				newRules = mutant.getRules();
				for (Rule r : newRules) {
					if (r.getLabel().equals(ruleLabel)) {
						ResponsePattern newResponsePattern = r.getResponsePattern();
						List<Action> newActions = newResponsePattern.getActions();
						Action newAction = getAction(newActions, index);
						if (newAction == null) {
							System.out.println("this is a bug");
							break;
						}
								
						Condition condition = newAction.getCondition();
						NegatedCondition negatedCondition = newAction.getNegatedCondition();
						List<Function> newConditions = new ArrayList<>();
						List<Function> newNegatedConditions = new ArrayList<>();
						List<Function> functions = new ArrayList<>();
						if (negatedCondition != null && negatedCondition.getCondition().size() >= 2) {
							int i = 0;
							functions = negatedCondition.getCondition();
							for (Function f : functions) {
								newNegatedConditions = addAllExcept(functions, f);
								negatedCondition.setCondition(newNegatedConditions);
								System.out.println("Running (ROF negated) " + ruleLabel + "|actionIndex:" + index + "|factorIndex:" + i);
								runMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), "ROF", ruleLabel);
								i++;
							}
						}
					}
				}
				index++;
			}
		}
		
		
	}
	
	private boolean isAbleToMutate(Rule r) {
		ResponsePattern responsePattern = r.getResponsePattern();
		Condition condition = responsePattern.getCondition();
		NegatedCondition negatedCondition = responsePattern.getNegatedCondition();
	
		if (condition == null) {
			if (negatedCondition == null)
				return false;
			else {
				//only have negatedCondition
				if (negatedCondition.getCondition().size() == 1)
					return false;
			}
		} else if (negatedCondition == null) {
			//only have Condition
			if (condition.getCondition().size() == 1)
				return false;
		} else {
			//have both Conditon and negateCondition
			if (condition.getCondition().size() == 1)
				if (negatedCondition.getCondition().size() == 1)
					return false;
		}
		return true;
	}
	
	
	private boolean isAbleToMutate(Action action) {
		Condition condition = action.getCondition();
		NegatedCondition negatedCondition = action.getNegatedCondition();
		if (condition == null) {
			if (negatedCondition == null)
				return false;
			else {
				//only have negateCondition
				if (negatedCondition.getCondition().size() == 1)
					return false;
			}
		} else if (negatedCondition == null) {
			//only have Condition
			if (condition.getCondition().size() == 1)
				return false;
		} else {
			//have both Conditon and negateCondition
			if (condition.getCondition().size() == 1)
				if (negatedCondition.getCondition().size() == 1)
					return false;
		}
		return true;
	}
	
	//parameters: graph and obligation are redundant here
	public void runMutant (Graph graph, Obligation mutant, File testSuite, String testMethod, int mutantNumber, String mutationMethod, String ruleLabel) throws PMException, IOException {
		double before, after;
//		System.out.println("Running (ROF) " + ruleLabel);
		
		setObligationMutant(mutant);
		before = getNumberOfKilledMutants();
		//invoke junit to kill obligation_mutant
		testMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), "ROF");
		after = getNumberOfKilledMutants();
		if (before == after) {
			//unkilled mutant caught
			System.out.println("Unkilled mutant (ROF) " + ruleLabel);
		}
		setNumberOfMutants(getNumberOfMutants() + 1);
	}
	
	public List<Function> addAllExcept (List<Function> functions, Function f) {
		List<Function> newFunctions = new ArrayList<>();
		newFunctions.addAll(functions);
		newFunctions.remove(f);
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