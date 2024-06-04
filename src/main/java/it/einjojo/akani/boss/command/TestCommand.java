package it.einjojo.akani.boss.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TestCommand implements CommandExecutor, TabCompleter {
    private final JavaPlugin plugin;

    public TestCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("test").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            switch (args[0]) {
                case "home" -> {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                       // Main thread...
                    });
                }
            }
        });
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}
