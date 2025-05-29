package com.example.cacophony.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.cacophony.data.model.React;
import com.example.cacophony.jooq.tables.records.ReactRecord;
import com.example.cacophony.repository.ReactJooqRepository;
import com.example.cacophony.repository.ReactRepository;

public class ReactServiceImpl implements ReactService {

    ReactJooqRepository reactRepository;

    public ReactServiceImpl(ReactJooqRepository reactRepository) {
        this.reactRepository = reactRepository;
    }

    public ReactRecord createReact(ReactRecord react) {
        return this.reactRepository.createReact(react);
    }

    public Optional<ReactRecord> getReact(UUID id) {
        return this.reactRepository.getReact(id);
    }

    public List<ReactRecord> listAllReacts() {
        return this.reactRepository.listAllReacts();
    }
}
