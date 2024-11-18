package org.example.beephone.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.beephone.entity.*;
import org.example.beephone.repository.HoaDonChiTietRepository;
import org.example.beephone.repository.HoaDonRepository;
import org.example.beephone.repository.KhachHangRepository;
import org.example.beephone.repository.KhuyenMaiRepository;
import org.example.beephone.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class HoaDonService {
    @Autowired
    private HoaDonRepository hdRP;
    @Autowired
    private NhanVienRepository nvRP;
    @Autowired
    private KhachHangRepository khRP;
    @Autowired
    private KhuyenMaiRepository kmRP;
    @Autowired
    private HoaDonChiTietRepository hdctRP;
    @Autowired
    private LichSuHoaDonService lsHoaDonService;


    public List<hoa_don> getAll(){
        return hdRP.findAll();
    }

    public List<hoa_don> getHoaDonBanHang(){
     return hdRP.getHDBanHang();
    }

    /// tạo hóa đơn bán hàng tại quầy mới
    public hoa_don createHoaDon(){
        nhan_vien nhanVien = nvRP.findById(5).get();
        khach_hang khachHang = khRP.findById(1).get();

        hoa_don hd = new hoa_don();
        hd.setMa_hoa_don("HD"+generateRandomCode());
        hd.setNhanVien(nhanVien);
        hd.setKhachHang(khachHang);
        // lấy ngày hiện tại
        LocalDate localDate = LocalDate.now();
        Date sqlDate = Date.valueOf(localDate);
        hd.setNgay_tao(sqlDate);
        hd.setTien_sau_giam_gia(BigDecimal.ZERO);
        hd.setThanh_tien(BigDecimal.ZERO);
        hd.setPhuong_thuc_thanh_toan(1);
        hd.setLoai_hoa_don(1);
        hd.setPhi_ship(BigDecimal.ZERO);
        hd.setTrang_thai(0);

        hdRP.save(hd);
        return hd;
    }

    public String generateRandomCode() {
        // Tạo mã giảm giá ngẫu nhiên (8 ký tự gồm chữ và số)
        int length = 8;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }

        return code.toString();
    }

    public Optional<hoa_don> findHoaDonById(Integer idHD){
        Optional<hoa_don> optionalHoa_don = hdRP.findById(idHD);
        return optionalHoa_don;
    }

    ////Tính tổng tiền hóa đơn
    public void tinhTongTienHoaDon(Integer idHD){
        BigDecimal tienHoaDon = hdctRP.tinhTongTienHoaDon(idHD);
        tienHoaDon = tienHoaDon != null ? tienHoaDon : BigDecimal.ZERO;
        System.out.println("Tổng tiền hóa đơn : " + tienHoaDon);

        hoa_don hd = hdRP.findById(idHD)
                .orElseThrow(() -> new EntityNotFoundException("Không thấy hóa đơn với id: " + idHD));

        if(hd.getKhuyenMai() != null && hd.getKhuyenMai().getTrang_thai() == 1){
            float giaTriKhuyenMai = hd.getKhuyenMai().getGia_tri();
            // lấy % giảm
            BigDecimal phanTramGiam = new BigDecimal(giaTriKhuyenMai).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
//            BigDecimal phanTramGiam = new BigDecimal(giaTriKhuyenMai).setScale(2, RoundingMode.HALF_UP);
            //Số tiền giảm
            BigDecimal giaTriGiam = tienHoaDon.multiply(phanTramGiam);
//            if(giaTriGiam.compareTo(hd.getKhuyenMai().getGia_tri_toi_thieu()) > 0){
//                BigDecimal giaGiam = tienHoaDon.subtract(hd.getKhuyenMai().getGia_tri_toi_thieu());
//                hdRP.capNhatTienHoaDon(tienHoaDon,giaGiam,idHD);
//            }else{
//                BigDecimal giaKhiGiam = tienHoaDon.subtract(giaTriGiam);
//                hdRP.capNhatTienHoaDon(tienHoaDon,giaKhiGiam,idHD);
//            }
            BigDecimal giaKhiGiam = tienHoaDon.subtract(giaTriGiam);
            hdRP.capNhatTienHoaDon(tienHoaDon,giaKhiGiam,idHD);
        }else{
            System.out.println("Không giảm ");
            hdRP.capNhatTienHoaDon(tienHoaDon,tienHoaDon,idHD);
        }

    }

    ///cập nhật khuyến mại cho hóa đơn và tính lại tổng tiền
    public void capNhatKMchoHD(Integer idKM,Integer idHD){
       khuyen_mai khuyenMai = kmRP.findById(idKM).get();
       hdRP.capNhatKhuyenMaiHD(khuyenMai,idHD);
       tinhTongTienHoaDon(idHD);
    }

    ///cập nhật khách hàng cho hóa đơn tại quầy
    public void capNhatKhachHangTaiQuay(Integer idKH,Integer idHD){
        khach_hang khachHang = khRP.findById(idKH).get();
        hdRP.capNhatKhachHangHD(khachHang,idHD);
    }

    ///cập nhật trạng thái cho hóa đơn tại quầy
    public void capNhatTrangThaiTaiQuay(int idHd,hoa_don hdView){
        hoa_don hoaDon = hdRP.findById(idHd).get();
        hdRP.capNhatTrangThaiHD(6,hdView.getTen_nguoi_nhan(),hdView.getEmail_nguoi_nhan(),hdView.getSdt_nguoi_nhan(),idHd);
        lsHoaDonService.taoLichSuTaiQuay(hoaDon,6);

    }

    ///cập nhật hóa đơn khách gọi đặt online
    public void capNhatHDKhachGoi(int idHd,hoa_don hdView){
        hdRP.capNhatHDGoiOnline(1,3,hdView.getDia_chi_nguoi_nhan(),hdView.getPhi_ship(),
                hdView.getMo_ta(),hdView.getTen_nguoi_nhan(),hdView.getEmail_nguoi_nhan(),hdView.getSdt_nguoi_nhan(),
                idHd);

        hoa_don hoaDon = hdRP.findById(idHd).get();
        lsHoaDonService.taoLichSuTaiQuay(hoaDon,1);

    }

    /// xóa khuyến mãi hóa đơn
     public void xoaKhuyenMaiHD(Integer idHd){
        hdRP.loaiBoKhuyenMaiHD(idHd);
     }



    // quản lý hóa đơn ( bán hàng online , danh sách các hóa đơn)

    public Page<hoa_don> getHoaDonByStatus(Integer page, Integer size, Integer trangThai) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Specification<hoa_don> spec = (root, query, criteriaBuilder) -> {
            if (trangThai != null) {
                return criteriaBuilder.equal(root.get("trang_thai"), trangThai);
            }
            return criteriaBuilder.conjunction();
        };
        return hdRP.findAll(spec, pageable);
    }

    // Cập nhật lại cột thành tiền , tiền sau giảm giá hóa đơn
    @Transactional
    public hoa_don updateHoaDon(int hoaDonId, BigDecimal thanhTien , BigDecimal tienSauGiamGia) {
        hoa_don hoaDon = hdRP.findById(hoaDonId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + hoaDonId));


        // Cập nhật tổng tiền hàng vào cột `thanh_tien`
        hoaDon.setThanh_tien(thanhTien);

        // Tính tổng tiền thanh toán sau khi trừ voucher
        hoaDon.setTien_sau_giam_gia(tienSauGiamGia);

        // Tăng trạng thái lên 1
        hoaDon.setTrang_thai(hoaDon.getTrang_thai() + 1);

        // Lưu lại hóa đơn đã cập nhật
        return hdRP.save(hoaDon);
    }
    // Cập nhật lại cột thành tiền , tiền sau giảm giá hóa đơn , Quay lại trạng thái hóa đơn
    @Transactional
    public hoa_don updateHoaDonCannel(int hoaDonId, BigDecimal thanhTien , BigDecimal tienSauGiamGia) {
        hoa_don hoaDon = hdRP.findById(hoaDonId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + hoaDonId));


        // Cập nhật tổng tiền hàng vào cột `thanh_tien`
        hoaDon.setThanh_tien(thanhTien);

        // Tính tổng tiền thanh toán sau khi trừ voucher
        hoaDon.setTien_sau_giam_gia(tienSauGiamGia);

        // Tăng trạng thái lên 1
        hoaDon.setTrang_thai(hoaDon.getTrang_thai() - 1);

        // Lưu lại hóa đơn đã cập nhật
        return hdRP.save(hoaDon);
    }

    // Chỉnh sửa thông tin hóa đơn
    public hoa_don updateHoaDonInfo(int id, hoa_don hoaDonUpdate) {
        Optional<hoa_don> hoaDon = hdRP.findById(id);
        if (hoaDon.isPresent()) {
            hoa_don hoaDonInDb = hoaDon.get();
            hoaDonInDb.setTen_nguoi_nhan(hoaDonUpdate.getTen_nguoi_nhan());
            hoaDonInDb.setSdt_nguoi_nhan(hoaDonUpdate.getSdt_nguoi_nhan());
            hoaDonInDb.setDia_chi_nguoi_nhan(hoaDonUpdate.getDia_chi_nguoi_nhan());
            hoaDonInDb.setMo_ta(hoaDonUpdate.getMo_ta());
            return hdRP.save(hoaDonInDb);
        }
        else return null ;
    }

    // Hủy hóa đơn
    @Transactional
    public void huyHoaDon(int idHD) {
        hoa_don hoaDon = hdRP.findById(idHD)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + idHD));

        // Cập nhật trạng thái hóa đơn về 7 (đã hủy)
        hoaDon.setTrang_thai(7);
        hdRP.save(hoaDon);

        // Lấy danh sách chi tiết hóa đơn
        List<hoa_don_chi_tiet> chiTietHoaDonList = hoaDon.getHoaDonChiTietList();
        for (hoa_don_chi_tiet chiTiet : chiTietHoaDonList) {
            // Tăng số lượng sản phẩm trở lại trong kho
            chiTiet.getChi_tiet_san_pham().setSo_luong(
                    chiTiet.getChi_tiet_san_pham().getSo_luong() + chiTiet.getSo_luong()
            );
            hdctRP.save(chiTiet);
        }
    }

    // Tạo hóa đơn bên người dùng web
    public hoa_don save(hoa_don hoaDon) {
        hoaDon.setMa_hoa_don("HD"+generateRandomCode());
        LocalDate localDate = LocalDate.now();
        Date sqlDate = Date.valueOf(localDate);
        hoaDon.setNgay_tao(sqlDate);
        return hdRP.save(hoaDon);
    }
}
