package com.example.demo.dao;

import lombok.Getter;
import lombok.ToString;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@ToString
public final class ActivePromotions {

    private final String promotionId;
    private final Collection<Promotion> promotions;
    private final int value;

    private static final Set<ActivePromotions> ACTIVE_PROMOTIONS = Set.of(
            new ActivePromotions("promo1", Set.of(new Promotion("A", 3)), 130),
            new ActivePromotions("promo2", Set.of(new Promotion("B", 2)), 45),
            new ActivePromotions("promo3", Set.of(new Promotion("C", 1), new Promotion("D", 1)), 30)
    );

    public ActivePromotions(String promotionId, Collection<Promotion> promotions, int value) {
        this.promotionId = promotionId;
        this.promotions = promotions;
        this.value = value;
    }

    public final Set<ActivePromotions> getActivePromotions() {
        return ACTIVE_PROMOTIONS;
    }

    public List<Promotion> getPromotions() {
        return new ArrayList<>(promotions);
    }

    public static final Collection<ActivePromotions> getPromotionByUnitNames(Collection<String> unitNames) {
        return ACTIVE_PROMOTIONS.stream()
                .filter(byAvailableUnits(unitNames))
                .collect(Collectors.toSet());
    }

    public final static Optional<ActivePromotions> getPromotionByUnitName(String unitName, int actualQuantity, Collection<ActivePromotions> activePromotions) {
        return activePromotions.stream()
                .filter(byAvailableUnit(unitName, actualQuantity))
                .findFirst();
    }

    private static Predicate<ActivePromotions> byAvailableUnits(Collection<String> unitNames) {
        return activePromo -> activePromo.promotions.stream().anyMatch(promo -> unitNames.contains(promo.getUnitName()));
    }

    private static Predicate<ActivePromotions> byAvailableUnit(String unitName, int quantity) {
        return activePromo -> activePromo.promotions.stream().anyMatch(promo -> promo.equals(unitName) && promo.getQuantity() < quantity);
    }
}
