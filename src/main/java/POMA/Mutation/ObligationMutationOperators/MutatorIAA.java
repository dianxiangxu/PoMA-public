package POMA.Mutation.ObligationMutationOperators;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import POMA.Exceptions.GraphDoesNotMatchTestSuitException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.obligations.model.EvrNode;
import gov.nist.csd.pm.pip.obligations.model.Obligation;
import gov.nist.csd.pm.pip.obligations.model.ResponsePattern;
import gov.nist.csd.pm.pip.obligations.model.Rule;
import gov.nist.csd.pm.pip.obligations.model.actions.Action;
import gov.nist.csd.pm.pip.obligations.model.actions.AssignAction;
import gov.nist.csd.pm.pip.obligations.model.actions.AssignAction.Assignment;

//incorrect assign action
public class MutatorIAA extends MutantTester2 {
//	String testMethod = "P";

	public MutatorIAA(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
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
		EvrNode what, where, oldWhere;
		
		getAllEvrNodes();
		List<Rule> rules = obligation.getRules();
		for (Rule rule : rules) {
			index = 0;
			ruleLabel = rule.getLabel();
			ResponsePattern responsePattern = rule.getResponsePattern();
			List<Action> actions = responsePattern.getActions();
			for (Action actionToChange : actions) {
				//these types of action not handled
				if (!(actionToChange instanceof AssignAction)) {
					index++;
					continue;
				}
				mutant = createObligationCopy();
				//make a copy of newActions
				newActions.clear();
				for (Action action : actions) {
					newActions.add(action);
				}
				newActions.remove(actionToChange);
				
				AssignAction newAction = new AssignAction();
				//AssignAction consists of a list of Assignment
				//Assignment consists of node_what and node_where
				//Here, only mutate node_where to another random existing node
				//This can be extended to mutate both nodes
				List<Assignment> assignments = ((AssignAction)actionToChange).getAssignments();
				for (Assignment as : assignments) {
					what = as.getWhat();
					//make a copy of newAction
					List<Assignment> tmpAssignments = new ArrayList<>();
					for (Assignment n : assignments) {
						tmpAssignments.add(n);
					}
					tmpAssignments.remove(as);
					newAction.setAssignments(tmpAssignments);
					
					oldWhere = as.getWhere();
					for (Node newWhere : UAsOAs) {
						if (newWhere.getName().equals(oldWhere.getName()))
							continue;
						if (!newWhere.getType().toString().equals(oldWhere.getType()))
							continue;
						where = new EvrNode(newWhere.getName(), newWhere.getType().toString(), newWhere.getProperties());
						
						System.out.println("Unkilled mutant (IAA) " + ruleLabel + "|actionIndex:" + index);
						System.out.println(what.getName() + "|" + what.getType());
						System.out.println(as.getWhere().getName() + "|" + as.getWhere().getType());
						System.out.println(where.getName() + "|" + where.getType());
						Assignment assignment = new Assignment(what, where);
						newAction.addAssignment(assignment);
						
						//keep conditions identical
						newAction.setCondition(actionToChange.getCondition());
						newAction.setNegatedCondition(actionToChange.getNegatedCondition());
						newActions.add(newAction);
						
						mutant = updateActions(mutant, ruleLabel, newActions);
						
						setObligationMutant(mutant);

						before = getNumberOfKilledMutants();
//						//invoke junit to kill obligation_mutant
						testMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), "IAA");
						after = getNumberOfKilledMutants();
						if (before == after) {
							//unkilled mutant caught
							System.out.println("Unkilled mutant (IAA) " + ruleLabel + "|actionIndex:" 
												+ index +"|" + where.getName());
							//just breakpoint for debug
							System.out.println("");
						}
						setNumberOfMutants(getNumberOfMutants() + 1);
						newActions.remove(newAction);
						newAction.setAssignments(tmpAssignments);
					}
				}
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
	
	//return an OA node as node_where
	EvrNode getRandomWhere (EvrNode oldWhere) throws PMException {
		for (EvrNode node : EvrNodes) {
			if (node.getName().equals(oldWhere.getName()))
				continue;
			if (!node.getType().equals(oldWhere.getType()))
				continue;
			return node;
		}
		return null;
	}
}