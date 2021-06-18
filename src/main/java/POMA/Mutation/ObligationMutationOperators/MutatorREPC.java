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
import gov.nist.csd.pm.pip.obligations.model.EvrProcess;
import gov.nist.csd.pm.pip.obligations.model.Obligation;
import gov.nist.csd.pm.pip.obligations.model.PolicyClass;
import gov.nist.csd.pm.pip.obligations.model.Rule;
import gov.nist.csd.pm.pip.obligations.model.Subject;

public class MutatorREPC extends MutantTester2 {
//	String testMethod = "P";

	public MutatorREPC(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
		super(testMethod, graph, obligationPath);
	}

	public void init() throws PMException, IOException {
		String testResults = "CSV/" + testMethod + "/" + testMethod + "testResultsREU.csv";
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
		
		getAllUserNames();
		
		List<Rule> rules = obligation.getRules();
		for (Rule rule : rules) {
			ruleLabel = rule.getLabel();
			EventPattern eventPattern = rule.getEventPattern();
			PolicyClass policyClass = eventPattern.getPolicyClass();
			if (policyClass == null)
				continue;
			
			mutant = createObligationCopy();
			List<String> eachPC = policyClass.getEachOf();
			if (eachPC == null || eachPC.isEmpty()) {
				List<String> anyPC = policyClass.getAnyOf();
				if (anyPC == null || anyPC.isEmpty()) {
					continue;
				} else {
					System.out.println("AnyPC:" + anyPC);
					mutant = removeEventAnyPC(mutant, ruleLabel);
				}
			} else {
				System.out.println(ruleLabel);
				System.out.println("EachPC:" + eachPC);
				mutant = removeEventEachPC(mutant, ruleLabel);
			}
			setObligationMutant(mutant);

//			//invoke junit to kill obligation_mutant
			testMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), "REPC");
			setNumberOfMutants(getNumberOfMutants() + 1);
			
		}
//		System.out.println("Total number of mutant is " + getNumberOfMutants());
	}

	private Obligation removeEventEachPC(Obligation obligation, String ruleLabel) {
		if (ruleLabel == null)
			return null;
		List<Rule> rules = obligation.getRules();
		List<Rule> newRules = new ArrayList<>();
		
		for (Rule newRule : rules) {
			if (newRule.getLabel().equals(ruleLabel)) {
				EventPattern eventPattern = newRule.getEventPattern();
				List<String> eachPC = eventPattern.getPolicyClass().getEachOf();
				//change newUser to null/""
				eachPC.clear();
				PolicyClass policyClass = new PolicyClass();
				policyClass.setEachOf(eachPC);
//				Subject subject = Subject.class.getConstructor(String.class).newInstance(null);
				eventPattern.setPolicyClass(policyClass);;
				newRule.setEventPattern(eventPattern);
			}
				
			newRules.add(newRule);
		}

		obligation.setRules(newRules);
		return obligation;
	}
	private Obligation removeEventAnyPC(Obligation obligation, String ruleLabel) {
		if (ruleLabel == null)
			return null;
		List<Rule> rules = obligation.getRules();
		List<Rule> newRules = new ArrayList<>();
		
		for (Rule newRule : rules) {
			if (newRule.getLabel().equals(ruleLabel)) {
				EventPattern eventPattern = newRule.getEventPattern();
				List<String> anyPC = eventPattern.getPolicyClass().getAnyOf();
				//change newUser to null/""
				anyPC.clear();
				PolicyClass policyClass = new PolicyClass();
				policyClass.setAnyOf(anyPC);
//				Subject subject = Subject.class.getConstructor(String.class).newInstance(null);
				eventPattern.setPolicyClass(policyClass);;
				newRule.setEventPattern(eventPattern);
			}
				
			newRules.add(newRule);
		}

		obligation.setRules(newRules);
		return obligation;
	}
}
