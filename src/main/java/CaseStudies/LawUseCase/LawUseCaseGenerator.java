package CaseStudies.LawUseCase;


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
import static org.junit.Assert.assertTrue;

import java.io.File;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.util.Map;

	public class LawUseCaseGenerator {

		public static void main(String[] argv) throws PMException, IOException {
			Graph lawfirmpolicy = new MemGraph(); 	  
	  
			lawfirmpolicy.createPolicyClass("LawFirmPolicy", null);

			lawfirmpolicy.createNode("Office1", UA, null, "LawFirmPolicy");
			lawfirmpolicy.createNode("MainOffice", UA, null, "Office1");
			lawfirmpolicy.createNode("HR", UA, null, "MainOffice");
			lawfirmpolicy.createNode("C-Suit", UA, null, "MainOffice");
			lawfirmpolicy.createNode("LeadAttorneys", UA, null, "MainOffice");
			lawfirmpolicy.createNode("Attorneys", UA, null, "Office1");
			lawfirmpolicy.createNode("JD1", UA, null, "Attorneys");
			lawfirmpolicy.createNode("JD2", UA, null, "Attorneys");
			lawfirmpolicy.createNode("Interns", UA, null, "Attorneys");
			lawfirmpolicy.createNode("I1", UA, null, "Interns");

			lawfirmpolicy.createNode("Cases", OA, null, "LawFirmPolicy");
			lawfirmpolicy.createNode("GeneralInfo", OA, null, "Cases");
			lawfirmpolicy.createNode("Case1", OA, null, "GeneralInfo");
			lawfirmpolicy.createNode("Case2", OA, null, "GeneralInfo");
			lawfirmpolicy.createNode("Bob", O, null, "Case1");
			lawfirmpolicy.createNode("State", O, null, "Case1");
			lawfirmpolicy.createNode("Alice", O, null, "Case2");
			lawfirmpolicy.createNode("Mike", O, null, "Case2");
			lawfirmpolicy.createNode("HR1", U, null, "HR");			
			
			lawfirmpolicy.associate("MainOffice", "Office1", new OperationSet("hire", "fire"));
			lawfirmpolicy.associate("Office1", "Cases", new OperationSet("access"));
			lawfirmpolicy.associate("Office1", "GeneralInfo", new OperationSet("addcase","deletecase"));
			
			lawfirmpolicy.associate("JD1", "Case1", new OperationSet("appointed"));
			lawfirmpolicy.associate("JD1", "Case2", new OperationSet("appointed"));
			//lawfirmpolicy.associate("JD1", "Case2", new OperationSet("appointed"));

			saveDataToFile(GraphSerializer.toJson(lawfirmpolicy), "GPMSPolicies/LawUseCase/LawFirmPolicy.json");	
		//	System.out.println("PROHIBITION#1(COMPLEMENT=FALSE) UA_test1 test Container1 ");
		//	Prohibitions prohibitions1 = new MemProhibitions();
		//	Prohibition prohibition = new Prohibition.Builder("prohibition1", "UA_test1", new OperationSet("test"))
		//			.addContainer("Container1", false).build();

			Graph casePolicy = new MemGraph(); 	  
			  
			casePolicy.createPolicyClass("CasePolicy", null);

			casePolicy.createNode("Attorneys", UA, null, "CasePolicy");
			casePolicy.createNode("LeadAttorneys", UA, null, "Attorneys");
			casePolicy.createNode("C-Suit", UA, null, "LeadAttorneys");
			casePolicy.createNode("LA1", U, null, "LeadAttorneys");
			casePolicy.createNode("A1", U, null, "Attorneys");
			casePolicy.createNode("C1", U, null, "C-Suit");

			casePolicy.createNode("Case3", OA, null, "CasePolicy");
			casePolicy.createNode("Apple", O, null, "Case3");
			casePolicy.createNode("Google", O, null, "Case3");
			casePolicy.createNode("Alice", O, null, "Case3");

			casePolicy.associate("Attorneys", "Case3", new OperationSet("accept", "refuse"));
			casePolicy.associate("LeadAttorneys", "Case3", new OperationSet("disapprove", "withdraw"));
			saveDataToFile(GraphSerializer.toJson(casePolicy), "GPMSPolicies/LawUseCase/CasePolicy.json");	

			Graph valueTypePolicy = new MemGraph(); 	  
			valueTypePolicy.createPolicyClass("ValueTypePolicy", null);

			valueTypePolicy.createNode("Type", OA, null, "ValueTypePolicy");
			valueTypePolicy.createNode("Subtype", OA, null, "ValueTypePolicy");
			valueTypePolicy.createNode("Value", OA, null, "ValueTypePolicy");
			valueTypePolicy.createNode("Criminal", OA, null, "Type");
			valueTypePolicy.createNode("Civil", OA, null, "Type");
			valueTypePolicy.createNode("Felony", OA, null, "Subtype");
			valueTypePolicy.createNode("Misdemeanor", OA, null, "Subtype");
			valueTypePolicy.createNode("lessequal5000", OA, null, "Value");
			valueTypePolicy.createNode("more5000", OA, null, "Value");
			saveDataToFile(GraphSerializer.toJson(valueTypePolicy), "GPMSPolicies/LawUseCase/ValueTypePolicy.json");	


			
			
			Prohibitions prohibitions = new MemProhibitions();
			Prohibition prohibition1 = new Prohibition.Builder("prohibition1", "Interns", new OperationSet("access"))
					.addContainer("GeneralInfo", false).build();
			Prohibition prohibition2 = new Prohibition.Builder("prohibition2", "Interns", new OperationSet("addcase","deletecase"))
					.addContainer("Cases", false).build();
			Prohibition prohibition3 = new Prohibition.Builder("prohibition3", "HR", new OperationSet("hire", "fire"))
					.addContainer("MainOffice", false).build();
			Prohibition prohibition4 = new Prohibition.Builder("prohibition4", "C-Suit", new OperationSet("accept", "disapprove"))
					.addContainer("Case3", false).build();
			Prohibition prohibition5 = new Prohibition.Builder("prohibition5", "LeadAttorneys", new OperationSet("refuse"))
					.addContainer("Case3", false).build();
			prohibitions.add(prohibition1);
			prohibitions.add(prohibition2);
			prohibitions.add(prohibition3);
			prohibitions.add(prohibition4);
			prohibitions.add(prohibition5);


			saveDataToFile(ProhibitionsSerializer.toJson(prohibitions), "GPMSPolicies/LawUseCase/prohibitions.json");	
		}

		
		private static void saveDataToFile(String code, String path) throws IOException {
			File file = new File(path);
			FileWriter myWriter = new FileWriter(file);
			myWriter.write(code);
			myWriter.close();
		}
		
	}


