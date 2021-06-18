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

//negate condition
public class MutatorNCD extends MutantTester2 {
//	String testMethod = "P";

	public MutatorNCD(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
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
		List<Action> newActions = new ArrayList<>();
		double before, after;
		int index = 0;
		Action newAction;
		
		//negate condition before actions
		List<Rule> rules = obligation.getRules();
		for (Rule rule : rules) {
			ruleLabel = rule.getLabel();
			mutant = createObligationCopy();
			mutant = negateCondition(mutant, ruleLabel);
			if (mutant == null)
				continue;
			setObligationMutant(mutant);
			before = getNumberOfKilledMutants();
			//invoke junit to kill obligation_mutant
			testMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), "NCD");
			after = getNumberOfKilledMutants();
			if (before == after) {
				//unkilled mutant caught
				System.out.println("Unkilled mutant (NCD) " + ruleLabel);
			}
			setNumberOfMutants(getNumberOfMutants() + 1);
		}
		
		//negate condition within actions
		for (Rule rule : rules) {
			ruleLabel = rule.getLabel();
			ResponsePattern responsePattern = rule.getResponsePattern();
			List<Action> actions = responsePattern.getActions();
			index = 0;
			for (Action action : actions) {
				System.out.println("Running (NCD) " + ruleLabel + "|actionIndex:" + index);
				
				mutant = createObligationCopy();
				newAction = negateDeepCondition(action);
				
				//no mutant generated due to no condition nor negatedcondition
				if (newAction == null) {
					index++;
					continue;
				}
				newActions.clear();
				for (Action actionT : actions) {
					newActions.add(actionT);
				}
				newActions.remove(action);
				newActions.add(newAction);
				mutant = updateActions(mutant, ruleLabel, newActions);
				setObligationMutant(mutant);
				before = getNumberOfKilledMutants();
				//invoke junit to kill obligation_mutant
				testMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), "NCD");
				after = getNumberOfKilledMutants();
				if (before == after) {
					//unkilled mutant caught
					System.out.println("Unkilled mutant (NCD) " + ruleLabel + "|actionIndex:" + index);
					//just breakpoint for debug
					System.out.println("");
				}
				setNumberOfMutants(getNumberOfMutants() + 1);
				index++;
			}
		}
	}
	
	private Obligation negateCondition(Obligation obligation, String ruleLabel) throws PMException, IOException {
		List<Rule> rules = obligation.getRules();
		
		for (Rule newRule : rules) {
			if (newRule.getLabel().equals(ruleLabel)) {
				ResponsePattern responsePattern = newRule.getResponsePattern();
				Condition condition = responsePattern.getCondition();
				NegatedCondition negatedCondition = responsePattern.getNegatedCondition();
				List<Function> newConditions = new ArrayList<>();
				List<Function> newNegatedConditions = new ArrayList<>();
				
				
				if (condition == null) {
					if (negatedCondition == null)
						return null;
					else {
						//only have negateCondition
						newConditions = negatedCondition.getCondition();
						Condition newCondition = new Condition();
						newCondition.setCondition(newConditions);
						
						responsePattern.setCondition(newCondition);
						responsePattern.setNegatedCondition(null);
					}
				} else if (negatedCondition == null) {
					//only have Condition
					newNegatedConditions = condition.getCondition();
					NegatedCondition newNegatedCondition = new NegatedCondition();
					newNegatedCondition.setCondition(newNegatedConditions);
					
					responsePattern.setCondition(null);
					responsePattern.setNegatedCondition(newNegatedCondition);
				} else {
					//have both Conditon and negateCondition
					newConditions = negatedCondition.getCondition();
					newNegatedConditions = condition.getCondition();
					
					condition.setCondition(newConditions);
					negatedCondition.setCondition(newNegatedConditions);
					responsePattern.setCondition(condition);
					responsePattern.setNegatedCondition(negatedCondition);
				}
				
				newRule.setResponsePattern(responsePattern);
			}
		}
		return obligation;
	}
	

	private Action negateDeepCondition(Action action){
//		Action newAction
		Condition condition = action.getCondition();
		NegatedCondition negatedCondition = action.getNegatedCondition();
		List<Function> newConditions = new ArrayList<>();
		List<Function> newNegatedConditions = new ArrayList<>();
		if (condition == null) {
			if (negatedCondition == null)
				return null;
			else {
				//only have negateCondition
				newConditions = negatedCondition.getCondition();
				Condition newCondition = new Condition();
				newCondition.setCondition(newConditions);
				
				action.setCondition(newCondition);
				action.setNegatedCondition(null);
			}
		} else if (negatedCondition == null) {
			//only have Condition
			newNegatedConditions = condition.getCondition();
			NegatedCondition newNegatedCondition = new NegatedCondition();
			newNegatedCondition.setCondition(newNegatedConditions);
			
			action.setCondition(null);
			action.setNegatedCondition(newNegatedCondition);
		} else {
			//have both Conditon and negateCondition
			newConditions = negatedCondition.getCondition();
			newNegatedConditions = condition.getCondition();
			
			condition.setCondition(newConditions);
			negatedCondition.setCondition(newNegatedConditions);
			action.setCondition(condition);
			action.setNegatedCondition(negatedCondition);
		}
		return action;
	}
	private Obligation updateActions(Obligation obligation, String ruleLabel, List<Action> newActions) {
		if (ruleLabel == null)
			return null;
		List<Rule> rules = obligation.getRules();
		List<Rule> newRules = new ArrayList<>();
		
		for (Rule newRule : rules) {
			if (newRule.getLabel().equals(ruleLabel)) {
				ResponsePattern responsePattern = newRule.getResponsePattern();
				responsePattern.setActions(newActions);
				newRule.setResponsePattern(responsePattern);
			}
			newRules.add(newRule);
		}
		obligation.setRules(newRules);
		return obligation;
	}
}