package net.noobsters.core.paper;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

/**
 * Core
 */
public class Core extends JavaPlugin implements Listener{

    private static @Getter Core instance;
    private @Getter Game game;
    private @Getter PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, this);

        game = new Game(this);
        game.runTaskTimerAsynchronously(this, 0L, 20L);

        commandManager = new PaperCommandManager(this);

        commandManager.registerCommand(new MundoModCMD(this));

    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void tickEvent(GameTickEvent e){
        var game = instance.getGame();
        Bukkit.getScheduler().runTask(this, () -> {
            Bukkit.getOnlinePlayers().stream().forEach(player ->{
                var uuid = player.getUniqueId().toString();
                var mask = game.getPlayerMasks().get(uuid);

                //TOTAL MINUTES PLAYED CHANGE 0
                var minutes = 0;
                if(minutes-30 == game.getTimelimit()){
                    player.sendMessage(ChatColor.GREEN + "Te quedan 30 minutos de juego.");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1, 1);

                }else if(minutes-10 == game.getTimelimit()){
                    player.sendMessage(ChatColor.GREEN + "Te quedan 10 minutos de juego.");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1, 1);

                }else if(minutes-5 == game.getTimelimit()){
                    player.sendMessage(ChatColor.GREEN + "Te quedan 5 minutos de juego.");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1, 1);

                }else if(minutes-1 == game.getTimelimit()){
                    player.sendMessage(ChatColor.GREEN + "Te quedan 1 minuto de juego.");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1, 1);

                }else if(minutes >= game.getTimelimit()){
                    player.kickPlayer(ChatColor.GREEN + "Tu tiempo de juego por hoy ha terminado.");

                }
            });
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        var player = e.getPlayer();
        var uuid = player.getUniqueId().toString();
        var game = instance.getGame();

        var masks = game.getPlayerMasks();

        //TODO: ON 0 GET JSON TIME PLAYED 
        var playerMask = new PlayerMask(uuid, game.getGameTime(), 0);

        if(masks.containsKey(uuid)){
            masks.remove(uuid);
            masks.put(uuid, playerMask);

        }else{
            masks.put(uuid, playerMask);
        }

        if(game.isSwitchTimeLimit()){
            playerMask.setJoinTime(game.getGameTime());

            //TODO: GET HOURS PLAYED on json and set
        }


    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        var player = e.getPlayer();
        var uuid = player.getUniqueId().toString();
        var game = instance.getGame();

        var masks = game.getPlayerMasks();
        if(masks.containsKey(uuid)){
            masks.remove(uuid);

        }
    }
    
}