def read_freqs(filename)
  freqs = Hash.new {|hash, key| hash[key] = Hash.new}

  lines = Array.new
  
  freqs_file = File.open(filename)
  
  begin
    lines = freqs_file.readlines.map{|line| line.chomp}
  rescue Exception => e
    puts "Failure reading file: #{e}"
  ensure
    freqs_file.close if freqs_file
  end

  loci = lines.shift.split(',').drop(1)

  lines.each {|line|
    vals = line.split(',')
    allele = vals.shift
    vals.each_with_index {|val,index|
      if val != ""
        freqs[loci[index]][allele] = val.to_f
      end
    }
  }

  freqs.each_pair{|key,val|
    puts "#{key} => [#{val.keys.sort{|a,b| a.to_f <=> b.to_f}.join(',')}]"
  }
  return freqs
end