package com.spms;

/**
 * Interface Storable
 * OOP Concept: Interface
 * Represents an object that can be serialized to and from a local text file.
 */
public interface Storable {
    String toStorageString();
    void fromStorageString(String data);
}
