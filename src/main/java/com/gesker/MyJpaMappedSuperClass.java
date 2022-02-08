package com.gesker;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.logging.Logger;

@MappedSuperclass
public class MyJpaMappedSuperClass {
    private static final Logger LOGGER = Logger.getLogger(MyJpaMappedSuperClass.class.getName());


    private boolean enabled = true;
    private Date createdOn;
    private long createdBy = 1;
    private Date modifiedOn;
    private long modifiedBy = 1;


    @Column(name = "enabled", columnDefinition = "BOOLEAN DEFAULT TRUE CHECK (enabled = TRUE OR enabled = FALSE)", nullable = false, updatable = true, insertable = true, unique = false)
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", columnDefinition = "timestamp with time zone DEFAULT current_timestamp", nullable = false, updatable = false, insertable = true, unique = false)
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "created_by", columnDefinition = "BIGINT DEFAULT 1 NOT NULL", nullable = false, updatable = false, insertable = true, unique = false)
    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    @Version
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_on", columnDefinition = "timestamp with time zone DEFAULT current_timestamp", nullable = false, updatable = true, insertable = true, unique = false)
    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @Column(name = "modified_by", columnDefinition = "BIGINT DEFAULT 1 NOT NULL", nullable = false, updatable = true, insertable = true, unique = false)
    public long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}


