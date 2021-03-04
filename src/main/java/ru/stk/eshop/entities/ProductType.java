package ru.stk.eshop.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * ProductType helps to arrange products by different types and
 * simplify searching of the product
 */
@Entity
@Table(name = "product_types")
@Getter
@Setter
@NoArgsConstructor
public class ProductType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pt_id")
    private Long id;

    @Column(name = "pt_name")
    private String name;
}