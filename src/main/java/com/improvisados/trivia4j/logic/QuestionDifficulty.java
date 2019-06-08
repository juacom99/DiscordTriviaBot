/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.improvisados.trivia4j.logic;

/**
 *
 * @author joaquin
 */
public enum QuestionDifficulty
{
    EASY("easy"),
    MEDIUM("medium"),
    HARD("hard");
    
    private String value;

    private QuestionDifficulty(String value)
    {
        this.value = value;
    }
    
    
    public static QuestionDifficulty getValue(String val) throws IllegalArgumentException
    {
        QuestionDifficulty output=null;
        for(QuestionDifficulty qd:QuestionDifficulty.values())
        {
            if(qd.value.equals(val))
            {
                output=qd;
                break;
            }
        }
        return output;
    }

    @Override
    public String toString()
    {
        return super.toString().toLowerCase();
    }
    
    
    
}
