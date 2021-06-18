package CaseStudies.ProhibitionsUseCase;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.UA;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.OA;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

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

public class MedicalExampleOA {
	public static void main(String[] argv) throws PMException, IOException {
		Graph g = new MemGraph(); 	  
  
		g.createPolicyClass("ClassificationPolicyClass", null);

		g.createNode("MaximumCarePatients", OA, null, "ClassificationPolicyClass");
		g.createNode("ModerateCarePatients", OA, null, "ClassificationPolicyClass");
		g.createNode("Patient1", OA, null, "MaximumCarePatients");
		g.createNode("Patient2", OA, null, "ModerateCarePatients");
		g.createNode("Patient3", OA, null, "MaximumCarePatients", "ModerateCarePatients");

		g.createNode("RegisteredNurse", UA, null, "ClassificationPolicyClass");
		g.createNode("Doctor", UA, null, "ClassificationPolicyClass");
		g.createNode("LicensedPracticalNurse", UA, null, "ClassificationPolicyClass");
		g.createNode("CertifiedNurseAssistant", UA, null, "ClassificationPolicyClass");

		g.associate("RegisteredNurse", "MaximumCarePatients", new OperationSet("access"));
		g.associate("RegisteredNurse", "ModerateCarePatients", new OperationSet("access"));

		g.associate("Doctor", "MaximumCarePatients", new OperationSet("write", "access"));
		g.associate("Doctor", "ModerateCarePatients", new OperationSet("write", "access"));

		g.associate("LicensedPracticalNurse", "MaximumCarePatients", new OperationSet("access"));
		g.associate("LicensedPracticalNurse", "ModerateCarePatients", new OperationSet("access"));

		g.associate("CertifiedNurseAssistant", "MaximumCarePatients", new OperationSet("access"));
		g.associate("CertifiedNurseAssistant", "ModerateCarePatients", new OperationSet("access"));

		
		saveDataToFile(GraphSerializer.toJson(g), "Policies/ProhibitionExample/ProhibitionsMedicalExampleOA/graph.json");	
		System.out.println("PROHIBITION#1(COMPLEMENT=FALSE) RegisteredNurse access MaximumCarePatients ");
		Prohibitions prohibitions1 = new MemProhibitions();
		Prohibition prohibition = new Prohibition.Builder("prohibition1", "RegisteredNurse", new OperationSet("access"))
				.addContainer("MaximumCarePatients", false).build();
		
		prohibitions1.add(prohibition);
		saveDataToFile(ProhibitionsSerializer.toJson(prohibitions1), "Policies/ProhibitionExample/ProhibitionsMedicalExampleOA/prohibitionsx1.json");	

		PReviewDecider decider = new PReviewDecider(g, prohibitions1);
		System.out.println("RegisteredNurse, Patient1, access"+": "+decider.check("RegisteredNurse", "", "Patient1", "access"));
		System.out.println("RegisteredNurse, Patient2, access"+": "+decider.check("RegisteredNurse", "", "Patient2", "access"));

		System.out.println("RegisteredNurse, Patient3, access"+": "+decider.check("RegisteredNurse", "", "Patient3", "access"));
		System.out.println("RegisteredNurse, MaximumCarePatients, access"+": "+decider.check("RegisteredNurse", "", "MaximumCarePatients", "access"));
		System.out.println("RegisteredNurse, ModerateCarePatients, access"+": "+decider.check("RegisteredNurse", "", "ModerateCarePatients", "access"));
		
		System.out.println();		
		System.out.println("PROHIBITION#2(COMPLEMENT=TRUE) RegisteredNurse access MaximumCarePatients ");
		Prohibitions prohibitions2 = new MemProhibitions();
		Prohibition prohibition2 = new Prohibition.Builder("prohibition1", "RegisteredNurse", new OperationSet("access"))
				.addContainer("MaximumCarePatients", true).build();
		prohibitions2.add(prohibition2);
		saveDataToFile(ProhibitionsSerializer.toJson(prohibitions2), "Policies/ProhibitionExample/ProhibitionsMedicalExampleOA/prohibitions2.json");	

		PReviewDecider decider2 = new PReviewDecider(g, prohibitions2);
		System.out.println("RegisteredNurse, Patient1, access"+": "+decider2.check("RegisteredNurse", "", "Patient1", "access"));
		System.out.println("RegisteredNurse, Patient2, access"+": "+decider2.check("RegisteredNurse", "", "Patient2", "access"));
		System.out.println("RegisteredNurse, Patient3, access"+": "+decider2.check("RegisteredNurse", "", "Patient3", "access"));
		System.out.println("RegisteredNurse, MaximumCarePatients, access"+": "+decider2.check("RegisteredNurse", "", "MaximumCarePatients", "access"));
		System.out.println("RegisteredNurse, ModerateCarePatients, access"+": "+decider2.check("RegisteredNurse", "", "ModerateCarePatients", "access"));
		
		
		
		
		System.out.println();		
		System.out.println("PROHIBITION#3(COMPLEMENT=FALSE) RegisteredNurse access MaximumCarePatients ModerateCarePatients (INTERSECTION=TRUE)");
		Prohibitions prohibitions3 = new MemProhibitions();
		Prohibition prohibition3 = new Prohibition.Builder("prohibition1", "RegisteredNurse", new OperationSet("access"))
				.addContainer("MaximumCarePatients", false).addContainer("ModerateCarePatients", false).setIntersection(true).build();
		prohibitions3.add(prohibition3);
		saveDataToFile(ProhibitionsSerializer.toJson(prohibitions3), "Policies/ProhibitionExample/ProhibitionsMedicalExampleOA/prohibitions3.json");	

		PReviewDecider decider3 = new PReviewDecider(g, prohibitions3);
		System.out.println("RegisteredNurse, Patient1, access"+": "+decider3.check("RegisteredNurse", "", "Patient1", "access"));
		System.out.println("RegisteredNurse, Patient2, access"+": "+decider3.check("RegisteredNurse", "", "Patient2", "access"));

		System.out.println("RegisteredNurse, Patient3, access"+": "+decider3.check("RegisteredNurse", "", "Patient3", "access"));
		System.out.println("RegisteredNurse, MaximumCarePatients, access"+": "+decider3.check("RegisteredNurse", "", "MaximumCarePatients", "access"));
		System.out.println("RegisteredNurse, ModerateCarePatients, access"+": "+decider3.check("RegisteredNurse", "", "ModerateCarePatients", "access"));
		
		
		System.out.println();		
		System.out.println("PROHIBITION#4(COMPLEMENT=FALSE) RegisteredNurse access MaximumCarePatients ModerateCarePatients (INTERSECTION=false)");
		Prohibitions prohibitions4 = new MemProhibitions();
		Prohibition prohibition4 = new Prohibition.Builder("prohibition1", "RegisteredNurse", new OperationSet("access"))
				.addContainer("MaximumCarePatients", false).addContainer("ModerateCarePatients", false).setIntersection(false).build();
		prohibitions4.add(prohibition4);
		saveDataToFile(ProhibitionsSerializer.toJson(prohibitions4), "Policies/ProhibitionExample/ProhibitionsMedicalExampleOA/prohibitions4.json");	

		PReviewDecider decider4 = new PReviewDecider(g, prohibitions4);
		System.out.println("RegisteredNurse, Patient1, access"+": "+decider4.check("RegisteredNurse", "", "Patient1", "access"));
		System.out.println("RegisteredNurse, Patient2, access"+": "+decider4.check("RegisteredNurse", "", "Patient2", "access"));

		System.out.println("RegisteredNurse, Patient3, access"+": "+decider4.check("RegisteredNurse", "", "Patient3", "access"));
		System.out.println("RegisteredNurse, MaximumCarePatients, access"+": "+decider4.check("RegisteredNurse", "", "MaximumCarePatients", "access"));
		System.out.println("RegisteredNurse, ModerateCarePatients, access"+": "+decider4.check("RegisteredNurse", "", "ModerateCarePatients", "access"));

		
		System.out.println();		
		System.out.println("PROHIBITION#5(COMPLEMENT=TRUE) RegisteredNurse access MaximumCarePatients ModerateCarePatients (INTERSECTION=false)");
		Prohibitions prohibitions5 = new MemProhibitions();
		Prohibition prohibition5 = new Prohibition.Builder("prohibition1", "RegisteredNurse", new OperationSet("access"))
				.addContainer("MaximumCarePatients", true).addContainer("ModerateCarePatients", true).setIntersection(false).build();
		prohibitions5.add(prohibition5);
		saveDataToFile(ProhibitionsSerializer.toJson(prohibitions5), "Policies/ProhibitionExample/ProhibitionsMedicalExampleOA/prohibitions5.json");	

		PReviewDecider decider5 = new PReviewDecider(g, prohibitions5);
		System.out.println("RegisteredNurse, Patient1, access"+": "+decider5.check("RegisteredNurse", "", "Patient1", "access"));
		System.out.println("RegisteredNurse, Patient2, access"+": "+decider5.check("RegisteredNurse", "", "Patient2", "access"));

		System.out.println("RegisteredNurse, Patient3, access"+": "+decider5.check("RegisteredNurse", "", "Patient3", "access"));
		System.out.println("RegisteredNurse, MaximumCarePatients, access"+": "+decider5.check("RegisteredNurse", "", "MaximumCarePatients", "access"));
		System.out.println("RegisteredNurse, ModerateCarePatients, access"+": "+decider5.check("RegisteredNurse", "", "ModerateCarePatients", "access"));
		
		System.out.println();		
		System.out.println("PROHIBITION#6(COMPLEMENT=TRUE) RegisteredNurse access MaximumCarePatients ModerateCarePatients (INTERSECTION=true)");
		Prohibitions prohibitions6 = new MemProhibitions();
		Prohibition prohibition6 = new Prohibition.Builder("prohibition1", "RegisteredNurse", new OperationSet("access"))
				.addContainer("MaximumCarePatients", true).addContainer("ModerateCarePatients", true).setIntersection(true).build();
		prohibitions6.add(prohibition6);
		saveDataToFile(ProhibitionsSerializer.toJson(prohibitions6), "Policies/ProhibitionExample/ProhibitionsMedicalExampleOA/prohibitions6.json");	

		PReviewDecider decider6 = new PReviewDecider(g, prohibitions6);
		System.out.println("RegisteredNurse, Patient1, access"+": "+decider6.check("RegisteredNurse", "", "Patient1", "access"));
		System.out.println("RegisteredNurse, Patient2, access"+": "+decider6.check("RegisteredNurse", "", "Patient2", "access"));

		System.out.println("RegisteredNurse, Patient3, access"+": "+decider6.check("RegisteredNurse", "", "Patient3", "access"));
		System.out.println("RegisteredNurse, MaximumCarePatients, access"+": "+decider6.check("RegisteredNurse", "", "MaximumCarePatients", "access"));
		System.out.println("RegisteredNurse, ModerateCarePatients, access"+": "+decider6.check("RegisteredNurse", "", "ModerateCarePatients", "access"));
		
		System.out.println();		
		System.out.println("PROHIBITION#7(COMPLEMENT MaximumCarePatients=TRUE) RegisteredNurse access MaximumCarePatients ModerateCarePatients (INTERSECTION=true)");
		Prohibitions prohibitions7 = new MemProhibitions();
		Prohibition prohibition7 = new Prohibition.Builder("prohibition1", "RegisteredNurse", new OperationSet("access"))
				.addContainer("MaximumCarePatients", true).addContainer("ModerateCarePatients", false).setIntersection(true).build();
		prohibitions7.add(prohibition7);
		saveDataToFile(ProhibitionsSerializer.toJson(prohibitions7), "Policies/ProhibitionExample/ProhibitionsMedicalExampleOA/prohibitions7.json");	

		PReviewDecider decider7 = new PReviewDecider(g, prohibitions7);
		System.out.println("RegisteredNurse, Patient1, access"+": "+decider7.check("RegisteredNurse", "", "Patient1", "access"));
		System.out.println("RegisteredNurse, Patient2, access"+": "+decider7.check("RegisteredNurse", "", "Patient2", "access"));

		System.out.println("RegisteredNurse, Patient3, access"+": "+decider7.check("RegisteredNurse", "", "Patient3", "access"));
		System.out.println("RegisteredNurse, MaximumCarePatients, access"+": "+decider7.check("RegisteredNurse", "", "MaximumCarePatients", "access"));
		System.out.println("RegisteredNurse, ModerateCarePatients, access"+": "+decider7.check("RegisteredNurse", "", "ModerateCarePatients", "access"));
		
		System.out.println();		
		System.out.println("PROHIBITION#8(COMPLEMENT ModerateCarePatients=TRUE) RegisteredNurse access MaximumCarePatients ModerateCarePatients (INTERSECTION=true)");
		Prohibitions prohibitions8 = new MemProhibitions();
		Prohibition prohibition8 = new Prohibition.Builder("prohibition1", "RegisteredNurse", new OperationSet("access"))
				.addContainer("MaximumCarePatients", false).addContainer("ModerateCarePatients", true).setIntersection(true).build();
		prohibitions8.add(prohibition8);
		saveDataToFile(ProhibitionsSerializer.toJson(prohibitions8), "Policies/ProhibitionExample/ProhibitionsMedicalExampleOA/prohibitions8.json");	

		PReviewDecider decider8 = new PReviewDecider(g, prohibitions8);
		System.out.println("RegisteredNurse, Patient1, access"+": "+decider8.check("RegisteredNurse", "", "Patient1", "access"));
		System.out.println("RegisteredNurse, Patient2, access"+": "+decider8.check("RegisteredNurse", "", "Patient2", "access"));

		System.out.println("RegisteredNurse, Patient3, access"+": "+decider8.check("RegisteredNurse", "", "Patient3", "access"));
		System.out.println("RegisteredNurse, MaximumCarePatients, access"+": "+decider8.check("RegisteredNurse", "", "MaximumCarePatients", "access"));
		System.out.println("RegisteredNurse, ModerateCarePatients, access"+": "+decider8.check("RegisteredNurse", "", "ModerateCarePatients", "access"));
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
