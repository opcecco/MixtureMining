require 'set'

require 'Getopt/long'

require_relative 'recurse.rb'
require_relative 'n_combinations.rb'

opt = Getopt::Long.getopts(
  ['--infile', '-i', Getopt::REQUIRED],
  ['--outfile', '-o', Getopt::REQUIRED],
  ['--seed', '-s', Getopt::OPTIONAL], # random seed
  ['--per', '-p', Getopt::REQUIRED], # number of samples to include per mixture
  ['--mixtures', '-m', Getopt::OPTIONAL],
)

samples_file = File.open(opt['infile'])
outfile_name = opt['outfile']
samples_per_mix = opt['per'].to_i
seed = opt['seed'].to_i || false


begin
  lines = samples_file.readlines.map{|line| line.chomp}
rescue Exception => e
  puts "Failure reading file: #{e}"
ensure
  samples_file.close if samples_file
end

loci = lines.shift.split(',').drop(1)
loci_names = loci.uniq

samples = Array.new
sample_names = Array.new

lines.each {|line|
  vals = line.split(',')
  sample_names.push vals.shift
  samples.push vals
}

if seed
  rand = Random.new(seed)
else
  rand = Random.new
  seed = rand.seed
end

# max possible combinations: (n! / (n-r + 1)! ) / r!
max_combinations = n_combinations(samples.size, samples_per_mix)

num_mixtures = (opt['mixtures'].to_i || max_combinations)

if num_mixtures > max_combinations
  abort("Requested number of mixtures greater than number of possible combinations")
end

if num_mixtures > 10000
  abort("Don't run with more than 10,000 mixtures for now.")
end

random_numbers = Set.new

# if performance is terrible, replace with binary search tree
while random_numbers.size < num_mixtures do
  random_numbers.add rand(max_combinations)
end

# p random_numbers

sorted_rand = random_numbers.to_a.sort

puts "Random numbers completed."

random_mixtures = Array.new

# parallel arrays
mixtures = Array.new
mixture_names = Array.new


start_val = 0
end_val = samples.size - 1
goal = 0

sorted_rand.each {|new_rand|
  result = recurse(0, end_val, new_rand, samples_per_mix)
  random_mixtures.push result['mixture']
}

def make_mixture(genotype_list, to_mix)
  new_mix = Array.new

  locus_count = genotype_list[0].size / 2

  (0...locus_count).each { |locus_index|

    # use Set to avoid duplicate alleles - we only care about unique alleles
    locus_alleles = Set.new

    # assumes one allele per column; two columns per locus;
    # add both allele columns to the locus set
    to_mix.each { |genotype_index|
      locus_alleles.add genotype_list[genotype_index][locus_index * 2]
      locus_alleles.add genotype_list[genotype_index][locus_index * 2 + 1]
    }

    # return an array rather than a set, but sort it first for readability
    new_mix.push locus_alleles.to_a.sort{ |a, b| a.to_f <=> b.to_f }
  }

  return new_mix

end #make_mixture

# make mixtures and names in parallel arrays
random_mixtures.each { |list_to_mix|
  
  # making mixtures
  new_mix = make_mixture(samples, list_to_mix)
  mixtures.push new_mix

  # making unique names for mixtures - intended for 1-column-wide in a CSV file, so no commas
  mixture_names.push list_to_mix.map{|i| sample_names[i]}.join('+')
}

# write to file in overwrite mode
puts "Writing file: #{opt['outfile']}"

begin
  outfile_already_exists = File.file?(outfile_name)

  outfile = File.open(outfile_name, 'a')
  outfile_seedfile = File.open(outfile_name + '.seed.txt','a')
  outfile_seedfile.puts "Seed: #{seed}"
  
  unless outfile_already_exists
    outfile.puts 'name,N,' + loci_names.join(',') + ','
  end

  mixtures.each_with_index { |mixed_genotype, mixture_index|
    outfile.puts mixture_names[mixture_index] + ',' + samples_per_mix.to_s + 'p,' + mixed_genotype.map{|locus| locus.join(' ')}.join(',')
  }
rescue Exception => e
  puts "Failure writing files: #{e}"
ensure
  outfile.close if outfile
  outfile_seedfile.close if outfile_seedfile
end