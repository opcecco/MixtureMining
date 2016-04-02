require 'Getopt/long'

require_relative 'read_freqs.rb'

opt = Getopt::Long.getopts(
  ['--infile', '-i', Getopt::REQUIRED],
  ['--outfile', '-o', Getopt::REQUIRED],
  ['--aftable', '-a', Getopt::OPTIONAL], # allele frequency table for outputting sum of allele frequencies per locus
  ['--ac', '-c', Getopt::BOOLEAN], # output number of alleles per locus in ordered columns
)

samples_file = File.open(opt['infile'])
ac = opt['ac']
freq_file = (opt['aftable'] || false)
af = freq_file ? true : false

begin
  lines = samples_file.readlines.map{|line| line.chomp}
rescue Exception => e
  puts "Failure reading file: #{e}"
ensure
  samples_file.close if samples_file
end

# lines.shift

loci = lines.shift.split(',').drop(2)

samples = Array.new
sample_allele_counts = Array.new
sample_allele_freqs = Array.new

lines.each {|line|
  vals = line.split(',')
  vals.shift
  vals.shift
  samples.push vals
}

if ac
  sample_allele_counts = samples.map{|genotype| genotype.map{|locus| locus.split(' ').size}}
end

# puts "sample allele counts: "
# p sample_allele_counts

if af
  freqs = read_freqs(freq_file)

  sample_allele_freqs = samples.map{|genotype| 
    genotype.map.with_index {|alleles, locus_index| 
      alleles.split(' ').map{|allele| 
        freqs[loci[locus_index]][allele]
      }.reduce(:+)
    }
  }
end

# write to file in overwrite mode
puts "Writing file: #{opt['outfile']}"

begin
  outfile = File.open(opt['outfile'], 'w')
  outfile.write 'name,N,' + loci.join(',')

  if ac
    outfile.write ',' + loci.map{|locus| locus + " allele count"}.join(',')
  end

  if af
    outfile.write ',' + loci.map{|locus| locus + " sum allele freq"}.join(',')
  end

  outfile.write "\n"

  lines.each_with_index { |line, line_index|
    outfile.write line
    if ac
      outfile.write ',' + sample_allele_counts[line_index].join(',')
    end

    if af
      outfile.write ',' + sample_allele_freqs[line_index].join(',')
    end
    outfile.write "\n"
  }
rescue Exception => e
  puts "Failure reading file: #{e}"
ensure
  outfile.close if outfile
end

puts "File writing complete."