package com.gif.currency.app.service.impl;

import com.gif.currency.app.model.RateStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.gif.currency.app.model.RateStatus.DECREASE;
import static com.gif.currency.app.model.RateStatus.INCREASE;
import static com.gif.currency.app.model.RateStatus.WITHOUT_CHANGES;

@Component
public class GifTagResolver {

    private final String increaseTag;
    private final String decreaseTag;
    private final String withoutChangesTag;
    private final Map<RateStatus, String> rateStatusToGifTagMap;

    public GifTagResolver(@Value("${giphy.rich}") String increaseTag, @Value("${giphy.broke}") String decreaseTag,
                          @Value("${giphy.zero}") String withoutChangesTag) {
        this.increaseTag = increaseTag;
        this.decreaseTag = decreaseTag;
        this.withoutChangesTag = withoutChangesTag;
        this.rateStatusToGifTagMap = buildGifTagMap();
    }

    public String getTag(RateStatus rateStatus) {
        return rateStatusToGifTagMap.get(rateStatus);
    }

    private Map<RateStatus, String> buildGifTagMap() {
        return Map.of(
                INCREASE, increaseTag,
                DECREASE, decreaseTag,
                WITHOUT_CHANGES, withoutChangesTag
        );
    }
}
