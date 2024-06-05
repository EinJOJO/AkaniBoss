package it.einjojo.akani.boss.fight;

import it.einjojo.akani.boss.boss.Boss;

import java.util.UUID;

public record EntrancePermission(UUID uuid, Boss boss, long expiry) {

    public boolean isExpired() {
        return System.currentTimeMillis() > expiry;
    }

}
