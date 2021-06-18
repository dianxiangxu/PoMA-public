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
import gov.nist.csd.pm.pip.obligations.model.Rule;
import gov.nist.csd.pm.pip.obligations.model.Subject;

public class MutatorREU extends MutantTester2 {
//	String testMethod = "P";

	public MutatorREU(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
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
		double before, after;
		
		getAllUserNames();
		
		List<Rule> rules = obligation.getRules();
		for (Rule rule : rules) {
			ruleLabel = rule.getLabel();
			EventPattern eventPattern = rule.getEventPattern();
			Subject subject = eventPattern.getSubject();
			
			mutant = createObligationCopy();
			String user = subject.getUser();
			if (user == null) {
				List<String> anyUser = subject.getAnyUser();
				if (anyUser == null || anyUser.isEmpty()) {
					EvrProcess process = subject.getProcess();
					if (process == null) {
//						//have to do some default mutation if all all null
//						mutant = clearEventUser(mutant, ruleLabel);
						continue;
					} else {
						//not sure how to change process yet
//						mutant = changeEventProcess(mutant, ruleLabel, changeToUser);
					}
				} else {
					System.out.println("AnyUserToBeCleared" + anyUser);
					mutant = removeEventAnyUser(mutant, ruleLabel);
				}
			} else {
				System.out.println("UserToBeCleared:" + user);
				mutant = clearEventUser(mutant, ruleLabel);
			}
			setObligationMutant(mutant);

//			//invoke junit to kill obligation_mutant
			before = getNumberOfKilledMutants();
			testMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), "REU");
			after = getNumberOfKilledMutants();
			if (before == after) {
				//unkilled mutant caught
				System.out.println("Unkilled mutant (REU) " + ruleLabel);
				//just breakpoint for debug
				System.out.println("");
			}
			setNumberOfMutants(getNumberOfMutants() + 1);
		}
//		System.out.println("Total number of mutant is " + getNumberOfMutants());
	}

	private Obligation clearEventUser(Obligation obligation, String ruleLabel) {
		if (ruleLabel == null)
			return null;
		List<Rule> rules = obligation.getRules();
		List<Rule> newRules = new ArrayList<>();
		
		for (Rule newRule : rules) {
			if (newRule.getLabel().equals(ruleLabel)) {
				EventPattern eventPattern = newRule.getEventPattern();
				//change newUser to ""
				Subject subject = new Subject("");
//				Subject subject = Subject.class.getConstructor(String.class).newInstance(null);
				eventPattern.setSubject(subject);
				newRule.setEventPattern(eventPattern);
			}
				
			newRules.add(newRule);
		}

		obligation.setRules(newRules);
		return obligation;
	}
	private Obligation removeEventAnyUser(Obligation obligation, String ruleLabel) {
		if (ruleLabel == null)
			return null;
		List<Rule> rules = obligation.getRules();
		List<Rule> newRules = new ArrayList<>();
		
		for (Rule newRule : rules) {
			if (newRule.getLabel().equals(ruleLabel)) {
				EventPattern eventPattern = newRule.getEventPattern();
				List<String> anyUser = eventPattern.getSubject().getAnyUser();
				anyUser.clear();
				//change anyUser to empty array
				Subject subject = new Subject(anyUser);
//				Subject subject = Subject.class.getConstructor(String.class).newInstance(anyUser);
				eventPattern.setSubject(subject);
				newRule.setEventPattern(eventPattern);
			}
				
			newRules.add(newRule);
		}

		obligation.setRules(newRules);
		return obligation;
	}

	public String getRandomUserName() {
		//0 can be replaced by a random number from 0 to length(Us)
		String userName = Us.get(0);
		System.out.println(userName);
		return userName;
	}
}
