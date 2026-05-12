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
}
