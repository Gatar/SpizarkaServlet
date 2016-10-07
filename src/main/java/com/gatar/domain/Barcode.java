package com.gatar.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

public class Barcode {

    @Id
    @Column
    @NotNull
    @ManyToOne
    private String barcode;


    @Column
    @NotNull
    private String title;
}
