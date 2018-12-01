package com.disney.studios.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Vote findOneByPetIdAndVoterId(Long petId, String voterId);

    Long countByPetIdAndUp(Long id, boolean b);
}
