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
        lexAnalyzer.lexAnalyze("horse enum: bay, black, chestnut, gray. \n" + "rider enum: Mountback, Hacking, Klamberon, Topalov. \n" + "action enum: trot, gallop, stand, jump.");
        lexAnalyzer.printLexResult();
        SortAnalyzer sortAnalyzer = new SortAnalyzer(lexAnalyzer.getLexList());
        sortAnalyzer.start();

        lexAnalyzer = new LexAnalyzer();
        lexAnalyzer.lexAnalyze("function X(x,y): action {all_different, hidden}. \n" + "function steed(rider): horse {all_different}.");
        VocabularyAnalyzer vocabularyAnalyzer = new VocabularyAnalyzer(lexAnalyzer.getLexList());
        vocabularyAnalyzer.start();


    }
}
