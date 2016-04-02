package edu.wright.cs4840.mixturemining;

import java.io.File;
import java.util.Enumeration;

import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

public class MMApp {

	public static final long serialVersionUID = 1L;
	private static Instances trainingData;
	private static Instances testData;

	public static void main(String[] args) {

		for (String arg : args) {
			System.out.println(arg);
		}

		File trainFile = new File(args[0]);
		File testFile = new File(args[1]);
		int numToSelect = Integer.parseInt(args[2]);
		
		System.out.println("Loading training file: " + trainFile.getPath());
		System.out.println("Loading testing file: " + testFile.getPath());
		if (trainFile.exists() && testFile.exists()) {
System.out.println("Working Directory = " + System.getProperty("user.dir"));
			try {
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

			AttributeSelection attrFilter = new AttributeSelection();
			CfsSubsetEval attrEvaluator = new CfsSubsetEval();
			
			GreedyStepwise search = new GreedyStepwise();
			
			search.setNumToSelect(numToSelect);
			search.setConservativeForwardSelection(false);
			search.setSearchBackwards(false);
			search.setGenerateRanking(true);
			
			attrFilter.setEvaluator(attrEvaluator);
			attrFilter.setSearch(search);
			try {
				// set filter for selection from training data
				attrFilter.setInputFormat(trainingData);

				// apply filters to data
				Instances reduced_data_training = Filter.useFilter(trainingData, attrFilter);
				Instances reduced_data_testing = Filter.useFilter(testData, attrFilter);

				// should print out selected features
				System.out.println(reduced_data_training.toSummaryString());
//				Enumeration<Attribute> attributes_training = reduced_data_training.enumerateAttributes();
//				while (attributes_training.hasMoreElements()) {
//					Attribute attr = attributes_training.nextElement();
//					System.out.println(reduced_data_training.attributeStats(attr.index()));
//				}

				// apply classifier
				NaiveBayes classifier = new NaiveBayes();
				classifier.buildClassifier(reduced_data_training);

				// should print out classifier data
				System.out.println(classifier);

				Evaluation evaluator = new Evaluation(reduced_data_training);
				evaluator.evaluateModel(classifier, reduced_data_testing);

					
					// should print out evaluator data
				System.out.println(evaluator.toSummaryString());
			} catch (Exception exp) {
		        System.err.println("Attribute selection filter failed. Reason: " + exp.getMessage());
			}
		}
	}
}
