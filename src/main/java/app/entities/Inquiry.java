package app.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public class Inquiry {
    private int inquiryId;
    private Customer customer;
    private Carport carport;
    private String remark;
}
