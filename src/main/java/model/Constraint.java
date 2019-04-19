package model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class Constraint {


    private DSTNode root = null;
    private List<DSTNode> dstNodes = new ArrayList<>();
}
