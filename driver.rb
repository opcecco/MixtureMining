# Assumptions/requirements:
#   Getopt gem installed for Ruby. If not, install with "> gem install getopt"
#   .\edu\wright\cs4840\mixturemining\MMApp.class
#   .\weka.jar
#   preprocessing\ contains preprocessing ruby files
#   preprocessing\single_source\361_caucasian_identifiler.csv contains genotypes
#   preprocessing\frequencies\361_cau.csv contains allele frequencies

SINGLESOURCE = "preprocessing/single_source/361_caucasian_identifiler_loci.csv"
FREQUENCIES = "preprocessing/frequencies/361_cau.csv"
JAVA_CALL = "java -jar MixtureMining.jar"

puts "Usage: > ruby driver.rb min max mixture_count [-n features] [-c BS] [-f AS | PC]"

min = ARGV[0]
max = ARGV[1]
mixture_count = ARGV[2]

p ARGV
java_args = (3...ARGV.size).map{|i| ARGV[i]}.join(" ")

mix_base_filename = "#{min}-#{max}_#{mixture_count}ea"

testing_filename = String.new
training_filename = String.new

[false, true].each {|testing|
  mix_filename = mix_base_filename
  
  if testing
    mix_filename += "_testing.csv"
    testing_filename = mix_filename + ".processed.csv"
  else
    mix_filename += "_training.csv"
    training_filename = mix_filename + ".processed.csv"
  end

  (min..max).each {|index|
    to_eval = "ruby preprocessing/mix_gen.rb -i #{SINGLESOURCE} -o #{mix_filename} -p #{index} -m #{mixture_count}"
    puts "Running: '#{to_eval}'"
    output = `#{to_eval}`
    puts output
  }

  file_to_generate = mix_filename + ".processed.csv"

  to_eval = "ruby preprocessing/locus_info.rb -i #{mix_filename} -o #{file_to_generate} -a #{FREQUENCIES} -c"
  puts "Running: '#{to_eval}'"
  output = `#{to_eval}`
  puts output
}

to_eval = "#{JAVA_CALL} #{training_filename} #{testing_filename} #{java_args}"
puts "Running: '#{to_eval}'"
`#{to_eval}`
output = `#{to_eval}`
puts output