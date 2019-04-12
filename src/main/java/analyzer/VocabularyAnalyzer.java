package analyzer;

import model.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
    private List<Sort> sortList = new ArrayList<>();
    private List<Model> modelList =new ArrayList<>();
    int scannerIndex = 0;

    public VocabularyAnalyzer(List<LexItem> lexItemList,List<Sort> sortList)
    {
        this.lexItemList = lexItemList;
        this.sortList = sortList;
    }


    @Override
    public void start()
    {
        getVocabularys();
        getAllModel();
    }

    private void getAllModel()
    {
        List<FunctionResult> functionResultList = new ArrayList<>();
        for(Vocabulary vocabulary: vocabularyList)
        {
            List<List<String>> allXCombinations = new ArrayList<>();
            List<String> independentVariables = vocabulary.getIndependentVariables();
            List<String> dependentVariables  = vocabulary.getDependentVariables();
            getAllXOrYCombinations(independentVariables,0,new ArrayList<>(),allXCombinations);
            List<List<String>> allYCombinations = new ArrayList<>();
            getAllXOrYCombinations(dependentVariables,0,new ArrayList<>(),allYCombinations);
            FunctionResult functionResult = new FunctionResult();
            functionResult.setFunctionName(vocabulary.getFunctionName());
            getXAndYCombinations(0,allXCombinations,allYCombinations,new RelationalMap(),functionResult);
            checkOptionalSettings(functionResult,vocabulary.getOptionSettings());
            functionResultList.add(functionResult);

        }
        getAllModels(functionResultList,0,new Model());
    }
    private void checkOptionalSettings(FunctionResult functionResult,List<String> optionSettings)
    {
        for(String setting:optionSettings)
        {
            if(setting.equals("all_different"))
            {
                checkAllDifferent(functionResult);
            }
        }
    }
    private void checkAllDifferent(FunctionResult functionResult)
    {
        List<Relation> checkList =new ArrayList<>();
        List<RelationalMap> noDuplicatedRelationalMaps = new ArrayList<>();
        for(RelationalMap relationalMap:functionResult.getRelationalMaps())
        {
            boolean isDuplicate =false;
            checkList=new ArrayList<>();
            for(Relation relation: relationalMap.getRelations())
            {
                if(isElementDuplicateInCheckList(relation,checkList))
                {
                    isDuplicate =true;
                    break;
                }
                checkList.add(relation);

            }
            if(isDuplicate==false)
            {
                noDuplicatedRelationalMaps.add(relationalMap);
            }

        }
        functionResult.setRelationalMaps(noDuplicatedRelationalMaps);
    }
    private boolean isElementDuplicateInCheckList(Relation element,List<Relation> checkList)
    {

        for(Relation relation:checkList)
        {
            List<String> list1 = element.getDependentVariables();
            List<String> list2 = relation.getDependentVariables();
            if(list2.size()!=list1.size())
            {
                return true;
            }
            for(int i=0;i<list1.size();i++)
            {
                if(list1.get(i).equals(list2.get(i)))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private void getAllModels(List<FunctionResult> functionResultList,int functionResultIndex,Model model)
    {
        if(functionResultIndex>=functionResultList.size())
        {
            Model curModel = new Model();
            curModel.getAllFunctionResults().addAll(model.getAllFunctionResults());
            modelList.add(curModel);
            return;
        }


        for(RelationalMap relationalMap:functionResultList.get(functionResultIndex).getRelationalMaps())
        {
            model.getAllFunctionResults().add(relationalMap);
            getAllModels(functionResultList,functionResultIndex+1,model);
            model.getAllFunctionResults().remove(relationalMap);
        }
    }


    private void getXAndYCombinations(int xCombinationIndex, List<List<String>> allXCombinations, List<List<String>> allYCombinations, RelationalMap relationalMap,FunctionResult functionResult)
    {
        if(xCombinationIndex>=allXCombinations.size())
        {
            RelationalMap curRelationalMap = new RelationalMap();
            curRelationalMap.getRelations().addAll(relationalMap.getRelations());
            functionResult.getRelationalMaps().add(curRelationalMap);
            return;
        }

        for(List<String> yCombination : allYCombinations)
        {
            Relation relation=new Relation(allXCombinations.get(xCombinationIndex),yCombination);
            relationalMap.getRelations().add(relation);
            getXAndYCombinations(xCombinationIndex+1,allXCombinations,allYCombinations,relationalMap,functionResult);
            relationalMap.getRelations().remove(relation);
        }
    }

    private void getAllXOrYCombinations(List<String> varibles,int scanIndex,List<String>combination,List<List<String>> allCombinations)
    {
        if(scanIndex>=varibles.size())
        {
            List<String> newCombination = new ArrayList<>(combination);
            allCombinations.add(newCombination);
            return;
        }

        Sort sort = getSortFromSortName(varibles.get(scanIndex));

        for(String contain : sort.getContains())
        {
            combination.add(contain);
            getAllXOrYCombinations(varibles,scanIndex+1,combination,allCombinations);
            combination.remove(contain);
        }
    }

    private Sort getSortFromSortName(String sortName)
    {
        for(Sort sort : sortList)
        {
            if(sortName.equals(sort.getName()))
            {
                return sort;
            }
        }
        return null;
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
