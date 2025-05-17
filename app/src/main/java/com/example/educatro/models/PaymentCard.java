package com.example.educatro.models;

import java.util.HashMap;
import java.util.Map;

public class PaymentCard {
    private String cardId;
    private String cardName;
    private String cardNumber;
    private String cardHolder;
    private String expiryDate;
    private boolean isDefault;

    // Empty constructor needed for Firebase
    public PaymentCard() {
    }

    public PaymentCard(String cardId, String cardName, String cardNumber, String cardHolder, String expiryDate, boolean isDefault) {
        this.cardId = cardId;
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.expiryDate = expiryDate;
        this.isDefault = isDefault;
    }

    // Convert PaymentCard object to a Map for Firebase
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cardId", cardId);
        result.put("cardName", cardName);
        result.put("cardNumber", cardNumber);
        result.put("cardHolder", cardHolder);
        result.put("expiryDate", expiryDate);
        result.put("default", isDefault);
        return result;
    }

    // Helper method to mask card number for display
    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "**** ****";
        }
        
        if (cardNumber.length() <= 8) {
            return "**** " + cardNumber.substring(cardNumber.length() - 4);
        }
        
        // For full card numbers, show first 4 and last 4 digits
        return cardNumber.substring(0, 4) + " **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }

    // Getters and setters
    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
} 