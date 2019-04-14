package analyzer;

import model.Constraint;
import model.Model;
import model.Vocabulary;

import java.util.ArrayList;
import java.util.List;

/**
 * User: u6613739
 * Date: 2019/4/14
 * Time: 18:14
 * Description:
 */
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
        for(Model model:allModelList)
        {
            if(isVaildModel(model))
            {
                allFitModelList.add(model);
            }
            //clean
        }
    }
    private boolean isVaildModel(Model model)
    {
        boolean isValid = true;
        for(Constraint constraint:constraintList)
        {
            if(!isFitConstraint(model,constraint))
            {
                isValid=false;
                break;
            }
        }
        return isValid;
    }
    private boolean isFitConstraint(Model model,Constraint constraint)
    {
        //TODO: isFitConstraint
        return false;
    }

}
