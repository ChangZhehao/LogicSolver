package analyzer;

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
    private List<Vocabulary> vocabularyList = new ArrayList<>();
    private List<Sort> sortList = new ArrayList<>();
    int scannerIndex = 0;
    public  ConstraintAnalyzer(List<LexItem> lexItemList,List<Vocabulary> vocabularyList,List<Sort> sortList)
    {
        this.lexItemList = lexItemList;
        this.vocabularyList = vocabularyList;
        this.sortList = sortList;
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

    private boolean analyzeConstraint(Constraint constraint)
    {
        updateLexItemType(constraint);

        while(true)
        {
            if(constraint.getDstNodes().size()==3)
            {
                break;
            }
            int[]place = ProcessOfFunctionAndBrackets(constraint);
            switch (place[0])
            {
                case 0:
                    if(isFunction(constraint,place[1]))
                    {
                        DSTNode newNode = new DSTNode(new LexItem(EnumLexItemType.DSTNODE_EXPRESSION,"dstNode Expression"));
                        newNode.setMove(constraint.getDstNodes().get(place[1]-1).getValue());
                        newNode.setChildrenNodes(new ArrayList<>());
                        for(int i=place[1]+1;i<=place[2]-1;i+=2)
                        {
                            newNode.getChildrenNodes().add(constraint.getDstNodes().get(i));
                        }
                        constraint.getDstNodes().set(place[1]-1,newNode);
                        for(int i=place[1];i<=place[2];i++)
                        {
                            constraint.getDstNodes().remove(place[1]);
                        }

                    }
                    else
                    {
                        DSTNode leftBracket = constraint.getDstNodes().get(place[1]);
                        DSTNode rightBracket = constraint.getDstNodes().get(place[2]);
                        parseSimpleExpr(constraint,place[1]+1,place[2]-1);

                        constraint.getDstNodes().remove(leftBracket);
                        constraint.getDstNodes().remove(rightBracket);
                    }
                    break;
                case 1:
                    parseSimpleExpr(constraint,0,constraint.getDstNodes().size()-1);
                    break;
                case 2:
                    break;
            }
        }

        return true;
    }

    private boolean parseSimpleExpr(Constraint constraint,int startIndex,int endIndex)
    {
        priorityOne(constraint,startIndex,endIndex);
        priorityTwo(constraint,startIndex,endIndex);
        priorityThree(constraint,startIndex,endIndex);
        priorityFour(constraint,startIndex,endIndex);
        priorityFive(constraint,startIndex,endIndex);
        prioritySix(constraint,startIndex,endIndex);
        prioritySeven(constraint,startIndex,endIndex);

        return true;
    }


    /**
     * plus, minus
     * rules:
     * + =: ^+A.... | Op+A....
     * - =: ^-A.... | Op-A....
     *
     * @param constraint
     * @return
     */
    private boolean priorityOne(Constraint constraint,int startIndex, int endIndex)
    {
        boolean ischanged = true;
        while(ischanged)
        {
            ischanged = false;
            for(int i=startIndex+2;i<=endIndex && i<constraint.getDstNodes().size();i++)
            {

                DSTNode node1 = constraint.getDstNodes().get(i-2);
                DSTNode node2 = constraint.getDstNodes().get(i-1);
                DSTNode node3 = constraint.getDstNodes().get(i);

                if(node1.getValue().getType()!=EnumLexItemType.START && node1.getValue().getType()!= EnumLexItemType.OPERATOR)
                {
                    continue;
                }
                if(node2.getValue().getType()!=EnumLexItemType.OPERATOR)
                {
                    continue;
                }
                if(!(node2.getValue().getData().equals("+") || node2.getValue().getData().equals("-")))
                {
                    continue;
                }
                if(node3.getValue().getType()!=EnumLexItemType.CONSTANT &&node3.getValue().getType()!=EnumLexItemType.DSTNODE_EXPRESSION && node3.getValue().getType()!=EnumLexItemType.NUMBER)
                {
                    continue;
                }
                ischanged = true;
                DSTNode newNode = new DSTNode(new LexItem(EnumLexItemType.DSTNODE_EXPRESSION,"dstNode Expression"));
                newNode.setChildrenNodes(new ArrayList<>());
                newNode.getChildrenNodes().add(node3);
                newNode.setMove(node2.getValue());
                constraint.getDstNodes().set(i-1,newNode);
                constraint.getDstNodes().remove(i);

                break;
            }

        }
        return true;
    }

    /**
     * muliply ,divide
     * rules:
     * * =: ...A*A...
     * / =: ...A/A...
     *
     * @param constraint
     * @return
     */
    private boolean priorityTwo(Constraint constraint,int startIndex, int endIndex)
    {
        boolean ischanged = true;
        while(ischanged)
        {
            ischanged = false;
            for(int i=startIndex+2;i<=endIndex && i<constraint.getDstNodes().size();i++)
            {

                DSTNode node1 = constraint.getDstNodes().get(i - 2);
                DSTNode node2 = constraint.getDstNodes().get(i - 1);
                DSTNode node3 = constraint.getDstNodes().get(i);

                if(node1.getValue().getType()!=EnumLexItemType.CONSTANT && node1.getValue().getType()!=EnumLexItemType.DSTNODE_EXPRESSION && node1.getValue().getType()!=EnumLexItemType.NUMBER)
                {
                    continue;
                }
                if(node2.getValue().getType()!=EnumLexItemType.OPERATOR)
                {
                    continue;
                }
                if(!(node2.getValue().getData().equals("*") || node2.getValue().getData().equals("/")))
                {
                    continue;
                }
                if(node3.getValue().getType()!=EnumLexItemType.CONSTANT && node3.getValue().getType()!=EnumLexItemType.DSTNODE_EXPRESSION &&node3.getValue().getType()!=EnumLexItemType.NUMBER)
                {
                    continue;
                }
                ischanged = true;
                DSTNode newNode = new DSTNode(new LexItem(EnumLexItemType.DSTNODE_EXPRESSION,"dstNode Expression"));
                newNode.setChildrenNodes(new ArrayList<>());
                newNode.getChildrenNodes().add(node1);
                newNode.getChildrenNodes().add(node3);
                newNode.setMove(node2.getValue());
                constraint.getDstNodes().set(i-2,newNode);
                constraint.getDstNodes().remove(node2);
                constraint.getDstNodes().remove(node3);
                break;

            }
        }
        return true;
    }
    /**
     * add ,subtract
     * rules:
     * + =: ...A+A...
     * - =: ...A-A...
     *
     * @param constraint
     * @return
     */
    private boolean priorityThree(Constraint constraint,int startIndex, int endIndex)
    {
        boolean ischanged = true;
        while(ischanged)
        {
            ischanged = false;
            for(int i=startIndex+2;i<=endIndex && i<constraint.getDstNodes().size();i++)
            {

                DSTNode node1 = constraint.getDstNodes().get(i - 2);
                DSTNode node2 = constraint.getDstNodes().get(i - 1);
                DSTNode node3 = constraint.getDstNodes().get(i);

                if(node1.getValue().getType()!=EnumLexItemType.CONSTANT && node1.getValue().getType()!=EnumLexItemType.DSTNODE_EXPRESSION && node1.getValue().getType()!=EnumLexItemType.NUMBER)
                {
                    continue;
                }
                if(node2.getValue().getType()!=EnumLexItemType.OPERATOR)
                {
                    continue;
                }
                if(!(node2.getValue().getData().equals("+") || node2.getValue().getData().equals("-")))
                {
                    continue;
                }
                if(node3.getValue().getType()!=EnumLexItemType.CONSTANT && node3.getValue().getType()!=EnumLexItemType.DSTNODE_EXPRESSION &&node3.getValue().getType()!=EnumLexItemType.NUMBER)
                {
                    continue;
                }
                ischanged = true;
                DSTNode newNode = new DSTNode(new LexItem(EnumLexItemType.DSTNODE_EXPRESSION,"dstNode Expression"));
                newNode.setChildrenNodes(new ArrayList<>());
                newNode.getChildrenNodes().add(node1);
                newNode.getChildrenNodes().add(node3);
                newNode.setMove(node2.getValue());
                constraint.getDstNodes().set(i-2,newNode);
                constraint.getDstNodes().remove(node2);
                constraint.getDstNodes().remove(node3);
                break;

            }
        }
        return true;
    }

    /**
     * relational comparisons
     * rules:
     * > =: ...A>A....
     * < =: ...A<A....
     * = =: ...A=A....
     *
     * @param constraint
     * @return
     */
    private boolean priorityFour(Constraint constraint,int startIndex, int endIndex)
    {
        boolean ischanged = true;
        while(ischanged)
        {
            ischanged = false;
            for(int i=startIndex+2;i<=endIndex && i<constraint.getDstNodes().size();i++)
            {

                DSTNode node1 = constraint.getDstNodes().get(i - 2);
                DSTNode node2 = constraint.getDstNodes().get(i - 1);
                DSTNode node3 = constraint.getDstNodes().get(i);

                if(node1.getValue().getType()!=EnumLexItemType.CONSTANT && node1.getValue().getType()!=EnumLexItemType.DSTNODE_EXPRESSION && node1.getValue().getType()!=EnumLexItemType.NUMBER)
                {
                    continue;
                }
                if(node2.getValue().getType()!=EnumLexItemType.OPERATOR)
                {
                    continue;
                }
                if(!(node2.getValue().getData().equals(">") || node2.getValue().getData().equals("<") || node2.getValue().getData().equals("=")))
                {
                    continue;
                }
                if(node3.getValue().getType()!=EnumLexItemType.CONSTANT && node3.getValue().getType()!=EnumLexItemType.DSTNODE_EXPRESSION &&node3.getValue().getType()!=EnumLexItemType.NUMBER)
                {
                    continue;
                }
                ischanged = true;
                DSTNode newNode = new DSTNode(new LexItem(EnumLexItemType.DSTNODE_JUDGEMENT,"dstNode Judgement"));
                newNode.setChildrenNodes(new ArrayList<>());
                newNode.getChildrenNodes().add(node1);
                newNode.getChildrenNodes().add(node3);
                newNode.setMove(node2.getValue());
                constraint.getDstNodes().set(i-2,newNode);
                constraint.getDstNodes().remove(node2);
                constraint.getDstNodes().remove(node3);
                break;

            }
        }
        return true;
    }

    /**
     *
     * and
     * rules:
     * AND =: ...A AND A....
     *
     * @param constraint
     * @return
     */
    private boolean priorityFive(Constraint constraint,int startIndex, int endIndex)
    {
        boolean ischanged = true;
        while(ischanged)
        {
            ischanged = false;
            for(int i=startIndex+2;i<=endIndex && i<constraint.getDstNodes().size();i++)
            {

                DSTNode node1 = constraint.getDstNodes().get(i - 2);
                DSTNode node2 = constraint.getDstNodes().get(i - 1);
                DSTNode node3 = constraint.getDstNodes().get(i);

                if(node1.getValue().getType()!=EnumLexItemType.CONSTANT && node1.getValue().getType()!=EnumLexItemType.DSTNODE_JUDGEMENT && node1.getValue().getType()!=EnumLexItemType.NUMBER)
                {
                    continue;
                }
                if(node2.getValue().getType()!=EnumLexItemType.KEYWORD)
                {
                    continue;
                }
                if(!(node2.getValue().getData().equals("AND")))
                {
                    continue;
                }
                if(node3.getValue().getType()!=EnumLexItemType.CONSTANT && node3.getValue().getType()!=EnumLexItemType.DSTNODE_JUDGEMENT &&node3.getValue().getType()!=EnumLexItemType.NUMBER)
                {
                    continue;
                }
                ischanged = true;
                DSTNode newNode = new DSTNode(new LexItem(EnumLexItemType.DSTNODE_JUDGEMENT,"dstNode Judgement"));
                newNode.setChildrenNodes(new ArrayList<>());
                newNode.getChildrenNodes().add(node1);
                newNode.getChildrenNodes().add(node3);
                newNode.setMove(node2.getValue());
                constraint.getDstNodes().set(i-2,newNode);
                constraint.getDstNodes().remove(node2);
                constraint.getDstNodes().remove(node3);
                break;

            }
        }
        return true;
    }
    /**
     *
     * or
     * rules:
     * OR =: ...A OR A....
     *
     * @param constraint
     * @return
     */
    private boolean prioritySix(Constraint constraint,int startIndex, int endIndex)
    {
        boolean ischanged = true;
        while(ischanged)
        {
            ischanged = false;
            for(int i=startIndex+2;i<=endIndex && i<constraint.getDstNodes().size();i++)
            {

                DSTNode node1 = constraint.getDstNodes().get(i - 2);
                DSTNode node2 = constraint.getDstNodes().get(i - 1);
                DSTNode node3 = constraint.getDstNodes().get(i);

                if(node1.getValue().getType()!=EnumLexItemType.CONSTANT && node1.getValue().getType()!=EnumLexItemType.DSTNODE_JUDGEMENT && node1.getValue().getType()!=EnumLexItemType.NUMBER)
                {
                    continue;
                }
                if(node2.getValue().getType()!=EnumLexItemType.KEYWORD)
                {
                    continue;
                }
                if(!(node2.getValue().getData().equals("OR")))
                {
                    continue;
                }
                if(node3.getValue().getType()!=EnumLexItemType.CONSTANT && node3.getValue().getType()!=EnumLexItemType.DSTNODE_JUDGEMENT&&node3.getValue().getType()!=EnumLexItemType.NUMBER)
                {
                    continue;
                }
                ischanged = true;
                DSTNode newNode = new DSTNode(new LexItem(EnumLexItemType.DSTNODE_JUDGEMENT,"dstNode Judgement"));
                newNode.setChildrenNodes(new ArrayList<>());
                newNode.getChildrenNodes().add(node1);
                newNode.getChildrenNodes().add(node3);
                newNode.setMove(node2.getValue());
                constraint.getDstNodes().set(i-2,newNode);
                constraint.getDstNodes().remove(node2);
                constraint.getDstNodes().remove(node3);
                break;

            }
        }
        return true;
    }
    /**
     *
     * IF, IFF
     * rules:
     * IF =: ...A IF A....
     * IFF =: ... A IFF A...
     *
     * @param constraint
     * @return
     */
    private boolean prioritySeven(Constraint constraint,int startIndex, int endIndex)
    {
        boolean ischanged = true;
        while(ischanged)
        {
            ischanged = false;
            for(int i=startIndex+2;i<=endIndex && i<constraint.getDstNodes().size();i++)
            {

                DSTNode node1 = constraint.getDstNodes().get(i - 2);
                DSTNode node2 = constraint.getDstNodes().get(i - 1);
                DSTNode node3 = constraint.getDstNodes().get(i);

                if(node1.getValue().getType()!=EnumLexItemType.CONSTANT && node1.getValue().getType()!=EnumLexItemType.DSTNODE_JUDGEMENT && node1.getValue().getType()!=EnumLexItemType.NUMBER)
                {
                    continue;
                }
                if(node2.getValue().getType()!=EnumLexItemType.KEYWORD)
                {
                    continue;
                }
                if(!(node2.getValue().getData().equals("IFF") || node2.getValue().getData().equals("IF")))
                {
                    continue;
                }
                if(node3.getValue().getType()!=EnumLexItemType.CONSTANT && node3.getValue().getType()!=EnumLexItemType.DSTNODE_JUDGEMENT&&node3.getValue().getType()!=EnumLexItemType.NUMBER)
                {
                    continue;
                }
                ischanged = true;
                DSTNode newNode = new DSTNode(new LexItem(EnumLexItemType.DSTNODE_JUDGEMENT,"dstNode Judgement"));
                newNode.setChildrenNodes(new ArrayList<>());
                newNode.getChildrenNodes().add(node1);
                newNode.getChildrenNodes().add(node3);
                newNode.setMove(node2.getValue());
                constraint.getDstNodes().set(i-2,newNode);
                constraint.getDstNodes().remove(node2);
                constraint.getDstNodes().remove(node3);
                break;

            }
        }
        return true;
    }

    /**
     * parenthesis and function's process
     *
     * intStatusCode
     * 0 success
     * 1 no parenthesis match
     * 2 list is null
     *
     * @param constraint
     * @return [intStatusCode, indexof'(', indexof ')']
     */
    private int[] ProcessOfFunctionAndBrackets(Constraint constraint)
    {
        if(constraint.getDstNodes().size()==0)
        {
            return new int[]{2,-1,-1};
        }


        boolean ischanged = false;
        int innerLeftBracketIndex = -1;
        int innerRightBracketIndex = -1;
        for(int i=0;i<constraint.getDstNodes().size();i++)
        {
            LexItem item = constraint.getDstNodes().get(i).getValue();
            if(item.getType()==EnumLexItemType.SEPARATOR && item.getData().equals("("))
            {
                innerLeftBracketIndex = i;
            }
            else if(item.getType() == EnumLexItemType.SEPARATOR && item.getData().equals(")"))
            {
                innerRightBracketIndex = i;
                break;
            }
        }

        if(innerRightBracketIndex == -1)
        {
            return new int[]{1,-1,-1};
        }
        else
        {
            return new int[]{0,innerLeftBracketIndex,innerRightBracketIndex};
        }

    }

    private boolean isFunction(Constraint constraint,int innerLeftBracketIndex)
    {
        if(innerLeftBracketIndex==0)
        {
            return false;
        }
        LexItem headItem = constraint.getDstNodes().get(innerLeftBracketIndex-1).getValue();
        if(headItem.getType()!=EnumLexItemType.FUNCTION_NAME)
        {
            return false;
        }
        else
        {
            return true;
        }

    }
    private void updateLexItemType(Constraint constraint)
    {
        for(DSTNode dstNode: constraint.getDstNodes())
        {
            if(isFunctionNameLexItem(dstNode.getValue()))
            {
                dstNode.getValue().setType(EnumLexItemType.FUNCTION_NAME);
            }
            else if(isVariableLexItem(dstNode.getValue()))
            {
                dstNode.getValue().setType(EnumLexItemType.VARIALBE);
            }
            else if(isConstantLexItem(dstNode.getValue()))
            {
                dstNode.getValue().setType(EnumLexItemType.CONSTANT);
            }
        }
    }
    private boolean isFunctionNameLexItem(LexItem item)
    {
        if(item.getType()!=EnumLexItemType.IDENTIFIER)
        {
            return false;
        }
        for(Vocabulary vocabulary :vocabularyList)
        {
            if(item.getData().equals(vocabulary.getFunctionName()))
            {
                return true;
            }
        }
        return false;
    }
    private boolean isVariableLexItem(LexItem item)
    {
        if(item.getType()!=EnumLexItemType.IDENTIFIER)
        {
            return false;
        }
        for(Sort sort :sortList)
        {
            if(item.getData().equals(sort.getName()))
            {
                return true;
            }
        }
        return false;
    }
    private boolean isConstantLexItem(LexItem item)
    {
        if(item.getType()!=EnumLexItemType.IDENTIFIER)
        {
            return false;
        }
        for(Sort sort :sortList)
        {
            for(String constant : sort.getContains())
            {
                if(item.getData().equals(constant))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean getConstraints(List<LexItem> lexList)
    {
        while (scannerIndex < lexItemList.size())
        {
            int status = 0;
            Constraint constraint =new Constraint();
            constraint.getDstNodes().add(new DSTNode(new LexItem(EnumLexItemType.START,"^")));
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
                        constraint.getDstNodes().add(new DSTNode(lexItem));
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
                            constraint.getDstNodes().add(new DSTNode(lexItem));
                            scannerIndex++;
                        }
                        break;
                    case -1:break;
                    default:return false;

                }
            }
            constraint.getDstNodes().add(new DSTNode(new LexItem(EnumLexItemType.END,"$")));
            constraintList.add(constraint);
        }

        return true;


    }

    @Override
    public boolean checkSyntax()
    {
        return false;
    }




}
