package POMA.Mutation.ProhibitionMutationOperators;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.ProhibitionsSerializer;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import POMA.Exceptions.GraphDoesNotMatchTestSuitException;

//Reverse Complement
public class MutatorRCT extends ProhibitionMutation {
	public MutatorRCT(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException, PMException {
		super(testMethod, graph, prohibitions);
		// TODO Auto-generated constructor stub
	}

	public void main(Graph graph, Prohibitions prohibitions, String testMethod) throws PMException, IOException {
		String testSuitePath = getTestSuitPathByMethod(testMethod);
		File testSuite = new File(testSuitePath);
		String mutationMethod = "RCT";
		double before, after;
		
		//change subject in prohibition1, save mutant to mutant.json//only for testing
		List<Prohibition> prohibitionList = getProhibitionList();
		for (Prohibition p : prohibitionList) {
			String name = p.getName();
			Prohibitions mutant = createProhibitionsCopy();
			Prohibition prohibitionMutant = mutant.get(name);
			Map<String, Boolean> containers = prohibitionMutant.getContainers();
			Set<String> containersKeySet = containers.keySet();
			for (String containerToReverse : containersKeySet) {
//				System.out.println(containerToRemove);
				boolean complement = containers.get(containerToReverse);
				containers.replace(containerToReverse, !complement);
				
				//update prohibition
				mutant.update(name, prohibitionMutant);
				
				//try kill mutant
				before = getNumberOfKilledMutants();
				testMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), mutationMethod);
				after = getNumberOfKilledMutants();
				if (before == after) {
					System.out.println("Mutant is not killed! Prohibition name:" + name +  "Reversed container is " + containerToReverse);
				}
				setNumberOfMutants(getNumberOfMutants() + 1);
				
				saveDataToFile(ProhibitionsSerializer.toJson(mutant), "src/main/java/POMA/Mutation/ProhibitionMutationOperators/mutant.json");
				
				//restore containers conditions
				containers.replace(containerToReverse, complement);
				mutant.update(name, prohibitionMutant);
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
