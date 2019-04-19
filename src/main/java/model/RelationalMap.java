package model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


/**
 * User: u6613739
 * Date: 2019/4/12
 * Time: 20:51
 * Description:
 */
@Data
public class RelationalMap
{

    private String functionName = "";
    private List<Relation> relations = new ArrayList<>();
    public String printResult()
    {
        StringBuilder sb =new StringBuilder();
        for(Relation relation: relations)
        {
            sb.append(relation.printResult()+"\r\n");
        }

        return sb.toString();
    }
}
