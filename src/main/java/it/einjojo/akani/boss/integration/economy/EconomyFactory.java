package it.einjojo.akani.boss.integration.economy;

import org.jetbrains.annotations.NotNull;

public class EconomyFactory {

    @NotNull
    public Economy createEconomy() {
        return new NullEconomy();
    }

}
