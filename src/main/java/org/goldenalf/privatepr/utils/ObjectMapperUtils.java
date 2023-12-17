package org.goldenalf.privatepr.utils;

import org.goldenalf.privatepr.dto.BookDto;
import org.goldenalf.privatepr.models.Book;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperUtils {

    @Bean
    public ModelMapper modelMapper () {
        ModelMapper modelMapper = new ModelMapper();
        //modelMapper.typeMap(ReviewDto.class, Review.class).addMappings(mapper -> mapper.skip(Review::setId));
        modelMapper.typeMap(BookDto.class, Book.class).addMappings(mapper -> mapper.skip(Book::setId));
        return modelMapper;
    }
}
