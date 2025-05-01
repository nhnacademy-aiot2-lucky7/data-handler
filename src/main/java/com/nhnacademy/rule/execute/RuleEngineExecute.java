package com.nhnacademy.rule.execute;

import com.nhnacademy.common.parser.dto.ParsingData;
import com.nhnacademy.common.thread.Executable;
import com.nhnacademy.rule.dto.RuleData;
import com.nhnacademy.rule.service.RuleEngineService;

public final class RuleEngineExecute implements Executable {

    private final RuleEngineService ruleEngineService;

    private final ParsingData parsingData;

    private int retryCount = 0;

    public RuleEngineExecute(RuleEngineService ruleEngineService, ParsingData parsingData) {
        this.ruleEngineService = ruleEngineService;
        this.parsingData = parsingData;
    }

    /// TODO: Rule Engine에게 전달하는 것은 잠시 보류
    @Override
    public void execute() {
        RuleData ruleData = new RuleData(
                parsingData.getGatewayId(),
                parsingData.getSensorId(),
                parsingData.getLocation(),
                parsingData.getSpot(),
                parsingData.getType(),
                parsingData.getValue(),
                parsingData.getTimestamp()
        );

    }

    @Override
    public int getRetryCount() {
        return retryCount;
    }

    @Override
    public void incrementRetryCount() {
        retryCount++;
    }
}
