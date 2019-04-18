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
        priorityOne(constraint);
        priorityTwo(constraint);
        priorityThree(constraint);
        priorityFour(constraint);
        priorityFive(constraint);
        prioritySix(constraint);
        prioritySeven(constraint);



        if(constraint.getDstNodes().size()==3) {
            return true;
        }
        else
        {
            return false;
        }
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
    private boolean priorityOne(Constraint constraint)
    {
        boolean ischanged = true;
        while(ischanged)
        {
            ischanged = false;
            for(int i=2;i<constraint.getDstNodes().size();i++)
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
    private boolean priorityTwo(Constraint constraint)
    {
        boolean ischanged = true;
        while(ischanged)
        {
            ischanged = false;
            for(int i=2;i<constraint.getDstNodes().size();i++)
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
    private boolean priorityThree(Constraint constraint)
    {
        boolean ischanged = true;
        while(ischanged)
        {
            ischanged = false;
            for(int i=2;i<constraint.getDstNodes().size();i++)
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
    private boolean priorityFour(Constraint constraint)
    {
        boolean ischanged = true;
        while(ischanged)
        {
            ischanged = false;
            for(int i=2;i<constraint.getDstNodes().size();i++)
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
    private boolean priorityFive(Constraint constraint)
    {
        boolean ischanged = true;
        while(ischanged)
        {
            ischanged = false;
            for(int i=2;i<constraint.getDstNodes().size();i++)
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
    private boolean prioritySix(Constraint constraint)
    {
        boolean ischanged = true;
        while(ischanged)
        {
            ischanged = false;
            for(int i=2;i<constraint.getDstNodes().size();i++)
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
    private boolean prioritySeven(Constraint constraint)
    {
        boolean ischanged = true;
        while(ischanged)
        {
            ischanged = false;
            for(int i=2;i<constraint.getDstNodes().size();i++)
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
     * @param constraint
     * @return
     */
    private boolean ProcessOfFunctionAndBrackets(Constraint constraint)
    {
        boolean ischanged = true;
        while(ischanged)
        {
            ischanged = false;
            int innerLeftBracketIndex = -1;
            int innerRightBracketIndex = -1;
            int matchCount = 0;
            for(int i=0;i<constraint.getDstNodes().size();i++)
            {
                LexItem item = constraint.getDstNodes().get(i).getValue();
                if(item.getType()==EnumLexItemType.SEPARATOR && item.getData().equals("("))
                {
                    innerLeftBracketIndex = i;
                    matchCount++;
                }
                else if(item.getType() == EnumLexItemType.SEPARATOR && item.getData().equals(")"))
                {
                    innerRightBracketIndex = i;
                    matchCount--;
                }
                if(innerLeftBracketIndex!= -1 && innerRightBracketIndex!= -1 && matchCount==0)
                {
                    ischanged=true;
                    break;
                }
            }
            if(!ischanged)
            {
                break;
            }

            if(isFunction(constraint,innerLeftBracketIndex))
            {
                //FIXME: didn't check if the paramters of function are suitable.
                DSTNode functionNode =constraint.getDstNodes().get(innerLeftBracketIndex-1);
                functionNode.setMove(functionNode.getValue());
                functionNode.setChildrenNodes(new ArrayList<>());
                for(int i=innerLeftBracketIndex+1;i<innerRightBracketIndex;i+=2)
                {
                    functionNode.getChildrenNodes().add(constraint.getDstNodes().get(i));
                }

                functionNode.setValue(new LexItem(EnumLexItemType.DSTNODE_EXPRESSION,"function"));
                for(int i=0;i<=innerRightBracketIndex-innerLeftBracketIndex;i++)
                {
                    constraint.getDstNodes().remove(innerLeftBracketIndex);
                }
            }
            else
            {
                //it should be (A). if the index1-index2 is not 2 ,there may be something wrong
                if(innerRightBracketIndex-innerLeftBracketIndex!=2)
                {
                    System.out.println("innerRight - innerLeft is not 2");
                    return false;
                }
                DSTNode dstNode1 = constraint.getDstNodes().get(innerLeftBracketIndex);
                DSTNode dstNode3 = constraint.getDstNodes().get(innerRightBracketIndex);
                constraint.getDstNodes().remove(dstNode1);
                constraint.getDstNodes().remove(dstNode3);
            }

        }
        return true;
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
