package com.seun.pree6.configuration;

import com.seun.pree6.other.CourseSelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManufacturingConfiguration {

    @Bean
    public CourseSelector newCourseSelector() {
        return new CourseSelector();
    }
}

