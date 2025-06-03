package com.example.cacophony.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jooq.DSLContext;

import static com.example.cacophony.jooq.tables.CUser.C_USER;
import com.example.cacophony.jooq.tables.records.CUserRecord;
import static com.example.cacophony.util.Jooq.optional;

public class UserJooqRepository {

    DSLContext dsl;

    public UserJooqRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public CUserRecord createUser(CUserRecord user) {
        return dsl.insertInto(C_USER).columns(C_USER.USERNAME, C_USER.EMAIL, C_USER.PASSWORD, C_USER.USER_ROLE)
                .values(user.getUsername(), user.getEmail(), user.getPassword(), user.getUserRole()).returning()
                .fetchOne();
    }

    public Optional<CUserRecord> findUserById(UUID id) {
        return dsl.selectFrom(C_USER).where(C_USER.ID.eq(id)).fetchOptional();
    }

    public Optional<CUserRecord> findUserByName(String username) {
        return dsl.selectFrom(C_USER).where(C_USER.USERNAME.eq(username)).fetchOptional();
    }

    public List<CUserRecord> listUsers() {
        return dsl.selectFrom(C_USER).fetch();
    }
}
