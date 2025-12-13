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

    static int phase = 1;  //자기장 단계
    static double size = 1000.0;  //자기장 크기
    static double borderSpeed = 1.0: //자기장 축소 속도
    static boolean isShrinking = false;  //자기장 축소 여부


    static double borderCenterX;  //현재 자기장 중심 X좌표
    static double borderCenterZ;  //현재 자기장 중심 Y좌표
    static double nextBorderCenterX;  //다음 자기장 중심 X좌표
    static double nextBorderCenterZ;  //다음 자기장 중심 Y좌표

    static Map<Integer, Double> borderSize = new HashMap<>();  //페이즈별 보더 크기

    static void init() {

        borderSize.put(1, 1000.0);
        borderSize.put(2, 500.0);
        borderSize.put(3,150.0);
        borderSize.put(4, 50.0);
        borderSize.put(5, 0.0);
    }

    public static JavaPlugin plugin = JavaPlugin.getPlugin(TaczRoyale.class);

    public static void setBorder(double size, double time){
        
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "worldborder set "  + size + " " + time);
    }

    


}

