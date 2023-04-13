package no.ntnu.idata2306.group6.service;

import no.ntnu.idata2306.group6.entity.Info;
import no.ntnu.idata2306.group6.repository.InfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for service logic to info.
 */
@Service
public class InfoService {

    @Autowired
    private InfoRepository infoRepository;

    public Iterable<Info> getAll() {
        return infoRepository.findAll();
    }

    /**
     * Find info by id.
     *
     * @param id of the info that shall be returned
     * @return info if found, else null
     */
    public Info findById(int id) {
        Optional<Info> info = infoRepository.findById(id);
        return info.orElse(null);
    }

    /**
     * Add info to the collection.
     *
     * @param info that shall be added
     * @return true if the info was added and false in cas of failure
     */
    public boolean addInfo(Info info) {
        boolean added;
        if (info.getInfoId() < 0 || infoRepository.findById(info.getInfoId()).orElse(null) == info) {
            added = false;
        } else {
            infoRepository.save(info);
            added = true;
        }
        return added;
    }

    /**
     * Remove an info from the collection.
     *
     * @param info that shall be removed
     * @return true on success, false on error
     */
    public boolean remove(Info info) {
        boolean removed;

        try {
            infoRepository.delete(info);
            removed = true;
        } catch (Exception e) {
            removed = false;
        }
        return removed;
    }

    /**
     * Update an info in the collection.
     *
     * @param id of the info that shall be edited
     * @param info the new info that shall be saved
     * @return null on success, else errormessage
     */
    public String update(Integer id, Info info) {
        String errorMessage = null;

        Info existingInfo = findById(id);

        if (existingInfo == null) {
            errorMessage = "No existing info with id: " + id;
        } else if (info == null) {
            errorMessage = "New info is invalid";
        } else if (info.getInfoId() != id) {
            errorMessage = "Id in URL does not match id in the info";
        }

        if (errorMessage == null) {
            infoRepository.save(info);
        }

        return errorMessage;
    }

}
