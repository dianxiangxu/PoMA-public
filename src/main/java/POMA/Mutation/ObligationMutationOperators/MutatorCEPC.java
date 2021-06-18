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
import gov.nist.csd.pm.pip.obligations.model.Obligation;
import gov.nist.csd.pm.pip.obligations.model.PolicyClass;
import gov.nist.csd.pm.pip.obligations.model.Rule;

public class MutatorCEPC extends MutantTester2 {
//	String testMethod = "P";

	public MutatorCEPC(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
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
		String changeToPC;
		
		getAllPCNames();
		List<Rule> rules = obligation.getRules();
		for (Rule rule : rules) {
			ruleLabel = rule.getLabel();
			EventPattern eventPattern = rule.getEventPattern();
			PolicyClass pc = eventPattern.getPolicyClass();
			if (pc == null || (pc.getAnyOf().isEmpty() && pc.getEachOf().isEmpty())) {
				continue;
			}
			
			mutant = createObligationCopy();
			changeToPC = getRandomPolicyClass();
			System.out.println("change from current pc:" + pc.getAnyOf() + " to " + changeToPC);
//			System.out.println("change from current pc:" + pc.getEachOf() + " to " + changeToPC);
			mutant = changePC(mutant, ruleLabel, changeToPC);
			setObligationMutant(mutant);

//			//invoke junit to kill obligation_mutant
			testMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), "CEPC");
			setNumberOfMutants(getNumberOfMutants() + 1);
			
		}
//		System.out.println("Total number of mutant is " + getNumberOfMutants());
	}

	private Obligation changePC(Obligation obligation, String ruleLabel, String changeToPCName) {
		if (ruleLabel == null)
			return null;
		List<Rule> rules = obligation.getRules();
		List<Rule> newRules = new ArrayList<>();
		
		for (Rule newRule : rules) {
			if (newRule.getLabel().equals(ruleLabel)) {
				EventPattern eventPattern = newRule.getEventPattern();
				PolicyClass pc = eventPattern.getPolicyClass();
				List<String> anyOf = pc.getAnyOf();
				anyOf.clear();
				anyOf.add(changeToPCName);
				//change newUser to ""
				pc.setAnyOf(anyOf);
				eventPattern.setPolicyClass(pc);				
				newRule.setEventPattern(eventPattern);
			}
				
			newRules.add(newRule);
		}

		obligation.setRules(newRules);
		return obligation;
	}
	
	public String getRandomPolicyClass() {
		String pcName = PCs.get(0);
		
		return pcName;
	}
}
