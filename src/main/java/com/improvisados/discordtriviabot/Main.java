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
import java.io.FileWriter;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.UnknownHostException;
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
            if(cfg.getProxy()==null)
            {
                DiscordTriviaBot bot=new DiscordTriviaBot(cfg.getToken(),cfg.getCommandPrefix(),cfg.getRoleName(),cfg.getRoleColor());
            }
            else
            {
                DiscordTriviaBot bot=new DiscordTriviaBot(cfg.getToken(),cfg.getCommandPrefix(),cfg.getRoleName(),cfg.getRoleColor(),cfg.getProxy());
            }
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LoginException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        /*
        Gson g=new GsonBuilder().setPrettyPrinting().create();
        InetAddress proxyIp;
        try {
            proxyIp = Inet4Address.getByName("Proxy ip or address");
            int proxyPort=8080;
            Configuration cfg=new Configuration("BOT TOKEN", "YOUR DISCORD ID","!","Trivia Master",new Color(40,184,141),proxyIp,proxyPort);
            System.out.println(g.toJson(cfg));    
        } catch (UnknownHostException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IOException ex)
        {
             Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        

        
    }
}
