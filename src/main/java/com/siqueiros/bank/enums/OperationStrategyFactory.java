package com.siqueiros.bank.enums;

import java.util.List;

public class OperationStrategyFactory {
    private final List<OperationStrategy> strategies;

    public OperationStrategyFactory(List<OperationStrategy> strategies) {
        this.strategies = strategies;
    }

    public OperationStrategy getStrategy(String operationName) {
        return strategies.stream()
                .filter(s -> s.supports(operationName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Operación no disponible"));
    }
}
