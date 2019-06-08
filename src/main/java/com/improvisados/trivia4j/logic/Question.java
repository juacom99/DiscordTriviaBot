/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.improvisados.trivia4j.logic;

import java.util.List;

/**
 *
 * @author joaquin
 */
public class Question
{
    private QuestionDifficulty difficulty;
    private QuestionType type;
    private String category;
    private String question;
    private List<String> anwsers;
    private int correctAnwser;

    public Question()
    {
    }

    public Question(String category,QuestionType type,QuestionDifficulty difficulty,String question, List<String> anwsers, int correctAnwser)
    {
        this.difficulty = difficulty;
        this.type = type;
        this.category = category;
        this.question = question;
        this.anwsers = anwsers;
        this.correctAnwser = correctAnwser;
    }

    public QuestionDifficulty getDifficulty()
    {
        return difficulty;
    }

    public QuestionType getType()
    {
        return type;
    }

    public String getCategory()
    {
        return category;
    }

    public String getQuestion()
    {
        return question;
    }

    public List<String> getAnwsers()
    {
        return anwsers;
    }
    
    
    public boolean checkAnswer(int index)
    {
        return this.correctAnwser==index;
    }
    
    public boolean checkAnswer(String answer)
    {
        return anwsers.get(correctAnwser).equalsIgnoreCase(answer);
    }
    
    public int getCorrectAnwser()
    {
        return correctAnwser;
    }
     public String getCorrectAnwserText()
    {
        return anwsers.get(correctAnwser);
    }


     
     
}
