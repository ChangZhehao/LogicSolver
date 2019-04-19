

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
        newConstraintTest();

    }
    public static void normalTest()
    {
        String sortString="plane enum: A,B.\n" + "size enum: small,big.\n" + "color enum: red,blue.";
        String vocabularyString="function getplane(size,color):plane.\n" + "function getsize(plane):size{all_different}.\n" + "function getcolor(plane):color.";
        String constraintString="(getplane(((getsize((B))),(getcolor((B))))))=(B).";
        LogicSolver.sloveLogicPuzzle(sortString,vocabularyString,constraintString);
    }
    public static void natTest()
    {
        String sortString="plane enum: A,B.";
        String vocabularyString="function indexof(plane): nat.";
        String constraintString="";
        LogicSolver.sloveLogicPuzzle(sortString,vocabularyString,constraintString);
    }
    public static void newConstraintTest()
    {
        String sortString="plane enum: A,B.\n" + "size enum: small,big.\n" + "color enum: red,blue.";
        String vocabularyString="function getplane(size,color):plane.\n" + "function getsize(plane):size{all_different}.\n" + "function getcolor(plane):color.";
        String constraintString=" 3+4*(1-3)/(((4)))=111.";
        LogicSolver.sloveLogicPuzzle(sortString,vocabularyString,constraintString);
    }
}
