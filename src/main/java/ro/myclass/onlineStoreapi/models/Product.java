package ro.myclass.onlineStoreapi.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Table(name = "products")
@Entity(name = "Product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @SequenceGenerator(name = "products_sequence",
    sequenceName = "products_sequence",
    allocationSize = 1)
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "products_sequence"
    )
    @Column(
            name = "id"
    )
    private Long id;

    @Column(name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @NotEmpty
    private String name;

    @Column(name = "price",
    nullable = false,
    columnDefinition = "DOUBLE")
    private double price;

    @Column(name = "image",
    nullable = false,
    columnDefinition = "TEXT")
    @NotEmpty

    private String image;

    @Column(name = "stock",
    nullable = false,
    columnDefinition = "INT")
    private int stock;

    @Override
    public String toString(){
        return id+","+name+","+price+","+image+","+stock;
    }

    @Override
    public boolean equals(Object obj){
        Product m = (Product) obj;
        if(this.name.equals(m.getName())&&this.price==m.price&&this.image.equals(m.getImage())&&this.stock==m.getStock()){
            return true;
        }
        return false;
    }

    @OneToMany(
            mappedBy = "orderdetails",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JsonManagedReference
    private List<OrderDetail> orderDetails = new ArrayList<>();


    


}
