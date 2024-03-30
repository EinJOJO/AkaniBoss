package it.einjojo.akani.boss.boss;

public class BossManager {

    public Boss boss(String id) {
        return new BossBuilder().build();
    }

    public void load() {

    }

    public void loadAll() {

    }

}
