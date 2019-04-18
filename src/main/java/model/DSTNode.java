package model;

import lombok.Data;

import java.util.List;
@Data
public class DSTNode {
    private DSTNode parentNode =null;
    private List<DSTNode> childrenNodes=null;
    private LexItem value = null;
    private LexItem move = null;

    public DSTNode(LexItem value){
        this.value = value;

    }
}
