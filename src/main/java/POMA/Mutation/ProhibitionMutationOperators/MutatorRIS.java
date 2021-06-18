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

import POMA.Exceptions.GraphDoesNotMatchTestSuitException;

//Reverse Intersection
public class MutatorRIS extends ProhibitionMutation {
	public MutatorRIS(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException, PMException {
		super(testMethod, graph, prohibitions);
		// TODO Auto-generated constructor stub
	}

	public void main(Graph graph, Prohibitions prohibitions, String testMethod) throws PMException, IOException {
		String testSuitePath = getTestSuitPathByMethod(testMethod);
		File testSuite = new File(testSuitePath);
		String mutationMethod = "RIS";
		double before, after;
		
		//change subject in prohibition1, save mutant to mutant.json//only for testing
		List<Prohibition> prohibitionList = getProhibitionList();
		for (Prohibition p : prohibitionList) {
			String name = p.getName();
			Prohibitions mutant = createProhibitionsCopy();
			Prohibition prohibitionMutant = mutant.get(name);
			
			prohibitionMutant.setIntersection(!prohibitionMutant.isIntersection());
			
			//update prohibition
			mutant.update(name, prohibitionMutant);
			
			//try kill mutant
			before = getNumberOfKilledMutants();
			testMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), mutationMethod);
			after = getNumberOfKilledMutants();
			if (before == after) {
				System.out.println("Mutant is not killed! Prohibition name:" + name +  " Reversed intersection is " + prohibitionMutant.isIntersection());
			}
			setNumberOfMutants(getNumberOfMutants() + 1);
			
			saveDataToFile(ProhibitionsSerializer.toJson(mutant), "src/main/java/POMA/Mutation/ProhibitionMutationOperators/mutant.json");
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
