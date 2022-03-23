package com.ilex.debug.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.ilex.debug.entity.TemporaryDataListAttr;

public interface TemporaryDataListAttrRepository extends JpaRepositoryImplementation<TemporaryDataListAttr, UUID> {

}
