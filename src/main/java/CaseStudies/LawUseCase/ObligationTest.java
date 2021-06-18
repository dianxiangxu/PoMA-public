package CaseStudies.LawUseCase;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import CaseStudies.LawUseCase.customEvents.AcceptEvent;
import CaseStudies.LawUseCase.customEvents.ApproveEvent;
import CaseStudies.LawUseCase.customEvents.CreateEvent;
import CaseStudies.LawUseCase.customEvents.DisapproveEvent;
import CaseStudies.LawUseCase.customEvents.RefuseEvent;
import CaseStudies.LawUseCase.customEvents.WithdrawEvent;
import POMA.Utils;
import gov.nist.csd.pm.epp.EPPOptions;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.GraphSerializer;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.obligations.MemObligations;
import gov.nist.csd.pm.pip.obligations.evr.EVRParser;
import gov.nist.csd.pm.pip.obligations.model.Obligation;
import gov.nist.csd.pm.pip.prohibitions.MemProhibitions;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.ProhibitionsSerializer;

public class ObligationTest {
	Obligation obligation = new Obligation();

	// Graph
	Graph ngacGraph = new MemGraph();
	Prohibitions prohibitions = new MemProhibitions();

	@Before
	public void setUp() throws Exception {
		File lawfirmpolicyfile = new File("Policies/LawUseCase/LawFirmPolicy.json");
		File casepolicyfile = new File("Policies/LawUseCase/CasePolicy.json");
		//File valuetypepolicyfile = new File("GPMSPolicies/LawUseCase/ValueTypePolicy.json");
		File prohibitionsfile = new File("Policies/LawUseCase/prohibitions.json");

		String lawfirmpolicy = new String(Files.readAllBytes(Paths.get(lawfirmpolicyfile.getAbsolutePath())));
		String casepolicy = new String(Files.readAllBytes(Paths.get(casepolicyfile.getAbsolutePath())));
		//String valuetypepolicy = new String(Files.readAllBytes(Paths.get(valuetypepolicyfile.getAbsolutePath())));
		String prohibitionsJSON = new String(Files.readAllBytes(Paths.get(prohibitionsfile.getAbsolutePath())));

		GraphSerializer.fromJson(ngacGraph, lawfirmpolicy);
		GraphSerializer.fromJson(ngacGraph, casepolicy);
		//GraphSerializer.fromJson(ngacGraph, valuetypepolicy);
		ProhibitionsSerializer.fromJson(prohibitions, prohibitionsJSON);
		File obligationFile = new File("Policies/LawUseCase/Obligations.yml");
		InputStream is = new FileInputStream(obligationFile);
		obligation = EVRParser.parse(is);

	}

	@Test
	public void prohibitionsTest() throws Exception {
		PReviewDecider decider = new PReviewDecider(ngacGraph, prohibitions);
		PDP pdp = getPDP(ngacGraph, prohibitions, obligation);
		assertFalse(ngacGraph.getChildren("Case3").contains("Case3Info"));
		pdp.getEPP().processEvent(new CreateEvent(ngacGraph.getNode("Case3")), "A1", "initialCreate");
		assertTrue(ngacGraph.getChildren("Case3").contains("Case3Info"));
		assertFalse(decider.check("HR1", "", "HR", "hire", "fire"));
		assertFalse(decider.check("LA1", "", "LeadAttorneys", "hire", "fire")); // no prohibition
		assertFalse(decider.check("C1", "", "C-Suit", "hire", "fire")); // no prohibition
		assertFalse(decider.check("I1", "", "GeneralInfo", "addcase", "deletecase"));
		assertFalse(decider.check("I1", "", "Case1", "access"));
		assertFalse(decider.check("C1", "", "Case3Info", "accept", "disapprove"));
		assertFalse(decider.check("LA1", "", "Case3Info", "refuse"));
		PReviewDecider decider2 = new PReviewDecider(ngacGraph);
		assertTrue(decider2.check("HR1", "", "HR", "hire", "fire"));
		assertFalse(decider2.check("LA1", "", "LeadAttorneys", "hire", "fire"));// no prohibition
		assertFalse(decider2.check("C1", "", "C-Suit", "hire", "fire"));// no prohibition
		assertTrue(decider2.check("I1", "", "GeneralInfo", "addcase", "deletecase"));
		assertTrue(decider2.check("I1", "", "Case1", "access"));
		assertTrue(decider2.check("C1", "", "Case3Info", "accept", "disapprove"));
		assertTrue(decider2.check("LA1", "", "Case3Info", "refuse"));
	}

	@Test
	public void caseApprovalAFirst() throws Exception {
		PReviewDecider decider = new PReviewDecider(ngacGraph, prohibitions);
		PDP pdp = getPDP(ngacGraph, prohibitions, obligation);
		assertFalse(ngacGraph.getChildren("Case3").contains("Case3Info"));
		pdp.getEPP().processEvent(new CreateEvent(ngacGraph.getNode("Case3")), "A1", "initialCreate");
		assertTrue(ngacGraph.getChildren("Case3").contains("Case3Info"));
		pdp.getEPP().processEvent(new AcceptEvent(ngacGraph.getNode("Case3Info")), "A1", "initialAccept");
		assertFalse(decider.check("Attorneys", "", "Case3", "accept"));
		assertFalse(decider.check("Attorneys", "", "Case3", "refuse"));
		assertTrue(decider.check("LA1", "", "Case3Info", "accept"));
		assertFalse(decider.check("C-Suit", "", "Case3", "approve"));
		pdp.getEPP().processEvent(new AcceptEvent(ngacGraph.getNode("Case3Info")), "LA1", "finalAccept");
		assertFalse(decider.check("Attorneys", "", "Case3", "accept"));
		assertFalse(decider.check("Attorneys", "", "Case3", "refuse"));
		assertTrue(decider.check("C-Suit", "", "Case3", "approve"));
		pdp.getEPP().processEvent(new ApproveEvent(ngacGraph.getNode("Case3Info")), "C1", "approve");
		assertTrue(ngacGraph.getChildren("GeneralInfo").contains("Case3"));
		assertFalse(decider.check("C-Suit", "", "Case3", "approve"));
	}

	@Test
	public void caseApprovalLAFirst() throws Exception {
		PReviewDecider decider = new PReviewDecider(ngacGraph, prohibitions);
		PDP pdp = getPDP(ngacGraph, prohibitions, obligation);
		assertFalse(ngacGraph.getChildren("Case3").contains("Case3Info"));
		pdp.getEPP().processEvent(new CreateEvent(ngacGraph.getNode("Case3")), "A1", "initialCreate");
		assertTrue(ngacGraph.getChildren("Case3").contains("Case3Info"));
		assertTrue(decider.check("LA1", "", "Case3Info", "accept"));
		assertTrue(ngacGraph.getParents("LA1").contains("LeadAttorneys"));
		pdp.getEPP().processEvent(new AcceptEvent(ngacGraph.getNode("Case3Info")), "LA1", "initialAccept");
		assertFalse(decider.check("LA1", "", "Case3Info", "accept"));
		assertFalse(decider.check("C-Suit", "", "Case3", "approve"));
		pdp.getEPP().processEvent(new AcceptEvent(ngacGraph.getNode("Case3Info")), "A1", "finalAccept");
		assertFalse(decider.check("Attorneys", "", "Case3", "accept"));
		assertFalse(decider.check("Attorneys", "", "Case3", "refuse"));
		assertTrue(decider.check("C-Suit", "", "Case3", "approve"));
		pdp.getEPP().processEvent(new ApproveEvent(ngacGraph.getNode("Case3Info")), "C1", "approve");
		assertTrue(ngacGraph.getChildren("GeneralInfo").contains("Case3"));
		assertFalse(decider.check("C-Suit", "", "Case3", "approve"));

	}

	@Test
	public void caseRefusalAFirst() throws Exception {
		PReviewDecider decider = new PReviewDecider(ngacGraph, prohibitions);
		PDP pdp = getPDP(ngacGraph, prohibitions, obligation);
		assertFalse(ngacGraph.getChildren("Case3").contains("Case3Info"));
		pdp.getEPP().processEvent(new CreateEvent(ngacGraph.getNode("Case3")), "A1", "initialCreate");
		assertTrue(ngacGraph.getChildren("Case3").contains("Case3Info"));
		pdp.getEPP().processEvent(new RefuseEvent(ngacGraph.getNode("Case3Info")), "A1", "initialAccept");
		assertFalse(decider.check("Attorneys", "", "Case3", "accept"));
		assertFalse(decider.check("Attorneys", "", "Case3", "refuse"));
		assertTrue(decider.check("LA1", "", "Case3Info", "accept"));
		assertFalse(decider.check("C-Suit", "", "Case3", "approve"));
		pdp.getEPP().processEvent(new AcceptEvent(ngacGraph.getNode("Case3Info")), "LA1", "finalAccept");
		assertFalse(decider.check("Attorneys", "", "Case3", "accept"));
		assertFalse(decider.check("Attorneys", "", "Case3", "refuse"));
		assertTrue(decider.check("C-Suit", "", "Case3", "approve"));
		pdp.getEPP().processEvent(new ApproveEvent(ngacGraph.getNode("Case3Info")), "C1", "approve");
		assertTrue(ngacGraph.getChildren("GeneralInfo").contains("Case3"));
		assertFalse(decider.check("C-Suit", "", "Case3", "approve"));
	}

	@Test
	public void caseTossed() throws Exception {
		PDP pdp = getPDP(ngacGraph, prohibitions, obligation);
		assertFalse(ngacGraph.getChildren("Case3").contains("Case3Info"));
		pdp.getEPP().processEvent(new CreateEvent(ngacGraph.getNode("Case3")), "A1", "initialCreate");
		assertTrue(ngacGraph.getChildren("Case3").contains("Case3Info"));
		assertTrue(ngacGraph.getChildren("CasePolicy").contains("Case3"));
		pdp.getEPP().processEvent(new WithdrawEvent(ngacGraph.getNode("Case3")), "LA1", "Withdraw");
		assertFalse(ngacGraph.getChildren("CasePolicy").contains("Case3"));
	}

	@Test
	public void caseDisapproved() throws Exception {
		PReviewDecider decider = new PReviewDecider(ngacGraph, prohibitions);
		PDP pdp = getPDP(ngacGraph, prohibitions, obligation);
		assertFalse(ngacGraph.getChildren("Case3").contains("Case3Info"));
		pdp.getEPP().processEvent(new CreateEvent(ngacGraph.getNode("Case3")), "A1", "initialCreate");
		assertTrue(ngacGraph.getChildren("Case3").contains("Case3Info"));
		assertTrue(decider.check("LA1", "", "Case3Info", "accept"));
		assertTrue(ngacGraph.getParents("LA1").contains("LeadAttorneys"));
		pdp.getEPP().processEvent(new AcceptEvent(ngacGraph.getNode("Case3Info")), "A1", "initialAccept");
		assertFalse(decider.check("A1", "", "Case3Info", "accept"));
		assertFalse(decider.check("C-Suit", "", "Case3", "approve"));
		pdp.getEPP().processEvent(new DisapproveEvent(ngacGraph.getNode("Case3")), "LA1", "initialAccept");
		assertTrue(decider.check("LA1", "", "Case3Info", "accept"));
	}

	@Test
	public void COIHandling() throws Exception {
		PReviewDecider decider = new PReviewDecider(ngacGraph, prohibitions);
		PDP pdp = getPDP(ngacGraph, prohibitions, obligation);

		// HAPPY SCENARIO WORKFLOW
		assertFalse(ngacGraph.getChildren("Case3").contains("Case3Info"));
		pdp.getEPP().processEvent(new CreateEvent(ngacGraph.getNode("Case3")), "A1", "initialCreate");
		assertTrue(ngacGraph.getChildren("Case3").contains("Case3Info"));
		pdp.getEPP().processEvent(new AcceptEvent(ngacGraph.getNode("Case3Info")), "A1", "initialAccept");
		assertFalse(decider.check("Attorneys", "", "Case3", "accept"));
		assertFalse(decider.check("Attorneys", "", "Case3", "refuse"));
		assertTrue(decider.check("LA1", "", "Case3Info", "accept"));
		assertFalse(decider.check("C-Suit", "", "Case3", "approve"));
		pdp.getEPP().processEvent(new AcceptEvent(ngacGraph.getNode("Case3Info")), "LA1", "finalAccept");
		assertFalse(decider.check("Attorneys", "", "Case3", "accept"));
		assertFalse(decider.check("Attorneys", "", "Case3", "refuse"));
		assertTrue(decider.check("C-Suit", "", "Case3", "approve"));
		pdp.getEPP().processEvent(new ApproveEvent(ngacGraph.getNode("Case3Info")), "C1", "approve");
		assertTrue(ngacGraph.getChildren("GeneralInfo").contains("Case3"));
		assertFalse(decider.check("C-Suit", "", "Case3", "approve"));

		// COI HANDLING (CAN BE DONE VIA CUSTOM FUNCTIONS)
		assertTrue(COIChecker(ngacGraph, "JD1", "Case3"));//COI true
		ngacGraph.deassign("Alice", "Case3"); //COI Removed
		assertFalse(COIChecker(ngacGraph, "JD1", "Case3"));//COI false
	}

	// (CAN BE DONE VIA CUSTOM FUNCTIONS)
	static boolean COIChecker(Graph graph, String attorneyName, String newCase) {
		Map<String, OperationSet> sourceAssociations = null;
		try {
			sourceAssociations = graph.getSourceAssociations(attorneyName);

			//System.out.println(sourceAssociations);
			for (String caseName : sourceAssociations.keySet()) {
				if (!Collections.disjoint(graph.getChildren(caseName), graph.getChildren(newCase))) {
					return true;
				}
			}
		} catch (PMException e) {
			e.printStackTrace();
		}
		return false;
	}

	static PDP getPDP(Graph graph, Prohibitions prohibitions, Obligation obligation) throws Exception {
		EPPOptions eppOptions = new EPPOptions();

		PDP pdp = new PDP(new PAP(graph, prohibitions, new MemObligations()), eppOptions);
		if (graph.exists("super_pc_rep")) {
			graph.deleteNode("super_pc_rep");
		}
		pdp.getPAP().getObligationsPAP().add(obligation, true);
		return pdp;
	}
}