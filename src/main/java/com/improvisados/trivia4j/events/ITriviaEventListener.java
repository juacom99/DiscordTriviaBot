/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.improvisados.trivia4j.events;

import com.improvisados.trivia4j.logic.Question;

/**
 *
 * @author joaquin
 */
public interface ITriviaEventListener
{
    public void onTriviaStart();
    
    public void onNewQuestion(Question q,int index);
    
    public void onQuestionTimeOut(Question q,int index);
    
    public void onTriviaFinish(boolean wasCanceled);
}
