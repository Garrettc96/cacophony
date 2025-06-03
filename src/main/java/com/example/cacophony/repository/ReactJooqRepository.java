package com.example.cacophony.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jooq.DSLContext;

import com.example.cacophony.jooq.tables.records.ReactRecord;
import static com.example.cacophony.jooq.tables.React.REACT;

public class ReactJooqRepository {

    DSLContext dsl;

    public ReactJooqRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public ReactRecord createReact(ReactRecord react) {
        return this.dsl.insertInto(REACT).columns(REACT.USER_ID, REACT.NAME, REACT.S3_PATH)
                .values(react.getUserId(), react.getName(), react.getS3Path()).returning().fetchOne();
    }

    public Optional<ReactRecord> getReact(UUID id) {
        return this.dsl.selectFrom(REACT).where(REACT.ID.eq(id)).fetchOptional();
    }

    public List<ReactRecord> listAllReacts() {
        return this.dsl.selectFrom(REACT).fetch();
    }
}
