package com.lifeisbananas.model.enums;

/**
 * Enumeration representing the lifecycle status of a Product.
 * Tracks the development and production phases of freeze-dried snacks.
 */

public enum ProductStatus {
    IN_DEVELOPMENT, // Product is currently being developed and tested
    TESTED, // Product has undergone testing phase
    APPROVED, // Product has been approved for production
    IN_PRODUCTION // Product is currently in active production
}