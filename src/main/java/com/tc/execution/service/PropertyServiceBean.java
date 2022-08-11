package com.tc.execution.service;


import com.tc.execution.data.DozerMapper;
import com.tc.execution.persistence.entity.PropertyEntity;
import com.tc.execution.persistence.repository.PropertyRepository;
import com.tc.oc.common.models.PropertyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@Service("PropertyService")
public class PropertyServiceBean implements PropertyService{

    @Autowired
    PropertyRepository propertyRepository;

    @Autowired
    DozerMapper dozerMapper;

    @PostConstruct
    private void setup(){

    }

    @Override
    public PropertyDTO getPropertyByKey(String key){
        PropertyEntity entity = propertyRepository.findByKey(key);
        if (entity != null){
            return dozerMapper.create(PropertyDTO.class, entity);
        }
        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setKey(key);
        return propertyDTO;
    }

    @Override
    public List<PropertyDTO> getAllProperties(){
        List<PropertyEntity> entities = propertyRepository.findAll();
        if (!entities.isEmpty()){
            return dozerMapper.createList(PropertyDTO.class, entities);
        }
        return new ArrayList<>();
    }


    @Override
    public void saveProperty(PropertyDTO propertyDTO){
        PropertyEntity entity = propertyRepository.findByKey(propertyDTO.getKey());
        if (entity == null) {
            PropertyEntity newEntity = new PropertyEntity();
            newEntity.setKey(propertyDTO.getKey());
            newEntity.setValue(propertyDTO.getValue());
            propertyRepository.save(newEntity);
        } else {
            entity.setValue(propertyDTO.getValue());
            propertyRepository.save(entity);
        }
    }
}
