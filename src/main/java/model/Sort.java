package model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * User: u6613739
 * Date: 2019/4/6
 * Time: 20:43
 * Description:
 */
@Data
public class Sort
{
    private String name;
    private List<String> contains= new ArrayList<>();
}
