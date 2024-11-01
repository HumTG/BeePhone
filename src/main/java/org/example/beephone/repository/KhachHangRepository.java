package org.example.beephone.repository;

import org.example.beephone.entity.khach_hang;
import org.example.beephone.entity.nhan_vien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface KhachHangRepository extends JpaRepository<khach_hang, Integer> {

    @Query("SELECT nv FROM nhan_vien nv " +
            "WHERE (:tenSdt IS NULL OR nv.ho_ten LIKE %:tenSdt% OR nv.sdt LIKE %:tenSdt%) " +
            "AND (:ngaySinhTu IS NULL OR nv.ngay_sinh >= :ngaySinhTu) " +
            "AND (:ngaySinhDen IS NULL OR nv.ngay_sinh <= :ngaySinhDen) " +
            "AND (:trangThai IS NULL OR nv.trang_thai = :trangThai) " +
            "AND (:maxTuoi IS NULL OR (YEAR(CURRENT_DATE) - YEAR(nv.ngay_sinh)) <= :maxTuoi)" +
            " order by nv.id desc ")
    Page<nhan_vien> searchNhanVien(
            @Param("tenSdt") String tenSdt,
            @Param("ngaySinhTu") Date ngaySinhTu,
            @Param("ngaySinhDen") Date ngaySinhDen,
            @Param("trangThai") Integer trangThai,
            @Param("maxTuoi") Integer maxTuoi,
            Pageable pageable
    );

    @Query("select kh from khach_hang kh order by kh.id asc ")
    Page<khach_hang> getKhachHangDESCID(Pageable pageable);

}
