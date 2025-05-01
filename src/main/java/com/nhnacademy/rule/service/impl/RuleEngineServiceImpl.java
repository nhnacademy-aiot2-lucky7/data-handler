package com.nhnacademy.rule.service.impl;

import com.nhnacademy.rule.adapter.RuleEngineAdapter;
import com.nhnacademy.rule.dto.RuleData;
import com.nhnacademy.rule.service.RuleEngineService;
import org.springframework.stereotype.Service;

@Service
public class RuleEngineServiceImpl implements RuleEngineService {

    private final RuleEngineAdapter ruleEngineAdapter;

    public RuleEngineServiceImpl(RuleEngineAdapter ruleEngineAdapter) {
        this.ruleEngineAdapter = ruleEngineAdapter;
    }

    @Override
    public void sensorDataSend(RuleData ruleData) {
        if (!ruleEngineAdapter.send(ruleData)) {
            /// TODO: Thread 영역에서 실패한 작업을 다시 시도할 수 있도록 throws 발생
            /// 추후 커스텀 Exception을 제작할 예정...
            throw new RuntimeException();
        }
    }
}
