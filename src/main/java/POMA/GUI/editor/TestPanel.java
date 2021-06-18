package POMA.GUI.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

import POMA.GUI.*;
import POMA.GUI.components.JPanelPB;
import POMA.GUI.components.MutationBasedTestMutationMethods;
import POMA.TestSuitGeneration.TestSuitGenerator;

//import org.seal.xacml.coverage.CoverageHelper;
//import org.seal.xacml.coverage.DecisionCoverage;
//import org.seal.xacml.coverage.MCDC;
//import org.seal.xacml.coverage.RuleBodyCoverage;
//import org.seal.xacml.coverage.RuleCoverage;
//import org.seal.xacml.coverage.RulePairCoverage;
//import org.seal.xacml.mutation.MutationBasedTestGenerator;
//import org.seal.xacml.mutation.PNOMutationBasedTestGenerator;
//import org.seal.xacml.policyUtils.PolicyLoader;
//import org.seal.xacml.semanticCoverage.Coverage;
//import org.seal.xacml.semanticCoverage.PolicyCoverageFactory;
//import org.seal.xacml.semanticCoverage.TestSuite;
//import org.seal.xacml.utils.ExceptionUtil;
//import org.seal.xacml.utils.TestUtil;
//import org.seal.xacml.poma.XPA;
//import org.umu.editor.XMLFileFilter;
//import org.wso2.balana.AbstractPolicy;

public class TestPanel extends JPanelPB {
	private POMA poma;
	private String workingTestSuiteFileName;
//	private PolicyTestSuite testSuite;
	private Vector<Vector<Object>> data;
	private TestTablePanel requestTablePanel;
	private JPanel requestPanel;
	private String type;
	private boolean hasFailure;

	
	public TestPanel(POMA poma) {
		this.poma = poma;
	}

	public POMA getDemo() {
		return this.poma;
	}
		
	
	public void setWorkingTestSuiteFileName(String filename)
	{
		this.workingTestSuiteFileName = filename;
	}

	public void setUpTestPanel() {
		removeAll();
		setLayout(new BorderLayout());
		String[] columnNames = { "No", "Name", "Request File","Expected Response", "Actual Response", "Verdict" };
//		data = TestUtil.getTestRecordsVector(testSuite.getTestRecords());
		
		if (data.size() == 0) {
			JOptionPane.showMessageDialog(poma, "There is no test!");
			return;
		}
		Vector<Object> selected = data.get(0);
		String request = selected.get(6).toString();
		requestPanel = new JPanel();
		requestPanel.setLayout(new BorderLayout());
		GeneralTablePanel gt = RequestTable.getRequestTable(request, false);
		gt.setMinRows(5);
		RequestTable.setPreferredColumnWidths(gt, this.getSize().getWidth());
		requestPanel.add(gt, BorderLayout.CENTER);

		requestTablePanel = new TestTablePanel(data, columnNames, 5,
				requestPanel);
		JScrollPane scrollpane = new JScrollPane(requestTablePanel);
		JSplitPane jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		jSplitPane.setTopComponent(scrollpane);
		jSplitPane.setBottomComponent(requestPanel);
		jSplitPane.setResizeWeight(0.7);

		add(jSplitPane, BorderLayout.CENTER);
		poma.setToTestPane();
		poma.updateMainTabbedPane();
	}

	public void openTests() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
//		File workingPolicy = poma.getWorkingPolicyFile();
//		if ( workingPolicy!= null){
//			fileChooser.setCurrentDirectory(poma.getWorkingPolicyFile().getParentFile());
//		} else {
//			fileChooser.setCurrentDirectory((new File(".")));
//		}
//		fileChooser.setFileFilter(new XMLFileFilter("xls"));
		fileChooser.setDialogTitle("Open Test Suite");
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File testSuiteFile = fileChooser.getSelectedFile();
			if (!testSuiteFile.toString().endsWith(".xls")) {
				JOptionPane.showMessageDialog(poma,
						"The open File is not a test suite *.xls",
						"Error of Selection", JOptionPane.WARNING_MESSAGE);
			} else {
				try {
					workingTestSuiteFileName = testSuiteFile.getAbsolutePath();
					this.startProgressStatus();
//					if(workingPolicy!=null){
//						testSuite = new PolicyTestSuite(workingTestSuiteFileName, workingPolicy.toString());
//					}else{
//						testSuite = new PolicyTestSuite(workingTestSuiteFileName,	"");
//					}
					setUpTestPanel();
					this.stopProgressStatus();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(poma, "Invalid test suite file");
				}
			}
		}
	}

	private JRadioButton exclusiveRuleCoverageRadio = new JRadioButton("Rule coverage");
	private JRadioButton DecisionCoverageRadio = new JRadioButton("Decision coverage");
	
	
	private JRadioButton MCDCRadio = new JRadioButton("MC\\DC ");
	private JRadioButton MCDCRadio_NoError = new JRadioButton("MC\\DC_NoError");
	private JRadioButton DecisionCoverageRadio_NoError = new JRadioButton("Decision coverage_NoError");
	private JRadioButton rulePairCoverage = new JRadioButton("Rule-Pair Coverage");
	private JRadioButton permitDenyRulePairCoverage = new JRadioButton("Permit/Deny Rule-Pair Coverage");
	
	private JPanel createPanel() {
		JPanel myPanel = new JPanel();
		exclusiveRuleCoverageRadio.setSelected(true);

		final ButtonGroup group = new ButtonGroup();
		group.add(exclusiveRuleCoverageRadio);
		group.add(DecisionCoverageRadio);
		group.add(MCDCRadio);
		group.add(MCDCRadio_NoError);
		group.add(DecisionCoverageRadio_NoError);
		group.add(rulePairCoverage);
		group.add(permitDenyRulePairCoverage);
		myPanel.setLayout(new GridLayout(3, 3));
		myPanel.add(exclusiveRuleCoverageRadio);
		myPanel.add(DecisionCoverageRadio);
		myPanel.add(DecisionCoverageRadio_NoError);
		myPanel.add(MCDCRadio);
		myPanel.add(MCDCRadio_NoError);
		myPanel.add(rulePairCoverage);
		myPanel.add(permitDenyRulePairCoverage);
		myPanel.setBorder(new TitledBorder(new EtchedBorder(), ""));

		return myPanel;
	}

	public void generateAllTestSuits() throws Exception {
		this.startProgressStatus();

		TestSuitGenerator graphTester = new TestSuitGenerator();
		graphTester.runAllTestGeneration();
		this.stopProgressStatus();;

	}
	
	
	public void generateCoverageBasedTests() {
//		if (!poma.hasWorkingPolicy()) {
//			JOptionPane.showMessageDialog(poma, "There is no policy!");
//			return;
//		}
//		int result = JOptionPane.showConfirmDialog(poma, createPanel(), "Please Select Test Generation Strategy", JOptionPane.OK_CANCEL_OPTION);
//		
//		if (result == JOptionPane.OK_OPTION) {
////			LoadPolicyDemo lp = new LoadPolicyDemo();
////			AbstractPolicy policy = lp.getPolicy(poma.getWorkingPolicyFilePath());
////			String policyFilePath = poma.getWorkingPolicyFilePath();
//			this.startProgressStatus();
//			if (exclusiveRuleCoverageRadio.isSelected()) {
//				try{
////					this.type = NameDirectory.RULE_COVERAGE;
////					RuleCoverage requestGenerator = new RuleCoverage(policyFilePath); 
//					long millis = System.currentTimeMillis();
////					List<String> requests = requestGenerator.generateTests();
//					long millis2 = System.currentTimeMillis();
//					System.out.println("-------------");
//					System.out.println((millis2-millis));
//					System.out.println("-------------");
//					
//					testSuite = new PolicyTestSuite(policyFilePath,requests,NameDirectory.RULE_COVERAGE);
//					testSuite.save();
//					workingTestSuiteFileName = TestUtil.getTestSuiteMetaFilePath(policyFilePath, NameDirectory.RULE_COVERAGE);
//				}catch(Exception e){
//					ExceptionUtil.handleInDefaultLevel(e);
//				}
//			} else if (DecisionCoverageRadio.isSelected()) {
//				try{
//					DecisionCoverage requestGenerator = new DecisionCoverage(policyFilePath,true);
//					//List<String> requests = requestGenerator.generateTests();
//					long millis = System.currentTimeMillis();
//					List<String> requests = requestGenerator.generateTests();
//					long millis2 = System.currentTimeMillis();
//					System.out.println("-------------");
//					System.out.println((millis2-millis));
//					System.out.println("-------------");
//					
//					testSuite = new PolicyTestSuite(policyFilePath,requests,NameDirectory.DECISION_COVERAGE);
//					testSuite.save();
//					workingTestSuiteFileName = TestUtil.getTestSuiteMetaFilePath(policyFilePath, NameDirectory.DECISION_COVERAGE);
//				}catch(Exception e){
//					ExceptionUtil.handleInDefaultLevel(e);
//				}
//			} else if (DecisionCoverageRadio_NoError.isSelected()) {
//				try{
//					DecisionCoverage requestGenerator = new DecisionCoverage(policyFilePath,false);
//					//List<String> requests = requestGenerator.generateTests();
//					long millis = System.currentTimeMillis();
//					List<String> requests = requestGenerator.generateTests();
//					long millis2 = System.currentTimeMillis();
//					System.out.println("-------------");
//					System.out.println((millis2-millis));
//					System.out.println("-------------");
//					
//					testSuite = new PolicyTestSuite(policyFilePath,requests,NameDirectory.DECISION_COVERAGE_NO_ERROR);
//					testSuite.save();
//					workingTestSuiteFileName = TestUtil.getTestSuiteMetaFilePath(policyFilePath, NameDirectory.DECISION_COVERAGE_NO_ERROR);
//				}catch(Exception e){
//					ExceptionUtil.handleInDefaultLevel(e);
//				}
//			} else if (MCDCRadio.isSelected()) {
//				try{
//					MCDC requestGenerator = new MCDC(policyFilePath,true);
//					long millis = System.currentTimeMillis();
//					List<String> requests = requestGenerator.generateTests();
//					long millis2 = System.currentTimeMillis();
//					System.out.println("-------------");
//					System.out.println((millis2-millis));
//					System.out.println("-------------");
//					testSuite = new PolicyTestSuite(policyFilePath,requests,NameDirectory.MCDC_COVERAGE);
//					testSuite.save();
//					workingTestSuiteFileName = TestUtil.getTestSuiteMetaFilePath(policyFilePath, NameDirectory.MCDC_COVERAGE);
//				}catch(Exception e){
//					ExceptionUtil.handleInDefaultLevel(e);
//				}
//			} else if (MCDCRadio_NoError.isSelected()) {
//				try{
//					MCDC requestGenerator = new MCDC(policyFilePath,false);
//					long millis = System.currentTimeMillis();
//					List<String> requests = requestGenerator.generateTests();
//					long millis2 = System.currentTimeMillis();
//					System.out.println("-------------");
//					System.out.println((millis2-millis));
//					System.out.println("-------------");
//					
//					testSuite = new PolicyTestSuite(policyFilePath,requests,NameDirectory.MCDC_COVERAGE_NO_ERROR);
//					testSuite.save();
//					workingTestSuiteFileName = TestUtil.getTestSuiteMetaFilePath(policyFilePath, NameDirectory.MCDC_COVERAGE_NO_ERROR);
//				}catch(Exception e){
//					ExceptionUtil.handleInDefaultLevel(e);
//				}
//			} else if (rulePairCoverage.isSelected()) {
//				try{
//					RulePairCoverage requestGenerator = new RulePairCoverage(policyFilePath);
//					List<String> requests = requestGenerator.generateTests(false);
//					testSuite = new PolicyTestSuite(policyFilePath,requests,NameDirectory.RULE_PAIR_COVERAGE);
//					testSuite.save();
//					workingTestSuiteFileName = TestUtil.getTestSuiteMetaFilePath(policyFilePath, NameDirectory.RULE_PAIR_COVERAGE);
//				}catch(Exception e){
//					ExceptionUtil.handleInDefaultLevel(e);
//				}
//			} else if (permitDenyRulePairCoverage.isSelected()) {
//				try{
//					RulePairCoverage requestGenerator = new RulePairCoverage(policyFilePath);
//					List<String> requests = requestGenerator.generateTests(true);
//					testSuite = new PolicyTestSuite(policyFilePath,requests,NameDirectory.PERMIT_DENY_RULE_PAIR_COVERAGE);
//					testSuite.save();
//					workingTestSuiteFileName = TestUtil.getTestSuiteMetaFilePath(policyFilePath, NameDirectory.PERMIT_DENY_RULE_PAIR_COVERAGE);
//				}catch(Exception e){
//					ExceptionUtil.handleInDefaultLevel(e);
//				}
//			}
//			
//			
//			String dir = poma.getWorkingPolicyFile().getParent();
//			Runtime run = Runtime.getRuntime();
//			try {
//				testSuite = new PolicyTestSuite(workingTestSuiteFileName, poma.getWorkingPolicyFilePath());
//				setUpTestPanel();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			this.stopProgressStatus();
//		}

	}

	public void generateMutationBasedTests() {
//		if (!poma.hasWorkingPolicy()) {
//			JOptionPane.showMessageDialog(poma, "There is no policy!");
//			return;
//		}
//		MutationBasedTestGenerator testGenerator;
//		List<TaggedRequest> taggedRequests;
//		try{
//			testGenerator = new MutationBasedTestGenerator(poma.getWorkingPolicyFilePath());
//			MutationBasedTestMutationMethods mbtMethods = new MutationBasedTestMutationMethods();
//			int result = JOptionPane.showConfirmDialog(poma, mbtMethods.createPanel(),"Please Select Mutation Methods",JOptionPane.OK_CANCEL_OPTION);
//			String policyFilePath = poma.getWorkingPolicyFilePath();
//			this.type = NameDirectory.MUTATION_BASED_TEST;
//			if (result == JOptionPane.OK_OPTION) {
//				this.startProgressStatus();
//				List<String> mutationMethods = mbtMethods.getMutationOperatorList(true);
//				long millis = System.currentTimeMillis();
//				taggedRequests = testGenerator.generateRequests(mutationMethods);
//				long millis2 = System.currentTimeMillis();
//				System.out.println("-------------");
//				System.out.println((millis2-millis));
//				System.out.println("-------------");
//				
//				PolicyTestSuite suite = new PolicyTestSuite(policyFilePath, this.type, taggedRequests);
//				suite.save();
//				this.testSuite = suite;
//				this.workingTestSuiteFileName = TestUtil.getTestSuiteMetaFilePath(policyFilePath, NameDirectory.MUTATION_BASED_TEST);
//				setUpTestPanel();
//				this.stopProgressStatus();
//			}
//		}catch(Exception e){
//			ExceptionUtil.handleInDefaultLevel(e);
//		}
	}
	
	public void generatePNOMutationBasedTests() {
//		if (!poma.hasWorkingPolicy()) {
//			JOptionPane.showMessageDialog(poma, "There is no policy!");
//			return;
//		}
//		PNOMutationBasedTestGenerator testGenerator;
//		List<TaggedRequest> taggedRequests;
//		try{
//			testGenerator = new PNOMutationBasedTestGenerator(poma.getWorkingPolicyFilePath());
//			MutationBasedTestMutationMethods mbtMethods = new MutationBasedTestMutationMethods();
//			int result = JOptionPane.showConfirmDialog(poma, mbtMethods.createPanel(),"Please Select Mutation Methods",JOptionPane.OK_CANCEL_OPTION);
//			String policyFilePath = poma.getWorkingPolicyFilePath();
//			this.type = NameDirectory.MUTATION_BASED_TEST;
//			if (result == JOptionPane.OK_OPTION) {
//				this.startProgressStatus();
//				List<String> mutationMethods = mbtMethods.getMutationOperatorList(true);
//				long millis = System.currentTimeMillis();
//				taggedRequests = testGenerator.generateRequests(mutationMethods);
//				long millis2 = System.currentTimeMillis();
//				System.out.println("-------------");
//				System.out.println((millis2-millis));
//				System.out.println("-------------");
//				
//				PolicyTestSuite suite = new PolicyTestSuite(policyFilePath, this.type, taggedRequests);
//				suite.save();
//				this.testSuite = suite;
//				this.workingTestSuiteFileName = TestUtil.getTestSuiteMetaFilePath(policyFilePath, NameDirectory.MUTATION_BASED_TEST);
//				setUpTestPanel();
//				this.stopProgressStatus();
//			}
//		}catch(Exception e){
//			ExceptionUtil.handleInDefaultLevel(e);
//		}
	}
	
	public void evaluateCoverage(){
//		if(checkInputs()){
//			TestSuite testSuite = getTestSuite();
//			try {
//				List<Boolean> results = testSuite.runTests(PolicyLoader.loadPolicy(poma.getWorkingPolicyFile()));
//			} catch(Exception e) {
//				e.printStackTrace();
//			}
//			this.startProgressStatus();
//			List<List<Coverage>> coverageMatrix = PolicyCoverageFactory.getCoverageMatrix();
//			float ruleCoverage = CoverageHelper.getRuleCoverage(coverageMatrix);
//            this.stopProgressStatus();
//        	DecimalFormat df = new DecimalFormat("###.##");
//            JOptionPane.showMessageDialog(null, "Rule Coverage : " + df.format(ruleCoverage) + " %", "Test Suite Coverages", JOptionPane.INFORMATION_MESSAGE);
//		}
	}
	


	private boolean checkInputs(){
//		if (!poma.hasWorkingPolicy()) {
//			JOptionPane.showMessageDialog(poma, "There is no policy!");
//			return false;
//		}
//		if (!poma.hasTests()){
//			JOptionPane.showMessageDialog(poma, "There are no tests.");
//			return false;
//		}
//		for(TestRecord record: poma.getTestPanel().getPolicyTestSuite().getTestRecords()){
//			if(record.getOracle().equals("")){
//				JOptionPane.showMessageDialog(poma, "There are no oracles in Test Suite");
//				return false;
//			}
//		}
//		return true;
		return true;
	}
//	private boolean hasFault(TestSuite testSuite) {
//		try{
//			List<Boolean> results = testSuite.runTests(PolicyLoader.loadPolicy(poma.getWorkingPolicyFile()));
//			for(Boolean b:results){
//				if(!b){
//					return true;
//				}
//			}
//		} catch(Exception e){
//			e.printStackTrace();
//		}
//		return false;
//	}
	
//	private TestSuite getTestSuite(){
//		List<String> requests = new ArrayList<String>();
//		List<String> oracles = new ArrayList<String>();
//		for(TestRecord record: poma.getTestPanel().getPolicyTestSuite().getTestRecords()){
//			requests.add(record.getRequest());
//			oracles.add(record.getOracle());
//		}
//		return new TestSuite(null,requests, oracles);
//		
//	}
	public void runTests() {
//		hasFailure = false; 
//		if (!poma.hasWorkingPolicy()) {
//			JOptionPane.showMessageDialog(poma, "There is no policy!");
//			return;
//		}
//		if (!hasTests()) {
//			JOptionPane.showMessageDialog(poma, "There are no tests.");
//			return; 
//		}
//
//		try {
//			/*PolicyRunner runner = new PolicyRunner(
//					demo.getWorkingPolicyFilePath());*/
//			AbstractPolicy policy = PolicyLoader.loadPolicy(poma.getWorkingPolicyFile());
//			this.startProgressStatus();
//			for (Vector<Object> child : data) {
//				int result = TestSuite.runTestWithoutOracle(policy, child.get(6).toString());
//				// System.out.println(result);
//				String actualResponse = ResultConverter.ConvertResult(result);
//				child.set(4, actualResponse);
//				if (child.get(3) != null && !child.get(3).toString().equals("")) {
//					String expectedResponse = child.get(3).toString();
//					if (actualResponse.equals(expectedResponse))
//						child.set(5, "pass");
//					else {
//						child.set(5, "fail");
//						
//						
//						
//						requestTablePanel.table.getColumnModel().getColumn(5).setCellRenderer(new verdictColumnCellRenderer());
//
//						hasFailure = true;
//					}
//				}
//				poma.setToTestPane();
//			}
//			this.stopProgressStatus();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		requestTablePanel.validate();
//		requestTablePanel.updateUI();
	}
	
	static class verdictColumnCellRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row,int column) {
			Component c = super.getTableCellRendererComponent(table, value,isSelected, hasFocus, row, column);

//			double d = (double)value;
			if (column == 5) {
				if (new String("fail").equals(value.toString())) {
					System.out.println("executed.....");
					Color myColor = new Color(255,0,0);
					c.setBackground(myColor);
				}else {
					Color myColor = new Color(255,255,255);
					c.setBackground(myColor);
				}
			}
			
			
			return c;
		}
	}
	public void saveActualResponsesAsOracleValues() {
//		if (!hasTests()) {
//			JOptionPane.showMessageDialog(poma, "There are no tests.");
//			return;
//		}
//		this.startProgressStatus();
//		boolean hasOracleValue = false;
//		for (Vector<Object> child : data) {
//			if (!child.get(3).toString().equals("")) {
//				hasOracleValue = true;
//			}
//		}
//		if (!hasOracleValue) {
//			List<TestRecord> recordList = new ArrayList<TestRecord>();
//			for (Vector<Object> child : data) {
//				recordList.add(TestUtil.getTestRecord(child));
//			}
//			PolicyTestSuite testSuite = new PolicyTestSuite(recordList, "GenTests/",this.type);
//			testSuite.writeMetaFile(workingTestSuiteFileName);
//			System.out.println(workingTestSuiteFileName + " saved.");
//			requestTablePanel.validate();
//			requestTablePanel.updateUI();
//			this.testSuite = testSuite;
//		} else {
//			JOptionPane.showMessageDialog(poma, "Oracle values already exist!");
//			return;
//		}
//		this.stopProgressStatus();
	}

	public String getTestsuiteXLSfileName(String testMethod) {
//		File file = poma.getWorkingPolicyFile();
//		String path = file.getParentFile().getAbsolutePath();
//		String name = file.getName();
//		name = name.substring(0, name.length() - 4);
//		path = path + File.separator + "test_suites" + File.separator + name + testMethod + File.separator + name + testMethod + ".xls";
//		return path;
		return null;
	}

	public String getTestOutputDestination(String testMethod) {
//
//		File file = poma.getWorkingPolicyFile();
//		String path = file.getParentFile().getAbsolutePath();
//		String name = file.getName();
//		name = name.substring(0, name.length() - 4);
//		path = path + File.separator + "test_suites" + File.separator + name
//				+ testMethod;
//		return path;
		return null;
	}

	public String getWorkingTestSuiteFileName() {
		return workingTestSuiteFileName;
	}

	public boolean hasTests() {
		return data != null && data.size() > 0;
	}
//	public PolicyTestSuite getPolicyTestSuite(){
//		return testSuite;
//	}
	public boolean hasTestFailure(){
		return hasFailure;
	}
}
