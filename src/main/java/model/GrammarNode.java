package model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * User: u6613739
 * Date: 2019/4/17
 * Time: 13:38
 * Description:
 */
@Data
public class GrammarNode
{
    String name = "";
    LexItem nodeContent=null;
    List<GrammarNode> childrenNode=null;

}
