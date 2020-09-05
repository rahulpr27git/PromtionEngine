package com.example.demo.service;

import com.example.demo.dao.ActivePromotions;
import com.example.demo.dao.Promotion;
import com.example.demo.model.UnitRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PromotionValueEngineService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PromotionValueEngineService.class);

    public int getTotalValue(Collection<UnitRequest> requests) throws JsonProcessingException {
        List<String> unitNames = requests.stream().map(UnitRequest::getUnitName)
                .collect(Collectors.toList());

        Collection<ActivePromotions> activePromotions = ActivePromotions.getPromotionByUnitNames(unitNames);
        LOGGER.info("--- getTotalValue --- Active Promotion by Unit names: {}", activePromotions);

        return getTotalValue(requests, activePromotions);
    }

    public int getTotalValue(Collection<UnitRequest> requests, Collection<ActivePromotions> activePromotions) throws JsonProcessingException {
        List<UnitRequest> detailedPurchaseInfo = new ArrayList<>();

        for (UnitRequest request : requests) {
            String unitName = request.getUnitName();
            Optional<ActivePromotions> promotionByUnitName = ActivePromotions.getPromotionByUnitName(
                    unitName,
                    request.getQuantity(),
                    activePromotions
            );

            if (promotionByUnitName.isPresent()) {
                LOGGER.info("--- getTotalValue --- promotion found: {}", promotionByUnitName);
                ActivePromotions activePromotion = promotionByUnitName.get();

                List<Promotion> promotions = activePromotion.getPromotions();
                if (promotions.size() == 1) {
                    Promotion promotion = promotions.get(0);
                    int promotionQuantity = promotion.getQuantity();

                    int actualQuantity = request.getQuantity();
                    int numberOfPromotion = actualQuantity / promotionQuantity;
                    int remainingQuantity = actualQuantity % promotionQuantity;

                    detailedPurchaseInfo.add(new UnitRequest(activePromotion.getPromotionId(), numberOfPromotion));

                    if (remainingQuantity > 0)
                        detailedPurchaseInfo.add(new UnitRequest(unitName, remainingQuantity));
                }

            } else
                detailedPurchaseInfo.add(request);
        }

        LOGGER.info("--- getTotalValue --- purchase history: {}", new ObjectMapper().writeValueAsString(detailedPurchaseInfo));
        return 0;

    }
}
