package com.disney.studios.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Vote {

    @Id
    @GeneratedValue
    private Long id;
    private String voterId;
    private Boolean up;

    @ManyToOne  //just one-way relationship, since the other end might have huge amounts of votes, and we never really need the actual list of votes
    private Pet pet;

    public Vote() {
    }

    public Vote(String voterId, Boolean up, Pet pet) {
        this.voterId = voterId;
        this.up = up;
        this.pet = pet;
    }


}