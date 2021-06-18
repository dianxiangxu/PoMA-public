package POMA.Mutation.MutationAnalysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;

import POMA.Mutation.MutationOperators.MutatorAAC;
import POMA.Mutation.MutationOperators.MutatorAAG;
import POMA.Mutation.MutationOperators.MutatorAARA;
import POMA.Mutation.MutationOperators.MutatorCAA;
import POMA.Mutation.MutationOperators.MutatorCAD;
import POMA.Mutation.MutationOperators.MutatorCOAA;
import POMA.Mutation.MutationOperators.MutatorCUAA;
import POMA.Mutation.MutationOperators.MutatorRAC;
import POMA.Mutation.MutationOperators.MutatorRAD;
import POMA.Mutation.MutationOperators.MutatorRAG;
import POMA.Mutation.MutationOperators.MutatorRARA;
import POMA.Mutation.MutationOperators.MutatorRARAA;
import POMA.Utils;
import POMA.Exceptions.GraphDoesNotMatchTestSuitException;
import POMA.Mutation.MutationOperators.*;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.*;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

public class MutationController {
	List<String> testMethods;
	int totalNumberOfMutants = 0;
	int totalNumberOfKilledMutants = 0;
	int totalNumberOfMutantsForTest = 0;
	int totalNumberOfKilledMutantsForTest = 0;
	List<String[]> data = new ArrayList<String[]>();
	String[] row;
	String CSVFilePath = "CSV/OverallMutationResults.csv";
	static int colCount = 15;
	Graph graph;
	public Prohibitions prohibitions;

	
	private void createHeaderForCSV(List<String> mutantNames) {
		String[] header = new String[colCount];
		header[0]= "TestMethod";
		for(int i=0;i< mutantNames.size(); i++) {
			header[i+1] =  mutantNames.get(i);
		}
		header[mutantNames.size()+1] = "totalMutationScore";
		data.add(header);
	}
	
	public static void main(String[] args) throws PMException, IOException, GraphDoesNotMatchTestSuitException {
		MutationController mc = new MutationController();
		List<String> mutantNames = new ArrayList<String>();
//        String initialGraphConfig = "Policies/SimpleGraph/simpleGraphToSMT.json";
//        String initialGraphConfig = "Policies/GPMS/Graph.json";
//        String initialGraphConfig = "Policies/LawUseCase/Graph.json";
//		String initialProhibitionConfig = "Policies/LawUseCase/prohibitions.json";
//        String initialGraphConfig = "Policies/BankPolicy/Complex/bank_policy_config.json";
        String initialGraphConfig = "Policies/ProhibitionExample/ProhibitionsMedicalExampleOA/graph.json";
        String initialProhibitionConfig = "Policies/ProhibitionExample/ProhibitionsMedicalExampleOA/prohibitionsx1.json";

        
        
		File folder = new File(initialGraphConfig).getParentFile();
		mutantNames.add("RAD");
		mutantNames.add("CAD");
		mutantNames.add("CAA");
		mutantNames.add("RAG");
		mutantNames.add("AAG");
		mutantNames.add("CUAA");
		mutantNames.add("COAA");
		mutantNames.add("RARA");
		mutantNames.add("AARA");
		mutantNames.add("RAC");
		mutantNames.add("AAC");
		mutantNames.add("RARAA");
		mc.graph = Utils.readAnyGraph(initialGraphConfig);// .readGPMSGraph();
		if (initialProhibitionConfig != "")
		    mc.prohibitions = Utils.readProhibitions(initialProhibitionConfig);
		mc.createMutants( mutantNames,  mc.graph, mc.prohibitions, folder);
	}

	public MutationController() {
		createTestMethods();
	}

	public void createMutants(List<String> mutantNames, Graph graph, Prohibitions prohibitions, File folder) throws GraphDoesNotMatchTestSuitException {
		
		//createHeaderForCSV(mutantNames);
		List<String[]> outputList = new ArrayList<String[]>();
		
		
		String[] row1 = new String[]{"TestMethod", "Pairwise", "AllCombination"};
		data.add(row1);
		String[] row2 = new String[colCount];
		String[] row3 = new String[colCount];
		String[] row4 = new String[colCount];
		String[] row5 = new String[colCount];
		String[] row6 = new String[colCount];
		String[] row7 = new String[colCount];
		String[] row8 = new String[colCount];
		String[] row9 = new String[colCount];
		String[] row10 = new String[colCount];
		String[] row11 = new String[colCount];
		String[] row12 = new String[colCount];
		String[] row13 = new String[colCount];
		String[] row14 = new String[colCount];
		row14[0] = "totalMutationScore";

		long startTime = System.currentTimeMillis();
		int j = 0;

		for (String testMethod : testMethods) {
			j++;
			String[] row = new String[colCount];
			totalNumberOfMutantsForTest = 0;
			totalNumberOfKilledMutantsForTest = 0;
			row[0] = testMethod;
			


			for (int i = 0; i < mutantNames.size(); i++) {
				System.out.println(mutantNames.toString());
				double result = 0;
				if (mutantNames.get(i).equals("RAD")) {
					row2[0] = "RAD";
//					row2[j] = Double.toString(testRAD(testMethod, graph, prohibitions));
				} else if (mutantNames.get(i).equals("CAD")) {
					row3[0] = "CAD";
					row3[j] = Double.toString(testCAD(testMethod, graph, prohibitions));
				} else if (mutantNames.get(i).equals("CAA")) {
					row4[0] = "CAA";
					row4[j] = Double.toString(testCAA(testMethod, graph, prohibitions));
				} else if (mutantNames.get(i).equals("RAG")) {
					row5[0] = "RAG";
					row5[j] = Double.toString(testRAG(testMethod, graph, prohibitions));
				} else if (mutantNames.get(i).equals("AAG")) {
					row6[0] = "AAG";
					row6[j] = Double.toString(testAAG(testMethod, graph, prohibitions));
				//	System.out.println("HELLO");
				} else if (mutantNames.get(i).equals("CUAA")) {
					row7[0] = "CUAA";
					row7[j] = Double.toString(testCUAA(testMethod, graph, prohibitions));
				} else if (mutantNames.get(i).equals("COAA")) {
					row8[0] = "COAA";
					row8[j] = Double.toString(testCOAA(testMethod, graph, prohibitions));
				} else if (mutantNames.get(i).equals("RARA")) {
					row9[0] = "RARA";
					row9[j] = Double.toString(testRARA(testMethod, graph, prohibitions));
				} else if (mutantNames.get(i).equals("AARA")) {
					row10[0] = "AARA";
					row10[j] = Double.toString(testAARA(testMethod, graph, prohibitions));
				} else if (mutantNames.get(i).equals("RAC")) {
					row11[0] = "RAC";
					row11[j] = Double.toString(testRAC(testMethod, graph, prohibitions));
				} else if (mutantNames.get(i).equals("AAC")) {
					row12[j] = Double.toString(testAAC(testMethod, graph, prohibitions));
					row12[0] = "AAC";
				} else if (mutantNames.get(i).equals("RARAA")) {
					row13[0] = "RARAA";
					row13[j] = Double.toString(testRARAA(testMethod, graph, prohibitions));
				}				
			}
			row14[j] = Double.toString(
					(double) totalNumberOfKilledMutantsForTest / (double) totalNumberOfMutantsForTest * 100);
		
		}
		data.add(row2);
		data.add(row3);
		data.add(row4);
		data.add(row5);
		data.add(row6);
		data.add(row7);
		data.add(row8);
		data.add(row9);
		data.add(row10);
		data.add(row11);
		data.add(row12);
		data.add(row13);
		data.add(row14);


		try {
			saveCSV(data, folder);
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();
		long timeElapsed = endTime - startTime;
		long minutes = (timeElapsed / 1000) / 60;
		int seconds = (int) ((timeElapsed / 1000) % 60);
		System.out.println("Execution time in milliseconds: " + timeElapsed);
		System.out.println("Execution time in min and sec: " + minutes + ":" + seconds);
	}

	private double testRAD(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException {
		MutatorRAD mutatorRAD = new MutatorRAD(testMethod, graph, prohibitions);
		System.out.println("MutationMethod is RAD");

		try {
			mutatorRAD.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorRAD.calculateMutationScore(mutatorRAD.getNumberOfMutants(),
				mutatorRAD.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorRAD.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorRAD.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorRAD.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorRAD.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testCAD(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException {
		MutatorCAD mutatorCAD = new MutatorCAD(testMethod, graph, prohibitions);
		System.out.println("MutationMethod is CAD");

		try {
			mutatorCAD.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorCAD.calculateMutationScore(mutatorCAD.getNumberOfMutants(),
				mutatorCAD.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorCAD.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorCAD.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorCAD.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorCAD.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testCAA(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException {
		MutatorCAA mutatorCAA = new MutatorCAA(testMethod, graph, prohibitions);
		System.out.println("MutationMethod is CAA");

		try {
			mutatorCAA.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorCAA.calculateMutationScore(mutatorCAA.getNumberOfMutants(),
				mutatorCAA.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorCAA.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorCAA.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorCAA.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorCAA.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testRAG(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException {
		MutatorRAG mutatorRAG = new MutatorRAG(testMethod, graph, prohibitions);
		System.out.println("MutationMethod is RAG");

		try {
			mutatorRAG.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorRAG.calculateMutationScore(mutatorRAG.getNumberOfMutants(),
				mutatorRAG.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorRAG.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorRAG.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorRAG.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorRAG.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testAAG(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException {
		MutatorAAG mutatorAAG = new MutatorAAG(testMethod, graph, prohibitions);
		System.out.println("MutationMethod is AAG");

		try {
			mutatorAAG.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorAAG.calculateMutationScore(mutatorAAG.getNumberOfMutants(),
				mutatorAAG.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorAAG.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorAAG.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorAAG.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorAAG.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testCUAA(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException {
		MutatorCUAA mutatorCUAA = new MutatorCUAA(testMethod, graph, prohibitions);
		System.out.println("MutationMethod is CUAA");

		try {
			mutatorCUAA.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorCUAA.calculateMutationScore(mutatorCUAA.getNumberOfMutants(),
				mutatorCUAA.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorCUAA.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorCUAA.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorCUAA.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorCUAA.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testCOAA(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException {
		MutatorCOAA mutatorCOAA = new MutatorCOAA(testMethod, graph, prohibitions);
		System.out.println("MutationMethod is COAA");

		try {
			mutatorCOAA.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorCOAA.calculateMutationScore(mutatorCOAA.getNumberOfMutants(),
				mutatorCOAA.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorCOAA.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorCOAA.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorCOAA.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorCOAA.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testRARA(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException {
		MutatorRARA mutatorRARA = new MutatorRARA(testMethod, graph, prohibitions);
		System.out.println("MutationMethod is RARA" + mutatorRARA.getMutationMethod());

		try {
			mutatorRARA.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorRARA.calculateMutationScore(mutatorRARA.getNumberOfMutants(),
				mutatorRARA.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorRARA.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorRARA.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorRARA.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorRARA.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testAARA(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException {
		MutatorAARA mutatorAARA = new MutatorAARA(testMethod, graph, prohibitions);
		System.out.println("MutationMethod is AARA");

		try {
			mutatorAARA.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorAARA.calculateMutationScore(mutatorAARA.getNumberOfMutants(),
				mutatorAARA.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorAARA.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorAARA.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorAARA.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorAARA.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testRAC(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException {
		MutatorRAC mutatorRAC = new MutatorRAC(testMethod, graph, prohibitions);
		System.out.println("MutationMethod is RAC");

		try {
			mutatorRAC.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorRAC.calculateMutationScore(mutatorRAC.getNumberOfMutants(),
				mutatorRAC.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorRAC.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorRAC.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorRAC.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorRAC.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testAAC(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException {
		MutatorAAC mutatorAAC = new MutatorAAC(testMethod, graph, prohibitions);
		System.out.println("MutationMethod is AAC");

		try {
			mutatorAAC.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorAAC.calculateMutationScore(mutatorAAC.getNumberOfMutants(),
				mutatorAAC.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorAAC.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorAAC.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorAAC.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorAAC.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testRARAA(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException {
		MutatorRARAA mutatorRARAA = new MutatorRARAA(testMethod, graph, prohibitions);
		System.out.println("MutationMethod is RARAA");

		try {
			mutatorRARAA.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorRARAA.calculateMutationScore(mutatorRARAA.getNumberOfMutants(),
				mutatorRARAA.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorRARAA.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorRARAA.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorRARAA.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorRARAA.getNumberOfKilledMutants();
		return mutationScore;
	}

	private void createTestMethods() {
		testMethods = new ArrayList<String>();
//		testMethods.add("RS");
//		testMethods.add("R");
		testMethods.add("Pairwise");
		testMethods.add("AllCombinations");

	}

	private void createHeaderForCSV() {
		String[] header = new String[colCount];


		header[0] = "TestMethod";
		header[1] = "RAD";
		header[2] = "CAD";
		header[3] = "CAA";
		header[4] = "RAG";
		header[5] = "AAG";
		header[6] = "CUAA";
		header[7] = "COAA";
		header[8] = "RARA";
		header[9] = "AARA";
		header[10] = "RAC";
		header[11] = "AAC";
		header[12] = "RARAA";
		header[13] = "totalMutationScore";
		data.add(header);
	}

	

	public void saveCSV(List<String[]> data, File directoryForTestResults) throws PMException, IOException {

		if (directoryForTestResults.createNewFile()) {
			System.out.println("File has been created.");
		} else {
			System.out.println("File already exists.");
		}
		BufferedWriter writer = null;
		writer = new BufferedWriter(new FileWriter(directoryForTestResults+"/CSV/OverallMutationResults.csv"));
		CSVWriter CSVwriter = new CSVWriter(writer);
		CSVwriter.writeAll(data);
		writer.flush();
		CSVwriter.close();

		if (writer != null)
			writer.close();
	}
}
