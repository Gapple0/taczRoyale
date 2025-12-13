package me.gapple.taczRoyale;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class borderManager {

    static int phase = 1;                // 현재 자기장 단계 (1,2,3,...)
    static double size = 5000;           // 현재 자기장 크기
    static double borderSpeed = 3.5;     // 자기장 축소 속도 (값이 작을수록 빠름)
    static boolean isShrinking = false;  // 현재 자기장이 줄어드는 중인지 여부
    static boolean debug = false;        // 디버그 모드 플래그

    static double borderCenterX;         // 현재 자기장 중심 X좌표
    static double borderCenterZ;         // 현재 자기장 중심 Z좌표
    static double nextBorderCenterX;     // 다음 자기장 중심 X좌표
    static double nextBorderCenterZ;     // 다음 자기장 중심 Z좌표

    static Map<Integer, Double> borderSize = new HashMap<>(); // 각 페이즈별 보더 크기


    static void init() {
        borderSize.put(1, 5000.0);
        borderSize.put(2, 3000.0);
        borderSize.put(3, 1000.0);
        borderSize.put(4, 300.0);
        //bordersize.put(phase, border size);
    }

    public static JavaPlugin plugin = JavaPlugin.getPlugin(TaczRoyale.class);

    public static void setBorder(double size, double time) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "worldborder set " + size + " " + time); //마인크래프트 월드보더 명령어를 불러와 실행
    }

    public static void setBorderCenter(double prevSize, double x1, double z1, double x2, double z2, int time) {
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

    public static void brBorderInit() {
        phase = 1;
        size = borderSize.get(phase);
        borderCenterX = new Random().nextInt(2001) - 1000;
        borderCenterZ = new Random().nextInt(2001) - 1000;
        borderSpeed = 1000;

        makeIngameBorder(1, size, size, borderCenterX, borderCenterZ);
    }

    public static void brShrinkBorder() {
        double prevSize = size;
        phase++;
        double newSize = borderSize.get(phase);
        borderSpeed = 3.5;
        size = newSize;
        makeIngameBorder(phase, newSize, prevSize, nextBorderCenterX, nextBorderCenterZ);
    }

    public static void makeIngameBorder(int phase, double size, double prevSize, double centerX, double centerZ) {
        Bukkit.broadcastMessage("§c[배틀로얄] §f자기장이 줄어듭니다!");
        double speed = Math.round((prevSize - size) / borderSpeed * 20);
        setBorderCenter(prevSize, borderCenterX, borderCenterZ, centerX, centerZ, (int) speed);
        setBorder(size, (prevSize - size) / borderSpeed);
    }

    public static void makeRandomCenter() {
        Random rand = new Random();

        double maxOffset = size / 4; // 현재 보더의 1/4 범위 내에서 이동
        double offsetX = rand.nextDouble() * maxOffset * 2 - maxOffset;
        double offsetZ = rand.nextDouble() * maxOffset * 2 - maxOffset;

        nextBorderCenterX = borderCenterX + offsetX;
        nextBorderCenterZ = borderCenterZ + offsetZ;

        if (Math.abs(nextBorderCenterX) > 5000) nextBorderCenterX = Math.signum(nextBorderCenterX) * 5000;
        if (Math.abs(nextBorderCenterZ) > 5000) nextBorderCenterZ = Math.signum(nextBorderCenterZ) * 5000;
    }

    public static void brIsInNextBorder(Player p) {
        Location loc = p.getLocation();
        double dist = Math.sqrt(Math.pow(loc.getX() - nextBorderCenterX, 2) + Math.pow(loc.getZ() - nextBorderCenterZ, 2));
        double radius = size / 2;

        if (dist > radius) {
            // 자기장 밖
            double over = dist - radius;
            p.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                    new net.md_5.bungee.api.chat.TextComponent("§c⚠ 다음 자기장 밖에 있습니다! (거리: " + String.format("%.1f", over) + "m)"));
        } else {
            // 자기장 안
            double remain = radius - dist;
            p.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                    new net.md_5.bungee.api.chat.TextComponent("§a다음 자기장 안쪽 (" + String.format("%.1f", remain) + "m 여유)"));
        }
    }

    public static void debug() {
        debug = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                debug = false;
                isShrinking = false;
            }
        }.runTaskLater(plugin, 20L);
    }

    public static void resetBorder(){
        isShrinking = false;
        phase = 1;
        size = borderSize.get(1);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "worldborder set " + size);
    }
}
