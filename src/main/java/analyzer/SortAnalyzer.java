package analyzer;

import model.EnumLexItemType;
import model.LexItem;
import model.Sort;
import model.Vocabulary;

import java.util.ArrayList;
import java.util.List;

/**
 * User: u6613739
 * Date: 2019/4/6
 * Time: 20:43
 * Description:
 */
public class SortAnalyzer implements AnalyzerImp
{
    private List<Sort> sortList = new ArrayList<>();
    private List<LexItem> lexItemList = new ArrayList<>();
    int scannerIndex = 0;
    public  SortAnalyzer(List<LexItem> lexItemList)
    {
        this.lexItemList = lexItemList;
    }
    @Override
    public void start()
    {
        getSorts();
    }

    private boolean getSorts()
    {
        while (scannerIndex < lexItemList.size())
        {
            int status = 0;
            Sort sort =new Sort();
            while (status != -1)
            {
                LexItem lexItem = lexItemList.get(scannerIndex);
                switch (status)
                {
                    case 0:
                        if(lexItem.getType()!= EnumLexItemType.IDENTIFIER)
                        {
                            return false;
                        }
                        sort.setName(lexItem.getData());
                        status =1;
                        scannerIndex++;
                        break;
                    case 1:
                        if(!(lexItem.getType()==EnumLexItemType.KEYWORD && lexItem.getData().equals("enum")))
                        {
                            return false;
                        }
                        status = 5;
                        scannerIndex++;
                        break;
                    case 2:
                        if(lexItem.getType()!=EnumLexItemType.IDENTIFIER)
                        {
                            return false;
                        }
                        sort.getContains().add(lexItem.getData());
                        status=3;
                        scannerIndex++;
                        break;
                    case 3:
                        if(lexItem.getType()==EnumLexItemType.SEPARATOR && lexItem.getData().equals(","))
                        {
                            status = 4;
                            scannerIndex++;
                        }
                        else if(lexItem.getType()==EnumLexItemType.SEPARATOR && lexItem.getData().equals("."))
                        {
                            status=-1;
                            scannerIndex++;
                        }
                        else
                        {
                            return false;
                        }
                        break;
                    case 4:
                        if(lexItem.getType()!=EnumLexItemType.IDENTIFIER)
                        {
                            return false;
                        }
                        sort.getContains().add(lexItem.getData());
                        status=3;
                        scannerIndex++;
                        break;
                    case 5:
                        if(!(lexItem.getType()==EnumLexItemType.SEPARATOR && lexItem.getData().equals(":")))
                        {
                            return false;
                        }
                        status =2;
                        scannerIndex++;
                        break;
                    case -1:break;
                    default:return false;

                }
            }
            sortList.add(sort);
        }

        return true;
    }

    @Override
    public boolean checkSyntax()
    {
        return false;
    }
}
