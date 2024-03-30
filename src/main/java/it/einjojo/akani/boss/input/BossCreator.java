package it.einjojo.akani.boss.input;

import it.einjojo.akani.boss.AkaniBoss;
import it.einjojo.akani.boss.boss.BossBuilder;
import it.einjojo.akani.boss.boss.BossDifficulty;
import it.einjojo.akani.boss.room.RoomTemplate;
import it.einjojo.akani.boss.util.BoundaryBox;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BossCreator {
    private static final int MAX_STEPS = 9;
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
        if (step == -1) {
            return;
        }
        if (step == MAX_STEPS) {
            akaniBoss.bossManager().registerBoss(bossBuilder.build());
            player.sendMessage("Boss erstellt!");
            cleanup();
            return;
        }
        switch (step) {
            case 0 -> {
                player.sendMessage("Gib eine ID für den Boss ein.");
                new PlayerChatInput(player, ((id) -> {
                    if (id.contains(" ")) {
                        sendInputError(player, "Die ID darf keine Leerzeichen enthalten!");
                        askForInput();
                        return;
                    }
                    bossBuilder.id(id);
                    step++;
                    askForInput();
                }), this::onCancel);
            }
            case 1 -> {
                player.sendMessage("Gib einen Namen für den Boss ein.");
                player.sendMessage("z.B <red>Der Böse Mann</red>");
                new PlayerChatInput(player, ((name) -> {
                    bossBuilder.name(name);
                    step++;
                    askForInput();
                }), this::onCancel);
            }
            case 2 -> {
                player.sendMessage("Gib ein Level für den Boss ein.");
                new PlayerChatInput(player, ((level) -> {
                    try {
                        bossBuilder.level(Integer.parseInt(level));
                        step++;
                    } catch (NumberFormatException e) {
                        sendInputError(player, "Das Level muss eine Zahl sein!");
                    }
                    askForInput();
                }), this::onCancel);
            }
            case 3 -> {
                player.sendMessage("Wähle eine Schwierigkeit aus:");
                BossDifficulty[] difficulties = BossDifficulty.values();
                for (int i = 0; i < difficulties.length; i++) {
                    player.sendMessage((i + 1) + ") " + difficulties[i].name());
                }
                new PlayerChatInput(player, ((input) -> {
                    try {
                        int index = Integer.parseInt(input) - 1;
                        if (index < 0 || index >= difficulties.length) {
                            sendInputError(player, "Die Schwierigkeit existiert nicht!");
                            askForInput();
                            return;
                        }
                        bossBuilder.difficulty(difficulties[index]);
                        step++;
                    } catch (NumberFormatException e) {
                        sendInputError(player, "Die Schwierigkeit muss eine Zahl sein!");
                    }
                    askForInput();
                }), this::onCancel);
            }
            case 4 -> {
                player.sendMessage("Wähle den Schlüsselblock aus.");
                new BlockSelectionInput(player, ((block) -> {
                    bossBuilder.keyRedeemLocation(block.getLocation());
                    step++;
                    askForInput();
                }), this::onCancel);

            }
            case 5 -> {
                player.sendMessage("Von welcher Template-Welt soll der Bossraum erstellt werden?");
                player.sendMessage("Gib den Namen des Ordners ein. (Der befindet sich unter: plugins/AkaniBoss/templates/)");
                new PlayerChatInput(player, ((template) -> {
                    RoomTemplate roomTemplate = akaniBoss.roomManager().roomTemplate(template);
                    if (roomTemplate == null) {
                        sendInputError(player, "Die Template-Welt existiert nicht!");
                        player.sendMessage("Vorhandene Templates: " + akaniBoss.roomManager().roomTemplates().keySet());
                        askForInput();
                        return;
                    }
                    bossBuilder.roomTemplateName(template);
                    step++;
                    askForInput();
                }), this::onCancel);
            }
            case 6 -> {
                player.sendMessage("Wähle 1. Ecke für den Eingang des Bossraumes aus.");
                new BlockSelectionInput(player, ((block) -> {
                    loc1 = block.getLocation();
                    step++;
                    askForInput();
                }), this::onCancel);
            }
            case 7 -> {
                player.sendMessage("Wähle 2. Ecke für den Eingang des Bossraumes aus.");
                new BlockSelectionInput(player, ((block) -> {
                    loc2 = block.getLocation();
                    step++;
                    askForInput();
                }), this::onCancel);
            }
            case 8 -> {
                bossBuilder.entranceBox(BoundaryBox.of(loc1, loc2));
                player.sendMessage("Boundary-Box erstellt!");
                step++;
                askForInput();
            }
            default -> {
                sendInputError(player, "Unbekannter Schritt (" + step + ") erreicht!");
                cleanup();
            }
        }
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
        player.sendMessage("Fehler: " + inputError);
    }

}
