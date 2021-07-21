package net.noobsters.core.paper;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("mundomod")
@CommandPermission("admin.perm")
public class MundoModCMD extends BaseCommand {

    private @NonNull Core instance;

    public MundoModCMD(Core instance) {
        this.instance = instance;

    }

    @Subcommand("timelimit")
    public void timelimit(CommandSender sender, Integer num){
        instance.getGame().setTimelimit(num);
        sender.sendMessage(ChatColor.GREEN + "TIME LIMIT SET TO: " + num);

    }

    @Subcommand("switch-timelimit")
    public void enable(CommandSender sender){

        //TODO:add var isswitchtimelimit to JSON global var
        
        if(instance.getGame().isSwitchTimeLimit()){
            instance.getGame().setSwitchTimeLimit(false);
            sender.sendMessage(ChatColor.GREEN + "TIME LIMIT DISABLED");
            
        }else{
            instance.getGame().setSwitchTimeLimit(true);
            sender.sendMessage(ChatColor.GREEN + "TIME LIMIT ENABLED");
        }
        

    }

    @Subcommand("check")
    public void check(CommandSender sender, String player){
        //TODO: Check json info hours played today (JSON)

        sender.sendMessage(ChatColor.GREEN + player + " Info: ");

    }

    @Subcommand("set")
    public void hoursSet(CommandSender sender, String player, Integer minutes){
        //TODO: Change total minutes played from a player (JSON)

        sender.sendMessage(ChatColor.GREEN + player + " minutes changed to " + minutes);

    }

    @Subcommand("reset")
    public void resetJson(CommandSender sender, Player player){
        //TODO: Reset json, clear info (JSON)

        sender.sendMessage(ChatColor.GREEN + "INFO CLEARED");

    }



}
