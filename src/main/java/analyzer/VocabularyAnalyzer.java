package analyzer;

import model.EnumLexItemType;
import model.LexItem;
import model.Vocabulary;

import java.util.ArrayList;
import java.util.List;


/**
 * User: u6613739
 * Date: 2019/4/6
 * Time: 20:43
 * Description:
 */
public class VocabularyAnalyzer implements AnalyzerImp
{
    private List<Vocabulary> vocabularyList = new ArrayList<>();
    private List<LexItem> lexItemList = new ArrayList<>();
    int scannerIndex = 0;

    public VocabularyAnalyzer(List<LexItem> lexItemList)
    {
        this.lexItemList = lexItemList;
    }


    @Override
    public void start()
    {
        getVocabularys();

    }

    private boolean getVocabularys()
    {

        while (scannerIndex < lexItemList.size())
        {
            int status = 0;
            Vocabulary vocabulary =new Vocabulary();
            while (status != -1)
            {
                LexItem lexItem = lexItemList.get(scannerIndex);
                switch (status)
                {
                    case 0:
                        if(!(lexItem.getType()==EnumLexItemType.KEYWORD && lexItem.getData().equals("function")))
                        {
                            return false;
                        }
                        status = 1;
                        scannerIndex++;
                        break;
                    case 1:
                        if(lexItem.getType()!=EnumLexItemType.IDENTIFIER)
                        {
                            return false;
                        }
                        vocabulary.setFunctionName(lexItem.getData());
                        scannerIndex++;
                        status = 2;
                        break;
                    case 2:
                        if(!(lexItem.getType()==EnumLexItemType.SEPARATOR && lexItem.getData().equals("(")))
                        {
                            return false;
                        }
                        scannerIndex++;
                        status=3;
                        break;
                    case 3:
                        if(lexItem.getType()!=EnumLexItemType.IDENTIFIER)
                        {
                            return false;
                        }
                        vocabulary.getIndependentVariables().add(lexItem.getData());
                        scannerIndex++;
                        status=4;
                        break;
                    case 4:
                        if(lexItem.getType()==EnumLexItemType.SEPARATOR && lexItem.getData().equals(")"))
                        {
                            scannerIndex++;
                            status=5;
                        }
                        else if(lexItem.getType()==EnumLexItemType.SEPARATOR && lexItem.getData().equals(","))
                        {
                            scannerIndex++;
                            status=8;
                        }
                        else
                        {
                            return false;
                        }
                        break;
                    case 5:
                        if(!(lexItem.getType()==EnumLexItemType.SEPARATOR && lexItem.getData().equals(":")))
                        {
                            return false;
                        }
                        scannerIndex++;
                        status=6;
                        break;
                    case 6:
                        if(lexItem.getType()!=EnumLexItemType.IDENTIFIER)
                        {
                            return false;
                        }
                        vocabulary.getDependentVariables().add(lexItem.getData());
                        scannerIndex++;
                        status=7;
                        break;
                    case 7:
                        if(lexItem.getType()==EnumLexItemType.SEPARATOR && lexItem.getData().equals("{"))
                        {
                            scannerIndex++;
                            status=9;
                        }
                        else if(lexItem.getType()==EnumLexItemType.SEPARATOR && lexItem.getData().equals("."))
                        {
                            scannerIndex++;
                            status=-1;
                        }
                        else
                        {
                            return false;
                        }
                        break;
                    case 8:
                        if(lexItem.getType()!=EnumLexItemType.IDENTIFIER)
                        {
                            return false;
                        }
                        vocabulary.getIndependentVariables().add(lexItem.getData());
                        scannerIndex++;
                        status=4;
                        break;
                    case 9:
                        if(lexItem.getType()!=EnumLexItemType.KEYWORD)
                        {
                            return false;
                        }
                        vocabulary.getOptionSettings().add(lexItem.getData());
                        scannerIndex++;
                        status=10;
                        break;
                    case 10:
                        if(lexItem.getType()==EnumLexItemType.SEPARATOR && lexItem.getData().equals(","))
                        {
                            scannerIndex++;
                            status=11;
                        }
                        else if(lexItem.getType()==EnumLexItemType.SEPARATOR && lexItem.getData().equals("}"))
                        {
                            scannerIndex++;
                            status=12;
                        }
                        else
                        {
                            return false;
                        }
                        break;

                    case 11:
                        if(lexItem.getType()!=EnumLexItemType.KEYWORD)
                        {
                            return false;
                        }
                        vocabulary.getOptionSettings().add(lexItem.getData());
                        scannerIndex++;
                        status=10;
                        break;
                    case 12:
                        if(!(lexItem.getType()==EnumLexItemType.SEPARATOR && lexItem.getData().equals(".")))
                        {
                            return false;
                        }
                        scannerIndex++;
                        status = -1;
                        break;
                    case -1:
                        break;
                    default:return false;


                }
            }
            vocabularyList.add(vocabulary);
        }

        return true;
    }


    @Override
    public boolean checkSyntax()
    {
        return false;
    }
}
