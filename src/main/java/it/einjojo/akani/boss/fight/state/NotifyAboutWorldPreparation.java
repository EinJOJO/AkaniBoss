package it.einjojo.akani.boss.fight.state;

import it.einjojo.akani.boss.fight.BossFight;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class NotifyAboutWorldPreparation implements StateLogic {
    @Override
    public void update(BossFight currentFight) {
        for (UUID uuid : currentFight.participants()) {
            Player player = Bukkit.getServer().getPlayer(uuid);
            if (player != null) {
                player.sendActionBar(MiniMessage.miniMessage().deserialize("<gray><i>Die Welt wird errichtet..."));
            }
        }

    }
}
