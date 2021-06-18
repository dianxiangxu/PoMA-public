package POMA.Mutation.ProhibitionMutationOperators;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
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

//Add one container
public class MutatorAOC extends ProhibitionMutation {

	public MutatorAOC(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException, PMException {
		super(testMethod, graph, prohibitions);
		// TODO Auto-generated constructor stub
	}

	public void main(Graph graph, Prohibitions prohibitions, String testMethod) throws PMException, IOException {
		String testSuitePath = getTestSuitPathByMethod(testMethod);
		File testSuite = new File(testSuitePath);
		String mutationMethod = "AOC";
		double before, after;
		
		//get all available containers/nodes
		Node[] nodes = getNodesInGraph();
		
		//change subject in prohibition1, save mutant to mutant.json//only for testing
		List<Prohibition> prohibitionList = getProhibitionList();
		for (Prohibition p : prohibitionList) {
			String name = p.getName();
			Prohibitions mutant = createProhibitionsCopy();
			Prohibition prohibitionMutant = mutant.get(name);
			Map<String, Boolean> containers = prohibitionMutant.getContainers();
			Set<String> containersKeySet = containers.keySet();
			//mutant: default complement is true
			System.out.println("Start mutator AOC/(complement=true)");
			for (Node containerToAdd : nodes) {
				//do not add duplicate node/container
				if (containersKeySet.contains(containerToAdd.getName()))
					continue;
				//exclude senseless container
				if (graph.getChildren(containerToAdd.getName()).isEmpty())
					continue;
				
				//default complement is true
				prohibitionMutant.addContainer(containerToAdd.getName(), true);
				//update prohibition
				mutant.update(name, prohibitionMutant);
				
				//try kill mutant
				before = getNumberOfKilledMutants();
				testMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), mutationMethod);
				after = getNumberOfKilledMutants();
				if (before == after) {
					System.out.println("Mutant is not killed! Prohibition name:" + name +  " Added container is " + containerToAdd.getName());
				}
				setNumberOfMutants(getNumberOfMutants() + 1);
//				System.out.println(node.getName());
				
//				saveDataToFile(ProhibitionsSerializer.toJson(mutant), "src/main/java/POMA/Mutation/ProhibitionMutationOperators/mutant.json");
				//restore containers conditions
				prohibitionMutant.removeContainerCondition(containerToAdd.getName());
				mutant.update(name, prohibitionMutant);
			}
			
			//mutant: default complement is false
			System.out.println("Start mutator AOC/(complement=false)");
			for (Node containerToAdd : nodes) {
				//do not add duplicate node/container
				if (containersKeySet.contains(containerToAdd.getName()))
					continue;
				//exclude senseless container
				if (graph.getChildren(containerToAdd.getName()).isEmpty())
					continue;
				//default complement is false
				prohibitionMutant.addContainer(containerToAdd.getName(), false);
				
				//update prohibition
				mutant.update(name, prohibitionMutant);
				
				//try kill mutant
				before = getNumberOfKilledMutants();
				testMutant(graph, mutant, testSuite, testMethod, getNumberOfMutants(), mutationMethod);
				after = getNumberOfKilledMutants();
				if (before == after) {
					System.out.println("Mutant is not killed! Prohibition name:" + name +  " Added container is " + containerToAdd.getName());
				}
				setNumberOfMutants(getNumberOfMutants() + 1);
//				System.out.println(node.getName());
				
				saveDataToFile(ProhibitionsSerializer.toJson(mutant), "src/main/java/POMA/Mutation/ProhibitionMutationOperators/mutant.json");
//				if (containerToAdd.getName().equals("UA2_2"))
//					break;
				//restore containers conditions
				prohibitionMutant.removeContainerCondition(containerToAdd.getName());
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
