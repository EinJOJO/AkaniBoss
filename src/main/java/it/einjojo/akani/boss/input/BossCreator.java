package it.einjojo.akani.boss.input;

import it.einjojo.akani.boss.AkaniBoss;
import it.einjojo.akani.boss.boss.BossBuilder;
import it.einjojo.akani.boss.boss.BossDifficulty;
import it.einjojo.akani.boss.room.RoomTemplate;
import it.einjojo.akani.boss.util.BoundaryBox;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class BossCreator {
    private static final int MAX_STEPS = 9;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final AkaniBoss akaniBoss;
    private final BossBuilder bossBuilder = new BossBuilder();
    private Player player;
    private int step = 0;

    private Location loc1;
    private Location loc2;

    public BossCreator(AkaniBoss akaniBossInstance, Player player) {
        this.akaniBoss = akaniBossInstance;
        this.player = player;
    }

    public void askForInput() {
        Bukkit.getScheduler().runTask(akaniBoss.plugin(), () -> { // Wrapping it in a bukkit task, so it does not build up on the previous methods.
            if (step == -1) {
                return;
            }
            if (step == MAX_STEPS) {
                akaniBoss.bossManager().registerBoss(bossBuilder.build());
                player.sendMessage("<green>Der Boss wurde erfolgreich erstellt!");
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
                        step++;
                        askForInput();
                    }), this::onCancel);
                }
                case 1 -> {
                    sendMessage(player, "Gib einen <blue>Namen <gray>für den Boss ein.");
                    sendMessage(player, "<i>z.B < red >Der Böse Mann< / red >");
                    new PlayerChatInput(player, ((name) -> {
                        bossBuilder.name(name);
                        sendMessage(player, "<green><i>Name gesetzt: <reset>" + name);
                        step++;
                        askForInput();
                    }), this::onCancel);
                }
                case 2 -> {
                    sendMessage(player, "Gib ein <blue>Level <gray>für den Boss ein.");
                    new PlayerChatInput(player, ((level) -> {
                        try {
                            bossBuilder.level(Integer.parseInt(level));
                            sendMessage(player, "<green><i>Level gesetzt: <reset>" + level);
                            step++;
                        } catch (NumberFormatException e) {
                            sendInputError(player, "Das Level muss eine Zahl sein!");
                        }
                        askForInput();
                    }), this::onCancel);
                }
                case 3 -> {
                    sendMessage(player, "Wähle eine <blue>Schwierigkeit <gray>aus:");
                    BossDifficulty[] difficulties = BossDifficulty.values();
                    for (int i = 0; i < difficulties.length; i++) {
                        sendMessage(player, "<blue>" + (i + 1) + "<dark_gray>) <gray>" + difficulties[i].name());
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
                            sendMessage(player, "<green><i>Schwierigkeit gesetzt: <reset>" + difficulty.name());
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
                        step++;
                        askForInput();
                    }), this::onCancel);

                }
                case 5 -> {
                    player.sendMessage("Von welcher <blue>Template-Welt<gray> soll der Bossraum erstellt werden?");
                    player.sendMessage("<i><gold>Gib den Namen des Welt-Ordners an.");
                    player.sendMessage("<i><gold>(Der befindet sich unter: plugins/AkaniBoss/templates/).");
                    new PlayerChatInput(player, ((template) -> {
                        RoomTemplate roomTemplate = akaniBoss.roomManager().roomTemplate(template);
                        if (roomTemplate == null) {
                            sendInputError(player, "Die Template-Welt existiert nicht!");
                            sendMessage(player, "Vorhandene Templates: <blue>" + akaniBoss.roomManager().roomTemplates().keySet());
                            askForInput();
                            return;
                        }
                        bossBuilder.roomTemplateName(template);
                        sendMessage(player, "<green><i>Template-Welt gesetzt: <reset>" + template);
                        step++;
                        askForInput();
                    }), this::onCancel);
                }
                case 6 -> {
                    sendMessage(player, "Wähle die <blue>Boundary-Box <gray>für den Eingang des Bossraumes aus.");
                    sendMessage(player, "Wähle <blue>1. Ecke <gray>für den Eingang des Bossraumes aus.");
                    new BlockSelectionInput(player, ((block) -> {
                        loc1 = block.getLocation();
                        step++;
                        askForInput();
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
                        bossBuilder.entranceBox(BoundaryBox.of(loc1, loc2));
                        sendMessage(player, "<green><i>Boundary-Box erstellt!");
                        step++;
                        askForInput();
                    }), this::onCancel);
                }
                case 8 -> {
                    sendMessage(player, "Gib das Schlüsselitem für das Öffnen des Raums an");
                    new DropItemInput(player, ((item) -> {
                        bossBuilder.keyItem(item);
                        sendMessage(player, "<green><i>Schlüsselitem gesetzt!");
                        step++;
                        askForInput();
                    }), this::onCancel);
                }

                default -> {
                    sendInputError(player, "Unbekannter Schritt (" + step + ") erreicht!");
                    cleanup();
                }
            }
        });
    }

    private void sendMessage(Player player, String message) {
        player.sendMessage(miniMessage.deserialize(progress() + ": <gray>" + message));
    }

    private String progress() {
        return "<dark_gray>[ <blue>" + step + "</blue> <gray>/</gray> <white>" + MAX_STEPS + "</white> ] </dark_gray>";
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
