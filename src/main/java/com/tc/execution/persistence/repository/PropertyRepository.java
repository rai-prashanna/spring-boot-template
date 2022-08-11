package com.tc.execution.persistence.repository;

import com.tc.execution.persistence.entity.PropertyEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity,Long> {
    PropertyEntity findByKey(String key);
}
