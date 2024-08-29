package model;

import org.json.JSONObject;

import java.time.LocalDate;

// Represents a single Medication with a name, no. of pills in pack and consume, consumption information and expiry date
public class Medication {

    private static final String noConsumptionInfo = "Consumption information has not been specified!";
    private EventLog eventLog = EventLog.getInstance();

    private final String name;
    private String consumptionInformation;
    private final int originalNumberOfPills;
    private int consumedNumberOfPills;
    private LocalDate expiryDate;


    // EFFECTS: Creates a new Medication with a name, original number of pills (as in how many pills come in the box),
    //          and an expiration date. Sets the consumed # of pills to 0 and consumptionInfo to noConsumptionInfo.
    public Medication(String name, int originalNumberOfPills, LocalDate expiryDate) {
        this.name = name;
        this.consumptionInformation = noConsumptionInfo;
        this.originalNumberOfPills = originalNumberOfPills;
        this.consumedNumberOfPills = 0;
        this.expiryDate = expiryDate;
    }

    // REQUIRES: n > 0
    // MODIFIES: this
    // EFFECTS: increase consumedNumberOfPills by n if n <= getOriginalNumberOfPills() - getOriginalNumberOfPills())
    //          else, consumedNumberOfPills = originalNumberOfPills
    public void consumePills(int n) {
        int max = getOriginalNumberOfPills() - getConsumedNumberOfPills();
        if (n <= max) {
            this.consumedNumberOfPills += n;
        } else {
            this.consumedNumberOfPills = this.originalNumberOfPills;
        }
        Event event = new Event("Consumed " + n + " pills of " + this.getName());
        eventLog.logEvent(event);
    }

    // EFFECTS: returns True if the medication is expired, else False
    public Boolean isExpired() {
        LocalDate today = LocalDate.now();
        return today.isAfter(expiryDate);
    }

    // REQUIRES: originalNumberOfPills > 0
    // EFFECTS: returns True if at least 90% and less than 100% of the medication is consumed, else false
    public Boolean isAlmostFinished() {
        double consumptionRatio = ((this.consumedNumberOfPills * 1.0) / this.originalNumberOfPills);
        return (0.9 <= consumptionRatio) & (consumptionRatio < 1.0);
    }

    // REQUIRES: originalNumberOfPills > 0
    // EFFECTS: returns True if originalNumberOfPills == consumedNumberOfPills
    public Boolean isFinished() {
        return (this.originalNumberOfPills == this.consumedNumberOfPills);
    }


    // getters
    public String getName() {
        return name;
    }

    public String getConsumptionInformation() {
        return consumptionInformation;
    }

    public int getOriginalNumberOfPills() {
        return originalNumberOfPills;
    }

    public int getConsumedNumberOfPills() {
        return consumedNumberOfPills;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    // setters

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setConsumedNumberOfPills(int pills) {
        consumedNumberOfPills = pills;
    }

    public void setConsumptionInformation(String consumptionInformation) {
        this.consumptionInformation = consumptionInformation;
    }

    // EFFECTS: creates a JSON object which contains all the information (all fields) for a Medication
    public JSONObject toJson() {
        JSONObject jsonMed = new JSONObject();

        jsonMed.put("name", name);
        jsonMed.put("consumption information", consumptionInformation);
        jsonMed.put("original pills", originalNumberOfPills);
        jsonMed.put("consumed pills", consumedNumberOfPills);
        jsonMed.put("expiry date", expiryDate.toString());

        return jsonMed;
    }

    @Override
    public String toString() {
        return name;
    }
}