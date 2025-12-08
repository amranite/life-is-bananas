package com.lifeisbananas.model.instruction;

import com.lifeisbananas.model.bioengineer.BioEngineer;
import com.lifeisbananas.model.formula.Formula;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a detailed technical instruction document for the freeze-drying process.
 * This document is generated for external production facilities and can be exported to PDF.
 * Contains step-by-step instructions and technical parameters for the freeze-drying process.
 */
public class FreezeDryInstruction {

    // The id of the freeze drying instruction
    private String instructionID;

    // Name of the formula this instruction is based on
    private String formulaName;

    // Version of the instruction (e.g., "1.0", "2.1")
    private String version;

    // Date when this instruction was created
    private LocalDate creationDate;

    // Detailed step-by-step textual description of the freeze-drying process
    private String steps;

    // Additional remarks, notes, or warnings for the production facility
    private String remarks;

    /**
     * The formula that this instruction is based on.
     * Many-to-one relationship: many instructions can be based on one formula.
     */
    private Formula formula;

    /**
     * The bio-engineer who generated this instruction.
     * Many-to-one relationship: one engineer can generate many instructions.
     */
    private BioEngineer bioEngineer;

    /**
     * Technical parameters for the freeze-drying process.
     * One-to-many aggregation: an instruction can contain multiple parameters.
     */
    private List<Parameter> parameters = new ArrayList<>();

    // --- Constructors ---

    // Default constructor for JPA
    public FreezeDryInstruction() {
    }

    // Constructor with essential fields
    public FreezeDryInstruction(String instructionID, String formulaName, String version,
                                String steps, String remarks) {
        this.instructionID = instructionID;
        this.formulaName = formulaName;
        this.version = version;
        this.steps = steps;
        this.remarks = remarks;
        this.creationDate = LocalDate.now();
    }

    // Full constructor including formula and bio-engineer
    public FreezeDryInstruction(String instructionID, String formulaName, String version,
                                String steps, String remarks, Formula formula,
                                BioEngineer bioEngineer) {
        this.instructionID = instructionID;
        this.formulaName = formulaName;
        this.version = version;
        this.steps = steps;
        this.remarks = remarks;
        this.formula = formula;
        this.bioEngineer = bioEngineer;
        this.creationDate = LocalDate.now();
    }

    // --- Business Methods ---

    /**
     * Adds a parameter to this instruction
     * @param parameter the parameter to add
     */
    public void addParameter(Parameter parameter) {
        parameter.setFreezeDryInstruction(this);
        this.parameters.add(parameter);
    }

    /**
     * Removes a parameter from this instruction
     * @param parameter the parameter to remove
     */
    public void removeParameter(Parameter parameter) {
        parameter.setFreezeDryInstruction(null);
        this.parameters.remove(parameter);
    }

    /**
     * Exports this instruction to PDF format.
     * This is a placeholder method that would integrate with a PDF generation library.
     * @return a string representing the PDF file path or content (placeholder)
     */
    public String exportToPDF() {
        // TODO: Implement PDF export functionality using a library like iText or Apache PDFBox
        StringBuilder pdfContent = new StringBuilder();
        pdfContent.append("=== FREEZE DRY INSTRUCTION ===\n");
        pdfContent.append("Instruction ID: ").append(this.instructionID).append("\n");
        pdfContent.append("Formula: ").append(this.formulaName).append(" (v").append(this.version).append(")\n");
        pdfContent.append("Creation Date: ").append(this.creationDate).append("\n\n");
        pdfContent.append("STEPS:\n").append(this.steps).append("\n\n");

        if (!this.parameters.isEmpty()) {
            pdfContent.append("PARAMETERS:\n");
            for (Parameter param : this.parameters) {
                pdfContent.append("- ").append(param.getName()).append(": ")
                        .append(param.getValue()).append(" ").append(param.getUnit());
                if (param.getCritical()) {
                    pdfContent.append(" [CRITICAL]");
                }
                pdfContent.append("\n");
            }
        }

        if (this.remarks != null && !this.remarks.isEmpty()) {
            pdfContent.append("\nREMARKS:\n").append(this.remarks).append("\n");
        }

        return pdfContent.toString();
    }

    /**
     * Gets all critical parameters from this instruction
     * @return list of critical parameters
     */
    public List<Parameter> getCriticalParameters() {
        return this.parameters.stream()
                .filter(Parameter::getCritical)
                .toList();
    }

    // --- Getters and Setters ---

    public String getInstructionID() {
        return instructionID;
    }

    public void setInstructionID(String instructionID) {
        this.instructionID = instructionID;
    }

    public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Formula getFormula() {
        return formula;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }

    public BioEngineer getBioEngineer() {
        return bioEngineer;
    }

    public void setBioEngineer(BioEngineer bioEngineer) {
        this.bioEngineer = bioEngineer;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}
