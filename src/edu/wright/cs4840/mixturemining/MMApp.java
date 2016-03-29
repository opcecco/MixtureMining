package edu.wright.cs4840.mixturemining;

import java.util.Enumeration;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
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

public class MMApp {

	public static final long serialVersionUID = 1L;
	private static Instances trainingData;
	private static Instances testData;

	public static void main(String[] args) {

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(loadOptions(), args);
		} catch (ParseException exp) {
	        System.err.println("Parsing failed. Reason: " + exp.getMessage());
		}
		System.out.println("Command: " + cmd);
		if (cmd.hasOption(Ops.DATA.getName())) {
			String trainFileName = Ops.DATA.op.getValue(1);
			String testFileName = Ops.DATA.op.getValue(2);
			System.out.println("Loading files: " + trainFileName + ' ' + testFileName);
			try {
				// Read accepts cvs, arff, or xrff file extensions
				trainingData = DataSource.read(trainFileName);
				testData = DataSource.read(testFileName);
			} catch (Exception exp) {
		        System.err.println("File load failed. Reason: " + exp.getMessage());
			}
		}

		// Set class attribute from first row in file.
		if (trainingData.classIndex() == -1) {
			trainingData.setClassIndex(0);
		}
		if (testData.classIndex() == -1) {
			testData.setClassIndex(0);
		}

		if (cmd.hasOption(Ops.FILTER.getName())) {
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

	private enum Ops {
		DATA(Option.builder("d").argName("trainingfile testfile" ).hasArg()
				.desc("load training and test data files").numberOfArgs(2)
				.longOpt("loaddata").build()),
		FILTER(Option.builder("f").hasArg()
				.desc("filter setting: bestfirst, bestlast")
				.longOpt("filter").build());

		private Option op;

		Ops(Option op) {
			this.op = op;
		}

		public String getName() {
			return op.getOpt();
		}
	}

	/**
	 * @return the options for the application
	 */
	private static Options loadOptions() {
		Options ops = new Options();
		ops.addOption(Ops.DATA.op);
		ops.addOption(Ops.FILTER.op);
		return ops;
	}

}
