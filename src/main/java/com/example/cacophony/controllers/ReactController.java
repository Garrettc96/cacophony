package com.example.cacophony.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cacophony.data.dto.CreateReactRequest;
import com.example.cacophony.data.dto.ReactResponse;
import com.example.cacophony.data.model.React;
import com.example.cacophony.exception.NotFoundException;
import com.example.cacophony.jooq.tables.records.ReactRecord;
import com.example.cacophony.mapper.ModelMapper;
import com.example.cacophony.service.ReactService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("reacts")
public class ReactController {
    ReactService reactService;
    ModelMapper modelMapper;

    public ReactController(ReactService reactService, ModelMapper modelMapper) {
        this.reactService = reactService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<ReactResponse> createReact(@RequestBody CreateReactRequest createReactBody) {
        return ResponseEntity.ok(this.modelMapper.reactToReactResponse(
                this.reactService.createReact(this.modelMapper.createReactRequestToReact(createReactBody))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReactResponse> getReact(@Valid @PathVariable UUID id) {
        ReactRecord react = this.reactService.getReact(id)
                .orElseThrow(() -> new NotFoundException(String.format("React not found for ID %s", id)));
        return ResponseEntity.ok(this.modelMapper.reactToReactResponse(react));
    }

    @GetMapping("/list")
    public ResponseEntity<List<ReactResponse>> listReacts() {
        return ResponseEntity
                .ok(this.reactService.listAllReacts().stream().map(modelMapper::reactToReactResponse).toList());
    }
}
