package de.neusta.ncc.domain;

import java.util.Optional;

public enum PersonAddition {

    VON("von"),
    VAN("van"),
    DE("de");

    private final String label;

    PersonAddition(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static PersonAddition valueOfByLabel(String label) {
        if (isLabelEmpty(label)) {
            return null;
        }

        return findAddition(label)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Person addition %s is not supported", label)));
    }

    private static Optional<PersonAddition> findAddition(String label) {
        for (PersonAddition addition : PersonAddition.values()) {
            if (addition.getLabel().toLowerCase().equals(label.trim().toLowerCase())) {
                return Optional.of(addition);
            }
        }
        return Optional.empty();
    }

    private static boolean isLabelEmpty(String label) {
        return label == null || label.length() == 0 || label.trim().length() == 0;
    }
}
