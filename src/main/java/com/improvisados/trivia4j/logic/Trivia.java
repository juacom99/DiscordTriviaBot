/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.improvisados.trivia4j.logic;

import com.improvisados.trivia4j.dataprovider.questionsprovider.QuestionProvider;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import com.improvisados.trivia4j.events.ITriviaEventListener;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author joaquin
 */
public class Trivia implements ActionListener
{

    public static final int DEFAULT_TIME = 20000;
    private static final int[] POINTS =
    {
        7, 5, 3, 1
    };

    private List<Question> questions;
    private int currentQuestion;
    private Timer timer;
    private List<ITriviaEventListener> eventListeners;
    private TriviaState state;
    private int rigthAnswersCount;
    private Map<Object, Integer> scoreboard;
    private List<Object> alreadyAwnser;

    public Trivia()
    {
        this.questions = new ArrayList<>();
        this.currentQuestion = -1;
        this.timer = new Timer(DEFAULT_TIME, this);
        this.timer.setInitialDelay(0);
        this.rigthAnswersCount = -1;
        this.eventListeners = new ArrayList<>();
        this.state = TriviaState.PRE_START;
        this.scoreboard = new HashMap<Object, Integer>();
        this.alreadyAwnser = new ArrayList<Object>();

    }

    public Trivia(QuestionProvider provider)
    {
        this();
        this.questions = provider.getQuestions();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (this.state == TriviaState.PLAYING)
        {
            if (currentQuestion > -1)
            {
                notifyQuestionTimeOut(questions.get(currentQuestion), currentQuestion + 1);
            }
            currentQuestion++;

            if (currentQuestion < questions.size())
            {

                //new Question
                rigthAnswersCount = -1;
                alreadyAwnser.clear();
                notifyNewQuestion(questions.get(currentQuestion), currentQuestion + 1);
            }
            else
            {
                //trivia finished
                stop(false);
            }
        }
    }

    public void addQuestion(Question q)
    {
        if (!questions.contains(q))
        {
            questions.add(q);
        }
    }

    public void addEventListener(ITriviaEventListener listener)
    {
        eventListeners.add(listener);
    }

    public boolean start()
    {
        boolean output = false;
        if (this.state == TriviaState.PRE_START)
        {
            this.state = TriviaState.PLAYING;
            this.timer.start();
            output = true;

            notifyStart();
        }
        return output;
    }

    public boolean stop(boolean canceled)
    {
        boolean output = false;
        if (this.state == TriviaState.PLAYING || this.state==TriviaState.PAUSED)
        {
            timer.stop();
            this.state = TriviaState.FINISHED;
            output = true;
            notifyFinish(canceled);
        }
        return output;
    }

    public boolean pause()
    {
        boolean output=false;
        if (this.state == TriviaState.PLAYING)
        {
            this.timer.stop();
            this.state = TriviaState.PAUSED;
            output=true;
        }
        
        return output;
    }

    public boolean resume()
    {
        boolean output=false;
        if (this.state == TriviaState.PAUSED)
        {
            this.currentQuestion--;
            this.state = TriviaState.PLAYING;
            this.timer.restart();
            output=true;
        }
        
        return output;
    }

    private void notifyStart()
    {
        for (ITriviaEventListener listener : this.eventListeners)
        {
            listener.onTriviaStart();
        }
    }

    private void notifyFinish(boolean wasCanceled)
    {
        for (ITriviaEventListener listener : this.eventListeners)
        {
            listener.onTriviaFinish(wasCanceled);
        }
    }

    private void notifyNewQuestion(Question q, int index)
    {
        for (ITriviaEventListener listener : this.eventListeners)
        {
            listener.onNewQuestion(q, index);
        }
    }

    private void notifyQuestionTimeOut(Question q, int index)
    {
        for (ITriviaEventListener listener : this.eventListeners)
        {
            listener.onQuestionTimeOut(q, index);
        }
    }

    public int size()
    {
        return this.questions.size();
    }

    public int checkAnswer(int index, Object forWho)
    {
        int output = -1;
        if (this.state == TriviaState.PLAYING)
        {            
            if (!alreadyAwnser.contains(forWho) && questions.get(currentQuestion).checkAnswer(index))
            {
                if (rigthAnswersCount < POINTS.length - 1)
                {
                    rigthAnswersCount++;
                }

                output = POINTS[rigthAnswersCount];

                int point = output;

                if (scoreboard.containsKey(forWho))
                {
                    point = scoreboard.get(forWho) + output;
                }

                scoreboard.put(forWho, point);
            }
            alreadyAwnser.add(forWho);
        }
        return output;
    }

    public boolean isPlaying()
    {
        return this.state == TriviaState.PLAYING;
    }

    public List<Entry<Object,Integer>> getWinners()
    {
        List<Entry<Object,Integer>> list=new ArrayList<Entry<Object,Integer>>(scoreboard.entrySet());
        
         list.sort(new Comparator<Entry<Object, Integer>>()
         {
            @Override
            public int compare(Entry<Object, Integer> o1, Entry<Object, Integer> o2)
            {
                return o2.getValue()-o1.getValue();
            }
             
         });
         
         if(list.size()<3)
         {
             return list.subList(0,list.size());
         }
         else
         {
             return list.subList(0,2);
         }
    }
}
