package edu.wright.cs4840.mixturemining;

import java.util.Enumeration;
import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import java.io.IOException;
import java.io.File;

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
		System.out.println("Loading training file: " + trainFile);
		System.out.println("Loading testing file: " + testFile);
		if (trainFile.exists() && testFile.exists()) {
System.out.println("Working Directory = " + System.getProperty("user.dir"));
			try {
				// Read accepts cvs, arff, or xrff file extensions
				trainingData = DataSource.read(trainFile.getCanonicalPath());
				testData = DataSource.read(testFile.getCanonicalPath());
			} catch (Exception exp) {
		        System.err.println("File load failed. Reason: " + exp.getMessage());
			}

			// Set class attribute from first row in file.
			if (trainingData.classIndex() == -1) {
				trainingData.setClassIndex(0);
			}
			if (testData.classIndex() == -1) {
				testData.setClassIndex(0);
			}

			AttributeSelection attrFilter = new AttributeSelection();
			CfsSubsetEval attrEvaluator = new CfsSubsetEval();
			BestFirst search = new BestFirst();

			attrFilter.setEvaluator(attrEvaluator);
			attrFilter.setSearch(search);
			try {
				// set filter for selection from training data
				attrFilter.setInputFormat(trainingData);

				// apply filters to data
				Instances reduced_data_training = Filter.useFilter(trainingData, attrFilter);
				Instances reduced_data_testing = Filter.useFilter(testData, attrFilter);

				// should print out selected features
				Enumeration<Attribute> attributes_training = reduced_data_training.enumerateAttributes();
				while (attributes_training.hasMoreElements()) {
					Attribute attr = attributes_training.nextElement();
					System.out.println(reduced_data_training.attributeStats(attr.index()));
				}
				Enumeration<Attribute> attributes_testing = reduced_data_testing.enumerateAttributes();
				while (attributes_testing.hasMoreElements()) {
					Attribute attr = attributes_testing.nextElement();
					System.out.println(reduced_data_testing.attributeStats(attr.index()));
				}

				// apply classifier
				NaiveBayes classifier = new NaiveBayes();
				classifier.buildClassifier(reduced_data_training);

				// should print out classifier data
				System.out.println(classifier);

				Evaluation evaluator = new Evaluation(reduced_data_testing);

				// should print out evaluator data
				System.out.println(evaluator.rootMeanPriorSquaredError());
			} catch (Exception exp) {
		        System.err.println("Attribute selection filter failed. Reason: " + exp.getMessage());
			}
		}
	}
}
