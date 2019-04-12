package analyzer;

import model.Constraint;
import model.EnumLexItemType;
import model.LexItem;
import model.Sort;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
    int scannerIndex = 0;
    public  ConstraintAnalyzer(List<LexItem> lexItemList)
    {
        this.lexItemList = lexItemList;
    }
    @Override
    public void start()
    {
        getConstraints(lexItemList);
        for(int i=0;i<constraintList.size();i++)
        {
            analyzeConstraint(constraintList.get(i));
        }
    }

    private boolean analyzeConstraint(Constraint parentConstraint)
    {

        /**
         * the type of constraint may be consider as following type:
         * (......) the element of subList is 1
         * (......)OPERATOR(......) the element of subList is 3
         * NOT(.....) the element of subList is 2
         *
         */
        Queue<Constraint> constraintQueue = new LinkedList<>();
        constraintQueue.offer(parentConstraint);

        while (constraintQueue.size() != 0)
        {
            Constraint currentConstraint = constraintQueue.poll();
            List<List<LexItem>> subList = getSubLists(currentConstraint.getLexItemList());
            switch (subList.size())
            {
                case 1:
                    if(subList.get(0).size()==1)
                    {
                        //only one elements
                        currentConstraint.setLeaf(subList.get(0).get(0));
                    }
                    else {
                        Constraint newConstraint = new Constraint();
                        newConstraint.setLexItemList(removeBrackets(subList.get(0)));
                        currentConstraint.getChildConstraintList().add(newConstraint);
                        constraintQueue.offer(newConstraint);
                    }
                    break;
                case 2:
                        Constraint newConstraint = new Constraint();
                        newConstraint.setLexItemList(removeBrackets(subList.get(1)));
                        currentConstraint.getChildConstraintList().add(newConstraint);
                        currentConstraint.setConnectSymbol(subList.get(0).get(0));
                        constraintQueue.offer(newConstraint);
                    break;
                case 3:
                    Constraint leftConstraint = new Constraint();

                    leftConstraint.setLexItemList(removeBrackets(subList.get(0)));
                    currentConstraint.getChildConstraintList().add(leftConstraint);
                    Constraint rightConstraint = new Constraint();
                    rightConstraint.setLexItemList(removeBrackets(subList.get(2)));
                    currentConstraint.getChildConstraintList().add(rightConstraint);

                    currentConstraint.setConnectSymbol(subList.get(1).get(0));
                    constraintQueue.offer(leftConstraint);
                    constraintQueue.offer(rightConstraint);
                    break;
                default:return false;
            }

        }
        return true;


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
