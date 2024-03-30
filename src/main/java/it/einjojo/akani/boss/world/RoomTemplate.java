package it.einjojo.akani.boss.world;

import org.bukkit.Location;

import java.nio.file.Path;

public record RoomTemplate(String templateName, Path worldTemplateFolder, Location spawnLocation) {


}
