# LogicSolver

## Develop on 2019.04.15
1.add '>', '<' to Constraints.

2.support precedence.


| Priority | Explanation | Symbol |

|1|plus,minus(didn't support, so we can't use negative number. if you want to. you can use '0-a' instead 'a')|+,-|
|2|muliply,divide| A*A, A/A|
|3|add, subtract | A+A, A-A|
|4|relational comparasion, scan from left to right| A>A, A<A, A=A|
|5|logical NOT | NOT A|
|6|logical AND | A AND A|
|7|logical OR | A OR A|
|8|IF, IFF | A IF A, A IFF A||

## Develop on 2019.04.15
1.add 'IF', 'IFF' to Sorts.

2.add 'nat' to Vocabulary.

## Master on 2019.04.14
The current LogicSolver can figure out:
### 1.Sort:

1.1 'enum' can used, but 'nat' still didn't finish.

### 2.Vocabulary:

2.1 function can used , it can add with mulitpile inputs and {'all_different'}.

### 3.Constraint:

3.1 support 'AND','OR','XOR','NOT','&','|','!','=' and function, but it is bracket strict. you should add the bracket to make sure the brackets has no wrong.

3.2 it should be notice that function(a,b) should be written ad function(((a),(b))).

3.3 The following coding will be written in develop branch.
