package CaseStudies.ProhibitionsUseCase;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.GraphSerializer;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.prohibitions.MemProhibitions;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.ProhibitionsSerializer;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class SimpleExample {

	public static void main(String[] argv) throws PMException, IOException {
		Graph g = new MemGraph(); 	  
  
		g.createPolicyClass("PC1", null);

		g.createNode("Container1", UA, null, "PC1");
		g.createNode("Container2", UA, null, "PC1");
		g.createNode("UA1_1", UA, null, "Container1");
		g.createNode("UA2_2", UA, null, "Container2");
		g.createNode("UA3_1_2", U, null, "Container1", "Container2");

		g.createNode("UA_test1", UA, null, "PC1");
		g.associate("UA_test1", "Container1", new OperationSet("test"));
		g.associate("UA_test1", "Container2", new OperationSet("test"));

		saveDataToFile(GraphSerializer.toJson(g), "Policies/ProhibitionExample/ProhibitionsInitialExample/graph.json");	
		System.out.println("PROHIBITION#1(COMPLEMENT=FALSE) UA_test1 test Container1 ");
		Prohibitions prohibitions1 = new MemProhibitions();
		Prohibition prohibition = new Prohibition.Builder("prohibition1", "UA_test1", new OperationSet("test"))
				.addContainer("Container1", false).build();
		
		prohibitions1.add(prohibition);
		saveDataToFile(ProhibitionsSerializer.toJson(prohibitions1), "Policies/ProhibitionExample/ProhibitionsInitialExample/prohibitionsx1.json");	

		PReviewDecider decider = new PReviewDecider(g, prohibitions1);
		System.out.println("UA_test1, UA1_1, test"+": "+decider.check("UA_test1", "", "UA1_1", "test"));
		System.out.println("UA_test1, UA2_2, test"+": "+decider.check("UA_test1", "", "UA2_2", "test"));

		System.out.println("UA_test1, UA3_1_2, test"+": "+decider.check("UA_test1", "", "UA3_1_2", "test"));
		System.out.println("UA_test1, Container1, test"+": "+decider.check("UA_test1", "", "Container1", "test"));
		System.out.println("UA_test1, Container2, test"+": "+decider.check("UA_test1", "", "Container2", "test"));
		
		System.out.println();		
		System.out.println("PROHIBITION#2(COMPLEMENT=TRUE) UA_test1 test Container1 ");
		Prohibitions prohibitions2 = new MemProhibitions();
		Prohibition prohibition2 = new Prohibition.Builder("prohibition1", "UA_test1", new OperationSet("test"))
				.addContainer("Container1", true).build();
		prohibitions2.add(prohibition2);
		saveDataToFile(ProhibitionsSerializer.toJson(prohibitions2), "Policies/ProhibitionExample/ProhibitionsInitialExample/prohibitions2.json");	

		PReviewDecider decider2 = new PReviewDecider(g, prohibitions2);
		System.out.println("UA_test1, UA1_1, test"+": "+decider2.check("UA_test1", "", "UA1_1", "test"));
		System.out.println("UA_test1, UA2_2, test"+": "+decider2.check("UA_test1", "", "UA2_2", "test"));
		System.out.println("UA_test1, UA3_1_2, test"+": "+decider2.check("UA_test1", "", "UA3_1_2", "test"));
		System.out.println("UA_test1, Container1, test"+": "+decider2.check("UA_test1", "", "Container1", "test"));
		System.out.println("UA_test1, Container2, test"+": "+decider2.check("UA_test1", "", "Container2", "test"));
		
		
		
		
		System.out.println();		
		System.out.println("PROHIBITION#3(COMPLEMENT=FALSE) UA_test1 test Container1 Container2 (INTERSECTION=TRUE)");
		Prohibitions prohibitions3 = new MemProhibitions();
		Prohibition prohibition3 = new Prohibition.Builder("prohibition1", "UA_test1", new OperationSet("test"))
				.addContainer("Container1", false).addContainer("Container2", false).setIntersection(true).build();
		prohibitions3.add(prohibition3);
		saveDataToFile(ProhibitionsSerializer.toJson(prohibitions3), "Policies/ProhibitionExample/ProhibitionsInitialExample/prohibitions3.json");	

		PReviewDecider decider3 = new PReviewDecider(g, prohibitions3);
		System.out.println("UA_test1, UA1_1, test"+": "+decider3.check("UA_test1", "", "UA1_1", "test"));
		System.out.println("UA_test1, UA2_2, test"+": "+decider3.check("UA_test1", "", "UA2_2", "test"));

		System.out.println("UA_test1, UA3_1_2, test"+": "+decider3.check("UA_test1", "", "UA3_1_2", "test"));
		System.out.println("UA_test1, Container1, test"+": "+decider3.check("UA_test1", "", "Container1", "test"));
		System.out.println("UA_test1, Container2, test"+": "+decider3.check("UA_test1", "", "Container2", "test"));
		
		
		System.out.println();		
		System.out.println("PROHIBITION#4(COMPLEMENT=FALSE) UA_test1 test Container1 Container2 (INTERSECTION=false)");
		Prohibitions prohibitions4 = new MemProhibitions();
		Prohibition prohibition4 = new Prohibition.Builder("prohibition1", "UA_test1", new OperationSet("test"))
				.addContainer("Container1", false).addContainer("Container2", false).setIntersection(false).build();
		prohibitions4.add(prohibition4);
		saveDataToFile(ProhibitionsSerializer.toJson(prohibitions4), "Policies/ProhibitionExample/ProhibitionsInitialExample/prohibitions4.json");	

		PReviewDecider decider4 = new PReviewDecider(g, prohibitions4);
		System.out.println("UA_test1, UA1_1, test"+": "+decider4.check("UA_test1", "", "UA1_1", "test"));
		System.out.println("UA_test1, UA2_2, test"+": "+decider4.check("UA_test1", "", "UA2_2", "test"));

		System.out.println("UA_test1, UA3_1_2, test"+": "+decider4.check("UA_test1", "", "UA3_1_2", "test"));
		System.out.println("UA_test1, Container1, test"+": "+decider4.check("UA_test1", "", "Container1", "test"));
		System.out.println("UA_test1, Container2, test"+": "+decider4.check("UA_test1", "", "Container2", "test"));

		
		System.out.println();		
		System.out.println("PROHIBITION#5(COMPLEMENT=TRUE) UA_test1 test Container1 Container2 (INTERSECTION=false)");
		Prohibitions prohibitions5 = new MemProhibitions();
		Prohibition prohibition5 = new Prohibition.Builder("prohibition1", "UA_test1", new OperationSet("test"))
				.addContainer("Container1", true).addContainer("Container2", true).setIntersection(false).build();
		prohibitions5.add(prohibition5);
		saveDataToFile(ProhibitionsSerializer.toJson(prohibitions5), "Policies/ProhibitionExample/ProhibitionsInitialExample/prohibitions5.json");	

		PReviewDecider decider5 = new PReviewDecider(g, prohibitions5);
		System.out.println("UA_test1, UA1_1, test"+": "+decider5.check("UA_test1", "", "UA1_1", "test"));
		System.out.println("UA_test1, UA2_2, test"+": "+decider5.check("UA_test1", "", "UA2_2", "test"));

		System.out.println("UA_test1, UA3_1_2, test"+": "+decider5.check("UA_test1", "", "UA3_1_2", "test"));
		System.out.println("UA_test1, Container1, test"+": "+decider5.check("UA_test1", "", "Container1", "test"));
		System.out.println("UA_test1, Container2, test"+": "+decider5.check("UA_test1", "", "Container2", "test"));
		
		System.out.println();		
		System.out.println("PROHIBITION#6(COMPLEMENT=TRUE) UA_test1 test Container1 Container2 (INTERSECTION=true)");
		Prohibitions prohibitions6 = new MemProhibitions();
		Prohibition prohibition6 = new Prohibition.Builder("prohibition1", "UA_test1", new OperationSet("test"))
				.addContainer("Container1", true).addContainer("Container2", true).setIntersection(true).build();
		prohibitions6.add(prohibition6);
		saveDataToFile(ProhibitionsSerializer.toJson(prohibitions6), "Policies/ProhibitionExample/ProhibitionsInitialExample/prohibitions6.json");	

		PReviewDecider decider6 = new PReviewDecider(g, prohibitions6);
		System.out.println("UA_test1, UA1_1, test"+": "+decider6.check("UA_test1", "", "UA1_1", "test"));
		System.out.println("UA_test1, UA2_2, test"+": "+decider6.check("UA_test1", "", "UA2_2", "test"));

		System.out.println("UA_test1, UA3_1_2, test"+": "+decider6.check("UA_test1", "", "UA3_1_2", "test"));
		System.out.println("UA_test1, Container1, test"+": "+decider6.check("UA_test1", "", "Container1", "test"));
		System.out.println("UA_test1, Container2, test"+": "+decider6.check("UA_test1", "", "Container2", "test"));
		
		System.out.println();		
		System.out.println("PROHIBITION#7(COMPLEMENT CONTAINER1=TRUE) UA_test1 test Container1 Container2 (INTERSECTION=true)");
		Prohibitions prohibitions7 = new MemProhibitions();
		Prohibition prohibition7 = new Prohibition.Builder("prohibition1", "UA_test1", new OperationSet("test"))
				.addContainer("Container1", true).addContainer("Container2", false).setIntersection(true).build();
		prohibitions7.add(prohibition7);
		saveDataToFile(ProhibitionsSerializer.toJson(prohibitions7), "Policies/ProhibitionExample/ProhibitionsInitialExample/prohibitions7.json");	

		PReviewDecider decider7 = new PReviewDecider(g, prohibitions7);
		System.out.println("UA_test1, UA1_1, test"+": "+decider7.check("UA_test1", "", "UA1_1", "test"));
		System.out.println("UA_test1, UA2_2, test"+": "+decider7.check("UA_test1", "", "UA2_2", "test"));

		System.out.println("UA_test1, UA3_1_2, test"+": "+decider7.check("UA_test1", "", "UA3_1_2", "test"));
		System.out.println("UA_test1, Container1, test"+": "+decider7.check("UA_test1", "", "Container1", "test"));
		System.out.println("UA_test1, Container2, test"+": "+decider7.check("UA_test1", "", "Container2", "test"));
		
		System.out.println();		
		System.out.println("PROHIBITION#8(COMPLEMENT CONTAINER2=TRUE) UA_test1 test Container1 Container2 (INTERSECTION=true)");
		Prohibitions prohibitions8 = new MemProhibitions();
		Prohibition prohibition8 = new Prohibition.Builder("prohibition1", "UA_test1", new OperationSet("test"))
				.addContainer("Container1", false).addContainer("Container2", true).setIntersection(true).build();
		prohibitions8.add(prohibition8);
		saveDataToFile(ProhibitionsSerializer.toJson(prohibitions8), "Policies/ProhibitionExample/ProhibitionsInitialExample/prohibitions8.json");	

		PReviewDecider decider8 = new PReviewDecider(g, prohibitions8);
		System.out.println("UA_test1, UA1_1, test"+": "+decider8.check("UA_test1", "", "UA1_1", "test"));
		System.out.println("UA_test1, UA2_2, test"+": "+decider8.check("UA_test1", "", "UA2_2", "test"));

		System.out.println("UA_test1, UA3_1_2, test"+": "+decider8.check("UA_test1", "", "UA3_1_2", "test"));
		System.out.println("UA_test1, Container1, test"+": "+decider8.check("UA_test1", "", "Container1", "test"));
		System.out.println("UA_test1, Container2, test"+": "+decider8.check("UA_test1", "", "Container2", "test"));
		for(Prohibition p :prohibitions8.getAll()) {
        	System.out.println("SUBJECT:" + p.getSubject()+":INTERSECTION: "+p.isIntersection());
	        for (Map.Entry<String,Boolean> entry : p.getContainers().entrySet())  {
	        	System.out.println(entry.getKey()+":"+entry.getValue());		        		        	
	        }			
		}
	}

	
	private static void saveDataToFile(String code, String path) throws IOException {
		File file = new File(path);
		FileWriter myWriter = new FileWriter(file);
		myWriter.write(code);
		myWriter.close();
	}
	
}
