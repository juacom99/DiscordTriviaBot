/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.improvisados.discordtriviabot.commands;

import com.improvisados.discordtriviabot.dataproviders.questionprovider.opentriviadb.OpenTriviaDBCategory;
import com.improvisados.discordtriviabot.dataproviders.questionprovider.opentriviadb.OpenTriviaDBQuestionsProvider;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;

/**
 *
 * @author jomartinez
 */
@CommandInfo(
        name =
        {
            "categories"
        },
        description = "List all aveilables categories"
)
public class CategoriesCommand extends Command
{

    public CategoriesCommand(String roleName)
    {
        this.name = "categories";
        this.help = "List all aveilables categories";
        this.guildOnly = true;
        this.requiredRole = roleName;
    }

    @Override
    protected void execute(CommandEvent ce)
    {
        List<OpenTriviaDBCategory> cats = OpenTriviaDBQuestionsProvider.getInstance().getCategories();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("List of Categories");
        String toAppend = "";
        for (OpenTriviaDBCategory cat : cats)
        {
            toAppend = cat.getName() + "   " + cat.getId() + "\n";
            if (builder.length() + toAppend.length() > 6000)
            {
                ce.getChannel().sendMessage(builder.build()).queue();
                builder.setDescription("");

            }
            builder.appendDescription(toAppend);
        }

        ce.getChannel().sendMessage(builder.build()).queue();
    }
}
