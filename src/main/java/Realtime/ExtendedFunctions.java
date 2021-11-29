package Realtime;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ExtendedFunctions {



    public static ArrayList<String> toLines(String text, int symbolsPerLine, String splitBy){

        ArrayList<String> array = new ArrayList<>();        //Array of strings returned
        String[] asIndividualWords = text.split(splitBy);   //When split
        StringBuilder currentLine = new StringBuilder();

        for(String word : asIndividualWords){

            //Just add up the words and mush them together until you reach max

            if((currentLine + word).getBytes(StandardCharsets.UTF_8).length < symbolsPerLine){
                currentLine.append(word).append(" ");

            }else{
                array.add(currentLine.toString());
                currentLine = new StringBuilder();
            }
        }
        return array;
    }
}
