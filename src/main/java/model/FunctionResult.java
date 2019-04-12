package model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * User: u6613739
 * Date: 2019/4/12
 * Time: 22:28
 * Description:
 */
@Data
public class FunctionResult
{
    String functionName="default";
    List<RelationalMap> relationalMaps=new ArrayList<>();
}
