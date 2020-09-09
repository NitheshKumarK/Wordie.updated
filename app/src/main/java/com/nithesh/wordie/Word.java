package com.nithesh.wordie;

import java.io.Serializable;

public class Word implements Serializable {
    private String word ;
    private String definition;
    private String pos;
    private String soundId;
    public Word(){}
    public Word(String word,String pos,String definition,String soundId){
        this.word = word;
        this.definition = definition;
        this.pos = pos;
        this.soundId = soundId;
    }
    public String getWord(){
        return word;
    }
    public String getDefinition(){
        return definition;
    }
    public String getPos(){return pos;}
    public String getSoundId(){return soundId;}
}
