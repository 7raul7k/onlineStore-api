package ro.myclass.onlineStoreapi.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.myclass.onlineStoreapi.models.OrderDetail;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepo extends JpaRepository<OrderDetail,Long> {

    @Query("SELECT o FROM OrderDetail  o")
    List<OrderDetail> getAllOrderDetails();

    @Query("select o from OrderDetail o where o.product.id = ?1 and o.order.id = ?2")
    Optional<OrderDetail> findOrderDetailByProductIdAndOrderId(long productId, long orderID);



}
