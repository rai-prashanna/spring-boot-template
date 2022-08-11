package com.tc.execution.service;

import com.tc.oc.common.models.PropertyDTO;;
import java.util.List;

public interface PropertyService {
    PropertyDTO getPropertyByKey(String key);
    void saveProperty(PropertyDTO propertyDTO);
    List<PropertyDTO> getAllProperties();
}
