package ro.myclass.onlineStoreapi.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonClassDescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Table(name ="orderDetails")
@Entity(name ="OrderDetail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    @Id
    @SequenceGenerator(name = "orderDetail_sequence",
    sequenceName = "orderDetail_sequence",
    allocationSize = 1 )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "orderDetail_sequence"
    )
    @Column(
            name = "id"
    )
    private Long id;

    @Column(name = "order_id",
    nullable = false,
    columnDefinition = "INT")
    private int orderId;

    @Column(name = "product_id",
    nullable = false,
    columnDefinition = "INT")
    private int productId;

    @Column(name = "price",
    nullable = false,
    columnDefinition = "DOUBLE")
    private double price;

    @Column(name = "quantity",
    nullable = false,
    columnDefinition = "INT")
    private int quantity;

    @Override
    public String toString(){
        return id+","+orderId+","+productId+","+price+","+quantity;
    }

    @Override
    public boolean equals(Object obj){
        OrderDetail m = (OrderDetail) obj;
        if(this.orderId==m.getOrderId()&&this.productId==m.getProductId()&&this.price==m.getPrice()&&this.quantity==m.getQuantity()){
            return true;
        }
        return false;
    }

    @ManyToOne
    @JoinColumn(name = "order_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "order_id_fk"))

    @JsonBackReference
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "order_id_fk"))

    @JsonBackReference
    private Product product;





}
