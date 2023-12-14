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
    public void getPage() {
        System.out.flush();
        System.out.println("Select to login as Trainee or Trainer");
        System.out.println("1. Trainee");
        System.out.println("2. Trainer");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine();

        String username;
        String password;

        switch (choice) {
            case 1 -> {
                boolean isTraineeUserNameAndPasswordMatching = false;
                do {
                    System.out.print("Enter your username: ");
                    username = sc.next();
                    System.out.print("Enter your password: ");
                    password = sc.next();
                    try {
                        isTraineeUserNameAndPasswordMatching = traineeUserNameAndPasswordMatching(username, password);
                    } catch (NotAuthenticated e) {
                        System.out.println("Your username and password does not match.");
                    } catch (NotFoundException e) {
                        System.out.println("Trainee with this username not found.");
                    }
                    if (isTraineeUserNameAndPasswordMatching) {
                        traineeFacade.setTraineeUsername(username);
                        traineeFacade.getPage();
                    }
                } while (!isTraineeUserNameAndPasswordMatching);
            }
            case 2 -> {
                boolean isTrainerUserNameAndPasswordMatching = false;
                do {
                    System.out.print("Enter your username: ");
                    username = sc.next();
                    System.out.print("Enter your password: ");
                    password = sc.next();
                    try {
                        isTrainerUserNameAndPasswordMatching = trainerUserNameAndPasswordMatching(username, password);
                    } catch (NotAuthenticated e) {
                        System.out.println("Your username and password does not match.");
                    } catch (NotFoundException e) {
                        System.out.println("Trainer with this username not found.");
                    }
                    if (isTrainerUserNameAndPasswordMatching) {
                        trainerFacade.getPage();
                    }
                } while (!isTrainerUserNameAndPasswordMatching);
            }
            case 3 -> {
                System.out.println("Thank you for using Gym CRM");
            }
            default -> {
                System.out.println("Invalid choice");
            }
        }
    }

    public boolean traineeUserNameAndPasswordMatching(String username, String password) {
        return traineeService.authenticate(username, password);
    }

    public boolean trainerUserNameAndPasswordMatching(String username, String password) {
        return trainerService.authenticate(username, password);
    }


}
