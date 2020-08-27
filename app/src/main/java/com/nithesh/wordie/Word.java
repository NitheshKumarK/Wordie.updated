package com.nithesh.wordie;

public class Word {
    private String word ;
    private String definition;
    private String pos;
    public Word(){}
    public Word(String word,String pos,String definition){
        this.word = word;
        this.definition = definition;
        this.pos = pos;
    }
    public String getWord(){
        return word;
    }
    public String getDefinition(){
        return definition;
    }
    public String getPos(){return pos;}
}
