package pl.coderslab;


import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;


public class Main {

    private static final String EXIT_COMMOND = "exit";
    private static final String ADD_CONTROLLER = "add";
    private static final String REMOVE_CONTROLLER = "remove";
    private static final String LIST_CONTROLLER = "list";

    protected static final String TASKS_PATH = "src/tasks.csv";

    static String[][] tasks;


    public static void main(String[] args) {

        tasks = taskToTable(TASKS_PATH);
        showOptions();
        Scanner scan = new Scanner(System.in);

        while (scan.hasNextLine()) {

                String input = scan.nextLine();

                switch (input) {
                    case EXIT_COMMOND:
                        safeTableToFile(tasks, TASKS_PATH);
                        System.out.println("Do zobaczenia!");
                        System.exit(0);
                        break;
                    case ADD_CONTROLLER:
                        addTaskToTable();
                        System.out.println(Arrays.toString(tasks));
                        break;
                    case REMOVE_CONTROLLER:
                        removeTask(tasks, getTheNumber());
                        break;
                    case LIST_CONTROLLER:
                        System.out.println(readTasks(tasks));
                        break;
                    default:
                        System.out.println("Wybierz jedną z wyświelonych opcji: ");
                }

                showOptions();

            }

    }

    static void showOptions() {

        System.out.println(ADD_CONTROLLER);
        System.out.println(REMOVE_CONTROLLER);
        System.out.println(LIST_CONTROLLER);
        System.out.println(EXIT_COMMOND);
    };

    public static String[][] taskToTable(String TASKS_PATH) {

        File file = new File(TASKS_PATH);
        String[][] taskTable = new String[0][];
        int index = 0;

        try (Scanner scanner = new Scanner(file)){

            while (scanner.hasNext()) {
                taskTable = Arrays.copyOf(taskTable, taskTable.length+1);
                taskTable[index] = scanner.nextLine().split(", ");
                index++;
            }

        } catch (FileNotFoundException e) {
            System.out.println("Nie znaleziono pliku: " + file);
        }

        return taskTable;
    };

    public static StringBuilder readTasks(String[][] tasks){

        StringBuilder builder = new StringBuilder();
        int id = 1;
        for (String[] strings : tasks) {
            builder.append(id).append(": ");
            for (int i = 0; i < strings.length; i++) {
                builder.append(strings[i]).append(", ");

            }
            builder.append("\n");
            id++;

        }
        return builder;

    }

    public static void addTaskToTable() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Podaj opis zadania : ");
        String desc = scanner.nextLine();

        System.out.println("Podaj datę wykoniania w formacie 'y:m:d' : ");
        String dateD = scanner.nextLine();

        System.out.println("Czy task jest priorytetowy t/n : ");
        String tempT = scanner.nextLine();
        String priorityTask = "false";

        if (tempT.equals("t")) {
            priorityTask = "true";
        }

        tasks = Arrays.copyOf(tasks, tasks.length +1);
        tasks[tasks.length - 1] = new String[3];
        tasks[tasks.length - 1][0] = desc;
        tasks[tasks.length - 1][1] = dateD;
        tasks[tasks.length - 1][2] = priorityTask;

    }

    public static int getTheNumber() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select number to remove.");

        String n = scanner.nextLine();
        while (Integer.parseInt(n) == 0) {
            System.out.println("Incorrect argument passed. Please give number greater or equal 0");
            scanner.nextLine();
        }
        return Integer.parseInt(n);
    }

    public static void removeTask(String[][] tab, int idToDelete) {

        try {
            if (idToDelete > 0 && idToDelete < tasks.length) {
                tasks = ArrayUtils.remove(tasks, idToDelete-1);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Element nie istnieje w tablicy");
        }
    }

    public static void safeTableToFile(String[][]tasks, String TASKS_PATH) {

        Path filePath = Paths.get(TASKS_PATH);
        String[] strTemp = new String[tasks.length];

        for (int i = 0; i < tasks.length; i++) {
            strTemp[i] = String.join(",", tasks[i]);
        }

        try {
            Files.write(filePath, Arrays.asList(strTemp));
        } catch (IOException e) {
            e.getMessage();
        }

    }


}