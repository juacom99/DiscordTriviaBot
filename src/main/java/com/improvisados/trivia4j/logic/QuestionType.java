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
public enum QuestionType
{
    MULTIPLE("multiple"),
    BOOLEAN("boolean");
    
    private String value;

    private QuestionType(String value)
    {
        this.value = value;
    }
    
    public static QuestionType getValue(String val) throws IllegalArgumentException
    {
        QuestionType output=null;
        for(QuestionType qt:QuestionType.values())
        {
            if(qt.value.equals(val))
            {
                output=qt;
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
