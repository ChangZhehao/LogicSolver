package analyzer;

import lombok.Data;
import model.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * User: u6613739
 * Date: 2019/4/14
 * Time: 18:14
 * Description:
 */
@Data
public class ModelAnalyzer
{

    List<Model> allModelList = new ArrayList<>();
    List<Model> allFitModelList = new ArrayList<>();
    List<Constraint> constraintList = new ArrayList<>();

    public ModelAnalyzer(List<Model> allModelList, List<Constraint> constraintList)
    {
        this.allModelList = allModelList;
        this.constraintList = constraintList;
    }

    public void findFitModel()
    {
        for (Model model : allModelList)
        {
            if (isVaildModel(model))
            {
                allFitModelList.add(model);
            }
            //clean
        }
        printFitModels();
    }

    private void printFitModels()
    {
        int i=0;
        for(Model model:allFitModelList)
        {
            i++;
            System.out.println("Model "+i);
            System.out.println(model.printResult());
        }
    }

    private boolean isVaildModel(Model model)
    {
        boolean isValid = true;
        for (Constraint constraint : constraintList)
        {
            if (!isFitConstraint(model, constraint))
            {
                isValid = false;
                break;
            }
        }
        return isValid;
    }

    private boolean isFitConstraint(Model model, Constraint constraint)
    {
        //TODO: isFitConstraint

        oneStep(model, constraint.getDstNodes().get(1));
        String result = constraint.getDstNodes().get(1).getResult();
        //clean Constraint;
        cleanConstraintTmpValue(constraint);

        if(result.equals("false"))
        {
            return false;
        }
        else {
            return true;
        }
    }
    private void cleanConstraintTmpValue(Constraint constraint)
    {
        Queue<DSTNode> dstNodeQueue = new LinkedList<>( constraint.getDstNodes());
        while(dstNodeQueue.size()!=0)
        {
            DSTNode dstNode = dstNodeQueue.poll();
            dstNode.setResult(null);
            if(dstNode.getChildrenNodes()==null)
            {
                continue;
            }
            for(DSTNode childNode : dstNode.getChildrenNodes())
            {
                ((LinkedList<DSTNode>) dstNodeQueue).push(childNode);
            }
        }
    }

    private boolean oneStep(Model model, DSTNode dstNode)
    {
        if (dstNode.getChildrenNodes() == null || dstNode.getChildrenNodes().size() == 0)
        {
            dstNode.setResult(dstNode.getValue().getData());
            return true;
        }


        for (DSTNode childNode : dstNode.getChildrenNodes())
        {
            oneStep(model, childNode);
        }

        if(dstNode.getMove()==null)
        {
            dstNode.setResult(dstNode.getValue().getData());
        }
        else
        {

            switch (dstNode.getMove().getType())
            {
                case OPERATOR :
                    dstNode.setResult(findResultFromOperator(dstNode));
                    break;
                case FUNCTION_NAME:
                    dstNode.setResult(findResultFromModel(model, dstNode));
                    break;

            }
        }
        return true;
    }

    private String findResultFromOperator(DSTNode dstNode)
    {
        String result = "null";
        String childResult1,childResult2;
        switch (dstNode.getMove().getData())
        {
            case "=":
                childResult1 = dstNode.getChildrenNodes().get(0).getResult();
                childResult2 = dstNode.getChildrenNodes().get(1).getResult();
                if(childResult1.equals(childResult2))
                {
                    result ="true";
                }
                else {
                    result = "false";
                }
                break;
            case ">":
                childResult1 = dstNode.getChildrenNodes().get(0).getResult();
                childResult2 = dstNode.getChildrenNodes().get(1).getResult();
                if(Integer.valueOf(childResult1) > Integer.valueOf(childResult2))
                {
                    result ="true";
                }
                else {
                    result = "false";
                }
                break;
            case "<":
                childResult1 = dstNode.getChildrenNodes().get(0).getResult();
                childResult2 = dstNode.getChildrenNodes().get(1).getResult();
                if(Integer.valueOf(childResult1) < Integer.valueOf(childResult2))
                {
                    result ="true";
                }
                else {
                    result = "false";
                }
                break;
            case "+":
                childResult1 = dstNode.getChildrenNodes().get(0).getResult();
                childResult2 = dstNode.getChildrenNodes().get(1).getResult();
                result = String.valueOf(Integer.valueOf(childResult1)+Integer.valueOf(childResult2));
                break;
            case "-":
                childResult1 = dstNode.getChildrenNodes().get(0).getResult();
                childResult2 = dstNode.getChildrenNodes().get(1).getResult();
                result = String.valueOf(Integer.valueOf(childResult1)-Integer.valueOf(childResult2));
                break;
            case "!":
                childResult1 = dstNode.getChildrenNodes().get(0).getResult();
                if(childResult1.equals("true"))
                {
                    result ="false";
                }
                else if(childResult1.equals("false")) {
                    result = "true";
                }
                else
                {
                    result="error";
                }
                break;
            case "&":
                childResult1 = dstNode.getChildrenNodes().get(0).getResult();
                childResult2 = dstNode.getChildrenNodes().get(1).getResult();
                if(childResult1.equals("true") && childResult2.equals("true"))
                {
                    result="true";
                }
                else
                {
                    result="false";
                }
                break;
            case "|":
                childResult1 = dstNode.getChildrenNodes().get(0).getResult();
                childResult2 = dstNode.getChildrenNodes().get(1).getResult();
                if(childResult1.equals("true") || childResult2.equals("true"))
                {
                    result = "true";
                }
                else
                {
                    result="false";
                }
            case "NOT":
                childResult1 = dstNode.getChildrenNodes().get(0).getResult();
                if(childResult1.equals("true"))
                {
                    result ="false";
                }
                else {
                    result = "true";
                }
                break;
            case "AND":
                childResult1 = dstNode.getChildrenNodes().get(0).getResult();
                childResult2 = dstNode.getChildrenNodes().get(1).getResult();
                if(childResult1.equals("true") && childResult2.equals("true"))
                {
                    result="true";
                }
                else
                {
                    result="false";
                }
                break;
            case "OR":
                childResult1 = dstNode.getChildrenNodes().get(0).getResult();
                childResult2 = dstNode.getChildrenNodes().get(1).getResult();
                if(childResult1.equals("true") || childResult2.equals("true"))
                {
                    result = "true";
                }
                else
                {
                    result="false";
                }
            case "XOR":
                childResult1 = dstNode.getChildrenNodes().get(0).getResult();
                childResult2 = dstNode.getChildrenNodes().get(1).getResult();
                if(childResult1.equals(childResult1))
                {
                    result = "false";
                }
                else
                {
                    result="true";
                }
            case "IF":
                childResult1 = dstNode.getChildrenNodes().get(0).getResult();
                childResult2 = dstNode.getChildrenNodes().get(1).getResult();
                if(childResult1.equals("false") && childResult2.equals("true"))
                {
                    result = "false";
                }
                else
                {
                    result="true";
                }
                break;
            case "IFF":
                childResult1 = dstNode.getChildrenNodes().get(0).getResult();
                childResult2 = dstNode.getChildrenNodes().get(1).getResult();
                if(childResult1.equals(childResult2))
                {
                    result = "true";
                }
                else
                {
                    result="false";
                }
                break;

        }
        return result;
    }

    private String findResultFromModel(Model model, DSTNode dstNode)
    {
        String result="null";
        String functionName = dstNode.getMove().getData();
        RelationalMap relationalMap = model.getAllFunctionResults().stream().filter(x->(x.getFunctionName().equals(functionName))).collect(Collectors.toList()).get(0);

        List<DSTNode> paramters = dstNode.getChildrenNodes();

        if(paramters.size()!=relationalMap.getRelations().get(0).getIndependentVariables().size())
        {
            result="error";
            //FIXME: bug
            return "error";
        }

        Relation selectedRelation = null;
        for(Relation relation: relationalMap.getRelations())
        {
            boolean allXFit=true;

            for(int i=0;i<paramters.size();i++)
            {
                if(paramters.get(i).getResult().equals(relation.getIndependentVariables().get(i)))
                {
                    continue;
                }
                else
                {
                    allXFit=false;
                    break;
                }
            }
            if(allXFit==true)
            {
                result=relation.getDependentVariables().get(0);
                break;
            }

        }

        return result;
    }

}
