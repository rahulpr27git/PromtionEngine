package com.example.demo.service;

import com.example.demo.dao.ActivePromotions;
import com.example.demo.dao.Promotion;
import com.example.demo.model.UnitRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class PromotionValueEngineService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PromotionValueEngineService.class);

    public int getTotalValue(Collection<UnitRequest> requests) throws JsonProcessingException {
        List<String> unitNames = requests.stream().map(UnitRequest::getUnitName)
                .collect(Collectors.toList());

        Collection<ActivePromotions> activePromotions = ActivePromotions.getPromotionByUnitNames(unitNames);

        Map<String, Integer> unitRequest = requests.stream()
                .collect(Collectors.toMap(UnitRequest::getUnitName, UnitRequest::getQuantity));
        return getTotalValue(unitRequest, mapOfActivePromotionToUnitName(activePromotions));
    }

    public int getTotalValue(Map<String, Integer> unitRequest, Map<String, ActivePromotions> mapOfPromoToUnit) throws JsonProcessingException {
        LOGGER.info("--- getTotalValue --- Unit -> Active Promo: {}", mapOfPromoToUnit);
        List<UnitRequest> detailedPurchaseInfo = new ArrayList<>();

        for (String unitName : unitRequest.keySet()) {
            int actualQuantity = unitRequest.get(unitName);

            ActivePromotions activePromo = mapOfPromoToUnit.get(unitName);
            if (isNull(activePromo)) {
                detailedPurchaseInfo.add(new UnitRequest(unitName, actualQuantity));
            } else {
                List<Promotion> promotions = activePromo.getPromotions();
                if (promotions.size() == 1) {
                    Promotion promotion = promotions.get(0);
                    int promotionQuantity = promotion.getQuantity();

                    int numberOfPromotion = actualQuantity / promotionQuantity;
                    int remainingQuantity = actualQuantity % promotionQuantity;

                    detailedPurchaseInfo.add(new UnitRequest(activePromo.getPromotionId(), numberOfPromotion));

                    if (remainingQuantity > 0)
                        detailedPurchaseInfo.add(new UnitRequest(unitName, remainingQuantity));
                } else {
                    int combination  = promotions.size();
                    int matched = 1;
                    for (Promotion promotion : promotions) {
                        if (promotion.getUnitName().equals(unitName)) {
                            //TODO: Think harder
                        }
                    }

                }
            }
        }

        /*for (UnitRequest request : requests) {
            String unitName = request.getUnitName();
            int actualQuantity = request.getQuantity();
            Optional<ActivePromotions> promotionByUnitName = ActivePromotions.getPromotionByUnitName(
                    unitName,
                    actualQuantity,
                    activePromotions
            );

            if (promotionByUnitName.isPresent()) {
                LOGGER.info("--- getTotalValue --- promotion found: {}", promotionByUnitName);
                ActivePromotions activePromotion = promotionByUnitName.get();

                List<Promotion> promotions = activePromotion.getPromotions();
                if (promotions.size() == 1) {
                    Promotion promotion = promotions.get(0);
                    int promotionQuantity = promotion.getQuantity();

                    int numberOfPromotion = actualQuantity / promotionQuantity;
                    int remainingQuantity = actualQuantity % promotionQuantity;

                    detailedPurchaseInfo.add(new UnitRequest(activePromotion.getPromotionId(), numberOfPromotion));

                    if (remainingQuantity > 0)
                        detailedPurchaseInfo.add(new UnitRequest(unitName, remainingQuantity));
                }

            } else
                detailedPurchaseInfo.add(request);
        }

        LOGGER.info("--- getTotalValue --- purchase history: {}", mapper.writeValueAsString(detailedPurchaseInfo));*/
        return 0;

    }

    public Map<String, ActivePromotions> mapOfActivePromotionToUnitName(Collection<ActivePromotions> activePromotions) throws JsonProcessingException {
        LOGGER.info("--- mapOfActivePromotionToUnitName --- Active Promotion filter by Unit names: {}", activePromotions);

        Map<String, ActivePromotions> mapOfPromoToUnit = new HashMap<>();
        for (ActivePromotions activePromo : activePromotions) {
            for (Promotion promo : activePromo.getPromotions()) {
                mapOfPromoToUnit.putIfAbsent(promo.getUnitName(), activePromo);
            }
        }
        return mapOfPromoToUnit;
    }
}
