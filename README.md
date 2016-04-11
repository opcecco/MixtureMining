# MixtureMining
Final project for Wright State University CS4840/6840, Intro to Machine Learning, Spring 2016

## Authors
* Nathaniel Adams, adams.201@wright.edu
* Oliver Ceccopieri, ceccopieri.2@wright.edu
* Nathan Jent, jent.2@wright.edu

## License
GNU GPL v3 (see separate license file). Weka, which this work references, is also licensed under GNU GPL v3.

## About

The MixtureMining project is intended to explore methods for estimating/inferring the number of contributors present in mixed DNA samples. This project includes:
* Preprocessing
  * sample genotypes for use in simulating mixed samples (see [Example data description below](#example-data))
  * a genotype "mixing" program for generating simulated mixed samples (mix_gen.rb)
  * a feature extraction/creation program for real or simulated mixed samples (locus_info.rb)
* Feature filtering
  * Utilizes forward feature selector, backwards feature selector, or principle components
* Estimation
  * Utilizes a naive Bayesian classifier for prediction

## System requirements
* [Ruby interpreter](https://www.ruby-lang.org/en/)
  * Required Gems (install using 'gem install <gem_name>')
    * getopt
* JRE 1.8+ (for running)
* JDK 1.8+ (for building)
* Apache Ant (for building)


## Build instructions
* Preprocessing:
None (pure Ruby)
* Filtering/estimation:
  * Use the "build" feature in the provided Ant build.xml file.
  * Internet connection required for downloading Weka.

## Run instructions
* Build the JAR file, then run the command:
```
> ruby driver.rb min_contributors max_contributors mixtures_per_class -f [AS/ASB/PC] -n features_to_keep -c BS'
```

### Preprocessing
*All paths relative to ./preprocessing/*

1. Mixture simulation 
  * If your mixtures already exist in the proper format (see ./preprocessing/mixtures for an example), proceed to step #2
```
> ruby mix_gen.rb --infile ./path_to/genotypes.csv --outfile ./path_to/mixture_output.csv --per num_samples_per_mix --mixtures num_mixture_to_make [--seed PRNG_seed_value]
```
Ex.
```
> ruby mix_gen.rb --infile single_source/361_caucasian_identifiler_loci.csv --outfile mixtures/361_cau_id_2_mix_500.csv --per 2 --mixtures 500
```

2. Feature creation
  2.1 Allele frequency feature creation: uses [--aftable aftable.csv] flag, requires allele frequency table to be passed to script.
  2.2 Allele counting feature creation: uses [--ac] flag
```
> ruby locus_info.rb --infile ./path_to/mixture_output.csv --outfile ./path_to/preprocessed_mixtures.csv [--aftable ./path_to/allele_frequencies_table.csv] [--ac]
```
Ex.
```
> ruby locus_info.rb --infile mixtures/361_cau_id_mix_2_3_4_1000each.csv --outfile mixes_preprocessed/361_cau_id_mix_2_3_4_1000each_preprocessed.csv --aftable frequencies/361_cau.csv --ac
```

### Filtering and estimation
```
> java -jar MixtureMining.jar training_file test_file -f [AS/ASB/PC] -n features_to_keep -c BS
```

### Example data
Example data taken from NIST genotype dataset and accompanying allele frequencies, available at http://www.cstl.nist.gov/strbase/NISTpop.htm

### Dev environments:
* Windows 8.1
  * ruby 2.2.4p230 (x64-mingw32)
  * Java 1.8.0_71 64-bit