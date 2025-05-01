package com.nhnacademy.rule.adapter;

import com.nhnacademy.rule.dto.RuleData;

public interface RuleEngineAdapter {

    boolean send(RuleData ruleData);
}
