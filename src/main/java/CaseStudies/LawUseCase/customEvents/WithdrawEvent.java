package CaseStudies.LawUseCase.customEvents;

import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;

public class WithdrawEvent extends EventContext {

	 public WithdrawEvent(Node target) {
	        super("withdraw", target);
	    } 
	 }
