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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Component

public class TraineeFacade implements Facade {

    private final TraineeService traineeService;
    private final TrainingTypeService trainingTypeService;
    private final TrainingService trainingService;
    private final TrainerService trainerService;
    private String traineeUsername;
    private Long traineeId;
    private Trainee trainee;
    String response;

    @Autowired
    public TraineeFacade(TraineeService traineeService, TrainingTypeService trainingTypeService, TrainingService trainingService, TrainerService trainerService) {
        this.traineeService = traineeService;
        this.trainingTypeService = trainingTypeService;
        this.trainingService = trainingService;
        this.trainerService = trainerService;
    }

    public Long getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(Long traineeId) {
        this.traineeId = traineeId;
    }

    public String getTraineeUsername() {
        return traineeUsername;
    }

    public void setTraineeUsername(String traineeUsername) {
        this.traineeUsername = traineeUsername;
    }


    @Override
    public void getPage(String response) {
        cleanScreen();
        System.out.println(response + "\n");

        trainee = getTraineeByUsername(getTraineeUsername());
        System.out.println("Trainee Main Page");
        System.out.println("1. Profile");
        System.out.println("2. Trainers");
        System.out.println("3. Trainings");
        System.out.println("4. Exit");

        System.out.print("Input your choice: ");

        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1 -> traineeProfilePage("");
            case 2 -> traineeTrainersPage("");
            case 3 -> traineeTrainingsPage("");
            case 4 -> System.out.println("Thank you for using Gym CRM");
        }
    }

    public void traineeProfilePage(String response) {
        cleanScreen();
        System.out.println(response + "\n");
        try {
            trainee = getTraineeByUsername(getTraineeUsername());
            System.out.println("\nTrainee Profile");
            System.out.println("Username: " + trainee.getUser().getUsername());
            System.out.println("FirstName: " + trainee.getUser().getFirstName());
            System.out.println("LastName: " + trainee.getUser().getLastName());
            System.out.println("Address: " + trainee.getAddress());
            System.out.println("Date of Birth: " + trainee.getDateOfBirth());
        } catch (Exception e) {
            this.response = "An error occurred when fetching trainee details.";
            getPage(this.response);
        }
        System.out.println();
        System.out.println("1. Update Profile");
        System.out.println("2. Change password");
        System.out.println("3. Change status");
        System.out.println("4. Delete Profile");
        System.out.println("5. Back to Trainee Main Page");
        System.out.println("6. Exit");
        System.out.print("Input your choice: ");

        int innerChoice = sc.nextInt();
        sc.nextLine();
        switch (innerChoice) {
            case 1 -> updateTraineePage();
            case 2 -> updateTraineePasswordPage();
            case 3 -> updateTraineeStatusPage();
            case 4 -> deleteTraineePage();
            case 5 -> getPage("");
            case 6 -> System.out.println("Thank you for using Gym CRM");
            default -> System.out.println("Invalid choice.");
        }
    }

    private void deleteTraineePage() {
        System.out.println("Confirm deletion of trainee profile by entering your username: ");
        String usernameToDelete = sc.nextLine();
        if (usernameToDelete.equals(getTraineeUsername())) {
            try {
                deleteTraineeByUsername(usernameToDelete);
                response = "Trainee profile deleted successfully.";
            } catch (Exception e) {
                response = "An error occurred when deleting trainee profile.";
            }
        } else {
            System.out.println("Invalid username. Please try again.");
            deleteTraineePage();
        }
    }

    public void traineeTrainingsPage(String response) {
        System.out.println(response + "\n");

        System.out.println("Trainee's Trainings");
        System.out.println("1. View All Trainee's Trainings");
        System.out.println("2. View Trainee's Trainings by Criteria");
        System.out.println("3. Add training");
        System.out.println("4. Update training");
        System.out.println("5. Delete training");
        System.out.println("6. Back to Trainee Main Page");
        System.out.println("7. Exit");
        System.out.print("Input your choice: ");

        int innerChoice = sc.nextInt();
        sc.nextLine();
        switch (innerChoice) {
            case 1 -> viewAllTraineeTrainings();
            case 2 -> viewTraineeTrainingsByCriteria();
            case 3 -> addOrUpdateTrainingPage(false);
            case 4 -> addOrUpdateTrainingPage(true);
            case 5 -> deleteTrainingPage();
            case 6 -> getPage("");
            case 7 -> System.out.println("Thank you for using Gym CRM");
            default -> System.out.println("Invalid choice.");
        }
    }

    public void deleteTrainingPage() {
        System.out.println("Delete Training");
        Training training = getTrainingFromInput();

        try {
            trainingService.deleteById(training.getId());
            response = "Training deleted successfully.";
        } catch (Exception e) {
            response = "An error occurred when deleting training.";
        }
        traineeTrainingsPage(response);
    }

    public Training getTrainingFromInput(){
        System.out.println("All Trainee's Trainings");
        List<Training> trainingList = null;
        try {
            trainingList = trainingService.findByTrainee(getTraineeId()).stream().flatMap(Optional::stream).toList();
            toStringListTrainings(trainingList);
        } catch (Exception e) {
            this.response = "An error occurred when fetching trainee's trainings.";
            traineeTrainingsPage(this.response);
        }

        Integer trainingIndex;

        do {
            System.out.print("Enter training index from list: ");
            trainingIndex = sc.nextInt();
            sc.nextLine();
            if (trainingIndex < 0 || trainingIndex > trainingList.size()) {
                System.out.println("Invalid training index. Please try again.");
            }
        } while (trainingIndex == null);

        return trainingList.get(trainingIndex);
    }
    public void addOrUpdateTrainingPage(boolean isUpdate) {
        Training training = null;
        if (isUpdate) {
            System.out.println("Update Training");
            training = getTrainingFromInput();
        } else {
            System.out.println("Add Training");
        }
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
            System.out.print("Enter your training type index from list: ");
            trainingTypeIndex = sc.nextInt();
            if (trainingTypeIndex < 0 || trainingTypeIndex > trainingTypeList.size()) {
                System.out.println("Invalid training type index. Please try again.");
            }
        } while (trainingTypeIndex == null);
        TrainingType trainingType = trainingTypeList.get(trainingTypeIndex);

        List<Trainer> trainerList = trainerService.findAll()
                .stream()
                .flatMap(Optional::stream)
                .toList();
        IntStream.range(0, trainerList.size())
                .forEach(index -> {
                    Trainer trainer = trainerList.get(index);
                    System.out.println("Index: " + index + ". FullName: " + trainer.getUser().getFirstName() +
                            " " + trainer.getUser().getLastName() +
                            ". Specialization: " + trainer.getSpecialization().getTrainingTypeName());
                });

        Integer trainerIndex;
        do {
            System.out.print("Enter trainer index from list: ");
            trainerIndex = sc.nextInt();
            if (trainerIndex < 0 || trainerIndex > trainerList.size()) {
                System.out.println("Invalid trainer type index. Please try again.");
            }
        } while (trainerIndex == null);

        Trainer trainer = trainerList.get(trainerIndex);

        if (isUpdate) {
            try {
                updateTraining(training.getId(), trainingName, trainingDate, trainingDuration, trainer, trainee, trainingType);
                response = "Training updated successfully.";
            } catch (Exception e) {
                response = "An error occurred when creating training.";
            }
        } else {
            try {
                createTraining(trainingName, trainingDate, trainingDuration, trainer, trainee, trainingType);
                response = "Training created successfully.";
            } catch (Exception e) {
                response = "An error occurred when creating training.";
            }
        }

        traineeTrainingsPage(response);
    }

    public void viewTraineeTrainingsByCriteria() {
        System.out.println("View Trainee's Trainings by Criteria");


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.print("Enter trainings start date (dd/mm/yyyy): ");
        String startDateStr = sc.next();
        Date startDate = null;
        try {
            startDate = sdf.parse(startDateStr);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please try again.");
            viewTraineeTrainingsByCriteria();
        }

        System.out.print("Enter trainings end date (dd/mm/yyyy): ");
        String endDateStr = sc.next();
        Date endDate = null;
        try {
            endDate = sdf.parse(endDateStr);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please try again.");
            viewTraineeTrainingsByCriteria();
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
            System.out.print("Enter your training type index (-1 -> null): ");
            trainingTypeIndex = sc.nextInt();
            if (trainingTypeIndex < -1 || trainingTypeIndex > trainingTypeList.size()) {
                System.out.println("Invalid training type index. Please try again.");
            }
        } while (trainingTypeIndex == null);
        try {
            List<Training> trainingListByCriteria = getTrainingsByTraineeUsernameAndCriteria(getTraineeUsername(), startDate, endDate, trainingTypeIndex != -1 ? trainingTypeList.get(trainingTypeIndex) : null);
            toStringListTrainings(trainingListByCriteria);
            System.out.println("Press Enter back to Trainee Main Page");

            waitForEnterKey(sc);
            traineeTrainingsPage("");
        } catch (Exception e) {
            response = "An error occurred when fetching trainee's trainings.";
            traineeTrainingsPage(response);
        }

    }

    public void toStringListTrainings(List<Training> trainings) {
        if (trainings.isEmpty()) {
            System.out.println("No trainings found.\n");
        } else {
            trainings.forEach(training -> {
                System.out.println();
                System.out.println("Index: " + trainings.indexOf(training));
                System.out.println("Training type: " + training.getTrainingType().getTrainingTypeName());
                System.out.println("Training Name: " + training.getTrainingName());
                System.out.println("Training Duration: " + training.getTrainingDuration());
                System.out.println("Trainer: " + training.getTrainer().getUser().getFirstName() + " " + training.getTrainer().getUser().getLastName());
                System.out.println("Date: " + training.getTrainingDate());
                System.out.println();
            });
        }
    }

    public void viewAllTraineeTrainings() {
        System.out.println("All Trainee's Trainings");
        try {
            List<Training> trainingList = trainingService.findByTrainee(getTraineeId()).stream().flatMap(Optional::stream).toList();
            toStringListTrainings(trainingList);
            System.out.println("Press Enter back to Trainee Main Page");

            waitForEnterKey(sc);
            traineeTrainingsPage("");

        } catch (Exception e) {
            this.response = "An error occurred when fetching trainee's trainings.";
            traineeTrainingsPage(this.response);
        }
    }

    public void traineeTrainersPage(String response) {
        System.out.println(response + "\n");
        try {
            List<Trainer> trainerList = traineeService.findTrainersByTraineeId(getTraineeId()).stream().flatMap(Optional::stream).toList();

            if (trainerList.isEmpty()) {
                System.out.println("No trainers found.\n");
            } else {
                System.out.println("Trainers");
                trainerList.forEach(trainer -> {
                    System.out.println();
                    System.out.println("Full name: " + trainer.getUser().getFirstName() + " " + trainer.getUser().getLastName());
                    System.out.println("Specialization: " + trainer.getSpecialization().getTrainingTypeName());
                    System.out.println();
                });
            }
        } catch (Exception e) {
            this.response = "An error occurred when fetching trainers.";
            getPage(this.response);
        }

        System.out.println("1. List not assigned active Trainers");
        System.out.println("2. Back to Trainee Main Page");
        System.out.println("34. Exit");
        System.out.print("Input your choice: ");

        int innerChoice = sc.nextInt();
        sc.nextLine();
        switch (innerChoice) {
            case 1 -> viewNotAssignedActiveTrainers();
            case 2 -> getPage("");
            case 3 -> System.out.println("Thank you for using Gym CRM");
            default -> System.out.println("Invalid choice.");
        }
    }

    public void viewNotAssignedActiveTrainers() {
        try {
            List<Trainer> notAssignedActiveTrainers = findNotAssignedActiveTrainers(getTraineeId());
            System.out.println("Not Assigned Active Trainers");

            if (notAssignedActiveTrainers.isEmpty()) {
                System.out.println("No trainers found.\n");
            } else {
                notAssignedActiveTrainers.forEach(trainer -> {
                    System.out.println();
                    System.out.println("Full name: " + trainer.getUser().getFirstName() + " " + trainer.getUser().getLastName());
                    System.out.println("Specialization: " + trainer.getSpecialization().getTrainingTypeName());
                    System.out.println();
                });
            }
        } catch (Exception e) {
            this.response = "An error occurred when fetching trainers.";
            traineeTrainersPage(this.response);
        }
        System.out.println("Press Enter back to Trainee Main Page");

        waitForEnterKey(sc);
        traineeTrainersPage("");
    }

    public void updateTraineePasswordPage() {
        System.out.println("Update Trainee Password");
        System.out.print("Enter your new password: ");
        String newPassword = sc.nextLine();
        try {
            trainee = changeTraineePassword(trainee.getId(), newPassword);
            response = "Trainee password updated successfully.";
        } catch (Exception e) {
            response = "An error occurred when updating trainee password.";
        }
        traineeProfilePage(response);
    }

    public void updateTraineeStatusPage() {
        System.out.println("Update Trainee Status");
        System.out.println("1. Activate");
        System.out.println("2. Deactivate");
        System.out.println("3. Back to Trainee Profile Page");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");

        int innerChoice = sc.nextInt();
        sc.nextLine();
        switch (innerChoice) {
            case 1 -> {
                try {
                    trainee = changeTraineeActive(trainee.getId(), true);
                    response = "Trainee status activated successfully.";
                } catch (Exception e) {
                    response = "An error occurred during trainee status activation.";
                }
                traineeProfilePage(response);
            }
            case 2 -> {
                try {
                    trainee = changeTraineeActive(trainee.getId(), false);
                    response = "Trainee status deactivated successfully.";
                } catch (Exception e) {
                    response = "An error occurred during trainee status deactivation.";
                }
                traineeProfilePage(response);
            }
            case 3 -> traineeProfilePage("");
            case 4 -> System.out.println("Thank you for using Gym CRM");
            default -> System.out.println("Invalid choice.");
        }
    }

    public void updateTraineePage() {
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
                if (sdf.parse(dateStr).after(new Calendar.Builder().setDate(1900, Calendar.JANUARY, 1).build().getTime())
                        && sdf.parse(dateStr).before(new Date())) {
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
            Trainee traineeToUpdate = new Trainee();
            traineeToUpdate.setId(trainee.getId());
            traineeToUpdate.setUser(trainee.getUser());
            traineeToUpdate.getUser().setFirstName(firstName);
            traineeToUpdate.getUser().setLastName(lastName);
            traineeToUpdate.setAddress(address);
            traineeToUpdate.setDateOfBirth(dateOfBirth);
            trainee = updateTrainee(trainee.getId(), traineeToUpdate);
            response = "Trainee profile updated successfully.";
        } catch (Exception e) {
            response = "An error occurred when updating trainee details.";
        }
        traineeProfilePage(response);
    }

    public Trainee changeTraineeActive(Long traineeId, boolean active) {
        return traineeService.updateActive(traineeId, active).get();
    }

    public Trainee changeTraineePassword(Long traineeId, String newPassword) {
        return traineeService.updatePassword(traineeId, newPassword).get();
    }

    public Trainee getTraineeByUsername(String traineeUsername) {
        return traineeService.findByUsername(traineeUsername).get();
    }

    public Trainee updateTrainee(Long traineeId, Trainee trainee) {
        return traineeService.update(traineeId, trainee).get();
    }

    public void deleteTraineeByUsername(String usernameToDelete) {
        traineeService.deleteByUsername(usernameToDelete);
    }

    public List<Training> getTrainingsByTraineeUsernameAndCriteria(String usernameToFind, Date dateFrom, Date dateTo, TrainingType trainingType) {
        return trainingService.findByTraineeAndCriteria(usernameToFind, dateFrom, dateTo, trainingType).stream().flatMap(Optional::stream).toList();
    }

    public List<Trainer> findNotAssignedActiveTrainers(Long traineeId) {
        return trainerService.findNotAssignedActiveTrainers(traineeId).stream().flatMap(Optional::stream).toList();
    }

    public Training createTraining(String trainingName, Date trainingDate, BigDecimal trainingDuration, Trainer trainer, Trainee trainee, TrainingType trainingType) {
        return trainingService.save(trainingService.createTraining(trainingName, trainingDate, trainingDuration, trainer, trainee, trainingType)).get();
    }

    public Training updateTraining(Long trainingId, String trainingName, Date trainingDate, BigDecimal trainingDuration, Trainer trainer, Trainee trainee, TrainingType trainingType) {
        return trainingService.update(trainingId, trainingService.createTraining(trainingName, trainingDate, trainingDuration, trainer, trainee, trainingType)).get();
    }

}
