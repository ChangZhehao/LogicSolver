package model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * User: u6613739
 * Date: 2019/4/6
 * Time: 20:39
 * Description:
 */
@Data
public class Constraint
{
    private boolean isConstraint = false;

    private LexItem connectSymbol = null;

    private List<Constraint> childConstraintList=new ArrayList<>();

    private LexItem leaf;

    private List<LexItem> lexItemList=new ArrayList<>();
}
