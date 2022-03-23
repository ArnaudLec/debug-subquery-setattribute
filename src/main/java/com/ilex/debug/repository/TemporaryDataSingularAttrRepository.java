package com.ilex.debug.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.ilex.debug.entity.TemporaryDataSingularAttr;

public interface TemporaryDataSingularAttrRepository extends JpaRepositoryImplementation<TemporaryDataSingularAttr, UUID> {

}
