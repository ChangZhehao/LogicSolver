package analyzer;

import model.Constraint;
import model.LexItem;

import java.util.ArrayList;
import java.util.List;

/**
 * User: u6613739
 * Date: 2019/4/6
 * Time: 20:38
 * Description:
 */
public class ConstraintAnalyzer implements AnalyzerImp
{
    private List<Constraint> constraintList = new ArrayList<>();
    private List<LexItem> lexItemList = new ArrayList<>();

    public  ConstraintAnalyzer(List<LexItem> lexItemList)
    {
        this.lexItemList = lexItemList;
    }
    @Override
    public void start()
    {

    }

    @Override
    public boolean checkSyntax()
    {
        return false;
    }


}
