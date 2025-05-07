package com.nhnacademy.rule.execute;

import com.nhnacademy.common.parser.dto.ParsingData;
import com.nhnacademy.common.thread.Executable;
import com.nhnacademy.rule.dto.RuleData;
import com.nhnacademy.rule.service.RuleEngineService;

public final class RuleEngineExecute implements Executable {

    private final RuleEngineService ruleEngineService;

    private final ParsingData parsingData;

    public RuleEngineExecute(RuleEngineService ruleEngineService, ParsingData parsingData) {
        this.ruleEngineService = ruleEngineService;
        this.parsingData = parsingData;
    }

    @Override
    public void execute() {
        RuleData ruleData = new RuleData(
                parsingData.getGatewayId(),
                parsingData.getSensorId(),
                parsingData.getType(),
                parsingData.getValue(),
                parsingData.getTimestamp()
        );
        ruleEngineService.sensorDataSend(ruleData);
    }
}
