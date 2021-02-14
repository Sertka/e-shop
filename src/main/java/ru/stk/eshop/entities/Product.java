package ru.stk.eshop.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_id")
    private Long id;

    @Column(name = "p_name")
    private String name;

    @Column(name = "p_description")
    private String description;

    @Column(name = "p_price")
    private BigDecimal price;

    @Column(name = "p_stock")
    private int stock;

    @Column(name = "p_size")
    private String size;

    @Column(name = "p_weight")
    private int weight;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private ProductType type;

    @Transient
    private String printStock;

    @Transient
    private String printPrice;
}
