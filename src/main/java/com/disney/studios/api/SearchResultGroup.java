package com.disney.studios.api;

import com.disney.studios.entities.Pet;
import lombok.Data;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Data
public class SearchResultGroup<T> {

    private String title;
    List<T> items;

    public SearchResultGroup(String title) {
        items = new ArrayList<>();
        this.title = title;
    }

    public SearchResultGroup(String title, List<T> items) {
        this.items = items;
        this.title = title;
    }

    public int getTotal() {
        return items.size();
    }
}
