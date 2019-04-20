package util;

import model.DSTNode;
import model.EnumLexItemType;

import java.util.List;

public class PrintUtil {

    public static void printDSTNodeList(List<DSTNode> list)
    {
        if(list==null)
        {
            return ;
        }
        for(DSTNode item : list)
        {
            printDSTNodeList(item.getChildrenNodes());
            System.out.print(item.getValue().getData() + "");

        }
        return;
    }
}
