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

public class MutatorCEU extends MutantTester2 {
//	String testMethod = "P";

	public MutatorCEU(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
		super(testMethod, graph, obligationPath);
	}

	public void init() throws PMException, IOException {
		String testResults = "CSV/" + testMethod + "/" + testMethod + "testResultsCEU.csv";
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
		String changeToUser;
		
		getAllUserNames();
		
		List<Rule> rules = obligation.getRules();
		for (Rule rule : rules) {
			System.out.println("RuleLabel:" + rule.getLabel());
			ruleLabel = rule.getLabel();
			EventPattern eventPattern = rule.getEventPattern();
			Subject subject = eventPattern.getSubject();
			
			for (int i = 0; i < Us.size(); i++) {
			mutant = createObligationCopy();
			String user = subject.getUser();
			if (user == null) {
				List<String> anyUser = subject.getAnyUser();
				if (anyUser == null || anyUser.isEmpty()) {
					EvrProcess process = subject.getProcess();
					if (process == null) {
//						//have to do some default mutation if all all null
//						changeToUser = getRandomUserName();
//						mutant = changeEventUser(mutant, ruleLabel, changeToUser);
						continue;
					} else {
						//not sure how to change process yet
//						mutant = changeEventProcess(mutant, ruleLabel, changeToUser);
						//if user/anyuser/process are all null
						continue;
					}
				} else {
					System.out.println("AnyUser" + anyUser);
					anyUser.clear();
					
//					changeToUser = getRandomUserName();
					changeToUser = getUserName(i);
					anyUser.add(changeToUser);
					mutant = changeEventAnyUser(mutant, ruleLabel, anyUser);
				}
			} else {
				System.out.println("UserToBeChanged:" + user);
//				changeToUser = getRandomUserName();
				changeToUser = getUserName(i);
				mutant = changeEventUser(mutant, ruleLabel, changeToUser);
			}
			setObligationMutant(mutant);

//			//invoke junit to kill obligation_mutant
			testMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), "CEU");
			setNumberOfMutants(getNumberOfMutants() + 1);
			}
			
		}
//		System.out.println("Total number of mutant is " + getNumberOfMutants());
	}

	private Obligation changeEventUser(Obligation obligation, String ruleLabel, String changeToUserName) {
		if (ruleLabel == null)
			return null;
		List<Rule> rules = obligation.getRules();
		List<Rule> newRules = new ArrayList<>();
		
		for (Rule newRule : rules) {
			if (newRule.getLabel().equals(ruleLabel)) {
				EventPattern eventPattern = newRule.getEventPattern();
				//change newUser to ""
				Subject subject = new Subject(changeToUserName);
//				Subject subject = Subject.class.getConstructor(String.class).newInstance(changeToUserName);
				eventPattern.setSubject(subject);
				
				newRule.setEventPattern(eventPattern);
			}
				
			newRules.add(newRule);
		}

		obligation.setRules(newRules);
		return obligation;
	}
	private Obligation changeEventAnyUser(Obligation obligation, String ruleLabel, List<String> newAnyUser) {
		if (ruleLabel == null)
			return null;
		List<Rule> rules = obligation.getRules();
		List<Rule> newRules = new ArrayList<>();
		
		for (Rule newRule : rules) {
			if (newRule.getLabel().equals(ruleLabel)) {
				EventPattern eventPattern = newRule.getEventPattern();
				//change newUser to ""
				Subject subject = new Subject(newAnyUser);
//				Subject subject = Subject.class.getConstructor(String.class).newInstance(newAnyUser);
				eventPattern.setSubject(subject);
				
				newRule.setEventPattern(eventPattern);
			}
				
			newRules.add(newRule);
		}

		obligation.setRules(newRules);
		return obligation;
	}
	private Obligation changeEventProcess(Obligation obligation, String ruleLabel, Process process) {
		//to be implemented
		return obligation;
	}
	public String getRandomUserName() {
		//0 can be replaced by a random number from 0 to length(Us)
		String userName = Us.get(0);
		System.out.println("changeToUser:" + userName);
		return userName;
	}
	
	public String getUserName(int i) {
		//0 can be replaced by a random number from 0 to length(Us)
		String userName = Us.get(i);
		System.out.println("changeToUser:" + userName);
		return userName;
	}
}
