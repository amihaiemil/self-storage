/*
 * This file is generated by jOOQ.
 */
package com.selfxdsd.storage.generated.jooq.tables;


import com.selfxdsd.storage.generated.jooq.Indexes;
import com.selfxdsd.storage.generated.jooq.Keys;
import com.selfxdsd.storage.generated.jooq.SelfXdsd;
import com.selfxdsd.storage.generated.jooq.tables.records.SlfTasksXdsdRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row9;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SlfTasksXdsd extends TableImpl<SlfTasksXdsdRecord> {

    private static final long serialVersionUID = -645327908;

    /**
     * The reference instance of <code>self_xdsd.slf_tasks_xdsd</code>
     */
    public static final SlfTasksXdsd SLF_TASKS_XDSD = new SlfTasksXdsd();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SlfTasksXdsdRecord> getRecordType() {
        return SlfTasksXdsdRecord.class;
    }

    /**
     * The column <code>self_xdsd.slf_tasks_xdsd.repo_fullname</code>.
     */
    public final TableField<SlfTasksXdsdRecord, String> REPO_FULLNAME = createField(DSL.name("repo_fullname"), org.jooq.impl.SQLDataType.VARCHAR(256).nullable(false), this, "");

    /**
     * The column <code>self_xdsd.slf_tasks_xdsd.issueId</code>.
     */
    public final TableField<SlfTasksXdsdRecord, String> ISSUEID = createField(DSL.name("issueId"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>self_xdsd.slf_tasks_xdsd.provider</code>.
     */
    public final TableField<SlfTasksXdsdRecord, String> PROVIDER = createField(DSL.name("provider"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>self_xdsd.slf_tasks_xdsd.isPullRequest</code>.
     */
    public final TableField<SlfTasksXdsdRecord, Boolean> ISPULLREQUEST = createField(DSL.name("isPullRequest"), org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>self_xdsd.slf_tasks_xdsd.role</code>.
     */
    public final TableField<SlfTasksXdsdRecord, String> ROLE = createField(DSL.name("role"), org.jooq.impl.SQLDataType.VARCHAR(32).nullable(false), this, "");

    /**
     * The column <code>self_xdsd.slf_tasks_xdsd.username</code>.
     */
    public final TableField<SlfTasksXdsdRecord, String> USERNAME = createField(DSL.name("username"), org.jooq.impl.SQLDataType.VARCHAR(100), this, "");

    /**
     * The column <code>self_xdsd.slf_tasks_xdsd.assigned</code>.
     */
    public final TableField<SlfTasksXdsdRecord, LocalDateTime> ASSIGNED = createField(DSL.name("assigned"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>self_xdsd.slf_tasks_xdsd.deadline</code>.
     */
    public final TableField<SlfTasksXdsdRecord, LocalDateTime> DEADLINE = createField(DSL.name("deadline"), org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>self_xdsd.slf_tasks_xdsd.estimation_minutes</code>.
     */
    public final TableField<SlfTasksXdsdRecord, Integer> ESTIMATION_MINUTES = createField(DSL.name("estimation_minutes"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * Create a <code>self_xdsd.slf_tasks_xdsd</code> table reference
     */
    public SlfTasksXdsd() {
        this(DSL.name("slf_tasks_xdsd"), null);
    }

    /**
     * Create an aliased <code>self_xdsd.slf_tasks_xdsd</code> table reference
     */
    public SlfTasksXdsd(String alias) {
        this(DSL.name(alias), SLF_TASKS_XDSD);
    }

    /**
     * Create an aliased <code>self_xdsd.slf_tasks_xdsd</code> table reference
     */
    public SlfTasksXdsd(Name alias) {
        this(alias, SLF_TASKS_XDSD);
    }

    private SlfTasksXdsd(Name alias, Table<SlfTasksXdsdRecord> aliased) {
        this(alias, aliased, null);
    }

    private SlfTasksXdsd(Name alias, Table<SlfTasksXdsdRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> SlfTasksXdsd(Table<O> child, ForeignKey<O, SlfTasksXdsdRecord> key) {
        super(child, key, SLF_TASKS_XDSD);
    }

    @Override
    public Schema getSchema() {
        return SelfXdsd.SELF_XDSD;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.SLF_TASKS_XDSD_ASSIGNEE, Indexes.SLF_TASKS_XDSD_PARENT_PROJECT);
    }

    @Override
    public UniqueKey<SlfTasksXdsdRecord> getPrimaryKey() {
        return Keys.KEY_SLF_TASKS_XDSD_PRIMARY;
    }

    @Override
    public List<UniqueKey<SlfTasksXdsdRecord>> getKeys() {
        return Arrays.<UniqueKey<SlfTasksXdsdRecord>>asList(Keys.KEY_SLF_TASKS_XDSD_PRIMARY);
    }

    @Override
    public List<ForeignKey<SlfTasksXdsdRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<SlfTasksXdsdRecord, ?>>asList(Keys.ASSIGNEE, Keys.PARENT_PROJECT);
    }

    public SlfContractsXdsd slfContractsXdsd() {
        return new SlfContractsXdsd(this, Keys.ASSIGNEE);
    }

    public SlfProjectsXdsd slfProjectsXdsd() {
        return new SlfProjectsXdsd(this, Keys.PARENT_PROJECT);
    }

    @Override
    public SlfTasksXdsd as(String alias) {
        return new SlfTasksXdsd(DSL.name(alias), this);
    }

    @Override
    public SlfTasksXdsd as(Name alias) {
        return new SlfTasksXdsd(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public SlfTasksXdsd rename(String name) {
        return new SlfTasksXdsd(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public SlfTasksXdsd rename(Name name) {
        return new SlfTasksXdsd(name, null);
    }

    // -------------------------------------------------------------------------
    // Row9 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row9<String, String, String, Boolean, String, String, LocalDateTime, LocalDateTime, Integer> fieldsRow() {
        return (Row9) super.fieldsRow();
    }
}
