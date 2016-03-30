/*
 * stub.java
 */


//package abcd;


//imports
import java.util.ArrayList;

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

public class stub {

	// we can pre-populate an attributes list, but this reduces our flexibility for datasets of different sizes
	// a more desirable alternative would be to read the attributes list from the top line of the data file
	static ArrayList<String> attributesList = new ArrayList<String>(
					// name of mixture,
					// loci (x15), - should probably remove these during feature creation
					// locus allele counts (x15),
					// locus allele frequencies
					// class
					);



// FastVector is for legacy code use ArrayList instead
	//public static FastVector allAttributes = new FastVector(attributesList.size());

	public static Instances inst_data_training;
	public static Instances inst_data_testing;

	public static void main(String[] args) {

//		initAttributes(attributesList);
//
//		int numFeaturesToUse; // num features to select


		//raw_data_training;
		//raw_data_training = read_from_file(filename);

		//inst_data_training = new Instances("Training data", allAttributes, /*raw_data_training size */);

		// if last element in list
		//inst_data_training.setClassIndex(inst_data_training.numAttributes() - 1);

		// converting data into Instance objects
		//populateInstances(inst_data_training, raw_data_training);


		try {

			//raw_data_testing;
			// raw_data_testing = read_from_file(filename);


			//inst_data_testing = new Instances("Testing data", allAttributes, /*raw_data_testing size*/);

			//inst_data_testing.setClassIndex(inst_data_testing.numAttributes() - 1);

			// converting data into Instance objects
			//populateInstances(inst_data_testing, raw_data_testing);


			// correlation-based feature selection
//			AttributeSelection filter = new AttributeSelection();
//			CfsSubsetEval evaluator = new CfsSubsetEval();
//			BestFirst search = new BestFirst();
//
//			filter.setEvaluator(evaluator);
//			filter.setSearch(search);

			// set filter for selection from training data
//			filter.setInputFormat(inst_data_training);

			// apply filters to data
//			Instances reduced_data_training = Filter.useFilter(inst_data_training, filter);
//			Instances reduced_data_testing = Filter.useFilter(inst_data_testing, filter);
//

			// should print out selected features

			// apply classifier
//			Classifier classifier = (Classifier)new NaiveBayes();
//			classifier.buildClassifier(reduced_data_training);
//			Evaluation evaluator;

			//			 should print out classifier data

			// should print out evaluator data
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
// done
	}

	public static void initAttributes(ArrayList<String> attributes) {
//		for (int i = 0; i < attributes.size() - 1; i++) {
//			allAttributes.addElement(new Attribute(attributes.get(i)));
//		}
//		FastVector stubClass = new FastVector();
//		stubClass.addElement(/*class 1*/);
//		// ...
//		stubClass.addElement(/*class N*/);
//
//        allAttributes.addElement(new Attribute("class",stubClass));
	}

}
