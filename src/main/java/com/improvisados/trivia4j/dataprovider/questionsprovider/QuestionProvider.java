/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.improvisados.trivia4j.dataprovider.questionsprovider;

import com.improvisados.trivia4j.logic.Question;
import java.util.List;

/**
 *
 * @author joaquin
 */
public abstract class QuestionProvider
{
    public abstract List<Question> getQuestions();
}
