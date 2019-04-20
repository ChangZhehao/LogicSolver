package model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class DSTNode implements Cloneable{
    private List<DSTNode> childrenNodes=null;
    private LexItem value = null;
    private LexItem move = null;
    private UUID uuid = null;
    private String result = null;

    public DSTNode(LexItem value){
        this.value = value;
        uuid=UUID.randomUUID();

    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        Object object = super.clone();
        if(value!=null) {
            ((DSTNode) object).setValue((LexItem) value.clone());
        }
        if(move!=null) {
            ((DSTNode) object).setMove((LexItem) move.clone());
        }

        ((DSTNode)object).uuid = UUID.randomUUID();

        if(childrenNodes!=null)
        {
            ((DSTNode)object).setChildrenNodes(new ArrayList<>());
            for(DSTNode node:childrenNodes)
            {
                ((DSTNode)object).getChildrenNodes().add((DSTNode) node.clone());
            }
        }
        return object;
    }
}
