# MixtureMining
Final project for Wright State University CS4840/6840, Intro to Machine Learning, Spring 2016

## Authors
* Nathaniel Adams, adams.201@wright.edu
* Oliver Ceccopieri, ceccopieri.2@wright.edu
* Nathan Jent, jent.2@wright.edu

## License
TODO

## About

The MixtureMining project is intended to explore methods for estimating/inferring the number of contributors present in mixed DNA samples. This project includes:
* Preprocessing
  * sample genotypes for use in simulating mixed samples (see [Example data description below](#example-data))
  * a genotype "mixing" program for generating simulated mixed samples (mix_gen.rb)
  * a feature extraction/creation program for real or simulated mixed samples (locus_info.rb)
* Feature filtering
TODO
* Estimation
TODO

## System requirements
* [Ruby interpreter](https://www.ruby-lang.org/en/)
  * Required Gems (install using 'gem install <gem_name>')
    * getopt
* Java JDK
TODO: Version number and any necessary libraries

## Build instructions
* Preprocessing:
None (pure Ruby)
* Filtering/estimation:
TODO

## Run instructions

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
  * **NOTE: _Currently requires manual "stitching" together of multiple mixture files. Remove seed value and all but top-most header from files when stitching together._**
  2.1 Allele frequency feature creation: uses [--aftable aftable.csv] flag, requires allele frequency table to be passed to script.
  2.2 Allele counting feature creation: uses [--ac] flag
```
> ruby locus_info.rb --infile ./path_to/mixture_output.csv --outfile ./path_to/preprocessed_mixtures.csv [--aftable ./path_to/allele_frequencies_table.csv] [--ac]
```
Ex.
```
> ruby locus_info.rb --infile mixtures/361_cau_id_mix_2_3_4_1000each.csv --outfile mixes_preprocessed/361_cau_id_mix_2_3_4_1000each_preprocessed.csv --aftable frequencies/361_cau.csv --ac
```

### Filtering
TODO

### Model training
TODO

### Testing
TODO

## Notes

### Weka
TODO

### Example data
Example data taken from NIST genotype dataset and accompanying allele frequencies, available at http://www.cstl.nist.gov/strbase/NISTpop.htm

### Dev environments:
* Windows 8.1
  * ruby 2.2.4p230 (x64-mingw32)
  * Java 1.8.0_71 64-bit