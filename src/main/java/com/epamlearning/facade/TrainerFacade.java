package com.epamlearning.facade;

import com.epamlearning.models.Trainee;
import com.epamlearning.models.Trainer;
import com.epamlearning.models.Training;
import com.epamlearning.models.TrainingType;
import com.epamlearning.services.TraineeService;
import com.epamlearning.services.TrainerService;
import com.epamlearning.services.TrainingService;
import com.epamlearning.services.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Component
public class TrainerFacade implements Facade {

    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;
    private final TrainingService trainingService;
    private final TraineeService traineeService;
    private String trainerUsername;
    private Long trainerId;
    private Trainer trainer;
    String response;

    @Autowired
    public TrainerFacade(TrainerService trainerService, TrainingTypeService trainingTypeService, TrainingService trainingService, TraineeService traineeService) {
        this.trainerService = trainerService;
        this.trainingTypeService = trainingTypeService;
        this.trainingService = trainingService;
        this.traineeService = traineeService;
    }

    public String getTrainerUsername() {
        return trainerUsername;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }

    @Override
    public void getPage(String response)  {
        cleanScreen();
        System.out.println(response + "\n");

        trainer = getTrainerByUsername(getTrainerUsername());
        System.out.println("Trainer Main Page");
        System.out.println("1. Profile");
        System.out.println("2. Trainings");
        System.out.println("3. Exit");

        System.out.print("Input your choice: ");
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1 -> trainerProfilePage("");
            case 2 -> trainerTrainingsPage("");
            case 3 -> System.out.println("Thank you for using Gym CRM");
            default -> System.out.println("Invalid choice");
        }

    }

    public void trainerTrainingsPage(String response) {
        System.out.println(response + "\n");

        System.out.println("Trainer's Trainings");
        System.out.println("1. View All Trainer's Trainings");
        System.out.println("2. View Trainer's Trainings by Criteria");
        System.out.println("3. Add training");
        System.out.println("4. Back to Trainer Main Page");
        System.out.println("5. Exit");
        System.out.print("Input your choice: ");

        int innerChoice = sc.nextInt();
        sc.nextLine();
        switch (innerChoice) {
            case 1 -> viewAllTrainerTrainings();
            case 2 -> viewTrainerTrainingsByCriteria();
            case 3 -> addTrainingPage();
            case 4 -> getPage("");
            case 5 -> System.out.println("Thank you for using Gym CRM");
            default -> System.out.println("Invalid choice.");
        }

    }

    public void addTrainingPage() {
        System.out.println("Adding Training");
        System.out.println("Enter training name: ");
        String trainingName = sc.nextLine();
        System.out.println("Enter training duration: ");
        BigDecimal trainingDuration = sc.nextBigDecimal();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date trainingDate = null;
        do {
            System.out.print("Enter training date (dd/mm/yyyy): ");
            String dateStr = sc.next();
            try {
                trainingDate = sdf.parse(dateStr);
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please try again.");
            }
        } while (trainingDate == null);

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
            System.out.print("Enter your training type index from list (-1 -> all): ");
            trainingTypeIndex = sc.nextInt();
            if (trainingTypeIndex < -1 || trainingTypeIndex > trainingTypeList.size()) {
                System.out.println("Invalid training type index. Please try again.");
            }
        } while (trainingTypeIndex == null);

        TrainingType trainingType = trainingTypeIndex != -1 ? trainingTypeList.get(trainingTypeIndex) : null;

        List<Trainee> traineeList = traineeService.findAll()
                .stream()
                .flatMap(Optional::stream)
                .toList();

        IntStream.range(0, traineeList.size())
                .forEach(index -> {
                    Trainee trainee = traineeList.get(index);
                    System.out.println("Index: " + index + ". FullName: " + trainee.getUser().getFirstName() +
                            " " + trainee.getUser().getLastName() + " Date of birth: " + trainee.getDateOfBirth());
                });

        Integer traineeIndex;

        do {
            System.out.print("Enter trainee index from list (-1 -> all): ");
            traineeIndex = sc.nextInt();
            if (traineeIndex < -1 || traineeIndex > traineeList.size()) {
                System.out.println("Invalid trainer type index. Please try again.");
            }
        } while (traineeIndex == null);

        Trainee trainee = traineeIndex != -1 ? traineeList.get(traineeIndex) : null;

        try {
            createTraining(trainingName, trainingDate, trainingDuration, trainer, trainee, trainingType);
            response = "Training created successfully.";
        } catch (Exception e) {
            response = "An error occurred when creating training.";
        }
        trainerTrainingsPage(response);
    }

    public void viewTrainerTrainingsByCriteria() {
        System.out.println("View Trainer's Trainings by Criteria");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.print("Enter trainings start date (dd/mm/yyyy): ");
        String startDateStr = sc.next();
        Date startDate = null;
        try {
            startDate = sdf.parse(startDateStr);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please try again.");
            viewTrainerTrainingsByCriteria();
        }

        System.out.print("Enter trainings end date (dd/mm/yyyy): ");
        String endDateStr = sc.next();
        Date endDate = null;
        try {
            endDate = sdf.parse(endDateStr);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please try again.");
            viewTrainerTrainingsByCriteria();
        }

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
            System.out.print("Enter your training type index (-1 -> all): ");
            trainingTypeIndex = sc.nextInt();
            if (trainingTypeIndex < -1  || trainingTypeIndex > trainingTypeList.size()) {
                System.out.println("Invalid training type index. Please try again.");
            }
        } while (trainingTypeIndex == null);
        try {
            List<Training> trainingListByCriteria = getTrainingsByTrainerUsernameAndCriteria(getTrainerUsername(), startDate, endDate, trainingTypeIndex != -1 ? trainingTypeList.get(trainingTypeIndex) : null);
            if (trainingListByCriteria.isEmpty()) {
                System.out.println("No trainings found.\n");
            } else {
                trainingListByCriteria.forEach(training -> {
                    System.out.println();
                    System.out.println("Training type: " + training.getTrainingType().getTrainingTypeName());
                    System.out.println("Training Name: " + training.getTrainingName());
                    System.out.println("Training Duration: " + training.getTrainingDuration());
                    System.out.println("Trainee: " + training.getTrainee().getUser().getFirstName() + " " + training.getTrainee().getUser().getLastName());
                    System.out.println("Date: " + training.getTrainingDate());
                    System.out.println();
                });
            }

            System.out.println("Press Enter back to Trainer Main Page");

            waitForEnterKey(sc);
            trainerTrainingsPage("");

        } catch (Exception e) {
            response = "An error occurred when fetching trainee's trainings.";
            trainerTrainingsPage(response);
        }
    }

    public void viewAllTrainerTrainings() {
        System.out.println("All Trainer's Trainings");
        try {
            List<Training> trainingList = trainingService.findByTrainer(getTrainerId()).stream().flatMap(Optional::stream).toList();
            if (trainingList.isEmpty()) {
                System.out.println("No trainings found.\n");
            } else {
                trainingList.forEach(training -> {
                    System.out.println();
                    System.out.println("Training type: " + training.getTrainingType().getTrainingTypeName());
                    System.out.println("Training Name: " + training.getTrainingName());
                    System.out.println("Training Duration: " + training.getTrainingDuration());
                    System.out.println("Trainee: " + training.getTrainee().getUser().getFirstName() + " " + training.getTrainee().getUser().getLastName());
                    System.out.println("Date: " + training.getTrainingDate());
                    System.out.println();
                });
            }
            System.out.println("Press Enter back to Trainer Main Page");

            waitForEnterKey(sc);
            trainerTrainingsPage("");

        } catch (Exception e) {
            this.response = "An error occurred when fetching trainer's trainings.";
            trainerTrainingsPage(this.response);
        }
    }

    public void trainerProfilePage(String response) {
        cleanScreen();
        System.out.println(response + "\n");
        try {
            trainer = getTrainerByUsername(getTrainerUsername());
            System.out.println("\nTrainer Profile");
            System.out.println("First Name: " + trainer.getUser().getFirstName());
            System.out.println("Last Name: " + trainer.getUser().getLastName());
            System.out.println("Username: " + trainer.getUser().getUsername());
            System.out.println("Specialization: " + trainer.getSpecialization().getTrainingTypeName());
            System.out.println("Active: " + trainer.getUser().isActive());
        } catch (Exception e) {
            this.response = "An error occurred when fetching trainee details.";
            getPage(this.response);
        }

        System.out.println();
        System.out.println("1. Update Profile");
        System.out.println("2. Change Password");
        System.out.println("3. Change Active");
        System.out.println("4. Back to Trainer Main Page");
        System.out.println("5. Exit");

        System.out.print("Input your choice: ");
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1 -> trainerUpdatePage();
            case 2 -> trainerChangePasswordPage();
            case 3 -> trainerChangeActivePage();
            case 4 -> getPage("");
            case 5 -> System.out.println("Thank you for using Gym CRM");
            default -> System.out.println("Invalid choice");
        }
    }

    public void trainerChangeActivePage() {
        System.out.println("Update Trainer Status");
        System.out.println("1. Activate");
        System.out.println("2. Deactivate");
        System.out.println("3. Back to Trainer Profile Page");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");

        int innerChoice = sc.nextInt();
        sc.nextLine();
        switch (innerChoice) {
            case 1 -> {
                try {
                    trainer = changeTrainerActive(trainer.getId(), true);
                    response = "Trainee status activated successfully.";
                } catch (Exception e) {
                    response = "An error occurred during trainee status activation.";
                }
                trainerProfilePage(response);
            }
            case 2 -> {
                try {
                    trainer = changeTrainerActive(trainer.getId(), false);
                    response = "Trainee status deactivated successfully.";
                } catch (Exception e) {
                    response = "An error occurred during trainee status deactivation.";
                }
                trainerProfilePage(response);
            }
            case 3 -> trainerProfilePage("");
            case 4 -> System.out.println("Thank you for using Gym CRM");
            default -> System.out.println("Invalid choice.");
        }
    }

    public void trainerChangePasswordPage() {
        System.out.println("Change Password");
        System.out.print("Enter your new password: ");
        String newPassword = sc.nextLine();
        try {
            this.trainer = changeTrainerPassword(getTrainerId(), newPassword);
            this.response = "Trainer password changed successfully.";
        } catch (Exception e) {
            this.response = "An error occurred when changing trainer password.";
        }
        trainerProfilePage(this.response);
    }

    public void trainerUpdatePage() {
        System.out.println("Update Trainee Profile");
        System.out.print("Enter your first name: ");
        String firstName = sc.nextLine();
        System.out.print("Enter your last name: ");
        String lastName = sc.nextLine();

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
            System.out.print("Enter your specialization index from list (-1 -> all): ");
            trainingTypeIndex = sc.nextInt();
            if (trainingTypeIndex < -1 || trainingTypeIndex > trainingTypeList.size()) {
                System.out.println("Invalid training type index. Please try again.");
            }
        } while (trainingTypeIndex == null);

        TrainingType trainingType = trainingTypeIndex != null ? trainingTypeList.get(trainingTypeIndex) : null;

        try {
            trainer.getUser().setFirstName(firstName);
            trainer.getUser().setLastName(lastName);
            trainer.setSpecialization(trainingType);
            this.trainer = updateTrainer(getTrainerId(), trainer);
            this.response = "Trainer updated successfully.";
        } catch (Exception e) {
            this.response = "An error occurred when updating trainer details.";
        }
        trainerProfilePage(this.response);
    }

    public Trainer getTrainerByUsername(String trainerUsername) {
            return trainerService.findByUsername(trainerUsername).get();
    }

    public Trainer changeTrainerPassword(Long trainerId, String newPassword) {
            return trainerService.updatePassword(trainerId, newPassword).get();
    }

    public Trainer changeTrainerActive(Long trainerId, boolean active) {
        return trainerService.updateActive(trainerId, active).get();
    }

    public Trainer updateTrainer(Long trainerId, Trainer trainer) {
            return trainerService.update(trainerId, trainer).get();
    }

    public List<Training> getTrainingsByTrainerUsernameAndCriteria(String usernameToFind, Date dateFrom, Date dateTo, TrainingType trainingType) {
            return trainingService.findByTrainerAndCriteria(usernameToFind, dateFrom, dateTo, trainingType).stream().flatMap(Optional::stream).toList();
    }

    public Training createTraining(String trainingName, Date trainingDate, BigDecimal trainingDuration, Trainer trainer, Trainee trainee, TrainingType trainingType) {
        return trainingService.save(trainingService.createTraining(trainingName, trainingDate, trainingDuration, trainer, trainee, trainingType)).get();
    }
}
