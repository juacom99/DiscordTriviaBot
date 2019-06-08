
package com.improvisados.discordtriviabot.dataproviders.questionprovider.opentriviadb;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class OpentDBCategoriesResponse {

    @SerializedName("trivia_categories")
    @Expose
    private List<OpenTriviaDBCategory> categories = null;

    public List<OpenTriviaDBCategory> getCategories()
    {
        return categories;
    }

    

    

}
