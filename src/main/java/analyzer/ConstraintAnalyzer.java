package analyzer;

import jdk.nashorn.internal.ir.LexicalContext;
import lombok.Data;
import model.*;

import java.util.*;

/**
 * User: u6613739
 * Date: 2019/4/6
 * Time: 20:38
 * Description:
 */
@Data
public class ConstraintAnalyzer implements AnalyzerImp
{
    private List<Constraint> constraintList = new ArrayList<>();
    private List<LexItem> lexItemList = new ArrayList<>();
    int scannerIndex = 0;
    public  ConstraintAnalyzer(List<LexItem> lexItemList)
    {
        this.lexItemList = lexItemList;
    }
    @Override
    public boolean start()
    {
        getConstraints(lexItemList);
        for(int i=0;i<constraintList.size();i++)
        {
            boolean isSuccess = analyzeConstraint(constraintList.get(i));
            if(!isSuccess)
            {
                return false;
            }
        }
        return true;
    }

    private boolean analyzeConstraint(Constraint parentConstraint)
    {

        GrammarNode grammarNode = new GrammarNode();
        Stack<String> statusStack = new Stack<>();
        List<LexItem> inputs = parentConstraint.getLexItemList();
        statusStack.push("$");
        statusStack.push("X");
        String status =statusStack.peek();
        LexItem input = inputs.get(0);
        while(status!="$")
        {
            if(status.equals(input.getData()) && input.getType()==EnumLexItemType.SEPARATOR)
        }




    }

    private List<LexItem> removeBrackets(List<LexItem> lexList)
    {
        return lexList.subList(1,lexList.size()-1);
    }

    private boolean getConstraints(List<LexItem> lexList)
    {
        while (scannerIndex < lexItemList.size())
        {
            int status = 0;
            Constraint constraint =new Constraint();
            while (status != -1)
            {
                LexItem lexItem = lexItemList.get(scannerIndex);
                switch (status)
                {
                    case 0:
                        if(lexItem.getType()== EnumLexItemType.SEPARATOR && lexItem.getData().equals("."))
                        {
                            return false;
                        }
                        constraint.getLexItemList().add(lexItem);
                        status =1;
                        scannerIndex++;
                        break;
                    case 1:
                        if(lexItem.getType()== EnumLexItemType.SEPARATOR && lexItem.getData().equals("."))
                        {
                            status = -1;
                            scannerIndex++;
                        }
                        else
                        {
                            status =1;
                            constraint.getLexItemList().add(lexItem);
                            scannerIndex++;
                        }
                        break;
                    case -1:break;
                    default:return false;

                }
            }
            constraintList.add(constraint);
        }

        return true;


    }

    private List<List<LexItem>> getSubLists(List<LexItem> lexList)
    {
        List<List<LexItem>> subLists = new ArrayList<>();
        int scannerIndex = 0;
        while(scannerIndex<lexList.size())
        {
            List<LexItem> subList = new ArrayList<>();
            int startIndex = -1;
            int bracektCoupleCount =0;
            while(!(bracektCoupleCount==0 && startIndex!=-1))
            {
                if(lexList.get(scannerIndex).getType()== EnumLexItemType.SEPARATOR && lexList.get(scannerIndex).getData().equals("("))
                {
                    subList.add(lexList.get(scannerIndex));
                    startIndex = scannerIndex;
                    bracektCoupleCount++;
                    scannerIndex++;
                }
                else if(lexList.get(scannerIndex).getType()==EnumLexItemType.SEPARATOR && lexList.get(scannerIndex).getData().equals(")"))
                {
                    subList.add(lexList.get(scannerIndex));
                    bracektCoupleCount--;
                    scannerIndex++;
                    if(bracektCoupleCount == 0)
                    {
                        subLists.add(subList);
                        break;
                    }
                }
                else if(bracektCoupleCount == 0)
                {
                    subList.add(lexList.get(scannerIndex));
                    subLists.add(subList);
                    scannerIndex++;
                    break;
                }
                else
                {
                    subList.add(lexList.get(scannerIndex));
                    scannerIndex++;
                }

            }
        }
        return subLists;
    }

    @Override
    public boolean checkSyntax()
    {
        return false;
    }


}
