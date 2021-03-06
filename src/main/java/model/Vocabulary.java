package model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * User: u6613739
 * Date: 2019/4/6
 * Time: 20:44
 * Description:
 */
@Data
public class Vocabulary
{

    private String functionName;
    private List<EnumVariableType> independentVariablesTypes = new ArrayList<>();
    private List<String> independentVariables = new ArrayList<>();
    private List<EnumVariableType> dependentVariablesTypes = new ArrayList<>();
    private List<String> dependentVariables = new ArrayList<>();
    private List<String> optionSettings = new ArrayList<>();
}
