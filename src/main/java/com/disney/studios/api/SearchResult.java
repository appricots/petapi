package com.disney.studios.api;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Generic representation of a search result tat supports grouping
 * Contains groups of results, such as breeds of dogs
 * @param <T>
 */
@Data
public class SearchResult<T> {
    int total;

    private List<SearchResultGroup<T>> groups;


    public SearchResult(Function<T, String> function, List<T> items) {
        SearchResultGroup<T> currentGroup = null;
        total = items.size();
        groups = new ArrayList<>();
        for (T item : items) {
            String mygroup = function.apply(item);
            if (currentGroup == null || !currentGroup.getTitle().equals(mygroup)) {
                currentGroup = new SearchResultGroup<>(mygroup);
                groups.add(currentGroup);
            }
            currentGroup.getItems().add(item);
        }
    }
}