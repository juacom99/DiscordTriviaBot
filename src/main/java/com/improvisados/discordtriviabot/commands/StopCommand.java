/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.improvisados.discordtriviabot.commands;

import com.improvisados.discordtriviabot.DiscordTriviaBot;
import com.improvisados.trivia4j.logic.Trivia;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 *
 * @author jomartinez
 */
public class StopCommand extends Command
{

    public StopCommand(String roleName)
    {
        this.name = "stop";
        this.help = "End the current trivia";
        this.guildOnly = true;
        this.requiredRole = roleName;
    }
    

    @Override
    protected void execute(CommandEvent ce)
    {
        TextChannel channel=(TextChannel) ce.getChannel();
                Trivia trivia;
        try
        {
            trivia = DiscordTriviaBot.getInstance().getTrivia(channel);
            if(trivia!=null)
                    {
                        if(trivia.stop(true))
                        {
                            channel.sendMessage("Trivia ***stoped*** by "+ce.getAuthor().getName()+" on ***"+ce.getGuild().getName()+"*** ***#"+channel.getName()+"***").queue();
                        }
                    }
        } catch (LoginException ex)
        {
            Logger.getLogger(StopCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
                    
                    
    }
    
}
