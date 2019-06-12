package com.improvisados.discordtriviabot;


import com.improvisados.discordtriviabot.commands.CategoriesCommand;
import com.improvisados.discordtriviabot.commands.PauseCommand;
import com.improvisados.discordtriviabot.commands.ResumeCommand;
import com.improvisados.discordtriviabot.commands.StartCommand;
import com.improvisados.discordtriviabot.commands.StopCommand;
import com.improvisados.discordtriviabot.configuration.Configuration;
import com.improvisados.trivia4j.events.ITriviaEventListener;
import com.improvisados.trivia4j.logic.Question;
import com.improvisados.trivia4j.logic.QuestionDifficulty;
import com.improvisados.trivia4j.logic.Trivia;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.utils.PermissionUtil;
import okhttp3.OkHttpClient;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @author Joaquin Martinez <juacom04@gmail.com>
 */
public class DiscordTriviaBot extends ListenerAdapter
{

    private JDA client;
    private static final Logger logger = LogManager.getLogger(DiscordTriviaBot.class.getName());
    private String roleName;
    private HashMap<Channel,Trivia> activeTrivias;
    public  Color roleColor;
    public static final String ICON_URL="https://cdn.discordapp.com/app-icons/441072535413719041/1d3c39467cbd0c85703fdb25b4fb256d.png";
    
    private static DiscordTriviaBot instance;

    public DiscordTriviaBot(String token, String commandPrefix,String roleName,Color roleColor) throws LoginException
    {
        this.roleName=roleName;
        this.roleColor=roleColor;
        this.activeTrivias=new HashMap<Channel,Trivia>();
        this.client = new JDABuilder(AccountType.BOT).setToken(token).build();
        this.client.addEventListener(this);
    }
    
        public DiscordTriviaBot(String token, String commandPrefix,String roleName,Color roleColor,Proxy proxy) throws LoginException
    {
        this.roleName=roleName;
        this.roleColor=roleColor;
        this.activeTrivias=new HashMap<Channel,Trivia>();
        OkHttpClient.Builder builder = new OkHttpClient.Builder().proxy(proxy);        
        this.client = new JDABuilder(AccountType.BOT).setHttpClientBuilder(builder).setToken(token).build();
        this.client.addEventListener(this);
      
    }
        

    @Override
    public void onReady(ReadyEvent event)
    {
        logger.trace("Bot is ready");
         String owner="368791176796700672";
        
        try
        {
            Configuration cfg=Configuration.getInstance();
            owner=cfg.getOwner();
        } catch (FileNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(DiscordTriviaBot.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         CommandClientBuilder builder = new CommandClientBuilder();
        builder.setPrefix("!");
        builder.setOwnerId(owner);
        builder.setGame(Game.playing("Trivia"));
        builder.addCommands(new StartCommand(this.roleName),new CategoriesCommand(this.roleName),new StopCommand(roleName),new PauseCommand(roleName),new ResumeCommand(roleName));
        

        CommandClient clientCommand = builder.build();
        
        client.addEventListener(clientCommand);
        
                
        
       
      
    }

    @Override
    public void onGuildReady(GuildReadyEvent event)
    {        
         Guild guild=event.getGuild();
       List<Role> rols=guild.getRolesByName(roleName, true);
       if (rols.size() == 0)
       {
           if(!PermissionUtil.checkPermission(guild.getSelfMember(),Permission.MANAGE_ROLES))
           {
               guild.getOwner().getUser().openPrivateChannel().complete().sendMessage("The bot don't have permission to create Role "+roleName+".Please invite the bot with the MANAGE ROLE permission").queue();
               guild.leave().completeAfter(5,TimeUnit.SECONDS);
               return;
           }

            logger.info("Adding Role "+this.roleName+" to guild "+guild.getName());
            buildRole(guild);
       }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Trivia t;
        if((t=activeTrivias.get(event.getChannel()))!=null && t.isPlaying())
        {
            //there is an active trivia check anwser for user
        Pattern isAnswser=Pattern.compile("^[(ABCD)]$",0);
            Matcher mAnswer=isAnswser.matcher(event.getMessage().getContentRaw().toUpperCase());
            if(mAnswer.matches())
            {
                int answer=((int)mAnswer.group(0).charAt(0))-65;
                
                int points=t.checkAnswer(answer,event.getAuthor());
                
                if(points>-1)
                {
                    event.getAuthor().openPrivateChannel().complete().sendMessage(points+" awarded on "+event.getGuild().getName()+" "+event.getChannel().getName()).queue();
                    logger.info(points+" awarded to "+event.getAuthor().getName()+" on "+event.getGuild().getName()+" "+event.getChannel().getName());
                }
            }
        }
    }
    
    private void buildRole(Guild guild)
    {
        
        List<Permission> permissions=new ArrayList<>();
        permissions.add(Permission.MESSAGE_READ);
        permissions.add(Permission.MESSAGE_WRITE);
        permissions.add(Permission.MESSAGE_EMBED_LINKS);
        
        GuildController controler=new GuildController(guild);
        try
        {
        controler.createRole().setName(this.roleName).setColor(this.roleColor).setPermissions(permissions).setMentionable(true).setHoisted(true).complete();
        }
        catch(Exception ex)
        {
            logger.error(ex.getMessage());
        }
       
       
    }
    
    
    public boolean addTrivia(Trivia trivia,TextChannel  channel)
    {
        boolean output=false;
        
        if(!activeTrivias.containsKey(channel))
        {
            trivia.addEventListener(new ITriviaEventListener()
            {
                @Override
                public void onTriviaStart()
                {                    
                    channel.sendMessage("Trivia on ***"+channel.getGuild().getName()+"*** ***#"+channel.getName()+"***  is about to start").queue();
                }

                @Override
                public void onNewQuestion(Question q, int index)
                {
                    EmbedBuilder builder=new EmbedBuilder();
                    
                    builder.setTitle(q.getQuestion());
                    
                   if(q.getDifficulty()==QuestionDifficulty.EASY)
                   {
                        builder.setColor(Color.GREEN);
                   }
                   else if(q.getDifficulty()==QuestionDifficulty.MEDIUM)
                   {
                       builder.setColor(Color.YELLOW);
                   }
                   else if(q.getDifficulty()==QuestionDifficulty.HARD)
                   {
                        builder.setColor(Color.RED);
                   }
                   
                   
                   builder.setThumbnail("https://cdn.discordapp.com/app-icons/441072535413719041/1d3c39467cbd0c85703fdb25b4fb256d.png");
                   builder.setDescription("Advancement: "+index+"/"+trivia.size()+"\n"+"Category: "+q.getCategory());

                   int answerCount=65;
                   for(String anwser: q.getAnwsers())
                   {
                       builder.addField("\u200b",((char)answerCount)+": "+anwser, false);
                       answerCount++;
                   }
                   
                   
                   channel.sendMessage(builder.build()).queue();

                   builder.clearFields();
                }

                @Override
                public void onQuestionTimeOut(Question q, int index)
                {
                    EmbedBuilder builder=new EmbedBuilder();
                    builder.setTitle("Correct Answer");
                    builder.setDescription(((char)(q.getCorrectAnwser()+65))+": "+q.getCorrectAnwserText());
                    builder.setThumbnail(ICON_URL);
                    builder.setColor(roleColor);
                    
                    channel.sendMessage(builder.build()).queue();
                }

                @Override
                public void onTriviaFinish(boolean wasCanceled)
                {
                    if(!wasCanceled)
                    {


                        //show Winners if there are any
                        channel.sendMessage("Trivia on ***"+channel.getGuild().getName()+"*** ***#"+channel.getName()+"***  finish");
                        List<Map.Entry<Object,Integer>> winners=trivia.getWinners();

                        if(winners.size()>0) {
                            EmbedBuilder builder = new EmbedBuilder();
                            builder.setTitle("Trivia winners!");
                            builder.setThumbnail(ICON_URL);
                            builder.setColor(roleColor);

                            int position = 1;
                            for (Map.Entry<Object, Integer> w : winners) {
                               builder.addField("\u200b", position + "º - " + ((User) w.getKey()).getName() + "   " + w.getValue() + " pts", false);
                               position++;
                            }

                            channel.sendMessage(builder.build()).queue();
                        }
                    }
                    
                    removeTrivia(channel);
                }
            });
            activeTrivias.put(channel,trivia);
            output=true;
        }
        
        return output;
    }
      
    public Trivia getTrivia(Channel channel)
    {
        return activeTrivias.get(channel);
    }
    
     public boolean removeTrivia(Channel channel)
     {
         Trivia removed=activeTrivias.remove(channel);         
         return removed!=null;
     }

    public JDA getClient()
    {
        return client;
    }

    public static DiscordTriviaBot getInstance() throws LoginException
    {
        if(instance==null)
        {
            Configuration cfg;
            try {
                cfg = Configuration.getInstance();
                instance=new DiscordTriviaBot(cfg.getToken(),cfg.getCommandPrefix(),cfg.getRoleName(),cfg.getRoleColor());                
            } catch (FileNotFoundException ex) {
                java.util.logging.Logger.getLogger(DiscordTriviaBot.class.getName()).log(Level.SEVERE, null, ex);
            }
            
           
        }
        return instance;
    }
         
   

    public String getRoleName()
    {
        return roleName;
    }
    
}
