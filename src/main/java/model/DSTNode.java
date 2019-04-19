package model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DSTNode {
    private List<DSTNode> childrenNodes=null;
    private LexItem value = null;
    private LexItem move = null;
    private UUID uuid = null;

    public DSTNode(LexItem value){
        this.value = value;
        uuid=UUID.randomUUID();

    }
}
