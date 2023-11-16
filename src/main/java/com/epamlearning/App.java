package com.epamlearning;

import com.epamlearning.configs.ApplicationConfig;
import com.epamlearning.facade.GymCRMFacade;
import com.epamlearning.models.Trainee;
import com.epamlearning.models.Trainer;
import com.epamlearning.storage.InMemoryStorage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Calendar;
import java.util.Date;

public class App {


    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                ApplicationConfig.class
        );

        GymCRMFacade gymCRMFacade = context.getBean(GymCRMFacade.class);


        System.out.println("\n============      GET ALL TRAINEES      ============");
        gymCRMFacade.getAllTrainees();
//                .forEach(trainee -> {
//            System.out.println(trainee);
            System.out.println("------------------------------------------------");
//        });
        System.out.println("====================================================");


        System.out.println("\n============      GET ALL TRAINERS      ============");
        gymCRMFacade.getAllTrainers();
//                .forEach(trainer -> {
//            System.out.println(trainer);
            System.out.println("------------------------------------------------");
//        });
        System.out.println("====================================================");


        System.out.println("\n============      GET ALL TRAININGS      ============");
        gymCRMFacade.getAllTrainings();
//                .forEach(training -> {
//            System.out.println(training);
            System.out.println("------------------------------------------------");
//        });
        System.out.println("====================================================");



        Trainee trainee = gymCRMFacade.createTrainee("Akmal", "Khamidov", "BIY 4-7", new Date(2003, Calendar.JANUARY, 3));
        System.out.println(trainee);

        Trainer trainer = gymCRMFacade.createTrainer("Akmal", "Khamidov", "Random Specialization");
        System.out.println(trainer);

    }
}
