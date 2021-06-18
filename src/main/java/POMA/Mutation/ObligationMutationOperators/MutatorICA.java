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
import gov.nist.csd.pm.pip.obligations.model.actions.CreateAction;
import gov.nist.csd.pm.pip.obligations.model.actions.CreateAction.CreateNode;

//incorrect create action
public class MutatorICA extends MutantTester2 {
//	String testMethod = "P";

	public MutatorICA(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
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
				if (!(actionToChange instanceof CreateAction)) {
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
				
				CreateAction newAction = new CreateAction();
				//CreateAction consists of a list of create action
				//Create action consists of node_what and node_where
				//Here, only mutate node_where to another random existing node
				//This can be extended to mutate both nodes
				List<CreateNode> createNodesList = ((CreateAction)actionToChange).getCreateNodesList();
				for (CreateNode cr : createNodesList) {
					what = cr.getWhat();
					//make a copy of newAction
					List<CreateNode> tmpCreateNodesList = new ArrayList<>();
					for (CreateNode n : createNodesList) {
						tmpCreateNodesList.add(n);
					}
					tmpCreateNodesList.remove(cr);
					newAction.setCreateNodesList(tmpCreateNodesList);
					
					oldWhere = cr.getWhere();
					for (Node newWhere : UAsOAs) {
						if (newWhere.getName().equals(oldWhere.getName()))
							continue;
						if (!newWhere.getType().toString().equals(oldWhere.getType()))
							continue;
						where = new EvrNode(newWhere.getName(), newWhere.getType().toString(), newWhere.getProperties());
						CreateNode node = new CreateNode(what, where);
						newAction.addCreateNode(node);
						
						//keep conditions identical
						newAction.setRules(null);
						newAction.setCondition(actionToChange.getCondition());
						newAction.setNegatedCondition(actionToChange.getNegatedCondition());
						newActions.add(newAction);
						
						mutant = updateActions(mutant, ruleLabel, newActions);
						
						setObligationMutant(mutant);

						before = getNumberOfKilledMutants();
//						//invoke junit to kill obligation_mutant
						testMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), "ICA");
						after = getNumberOfKilledMutants();
						if (before == after) {
							//unkilled mutant caught
							System.out.println("Unkilled mutant (ICA) " + ruleLabel + "|actionIndex:" + index + "|"
									+ actions.toString() + "|" + actionToChange.toString());
							//just breakpoint for debug
							System.out.println("");
						}
						setNumberOfMutants(getNumberOfMutants() + 1);
						newActions.remove(newAction);
						newAction.setCreateNodesList(tmpCreateNodesList);
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
	EvrNode getRandomWhere (String mutateName) {
		for (EvrNode node : EvrNodes) {
			if (node.getName().equals(mutateName))
				continue;
			if (!node.getType().equals("OA"))
				continue;
			return node;
		}
		return null;
	}
}