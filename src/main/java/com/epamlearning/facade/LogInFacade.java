package com.epamlearning.facade;

import com.epamlearning.exceptions.NotAuthenticated;
import com.epamlearning.exceptions.NotFoundException;
import com.epamlearning.services.TraineeService;
import com.epamlearning.services.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class LogInFacade implements Facade{

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TraineeFacade traineeFacade;
    private final TrainerFacade trainerFacade;

    @Autowired
    public LogInFacade(TraineeService traineeService, TrainerService trainerService, TraineeFacade traineeFacade, TrainerFacade trainerFacade) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.traineeFacade = traineeFacade;
        this.trainerFacade = trainerFacade;
    }

    Scanner sc = new Scanner(System.in);

    @Override
    public void getPage(String response) {
        cleanScreen();
        System.out.println(response + "\n");
        System.out.println("Select to login as Trainee or Trainer");
        System.out.println("1. Trainee");
        System.out.println("2. Trainer");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> {
                traineeLogin();
            }
            case 2 -> {
                trainerLogin();
            }
            case 3 -> {
                System.out.println("Thank you for using Gym CRM");
            }
            default -> {
                System.out.println("Invalid choice");
            }
        }
    }

    public void traineeLogin(){
        String username;
        String password;
        Long traineeId = null;
        boolean isTraineeUserNameAndPasswordMatching = false;
        do {
            System.out.print("Enter your username: ");
            username = sc.next();
            System.out.print("Enter your password: ");
            password = sc.next();
            try {
                traineeId = traineeAuthenticate(username, password);
                if(traineeId != null) {
                    isTraineeUserNameAndPasswordMatching = true;
                }
            } catch (NotAuthenticated e) {
                System.out.println("Your username and password does not match.");
            } catch (NotFoundException e) {
                System.out.println("Trainee with this username not found.");
            }
            if (isTraineeUserNameAndPasswordMatching) {
                traineeFacade.setTraineeUsername(username);
                traineeFacade.setTraineeId(traineeId);
                traineeFacade.getPage("Successfully logged in.");
            }
        } while (!isTraineeUserNameAndPasswordMatching);
    }

    public void trainerLogin(){
        String username;
        String password;
        Long trainerId = null;
        boolean isTrainerUserNameAndPasswordMatching = false;
        do {
            System.out.print("Enter your username: ");
            username = sc.next();
            System.out.print("Enter your password: ");
            password = sc.next();
            try {
                trainerId = trainerAuthenticate(username, password);
                if(trainerId != null) {
                    isTrainerUserNameAndPasswordMatching = true;
                }
            } catch (NotAuthenticated e) {
                System.out.println("Your username and password does not match.");
            } catch (NotFoundException e) {
                System.out.println("Trainer with this username not found.");
            }
            if (isTrainerUserNameAndPasswordMatching) {
                trainerFacade.setTrainerUsername(username);
                trainerFacade.setTrainerId(trainerId);
                trainerFacade.getPage("Successfully logged in.");
            }
        } while (!isTrainerUserNameAndPasswordMatching);
    }

    public Long traineeAuthenticate(String username, String password) {
        return traineeService.authenticate(username, password);
    }

    public Long trainerAuthenticate(String username, String password) {
        return trainerService.authenticate(username, password);
    }


}
