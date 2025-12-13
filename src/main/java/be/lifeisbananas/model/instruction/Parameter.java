package be.lifeisbananas.model.instruction;

/**
 * Represents a specific process parameter for the freeze-drying process.
 * Parameters define technical values like temperature, pressure, duration, etc.
 * that must be followed during the freeze-drying process.
 */

@SuppressWarnings("unused") // This annotation is to get rid of the "is never used" warnings
public class Parameter {

    // The id of the parameter
    private int parameterID;

    // Name/description of the parameter (e.g., "Temperature Phase 1", "Vacuum Pressure", "Drying Duration")
    private String name;

    // The value of this parameter (can be numeric or text)
    private String value;

    // Unit of measurement for this parameter (e.g., "°C", "mbar", "hours", "%")
    private String unit;

    // Minimum acceptable limit for this parameter
    private Double minimumLimit;

    // Maximum acceptable limit for this parameter
    private Double maximumLimit;

    /**
     * Indicates whether this parameter is critical for the process.
     * Critical parameters must be strictly followed.
     */
    private Boolean critical;

    /**
     * The freeze-dry instruction this parameter belongs to.
     * Many-to-one relationship: many parameters belong to one instruction.
     */
    private FreezeDryInstruction freezeDryInstruction;

    // -- Constructors ---

    // Default constructor for JPA
    public Parameter() {
    }

    // Constructor with essential fields
    public Parameter(int parameterID, String name, String value, String unit, Boolean critical) {
        this.parameterID = parameterID;
        this.name = name;
        this.value = value;
        this.unit = unit;
        this.critical = critical;
    }

    // Full constructor including limits
    public Parameter(int parameterID, String name, String value, String unit,
                     Double minimumLimit, Double maximumLimit, Boolean critical) {
        this.parameterID = parameterID;
        this.name = name;
        this.value = value;
        this.unit = unit;
        this.minimumLimit = minimumLimit;
        this.maximumLimit = maximumLimit;
        this.critical = critical;
    }

    // Full constructor including instruction
    public Parameter(int parameterID, String name, String value, String unit,
                     Double minimumLimit, Double maximumLimit, Boolean critical,
                     FreezeDryInstruction freezeDryInstruction) {
        this.parameterID = parameterID;
        this.name = name;
        this.value = value;
        this.unit = unit;
        this.minimumLimit = minimumLimit;
        this.maximumLimit = maximumLimit;
        this.critical = critical;
        this.freezeDryInstruction = freezeDryInstruction;
    }

    // --- Business Methods ---

    /**
     * Checks if a given numeric value is within the acceptable range
     * @param testValue the value to test
     * @return true if the value is within range, false otherwise
     */
    public boolean isWithinRange(Double testValue) {
        if (testValue == null) {
            return false;
        }

        boolean aboveMinimum = (this.minimumLimit == null) || (testValue >= this.minimumLimit);
        boolean belowMaximum = (this.maximumLimit == null) || (testValue <= this.maximumLimit);

        return aboveMinimum && belowMaximum;
    }

    /**
     * Gets a formatted string representation of this parameter
     * @return formatted parameter string
     */
    public String getFormattedParameter() {
        StringBuilder formatted = new StringBuilder();
        formatted.append(this.name).append(": ").append(this.value);

        if (this.unit != null && !this.unit.isEmpty()) {
            formatted.append(" ").append(this.unit);
        }

        if (this.minimumLimit != null || this.maximumLimit != null) {
            formatted.append(" [Range: ");
            if (this.minimumLimit != null) {
                formatted.append(this.minimumLimit);
            } else {
                formatted.append("∞");
            }
            formatted.append(" - ");
            if (this.maximumLimit != null) {
                formatted.append(this.maximumLimit);
            } else {
                formatted.append("∞");
            }
            formatted.append("]");
        }

        if (this.critical) {
            formatted.append(" [CRITICAL]");
        }

        return formatted.toString();
    }

    // --- Getters and Setters ---

    public int getParameterID() {
        return parameterID;
    }

    public void setParameterID(int parameterID) {
        this.parameterID = parameterID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getMinimumLimit() {
        return minimumLimit;
    }

    public void setMinimumLimit(Double minimumLimit) {
        this.minimumLimit = minimumLimit;
    }

    public Double getMaximumLimit() {
        return maximumLimit;
    }

    public void setMaximumLimit(Double maximumLimit) {
        this.maximumLimit = maximumLimit;
    }

    public Boolean getCritical() {
        return critical;
    }

    public void setCritical(Boolean critical) {
        this.critical = critical;
    }

    public FreezeDryInstruction getFreezeDryInstruction() {
        return freezeDryInstruction;
    }

    public void setFreezeDryInstruction(FreezeDryInstruction freezeDryInstruction) {
        this.freezeDryInstruction = freezeDryInstruction;
    }
}
