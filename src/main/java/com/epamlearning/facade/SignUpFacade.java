package com.epamlearning.facade;

import com.epamlearning.models.Trainee;
import com.epamlearning.models.Trainer;
import com.epamlearning.models.TrainingType;
import com.epamlearning.models.User;
import com.epamlearning.services.TraineeService;
import com.epamlearning.services.TrainerService;
import com.epamlearning.services.TrainingTypeService;
import com.epamlearning.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

@Component
public class SignUpFacade implements Facade{

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;
    private final UserService userService;

    Scanner sc = new Scanner(System.in);

    @Autowired
    public SignUpFacade(TraineeService traineeService, TrainerService trainerService, TrainingTypeService trainingTypeService, UserService userService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingTypeService = trainingTypeService;
        this.userService = userService;
    }

    @Override
    public void getPage() {
        System.out.println("Welcome to Gym CRM");
        System.out.println("Sign Up as Trainee or Trainer");
        System.out.println("1. Trainee");
        System.out.println("2. Trainer");
        System.out.println("3. Exit");

        System.out.print("Input your choice: ");

        String firstName;
        String lastName;
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1 -> {
                System.out.print("Enter your first name: ");
                firstName = sc.nextLine();
                System.out.print("Enter your last name: ");
                lastName = sc.nextLine();
                System.out.print("Enter your address: ");
                String address = sc.nextLine();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date dateOfBirth = null;
                do {
                    System.out.print("Enter your date of birth (dd/mm/yyyy): ");
                    String dateStr = sc.next();
                    try {
                        if(sdf.parse(dateStr).after(new Calendar.Builder().setDate(1900, Calendar.JANUARY, 1).build().getTime())
                                && sdf.parse(dateStr).before(new Date())){
                            dateOfBirth = sdf.parse(dateStr);
                        } else {
                            System.out.println("Invalid date. Please try again.");
                        }
                        System.out.println(sdf.parse(dateStr));
                    } catch (ParseException e) {
                        System.out.println("Invalid date format. Please try again.");
                    }
                } while (dateOfBirth == null);
                try{
                    createTrainee(firstName, lastName, address, dateOfBirth);
                } catch (Exception e) {
                    System.out.println("Error creating trainee. Error: " + e.getMessage());
                    getPage();
                }
            }
            case 2 -> {
                System.out.print("Enter your first name: ");
                firstName = sc.nextLine();
                System.out.print("Enter your last name: ");
                lastName = sc.nextLine();
                System.out.print("List of training types: ");
                List<TrainingType> trainingTypeList = trainingTypeService.findAll()
                        .stream()
                        .flatMap(Optional::stream)
                        .toList();

                IntStream.range(0, trainingTypeList.size())
                        .forEach(index -> {
                            TrainingType trainingType = trainingTypeList.get(index);
                            System.out.println("Index: " + index + ". Name: " + trainingType.getTrainingTypeName());
                        });

                Integer trainingTypeIndex;

                do {
                    System.out.print("Enter your training type index: ");
                    trainingTypeIndex = sc.nextInt();
                    if(trainingTypeIndex < 0 || trainingTypeIndex > trainingTypeList.size()) {
                        System.out.println("Invalid training type index. Please try again.");
                    }
                } while (trainingTypeIndex == null);

                try{
                    createTrainer(firstName, lastName, trainingTypeList.get(trainingTypeIndex));
                } catch (Exception e){
                    System.out.println("Error creating trainer. Error: " + e.getMessage());
                    getPage();
                }
            }
            case 3 -> System.out.println("Thank you for using Gym CRM");
            default -> System.out.println("Invalid choice");
        }
    }

    public Trainee createTrainee(String firstName, String lastName, String address, Date dateOfBirth) {
        User user = userService.createUser(firstName, lastName);
        return traineeService.save(traineeService.createTrainee(user, address, dateOfBirth)).get();
    }

    public Trainer createTrainer(String firstName, String lastName, TrainingType trainingType) {
        User user = userService.createUser(firstName, lastName);
        return trainerService.save(trainerService.createTrainer(user, trainingType)).get();
    }


}
