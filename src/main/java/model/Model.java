package model;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: u6613739
 * Date: 2019/4/12
 * Time: 20:50
 * Description:
 */
@Data
public class Model
{
    List<RelationalMap> allFunctionResults = new ArrayList<>();
    boolean isValid = false;

}
