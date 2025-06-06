/*
 * This file is generated by jOOQ.
 */
package com.example.cacophony.jooq.tables;

import com.example.cacophony.jooq.Cacophony;
import com.example.cacophony.jooq.Keys;
import com.example.cacophony.jooq.tables.records.DatabasechangeloglockRecord;

import java.time.LocalDateTime;
import java.util.Collection;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Databasechangeloglock extends TableImpl<DatabasechangeloglockRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>cacophony.databasechangeloglock</code>
     */
    public static final Databasechangeloglock DATABASECHANGELOGLOCK = new Databasechangeloglock();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<DatabasechangeloglockRecord> getRecordType() {
        return DatabasechangeloglockRecord.class;
    }

    /**
     * The column <code>cacophony.databasechangeloglock.id</code>.
     */
    public final TableField<DatabasechangeloglockRecord, Integer> ID = createField(DSL.name("id"),
            SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>cacophony.databasechangeloglock.locked</code>.
     */
    public final TableField<DatabasechangeloglockRecord, Boolean> LOCKED = createField(DSL.name("locked"),
            SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column <code>cacophony.databasechangeloglock.lockgranted</code>.
     */
    public final TableField<DatabasechangeloglockRecord, LocalDateTime> LOCKGRANTED = createField(
            DSL.name("lockgranted"), SQLDataType.LOCALDATETIME(6), this, "");

    /**
     * The column <code>cacophony.databasechangeloglock.lockedby</code>.
     */
    public final TableField<DatabasechangeloglockRecord, String> LOCKEDBY = createField(DSL.name("lockedby"),
            SQLDataType.VARCHAR(255), this, "");

    private Databasechangeloglock(Name alias, Table<DatabasechangeloglockRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Databasechangeloglock(Name alias, Table<DatabasechangeloglockRecord> aliased, Field<?>[] parameters,
            Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>cacophony.databasechangeloglock</code> table reference
     */
    public Databasechangeloglock(String alias) {
        this(DSL.name(alias), DATABASECHANGELOGLOCK);
    }

    /**
     * Create an aliased <code>cacophony.databasechangeloglock</code> table reference
     */
    public Databasechangeloglock(Name alias) {
        this(alias, DATABASECHANGELOGLOCK);
    }

    /**
     * Create a <code>cacophony.databasechangeloglock</code> table reference
     */
    public Databasechangeloglock() {
        this(DSL.name("databasechangeloglock"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Cacophony.CACOPHONY;
    }

    @Override
    public UniqueKey<DatabasechangeloglockRecord> getPrimaryKey() {
        return Keys.DATABASECHANGELOGLOCK_PKEY;
    }

    @Override
    public Databasechangeloglock as(String alias) {
        return new Databasechangeloglock(DSL.name(alias), this);
    }

    @Override
    public Databasechangeloglock as(Name alias) {
        return new Databasechangeloglock(alias, this);
    }

    @Override
    public Databasechangeloglock as(Table<?> alias) {
        return new Databasechangeloglock(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Databasechangeloglock rename(String name) {
        return new Databasechangeloglock(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Databasechangeloglock rename(Name name) {
        return new Databasechangeloglock(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Databasechangeloglock rename(Table<?> name) {
        return new Databasechangeloglock(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Databasechangeloglock where(Condition condition) {
        return new Databasechangeloglock(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Databasechangeloglock where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Databasechangeloglock where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Databasechangeloglock where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Databasechangeloglock where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Databasechangeloglock where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Databasechangeloglock where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Databasechangeloglock where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Databasechangeloglock whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Databasechangeloglock whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
