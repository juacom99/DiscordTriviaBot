package com.improvisados.discordtriviabot.dataproviders.questionprovider.opentriviadb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.improvisados.trivia4j.dataprovider.questionsprovider.QuestionProvider;
import com.improvisados.trivia4j.logic.Question;
import com.improvisados.trivia4j.logic.QuestionDifficulty;
import com.improvisados.trivia4j.logic.QuestionType;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Joaquin Martinez <juacom04@gmail.com>
 */
public class OpenTriviaDBQuestionsProvider extends QuestionProvider
{

    private int amount;
    private int categoty;
    private QuestionDifficulty difficulty;
    private QuestionType type;
    private Gson gson;

    private static OpenTriviaDBQuestionsProvider instance;

    private static String API_BASE_URL = "https://opentdb.com/";

    private OpenTriviaDBQuestionsProvider()
    {
        amount = 15;
        categoty = 0;
        difficulty = null;
        type = null;
        gson = new GsonBuilder().registerTypeAdapter(Question.class, new QuestionModelDeserializer()).setPrettyPrinting().create();
    }

    public void withAmmount(int amount)
    {
        if (amount > -1)
        {
            this.amount = amount;
        }
    }

    public void withCategory(int categoty)
    {
        this.categoty = categoty;
    }

    public void withDifficulty(QuestionDifficulty difficulty)
    {
        this.difficulty = difficulty;
    }

    public void withType(QuestionType type)
    {
        this.type = type;
    }

    @Override
    public List<Question> getQuestions()
    {
        List<Question> output=null;
        try
        {
            URL url=buildQuestionsURL();
            Reader reader=getHttpResponse(url);
            OpentDBQuestionResponse answer = gson.fromJson(reader, OpentDBQuestionResponse.class);
            
            if(answer.getResponseCode()==0)
            {
                output=answer.getQuestions();
            }
        }
        catch (MalformedURLException ex)
        {
            Logger.getLogger(OpenTriviaDBQuestionsProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return output;
    }
    
    
    public List<OpenTriviaDBCategory> getCategories()
    {
        List<OpenTriviaDBCategory> output=new ArrayList<OpenTriviaDBCategory>();
        try
        {
            URL url=buildCategoriesURL();
            Reader reader=getHttpResponse(url);
            
            OpentDBCategoriesResponse response=gson.fromJson(reader,OpentDBCategoriesResponse.class);
            
            output=response.getCategories();
        }
        catch (MalformedURLException ex)
        {
            Logger.getLogger(OpenTriviaDBQuestionsProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                
        return output;
    }

    public static OpenTriviaDBQuestionsProvider getInstance()
    {
        if (instance == null)
        {
            instance = new OpenTriviaDBQuestionsProvider();
        }

        return instance;
    }

    private URL buildQuestionsURL() throws MalformedURLException
    {

        Map<String, String> map = new HashMap<String, String>();

        if (this.amount > 0)
        {
            map.put("amount", this.amount + "");
        }

        if (categoty > 0)
        {
            map.put("category", this.categoty + "");
        }

        if (difficulty != null)
        {
            map.put("difficulty", difficulty.toString());
        }

        if (type != null)
        {
            map.put("type", type.toString());
        }

        URL url = new URL(API_BASE_URL+"api.php" + urlEncodeUTF8(map));

        return url;
    }

    private URL buildCategoriesURL() throws MalformedURLException
    {
         return new URL(API_BASE_URL+"api_category.php");
    }
    
    private static String urlEncodeUTF8(Map<String, String> map)
    {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : map.entrySet())
        {
            if (sb.length() == 0)
            {
                sb.append("?");
            }
            if (sb.length() > 1)
            {
                sb.append("&");
            }
            sb.append(String.format("%s=%s", entry.getKey(), entry.getValue().toString()));
        }
        return sb.toString();
    }

    
    
    private Reader getHttpResponse(URL url)
    {
        Reader reader=null;
        try
        {
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setDoOutput(true);
            request.setRequestMethod("GET");
            request.connect();
            reader = new InputStreamReader((InputStream) request.getContent());
        }
        catch (IOException ex)
        {
            Logger.getLogger(OpenTriviaDBQuestionsProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return reader;
    }
    
    
    
    
}
