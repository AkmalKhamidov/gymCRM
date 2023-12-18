package com.epamlearning;

import com.epamlearning.configs.ApplicationConfig;
import com.epamlearning.facade.GymCRMFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class App {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                ApplicationConfig.class
        );

        GymCRMFacade gymCRMFacade = context.getBean(GymCRMFacade.class);
        gymCRMFacade.getPage("");
    }
}
