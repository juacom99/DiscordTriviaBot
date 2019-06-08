/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.improvisados.discordtriviabot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.improvisados.discordtriviabot.configuration.Configuration;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;

/**
 *
 * @author jomartinez
 */
public class Main
{
    public static void main(String[] args)
    {
        
       try
        {
            Configuration cfg=Configuration.getInstance();
            DiscordTriviaBot bot=new DiscordTriviaBot(cfg.getToken(),cfg.getCommandPrefix(),cfg.getRoleName(),cfg.getRoleColor());
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LoginException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
/*
        Gson g=new GsonBuilder().create();
        
        System.out.println(g.toJson(new Color(40,184,141)));

        */
    }
}
