package me.gapple.taczRoyale;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class borderManager {

    static int phase = 1;
    static double size = 5000;
    static double borderSpeed = 3.5;
    static boolean isShrinking = false;
    static boolean debug = false;

    static double borderCenterX;
    static double borderCenterZ;
    static double nextBorderCenterX;
    static double nextBorderCenterZ;

    static Map<Integer, Double> borderSize = new HashMap<>();

    static void init() {
        borderSize.put(1, 5000.0);
        borderSize.put(2, 3000.0);
        borderSize.put(3, 1000.0);
        borderSize.put(4, 300.0);
    }

    static JavaPlugin plugin = JavaPlugin.getPlugin(TaczRoyale.class);

    static void setBorder(double size, double time) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "worldborder set " + size + " " + time);
    }

    static void setBorderCenter(double prevSize, double x1, double z1, double x2, double z2, int time) {
        isShrinking = true;
        double xStep = (x2 - x1) / time;
        double zStep = (z2 - z1) / time;

        final double[] posX = {x1};
        final double[] posZ = {z1};

        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (count >= time || debug) {
                    isShrinking = false;
                    cancel();
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "worldborder center " + x2 + " " + z2);
                    return;
                }

                double progress = (double) count / time * 100;
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                            new net.md_5.bungee.api.chat.TextComponent("§7자기장 크기: §c" + prevSize + "§7 > §c" + size
                                    + " §f| §7축소 진행률: §c" + String.format("%.1f", progress) + "%"));
                }

                posX[0] += xStep;
                posZ[0] += zStep;

                borderCenterX = posX[0];
                borderCenterZ = posZ[0];

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                        "worldborder center " + posX[0] + " " + posZ[0]);
                count++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    static void brBorderInit() {
        phase = 1;
        size = borderSize.get(phase);
        borderCenterX = new Random().nextInt(2001) - 1000;
        borderCenterZ = new Random().nextInt(2001) - 1000;
        borderSpeed = 1000;

        makeIngameBorder(1, size, size, borderCenterX, borderCenterZ);
    }

    static void brShrinkBorder() {
        double prevSize = size;
        phase++;
        double newSize = borderSize.get(phase);
        borderSpeed = 3.5;
        size = newSize;
        makeIngameBorder(phase, newSize, prevSize, nextBorderCenterX, nextBorderCenterZ);
    }

    static void makeIngameBorder(int phase, double size, double prevSize, double centerX, double centerZ) {
        Bukkit.broadcastMessage("§c[배틀로얄] §f자기장이 줄어듭니다!");
        double speed = Math.round((prevSize - size) / borderSpeed * 20);
        setBorderCenter(prevSize, borderCenterX, borderCenterZ, centerX, centerZ, (int) speed);
        setBorder(size, (prevSize - size) / borderSpeed);
    }

    static void debug() {
        debug = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                debug = false;
                isShrinking = false;
            }
        }.runTaskLater(plugin, 20L);
    }
}
