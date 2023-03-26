package ro.myclass.onlineStoreapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ro.myclass.onlineStoreapi.exceptions.OrderNotFoundException;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Table(name = "customers")
@Entity(name = "Customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Customer {
    @Id
    @SequenceGenerator(name = "customer_sequence",
    sequenceName = "customer_sequence",
    allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE,
    generator = "customer_sequence")
    @Column(name = "id")
    private Long id;
    @Column(name = "email",
    nullable = false,
    columnDefinition = "TEXT")
    @NotEmpty
    private String email;
    @Column(name = "password",
    nullable = false,
    columnDefinition = "TEXT")
    @NotEmpty
    private String password;
    @Column(name = "full_name",
    nullable = false,
    columnDefinition = "TEXT")
    @NotEmpty
    private String fullName;

    @Override
    public String toString(){
        return id+","+email+","+password+","+fullName;
    }

    @Override
    public boolean equals(Object obj){
        Customer m = (Customer) obj;
        if(this.email.equals(m.getEmail())&&this.password.equals(m.getPassword())&&this.fullName.equals(m.getFullName())){
            return true;
        }
        return false;
    }

    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JsonManagedReference(value = "test10")
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    public Customer(String email, String password, String fullName) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

    public void addOrder(Order order){
        order.setCustomer(this);
        orders.add(order);

    }

    public void eraseOrder(Order order){
       orders.remove(order);
    }

    public Order getOrder(int orderId){

        Order order=Order.builder().id((long) orderId).customer(this).orderDate(LocalDate.now()).build();

        if(  this.orders.contains(order)){

            return  this.orders.get(this.orders.indexOf(order));
        }

        return  null;

    }



}
