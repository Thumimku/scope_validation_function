# scope_validation_function


 * This is a custom function to return a boolean that indicates the set of scope requested for token issuance
 * is sub set of allowed scopes. Even if one scope if requested out of allowed scopes function will deny.
 
 * This function consumes two input values.
 
  1. The list of scopes requested (Bag of Strings)
  2. Comma separated allowed scopes (String)
 
 * This function returns the list of departments (Bag of Strings)
 