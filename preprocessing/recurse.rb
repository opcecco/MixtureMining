require_relative 'ncombinations.rb'

def recurse(start_val, end_val, goal, remaining)
  puts "recurse(#{start_val}, #{end_val}, #{goal}, #{remaining})"
  if goal <= 0
    abort("illegal combination requested - zero or negative")
  elsif goal > n_combinations(end_val + 1, remaining)
    abort("Illegal combination requested - too high")
  end

  new_mix = []

  while true
    # puts "goal: #{goal}"
    if remaining == 1
      new_mix.push (start_val + goal - 1)
      return {"start" => start_val, "mixture" => new_mix, "passed" => goal}
    end

    poss_comb = n_combinations(end_val - start_val, remaining - 1)
    # puts "comb: #{poss_comb}"

    if goal <= poss_comb
      new_mix.push start_val
      # puts "mix: #{new_mix}"
      new_mix.concat recurse(start_val + 1, end_val, goal, remaining - 1)['mixture']
      # puts "mix: #{new_mix}"
      return {"start" => start_val, "mixture" => new_mix, "passed" => goal}
    else
      goal -= poss_comb
      start_val += 1
    end
  end

  return {"start" => start_val, "mixture" => new_mix, "passed" => goal}
end