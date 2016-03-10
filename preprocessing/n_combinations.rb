# max possible combinations: (n! / (n-r + 1)! ) / r!
def n_combinations(n, r)
  ((n - r + 1)..n).reduce(:*) / (1..r).reduce(:*)
end