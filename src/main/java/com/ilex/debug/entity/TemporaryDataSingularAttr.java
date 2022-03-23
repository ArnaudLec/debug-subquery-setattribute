package com.ilex.debug.entity;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(indexes = @Index(name = "IDX_TMP_DATA_SINGULAR_CORREL_ID", columnList = TemporaryDataSingularAttr.CORRELATION_ID))
public class TemporaryDataSingularAttr {
  static final String CORRELATION_ID = "correlation_id";

  @Id
  private String id;

  @Column(name = CORRELATION_ID, insertable = true, updatable = false, nullable = false)
  private String correlationId;

  @Column
  private String info;

  @Column(insertable = true, updatable = false, nullable = false)
  private Instant insertDate;

  @Column(insertable = true, updatable = false, nullable = false)
  private String value;

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(final String correlationId) {
    this.correlationId = correlationId;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(final String info) {
    this.info = info;
  }

  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }

  public Instant getInsertDate() {
    return insertDate;
  }

  public void setInsertDate(final Instant insertDate) {
    this.insertDate = insertDate;
  }

  @PrePersist
  void prePersist() {
    if (id == null) {
      id = UUID.randomUUID().toString();
    }
    insertDate = Instant.now();
  }
}
