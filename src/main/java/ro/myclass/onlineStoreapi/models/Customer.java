package ro.myclass.onlineStoreapi.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Table(name = "customers")
@Entity(name = "Customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private String email;
    @Column(name = "password",
    nullable = false,
    columnDefinition = "TEXT")
    private String password;
    @Column(name = "full_name",
    nullable = false,
    columnDefinition = "TEXT")
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
            mappedBy = "orders",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JsonManagedReference
    private List<Order> orders = new ArrayList<>();

}
