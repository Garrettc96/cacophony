package com.example.cacophony.security;

public sealed interface ResourceAccessType {
    record ResourceAccessPathId() implements ResourceAccessType {
    };

    record ResourceAccessGeneral() implements ResourceAccessType {
    };

    record ResourceAccessBodyId() implements ResourceAccessType {
    };
}
