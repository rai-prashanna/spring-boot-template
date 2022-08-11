package com.tc.execution.data;
import com.tc.oc.common.models.Range;
import org.dozer.DozerBeanMapper;
import org.dozer.DozerBeanMapperBuilder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class DozerMapperBean implements DozerMapper {
    private DozerBeanMapper mapper;

    public DozerMapperBean() {
        mapper = (DozerBeanMapper) DozerBeanMapperBuilder.create().build();
    }

    @Override
    public final <T> T create(final Class<T> target, final Object source) {
        return this.create(target, source, null);
    }


    @Override
    public final <T> T create(final Class<T> target, final Object source, final String mappingId) {
        if (source == null) {
            return null;
        }
        T result;
        if (mappingId != null) {
            result = mapper.map(source, target, mappingId);
        } else {
            result = mapper.map(source, target);
        }
        return result;
    }

    @Override
    public final <T, S> List<T> createList(final Class<T> target, final Iterable<S> source)  {
        return createList(target, source, null);
    }

    @Override
    public final <T, S> List<T> createList(final Class<T> target, final Iterable<S> source, final String mappingId) {
        return StreamSupport.stream(source.spliterator(), false).map(o -> create(target, o, mappingId))
                .collect(Collectors.toList());
    }




    @Override
    public final void transfer(final Object source, final Object target) {
        mapper.map(source, target);
    }

    public final DozerBeanMapper getMapper() {
        return this.mapper;
    }
}
