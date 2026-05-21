package com.spms;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * StorageManager Class
 * Purpose: Handles saving and loading Storable objects to/from local text files.
 * OOP Concept: Single Responsibility Principle, Encapsulation, Polymorphism.
 */
public class StorageManager {
    private static final String DATA_DIR = "StudentData/Database/";

    public StorageManager() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Saves a list of Storable items to a specific file.
     * Demonstrates Polymorphism since it can take a list of ANY class that implements Storable.
     */
    public <T extends Storable> void saveItems(String filename, ArrayList<T> items) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_DIR + filename))) {
            for (T item : items) {
                writer.println(item.toStorageString());
            }
        } catch (IOException e) {
            System.err.println("Error saving to " + filename + ": " + e.getMessage());
        }
    }

    /**
     * Loads items from a file, creating instances using the provided factory interface.
     */
    public <T extends Storable> ArrayList<T> loadItems(String filename, StorableFactory<T> factory) {
        ArrayList<T> items = new ArrayList<>();
        File file = new File(DATA_DIR + filename);
        
        if (!file.exists()) {
            return items; // Return empty list if file doesn't exist yet
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.trim().isEmpty()) {
                    T item = factory.create();
                    item.fromStorageString(line);
                    items.add(item);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error loading from " + filename + ": " + e.getMessage());
        }
        
        return items;
    }

    /**
     * Simple functional interface to act as a factory for creating Storable instances.
     */
    public interface StorableFactory<T extends Storable> {
        T create();
    }
}
