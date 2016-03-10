package edu.wright.cs4840.mixturemining;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class MMApp {

  public static final long serialVersionUID = 1L;
  public static void main(String[] args) {
    try {
      DataSource source = new DataSource("/some/where/data.arff");
      Instances data = source.getDataSet();
      // setting class attribute if the data format does not provide this information
      // For example, the XRFF format saves the class attribute information as well
      if (data.classIndex() == -1) {
        data.setClassIndex(data.numAttributes() - 1);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
