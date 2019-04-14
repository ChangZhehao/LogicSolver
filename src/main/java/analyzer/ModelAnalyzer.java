package analyzer;

import lombok.Data;
import model.*;

import java.util.ArrayList;
import java.util.List;
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
            System.out.println(model);
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

        oneStep(model, constraint);
        String value = constraint.getValue();

        //clean Constraint;
        cleanConstraintTmpValue(constraint);

        if(value.equals("false"))
        {
            return false;
        }
        else {
            return true;
        }
    }
    private void cleanConstraintTmpValue(Constraint constraint)
    {
        constraint.setValue(null);
        for(Constraint child: constraint.getChildConstraintList())
        {
            cleanConstraintTmpValue(child);
        }
    }

    private boolean oneStep(Model model, Constraint constraint)
    {
        if (constraint.getChildConstraintList() == null || constraint.getChildConstraintList().size() == 0)
        {
            constraint.setValue(constraint.getLeaf().getData());
            return true;
        }


        for (Constraint childConstraint : constraint.getChildConstraintList())
        {
            oneStep(model, childConstraint);
        }

        if(constraint.getConnectSymbol()==null)
        {
            constraint.setValue(constraint.getChildConstraintList().get(0).getValue());
        }
        else
        {

            switch (constraint.getConnectSymbol().getType())
            {
                case OPERATOR :
                    constraint.setValue(findValueFromOperator(constraint));
                    break;
                case IDENTIFIER:
                    constraint.setValue(findValueFromModel(model, constraint));
                    break;
                case KEYWORD:
                    constraint.setValue(findValueFromOperator(constraint));
                case SEPARATOR:
                    if(constraint.getConnectSymbol().getData().equals(","))
                    {
                        constraint.setValue(setValueFromFuncParamters(constraint));
                    }
                    break;

            }
        }
        return true;
    }
    private String setValueFromFuncParamters(Constraint constraint)
    {
        StringBuilder sb =new StringBuilder();
        for(int i=0;i<constraint.getChildConstraintList().size();i++)
        {
            sb.append(constraint.getChildConstraintList().get(i).getValue());
            if(i!=constraint.getChildConstraintList().size()-1)
            {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    private String findValueFromOperator(Constraint constraint)
    {
        //TODO: add NOT() AND ,OR XOR, operator
        String value = "null";
        String childValue1,childValue2;
        switch (constraint.getConnectSymbol().getData())
        {
            case "=":
                childValue1 = constraint.getChildConstraintList().get(0).getValue();
                childValue2 = constraint.getChildConstraintList().get(1).getValue();
                if(childValue1.equals(childValue2))
                {
                    value ="true";
                }
                else {
                    value = "false";
                }
                break;
            case "+":
                childValue1 = constraint.getChildConstraintList().get(0).getValue();
                childValue2 = constraint.getChildConstraintList().get(1).getValue();
                value = String.valueOf(Integer.valueOf(childValue1)+Integer.valueOf(childValue2));
                break;
            case "-":
                childValue1 = constraint.getChildConstraintList().get(0).getValue();
                childValue2 = constraint.getChildConstraintList().get(1).getValue();
                value = String.valueOf(Integer.valueOf(childValue1)-Integer.valueOf(childValue2));
                break;
            case "!":
                childValue1 = constraint.getChildConstraintList().get(0).getValue();
                if(childValue1.equals("true"))
                {
                    value ="false";
                }
                else {
                    value = "true";
                }
                break;
            case "&":
                childValue1 = constraint.getChildConstraintList().get(0).getValue();
                childValue2 = constraint.getChildConstraintList().get(1).getValue();
                if(childValue1.equals("true") && childValue2.equals("true"))
                {
                    value="true";
                }
                else
                {
                    value="false";
                }
                break;
            case "|":
                childValue1 = constraint.getChildConstraintList().get(0).getValue();
                childValue2 = constraint.getChildConstraintList().get(1).getValue();
                if(childValue1.equals("true") || childValue2.equals("true"))
                {
                    value = "true";
                }
                else
                {
                    value="false";
                }
            case "NOT":
                childValue1 = constraint.getChildConstraintList().get(0).getValue();
                if(childValue1.equals("true"))
                {
                    value ="false";
                }
                else {
                    value = "true";
                }
                break;
            case "AND":
                childValue1 = constraint.getChildConstraintList().get(0).getValue();
                childValue2 = constraint.getChildConstraintList().get(1).getValue();
                if(childValue1.equals("true") && childValue2.equals("true"))
                {
                    value="true";
                }
                else
                {
                    value="false";
                }
                break;
            case "OR":
                childValue1 = constraint.getChildConstraintList().get(0).getValue();
                childValue2 = constraint.getChildConstraintList().get(1).getValue();
                if(childValue1.equals("true") || childValue2.equals("true"))
                {
                    value = "true";
                }
                else
                {
                    value="false";
                }
            case "XOR":
                childValue1 = constraint.getChildConstraintList().get(0).getValue();
                childValue2 = constraint.getChildConstraintList().get(1).getValue();
                if(childValue1.equals(childValue1))
                {
                    value = "false";
                }
                else
                {
                    value="true";
                }

        }
        return value;
    }

    private String findValueFromModel(Model model, Constraint constraint)
    {
        String value="null";
        String functionName = constraint.getConnectSymbol().getData();
        RelationalMap relationalMap = model.getAllFunctionResults().stream().filter(x->(x.getFunctionName().equals(functionName))).collect(Collectors.toList()).get(0);

        String[] paramters = constraint.getChildConstraintList().get(0).getValue().split(",");

        if(paramters.length!=relationalMap.getRelations().get(0).getIndependentVariables().size())
        {
            value="false";
            //FIXME: bug
            return "null";
        }

        Relation selectedRelation = null;
        for(Relation relation: relationalMap.getRelations())
        {
            boolean allXFit=true;

            for(int i=0;i<paramters.length;i++)
            {
                if(paramters[i].equals(relation.getIndependentVariables().get(i)))
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
                value=relation.getDependentVariables().get(0);
                break;
            }

        }

        return value;
    }

}
