package app.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode

public class Inquiry {
    private int inquiryId;
    private int customerId;
    private String remark;
    private int carportId;
    private boolean quoteSend;

    public Inquiry(int customerId, String remark, int carportId) {
        this.customerId = customerId;
        this.remark = remark;
        this.carportId = carportId;
    }
}





