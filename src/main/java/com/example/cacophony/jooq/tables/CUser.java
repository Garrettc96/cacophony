/*
 * This file is generated by jOOQ.
 */
package com.example.cacophony.jooq.tables;

import com.example.cacophony.jooq.Cacophony;
import com.example.cacophony.jooq.Keys;
import com.example.cacophony.jooq.tables.Message.MessagePath;
import com.example.cacophony.jooq.tables.React.ReactPath;
import com.example.cacophony.jooq.tables.UserConversation.UserConversationPath;
import com.example.cacophony.jooq.tables.UserRole.UserRolePath;
import com.example.cacophony.jooq.tables.records.CUserRecord;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.InverseForeignKey;
import org.jooq.Name;
import org.jooq.Path;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.Record;
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
public class CUser extends TableImpl<CUserRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>cacophony.c_user</code>
     */
    public static final CUser C_USER = new CUser();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<CUserRecord> getRecordType() {
        return CUserRecord.class;
    }

    /**
     * The column <code>cacophony.c_user.id</code>.
     */
    public final TableField<CUserRecord, UUID> ID = createField(DSL.name("id"),
            SQLDataType.UUID.nullable(false).defaultValue(DSL.field(DSL.raw("gen_random_uuid()"), SQLDataType.UUID)),
            this, "");

    /**
     * The column <code>cacophony.c_user.username</code>.
     */
    public final TableField<CUserRecord, String> USERNAME = createField(DSL.name("username"),
            SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>cacophony.c_user.email</code>.
     */
    public final TableField<CUserRecord, String> EMAIL = createField(DSL.name("email"),
            SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>cacophony.c_user.password</code>.
     */
    public final TableField<CUserRecord, String> PASSWORD = createField(DSL.name("password"),
            SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>cacophony.c_user.created_at</code>.
     */
    public final TableField<CUserRecord, OffsetDateTime> CREATED_AT = createField(DSL.name("created_at"),
            SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false)
                    .defaultValue(DSL.field(DSL.raw("now()"), SQLDataType.TIMESTAMPWITHTIMEZONE)),
            this, "");

    /**
     * The column <code>cacophony.c_user.updated_at</code>.
     */
    public final TableField<CUserRecord, OffsetDateTime> UPDATED_AT = createField(DSL.name("updated_at"),
            SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false)
                    .defaultValue(DSL.field(DSL.raw("now()"), SQLDataType.TIMESTAMPWITHTIMEZONE)),
            this, "");

    /**
     * The column <code>cacophony.c_user.user_role</code>.
     */
    public final TableField<CUserRecord, String> USER_ROLE = createField(DSL.name("user_role"),
            SQLDataType.VARCHAR(50).nullable(false), this, "");

    private CUser(Name alias, Table<CUserRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private CUser(Name alias, Table<CUserRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>cacophony.c_user</code> table reference
     */
    public CUser(String alias) {
        this(DSL.name(alias), C_USER);
    }

    /**
     * Create an aliased <code>cacophony.c_user</code> table reference
     */
    public CUser(Name alias) {
        this(alias, C_USER);
    }

    /**
     * Create a <code>cacophony.c_user</code> table reference
     */
    public CUser() {
        this(DSL.name("c_user"), null);
    }

    public <O extends Record> CUser(Table<O> path, ForeignKey<O, CUserRecord> childPath,
            InverseForeignKey<O, CUserRecord> parentPath) {
        super(path, childPath, parentPath, C_USER);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class CUserPath extends CUser implements Path<CUserRecord> {

        private static final long serialVersionUID = 1L;

        public <O extends Record> CUserPath(Table<O> path, ForeignKey<O, CUserRecord> childPath,
                InverseForeignKey<O, CUserRecord> parentPath) {
            super(path, childPath, parentPath);
        }

        private CUserPath(Name alias, Table<CUserRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public CUserPath as(String alias) {
            return new CUserPath(DSL.name(alias), this);
        }

        @Override
        public CUserPath as(Name alias) {
            return new CUserPath(alias, this);
        }

        @Override
        public CUserPath as(Table<?> alias) {
            return new CUserPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Cacophony.CACOPHONY;
    }

    @Override
    public UniqueKey<CUserRecord> getPrimaryKey() {
        return Keys.C_USER_PKEY;
    }

    @Override
    public List<UniqueKey<CUserRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.C_USER_USERNAME_KEY);
    }

    @Override
    public List<ForeignKey<CUserRecord, ?>> getReferences() {
        return Arrays.asList(Keys.C_USER__FK_C_USER_USER_ROLE);
    }

    private transient UserRolePath _userRole;

    /**
     * Get the implicit join path to the <code>cacophony.user_role</code> table.
     */
    public UserRolePath userRole() {
        if (_userRole == null)
            _userRole = new UserRolePath(this, Keys.C_USER__FK_C_USER_USER_ROLE, null);

        return _userRole;
    }

    private transient MessagePath _message;

    /**
     * Get the implicit to-many join path to the <code>cacophony.message</code> table
     */
    public MessagePath message() {
        if (_message == null)
            _message = new MessagePath(this, null, Keys.MESSAGE__FK_MESSAGE_C_USER.getInverseKey());

        return _message;
    }

    private transient ReactPath _react;

    /**
     * Get the implicit to-many join path to the <code>cacophony.react</code> table
     */
    public ReactPath react() {
        if (_react == null)
            _react = new ReactPath(this, null, Keys.REACT__FK_REACT_C_USER.getInverseKey());

        return _react;
    }

    private transient UserConversationPath _userConversation;

    /**
     * Get the implicit to-many join path to the <code>cacophony.user_conversation</code> table
     */
    public UserConversationPath userConversation() {
        if (_userConversation == null)
            _userConversation = new UserConversationPath(this, null,
                    Keys.USER_CONVERSATION__FK_USER_CONVERSATION_C_USER.getInverseKey());

        return _userConversation;
    }

    @Override
    public CUser as(String alias) {
        return new CUser(DSL.name(alias), this);
    }

    @Override
    public CUser as(Name alias) {
        return new CUser(alias, this);
    }

    @Override
    public CUser as(Table<?> alias) {
        return new CUser(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public CUser rename(String name) {
        return new CUser(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CUser rename(Name name) {
        return new CUser(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public CUser rename(Table<?> name) {
        return new CUser(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public CUser where(Condition condition) {
        return new CUser(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public CUser where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public CUser where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public CUser where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public CUser where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public CUser where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public CUser where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public CUser where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public CUser whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public CUser whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
