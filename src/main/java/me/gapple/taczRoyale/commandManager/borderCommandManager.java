package me.gapple.taczRoyale.commandManager;

import me.gapple.taczRoyale.borderManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class borderCommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (strings.length == 0) {
            commandSender.sendMessage("§c사용법: /royaleborder <start|shrink|debug|check|reset>");
            return true;
        }
        String sub = strings[0].toLowerCase();

        switch (sub) {
            case "start":
                borderManager.brBorderInit();
                Bukkit.broadcastMessage("§a[보더] §f자기장이 초기화되었습니다.");
                break;

            case "debug":
                borderManager.debug();
                commandSender.sendMessage("§7[보더] §c디버그 모드가 1초 동안 실행됩니다.");
                break;

            case "check":
                if (commandSender instanceof Player p) {
                    borderManager.brIsInNextBorder(p);
                } else {
                    commandSender.sendMessage("§c콘솔에서는 사용할 수 없습니다!");
                }
                break;

            default:
                commandSender.sendMessage("§c잘못된 명령어입니다. /br <start|shrink|debug|check>");
                break;
        }
        return true;
    }
}