package com.eden.edenbe.packages;

import java.math.BigDecimal;

public class MoneyCalc {

    private BigDecimal silver_money_amount = new BigDecimal(3998);
    private BigDecimal gold_money_amount = new BigDecimal(11998);
    private BigDecimal diamond_money_amount = new BigDecimal(35998);
    private Integer silver_package_points = 40;
    private Integer gold_package_points = 120;
    private Integer diamond_package_points = 180;

    public BigDecimal calculatePackage(String package_type) {
        switch (package_type) {
            case "Silver":
                return this.silver_money_amount;
            case "Gold":
                return this.gold_money_amount;
            case "Diamond":
                return this.diamond_money_amount;
            default:
                return new BigDecimal(0);
        }
    }

    public Integer getInitialPointsForPackage(String package_type) {
        switch (package_type) {
            case "Silver":
                return this.silver_package_points;
            case "Gold":
                return this.gold_package_points;
            case "Diamond":
                return this.diamond_package_points;
            default:
                return 0;
        }
    }
}
