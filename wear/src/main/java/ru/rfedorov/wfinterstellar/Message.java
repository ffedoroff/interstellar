package ru.rfedorov.wfinterstellar;

import java.util.ArrayList;
import java.util.List;


public class Message {
    Character[] alpha = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', '0', ' '};
    String[] dottie = { ".-", "-...", "-.-.", "-..", ".", "..-.", "--.",
            "....", "..", ".---", "-.-", ".-..", "--", "-.", "---", ".--.",
            "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-",
            "-.--", "--..", ".----", "..---", "...--", "....-", ".....",
            "-....", "--...", "---..", "----.", "-----", " "};


    private ArrayList<Boolean> wordCode = new ArrayList<>();
    private int index = 0;

    public void setMessage(String word){
        if (messageExist()){
            wordCode.clear();
        }
        word += " ";
        for(int i = 0; i < word.length(); i++){
            Character character = word.charAt(i);
            for (int j = 0 ; j < alpha.length; j++){
                if (character.equals(alpha[j])){
                    convertCharToBit(dottie[j]);
                    wordCode.addAll(convertCharToBit(dottie[j]));
                    break;
                }
            }
            wordCode.add(false);
            wordCode.add(false);
        }
    }

    private List<Boolean> convertCharToBit(String morse){
        List<Boolean> res = new ArrayList<>();
        for(int i = 0; i < morse.length(); i++){
            Character c = morse.charAt(i);
            if (c.equals('.')){
                res.add(true);
            }else if (c.equals('-')){
                res.add(true);
                res.add(true);
                res.add(true);
            }else if (c.equals(' ')){
                res.add(false);
                res.add(false);
                res.add(false);
                res.add(false);
                res.add(false);
                res.add(false);
            }
            res.add(false);
        }
        return res;
    }

    public boolean messageExist(){
        return wordCode.size() > 0;
    }

    public int getCode(int sec){
        if (wordCode.get(index)){
            sec--;
        }
        index++;
        if (index >= wordCode.size() - 1){
//            wordCode.clear();
            index = 0;
        }
        return sec;
    }

    public void stopMessage(){
        wordCode.clear();
    }

}
