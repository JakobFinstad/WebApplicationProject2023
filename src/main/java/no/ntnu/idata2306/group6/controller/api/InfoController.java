package no.ntnu.idata2306.group6.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import no.ntnu.idata2306.group6.entity.Info;
import no.ntnu.idata2306.group6.service.InfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/info")
public class InfoController {
    private InfoService infoService;

    private static final Logger logger = LoggerFactory.getLogger(InfoController.class.getSimpleName());

    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    /**
     * Get all infos.
     * HTTP Get to /info
     *
     * @return list of all infos currently in the collection
     */
    @GetMapping
    @Operation(
            summary = "Get all infos",
            description = "List of all infos currently stored in the collection"
    )
    public ResponseEntity<Object> getAll() {
        logger.error("Getting all");
        Iterable<Info> infos = infoService.getAll();

        return new ResponseEntity<>(infos, HttpStatus.OK);
    }

    /**
     * Get a specific info.
     *
     * @param id of the returned info
     * @return info with the given id or status 404
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get a specific info",
            description = "Retrieve an info with the specified ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Info found"),
            @ApiResponse(responseCode = "404", description = "Info not found")
    })
    public ResponseEntity<Info> getOne(@PathVariable int id) {
        ResponseEntity<Info> response;
        Optional<Info> info = Optional.ofNullable(infoService.findById(id));
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Info created"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<String> add(@RequestBody Info info) {
        ResponseEntity<String> response;

        try {
            addInfoToCollection(info);
            response = new ResponseEntity<>(String.valueOf(info.getInfoId()), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Remove info from the collection.
     *
     * @param info to remove
     * @return true when the info with that ID is removed, false otherwise
     */
    private boolean removeInfoFromCollection(Info info) {
        boolean deleted = false;
        try {
            infoService.remove(info);
            deleted = true;
        } catch (DataAccessException e) {
            logger.warn("Could not delete the info with ID: " + info.getInfoId() + " : " + e.getMessage());
        }
        return deleted;
    }

    /**
     * Delete an info from the collection.
     *
     * @param id ID of the info to delete
     * @return 200 OK on success, 404 Not found on error
     */
    @DeleteMapping("/{id}")
    @Operation(hidden = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Info deleted"),
            @ApiResponse(responseCode = "404", description = "Info not found")
    })
    public ResponseEntity<String> delete(@PathVariable int id) {
        ResponseEntity<String> response;
        Info infoToDelete = infoService.findById(id);
        if (removeInfoFromCollection(infoToDelete)) {
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * Update an info in the repository.
     *
     * @param id   of the info to update, from the URL
     * @param info new info data to store, from the request body
     * @return 200 OK on success, 400 Bad request on error
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Update an info",
            description = "Update an info with the specified ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Info updated"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
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
     * Add an info to the collection.
     *
     * @param info the info to be added to the collection if it is valid
     * @throws IllegalArgumentException if the info is invalid
     */
    private void addInfoToCollection(Info info) throws IllegalArgumentException {
        if (info == null || info.getInfoId() < 0) {
            throw new IllegalArgumentException("Info is invalid");
        }
        infoService.addInfo(info);
    }

    /**
     * Try to update an info with the given ID. The info id must match the ID.
     *
     * @param id   of the info
     * @param info the update info data
     * @throws IllegalArgumentException if something goes wrong
     */
    private void updateInfo(int id, Info info) throws IllegalArgumentException {
        Optional<Info> existingInfo = Optional.ofNullable(infoService.findById(id));
        if (existingInfo.isEmpty()) {
            throw new IllegalArgumentException("No info with ID " + id + " found");
        }
        if (info == null) {
            throw new IllegalArgumentException("Wrong data in the request body");
        }
        if (info.getInfoId() != id) {
            throw new IllegalArgumentException("Info ID in the URL does not match the ID in the JSON data (request body)");
        }

        try {
            infoService.update(id, info);
        } catch (Exception e) {
            logger.warn("Could not update info " + info.getInfoId() + ": " + e.getMessage());
            throw new IllegalArgumentException("Could not update info " + info.getInfoId());
        }
    }
}
