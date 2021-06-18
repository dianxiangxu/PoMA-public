package CaseStudies.LawUseCase.customEvents;

import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;

public class RefuseEvent extends EventContext {

	 public RefuseEvent(Node target) {
	        super("refuse", target);
	    } 
	 }
