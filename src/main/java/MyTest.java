

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
        normalTest();
    }
    public static void normalTest()
    {
        String sortString="plane enum: A,B.\n" + "size enum: small,big.\n" + "color enum: red,blue.";
        String vocabularyString="function getplane(size,color):plane.\n" + "function getsize(plane):size{all_different}.\n" + "function getcolor(plane):color.";
        String constraintString="((getplane(((getsize((B))),(getcolor((B))))))=(B)).";
        LogicSolver.sloveLogicPuzzle(sortString,vocabularyString,constraintString);
    }
}
