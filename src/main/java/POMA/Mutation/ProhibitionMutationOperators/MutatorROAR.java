package POMA.Mutation.ProhibitionMutationOperators;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import POMA.Exceptions.GraphDoesNotMatchTestSuitException;

//Remove one operation
public class MutatorROAR extends ProhibitionMutation {
	public MutatorROAR(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException, PMException {
		super(testMethod, graph, prohibitions);
		// TODO Auto-generated constructor stub
	}

	public void main(Graph graph, Prohibitions prohibitions, String testMethod) throws PMException, IOException {
		String testSuitePath = getTestSuitPathByMethod(testMethod);
		File testSuite = new File(testSuitePath);
		String mutationMethod = "ROAR";
		double before, after;
		
		//change subject in prohibition1, save mutant to mutant.json//only for testing
		List<Prohibition> prohibitionList = getProhibitionList();
		for (Prohibition p : prohibitionList) {
			String name = p.getName();
			Prohibitions mutant = createProhibitionsCopy();
			Prohibition prohibitionMutant = mutant.get(name);
			Set<String> ops = prohibitionMutant.getOperations();
			OperationSet operations = new OperationSet();
			
			for (String op : ops) {
				for (String s : ops) {
					if (s.equals(op))
						continue;
					operations.add(s);
				}
				prohibitionMutant.setOperations(operations);
				
				//update prohibition
				mutant.update(name, prohibitionMutant);
				
				//try kill mutant
				before = getNumberOfKilledMutants();
				testMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), mutationMethod);
				after = getNumberOfKilledMutants();
				if (before == after) {
					System.out.println("Mutant is not killed! Prohibition name:" + name +  "Removed operation is " + op);
				}
				setNumberOfMutants(getNumberOfMutants() + 1);
				
				//restore to origin mutant
				operations.add(op);
				prohibitionMutant.setOperations(operations);
				mutant.update(name, prohibitionMutant);
				operations.clear();
			}
		}
		if (getNumberOfMutants() == 0) {
			System.out.println("No mutant generated!");
		} else {
			System.out.println("#Mutant=" + getNumberOfMutants() + 
					"    #Killed=" + getNumberOfKilledMutants() + 
					"    MS=" + getNumberOfKilledMutants()*100/getNumberOfMutants() + "%");
		}
	}
		
	
	
}
