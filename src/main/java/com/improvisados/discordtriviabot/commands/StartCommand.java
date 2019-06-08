/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.improvisados.discordtriviabot.commands;

import com.improvisados.discordtriviabot.DiscordTriviaBot;
import com.improvisados.discordtriviabot.dataproviders.questionprovider.opentriviadb.OpenTriviaDBQuestionsProvider;
import com.improvisados.trivia4j.logic.QuestionDifficulty;
import com.improvisados.trivia4j.logic.QuestionType;
import com.improvisados.trivia4j.logic.Trivia;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 *
 * @author jomartinez
 */
@Author("Joaquin Martinez")

public class StartCommand extends Command
{

    public StartCommand(String roleName)
    {
        this.name = "start";
        this.help = "Start a trivia. Use a: for the questions ammount d: for the difficulty (easy,medium,hard),t: for the type (multiple,boolean) and c: for the category";
        this.guildOnly = true;
        this.requiredRole = roleName;
    }

    @Override
    protected void execute(CommandEvent ce)
    {
        if (ce.getChannelType() == ChannelType.TEXT)
        {
            OpenTriviaDBQuestionsProvider prov = OpenTriviaDBQuestionsProvider.getInstance();
            String params = ce.getArgs();
            if (params != null && params.length() > 0)
            {
                String[] command = params.split(" ");

                String[] values;

                for (String pair : command)
                {
                    values = pair.split(":");
                    if (values.length == 2)
                    {
                        switch (values[0].toLowerCase())
                        {
                            case "c":
                                try
                                {
                                    int cat = Integer.parseInt(values[1]);
                                    prov.withCategory(cat);
                                } catch (NumberFormatException ex)
                                {
                                    //invalid cat
                                }
                                break;
                            case "a":
                                try
                                {
                                    int amount = Integer.parseInt(values[1]);
                                    prov.withAmmount(amount);
                                } catch (NumberFormatException ex)
                                {
                                    //invalid amount
                                }
                                break;
                            case "t":

                                QuestionType type = QuestionType.getValue(values[1]);
                                if (type != null)
                                {
                                    prov.withType(type);
                                }
                                break;
                            case "d":

                                QuestionDifficulty difficulty = QuestionDifficulty.getValue(values[1]);
                                if (difficulty != null)
                                {
                                    prov.withDifficulty(difficulty);
                                }

                                break;
                        }
                    } else
                    {
                        
                    }
                }

            } else
            {
                prov.withAmmount(15);
            }
            Trivia trivia = new Trivia(prov);
                    
                    
                    try
                    {
                        if (DiscordTriviaBot.getInstance().addTrivia(trivia, (TextChannel) ce.getChannel()))
                        {
                            trivia.start();
                        }
                    } catch (LoginException ex)
                    {
                        Logger.getLogger(StartCommand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
        } else
        {
            //not a text channel
        }
    }

}
