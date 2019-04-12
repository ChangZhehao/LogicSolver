import analyzer.ConstraintAnalyzer;
import analyzer.LexAnalyzer;
import analyzer.SortAnalyzer;
import analyzer.VocabularyAnalyzer;
import model.Vocabulary;

/**
 * User: u6613739
 * Date: 2019/4/3
 * Time: 21:46
 * Description:
 */
public class LogicSolver
{
    public static void main(String[] args)
    {
        LexAnalyzer lexAnalyzer = new LexAnalyzer();
        lexAnalyzer.lexAnalyze("plane enum: A,B.\n" + "size enum: small,big.\n" + "color enum: red,blue.");
        lexAnalyzer.printLexResult();
        SortAnalyzer sortAnalyzer = new SortAnalyzer(lexAnalyzer.getLexList());
        sortAnalyzer.start();

        lexAnalyzer = new LexAnalyzer();
        lexAnalyzer.lexAnalyze("function getplane(size,color):plane.\n" + "function getsize(plane):size{all_different}.\n" + "function getcolor(plane):color.");
        VocabularyAnalyzer vocabularyAnalyzer = new VocabularyAnalyzer(lexAnalyzer.getLexList(),sortAnalyzer.getSortList());
        vocabularyAnalyzer.start();

        lexAnalyzer = new LexAnalyzer();
        lexAnalyzer.lexAnalyze("NOT(((x) = (y)) AND ((y) = (z))). \n"+"(((mine) + (yours)) = (10)).");
        ConstraintAnalyzer constraintAnalyzer = new ConstraintAnalyzer(lexAnalyzer.getLexList());
        constraintAnalyzer.start();




    }
}
