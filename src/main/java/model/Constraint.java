package model;

import lombok.Data;

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
    private List<LexItem> constraint;

    public Constraint(List<LexItem> constraint)
    {
        this.constraint = constraint;
    }
}
