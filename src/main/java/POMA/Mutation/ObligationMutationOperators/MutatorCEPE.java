package POMA.Mutation.ObligationMutationOperators;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import POMA.Exceptions.GraphDoesNotMatchTestSuitException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.obligations.model.EventPattern;
import gov.nist.csd.pm.pip.obligations.model.EvrNode;
import gov.nist.csd.pm.pip.obligations.model.Obligation;
import gov.nist.csd.pm.pip.obligations.model.Rule;
import gov.nist.csd.pm.pip.obligations.model.Target;

//change event policy element
public class MutatorCEPE extends MutantTester2 {
//	String testMethod = "P";

	public MutatorCEPE(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
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
		double before, after;
		
		getAllEvrNodes();
		List<Rule> rules = obligation.getRules();
		for (Rule rule : rules) {
			ruleLabel = rule.getLabel();
			EventPattern eventPattern = rule.getEventPattern();
			Target target = eventPattern.getTarget();
//			if (target == null) {
//				continue;
//			}
			List<EvrNode> policyElements = target.getPolicyElements();
			System.out.println(ruleLabel + "|" + getNumberOfMutants());
			for (EvrNode originPolicyElement : policyElements) {
				for (EvrNode changeToPolicyElement : EvrNodes) {
					if (originPolicyElement.equals(changeToPolicyElement)) {
						continue;
					}
					if (policyElements.contains(changeToPolicyElement)) {
						continue;
					}
					mutant = createObligationCopy();
					mutant = changeEventPolicyElement(mutant, ruleLabel, originPolicyElement, changeToPolicyElement);
					setObligationMutant(mutant);

//					//invoke junit to kill obligation_mutant
					before = getNumberOfKilledMutants();
					testMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), "CEPE");
					after = getNumberOfKilledMutants();
					if (before == after) {
						//unkilled mutant caught
						System.out.println("Unkilled mutant (CEPE) " + ruleLabel 
								+ "|originPE:" + originPolicyElement.getName()
								+ "|changeToPE:" + changeToPolicyElement.getName());
						//just breakpoint for debug
						System.out.println("");
					}
					setNumberOfMutants(getNumberOfMutants() + 1);
				}
			}
			
		}
//		System.out.println("Total number of mutant is " + getNumberOfMutants());
	}

	private Obligation changeEventPolicyElement(Obligation obligation, String ruleLabel, EvrNode originPE, EvrNode changeToPE) {
		if (ruleLabel == null)
			return null;
		List<Rule> rules = obligation.getRules();
		List<Rule> newRules = new ArrayList<>();
		
		for (Rule newRule : rules) {
			if (newRule.getLabel().equals(ruleLabel)) {
				EventPattern eventPattern = newRule.getEventPattern();
				Target target = eventPattern.getTarget();
				List<EvrNode> policyElements = target.getPolicyElements();
				List<EvrNode> newPEs = new ArrayList<>();
				for (EvrNode node : policyElements) {
					if (node.getName().equals(originPE.getName())) {
						newPEs.add(changeToPE);
					} else {
						newPEs.add(node);
					}
				}
				
				target.setPolicyElements(newPEs);
				eventPattern.setTarget(target);;				
				newRule.setEventPattern(eventPattern);
			}
				
			newRules.add(newRule);
		}

		obligation.setRules(newRules);
		return obligation;
	}
}
