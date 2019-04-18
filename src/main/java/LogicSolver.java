import analyzer.*;
import model.Vocabulary;

/**
 * User: u6613739
 * Date: 2019/4/3
 * Time: 21:46
 * Description:
 */
public class LogicSolver
{

    public static void sloveLogicPuzzle(String sortString,String vocabularyString,String ConstraintString)
    {
        LexAnalyzer lexAnalyzer = new LexAnalyzer();
        lexAnalyzer.lexAnalyze(sortString);
        System.out.println("Sort Lex Result:");
        lexAnalyzer.printLexResult();
        SortAnalyzer sortAnalyzer = new SortAnalyzer(lexAnalyzer.getLexList());
        if(!sortAnalyzer.start())
        {
            System.out.println("sortAnalyers wrong!");
            return;
        }

        lexAnalyzer = new LexAnalyzer();
        lexAnalyzer.lexAnalyze(vocabularyString);
        System.out.println("\r\nVocabulary Lex Result:");
        lexAnalyzer.printLexResult();
        VocabularyAnalyzer vocabularyAnalyzer = new VocabularyAnalyzer(lexAnalyzer.getLexList(),sortAnalyzer.getSortList());
        if(!vocabularyAnalyzer.start())
        {
            System.out.println("vocabularyAnalyzer wrong!");
            return;
        }

        lexAnalyzer = new LexAnalyzer();
        lexAnalyzer.lexAnalyze(ConstraintString);
        System.out.println("\r\nConstraint Lex Result:");
        lexAnalyzer.printLexResult();
        ConstraintAnalyzer constraintAnalyzer = new ConstraintAnalyzer(lexAnalyzer.getLexList(),vocabularyAnalyzer.getVocabularyList(),sortAnalyzer.getSortList());

        if(!constraintAnalyzer.start())
        {
            System.out.println("constraintAnalyzer wrong!");
            return;
        }

        //ModelAnalyzer modelAnalyzer = new ModelAnalyzer(vocabularyAnalyzer.getModelList(),constraintAnalyzer.getConstraintList());
        //modelAnalyzer.findFitModel();

    }
}
