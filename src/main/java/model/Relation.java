package model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * User: u6613739
 * Date: 2019/4/12
 * Time: 22:20
 * Description:
 */
@Data
public class Relation
{
    List<String> independentVariables=new ArrayList<>();
    List<String> dependentVariables=new ArrayList<>();

    public Relation (List<String> independentVariables,List<String> dependentVariables)
    {
        this.independentVariables = independentVariables;
        this.dependentVariables=dependentVariables;
    }

}