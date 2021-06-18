package POMA.Mutation.ObligationMutationOperators;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import POMA.Exceptions.GraphDoesNotMatchTestSuitException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.obligations.model.EvrNode;
import gov.nist.csd.pm.pip.obligations.model.Obligation;
import gov.nist.csd.pm.pip.obligations.model.ResponsePattern;
import gov.nist.csd.pm.pip.obligations.model.Rule;
import gov.nist.csd.pm.pip.obligations.model.actions.Action;
import gov.nist.csd.pm.pip.obligations.model.actions.AssignAction;
import gov.nist.csd.pm.pip.obligations.model.actions.CreateAction;
import gov.nist.csd.pm.pip.obligations.model.actions.DeleteAction;
import gov.nist.csd.pm.pip.obligations.model.actions.DenyAction;
import gov.nist.csd.pm.pip.obligations.model.actions.DenyAction.Target;
import gov.nist.csd.pm.pip.obligations.model.actions.DenyAction.Target.Container;
import gov.nist.csd.pm.pip.obligations.model.actions.FunctionAction;
import gov.nist.csd.pm.pip.obligations.model.actions.GrantAction;
import gov.nist.csd.pm.pip.obligations.model.actions.AssignAction.Assignment;
import gov.nist.csd.pm.pip.obligations.model.actions.CreateAction.CreateNode;

//change one action
public class MutatorCAC extends MutantTester2 {
//	String testMethod = "P";

	public MutatorCAC(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
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
		EvrNode what, where;
		
		List<Rule> rules = obligation.getRules();
		for (Rule rule : rules) {
			index = 0;
			ruleLabel = rule.getLabel();
			ResponsePattern responsePattern = rule.getResponsePattern();
			List<Action> actions = responsePattern.getActions();
			for (Action actionToChange : actions) {
				//these types of action not handled
				if ((actionToChange instanceof FunctionAction) || (actionToChange instanceof DeleteAction)) {
					index++;
					continue;
				}
				mutant = createObligationCopy();
				newActions.clear();
				for (Action action : actions) {
					newActions.add(action);
				}
				newActions.remove(actionToChange);
				
				//change assign to create
				if ((actionToChange instanceof AssignAction)) {
					CreateAction newAction = new CreateAction();
					List<Assignment> assignments = ((AssignAction)actionToChange).getAssignments();
					
					for (Assignment as : assignments) {
						what = as.getWhat();
						where = as.getWhere();
						CreateNode node = new CreateNode(what, where);
						newAction.addCreateNode(node);
					}
					newAction.setRules(null);
					newAction.setCondition(actionToChange.getCondition());
					newAction.setNegatedCondition(actionToChange.getNegatedCondition());
					newActions.add(newAction);
				}
				//change create to assign
				if (actionToChange instanceof CreateAction) {
					AssignAction newAction = new AssignAction();
					List<CreateNode> createNodesList = ((CreateAction)actionToChange).getCreateNodesList();
					for (CreateNode cr : createNodesList) {
						what = cr.getWhat();
						where = cr.getWhere();
						Assignment assignment = new Assignment(what, where);
						newAction.addAssignment(assignment);
					}
					newAction.setCondition(actionToChange.getCondition());
					newAction.setNegatedCondition(actionToChange.getNegatedCondition());
					newActions.add(newAction);
				}
				//change grant to deny
				if (actionToChange instanceof GrantAction) {
					EvrNode node = ((GrantAction)actionToChange).getTarget();
					DenyAction.Target target = new Target();
					Container container = new Container(node.getName(), node.getType(), node.getProperties());
					List<Container> containers = new ArrayList<>();
					containers.add(container);
					
					DenyAction newAction = new DenyAction();
					newAction.setSubject(((GrantAction)actionToChange).getSubject());
					newAction.setOperations(((GrantAction)actionToChange).getOperations());
					target.setComplement(false);
					target.setIntersection(false);
					target.setContainers(containers);
					newAction.setTarget(target);
					newAction.setCondition(actionToChange.getCondition());
					newAction.setNegatedCondition(actionToChange.getNegatedCondition());
					newActions.add(newAction);
				}
				//change deny to grant
				if (actionToChange instanceof DenyAction) {
					
					Target target = ((DenyAction)actionToChange).getTarget();
					List<Container> containers = target.getContainers();
					//containers has at least one element
					Container container = containers.get(0);
					EvrNode node = new EvrNode(container.getName(), container.getType(), container.getProperties());
					
					GrantAction newAction = new GrantAction();
					newAction.setSubject(((DenyAction)actionToChange).getSubject());
					newAction.setOperations(((DenyAction)actionToChange).getOperations());
					newAction.setTarget(node);
					newAction.setCondition(actionToChange.getCondition());
					newAction.setNegatedCondition(actionToChange.getNegatedCondition());
					newActions.add(newAction);
				}
				
				mutant = updateActions(mutant, ruleLabel, newActions);
				
				setObligationMutant(mutant);

				before = getNumberOfKilledMutants();
//				//invoke junit to kill obligation_mutant
				testMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), "CAC");
				after = getNumberOfKilledMutants();
				if (before == after) {
					//unkilled mutant caught
					System.out.println("Unkilled mutant (CAC) " + ruleLabel + "|actionIndex:" + index + "|"
							+ actions.toString() + "|" + actionToChange.toString());
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