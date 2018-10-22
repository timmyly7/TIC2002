package DTOs.expenditure;

import DTOs.Constant;

import java.util.Date;

public class ExpenditureDTO {
    private Integer id;
    private Date recordDate;
    private double amount;
    private String description;
    // 0 - expense, 1 - income
    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        String expenseType;
        if (type == Constant.INCOME_TYPE){
            expenseType = "INCOME";
        }else {
            expenseType = "EXPENSE";
        }
        return "This is expenditure record for "+expenseType +
                "--> id=" + id +
                ", recordDate=" + recordDate +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", type=" + type +
                '}';
    }
}
