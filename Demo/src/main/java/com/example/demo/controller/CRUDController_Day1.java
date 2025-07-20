package com.example.demo.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo/v1") // Base path for all endpoints in this controller

// TODO: Day1:Practical 1
// Create a new Spring Boot Project as per guide
// Create a new RestController with class name of CRUDController_Day1Practical.java
// Add DOGET, DOPOST, DODELETE and DOPUT function as below 
// Ensure it's working with Postman
// Upload your code your Git Hub

//TODO: Day1:Practical 2
// Loose coupling design - Related slide (Microservices)

// Discuss in a group to identify 2 items below are duplicated and how to improve
// Brainstorm it together and exchange idea
// Create a new class to decouple the logic below

public class CRUDController_Day1 {

    private final Map<Long, String> dataStore = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong();
    private record ItemRequest(String id, String value) {}
    
    // --- CREATE (HTTP POST) ---
    @PostMapping
    public ResponseEntity<String> createItem(@RequestBody String newItemName) {
        if (newItemName == null || newItemName.isBlank()) {
            return new ResponseEntity<>("Item name cannot be empty or blank.", HttpStatus.BAD_REQUEST); // 400 Bad Request if name is empty
        }
        long newId = idCounter.incrementAndGet();
        dataStore.put(newId, newItemName);
        // Returning the ID and the data for confirmation
        return new ResponseEntity<>("Item created successfully with ID: " + newId + " and data: " + newItemName, HttpStatus.CREATED); // 201 Created
    }
    
    // --- CREATE (HTTP POST) ---
    
    @PostMapping("/object")
    public ResponseEntity<String> createItemWithID(@RequestBody ItemRequest itemRequest) {

        if (itemRequest == null || itemRequest.id() == null || itemRequest.id().isBlank()) {
            return new ResponseEntity<>("Item name (value associated with '1') cannot be empty or blank.", HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
                
        dataStore.put(Long.valueOf(itemRequest.id), itemRequest.value());
        // Returning the ID and the data for confirmation
        return new ResponseEntity<>("Item created successfully with ID: " + itemRequest.id + " and data: " + itemRequest.value  , HttpStatus.CREATED); // 201 Created
    }

    // --- READ (HTTP GET) ---
    @GetMapping
    public ResponseEntity<Map<Long, String>> getAllItems() {

        return new ResponseEntity<>(dataStore, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getItemById(@PathVariable Long id) {
        String item = dataStore.get(id);
        if (item != null) {
            return new ResponseEntity<>("Found item with ID: " + id + " and data: " + item, HttpStatus.OK); // 200 OK if found
        }
        return new ResponseEntity<>("Item with ID: " + id + " not found.", HttpStatus.NOT_FOUND); // 404 Not Found if not found
    }


    // --- UPDATE (HTTP PUT) ---
    @PutMapping("/{id}")
    public ResponseEntity<String> updateItem(@PathVariable Long id, @RequestBody String updatedName) {
        if (updatedName == null || updatedName.isBlank()) {
            return new ResponseEntity<>("Updated item name cannot be empty or blank.", HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
        if (dataStore.containsKey(id)) {
            dataStore.put(id, updatedName); // Replace existing item
            return new ResponseEntity<>("Item with ID: " + id + " updated successfully to: " + updatedName, HttpStatus.OK); // 200 OK if updated
        }
        return new ResponseEntity<>("Item with ID: " + id + " not found for update.", HttpStatus.NOT_FOUND); // 404 Not Found if not found
    }

    // --- DELETE (HTTP DELETE) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {
        String removedItem = dataStore.remove(id); // Returns the removed value or null if not found
        if (removedItem != null) {
            return new ResponseEntity<>("Item with ID: " + id + " and data: '" + removedItem + "' deleted successfully.", HttpStatus.NO_CONTENT); // 204 No Content for successful deletion
        }
        return new ResponseEntity<>("Item with ID: " + id + " not found for deletion.", HttpStatus.NOT_FOUND); // 404 Not Found if item didn't exist
    }
}
