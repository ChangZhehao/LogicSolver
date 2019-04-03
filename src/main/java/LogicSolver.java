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
        lexAnalyzer.lexAnalyze("tree  enum: oak, ash, elm, fir.");
        lexAnalyzer.printLexResult();
    }
}
