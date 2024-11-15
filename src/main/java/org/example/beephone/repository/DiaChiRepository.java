package org.example.beephone.repository;

import org.example.beephone.entity.dia_chi_khach_hang;
import org.example.beephone.entity.khach_hang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DiaChiRepository extends JpaRepository<dia_chi_khach_hang, Integer> {

    // Phương thức để xóa tất cả các địa chỉ của một khách hàng
    @Modifying
    @Transactional
    void deleteByKhachHang(khach_hang khachHang);

    // Phương thức để đặt tất cả địa chỉ của khách hàng về trạng thái không mặc định (trang_thai = 0)
    @Modifying
    @Transactional
    @Query("UPDATE dia_chi_khach_hang dc SET dc.trang_thai = 0 WHERE dc.khachHang.id = :customerId")
    void updateAllAddressesToNonDefault(@Param("customerId") Integer customerId);

    // Phương thức để đặt trạng thái của một địa chỉ là mặc định (trang_thai = 1)
    @Modifying
    @Transactional
    @Query("UPDATE dia_chi_khach_hang dc SET dc.trang_thai = 1 WHERE dc.id = :addressId")
    void setAddressAsDefault(@Param("addressId") Integer addressId);
}