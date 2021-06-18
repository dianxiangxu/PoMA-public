package POMA.Mutation.MutationAnalysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;

import POMA.Mutation.ProhibitionMutationOperators.*;
import POMA.Utils;
import POMA.Exceptions.GraphDoesNotMatchTestSuitException;
import POMA.Exceptions.NoTypeProvidedException;
import POMA.Mutation.MutationOperators.*;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.*;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

public class ProhibitionMutationController {
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
	Prohibitions prohibitions;

	
	private void createHeaderForCSV(List<String> mutantNames) {
		String[] header = new String[colCount];
		header[0]= "TestMethod";
		for(int i=0;i< mutantNames.size(); i++) {
			header[i+1] =  mutantNames.get(i);
		}
		header[mutantNames.size()+1] = "totalMutationScore";
		data.add(header);
	}
	
	public static void main(String[] args) throws PMException, IOException, GraphDoesNotMatchTestSuitException, InstantiationException, NoTypeProvidedException {
		ProhibitionMutationController pmc = new ProhibitionMutationController();
		List<String> mutantNames = new ArrayList<String>();
//        String initialGraphConfig = "Policies/SimpleGraph/simpleGraphToSMT.json";
//        String initialGraphConfig = "Policies/GPMS/Graph.json";
        String initialGraphConfig = "Policies/LawUseCase/Graph.json";
      String initialProhibitionConfig = "Policies/LawUseCase/prohibitions.json";
//        String initialGraphConfig = "Policies/BankPolicy/Complex/bank_policy_config.json";

//		String initialGraphConfig = "Policies/ProhibitionExample/ProhibitionsMedicalExampleOA/graph.json";
//		String initialProhibitionConfig = "Policies/ProhibitionExample/ProhibitionsMedicalExampleOA/prohibitionsx1.json";
		
		File folder = new File(initialGraphConfig).getParentFile();
		mutantNames.add("AOC");
		mutantNames.add("AOAR");
		mutantNames.add("COC");
		mutantNames.add("COAR");
		mutantNames.add("CSS");
		mutantNames.add("RCT");
		mutantNames.add("RIS");
		mutantNames.add("ROCT");
		mutantNames.add("ROAR");
		mutantNames.add("ROP");


		pmc.graph = Utils.readAnyGraph(initialGraphConfig);
		pmc.prohibitions = ProhibitionMutation.readProhibition(initialProhibitionConfig);
		pmc.createMutants2( mutantNames, pmc.graph, pmc.prohibitions, folder);
	}

	public ProhibitionMutationController() {
		createTestMethods();
	}

	public void createMutants2(List<String> mutantNames, Graph graph, Prohibitions prohibitions, File folder) throws GraphDoesNotMatchTestSuitException, PMException {
		
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

		row12[0] = "totalMutationScore";

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
				if (mutantNames.get(i).equals("AOC")) {
					row2[0] = "AOC";
					row2[j] = Double.toString(testAOC(testMethod, graph, prohibitions));
				} else if (mutantNames.get(i).equals("AOAR")) {
					row3[0] = "AOAR";
					row3[j] = Double.toString(testAOAR(testMethod, graph, prohibitions));
				} else if (mutantNames.get(i).equals("COC")) {
					row4[0] = "COC";
					row4[j] = Double.toString(testCOC(testMethod, graph, prohibitions));
				} else if (mutantNames.get(i).equals("COAR")) {
					row5[0] = "COAR";
					row5[j] = Double.toString(testCOAR(testMethod, graph, prohibitions));
				}
				else if (mutantNames.get(i).equals("CSS")) {
					row6[0] = "CSS";
					row6[j] = Double.toString(testCSS(testMethod, graph, prohibitions));
				}
				else if (mutantNames.get(i).equals("RCT")) {
					row7[0] = "RCT";
					row7[j] = Double.toString(testRCT(testMethod, graph, prohibitions));
				}
				else if (mutantNames.get(i).equals("RIS")) {
					row8[0] = "RIS";
					row8[j] = Double.toString(testRIS(testMethod, graph, prohibitions));
				}
				else if (mutantNames.get(i).equals("ROCT")) {
					row9[0] = "ROCT";
					row9[j] = Double.toString(testROCT(testMethod, graph, prohibitions));
				}
				else if (mutantNames.get(i).equals("ROAR")) {
					row10[0] = "ROAR";
					row10[j] = Double.toString(testROAR(testMethod, graph, prohibitions));
				}else if (mutantNames.get(i).equals("ROP")) {
					row11[0] = "ROP";
					row11[j] = Double.toString(testROP(testMethod, graph, prohibitions));
				}
				
			}
			row12[j] = Double.toString(
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

	private double testAOC(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException, PMException {
		MutatorAOC mutatorAOC = new MutatorAOC(testMethod, graph, prohibitions);
		System.out.println("MutationMethod is AOC");

		try {
			mutatorAOC.main(graph, prohibitions, testMethod);
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorAOC.calculateMutationScore(mutatorAOC.getNumberOfMutants(),
				mutatorAOC.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorAOC.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorAOC.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorAOC.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorAOC.getNumberOfKilledMutants();
		return mutationScore;
	}
	
	private double testAOAR(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException, PMException {
		MutatorAOAR mutatorAOAR = new MutatorAOAR(testMethod, graph, prohibitions);
		System.out.println("MutationMethod is AOAR");

		try {
			mutatorAOAR.main(graph, prohibitions, testMethod);
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorAOAR.calculateMutationScore(mutatorAOAR.getNumberOfMutants(),
				mutatorAOAR.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorAOAR.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorAOAR.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorAOAR.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorAOAR.getNumberOfKilledMutants();
		return mutationScore;
	}
	
	private double testCOC(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException, PMException {
		MutatorCOC mutatorCOC = new MutatorCOC(testMethod, graph, prohibitions);
		System.out.println("MutationMethod is COC");

		try {
			mutatorCOC.main(graph, prohibitions, testMethod);
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorCOC.calculateMutationScore(mutatorCOC.getNumberOfMutants(),
				mutatorCOC.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorCOC.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorCOC.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorCOC.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorCOC.getNumberOfKilledMutants();
		return mutationScore;
	}
	
	private double testCOAR(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException, PMException {
		MutatorCOAR mutatorCOAR = new MutatorCOAR(testMethod, graph, prohibitions);
		System.out.println("MutationMethod is COAR");

		try {
			mutatorCOAR.main(graph, prohibitions, testMethod);
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorCOAR.calculateMutationScore(mutatorCOAR.getNumberOfMutants(),
				mutatorCOAR.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorCOAR.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorCOAR.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorCOAR.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorCOAR.getNumberOfKilledMutants();
		return mutationScore;
	}
	
	private double testCSS(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException, PMException {
		MutatorCSS mutatorCSS = new MutatorCSS(testMethod, graph, prohibitions);
		System.out.println("MutationMethod is CSS");

		try {
			mutatorCSS.main(graph, prohibitions, testMethod);
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorCSS.calculateMutationScore(mutatorCSS.getNumberOfMutants(),
				mutatorCSS.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorCSS.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorCSS.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorCSS.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorCSS.getNumberOfKilledMutants();
		return mutationScore;
	}
	
	private double testRCT(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException, PMException {
		MutatorRCT mutatorRCT = new MutatorRCT(testMethod, graph, prohibitions);
		System.out.println("MutationMethod is RCT");

		try {
			mutatorRCT.main(graph, prohibitions, testMethod);
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorRCT.calculateMutationScore(mutatorRCT.getNumberOfMutants(),
				mutatorRCT.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorRCT.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorRCT.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorRCT.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorRCT.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testRIS(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException, PMException {
		MutatorRIS mutatorRIS = new MutatorRIS(testMethod, graph, prohibitions);
		System.out.println("MutationMethod is RIS");

		try {
			mutatorRIS.main(graph, prohibitions, testMethod);
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorRIS.calculateMutationScore(mutatorRIS.getNumberOfMutants(),
				mutatorRIS.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorRIS.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorRIS.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorRIS.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorRIS.getNumberOfKilledMutants();
		return mutationScore;
	}
	
	private double testROCT(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException, PMException {
		MutatorROCT mutatorROCT = new MutatorROCT(testMethod, graph, prohibitions);
		System.out.println("MutationMethod is ROCT");

		try {
			mutatorROCT.main(graph, prohibitions, testMethod);
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorROCT.calculateMutationScore(mutatorROCT.getNumberOfMutants(),
				mutatorROCT.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorROCT.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorROCT.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorROCT.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorROCT.getNumberOfKilledMutants();
		return mutationScore;
	}
	
	private double testROAR(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException, PMException {
		MutatorROAR mutatorROAR = new MutatorROAR(testMethod, graph, prohibitions);
		System.out.println("MutationMethod is ROAR");

		try {
			mutatorROAR.main(graph, prohibitions, testMethod);
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorROAR.calculateMutationScore(mutatorROAR.getNumberOfMutants(),
				mutatorROAR.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorROAR.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorROAR.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorROAR.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorROAR.getNumberOfKilledMutants();
		return mutationScore;
	}
	
	private double testROP(String testMethod, Graph graph, Prohibitions prohibitions) throws GraphDoesNotMatchTestSuitException, PMException {
		MutatorROP mutatorROP = new MutatorROP(testMethod, graph, prohibitions);
		System.out.println("MutationMethod is ROP");

		try {
			mutatorROP.main(graph, prohibitions, testMethod);
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorROP.calculateMutationScore(mutatorROP.getNumberOfMutants(),
				mutatorROP.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorROP.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorROP.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorROP.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorROP.getNumberOfKilledMutants();
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
		header[1] = "AOC";
		header[2] = "AOAR";
		header[3] = "COC";
		header[4] = "COAR";
		header[5] = "CSS";
		header[6] = "RCT";
		header[7] = "RIS";
		header[8] = "ROCT";
		header[9] = "ROAR";
		header[10] = "ROP";
		
		header[11] = "totalMutationScore";
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
