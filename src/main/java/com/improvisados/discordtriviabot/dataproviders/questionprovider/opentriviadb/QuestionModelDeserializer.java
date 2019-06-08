package com.improvisados.discordtriviabot.dataproviders.questionprovider.opentriviadb;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.improvisados.trivia4j.logic.Question;
import com.improvisados.trivia4j.logic.QuestionDifficulty;
import com.improvisados.trivia4j.logic.QuestionType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.text.StringEscapeUtils;




/**
 * @author Joaquin Martinez <juacom04@gmail.com>
 */
public class QuestionModelDeserializer implements JsonDeserializer<Question> 
{

    @Override
    public Question deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException
    {
        JsonObject qObj=je.getAsJsonObject();
        String qCategory=qObj.get("category").getAsString();
        QuestionType qType=QuestionType.getValue(qObj.get("type").getAsString());
        
        
        QuestionDifficulty qDifficulty=QuestionDifficulty.getValue(qObj.get("difficulty").getAsString());
        String qQuestion=StringEscapeUtils.unescapeHtml4(qObj.get("question").getAsString());
        
        int correctAnwser=-1;
        List<String> anwsers=new ArrayList<String>();
        
        if(qType==QuestionType.MULTIPLE)
        {
            //scramble anwser
            for(JsonElement incAnw: qObj.get("incorrect_answers").getAsJsonArray())
            {
                anwsers.add(StringEscapeUtils.unescapeHtml4(incAnw.getAsString()));
            }
            Random rnd=new Random();
            correctAnwser=rnd.nextInt(anwsers.size());
            anwsers.add(correctAnwser,StringEscapeUtils.unescapeHtml4(qObj.get("correct_answer").getAsString()));
            
        }
        else if(qType==QuestionType.BOOLEAN)
        {
            anwsers.add("True");
            anwsers.add("False");
            
            if(qObj.get("correct_answer").getAsString().equals("True"))
            {
                correctAnwser=0;
            }
            else
            {
                correctAnwser=1;
            }
        }
        return new Question(qCategory, qType, qDifficulty, qQuestion, anwsers, correctAnwser);
    }
}