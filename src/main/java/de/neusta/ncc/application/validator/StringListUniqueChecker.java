package de.neusta.ncc.application.validator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Validate each string is unique in given list of strings (with ignoring case sensitive).
 */
public class StringListUniqueChecker {

    public boolean itemsUnique(List<String> values) {
        final List<String> lowerCaseRooms = values.stream().map(String::toLowerCase).collect(Collectors.toList());
        final Set<String> roomFilteredByDuplicates = new HashSet<>(lowerCaseRooms);

        return roomFilteredByDuplicates.size() == values.size();
    }

}