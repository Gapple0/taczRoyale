package me.gapple.taczRoyale;

import me.gapple.taczRoyale.commandManager.borderCommandManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class TaczRoyale extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Plugin taczRoyale is activated");

        getCommand("royaleborder").setExecutor(new borderCommandManager());
    }

    @Override
    public void onDisable() {

        System.out.println("Plugin taczRoyale is disabled");
    }
}
