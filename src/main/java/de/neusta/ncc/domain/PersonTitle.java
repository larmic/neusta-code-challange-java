package de.neusta.ncc.domain;

import java.util.Optional;

public enum PersonTitle {

    DR("Dr.");

    private final String label;

    PersonTitle(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static PersonTitle valueOfByLabel(String label) {
        if (isLabelEmpty(label)) {
            return null;
        }

        return findTitle(label)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Person title %s is not supported", label)));
    }

    private static Optional<PersonTitle> findTitle(String label) {
        for (PersonTitle title : PersonTitle.values()) {
            if (title.getLabel().toLowerCase().equals(label.trim().toLowerCase())) {
                return Optional.of(title);
            }
        }
        return Optional.empty();
    }

    private static boolean isLabelEmpty(String label) {
        return label == null || label.trim().length() == 0;
    }
}
