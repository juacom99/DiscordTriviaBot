package com.improvisados.discordtriviabot.dataproviders.questionprovider.opentriviadb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Joaquin Martinez <juacom04@gmail.com>
 */
public class OpenTriviaDBCategory
{
    @SerializedName("id")
    @Expose
    private int id;
    
    @SerializedName("name")
    @Expose
    private String name;

    public OpenTriviaDBCategory(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
    
    
}
