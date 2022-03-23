package com.ilex.debug.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(indexes = @Index(name = "IDX_TMP_DATA_LIST_CORREL_ID", columnList = TemporaryDataListAttr.CORRELATION_ID))
public class TemporaryDataListAttr {
  static final String CORRELATION_ID = "correlation_id";

  @Id
  private String id;

  @Column(name = CORRELATION_ID, insertable = true, updatable = false)
  private String correlationId;

  @Column
  private String info;

  @Column(insertable = true, updatable = false)
  private Instant insertDate;

  @ElementCollection
  @CollectionTable(indexes = @Index(name = "IDX_TMP_DATA_ID_LIST_VALUES", columnList = "temporary_data_list_attr_id"))
  private List<String> strValues = new ArrayList<>();

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

  public List<String> getStrValues() {
    return strValues;
  }

  public void setStrValues(final List<String> values) {
    this.strValues = values;
  }

  public void addValues(final Collection<String> values) {
    this.strValues.addAll(values);
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
