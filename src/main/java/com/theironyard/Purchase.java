package com.theironyard;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by jessicahuffstutler on 11/11/15.
 */
@Entity
public class Purchase {
    @Id
    @GeneratedValue
    Integer id;

    String date; //could have used LocalDateTime instead but isnt necessary
    String creditCard;
    Integer cvv; //MUST be Integer NOT int
    String category;

    //entire user object inside of the Purchase class. so we do NOT need to store customer_id field.
    @ManyToOne
    Customer customer;
}
