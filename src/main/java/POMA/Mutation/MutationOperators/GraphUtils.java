package POMA.Mutation.MutationOperators;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.opencsv.CSVReader;

import POMA.Exceptions.GraphDoesNotMatchTestSuitException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

public class GraphUtils extends MutantTester {

	public GraphUtils(String testSuit, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException {
		super(testSuit, graph, prohibitions);
	}

	// decide whether a is contained by b or not
	public static boolean isContained(Node nodeA, Node nodeB) throws PMException {
		for (String parent : graph.getParents(nodeA.getName())) {
			if (parent.equals(nodeB.getName())) {
				return true;
			} else {
				if (isContained(graph.getNode(parent), nodeB)) {
					return true;
				}
			}
		}
		return false;
	}

	public static Node getPcOf(Node node) throws PMException {
		Node nodePc;

		for (String pc : graph.getPolicyClasses()) {
			nodePc = graph.getNode(pc);
			if (isContained(node, nodePc)) {
				return nodePc;
			}
		}
		return node;
	}

	public static OperationSet getAllAccessRights() throws PMException, IOException {
		OperationSet ARSet = new OperationSet();
		for (Node SourceNode : UAs) {
			if (graph.getSourceAssociations(SourceNode.getName()) == null) {
				continue;
			}
			Map<String, OperationSet> sources = graph.getSourceAssociations(SourceNode.getName());
			List<String> targetNodes = new ArrayList<String>(sources.keySet());
			for (String targetNode : targetNodes) {
				Set<String> operateSet = sources.get(targetNode);
				OperationSet accessRights = new OperationSet(operateSet);
				ARSet.addAll(accessRights);
			}
		}
		// System.out.println("allAccessRightSet is :" + ARSet);
		return ARSet;
	}

}