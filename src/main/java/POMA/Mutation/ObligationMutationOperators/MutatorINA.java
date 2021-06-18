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
import gov.nist.csd.pm.pip.obligations.model.actions.DenyAction;
import gov.nist.csd.pm.pip.obligations.model.actions.DenyAction.Target;
import gov.nist.csd.pm.pip.obligations.model.actions.DenyAction.Target.Container;

//incorrect deny action
public class MutatorINA extends MutantTester2 {
//	String testMethod = "P";

	public MutatorINA(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
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
		Target target, oldTarget;
		Container newContainer;
		
		getAllEvrNodes();
		List<Rule> rules = obligation.getRules();
		for (Rule rule : rules) {
			index = 0;
			ruleLabel = rule.getLabel();
			ResponsePattern responsePattern = rule.getResponsePattern();
			List<Action> actions = responsePattern.getActions();
			for (Action actionToChange : actions) {
				if (!(actionToChange instanceof DenyAction)) {
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
				
				DenyAction newAction = new DenyAction();
				//Here, only mutate target node to another random existing node
				//This can be extended to mutate subject and mutate operation set as well

				oldTarget = ((DenyAction)actionToChange).getTarget();
				List<Container> containers = oldTarget.getContainers();
				for (Container container : containers) {
					//make a copy of newAction
					List<Container> tmpContainers = new ArrayList<>();
					for (Container c : containers) {
						tmpContainers.add(c);
					}
					tmpContainers.remove(container);

					for (Node node : UAsOAs) {
						if (node.getName().equals(container.getName()))
							continue;
						if (!node.getType().toString().equals(container.getType()))
							continue;
						newContainer = new Container(node.getName(), node.getType().toString(), node.getProperties());
						tmpContainers.add(newContainer);
						
						target = new Target();
						target.setContainers(tmpContainers);
						target.setComplement(oldTarget.isComplement());
						target.setIntersection(oldTarget.isIntersection());
						newAction.setLabel(((DenyAction)actionToChange).getLabel());
						newAction.setSubject(((DenyAction)actionToChange).getSubject());
						newAction.setOperations(((DenyAction)actionToChange).getOperations());
						newAction.setTarget(target);
					
						//keep conditions identical
						newAction.setCondition(actionToChange.getCondition());
						newAction.setNegatedCondition(actionToChange.getNegatedCondition());
						newActions.add(newAction);
						
						mutant = updateActions(mutant, ruleLabel, newActions);
					
						setObligationMutant(mutant);
						before = getNumberOfKilledMutants();
						//invoke junit to kill obligation_mutant
						testMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), "INA");
						after = getNumberOfKilledMutants();
						if (before == after) {
							//unkilled mutant caught
							System.out.println("Unkilled mutant (INA) " + ruleLabel + "|actionIndex:" 
												+ index +"|" + node.getName());
							//just breakpoint for debug
							System.out.println("");
						}
						setNumberOfMutants(getNumberOfMutants() + 1);
						//revert status 
						newActions.remove(newAction);
						tmpContainers.remove(newContainer);
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