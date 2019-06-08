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
import net.dv8tion.jda.core.entities.TextChannel;

/**
 *
 * @author jomartinez
 */
public class ResumeCommand extends Command
{

    public ResumeCommand(String roleName)
    {
        this.name = "resume";
        this.help = "Resume the current paused trivia";
        this.guildOnly = true;
        this.requiredRole = roleName;
    }

    @Override
    protected void execute(CommandEvent ce)
    {
        try
        {
            TextChannel channel = (TextChannel) ce.getChannel();
            Trivia trivia = DiscordTriviaBot.getInstance().getTrivia(channel);

            if (trivia != null)
            {
                if (trivia.resume())
                {
                    channel.sendMessage("Trivia ***resumed*** by " + ce.getAuthor().getName() + " on ***" + ce.getGuild().getName() + "*** ***#" + channel.getName() + "***").queue();
                }

            }
        } catch (LoginException ex)
        {
            Logger.getLogger(ResumeCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
