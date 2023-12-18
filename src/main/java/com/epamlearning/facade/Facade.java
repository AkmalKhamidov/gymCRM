package com.epamlearning.facade;

import java.util.Scanner;

public interface Facade {

    Scanner sc = new Scanner(System.in);
    void getPage(String response);

    default void cleanScreen(){
        for(int i = 0; i < 100; i++)
        {
            System.out.println("\b");
        }
    }

    default void waitForEnterKey(Scanner scanner) {
        // This loop will keep running until the Enter key is pressed
        while (true) {
            // Check if the next line is empty (Enter key pressed)
            if (scanner.nextLine().isEmpty()) {
                break;
            }
        }
    }
}
