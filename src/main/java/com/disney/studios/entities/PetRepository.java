package com.disney.studios.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findAllByOrderByBreedAscVoteCountDesc();

    List<Pet> findByBreedOrderByVoteCountDesc(String breed);
}
