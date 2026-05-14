package app.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class Inquiry {
    private int inquiryId;
    private int customerId;
    private String remark;
    private int carportId;


    public Inquiry(int customerId, String remark, int carportId) {
        this.customerId = customerId;
        this.remark = remark;
        this.carportId = carportId;
    }
}





