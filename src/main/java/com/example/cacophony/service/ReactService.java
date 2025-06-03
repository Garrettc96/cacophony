package com.example.cacophony.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.cacophony.jooq.tables.records.ReactRecord;

public interface ReactService {
    public ReactRecord createReact(ReactRecord react);

    public Optional<ReactRecord> getReact(UUID id);

    public List<ReactRecord> listAllReacts();
}
