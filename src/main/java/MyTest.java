

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
        //natTest();
        //newConstraintTest();
        //innerFunctionTest();
        //NOTTest();
        //IntegerInequalitiesTest();
        //simpleQuantifierALLTest();
        //simpleQuantifierSOMETest();
        //complexQuantifierALLTest();


        //FourHorsemenTest();
        LibarayBooksTest();
        //FloraTest();
    }
    public static void natTest()
    {
        String sortString="child enum:A,B.";
        String vocabularyString="function age(child):nat.";
        String constraintString="age(x) > 0.";
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
    public static void FourHorsemenTest()
    {
        String sortString="horse enum: bay, black, chestnut, gray. \n" +
                "rider enum: Mountback, Hacking, Klamberon, Topalov. \n" +
                "action enum: trot, gallop, stand, jump.";
        String vocabularyString="function gait(horse): action {all_different}.\n" +
                "function steed(rider): horse {all_different}." ;
        String constraintString=
                "gait(steed(Mountback)) = gallop.\n" +
                        "steed(Hacking) = bay AND NOT(gait(bay)=trot OR gait(bay)=jump). \n" +
                        "NOT(gait(steed(Klamberon)) = jump) AND NOT(gait(gray) = trot). \n" +
                        "steed(Topalov) = chestnut.";
        LogicSolver.solveLogicPuzzle(sortString,vocabularyString,constraintString);
    }

    public static void LibarayBooksTest()
    {
        String sortString="pupil enum: Arthur, Belinda, Clarissa. \n" +
                "subject enum: English, French, German.";
        String vocabularyString="function student(subject): pupil {all_different} .\n" +
                "function loans(pupil): nat." ;
        String constraintString="loans(Arthur) + loans(Belinda) + loans(Clarissa) = 25. \n" +
                "NOT(SOME x BELONG pupil(loans(x) > 14)). \n" +
                "loans(student(French)) = loans(Arthur) + 3. \n" +
                "loans(Clarissa) = loans(student(English)) + 2.";
        LogicSolver.solveLogicPuzzle(sortString,vocabularyString,constraintString);
    }
    public static void FloraTest()
    {
        String sortString="flower enum: buttercup, celandine, y_loosestrife, t_loosestrife,wall_rocket, m_marigold, p_lettuce. \n" +
                "stem_type enum: hairy, smooth, branched. \n" +
                "leaf_type enum: alternate, paired. \n" +
                "petal_count enum: four, five, many. \n" +
                "specimen enum: first, second, third, fourth.";
        String vocabularyString="function stem(flower): stem_type.\n" +
                "function petals(flower): petal_count.\n" +
                "function leaves(flower): leaf_type.\n" +
                "function id(specimen): flower." ;
        String constraintString="SOME x BELONG specimen (petals(id(x)) = five). \n" +
                "\n" +
                "stem(id(second)) = stem(id(third)) AND leaves(id(second)) = leaves(id(third)) AND NOT(petals(id(second)) = petals(id(third))). \n" +
                "\n" +
                "stem(id(first)) = branched AND petals(id(first)) > petals(id(second)). \n" +
                "\n" +
                "NOT(leaves(id(first)) = leaves(id(fourth))). \n" +
                "\n" +
                "\n" +
                "stem(buttercup) = hairy. \n" +
                "leaves(buttercup) = alternate. \n" +
                "petals(buttercup) = five. \n" +
                "\n" +
                "stem(y_loosestrife) = hairy. \n" +
                "leaves(y_loosestrife) = paired. \n" +
                "petals(y_loosestrife) = five. \n" +
                "\n" +
                "stem(m_marigold) = smooth. \n" +
                "leaves(m_marigold) = alternate. \n" +
                "petals(m_marigold) = five. \n" +
                "\n" +
                "stem(t_loosestrife) = smooth. \n" +
                "leaves(t_loosestrife) = paired. \n" +
                "petals(t_loosestrife) = many. \n" +
                "\n" +
                "stem(celandine) = branched. \n" +
                "leaves(celandine) = paired. \n" +
                "petals(celandine) = four. \n" +
                "\n" +
                "stem(wall_rocket)=branched. \n" +
                "leaves(wall_rocket)=alternate. \n" +
                "petals(wall_rocket)=four. \n" +
                "\n" +
                "stem(p_lettuce) = branched. \n" +
                "leaves(p_lettuce) = alternate. \n" +
                "petals(p_lettuce) = many.";
        LogicSolver.solveLogicPuzzle(sortString,vocabularyString,constraintString);
    }

    public static void simpleQuantifierALLTest()
    {
        String sortString="plane enum: A,B.\n"+ "size enum: small,big.";
        String vocabularyString="function getsize(plane):size." ;
        String constraintString="ALL x BELONG plane(getsize(x)=small).";
        LogicSolver.solveLogicPuzzle(sortString,vocabularyString,constraintString);
    }
    public static void simpleQuantifierSOMETest()
    {
        String sortString="plane enum: A,B.\n"+ "size enum: small,big.";
        String vocabularyString="function getsize(plane):size." ;
        String constraintString="SOME x BELONG plane(getsize(x)=small).";
        LogicSolver.solveLogicPuzzle(sortString,vocabularyString,constraintString);
    }
    public static void complexQuantifierALLTest()
    {
        String sortString="kid enum: A, B.";
        String vocabularyString="function age(kid): nat{all_different}.\n";
        String constraintString="ALL k BELONG kid ( age(k) > 5 AND age(k) < 10)."+
                "age(A) = age(B). \n";
        LogicSolver.solveLogicPuzzle(sortString,vocabularyString,constraintString);
    }
}
