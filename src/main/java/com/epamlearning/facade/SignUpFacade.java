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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Component
public class SignUpFacade implements Facade{

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;
    private final UserService userService;

    private final LogInFacade logInFacade;

    @Autowired
    public SignUpFacade(TraineeService traineeService, TrainerService trainerService, TrainingTypeService trainingTypeService, UserService userService, LogInFacade logInFacade) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingTypeService = trainingTypeService;
        this.userService = userService;
        this.logInFacade = logInFacade;
    }

    @Override
    public void getPage(String response) {
        cleanScreen();

        System.out.println("Welcome to Gym CRM");
        System.out.println("Sign Up as Trainee or Trainer");
        System.out.println("1. Trainee");
        System.out.println("2. Trainer");
        System.out.println("3. Exit");

        System.out.print("Input your choice: ");

        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1 -> {
                traineeSignUp();
            }
            case 2 -> {
                trainerSignUp();
            }
            case 3 -> System.out.println("Thank you for using Gym CRM");
            default -> System.out.println("Invalid choice");
        }
    }

    public void traineeSignUp(){
        String firstName;
        String lastName;
        String response;
        System.out.println("Trainee Sign Up");
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
            logInFacade.getPage("Trainee created successfully. Please login to continue.");
        } catch (Exception e) {
            getPage("Error creating trainee. Error: " + e.getMessage());
        }
    }

    public void trainerSignUp(){
        String firstName;
        String lastName;
        System.out.println("Trainer Sign Up");
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

        Integer trainingTypeIndex = -1;

        do {
            System.out.print("Enter your training type index : ");
            trainingTypeIndex = sc.nextInt();
            if(trainingTypeIndex < 0 || trainingTypeIndex > trainingTypeList.size()) {
                System.out.println("Invalid training type index. Please try again.");
            }
        } while (trainingTypeIndex == -1);

        try{
            createTrainer(firstName, lastName, trainingTypeIndex != null ? trainingTypeList.get(trainingTypeIndex): null);
            logInFacade.getPage("Trainer created successfully. Please login to continue.");
        } catch (Exception e){
            getPage("Error creating trainer. Error: " + e.getMessage());
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
