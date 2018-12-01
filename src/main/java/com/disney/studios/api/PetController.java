package com.disney.studios.api;

import com.disney.studios.entities.Pet;
import com.disney.studios.service.DuplicateVoteException;
import com.disney.studios.service.PetManager;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@Api(value="Pet Image API", description="Operations to load pet pictures and vote for favorites")
public class PetController {

    @Autowired
    PetManager petManager;

    @GetMapping("/pets")
    @ApiOperation(value = "Load All Pets", notes = "Grouped by breed,  sorted by favorite count")
    public SearchResult<Pet> pets() {
        return petManager.findPetsAll();
    }

    @GetMapping("/pets/{breed}")
    @ApiOperation(value = "Load Pets For a breed")
    public SearchResultGroup<Pet> pets(@PathVariable final String breed) {
        return petManager.findPets(breed);
    }

    @GetMapping("/pet/{id}")
    @ApiOperation(value = "Load pet by id", response = Pet.class)
    public Pet pet(@PathVariable final Long id) {
        return petManager.getPet(id);
    }

    @PostMapping("/vote/{id}/{up}")
    @ApiOperation(value = "Vote for a pet", notes = "Only one vote per client allowed. Unless client changes the vote from up to down. Score is caculated based on total amount of up-votes")
    public ResponseEntity<?> vote(@PathVariable final Long id, @PathVariable Boolean up, @CookieValue(value="cid", required=false) String cid, HttpServletResponse response) {
        cid = checkCookie(cid, response);

        try {
            Pet pet = petManager.vote(id, cid, up);
            return ResponseEntity
                    .ok(pet);

        } catch (DuplicateVoteException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("Vote ignored. Already voted up");
        }
    }


    private String checkCookie(String cid, HttpServletResponse response) {
        if (StringUtils.isBlank(cid)) {
            cid = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("cid", cid);
            response.addCookie(cookie);
        }
        return cid;
    }
}
