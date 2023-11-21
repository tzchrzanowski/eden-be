package com.eden.edenbe.packages;

public class UserPackages {
    private String[] silver;
    private String[] gold;
    private String[] diamond;

    public UserPackages() {
        this.silver = new String[]{};
        this.gold = new String[]{};
        this.diamond = new String[]{};
    }

    public String[] getSilver() {
        return silver;
    }

    public void setSilver(String[] silver) {
        this.silver = silver;
    }

    public String[] getGold() {
        return gold;
    }

    public void setGold(String[] gold) {
        this.gold = gold;
    }

    public String[] getDiamond() {
        return diamond;
    }

    public void setDiamond(String[] diamond) {
        this.diamond = diamond;
    }
}
