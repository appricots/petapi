package com.disney.studios.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Pet {

    @Id
    @GeneratedValue
    private Long id;
    private String breed;
    private String url;
    /**
     * denormalized field for performance. Calculated at each vote.
     *
     */
    private Long voteCount;

    public Pet() {
    }

    public Pet(String breed, String url) {
        voteCount = 0L;
        this.breed = breed;
        this.url = url;
    }
}