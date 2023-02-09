package ro.myclass.onlineStoreapi.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Table(name = "orders")
@Entity(name = "order")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Order {
    @Id
    @SequenceGenerator(name = "order_sequence",
    sequenceName = "order_sequence",
    allocationSize = 1)
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "order_sequence"
    )
    @Column(
            name = "id"
    )
    private Long id;

    @Column(name = "ammount",
    nullable = false,
    columnDefinition = "DOUBLE")
    private double ammount;
    @Column(name = "order_date",
    nullable = false,
    columnDefinition = "DATE")
    private Date orderDate;

    @Override
    public String toString(){
        return id+","+ammount+","+orderDate;
    }


    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JsonManagedReference
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @ManyToOne
    @JoinColumn(
            name = "customer",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "customer_id_fk"))
    @JsonBackReference
    private Customer customer;


   public void addOrderDetails(OrderDetail m){
     orderDetails.add(m);
       m.setOrder(this);
   }

   public void eraseOrderDetails(OrderDetail orderDetail){
      orderDetails.remove(orderDetail);
   }

    public Order(double ammount, Date orderDate) {
        this.ammount = ammount;
        this.orderDate = orderDate;
    }
}
