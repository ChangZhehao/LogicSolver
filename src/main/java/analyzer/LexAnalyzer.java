package analyzer;

import lombok.Data;
import model.EnumLexItemType;
import model.EnumLexStatus;
import model.LexItem;

import java.util.ArrayList;
import java.util.List;

/**
 * User: u6613739
 * Date: 2019/4/3
 * Time: 21:49
 * Description:
 */
@Data
public class LexAnalyzer
{
    private String[] keyWords = {"enum", "function","all_different","hidden","AND","OR","XOR","NOT","IF","IFF","BELONG","nat","SOME","ALL"};
    private char[] operators = {'=', '+', '-','*','/', '>','<' ,'!', '&', '|'};
    private char[] seperators = {':', ',', '.', '(', ')', '{', '}'};

    private List<LexItem> lexList = new ArrayList<>();

    public void lexAnalyze(String text)
    {
        text = filterResource(text);
        int scannerIndex = 0;

        while (scannerIndex < text.length())
        {
            EnumLexStatus status = EnumLexStatus.STA_START;
            StringBuilder process = new StringBuilder();
            while (status != EnumLexStatus.STA_DONE)
            {
                System.out.println(status.toString() + scannerIndex);
                char ch = text.charAt(scannerIndex);
                System.out.println(ch);
                switch (status)
                {
                    case STA_START:
                        if (isSpaceOrLineBreak(ch))
                        {
                            status = EnumLexStatus.STA_START;
                            scannerIndex++;
                        }
                        else if (isDigit(ch))
                        {
                            status = EnumLexStatus.STA_NUMBER;
                            process.append(ch);
                            scannerIndex++;
                        }
                        else if (isLetter(ch))
                        {
                            status = EnumLexStatus.STA_ID_OR_KEYWORD;
                            process.append(ch);
                            scannerIndex++;
                        }
                        else if (isOperator(ch))
                        {
                            status = EnumLexStatus.STA_DONE;
                            process.append(ch);
                            LexItem lexItem = new LexItem(EnumLexItemType.OPERATOR, process.toString());
                            lexList.add(lexItem);
                            scannerIndex++;
                        }
                        else if (isSeperator(ch))
                        {
                            status = EnumLexStatus.STA_DONE;
                            process.append(ch);
                            LexItem lexItem = new LexItem(EnumLexItemType.SEPARATOR, process.toString());
                            lexList.add(lexItem);
                            scannerIndex++;
                        }
                        else
                        {
                            status = EnumLexStatus.STA_DONE;
                            LexItem lexItem = new LexItem(EnumLexItemType.UNKNOWN, process.toString());
                            lexList.add(lexItem);
                            break;
                            //wrong
                        }
                        break;
                    case STA_NUMBER:
                        if (isDigit(ch))
                        {
                            status = EnumLexStatus.STA_NUMBER;
                            process.append(ch);
                            scannerIndex++;
                        }
                        else
                        {
                            status = EnumLexStatus.STA_DONE;
                            LexItem lexItem = new LexItem(EnumLexItemType.NUMBER, process.toString());
                            lexList.add(lexItem);
                        }
                        break;
                    case STA_ID_OR_KEYWORD:
                        if (isLetter(ch))
                        {
                            status = EnumLexStatus.STA_ID_OR_KEYWORD;
                            process.append(ch);
                            scannerIndex++;
                        }
                        else
                        {
                            status = EnumLexStatus.STA_DONE;
                            LexItem lexItem;
                            if (isKeyWord(process.toString()))
                            {
                                lexItem = new LexItem(EnumLexItemType.KEYWORD, process.toString());
                            }
                            else
                            {
                                lexItem = new LexItem(EnumLexItemType.IDENTIFIER, process.toString());
                            }
                            lexList.add(lexItem);
                        }
                        break;
                    case STA_DONE:
                        break;
                }
            }
        }
    }

    private boolean isSpaceOrLineBreak(char character)
    {
        if (character == ' ' || character == '\r' || character == '\n')
        {
            return true;
        }
        return false;
    }

    private boolean isKeyWord(String data)
    {
        for (String reserveWord : keyWords)
        {
            if (data.equals(reserveWord))
            {
                return true;
            }
        }
        return false;
    }

    private String filterResource(String resource)
    {
        return resource.trim();
    }

    private boolean isOperator(char character)
    {
        for (int i = 0; i < operators.length; i++)
        {
            if (character == operators[i])
            {
                return true;
            }
        }
        return false;
    }

    private boolean isSeperator(char character)
    {
        for (int i = 0; i < seperators.length; i++)
        {
            if (character == seperators[i])
            {
                return true;
            }
        }
        return false;
    }

    private boolean isLetter(char letter)
    {
        if (letter >= 'a' && letter <= 'z')
        {
            return true;
        }
        if (letter >= 'A' && letter <= 'Z')
        {
            return true;
        }
        else if(letter=='_')
        {
            return true;
        }
        return false;
    }

    private boolean isDigit(char digit)
    {
        if (digit >= '0' && digit <= '9')
        {
            return true;
        }
        return false;
    }

    public void printLexResult()
    {
        for (LexItem lexItem : lexList)
        {
            System.out.println(String.format("< %s,  %s >", lexItem.getType(), lexItem.getData()));
        }
    }
}
