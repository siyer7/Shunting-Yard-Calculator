Siddhant Iyer
Net ID: siyer7
siyer7@u.rochester.edu

I have provided appropriate comments along with my any code that may be indecipherable. 
I first trim the input string, to rid of trailing and leading spaces.
Then, I want to remove the spaces from the middle, but while doing so< I check for an ill formed expression.

Then I consider 1 character at a time, and if it is an operator or a variables, I have to further run a second loop which concatenates consecutive digits or letters, if any, to form the whole number/variable name.
I've used 1 stack each for numbers and operators, and I use the shunting yard algorithm to compute the values.
I've used a map to store the variables.
If an asignment occurs, I don't print anything.
The program runs till user inputs "exit".