package com.epamlearning.facade;

import com.epamlearning.models.Trainee;
import com.epamlearning.services.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

@Component
public class TraineeFacade implements Facade {

    private final TraineeService traineeService;

    private String traineeUsername;
    Scanner sc = new Scanner(System.in);

    @Autowired
    public TraineeFacade(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    public String getTraineeUsername() {
        return traineeUsername;
    }

    public void setTraineeUsername(String traineeUsername) {
        this.traineeUsername = traineeUsername;
    }

    @Override
    public void getPage() {
        System.out.println("Trainee Main Page");
        System.out.println("1. Profile");
        System.out.println("2. Trainer");
        System.out.println("3. Exit");

        System.out.print("Input your choice: ");

        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1 -> {
                System.out.println("Trainee Profile");
                try {
                    Trainee trainee = getTraineeByUsername(getTraineeUsername());

                    System.out.println("Username: " + trainee.getUser().getUsername());
                    System.out.println("FirstName: " + trainee.getUser().getFirstName());
                    System.out.println("LastName: " + trainee.getUser().getLastName());
                    System.out.println("Address: " + trainee.getAddress());
                    System.out.println("Date of Birth: " + trainee.getDateOfBirth());
                } catch (Exception e) {
                    System.out.println("An error occurred when fetching trainee details.");
                    getPage();
                }

                System.out.println("1. Update Profile");
                System.out.println("2. Exit");

                int innerChoice = sc.nextInt();
                sc.nextLine();
                switch (innerChoice) {
                    case 1 -> {
                        System.out.println("Update Trainee Profile");
                        System.out.print("Enter your first name: ");
                        String firstName = sc.nextLine();
                        System.out.print("Enter your last name: ");
                        String lastName = sc.nextLine();
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
                        try {
                            Trainee trainee = getTraineeByUsername(getTraineeUsername());
                            trainee.getUser().setFirstName(firstName);
                            trainee.getUser().setLastName(lastName);
                            trainee.setAddress(address);
                            trainee.setDateOfBirth(dateOfBirth);
                            updateTrainee(trainee.getId(), trainee);
                            System.out.println("Trainee profile updated successfully.");
                        } catch (Exception e) {
                            System.out.println("An error occurred when updating trainee details.");
                            getPage();
                        }
                    }
                    case 2 -> getPage();
                }


            }
        }
    }

    public Trainee getTraineeByUsername(String traineeUsername) {
            return traineeService.findByUsername(traineeUsername).get();
    }

    public Trainee updateTrainee(Long traineeId, Trainee trainee) {
            return traineeService.update(traineeId, trainee).get();
    }

}
