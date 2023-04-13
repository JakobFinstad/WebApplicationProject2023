package no.ntnu.idata2306.group6.controller;

import io.swagger.v3.oas.annotations.Operation;
import no.ntnu.idata2306.group6.entity.Info;
import no.ntnu.idata2306.group6.repository.InfoRepository;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/info")
public class InfoController {
    private InfoRepository infoRepository;

    private static final Logger logger = LoggerFactory.getLogger(InfoController.class.getSimpleName());

    public InfoController(InfoRepository infoRepository) {
        this.infoRepository = infoRepository;
    }

    /**
     * Get all infos.
     * HTTP Get to /info
     *
     * @return list of all infos currently in collection
     */
    @GetMapping
    @Operation(
            summary = "Get all infos",
            description = "List of all infos currently stored in collection"
    )
    public ResponseEntity<Object> getAll() {
        logger.error("Getting all ");
        Iterable<Info> infos = infoRepository.findAll();
        return new ResponseEntity<>(infos, HttpStatus.OK);
    }

    /**
     * Get a specific info.
     *
     * @param id of the returned info
     * @return info with the given id or status 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Info> getOne(@PathVariable Integer id) {
        ResponseEntity<Info> response;
        Optional<Info> info = infoRepository.findById(id);
        if (info.isPresent()) {
            response = new ResponseEntity<>(info.get(), HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * HTTP POST endpoint for adding a new info.
     *
     * @param info data of the info to add. ID will be ignored.
     * @return 201 Created on success and the new ID in the response body,
     * 400 Bad request if some data is missing or incorrect
     */
    @PostMapping()
    @Operation(deprecated = true)
    public ResponseEntity<String> add(@RequestBody Info info) {
        ResponseEntity<String> response;

        try {
            addInfoToCollection(info);
            response = new ResponseEntity<>("" + info.getInfoId(), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Remove info from the collection.
     *
     * @param id ID of the product to remove
     * @return true when product with that ID is removed, false otherwise
     */
    private boolean removeInfoFromCollection(int id) {
        boolean deleted = false;
        try {
            infoRepository.deleteById(id);
            deleted = true;
        } catch (DataAccessException e) {
            logger.warn("Could not delete the info with ID: " + id + " : " + e.getMessage());
        }
        return deleted;
    }

    /**
     * Delete a info from the collection.
     *
     * @param id ID of the info to delete
     * @return 200 OK on success, 404 Not found on erro
     */
    @DeleteMapping("/{id}")
    @Operation(hidden = true)
    public ResponseEntity<String> delete(@PathVariable int id) {
        ResponseEntity<String> response;
        if (removeInfoFromCollection(id)) {
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * Update an info in the repository.
     *
     * @param id of the info to update, from the URL
     * @param info new info data to store, from request body
     * @return 200 OK on success, 400 Bad request on error
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Info info) {
        ResponseEntity<String> response;
        try {
            updateInfo(id, info);
            response = new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    /**
     * Add an info to collection.
     *
     * @param info the info to be added to collection if it is valid
     * @throws IllegalArgumentException
     */
    private void addInfoToCollection(Info info) throws IllegalArgumentException {
        if (info == null || info.getInfoId() < 0) {
            throw new IllegalArgumentException("Product is invalid");
        }
        infoRepository.save(info);
    }

    /**
     * Try to update an info with given ID. The info id must match the ID.
     *
     * @param id of the info
     * @param info the update info data
     * @throws IllegalArgumentException if something goes wrong
     */
    private void updateInfo(int id, Info info) throws  IllegalArgumentException {
        Optional<Info> existingInfo = infoRepository.findById(id);
        if (existingInfo.isEmpty()) {
            throw new IllegalArgumentException("No product with id " + id + " found")
        }
        if (info == null) {
            throw new IllegalArgumentException("Wrong data in request body");
        }
        if (info.getInfoId() != id) {
            throw new IllegalArgumentException("Info ID in the URL does not match the ID " +
                    "in the ID in JSON data(request body)");
        }

        try {
            infoRepository.save(info);
        } catch (Exception e) {
            logger.warn("Could not update info " + info.getInfoId() + ": " + e.getMessage());
            throw new IllegalArgumentException("Could not update info " + info.getInfoId());
        }
    }
}
