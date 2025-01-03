package org.example.beephone.service;


import org.example.beephone.dto.DoiMatKhauDTO;
import org.example.beephone.dto.KhachHangDTO;
import org.example.beephone.dto.register.OtpDTO;
import org.example.beephone.dto.register.PasswordDTO;
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
import java.time.LocalDateTime;
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

    @Autowired
    private EmailService emailService;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int MA_KH_LENGTH = 8; // Độ dài mã khách hàng
    private static final Random RANDOM = new Random();

    /* admin */

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
        khachHang.setGioi_tinh(khachHangDTO.getGioiTinh() == 1 ? 1 : 0);
        khachHang.setTrang_thai(khachHangDTO.getTrangThai() == null ? 0 :
                (khachHangDTO.getTrangThai() == 1 ? 1 : 0));

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
            existingCustomer.setGioi_tinh(khachHangDTO.getGioiTinh() == 1 ? 1 : 0);
            existingCustomer.setTrang_thai(khachHangDTO.getTrangThai() == null ? 0 :
                    (khachHangDTO.getTrangThai() == 1 ? 1 : 0));

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

    // Tạo khách hàn bên web khi khách hàng không đăng nhập ( hehe )
    public khach_hang save(khach_hang khachHang) {
        khachHang.setMa_khach_hang(generateMaKhachHang());
        return khachHangRepository.save(khachHang);
    }

    // Lấy thông tin khách hàng b��ng tài khoản và mật khẩu
    public khach_hang findByEmailAndMatKhau(String email, String matKhau) {
        return khachHangRepository.findByEmailAndMatKhau(email, matKhau);
    }

    //thêm nhanh 1 khách hàng tại quầy
    public khach_hang themKhachHangTaiQuay(khach_hang khachHang){
        //check tồn tại email,sdt
        if(khachHangRepository.existsByEmail(khachHang.getEmail())){
            throw new IllegalArgumentException("Email đã tồn tại!");
        }
        if(khachHangRepository.existsBySdt(khachHang.getSdt())){
            throw new IllegalArgumentException("Số điện thoại đã tồn tại!");
        }

        khach_hang khachHangNew = new khach_hang();

        khachHangNew.setMa_khach_hang(generateMaKhachHang());
        khachHangNew.setHo_ten(khachHang.getHo_ten());
        khachHangNew.setEmail(khachHang.getEmail());
        khachHangNew.setSdt(khachHang.getSdt());
        khachHangNew.setGioi_tinh(khachHang.getGioi_tinh());
        khachHangNew.setTrang_thai(1);

        return khachHangRepository.save(khachHangNew);
    }

    /* customer */

    // Lấy thông tin chi tiết khách hàng để hiển thị profile
    public KhachHangDTO getCustomerProfile(Integer customerId) {
        Optional<khach_hang> khachHang = khachHangRepository.findById(customerId);
        if (khachHang.isPresent()) {
            KhachHangDTO profileDTO = KhachHangDTO.fromEntity(khachHang.get());
            // Lấy địa chỉ mặc định
            Optional<dia_chi_khach_hang> defaultAddress = khachHang.get().getDiaChiKhachHang().stream()
                    .filter(addr -> addr.getTrang_thai() == 1)
                    .findFirst();
            if (defaultAddress.isPresent()) {
                DiaChiDTO defaultAddressDTO = new DiaChiDTO();
                defaultAddressDTO.setDiaChiChiTiet(defaultAddress.get().getDia_chi_chi_tiet());
                profileDTO.setDiaChiMacDinh(defaultAddressDTO);
            }
            // Log thêm để kiểm tra
            System.out.println("Customer Profile: " + profileDTO);
            System.out.println("Addresses count: " + profileDTO.getDiaChiChiTiet().size());
            return profileDTO;
        } else {
            throw new ResourceNotFoundException("Không tìm thấy khách hàng với ID: " + customerId);
        }
    }

    // Cập nhật thông tin khách hàng cho user
    @Transactional
    public KhachHangDTO updateCustomerProfile(KhachHangDTO khachHangDTO) {
        if (khachHangDTO.getId() == null) {
            throw new IllegalArgumentException("ID khách hàng không được để trống");
        }

        // Tìm kiếm khách hàng trong cơ sở dữ liệu
        Optional<khach_hang> existingCustomerOpt = khachHangRepository.findById(khachHangDTO.getId());
        if (!existingCustomerOpt.isPresent()) {
            throw new ResourceNotFoundException("Không tìm thấy khách hàng với ID: " + khachHangDTO.getId());
        }

        khach_hang existingCustomer = existingCustomerOpt.get();

        // Cập nhật thông tin cá nhân (Không bao gồm mật khẩu và địa chỉ)
        if (khachHangDTO.getTaiKhoan() != null && !khachHangDTO.getTaiKhoan().trim().isEmpty()) {
            existingCustomer.setTai_khoan(khachHangDTO.getTaiKhoan());
        }
        if (khachHangDTO.getHoTen() != null && !khachHangDTO.getHoTen().trim().isEmpty()) {
            existingCustomer.setHo_ten(khachHangDTO.getHoTen());
        }
        if (khachHangDTO.getEmail() != null && !khachHangDTO.getEmail().trim().isEmpty()) {
            existingCustomer.setEmail(khachHangDTO.getEmail());
        }
        if (khachHangDTO.getSdt() != null && !khachHangDTO.getSdt().trim().isEmpty()) {
            existingCustomer.setSdt(khachHangDTO.getSdt());
        }
        if (khachHangDTO.getNgaySinh() != null) {
            existingCustomer.setNgay_sinh(khachHangDTO.getNgaySinh());
        }
        if (khachHangDTO.getGioiTinh() != null) {
            existingCustomer.setGioi_tinh(khachHangDTO.getGioiTinh());
        }
        if (khachHangDTO.getTrangThai() != null) {
            existingCustomer.setTrang_thai(khachHangDTO.getTrangThai());
        }

        // Lưu các thay đổi vào cơ sở dữ liệu
        khachHangRepository.save(existingCustomer);

        // Trả về DTO đã được cập nhật
        return KhachHangDTO.fromEntity(existingCustomer);
    }

    @Transactional
    public boolean doiMatKhau(DoiMatKhauDTO doiMatKhauDTO) {
        Optional<khach_hang> khachHangOpt = khachHangRepository.findById(doiMatKhauDTO.getCustomerId());
        if (!khachHangOpt.isPresent()) {
            throw new ResourceNotFoundException("Không tìm thấy khách hàng");
        }

        khach_hang khachHang = khachHangOpt.get();

        // Kiểm tra mật khẩu hiện tại
        if (!khachHang.getMat_khau().equals(doiMatKhauDTO.getMatKhauHienTai())) {
            throw new IllegalArgumentException("Mật khẩu hiện tại không đúng");
        }

        // Cập nhật mật khẩu
        khachHang.setMat_khau(doiMatKhauDTO.getMatKhauMoi());
        khachHangRepository.save(khachHang);
        return true;
    }

}