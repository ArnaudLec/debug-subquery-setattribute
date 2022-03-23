package com.ilex.debug.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.ilex.debug.entity.TemporaryDataSetAttr;

public interface TemporaryDataSetAttrRepository extends JpaRepositoryImplementation<TemporaryDataSetAttr, UUID> {

}
