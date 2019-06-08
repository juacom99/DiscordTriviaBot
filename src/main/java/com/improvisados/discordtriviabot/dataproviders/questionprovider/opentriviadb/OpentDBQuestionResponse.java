
package com.improvisados.discordtriviabot.dataproviders.questionprovider.opentriviadb;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.improvisados.trivia4j.logic.Question;

/**
 * @author Joaquin Martinez <juacom04@gmail.com>
 */
public class OpentDBQuestionResponse {

    @SerializedName("response_code")
    @Expose
    private Integer responseCode;
    @SerializedName("results")
    @Expose
    private List<Question> questions = null;

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> results) {
        this.questions = results;
    }

    

}
