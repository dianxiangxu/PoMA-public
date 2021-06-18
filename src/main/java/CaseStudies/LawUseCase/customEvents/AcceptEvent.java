package CaseStudies.LawUseCase.customEvents;

import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;

public class AcceptEvent extends EventContext {

	 public AcceptEvent(Node target) {
	        super("accept", target);
	    } 
	 }
