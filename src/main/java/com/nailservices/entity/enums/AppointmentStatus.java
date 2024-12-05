package com.nailservices.entity.enums;

public enum AppointmentStatus {
    PENDING,      // Initial state when appointment is created
    CONFIRMED,    // Provider has confirmed the appointment
    CANCELLED,    // Either party has cancelled the appointment
    COMPLETED,    // Service has been provided
    NO_SHOW       // Customer didn't show up
}
