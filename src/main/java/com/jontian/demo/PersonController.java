package com.jontian.demo;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class PersonController {

    public PagedResources<?> toPagedResources(Page<?> data, Class<?> clazz) {
        List<?> content = data.getContent();

        if (content.isEmpty()) {
            EmbeddedWrapper wrapper = new EmbeddedWrappers(false).emptyCollectionOf(clazz);
            content = Collections.singletonList(wrapper);
        }
        return new PagedResources<>(content,
                new PagedResources.PageMetadata(
                        (long) data.getSize(), (long) data.getNumber(), (long) data.getTotalElements(), (long) data.getTotalPages())
        );

    }
}
