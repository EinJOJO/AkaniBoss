package it.einjojo.akani.boss.input.creator;

import it.einjojo.akani.boss.BossSystem;
import it.einjojo.akani.boss.boss.Boss;
import it.einjojo.akani.boss.boss.BossBuilder;
import it.einjojo.akani.boss.boss.BossDifficulty;
import it.einjojo.akani.boss.boss.mob.MythicBossMobFactory;
import it.einjojo.akani.boss.boss.mob.VanillaMobFactory;
import it.einjojo.akani.boss.input.BlockSelectionInput;
import it.einjojo.akani.boss.input.DropItemInput;
import it.einjojo.akani.boss.input.PlayerChatInput;
import it.einjojo.akani.boss.integration.economy.EconomyFactory;
import it.einjojo.akani.boss.loot.LootFactory;
import it.einjojo.akani.boss.room.RoomTemplate;
import it.einjojo.akani.boss.util.TextUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.BoundingBox;

public class BossCreator {
    private static final int MAX_STEPS = 11;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final BossSystem bossSystem;
    private final BossBuilder bossBuilder = new BossBuilder();
    private Player player;
    private int step = 0;

    private Location loc1;
    private Location loc2;

    public BossCreator(BossSystem bossSystemInstance, Player player) {
        this.bossSystem = bossSystemInstance;
        this.player = player;
    }

    public void askForInput() {
        Bukkit.getScheduler().runTask(bossSystem.plugin(), () -> { // Wrapping it in a bukkit task, so it does not build up on the previous methods.
            if (step == -1) {
                return;
            }
            player.sendMessage("");
            if (step == MAX_STEPS) {
                Boss boss = bossBuilder.build();
                bossSystem.bossManager().registerBoss(boss);
                Bukkit.getScheduler().runTaskAsynchronously(bossSystem.plugin(), () -> {
                    bossSystem.bossManager().saveBoss(boss);
                    player.sendActionBar(Component.text("Boss erfolgreich abgespeichert!").color(NamedTextColor.GRAY));
                });
                sendMessage(player, "<green>Der Boss wurde erfolgreich erstellt!");
                FireworkEffect effect = FireworkEffect.builder()
                        .with(FireworkEffect.Type.STAR)
                        .withTrail()
                        .withColor(Color.AQUA, Color.RED, Color.YELLOW)
                        .build();
                Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                FireworkMeta meta = firework.getFireworkMeta();
                meta.addEffects(effect);
                meta.setPower(1);
                firework.setFireworkMeta(meta);
                cleanup();
                return;
            }
            switch (step) {
                case 0 -> {
                    sendMessage(player, "Gib eine <blue>ID <gray>für den Boss ein.");
                    new PlayerChatInput(player, ((id) -> {
                        if (id.contains(" ")) {
                            sendInputError(player, "Die ID darf keine Leerzeichen enthalten!");
                            askForInput();
                            return;
                        }
                        bossBuilder.id(id);
                        sendMessage(player, "<green><i>ID gesetzt: <reset>" + id);
                        next();
                    }), this::onCancel);
                }
                case 1 -> {
                    sendMessage(player, "Gib einen <blue>Namen <gray>für den Boss ein.");
                    sendMessage(player, "<gold><i>z.B < red >Der Böse Mann< / red >");
                    new PlayerChatInput(player, ((name) -> {
                        bossBuilder.name(name);
                        sendMessage(player, "<green><i>Name gesetzt: <reset>" + name);
                        next();
                    }), this::onCancel);
                }
                case 2 -> {
                    sendMessage(player, "Gib ein <blue>Level <gray>für den Boss ein.");
                    new PlayerChatInput(player, ((level) -> {
                        try {
                            bossBuilder.level(Integer.parseInt(level));
                            sendMessage(player, "<green><i>Level gesetzt: <reset>" + level);
                            next();
                        } catch (NumberFormatException e) {
                            sendInputError(player, "Das Level muss eine Zahl sein!");
                            askForInput();
                        }
                    }), this::onCancel);
                }
                case 3 -> {
                    sendMessage(player, "Wähle eine <blue>Schwierigkeit <gray>aus:");
                    BossDifficulty[] difficulties = BossDifficulty.values();
                    for (int i = 0; i < difficulties.length; i++) {
                        sendMessage(player, "<blue>" + (i + 1) + "<dark_gray>) <gray>" + TextUtil.transformLegacyToMiniMessage(difficulties[i].legacyText()));
                    }
                    new PlayerChatInput(player, ((input) -> {
                        try {
                            int index = Integer.parseInt(input) - 1;
                            if (index < 0 || index >= difficulties.length) {
                                sendInputError(player, "Die Schwierigkeit existiert nicht!");
                                askForInput();
                                return;
                            }
                            BossDifficulty difficulty = difficulties[index];
                            bossBuilder.difficulty(difficulty);
                            sendMessage(player, "<green><i>Schwierigkeit gesetzt: <reset>" + TextUtil.transformLegacyToMiniMessage(difficulty.legacyText()));
                            step++;
                        } catch (NumberFormatException e) {
                            sendInputError(player, "Die Schwierigkeit muss eine Zahl sein!");
                        }
                        askForInput();
                    }), this::onCancel);
                }
                case 4 -> {
                    sendMessage(player, "Wähle den <blue>Block für den Key<gray> aus.");
                    new BlockSelectionInput(player, ((block) -> {
                        bossBuilder.keyRedeemLocation(block.getLocation());
                        sendMessage(player, "<green><i>Key-Block gesetzt: <reset>" + block.getType().name());
                        next();
                    }), this::onCancel);

                }
                case 5 -> {

                    sendMessage(player, "Von welcher <blue>Template-Welt<gray> soll der Bossraum erstellt werden?");
                    sendMessage(player, "<i><gold>Gib den Namen des Welt-Ordners an.");
                    sendMessage(player, "<i><gold>(Der befindet sich unter: plugins/AkaniBoss/templates/).");
                    new PlayerChatInput(player, ((template) -> {
                        RoomTemplate roomTemplate = bossSystem.roomManager().roomTemplate(template);
                        if (roomTemplate == null) {
                            sendInputError(player, "Die Template-Welt existiert nicht!");
                            sendMessage(player, "Vorhandene Templates: <blue>" + bossSystem.roomManager().roomTemplates().keySet());
                            askForInput();
                            return;
                        }
                        bossBuilder.roomTemplateName(template);
                        sendMessage(player, "<green><i>Template-Welt gesetzt: <reset>" + template);
                        next();
                    }), this::onCancel);
                }
                case 6 -> {
                    sendMessage(player, "Wähle die <blue>Boundary-Box <gray>für den Eingang des Bossraumes aus.");
                    sendMessage(player, "Wähle <blue>1. Ecke <gray>für den Eingang des Bossraumes aus.");
                    new BlockSelectionInput(player, ((block) -> {
                        loc1 = block.getLocation();
                        next();
                    }), this::onCancel);
                }
                case 7 -> {
                    sendMessage(player, "Wähle <blue>2. Ecke <gray>für den Eingang des Bossraumes aus.");
                    new BlockSelectionInput(player, ((block) -> {
                        loc2 = block.getLocation();
                        if (loc2.equals(loc1)) {
                            sendInputError(player, "Die Ecken dürfen nicht identisch sein!");
                            askForInput();
                            return;
                        }
                        bossBuilder.entranceBox(BoundingBox.of(loc1, loc2));
                        sendMessage(player, "<green><i>Boundary-Box erstellt!");
                        next();
                    }), this::onCancel);
                }
                case 8 -> {
                    sendMessage(player, "Gib das Schlüsselitem für das Öffnen des Raums an");
                    new DropItemInput(player, ((item) -> {
                        bossBuilder.keyItem(item);
                        sendMessage(player, "<green><i>Schlüsselitem gesetzt!");
                        next();
                    }), this::onCancel);
                }
                case 9 -> {
                    sendMessage(player, "<gray>Wie heißt der Boss? <yellow>MythicMob-ID <gray>/ <yellow>Entity-Name <gray>(ZOMBIE,...)");
                    new PlayerChatInput(player, ((input) -> {
                        try {
                            bossBuilder.bossMob(new VanillaMobFactory().createBossMob(input));
                            sendMessage(player, "<green><i>Boss-Mob auf Vanilla gesetzt!");
                            next();
                            return;
                        } catch (IllegalArgumentException ignore) {
                        }
                        try {
                            bossBuilder.bossMob(new MythicBossMobFactory().createBossMob(input));
                            sendMessage(player, "<green><i>Boss-Mob auf MythicMob gesetzt!");
                            next();
                        } catch (IllegalArgumentException ignore) {
                            sendInputError(player, "Dieses Mob existiert nicht!");
                            askForInput();
                            return;
                        }
                    }), this::onCancel);
                }
                case 10 -> {
                    sendMessage(player, "Lege die Belohnungen für den Boss fest");
                    new LootListCreator(bossSystem.plugin(), player, (loots) -> {
                        bossBuilder.lootList(loots);
                    }, new LootFactory(new EconomyFactory().createEconomy()));
                }

                default -> {
                    sendInputError(player, "Unbekannter Schritt (" + step + ") erreicht!");
                    cleanup();
                }
            }
        });
    }

    private void next() {
        step++;
        askForInput();
    }

    private void sendMessage(Player player, String message) {
        player.sendMessage(miniMessage.deserialize(progressString() + ": <gray>" + message));
    }

    private String progressString() {
        return "<dark_gray>[<red>" + (step + 1) + "</red><gray>/</gray><red>" + (MAX_STEPS + 1) + "</red>] </dark_gray>";
    }

    public void onCancel() {
        player.sendMessage("Du hast den Vorgang abgebrochen.");
        cleanup();
    }

    public void cleanup() {
        step = -1;
        loc1 = null;
        loc2 = null;
        player = null;
    }

    public void sendInputError(Player player, String inputError) {
        player.sendMessage(miniMessage.deserialize("<red>FEHLER: <gray>" + inputError));
        player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_ANVIL_LAND, 1, 6);
    }

}
