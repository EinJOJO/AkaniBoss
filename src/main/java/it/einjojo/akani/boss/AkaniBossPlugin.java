package it.einjojo.akani.boss;

import org.bukkit.plugin.java.JavaPlugin;

public class AkaniBossPlugin extends JavaPlugin {
    private AkaniBoss akaniBoss;

    @Override
    public void onEnable() {
        akaniBoss = new AkaniBoss(this);
        akaniBoss.roomManager().load();
    }

    @Override
    public void onDisable() {
        akaniBoss = null;
    }

    public AkaniBoss akaniBoss() {
        return akaniBoss;
    }
}
