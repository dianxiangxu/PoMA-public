package POMA.GUI.components;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class MutationBasedTestMutationMethods {
	private JCheckBox boxAAC;
	private JCheckBox boxAAG;
	private JCheckBox boxCAA;
	private JCheckBox boxRAD;
	private JCheckBox boxCAD;
	private JCheckBox boxCOAA;
	private JCheckBox boxCUAA;
	private JCheckBox boxRAC;
	private JCheckBox boxRAG;
	private JCheckBox boxRARA;
	private JCheckBox boxRARAA;
	private JCheckBox boxRNF;
	private JCheckBox boxRPTE;
	private JCheckBox boxAARA;
	//private JCheckBox boxRCCF;
	//private JCheckBox boxPCCF;
	private JCheckBox boxSelectM8;
	private JCheckBox boxSelectAllG;
	private JCheckBox boxAEO;
	private JCheckBox boxCOA;
	private JCheckBox boxCEO;
	private JCheckBox boxCEPC;
	private JCheckBox boxCEPE;
	private JCheckBox boxCEU;
	private JCheckBox boxIAA;
	private JCheckBox boxICA;
	private JCheckBox boxIGA;
	private JCheckBox boxIDA;
	private JCheckBox boxNOC;
	private JCheckBox boxNOF;
	private JCheckBox boxRCA;
	private JCheckBox boxREO;
	private JCheckBox boxREPC;
	private JCheckBox boxREPE;
	private JCheckBox boxREU;
	private JCheckBox boxROA;
	private JCheckBox boxROC;
	private JCheckBox boxROF;
	private JCheckBox boxROB;
	private JCheckBox boxSelectAllO;
	
	private JCheckBox boxCUS;
	private JCheckBox boxAOAR;
	private JCheckBox boxCOAR;
	private JCheckBox boxROAR;
	private JCheckBox boxROP;
	private JCheckBox boxRIS;
	private JCheckBox boxAOC;
	private JCheckBox boxCOC;
	private JCheckBox boxROCT;
	private JCheckBox boxRCT;
	private JCheckBox boxSelectAllP;

	
	public JCheckBox getBoxPTT() {
		return boxAAC;
	}


	public void setBoxPTT(JCheckBox boxPTT) {
		this.boxAAC = boxPTT;
	}


	public JCheckBox getBoxPTF() {
		return boxAAG;
	}


	public void setBoxPTF(JCheckBox boxPTF) {
		this.boxAAG = boxPTF;
	}

	public JCheckBox getBoxCRE() {
		return boxCAA;
	}


	public void setBoxCRE(JCheckBox boxCRE) {
		this.boxCAA = boxCRE;
	}


	public JCheckBox getBoxRER() {
		return boxRAD;
	}


	public void setBoxRER(JCheckBox boxRER) {
		this.boxRAD = boxRER;
	}


	public JCheckBox getBoxRTT() {
		return boxCAD;
	}


	public void setBoxRTT(JCheckBox boxRTT) {
		this.boxCAD = boxRTT;
	}


	public JCheckBox getBoxRTF() {
		return boxCOAA;
	}


	public void setBoxRTF(JCheckBox boxRTF) {
		this.boxCOAA = boxRTF;
	}


	public JCheckBox getBoxCUAA() {
		return boxCUAA;
	}


	public void setBoxCUAA(JCheckBox boxCUAA) {
		this.boxCUAA = boxCUAA;
	}


	public JCheckBox getBoxRCF() {
		return boxRAC;
	}


	public void setBoxRCF(JCheckBox boxRCF) {
		this.boxRAC = boxRCF;
	}


	public JCheckBox getBoxFPR() {
		return boxRAG;
	}


	public void setBoxFPR(JCheckBox boxFPR) {
		this.boxRAG = boxFPR;
	}


	public JCheckBox getBoxFDR() {
		return boxRARA;
	}


	public void setBoxFDR(JCheckBox boxFDR) {
		this.boxRARA = boxFDR;
	}


	public JCheckBox getBoxANF() {
		return boxRARAA;
	}


	public void setBoxANF(JCheckBox boxANF) {
		this.boxRARAA = boxANF;
	}


	public JCheckBox getBoxRNF() {
		return boxRNF;
	}


	public void setBoxRNF(JCheckBox boxRNF) {
		this.boxRNF = boxRNF;
	}


	public JCheckBox getBoxRPTE() {
		return boxRPTE;
	}


	public void setBoxRPTE(JCheckBox boxRPTE) {
		this.boxRPTE = boxRPTE;
	}

	public JCheckBox getBoxCCA() {
		return boxAARA;
	}


	public void setBoxCCA(JCheckBox boxCCA) {
		this.boxAARA = boxCCA;
	}

//	public JCheckBox getBoxRCCF() {
//		return boxRCCF;
//	}
//
//
//	public void setBoxRCCF(JCheckBox boxRCCF) {
//		this.boxRCCF = boxRCCF;
//	}
//
//	public JCheckBox getBoxPCCF() {
//		return boxPCCF;
//	}
//
//	public void setBoxPCCF(JCheckBox boxPCCF) {
//		this.boxPCCF = boxPCCF;
//	}
	
	public JCheckBox getboxSelectAllG() {
		return boxSelectAllG;
	}

	public void setboxSelectAllG(JCheckBox boxSelectAllG) {
		this.boxSelectAllG = boxSelectAllG;
	}
	
	public JCheckBox getBoxAEO() {
		return boxAEO;
	}

	public void setBoxAEO(JCheckBox boxAEO) {
		this.boxAEO = boxAEO;
	}
	
	public JCheckBox getBoxCOA() {
		return boxCOA;
	}

	public void setBoxCOA(JCheckBox boxCOA) {
		this.boxCOA = boxCOA;
	}
	
	public JCheckBox getBoxCEO() {
		return boxCEO;
	}

	public void setBoxCEO(JCheckBox boxCEO) {
		this.boxCEO = boxCEO;
	}
	
	public JCheckBox getBoxCEPC() {
		return boxCEPC;
	}

	public void setBoxCEPC(JCheckBox boxCEPC) {
		this.boxCEPC = boxCEPC;
	}
	
	public JCheckBox getBoxCEPE() {
		return boxCEPE;
	}

	public void setBoxCEPE(JCheckBox boxCEPE) {
		this.boxCEPE = boxCEPE;
	}
	
	public JCheckBox getBoxCEU() {
		return boxCEU;
	}

	public void setBoxCEU(JCheckBox boxCEU) {
		this.boxCEU = boxCEU;
	}
	
	public JCheckBox getBoxIAA() {
		return boxIAA;
	}

	public void setBoxIAA(JCheckBox boxIAA) {
		this.boxIAA = boxIAA;
	}
	
	public JCheckBox getBoxICA() {
		return boxICA;
	}

	public void setBoxICA(JCheckBox boxICA) {
		this.boxICA = boxICA;
	}
	
	public JCheckBox getBoxIGA() {
		return boxIGA;
	}

	public void setBoxIGA(JCheckBox boxIGA) {
		this.boxIGA = boxIGA;
	}
	
	public JCheckBox getBoxIDA() {
		return boxIDA;
	}

	public void setBoxIDA(JCheckBox boxIDA) {
		this.boxIDA = boxIDA;
	}
	
	public JCheckBox getBoxNOC() {
		return boxNOC;
	}

	public void setBoxNOC(JCheckBox boxNOC) {
		this.boxNOC = boxNOC;
	}
	public JCheckBox getBoxNOF() {
		return boxNOF;
	}

	public void setBoxNOF(JCheckBox boxNOF) {
		this.boxNOF = boxNOF;
	}
	
	public JCheckBox getBoxRCA() {
		return boxRCA;
	}

	public void setBoxRCA(JCheckBox boxRCA) {
		this.boxRCA = boxRCA;
	}
	
	public JCheckBox getBoxREO() {
		return boxREO;
	}

	public void setBoxREO(JCheckBox boxREO) {
		this.boxREO = boxREO;
	}
	
	public JCheckBox getBoxREPC() {
		return boxREPC;
	}

	public void setBoxREPC(JCheckBox boxREPC) {
		this.boxREPC = boxREPC;
	}
	
	public JCheckBox getBoxREPE() {
		return boxREPE;
	}

	public void setBoxREPE(JCheckBox boxREPE) {
		this.boxREPE = boxREPE;
	}
	
	public JCheckBox getBoxREU() {
		return boxREU;
	}

	public void setBoxREU(JCheckBox boxREU) {
		this.boxREU = boxREU;
	}
	
	public JCheckBox getBoxROA() {
		return boxROA;
	}

	public void setBoxROA(JCheckBox boxROA) {
		this.boxROA = boxROA;
	}
	
	public JCheckBox getBoxROC() {
		return boxROC;
	}

	public void setBoxROC(JCheckBox boxROC) {
		this.boxROC = boxROC;
	}
	
	public JCheckBox getBoxROF() {
		return boxROF;
	}

	public void setBoxROF(JCheckBox boxROF) {
		this.boxROF = boxROF;
	}
	
	public JCheckBox getBoxROB() {
		return boxROB;
	}

	public void setBoxROB(JCheckBox boxROB) {
		this.boxROB = boxROB;
	}
	
	public JCheckBox getboxSelectAllO() {
		return boxSelectAllO;
	}

	public void setboxSelectAllO(JCheckBox boxSelectAllO) {
		this.boxSelectAllO = boxSelectAllO;
	}
	
	public JCheckBox getBoxCUS() {
		return boxCUS;
	}

	public void setBoxCUS(JCheckBox boxCUS) {
		this.boxCUS = boxCUS;
	}
	
	public JCheckBox getBoxAOAR() {
		return boxAOAR;
	}

	public void setBoxAOAR(JCheckBox boxAOAR) {
		this.boxAOAR = boxAOAR;
	}
	
	public JCheckBox getBoxCOAR() {
		return boxCOAR;
	}

	public void setBoxCOAR(JCheckBox boxCOAR) {
		this.boxCOAR = boxCOAR;
	}
	
	public JCheckBox getBoxROAR() {
		return boxROAR;
	}

	public void setBoxROAR(JCheckBox boxROAR) {
		this.boxROAR = boxROAR;
	}
	
	public JCheckBox getBoxROP() {
		return boxROP;
	}

	public void setBoxROP(JCheckBox boxROP) {
		this.boxROP = boxROP;
	}
	
	public JCheckBox getBoxRIS() {
		return boxRIS;
	}

	public void setBoxRIS(JCheckBox boxRIS) {
		this.boxRIS = boxRIS;
	}
	
	public JCheckBox getBoxAOC() {
		return boxAOC;
	}

	public void setBoxAOC(JCheckBox boxAOC) {
		this.boxAOC = boxAOC;
	}
	
	public JCheckBox getBoxCOC() {
		return boxCOC;
	}

	public void setBoxCOC(JCheckBox boxCOC) {
		this.boxCOC = boxCOC;
	}
	
	public JCheckBox getBoxROCT() {
		return boxROCT;
	}

	public void setBoxROCT(JCheckBox boxROCT) {
		this.boxROCT = boxROCT;
	}
	
	public JCheckBox getBoxRCT() {
		return boxRCT;
	}

	public void setBoxRCT(JCheckBox boxRCT) {
		this.boxRCT = boxRCT;
	}
	
	public JCheckBox getboxSelectAllP() {
		return boxSelectAllP;
	}

	public void setboxSelectAllP(JCheckBox boxSelectAllP) {
		this.boxSelectAllP = boxSelectAllP;
	}
	
	//debug
//	public List<JCheckBox> getAllBoxes() {
//		List<JCheckBox> boxes = new ArrayList<JCheckBox>();
//		boxes.add(boxAAC);
//		boxes.add(boxAAG);
//		boxes.add(boxAARA);
//		boxes.add(boxCAA);
//		boxes.add(boxCAD);
//		boxes.add(boxCOAA);
//		boxes.add(boxCUAA);
//		boxes.add(boxRAC);
//		boxes.add(boxRAD);
//		boxes.add(boxRAG);
//		boxes.add(boxRARA);
//		boxes.add(boxRARAA);
//		//boxes.add(boxRNF);
////		boxes.add(boxRCCF);
////		boxes.add(boxPCCF);
//		//boxes.add(boxRPTE);
//	//	boxes.add(boxSelectM8);
//		boxes.add(boxSelectAllG);
//		return boxes;
//	}


	public MutationBasedTestMutationMethods(){
		boxAAC = new JCheckBox("Add Association With One Access Right(AAC)");
		boxAAG = new JCheckBox("Add An Assignment(AAG)");
		boxAARA = new JCheckBox("Add One Access Right To Association(AARA)");
		boxCAA = new JCheckBox("Change Assignment Ascendent(CAA)");
		boxCAD = new JCheckBox("Change Assignment Direction(CAD)");
		boxCOAA = new JCheckBox("Change Object Attribute of Association(COAA)");
		boxCUAA = new JCheckBox("Change User Attribute of Association(CUAA)");
		boxRAC = new JCheckBox("Remove An Association(RAC)");
		boxRAD = new JCheckBox("Reverse Assignment Direction(RAD)");
		boxRAG = new JCheckBox("Remove An Assignment(RAG)");
		boxRARA = new JCheckBox("Remove One Access Right From Association(RARA)");
		boxRARAA = new JCheckBox("Remove An Access Right From All Associations(RARAA)");
		//boxRNF = new JCheckBox("Remove Not Function (RNF)");
		boxSelectAllG = new JCheckBox("Select All Graph Operators");
		//boxSelectM8 = new JCheckBox("Select Eight(M8)");
		
		boxSelectAllG.addActionListener(new ActionListener() {		 
			@Override
			public void actionPerformed(ActionEvent e) {
				if (boxSelectAllG.isSelected())
		        	setAllIndividualBoxes(true);
		        else
		        	setAllIndividualBoxes(false);			
			}
        });
		
//		boxSelectM8.addActionListener(new ActionListener() {		 
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if (boxSelectM8.isSelected())
//		        	setM8Boxes(true);
//		        else
//		        	setM8Boxes(false);			
//			}
//        });
		
		boxAEO = new JCheckBox("Add Event Operation(AEO)");
		boxCOA = new JCheckBox("Change One Action(COA)");
		boxCEO = new JCheckBox("Change Event Operation(CEO)");
		boxCEPC = new JCheckBox("Change Event Policy Class(CEPC)");
		boxCEPE = new JCheckBox("Change Event Policy Element(CEPE)");
		boxCEU = new JCheckBox("Change Event User(CEU)");
		boxIAA = new JCheckBox("Incorrect Assign Action(IAA)");
		boxICA = new JCheckBox("Incorrect Create Action(ICA)");
		boxIGA = new JCheckBox("Incorrect Grant Action(IGA)");
		boxIDA = new JCheckBox("Incorrect Deny Action(IDA)");
		boxNOC = new JCheckBox("Negate One Condition(NOC)");
		boxNOF = new JCheckBox("Negate One Factor(NOF)");
		boxRCA = new JCheckBox("Remove Conditional Action(RCA)");
		boxREO = new JCheckBox("Remove Event Operation(REO)");
		boxREPC = new JCheckBox("Remove Event Policy Class(REPC)");
		boxREPE = new JCheckBox("Remove Event Policy Element(REPE)");
		boxREU = new JCheckBox("Remove Event User(REU)");
		boxROA = new JCheckBox("Remove One Action(ROA)");
		boxROC = new JCheckBox("Remove One Condition(ROC)");
		boxROF = new JCheckBox("Remove One Factor(ROF)");
		boxROB = new JCheckBox("Remove one OBligation(ROB)");
		boxSelectAllO = new JCheckBox("Select All Obligation Operators");
		boxSelectAllO.addActionListener(new ActionListener() {		 
			@Override
			public void actionPerformed(ActionEvent e) {
				if (boxSelectAllO.isSelected())
		        	setAllIndividualBoxesO(true);
		        else
		        	setAllIndividualBoxesO(false);			
			}
        });
		
		boxCUS = new JCheckBox("Change User Spec(CUS)");
		boxAOAR = new JCheckBox("Add One Access Right(AOAR)");
		boxCOAR = new JCheckBox("Change One Access Right(COAR)");
		boxROAR = new JCheckBox("Remove One Access Right(ROAR)");
		boxROP = new JCheckBox("Remove One Prohibition(ROP)");
		boxRIS = new JCheckBox("Reverse Intersection(RIS)");
		boxAOC = new JCheckBox("Add One Container(AOC)");
		boxCOC = new JCheckBox("Change One Container(COC)");
		boxROCT = new JCheckBox("Remove One Container(ROCT)");
		boxRCT = new JCheckBox("Reverse Complement(RCT)");
		boxSelectAllP = new JCheckBox("Select All Prohibition Operators");
		boxSelectAllP.addActionListener(new ActionListener() {		 
			@Override
			public void actionPerformed(ActionEvent e) {
				if (boxSelectAllP.isSelected())
		        	setAllIndividualBoxesP(true);
		        else
		        	setAllIndividualBoxesP(false);			
			}
        });
	}
	
	public List<String> getMutationOperatorList(boolean filter){
		boolean flag = false;
		List<String> lst = new ArrayList<String>();
		if (boxCAA.isSelected()) {
			if(!filter||!flag)
			lst.add("CAA");
			flag = true;
		}
//		if (boxRPTE.isSelected()) {
//			lst.add("RPTE");
//		}
		if (boxRARAA.isSelected()) {
			if(!filter||!flag)
			lst.add("RARAA");
			flag = true;

		}
		if (boxRAD.isSelected()) {
			if(!filter||!flag)
			lst.add("RAD");
			flag = true;

		}
		if (boxCOAA.isSelected()) {
			
			if(!filter||!flag)
				
			lst.add("COAA");
			flag = true;
		}
		if (boxCUAA.isSelected()) {
			lst.add("CUAA");
		}
		if (boxRAC.isSelected()) {
			if(!filter||!flag)
				
			lst.add("RAC");
			flag = true;

		}
		if (boxRAG.isSelected()) {
			lst.add("RAG");
		}
		if (boxRARA.isSelected()) {
			lst.add("RARA");
		}

//		if (boxRNF.isSelected()) {
//			if(!filter||!flag)
//				
//			lst.add("createRemoveNotFunctionMutants");
//			flag = true;
//
//		}
		
		if (boxAARA.isSelected()) {
			lst.add("AARA"); //+
		}
		if (boxCAD.isSelected()) {
			lst.add("CAD");
		}
//		if (boxRCCF.isSelected()) {
//			lst.add("createRuleChangeComparisonFunctionMutants");
//		}
//		if (boxPCCF.isSelected()) {
//			lst.add("createPolicyTargetChangeComparisonFunctionMutants");
//		}
		
		if (boxAAC.isSelected()) {
			lst.add("AAC");
		}
		if (boxAAG.isSelected()) {
			lst.add("AAG");
		}
		
		return lst;
	}
	
	public List<String> getObligationMutationOperatorList(boolean filter){
		boolean flag = false;
		List<String> lst = new ArrayList<String>();
//		if (boxCAA.isSelected()) {
//			if(!filter||!flag)
//			lst.add("CAA");
//			flag = true;
//		}

		if (boxAEO.isSelected()) {
			lst.add("AEO");
		}
		if (boxCOA.isSelected()) {
			lst.add("COA");
		}
		if (boxCEO.isSelected()) {
			lst.add("CEO");
		}
		if (boxCEPC.isSelected()) {
			lst.add("CEPC");
		}
		if (boxCEPE.isSelected()) {
			lst.add("CEPE");
		}
		if (boxCEU.isSelected()) {
			lst.add("CEU");
		}
		if (boxIAA.isSelected()) {
			lst.add("IAA");
		}
		if (boxICA.isSelected()) {
			lst.add("ICA");
		}
		if (boxIGA.isSelected()) {
			lst.add("IGA");
		}
		if (boxIDA.isSelected()) {
			lst.add("IDA");
		}
		if (boxNOC.isSelected()) {
			lst.add("NOC");
		}
		if (boxNOF.isSelected()) {
			lst.add("NOF");
		}
		if (boxRCA.isSelected()) {
			lst.add("RCA");
		}
		if (boxREO.isSelected()) {
			lst.add("REO");
		}
		if (boxREPC.isSelected()) {
			lst.add("REPC");
		}
		if (boxREPE.isSelected()) {
			lst.add("REPE");
		}
		if (boxREU.isSelected()) {
			lst.add("REU");
		}
		if (boxROA.isSelected()) {
			lst.add("ROA");
		}
		if (boxROC.isSelected()) {
			lst.add("ROC");
		}
		if (boxROF.isSelected()) {
			lst.add("ROF");
		}
		if (boxROB.isSelected()) {
			lst.add("ROB");
		}
		
		return lst;
	}
	
	public List<String> getProhibitionMutationOperatorList(boolean filter){
		boolean flag = false;
		List<String> lst = new ArrayList<String>();
//		if (boxCAA.isSelected()) {
//			if(!filter||!flag)
//			lst.add("CAA");
//			flag = true;
//		}

		if (boxCUS.isSelected()) {
			lst.add("CUS");
		}
		if (boxAOAR.isSelected()) {
			lst.add("AOAR");
		}
		if (boxCOAR.isSelected()) {
			lst.add("COAR");
		}
		if (boxROAR.isSelected()) {
			lst.add("ROAR");
		}
		if (boxROP.isSelected()) {
			lst.add("ROP");
		}
		if (boxRIS.isSelected()) {
			lst.add("RIS");
		}
		if (boxAOC.isSelected()) {
			lst.add("AOC");
		}
		if (boxCOC.isSelected()) {
			lst.add("COC");
		}
		if (boxROCT.isSelected()) {
			lst.add("ROCT");
		}
		if (boxRCT.isSelected()) {
			lst.add("RCT");
		}
		
		return lst;
	}
	
	public void setAllIndividualBoxes(boolean selected) {
		boxAAC.setSelected(selected);
		boxAAG.setSelected(selected);
		boxAARA.setSelected(selected);
		boxCAA.setSelected(selected);
		boxCAD.setSelected(selected);
		boxCOAA.setSelected(selected);
		boxCUAA.setSelected(selected);
		boxRAC.setSelected(selected);
		boxRAD.setSelected(selected);
		
		boxRAG.setSelected(selected);
		boxRARA.setSelected(selected);
		boxRARAA.setSelected(selected);
		//boxRNF.setSelected(selected);
		//boxRPTE.setSelected(selected);
//		boxRCCF.setSelected(selected);
//		boxPCCF.setSelected(selected);
		
		boxSelectAllG.setSelected(selected);
	}
	
	public void setAllIndividualBoxesO(boolean selected) {
		boxAEO.setSelected(selected);
		boxCOA.setSelected(selected);
		boxCEO.setSelected(selected);
		boxCEPC.setSelected(selected);
		boxCEPE.setSelected(selected);
		boxCEU.setSelected(selected);
		boxIAA.setSelected(selected);
		boxICA.setSelected(selected);
		boxIGA.setSelected(selected);
		boxIDA.setSelected(selected);
		boxNOC.setSelected(selected);
		boxNOF.setSelected(selected);
		boxRCA.setSelected(selected);
		boxREO.setSelected(selected);
		boxREPC.setSelected(selected);
		boxREPE.setSelected(selected);
		boxREU.setSelected(selected);
		boxROA.setSelected(selected);
		boxROC.setSelected(selected);
		boxROF.setSelected(selected);
		boxROB.setSelected(selected);

		boxSelectAllO.setSelected(selected);
	}
	
	public void setAllIndividualBoxesP(boolean selected) {
		boxCUS.setSelected(selected);
		boxAOAR.setSelected(selected);
		boxCOAR.setSelected(selected);
		boxROAR.setSelected(selected);
		boxROP.setSelected(selected);
		boxRIS.setSelected(selected);
		boxAOC.setSelected(selected);
		boxCOC.setSelected(selected);
		boxROCT.setSelected(selected);
		boxRCT.setSelected(selected);

		boxSelectAllP.setSelected(selected);
	}
	
//	public void setM8Boxes(boolean selected) {
//		setAllIndividualBoxes(false);
//		boxAAC.setSelected(selected);
//		boxAAG.setSelected(selected);
//		boxAARA.setSelected(selected);
//		boxCAA.setSelected(selected);
//		boxCAD.setSelected(selected);
//		boxCOAA.setSelected(selected);
//		boxCUAA.setSelected(selected);
//		boxRAC.setSelected(selected);
//		
//		
//	}
	

	
	public JPanel createPanel() {
//		setAllIndividualBoxes(true);
//		boxSelectAllG.setSelected(true);
//		setAllIndividualBoxesO(true);
//		boxSelectAllO.setSelected(true);
		
		JPanel graphMutationPanel = new JPanel();
		//graphMutationPanel.setLayout(new GridLayout(13, 4));
		graphMutationPanel.add(new JTextArea("Graph Operators"));
		graphMutationPanel.add(boxAAC);
		graphMutationPanel.add(boxAAG);
		graphMutationPanel.add(boxAARA);
		graphMutationPanel.add(boxCAA);
		graphMutationPanel.add(boxRAD);
		graphMutationPanel.add(boxCAD);
		graphMutationPanel.add(boxCOAA);
		graphMutationPanel.add(boxCUAA);
		graphMutationPanel.add(boxRAC);
		graphMutationPanel.add(boxRAG);
		graphMutationPanel.add(boxRARA);
		graphMutationPanel.add(boxRARAA);
		graphMutationPanel.add(boxSelectAllG);

		JPanel prohibitionMutationPanel = new JPanel();
		prohibitionMutationPanel.add(new JTextArea("Prohibition Operators"));
		prohibitionMutationPanel.add(boxCUS);
		prohibitionMutationPanel.add(boxAOAR);
		prohibitionMutationPanel.add(boxCOAR);
		prohibitionMutationPanel.add(boxROAR);
		prohibitionMutationPanel.add(boxROP);
		prohibitionMutationPanel.add(boxRIS);
		prohibitionMutationPanel.add(boxAOC);
		prohibitionMutationPanel.add(boxCOC);
		prohibitionMutationPanel.add(boxROCT);
		prohibitionMutationPanel.add(boxRCT);
		prohibitionMutationPanel.add(boxSelectAllP);
		
		JPanel obligationMutationPanel = new JPanel();
		obligationMutationPanel.add(new JTextArea("Obligation Operators"));
		obligationMutationPanel.add(new JTextArea(""));
		obligationMutationPanel.add(boxAEO);
		obligationMutationPanel.add(boxCOA);
		obligationMutationPanel.add(boxCEO);
		obligationMutationPanel.add(boxCEPC);
		obligationMutationPanel.add(boxCEPE);
		obligationMutationPanel.add(boxCEU);
		obligationMutationPanel.add(boxIAA);
		obligationMutationPanel.add(boxICA);
		obligationMutationPanel.add(boxIGA);
		obligationMutationPanel.add(boxIDA);
		obligationMutationPanel.add(boxNOC);
		obligationMutationPanel.add(boxNOF);
		obligationMutationPanel.add(boxRCA);
		obligationMutationPanel.add(boxREO);
		obligationMutationPanel.add(boxREPC);
		obligationMutationPanel.add(boxREPE);
		obligationMutationPanel.add(boxREU);
		obligationMutationPanel.add(boxROA);
		obligationMutationPanel.add(boxROC);
		obligationMutationPanel.add(boxROF);
		obligationMutationPanel.add(boxROB);
		obligationMutationPanel.add(boxSelectAllO);

		JPanel mutationPanel = new JPanel();
		
		graphMutationPanel.setLayout(new GridLayout(14, 1));
		prohibitionMutationPanel.setLayout(new GridLayout(14, 1));
		obligationMutationPanel.setLayout(new GridLayout(14, 2));

		mutationPanel.setLayout(new GridLayout(1, 3));
		mutationPanel.add(graphMutationPanel);
		mutationPanel.add(prohibitionMutationPanel);
		mutationPanel.add(obligationMutationPanel);
		mutationPanel.setBorder(new TitledBorder(new EtchedBorder(), ""));
		return mutationPanel;
	}

}
