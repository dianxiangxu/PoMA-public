package POMA.Mutation.MutationAnalysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;

import POMA.GlobalVariables;
import POMA.Utils;
import POMA.Exceptions.GraphDoesNotMatchTestSuitException;
import POMA.Exceptions.NoTypeProvidedException;
import POMA.Mutation.ObligationMutationOperators.*;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.*;
import gov.nist.csd.pm.pip.obligations.evr.EVRParser;
import gov.nist.csd.pm.pip.obligations.model.Obligation;

public class ObligationMutationController {
	List<String> testMethods;
	int totalNumberOfMutants = 0;
	int totalNumberOfKilledMutants = 0;
	int totalNumberOfMutantsForTest = 0;
	int totalNumberOfKilledMutantsForTest = 0;
	List<String[]> data = new ArrayList<String[]>();
	String[] row;
	String CSVFilePath = "CSV/OverallMutationResultsObligations.csv";
	static int rowCount = 30;
	static int colCount = 3;
	Graph graph;
	Obligation obligation;

	public static void main(String[] args)
			throws PMException, IOException, InstantiationException, IllegalAccessException,
			GraphDoesNotMatchTestSuitException, CloneNotSupportedException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, NoTypeProvidedException {
		ObligationMutationController omc = new ObligationMutationController();
		File CSV = new File(omc.CSVFilePath);
		long startTime = System.currentTimeMillis();
		omc.createHeaderForCSV();

		String initialGraphConfig = "Policies/GPMS/Graph.json";
		String initialObligationConfig = "Policies/GPMS/Obligations.yml";
//		String initialGraphConfig = "Policies/LawUseCase/Graph.json";
//		String initialObligationConfig = "Policies/LawUseCase/Obligations.yml";
		omc.graph = Utils.readAnyGraph(initialGraphConfig);
//		Utils.combineGraphs();
		//omc.graph = Utils.readGPMSGraph();
		//omc.obligation = readGPMSObligation();
		for (String testMethod : omc.testMethods) {

			String[] row = new String[rowCount];
			omc.totalNumberOfMutantsForTest = 0;
			omc.totalNumberOfKilledMutantsForTest = 0;
//			double resultROB = omc.testROB(testMethod, omc.graph, initialObligationConfig);
//			double resultCEU = omc.testCEU(testMethod, omc.graph, initialObligationConfig);
//			double resultREU = omc.testREU(testMethod, omc.graph, initialObligationConfig);
//			double resultCEPC = omc.testCEPC(testMethod, omc.graph, initialObligationConfig);
//			double resultREPC = omc.testREPC(testMethod, omc.graph, initialObligationConfig);
//			double resultCEO = omc.testCEO(testMethod, omc.graph, initialObligationConfig);
//			double resultAEO = omc.testAEO(testMethod, omc.graph, initialObligationConfig);
//			double resultREO = omc.testREO(testMethod, omc.graph, initialObligationConfig);
//			double resultCEPE = omc.testCEPE(testMethod, omc.graph, initialObligationConfig);
//			double resultREPE = omc.testREPE(testMethod, omc.graph, initialObligationConfig);
//			double resultROC = omc.testROC(testMethod, omc.graph, initialObligationConfig);
//			double resultROA = omc.testROA(testMethod, omc.graph, initialObligationConfig);
//			double resultNCD = omc.testNCD(testMethod, omc.graph, initialObligationConfig);
			double resultROF = omc.testROF(testMethod, omc.graph, initialObligationConfig);
			double resultNOF = omc.testNOF(testMethod, omc.graph, initialObligationConfig);
//			double resultCAC = omc.testCAC(testMethod, omc.graph, initialObligationConfig);
//			double resultICA = omc.testICA(testMethod, omc.graph, initialObligationConfig);
//			double resultIAA = omc.testIAA(testMethod, omc.graph, initialObligationConfig);
			double resultIGA = omc.testIGA(testMethod, omc.graph, initialObligationConfig);
//			double resultINA = omc.testINA(testMethod, omc.graph, initialObligationConfig);
			

			row[0] = testMethod;
//			row[1] = Double.toString(resultROB);
//			row[2] = Double.toString(resultCEU);
//			row[3] = Double.toString(resultREU);
//			row[4] = Double.toString(resultCEPC);
//			row[5] = Double.toString(resultREPC);
//			row[6] = Double.toString(resultCEO);
//			row[7] = Double.toString(resultAEO);
//			row[8] = Double.toString(resultREO);
//			row[9] = Double.toString(resultCEPE);
//			row[10] = Double.toString(resultREPE);
//			row[11] = Double.toString(resultROC);
//			row[12] = Double.toString(resultROA);
//			row[13] = Double.toString(resultNCD);
//			row[14] = Double.toString(resultROF);
//			row[15] = Double.toString(resultNOF);
//			row[16] = Double.toString(resultCAC);
//			row[17] = Double.toString(resultICA);
//			row[18] = Double.toString(resultIAA);
//			row[19] = Double.toString(resultIGA);
//			row[20] = Double.toString(resultINA);
			row[25] = Double.toString(
					(double) omc.totalNumberOfKilledMutantsForTest / (double) omc.totalNumberOfMutantsForTest * 100);
			omc.data.add(row);
		}
		omc.saveCSV(omc.data, CSV);

		long endTime = System.currentTimeMillis();
		long timeElapsed = endTime - startTime;
		long minutes = (timeElapsed / 1000) / 60;
		int seconds = (int) ((timeElapsed / 1000) % 60);
		System.out.println("Execution time in milliseconds: " + timeElapsed);
		System.out.println("Execution time in min and sec: " + minutes + ":" + seconds);

	}

	public ObligationMutationController() {
		createTestMethods();
	}

	public void createMutants(List<String> mutantNames, Graph graph, String obligationPath, File folder)
			throws GraphDoesNotMatchTestSuitException, NoTypeProvidedException, InstantiationException {

		// createHeaderForCSV(mutantNames);
		List<String[]> outputList = new ArrayList<String[]>();

		String[] row1 = new String[] { "TestMethod", "Pairwise", "AllCombination" };
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
		String[] row15 = new String[colCount];
		String[] row16 = new String[colCount];
		String[] row17 = new String[colCount];
		String[] row18 = new String[colCount];
		String[] row19 = new String[colCount];
		String[] row20 = new String[colCount];
		String[] row21 = new String[colCount];
		String[] row22 = new String[colCount];
		String[] row23 = new String[colCount];
		row23[0] = "totalMutationScore";

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
				if (mutantNames.get(i).equals("AEO")) {
					row2[0] = "AEO";
					row2[j] = Double.toString(testAEO(testMethod, graph, obligationPath));
				} else if (mutantNames.get(i).equals("CAC")) {
					row3[0] = "CAC";
					row3[j] = Double.toString(testCAC(testMethod, graph, obligationPath));
				} else if (mutantNames.get(i).equals("CEO")) {
					row4[0] = "CEO";
					row4[j] = Double.toString(testCEO(testMethod, graph, obligationPath));
				} else if (mutantNames.get(i).equals("CEPC")) {
					row5[0] = "CEPC";
					row5[j] = Double.toString(testCEPC(testMethod, graph, obligationPath));
				} else if (mutantNames.get(i).equals("CEPE")) {
					row6[0] = "CEPE";
					row6[j] = Double.toString(testCEPE(testMethod, graph, obligationPath));
			//		System.out.println("HELLO");
				} else if (mutantNames.get(i).equals("CEU")) {
					row7[0] = "CEU";
					row7[j] = Double.toString(testCEU(testMethod, graph, obligationPath));
				} else if (mutantNames.get(i).equals("IAA")) {
					row8[0] = "IAA";
					row8[j] = Double.toString(testIAA(testMethod, graph, obligationPath));
				} else if (mutantNames.get(i).equals("ICA")) {
					row9[0] = "ICA";
					row9[j] = Double.toString(testICA(testMethod, graph, obligationPath));
				} else if (mutantNames.get(i).equals("IGA")) {
					row10[0] = "IGA";
					row10[j] = Double.toString(testIGA(testMethod, graph, obligationPath));
				} else if (mutantNames.get(i).equals("INA")) {
					row11[0] = "INA";
					row11[j] = Double.toString(testINA(testMethod, graph, obligationPath));
				} else if (mutantNames.get(i).equals("NCD")) {
					row12[0] = "NCD";
					row12[j] = Double.toString(testNCD(testMethod, graph, obligationPath));
				} else if (mutantNames.get(i).equals("NOF")) {
					row13[0] = "NOF";
					row13[j] = Double.toString(testNOF(testMethod, graph, obligationPath));
				} else if (mutantNames.get(i).equals("RCA")) {
					row14[0] = "RCA";
					row14[j] = Double.toString(testRCA(testMethod, graph, obligationPath));
				} else if (mutantNames.get(i).equals("REO")) {
					row15[0] = "REO";
					row15[j] = Double.toString(testREO(testMethod, graph, obligationPath));
				} else if (mutantNames.get(i).equals("REPC")) {
					row16[0] = "REPC";
					row16[j] = Double.toString(testREPC(testMethod, graph, obligationPath));
				} else if (mutantNames.get(i).equals("REPE")) {
					row17[0] = "REPE";
					row17[j] = Double.toString(testREPE(testMethod, graph, obligationPath));
				} else if (mutantNames.get(i).equals("REU")) {
					row18[0] = "REU";
					row18[j] = Double.toString(testREU(testMethod, graph, obligationPath));
				} else if (mutantNames.get(i).equals("ROA")) {
					row19[0] = "ROA";
					row19[j] = Double.toString(testROA(testMethod, graph, obligationPath));
				} else if (mutantNames.get(i).equals("ROC")) {
					row20[0] = "ROC";
					row20[j] = Double.toString(testROC(testMethod, graph, obligationPath));
				} else if (mutantNames.get(i).equals("ROF")) {
					row21[0] = "ROF";
					row21[j] = Double.toString(testROF(testMethod, graph, obligationPath));
				} else if (mutantNames.get(i).equals("ROB")) {
					row22[0] = "ROB";
					row22[j] = Double.toString(testROB(testMethod, graph, obligationPath));
				}

			}
			row14[j] = Double
					.toString((double) totalNumberOfKilledMutantsForTest / (double) totalNumberOfMutantsForTest * 100);

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
		data.add(row15);
		data.add(row16);
		data.add(row17);
		data.add(row18);
		data.add(row19);
		data.add(row20);
		data.add(row21);
		data.add(row22);
		data.add(row23);

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

	private double testROB(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
		MutatorROB mutatorROB = new MutatorROB(testMethod, graph, obligationPath);
		System.out.println("MutationMethod is ROB");

		try {
			mutatorROB.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorROB.calculateMutationScore(mutatorROB.getNumberOfMutants(),
				mutatorROB.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorROB.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorROB.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorROB.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorROB.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testCEU(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
		MutatorCEU mutatorCEU = new MutatorCEU(testMethod, graph, obligationPath);
		System.out.println("MutationMethod is CEU");

		try {
			mutatorCEU.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorCEU.calculateMutationScore(mutatorCEU.getNumberOfMutants(),
				mutatorCEU.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorCEU.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorCEU.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorCEU.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorCEU.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testREU(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
		MutatorREU mutatorREU = new MutatorREU(testMethod, graph, obligationPath);
		System.out.println("MutationMethod is REU");

		try {
			mutatorREU.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorREU.calculateMutationScore(mutatorREU.getNumberOfMutants(),
				mutatorREU.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorREU.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorREU.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorREU.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorREU.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testCEPC(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
		MutatorCEPC mutatorCEPC = new MutatorCEPC(testMethod, graph, obligationPath);
		System.out.println("MutationMethod is CEPC");

		try {
			mutatorCEPC.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorCEPC.calculateMutationScore(mutatorCEPC.getNumberOfMutants(),
				mutatorCEPC.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorCEPC.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorCEPC.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorCEPC.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorCEPC.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testREPC(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
		MutatorREPC mutatorREPC = new MutatorREPC(testMethod, graph, obligationPath);
		System.out.println("MutationMethod is REPC");

		try {
			mutatorREPC.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorREPC.calculateMutationScore(mutatorREPC.getNumberOfMutants(),
				mutatorREPC.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorREPC.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorREPC.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorREPC.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorREPC.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testCEO(String testMethod, Graph graph, String obligationPath)
			throws GraphDoesNotMatchTestSuitException, NoTypeProvidedException {
		MutatorCEO mutatorCEO = new MutatorCEO(testMethod, graph, obligationPath);
		System.out.println("MutationMethod is CEO");

		try {
			mutatorCEO.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorCEO.calculateMutationScore(mutatorCEO.getNumberOfMutants(),
				mutatorCEO.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorCEO.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorCEO.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorCEO.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorCEO.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testAEO(String testMethod, Graph graph, String obligationPath)
			throws GraphDoesNotMatchTestSuitException, NoTypeProvidedException {
		MutatorAEO mutatorAEO = new MutatorAEO(testMethod, graph, obligationPath);
		System.out.println("MutationMethod is AEO");

		try {
			mutatorAEO.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorAEO.calculateMutationScore(mutatorAEO.getNumberOfMutants(),
				mutatorAEO.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorAEO.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorAEO.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorAEO.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorAEO.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testREO(String testMethod, Graph graph, String obligationPath)
			throws GraphDoesNotMatchTestSuitException, NoTypeProvidedException {
		MutatorREO mutatorREO = new MutatorREO(testMethod, graph, obligationPath);
		System.out.println("MutationMethod is REO");

		try {
			mutatorREO.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorREO.calculateMutationScore(mutatorREO.getNumberOfMutants(),
				mutatorREO.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorREO.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorREO.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorREO.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorREO.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testCEPE(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
		MutatorCEPE mutatorCEPE = new MutatorCEPE(testMethod, graph, obligationPath);
		System.out.println("MutationMethod is CEPE");

		try {
			mutatorCEPE.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorCEPE.calculateMutationScore(mutatorCEPE.getNumberOfMutants(),
				mutatorCEPE.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorCEPE.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorCEPE.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorCEPE.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorCEPE.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testREPE(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
		MutatorREPE mutatorREPE = new MutatorREPE(testMethod, graph, obligationPath);
		System.out.println("MutationMethod is REPE");

		try {
			mutatorREPE.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorREPE.calculateMutationScore(mutatorREPE.getNumberOfMutants(),
				mutatorREPE.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorREPE.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorREPE.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorREPE.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorREPE.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testROC(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
		MutatorROC mutatorROC = new MutatorROC(testMethod, graph, obligationPath);
		System.out.println("MutationMethod is ROC");

		try {
			mutatorROC.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorROC.calculateMutationScore(mutatorROC.getNumberOfMutants(),
				mutatorROC.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorROC.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorROC.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorROC.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorROC.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testRCA(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
		MutatorRCA mutatorRCA = new MutatorRCA(testMethod, graph, obligationPath);
		System.out.println("MutationMethod is RCA");

		try {
			mutatorRCA.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorRCA.calculateMutationScore(mutatorRCA.getNumberOfMutants(),
				mutatorRCA.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorRCA.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorRCA.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorRCA.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorRCA.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testROA(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
		MutatorROA mutatorROA = new MutatorROA(testMethod, graph, obligationPath);
		System.out.println("MutationMethod is ROA");

		try {
			mutatorROA.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorROA.calculateMutationScore(mutatorROA.getNumberOfMutants(),
				mutatorROA.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorROA.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorROA.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorROA.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorROA.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testNCD(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
		MutatorNCD mutatorNCD = new MutatorNCD(testMethod, graph, obligationPath);
		System.out.println("MutationMethod is NCD");

		try {
			mutatorNCD.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorNCD.calculateMutationScore(mutatorNCD.getNumberOfMutants(),
				mutatorNCD.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorNCD.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorNCD.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorNCD.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorNCD.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testROF(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
		MutatorROF mutatorROF = new MutatorROF(testMethod, graph, obligationPath);
		System.out.println("MutationMethod is ROF");

		try {
			mutatorROF.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorROF.calculateMutationScore(mutatorROF.getNumberOfMutants(),
				mutatorROF.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorROF.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorROF.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorROF.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorROF.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testNOF(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
		MutatorNOF mutatorNOF = new MutatorNOF(testMethod, graph, obligationPath);
		System.out.println("MutationMethod is NOF");

		try {
			mutatorNOF.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorNOF.calculateMutationScore(mutatorNOF.getNumberOfMutants(),
				mutatorNOF.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorNOF.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorNOF.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorNOF.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorNOF.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testCAC(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
		MutatorCAC mutatorCAC = new MutatorCAC(testMethod, graph, obligationPath);
		System.out.println("MutationMethod is CAC");

		try {
			mutatorCAC.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorCAC.calculateMutationScore(mutatorCAC.getNumberOfMutants(),
				mutatorCAC.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorCAC.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorCAC.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorCAC.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorCAC.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testICA(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
		MutatorICA mutatorICA = new MutatorICA(testMethod, graph, obligationPath);
		System.out.println("MutationMethod is ICA");

		try {
			mutatorICA.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorICA.calculateMutationScore(mutatorICA.getNumberOfMutants(),
				mutatorICA.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorICA.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorICA.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorICA.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorICA.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testIAA(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
		MutatorIAA mutatorIAA = new MutatorIAA(testMethod, graph, obligationPath);
		System.out.println("MutationMethod is IAA");

		try {
			mutatorIAA.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorIAA.calculateMutationScore(mutatorIAA.getNumberOfMutants(),
				mutatorIAA.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorIAA.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorIAA.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorIAA.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorIAA.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testIGA(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
		MutatorIGA mutatorIGA = new MutatorIGA(testMethod, graph, obligationPath);
		System.out.println("MutationMethod is IGA");

		try {
			mutatorIGA.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorIGA.calculateMutationScore(mutatorIGA.getNumberOfMutants(),
				mutatorIGA.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorIGA.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorIGA.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorIGA.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorIGA.getNumberOfKilledMutants();
		return mutationScore;
	}

	private double testINA(String testMethod, Graph graph, String obligationPath) throws GraphDoesNotMatchTestSuitException {
		MutatorINA mutatorINA = new MutatorINA(testMethod, graph, obligationPath);
		System.out.println("MutationMethod is INA");

		try {
			mutatorINA.init();
		} catch (PMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double mutationScore = mutatorINA.calculateMutationScore(mutatorINA.getNumberOfMutants(),
				mutatorINA.getNumberOfKilledMutants());
		System.out.println("TestMethod is " + testMethod);
		System.out.println("Number of mutations: " + mutatorINA.getNumberOfMutants());
		System.out.println("Number of killed mutants: " + mutatorINA.getNumberOfKilledMutants());

		System.out.println("Mutation Score: " + mutationScore + "%");
		System.out.println();
		totalNumberOfMutantsForTest += mutatorINA.getNumberOfMutants();
		totalNumberOfKilledMutantsForTest += mutatorINA.getNumberOfKilledMutants();
		return mutationScore;
	}

	private void createTestMethods() {
		testMethods = new ArrayList<String>();
//		testMethods.add("RS");
//		testMethods.add("R");
		testMethods.add("Pairwise");
//		testMethods.add("AllCombinations");

	}

	private void createHeaderForCSV() {
		String[] header = new String[rowCount];
		header[0] = "TestMethod";
		header[1] = "ROB";
		header[2] = "CEU";
		header[3] = "REU";
		header[4] = "CEPC";
		header[5] = "REPC";
		header[6] = "CEO";
		header[7] = "AEO";
		header[8] = "REO";
		header[9] = "CEPE";
		header[10] = "REPE";
		header[11] = "ROC";
		header[12] = "ROA";
		header[13] = "NCD";
		header[14] = "ROF";
		header[15] = "NOF";
		header[16] = "CAC";
		header[17] = "ICA";
		header[18] = "IAA";
		header[19] = "IGA";
		header[20] = "INA";
		header[25] = "totalMutationScore";
		data.add(header);
	}

	private void createHeaderForCSV(List<String> mutantNames) {
		String[] header = new String[rowCount];
		header[0] = "TestMethod";
		for (int i = 0; i < mutantNames.size(); i++) {
			header[i + 1] = mutantNames.get(i);
		}
		header[mutantNames.size() + 1] = "totalMutationScore";
		data.add(header);
	}

	public void saveCSV(List<String[]> data, File directoryForTestResults) throws PMException, IOException {
		File globalFile = new File(GlobalVariables.currentPath);
		if(!globalFile.exists()) {
			globalFile = new File("CSV/OverallMutationResultsObligations.csv");
		}
		else if (!globalFile.isDirectory()) {
			globalFile = new File(globalFile.getParent() + "/CSV/OverallMutationResultsObligations.csv");
		} else {
			globalFile = new File(GlobalVariables.currentPath + "/CSV/OverallMutationResultsObligations.csv");

		}
		if (globalFile.createNewFile()) {
			System.out.println("File has been created.");
		} else {
			System.out.println("File already exists.");
		}
		BufferedWriter writer = null;
		writer = new BufferedWriter(new FileWriter(globalFile));
		CSVWriter CSVwriter = new CSVWriter(writer);
		CSVwriter.writeAll(data);
		writer.flush();
		CSVwriter.close();

		if (writer != null)
			writer.close();
	}

	public static Obligation readGPMSObligation() throws PMException, IOException {
		File obligationFile = getFileFromResources("Policies/GPMS/Obligations.yml");
		InputStream inputStream = new FileInputStream(obligationFile);
		Obligation obligation = EVRParser.parse(inputStream);
		return obligation;
	}

	public static File getFileFromResources(String fileName) {
		File resource = new File(fileName);
		return resource;
	}
}
