package net.noobsters.core.paper;

import java.util.HashMap;

import com.google.gson.Gson;

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
public class Core extends JavaPlugin implements Listener {

    private static @Getter Core instance;
    private @Getter Game game;
    private @Getter PaperCommandManager commandManager;
    // Cosas de json starts
    private final Gson gson = new Gson();
    JsonFile jsonFile;
    private @Getter HashMap<String, PlayerMask> playerMasks = new HashMap<>();
    // Cosas de json ends

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, this);

        game = new Game(this);
        game.runTaskTimerAsynchronously(this, 0L, 20L);

        commandManager = new PaperCommandManager(this);

        commandManager.registerCommand(new MundoModCMD(this));

        try {
            this.jsonFile = new JsonFile("player_data.json");
            Bukkit.getScheduler().runTaskTimerAsynchronously(instance, () -> saveInfoToFlatFile(), 0, 60 * 20L);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Este metodo guarda toda en el archcivo player_data.json
    public void saveInfoToFlatFile() {
        //Variable of current time
        var now = System.currentTimeMillis();
        //Update all masks with the new time
        playerMasks.values().stream().forEach(all -> all.update(now));
        //Map all masks to jsons
        jsonFile.setJsonObject(gson.toJsonTree(playerMasks).getAsJsonObject());
        //Save all masks to file
        try {
            jsonFile.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        saveInfoToFlatFile();

    }

    @EventHandler
    public void tickEvent(GameTickEvent e) {
        var game = instance.getGame();
        Bukkit.getScheduler().runTask(this, () -> {
            Bukkit.getOnlinePlayers().stream().forEach(player -> {
                var uuid = player.getUniqueId().toString();
                var mask = playerMasks.get(uuid);

                // TOTAL MINUTES PLAYED CHANGE 0
                var minutes = 0;
                if (minutes - 30 == game.getTimelimit()) {
                    player.sendMessage(ChatColor.GREEN + "Te quedan 30 minutos de juego.");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1, 1);

                } else if (minutes - 10 == game.getTimelimit()) {
                    player.sendMessage(ChatColor.GREEN + "Te quedan 10 minutos de juego.");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1, 1);

                } else if (minutes - 5 == game.getTimelimit()) {
                    player.sendMessage(ChatColor.GREEN + "Te quedan 5 minutos de juego.");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1, 1);

                } else if (minutes - 1 == game.getTimelimit()) {
                    player.sendMessage(ChatColor.GREEN + "Te quedan 1 minuto de juego.");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1, 1);

                } else if (minutes >= game.getTimelimit()) {
                    player.kickPlayer(ChatColor.GREEN + "Tu tiempo de juego por hoy ha terminado.");

                }
            });
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        var player = e.getPlayer();
        var uuid = player.getUniqueId().toString();
        var game = instance.getGame();

        // TODO: ON 0 GET JSON TIME PLAYED
        var playerMask = PlayerMask.create(uuid, game.getGameTime(), 0);

        if (playerMasks.containsKey(uuid)) {
            playerMasks.remove(uuid);
            playerMasks.put(uuid, playerMask);

        } else {
            playerMasks.put(uuid, playerMask);
        }

        if (game.isSwitchTimeLimit()) {
            playerMask.setJoinTime(game.getGameTime());
            // TODO: GET HOURS PLAYED on json and set
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        var player = e.getPlayer();
        var uuid = player.getUniqueId().toString();
        var game = instance.getGame();

        if (playerMasks.containsKey(uuid)) {
            playerMasks.remove(uuid);
        }
    }

}