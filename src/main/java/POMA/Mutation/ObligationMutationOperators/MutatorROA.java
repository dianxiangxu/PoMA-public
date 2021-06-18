package POMA.Mutation.ObligationMutationOperators;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import POMA.Exceptions.GraphDoesNotMatchTestSuitException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.obligations.model.Obligation;
import gov.nist.csd.pm.pip.obligations.model.ResponsePattern;
import gov.nist.csd.pm.pip.obligations.model.Rule;
import gov.nist.csd.pm.pip.obligations.model.actions.Action;

//remove one action
public class MutatorROA extends MutantTester2 {
//	String testMethod = "P";

	public MutatorROA(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
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
		Obligation mutant;
		List<Action> newActions = new ArrayList<>();
		double before, after;
		int index = 0;
		
		List<Rule> rules = obligation.getRules();
		for (Rule rule : rules) {
			//choose rules to test, all avoided by default
//			if (
//					rule.getLabel().equals("create_proposal") || 
//					rule.getLabel().equals("add_copi") ||
//					rule.getLabel().equals("submit_proposal")||
//					rule.getLabel().equals("add_sp")||
//					rule.getLabel().equals("delete_copi")||
//					rule.getLabel().equals("delete_sp") ||
//					rule.getLabel().equals("chair_approve") ||
//					rule.getLabel().equals("chair_disapprove") ||
//					rule.getLabel().equals("bm_approve") ||
//					rule.getLabel().equals("bm_disapprove") ||
//					rule.getLabel().equals("dean_approve") ||
//					rule.getLabel().equals("dean_disapprove") ||
//					rule.getLabel().equals("irb_approve") ||
//					rule.getLabel().equals("irb_disapprove") ||
//					rule.getLabel().equals("ra_approve") ||
//					rule.getLabel().equals("ra_disapprove") ||
//					rule.getLabel().equals("rd_approve") ||
//					rule.getLabel().equals("rd_disapprove") ||
////					rule.getLabel().equals("ra_submit")
//					) {
//				continue;
//			}
			index = 0;
			ruleLabel = rule.getLabel();
			ResponsePattern responsePattern = rule.getResponsePattern();
			List<Action> actions = responsePattern.getActions();
			for (Action actionToDelete : actions) {
				mutant = createObligationCopy();
				
				//delete one action from current action list
				newActions.clear();
				for (Action action : actions) {
					newActions.add(action);
				}
				newActions.remove(actionToDelete); 
				mutant = updateActions(mutant, ruleLabel, newActions);
				
				setObligationMutant(mutant);

				before = getNumberOfKilledMutants();
//				//invoke junit to kill obligation_mutant
				testMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), "ROA");
				after = getNumberOfKilledMutants();
				if (before == after) {
					//unkilled mutant caught
					System.out.println("Unkilled mutant (ROA) " + ruleLabel + "|actionIndex:" + index + "|"
							+ actions.toString() + "|" + actionToDelete.toString());
					//just breakpoint for debug
					System.out.println("");
				}
				setNumberOfMutants(getNumberOfMutants() + 1);
				index++;
			}
		}
//		System.out.println("Total number of mutant is " + getNumberOfMutants());
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