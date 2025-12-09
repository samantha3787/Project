package project.ui;

import project.processor.MenuOptionOne;
import project.processor.MenuOptionTwo;
import project.processor.MenuOptionThree;
import project.processor.MenuOptionFour;
import project.processor.MenuOptionFive;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Collections;

public class DisplayUI {

    private final MenuOptionOne optionOne;
    private final MenuOptionTwo optionTwo;
    private final MenuOptionThree optionThree;
    private final MenuOptionFour optionFour;
    private final MenuOptionFive optionFive;

    private final Scanner scanner;

    public DisplayUI(MenuOptionOne optionOne, MenuOptionTwo optionTwo, MenuOptionThree optionThree,
                     MenuOptionFour optionFour, MenuOptionFive optionFive) {

        if (optionOne == null || optionTwo == null || optionThree == null || optionFour == null || optionFive == null) {
            throw new IllegalArgumentException("Processor objects should not be null.");
        }

        this.optionOne = optionOne;
        this.optionTwo = optionTwo;
        this.optionThree = optionThree;
        this.optionFour = optionFour;
        this.optionFive = optionFive;
        this.scanner = new Scanner(System.in);
    }

    public void begin() {
        int selection;
        do {
            showMenu();
            selection = userInput("Choose your menu option: ", 0, 5);

            switch(selection){
                case 1:
                    doOptionOne();
                    break;
                case 2:
                    doOptionTwo();
                    break;
                case 3:
                    doOptionThree();
                    break;
                case 4:
                    doOptionFour();
                    break;
                case 5:
                    doOptionFive();
                    break;
                case 0:
                    exitProgram();
                    break;
                default:
                    System.out.println("Error in choosing option.");
            }
        } while(selection != 0);
    }

    private void showMenu() {
        System.out.println("Main Menu:");
        System.out.println("1: Total population for all zipcodes");
        System.out.println("2: Parking fines per capita for each zipcode");
        System.out.println("3: Average market value for residences for specified zipcode");
        System.out.println("4: Average total livable area for residences for specified zipcode");
        System.out.println("5: Residential market value per capita for specified zipcode");
        System.out.println("0: Exit the program");
    }

    private int userInput(String question, int minValue, int maxValue) {
        while(true) {
            System.out.println(question);
            String line = scanner.nextLine();
            if(line == null) {
                System.out.println("Input was null. Please choose again.");
                continue;
            }
            line = line.trim();
            if(line.isEmpty()) {
                System.out.println("Please choose again.");
                continue;
            }

            try {
                int num = Integer.parseInt(line);
                if(num < minValue || num > maxValue) {
                    System.out.println("Choose a value between " + minValue + " and " + maxValue + ".");
                    continue;
                }
                return num;
            } catch (NumberFormatException e){
                System.out.println("This is not an integer. Please choose again.");
            }
        }
    }

    private String readZip(String question) {
        while(true) {
            System.out.println(question);
            String zip = scanner.nextLine();

            if(zip == null) {
                System.out.println("Zipcode cannot be null. Please try again.");
                continue;
            }

            zip = zip.trim();
            if(zip.isEmpty()) {
                System.out.println("Zipcode cannot be empty. Please try again.");
                continue;
            }

            if(!(zip.length() == 5)) {
                System.out.println("Zipcode must be 5 digits. Please try again.");
                continue;
            }

            return zip;
        }
    }

    private void doOptionOne(){
        long result = optionOne.findTotalPopulation();
        System.out.println("Total Population: " + result);
    }

    private void doOptionTwo() {
        Map<String, Double> map = optionTwo.findFinesPerCapita();

        if(map.isEmpty()) {
            System.out.println("Cannot obtain fines per capita data.");
            return;
        }

        List<String> zipcodes = new ArrayList<>(map.keySet());
        Collections.sort(zipcodes);

        for(String zip : zipcodes) {
            double num = map.get(zip);
            String result = String.format("%.4f", num);
            System.out.println(zip + " " + result);
        }

    }

    private void doOptionThree() {
        String zip =  readZip("Please enter the zipcode: ");
        int result = optionThree.findAverageResidentalMarketValue(zip);
        if(result == 0) {
            System.out.println("No residential market value data provided for the zipcode " + zip + ".");
        } else {
            System.out.println("Average residential market value: " + result);
        }
    }

    private void doOptionFour() {
        String zip =  readZip("Please enter the zipcode: ");
        int result = optionFour.findAverageResidentialTotalLivableArea(zip);
        if(result == 0) {
            System.out.println("No residential total livable area data provided for the zipcode " + zip + ".");
        } else {
            System.out.println("Average residential total livable area: " + result);
        }
    }

    private void doOptionFive() {
        String zip =  readZip("Please enter the zipcode: ");
        int result = optionFive.findResidentialMarketValuePerCapita(zip);
        if(result == 0) {
            System.out.println("No residential market value data provided for the zipcode " + zip + ".");
        } else {
            System.out.println("Residential market value per capita: " + result);
        }
    }

    private void exitProgram() {
        System.out.println("This is the end of the program.");
    }

}
