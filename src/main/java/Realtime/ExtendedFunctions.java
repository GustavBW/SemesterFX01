package Realtime;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class ExtendedFunctions {



    public static ArrayList<String> toLines(String text, int symbolsPerLine, String splitBy){

        ArrayList<String> array = new ArrayList<>();        //Array of strings returned
        String[] asIndividualWords = text.split(splitBy);   //When split


        StringBuilder currentLine = new StringBuilder();
        int wordsRolledThrough = 0;

        for(String word : asIndividualWords){

            //Just add up the words and mush them together until you reach max
            if(asIndividualWords.length - wordsRolledThrough > 1) {

                if ((currentLine + word).getBytes(StandardCharsets.UTF_8).length < symbolsPerLine) {
                    currentLine.append(word).append(" ");
                    wordsRolledThrough++;

                } else {
                    array.add(currentLine.toString());
                    currentLine = new StringBuilder();
                    currentLine.append(word).append(" ");
                    wordsRolledThrough++;
                }

            }else{
                array.add(currentLine + word);
            }
        }
        return array;
    }
}
