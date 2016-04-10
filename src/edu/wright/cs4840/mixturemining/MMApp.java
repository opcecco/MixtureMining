package edu.wright.cs4840.mixturemining;

import java.awt.BorderLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFrame;

import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.PrincipalComponents;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.ThresholdVisualizePanel;

public class MMApp {

	public static final long serialVersionUID = 1L;
	private static Instances trainingData;
	private static Instances testData;

	public static void main(String[] args) {

		// Handle command line options
		File trainFile = null;
		File testFile = null;
		boolean showCurve = false;
		boolean kernelEstimator = false;
		int numAttributes = 10;
		String filter = "AS";
		String classer = "BS";
		if (args.length == 0) {
			args = new String[] {"-help"};
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("-")) {
				if (args[i].equals("-help") || args[i].equals("-h") || args[i].equals("-?")) { 
					System.out.println(
							"Specify training and testing files: ex. java -jar MixtureMining.jar train.csv test.csv\n"
							+ "-f : specify filter { AS : Attribute Selection, PC : Principle Component analysis }\n"
							+ "-n : specify number of attributes to retain\n"
							+ "-c : specify classifier { BS : Naive Bayes, }");
					return;
				} else if (args[i].equals("-f")) { 
					filter = args[++i];
				} else if (args[i].equals("-c")) {
					classer = args[++i];
				} else if (args[i].equals("-n")) {
					numAttributes = Integer.parseInt(args[++i]);
				} else if (args[i].equals("-showcurve")) {
					showCurve = true;
				} else if (args[i].equals("-k")) {
					kernelEstimator = true;
				}
			} else {
				trainFile = new File(args[i++]);
				testFile = new File(args[i]);
			}
		}
		if (trainFile == null || testFile == null) {
			System.err.println("Please specify both training and testing files. ex. java -jar MixtureMining.jar train.csv test.csv");
			return;
		}
		try(FileWriter fw = new FileWriter("results.txt", true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter results = new PrintWriter(bw))
		{
			System.out.println("Loading training file: " + trainFile.getPath());
			System.out.println("Loading testing file: " + testFile.getPath());
			if (trainFile.exists() && testFile.exists()) {
				try {
					//Print file name for mixture information
					results.println(trainFile.getName());
					
					// Read accepts cvs, arff, or xrff file extensions
					trainingData = DataSource.read(trainFile.getPath());
					testData = DataSource.read(testFile.getPath());
				} catch (Exception exp) {
			        System.err.println("File load failed. Reason: " + exp.getMessage());
				}
	
				// Set class attribute from first row in file.
				if (trainingData.classIndex() == -1) {
					trainingData.setClassIndex(1);
				}
				if (testData.classIndex() == -1) {
					testData.setClassIndex(1);
				}
				Instances reduced_data_training = null;
				Instances reduced_data_testing = null;
				
				// Feature selection
				if (filter.equalsIgnoreCase("PC")) {
					PrincipalComponents pcaFilter = new PrincipalComponents();
					pcaFilter.setMaximumAttributes(numAttributes);
					try {
						pcaFilter.setInputFormat(trainingData);
						reduced_data_training = Filter.useFilter(trainingData, pcaFilter);
						reduced_data_testing = Filter.useFilter(testData, pcaFilter);
					} catch (Exception exp) {
				        System.err.println("PCA filter failed. Reason: " + exp.getMessage());
					}
				} else if (filter.startsWith("AS")){
	 				AttributeSelection attrFilter = new AttributeSelection();
					CfsSubsetEval attrEvaluator = new CfsSubsetEval();
					
					GreedyStepwise search = new GreedyStepwise();
					
					search.setNumToSelect(numAttributes);
					search.setConservativeForwardSelection(false);
					
					if (filter.equalsIgnoreCase("ASB"))
						search.setSearchBackwards(true);
					else
						search.setSearchBackwards(false);
					
					search.setGenerateRanking(true);
					
					attrFilter.setEvaluator(attrEvaluator);
					attrFilter.setSearch(search);
					try {
						// set filter for selection from training data
						attrFilter.setInputFormat(trainingData);
		
						// apply filters to data
						reduced_data_training = Filter.useFilter(trainingData, attrFilter);
						reduced_data_testing = Filter.useFilter(testData, attrFilter);
							
					} catch (Exception exp) {
				        System.err.println("Attribute selection filter failed. Reason: " + exp.getMessage());
					}
				}
				// write results of feature selection
				results.println("Num attributes: " + reduced_data_training.numAttributes());
//				results.println(reduced_data_training.toSummaryString());
				
	//				Enumeration<Attribute> attributes_training = reduced_data_training.enumerateAttributes();
	//				while (attributes_training.hasMoreElements()) {
	//					Attribute attr = attributes_training.nextElement();
	//					System.out.println(reduced_data_training.attributeStats(attr.index()));
	//				}
					
				Classifier classifier = null;
				if (classer.equalsIgnoreCase("L")) {
					
				} else if (classer.equalsIgnoreCase("BS") || classer == null) {
					classifier = new NaiveBayes();
					((NaiveBayes) classifier).setUseKernelEstimator(kernelEstimator);
					try {
						classifier.buildClassifier(reduced_data_training);
					} catch (Exception exp) {
				        System.err.println("Bayes classification failed. Reason: " + exp.getMessage());
					}
				}
	
//				results.println(classifier);
	
				Evaluation evaluator = null;
				try {
					evaluator = new Evaluation(reduced_data_training);
					evaluator.evaluateModel(classifier, reduced_data_testing);
//					results.println(evaluator.toClassDetailsString());
//					results.println(evaluator.toMatrixString());
//					results.println(evaluator.toSummaryString());
					for (int j = 0; j < reduced_data_testing.numClasses(); j++) {
						results.println(reduced_data_testing.classAttribute().value(j) 
								+ " "
								+ evaluator.truePositiveRate(j)
								+ "% true positive rate");
					}
					if (showCurve) {
						// generate curve
					    ThresholdCurve tc = new ThresholdCurve();
					    int classIndex = 2;
					    Instances curve = tc.getCurve(evaluator.predictions(), classIndex);
	
					    // plot curve
					    ThresholdVisualizePanel tvp = new ThresholdVisualizePanel();
					    tvp.setROCString("(Area under ROC = " + 
						Utils.doubleToString(ThresholdCurve.getROCArea(curve), 4) + ")");
					    tvp.setName(curve.relationName());
					    PlotData2D plotdata = new PlotData2D(curve);
					    plotdata.setPlotName(curve.relationName());
					    plotdata.addInstanceNumberAttribute();
					    // specify which points are connected
					    boolean[] cp = new boolean[curve.numInstances()];
					    for (int n = 1; n < cp.length; n++)
					      cp[n] = true;
					    plotdata.setConnectPoints(cp);
					    // add plot
					    tvp.addPlot(plotdata);
	
					    // display curve
					    final JFrame jf = new JFrame("WEKA ROC: " + tvp.getName());
					    jf.setSize(500,400);
					    jf.getContentPane().setLayout(new BorderLayout());
					    jf.getContentPane().add(tvp, BorderLayout.CENTER);
					    jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					    jf.setVisible(true);
					}
				} catch (Exception exp) {
			        System.err.println("Evaluation failed. Reason: " + exp.getMessage());
				}
			}
		} catch (IOException e) {
	        System.err.println("File write failed. Reason: " + e.getMessage());
		}
	}
}
