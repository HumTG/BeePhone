package org.example.beephone.service;


import org.example.beephone.dto.KhachHangDTO;
import org.example.beephone.entity.dia_chi_khach_hang;
import org.example.beephone.entity.khach_hang;
import org.example.beephone.repository.DiaChiRepository;
import org.example.beephone.repository.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.example.beephone.dto.DiaChiDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class KhachHangService {

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private DiaChiRepository diaChiRepository;

    @Autowired
    private DiaChiService diaChiService;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int MA_KH_LENGTH = 8; // Độ dài mã khách hàng
    private static final Random RANDOM = new Random();

    // Hàm tạo mã khách hàng tự động
    private String generateMaKhachHang() {
        StringBuilder maKhachHang = new StringBuilder("KH");
        for (int i = 0; i < MA_KH_LENGTH - 2; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            maKhachHang.append(CHARACTERS.charAt(index));
        }
        return maKhachHang.toString();
    }

    // Hàm tạo mã địa chỉ tự động
    private String generateMaDiaChi() {
        return "DC" + System.currentTimeMillis();
    }

    // Hàm tạo mật khẩu ngẫu nhiên
    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[15];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    // Thêm khách hàng với địa chỉ chi tiết
    public KhachHangDTO addCustomerWithAddress(KhachHangDTO khachHangDTO) {
        khach_hang khachHang = new khach_hang();

        String randomPassword = generateRandomPassword();

        khachHang.setMa_khach_hang(generateMaKhachHang());
        khachHang.setTai_khoan(khachHangDTO.getTaiKhoan());
        khachHang.setHo_ten(khachHangDTO.getHoTen());
        khachHang.setEmail(khachHangDTO.getEmail());
        khachHang.setSdt(khachHangDTO.getSdt());
        khachHang.setMat_khau(randomPassword);
        khachHang.setNgay_sinh(khachHangDTO.getNgaySinh());
        khachHang.setGioi_tinh(khachHangDTO.getGioiTinh());
        khachHang.setTrang_thai(khachHangDTO.getTrangThai());

        List<dia_chi_khach_hang> diaChiList = new ArrayList<>();
        for (DiaChiDTO diaChiDTO : khachHangDTO.getDiaChiChiTiet()) {
            dia_chi_khach_hang diaChi = new dia_chi_khach_hang();
            diaChi.setMa_dia_chi(generateMaDiaChi());
            diaChi.setDia_chi_chi_tiet(diaChiDTO.getDiaChiChiTiet());
            diaChi.setTrang_thai(diaChiDTO.getTrangThai() != null ? diaChiDTO.getTrangThai() : 0);
            diaChi.setKhachHang(khachHang);
            diaChiList.add(diaChi);
        }
        khachHang.setDiaChiKhachHang(diaChiList);

        khachHang = khachHangRepository.save(khachHang);

        // Include the generated password in the response
        KhachHangDTO responseDTO = KhachHangDTO.fromEntity(khachHang);
        responseDTO.setMatKhau(randomPassword); // So the client knows the generated password
        return responseDTO;
    }


    // Lấy danh sách khách hàng có phân trang
    public Page<KhachHangDTO> getAllCustomers(Integer page) {
        Pageable pageable = PageRequest.of(page, 10); // chỉ định 10 bản ghi mỗi trang
        Page<khach_hang> khachHangPage = khachHangRepository.getKhachHangDESCID(pageable);
        return khachHangPage.map(KhachHangDTO::fromEntity);
    }

    // Lấy tất cả khách hàng không phân trang
    public List<KhachHangDTO> getAllCustomersList() {
        List<khach_hang> customers = khachHangRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return customers.stream().map(KhachHangDTO::fromEntity).collect(Collectors.toList());
    }

    // Lấy chi tiết khách hàng
    public KhachHangDTO getKhachHangDetail(Integer id) {
        Optional<khach_hang> khachHang = khachHangRepository.findById(id);
        if (khachHang.isPresent()) {
            return KhachHangDTO.fromEntity(khachHang.get());
        } else {
            throw new ResourceNotFoundException("Không tìm thấy khách hàng với ID: " + id);
        }
    }

    // Phương thức để cập nhật địa chỉ mặc định
    public void updateDefaultAddress(Integer customerId, Integer addressId) {
        // Đặt tất cả địa chỉ của khách hàng về trạng thái 0
        diaChiRepository.updateAllAddressesToNonDefault(customerId);

        // Đặt địa chỉ được chọn là mặc định
        diaChiRepository.setAddressAsDefault(addressId);
    }

    // Sửa thông tin khách hàng
    public KhachHangDTO updateCustomer(KhachHangDTO khachHangDTO) {
        Optional<khach_hang> existingCustomerOpt = khachHangRepository.findById(khachHangDTO.getId());
        if (existingCustomerOpt.isPresent()) {
            khach_hang existingCustomer = existingCustomerOpt.get();

            // Update all fields except password
            existingCustomer.setHo_ten(khachHangDTO.getHoTen());
            existingCustomer.setEmail(khachHangDTO.getEmail());
            existingCustomer.setSdt(khachHangDTO.getSdt());
            existingCustomer.setNgay_sinh(khachHangDTO.getNgaySinh());
            existingCustomer.setGioi_tinh(khachHangDTO.getGioiTinh());
            existingCustomer.setTrang_thai(khachHangDTO.getTrangThai());

            // Handle address updates
            if (khachHangDTO.getDiaChiChiTiet() != null) {
                diaChiRepository.deleteByKhachHang(existingCustomer);
                for (DiaChiDTO diaChiDTO : khachHangDTO.getDiaChiChiTiet()) {
                    dia_chi_khach_hang newAddress = new dia_chi_khach_hang();
                    newAddress.setMa_dia_chi(generateMaDiaChi());
                    newAddress.setDia_chi_chi_tiet(diaChiDTO.getDiaChiChiTiet());
                    newAddress.setTrang_thai(diaChiDTO.getTrangThai() != null ? diaChiDTO.getTrangThai() : 0);
                    newAddress.setKhachHang(existingCustomer);
                    diaChiRepository.save(newAddress);
                }
            }

            khachHangRepository.save(existingCustomer);
            return KhachHangDTO.fromEntity(existingCustomer);
        }
        throw new ResourceNotFoundException("Không tìm thấy khách hàng với ID: " + khachHangDTO.getId());
    }

    // Xóa khách hàng
    @Transactional
    public boolean deleteKhachHangById(Integer id) {
        if (khachHangRepository.existsById(id)) {
            khachHangRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<KhachHangDTO> filterCustomers(String search, LocalDate dobFrom, LocalDate dobTo, Integer status, Integer ageRange) {
        // Tìm kiếm tất cả khách hàng trong DB
        List<khach_hang> customers = khachHangRepository.findAll();

        // Áp dụng bộ lọc
        Stream<khach_hang> filteredStream = customers.stream();

        if (search != null && !search.isEmpty()) {
            filteredStream = filteredStream.filter(c ->
                    c.getHo_ten().contains(search) || c.getSdt().contains(search));
        }

        if (dobFrom != null) {
            filteredStream = filteredStream.filter(c -> !c.getNgay_sinh().isBefore(dobFrom));
        }

        if (dobTo != null) {
            filteredStream = filteredStream.filter(c -> !c.getNgay_sinh().isAfter(dobTo));
        }

        if (status != null) {
            filteredStream = filteredStream.filter(c -> c.getTrang_thai().equals(status));
        }

        if (ageRange != null) {
            int currentYear = LocalDate.now().getYear();
            filteredStream = filteredStream.filter(c -> {
                int age = currentYear - c.getNgay_sinh().getYear();
                return age <= ageRange;
            });
        }

        // Chuyển đổi danh sách khách hàng đã lọc sang DTO
        return filteredStream.map(KhachHangDTO::fromEntity).collect(Collectors.toList());
    }

    /// lấy danh sách khách hàng để bán hàng
    public Page<khach_hang> getListKhBanHang(Integer page){
        Pageable khachHangPage = PageRequest.of(page,8);
        return khachHangRepository.getKhachHangBanHang(khachHangPage);
    }

    // Tạo khách hàn bên web khi khách hàng không đăng nhập ( lol đức đừng xóa lần nữa )
    public khach_hang save(khach_hang khachHang) {
        khachHang.setMa_khach_hang(generateMaKhachHang());
        return khachHangRepository.save(khachHang);
    }


}