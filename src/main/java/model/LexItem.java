package model;

import lombok.Data;

import java.util.List;

/**
 * User: u6613739
 * Date: 2019/4/3
 * Time: 22:02
 * Description:
 */
@Data
public class LexItem
{
    private EnumLexItemType type;
    private String data;

    public LexItem(EnumLexItemType type,String data)
    {
        this.type =type;
        this.data=data;
    }
}
