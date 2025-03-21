package com.nails.api.entity.enums;

public enum TimeSlotStatus {
    AVAILABLE,    // Slot is available for booking
    BOOKED,      // Slot has been booked
    BLOCKED,     // Slot has been manually blocked by provider
    PAST         // Slot is in the past
}
