package app.entities;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode

public class SalesRep extends User {


    public SalesRep(int salesRepId, String firstName, String lastName, String email, String password, String phoneNumber) {
        super(salesRepId, firstName, lastName, email, password, phoneNumber);
    }
}
