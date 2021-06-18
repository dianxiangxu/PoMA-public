package POMA.Mutation.ObligationMutationOperators;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import POMA.Exceptions.GraphDoesNotMatchTestSuitException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.obligations.model.Obligation;
import gov.nist.csd.pm.pip.obligations.model.ResponsePattern;
import gov.nist.csd.pm.pip.obligations.model.Rule;

//remove conditional action(to be implemented)
public class MutatorRCA extends MutantTester2 {
//	String testMethod = "P";

	public MutatorRCA(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
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
		Obligation ob = MutantTester2.originObligation;
		
		List<Rule> rules = obligation.getRules();
		for (Rule rule : rules) {
			ruleLabel = rule.getLabel();
			ResponsePattern responsePattern = rule.getResponsePattern();

			mutant = createObligationCopy();
//			mutant = removeEventPolicyElement(mutant, ruleLabel, peToDelete);
			setObligationMutant(mutant);

//			//invoke junit to kill obligation_mutant
			testMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), "RCA");
			setNumberOfMutants(getNumberOfMutants() + 1);
		}
//		System.out.println("Total number of mutant is " + getNumberOfMutants());
	}

}

