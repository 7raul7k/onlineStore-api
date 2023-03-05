package ro.myclass.onlineStoreapi.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Table(name = "orders")
@Entity(name = "Order")
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

    @Column(name = "order_date",
    nullable = false,
    columnDefinition = "DATE")
    private LocalDate orderDate;

    @Override
    public String toString(){
        return id+","       +","+orderDate;
    }


    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JsonManagedReference
    @Builder.Default
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @ManyToOne
    @JoinColumn(
            name = "customer_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "customer_id_fk"))

    @JsonBackReference(value = "test1")
    private Customer customer;

    @Override
    public boolean equals(Object obj){
        return  this.getId().equals(((Order) obj).getId());
    }


   public void addOrderDetails(OrderDetail orderDetail){

       orderDetails.add(orderDetail);

       orderDetail.setOrder(this);

   }

   public void eraseOrderDetails(OrderDetail orderDetail){
      orderDetails.remove(orderDetail);
   }




}
