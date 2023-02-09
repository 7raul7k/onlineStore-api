package ro.myclass.onlineStoreapi.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.myclass.onlineStoreapi.models.OrderDetail;

import java.util.List;

@Repository
public interface OrderDetailRepo extends JpaRepository<OrderDetail,Long> {

    @Query("SELECT o FROM OrderDetail  o")
    List<OrderDetail> getAllOrderDetails();

}
