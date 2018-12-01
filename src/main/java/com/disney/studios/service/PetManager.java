package com.disney.studios.service;

import com.disney.studios.api.SearchResult;
import com.disney.studios.api.SearchResultGroup;
import com.disney.studios.entities.Pet;
import com.disney.studios.entities.PetRepository;
import com.disney.studios.entities.Vote;
import com.disney.studios.entities.VoteRepository;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
public class PetManager {

    @Autowired
    PetRepository petRepository;

    @Autowired
    VoteRepository voteRepository;

    /**
     * Loads all breeds of pets
     *
     * @return strutcured object grouping by breed
     */
    public SearchResult<Pet> findPetsAll() {
        log.debug("Finding all pets");
        List<Pet> pets = findPetList(null);
        SearchResult<Pet> ret = new SearchResult<>(Pet::getBreed, pets);
        log.debug("fround {} pets returning {} groups", pets.size(), ret.getGroups().size());
        return ret;
    }

    /**
     *
     * Finds all pets for a given breed
     * @param breed
     * @return
     */
    public SearchResultGroup<Pet> findPets(String breed) {
        log.debug("Finding breed {}", breed);
        List<Pet> pets = findPetList(breed);
        SearchResultGroup<Pet> ret = new SearchResultGroup<>(breed, pets);
        log.debug("fround {} pets for breed {}", ret.getTotal(), ret.getTitle());

        return ret;
    }


    /**
     * Load list of dogs, ordered by breed and descending vote count
     * @param breed
     * @return
     */
    private List<Pet> findPetList(String breed) {
        List<Pet> pets;
        if (StringUtils.isEmpty(breed)) {
            pets = petRepository.findAllByOrderByBreedAscVoteCountDesc();
        } else {
            pets = petRepository.findByBreedOrderByVoteCountDesc(breed);
        }
        return pets;
    }

    /**
     * Not simply incrementing the voteCount because there might be simultaneouse voting happening.
     *
     * For more scalable solution, voting should better be performed via an asynchronous queue, to handle situations when many people vote at the
     * @param id
     * @param cid
     * @param up
     * @return
     */
    @Transactional
    @Synchronized
    public synchronized Pet vote(Long id, String cid, boolean up) {
        if (id == null) {
            throw new PetRuntimeException("No id. Rejecting vote");
        }
        if (StringUtils.isEmpty(cid)) {
            throw new PetRuntimeException("No cid. Rejecting vote for " + id);
        }
        Pet pet = petRepository.getOne(id);
        if (pet == null) {
            throw new PetRuntimeException("No pet found for id " + id + ". Rejecting vote");
        }

        Vote found = voteRepository.findOneByPetIdAndVoterId(id, cid);
        if (found != null) {
            if (found.getUp() == up) {
                throw new DuplicateVoteException("Already voted. Cid= " + cid + " Pet " + id);
            } else {
                //this is a change of mind, or error, so allow to change direction
                voteRepository.delete(found);
            }
        }

        voteRepository.save(new Vote(cid, up, pet));
        updateVotes(pet);
        return pet;
    }

    /**
     * Updates vote count to total amount of up-votes (ignoring the down votes, as per specification)
     * @param pet
     */
    private void updateVotes(Pet pet) {
        Long count = voteRepository.countByPetIdAndUp(pet.getId(), true);
        pet.setVoteCount(count);

    }


    public Pet getPet(Long id) {
        return petRepository.getOne(id);
    }
}
