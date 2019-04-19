

/**
 * User: u6613739
 * Date: 2019/4/3
 * Time: 23:59
 * Description:
 */
public class MyTest
{
    public static void main(String[] args)
    {
        //normalTest();
        //natTest();
        //newConstraintTest();
        //innerFunctionTest();
        //NOTTest();
        IntegerInequalitiesTest();
    }
    public static void normalTest()
    {
        String sortString="plane enum: A,B.\n" + "size enum: small,big.\n" + "color enum: red,blue.";
        String vocabularyString="function getplane(size,color):plane.\n" + "function getsize(plane):size{all_different}.\n" + "function getcolor(plane):color.";
        String constraintString="(getplane(((getsize((B))),(getcolor((B))))))=(B).";
        LogicSolver.solveLogicPuzzle(sortString,vocabularyString,constraintString);
    }
    public static void natTest()
    {
        String sortString="plane enum: A,B.";
        String vocabularyString="function indexof(plane): nat.";
        String constraintString="";
        LogicSolver.solveLogicPuzzle(sortString,vocabularyString,constraintString);
    }
    public static void newConstraintTest()
    {
        String sortString="plane enum: A,B.\n" + "size enum: small,big.\n" + "color enum: red,blue.";
        String vocabularyString="function getplane(size,color):plane.\n" + "function getsize(plane):size{all_different}.\n" + "function getcolor(plane):color.";
        String constraintString=" 3+4*(1-3)/(((4)))=111.";
        LogicSolver.solveLogicPuzzle(sortString,vocabularyString,constraintString);
    }

    public static void innerFunctionTest()
    {
        String sortString="plane enum: A,B.\n" + "size enum: small,big.\n" + "color enum: red,blue.";
        String vocabularyString="function getplane(size,color):plane.\n" + "function getsize(plane):size{all_different}.\n" + "function getcolor(plane):color.";
        String constraintString="getplane(getsize(A),getcolor(A))=A.";
        LogicSolver.solveLogicPuzzle(sortString,vocabularyString,constraintString);
    }
    public static void NOTTest()
    {
        String sortString="plane enum: A.\n" +
                "size enum: small,big.";
        String vocabularyString="function getsize(plane):size{all_different}.";
        String constraintString="NOT(getsize(A)=big).";
        LogicSolver.solveLogicPuzzle(sortString,vocabularyString,constraintString);
    }
    public static void IntegerInequalitiesTest()
    {
        String sortString="child enum:A,B,C,D.";
        String vocabularyString="function age(child):nat.";
        String constraintString="age(A) > 0.\n" +
                "age(B) < 7.\n" +
                "age(C) > 3.\n" +
                "(age(D) > age(A)) AND (age(D) < 4).\n" +
                "age(C) + age(D) > age(B).\n" +
                "age(A) + age(C) < age(B).";
        LogicSolver.solveLogicPuzzle(sortString,vocabularyString,constraintString);
    }
}
