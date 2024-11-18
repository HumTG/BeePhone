package org.example.beephone.controller.api;

import org.example.beephone.dto.KhachHangDTO;
import org.example.beephone.dto.LoginRequest;
import org.example.beephone.entity.khach_hang;
import org.example.beephone.repository.KhachHangRepository;
import org.example.beephone.service.DiaChiService;
import org.example.beephone.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rest/khach-hang")
public class KhachHangController {

    @Autowired
    private KhachHangService service;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private DiaChiService  diaChiService;

    // Lấy danh sách khách hàng với phân trang
    @GetMapping("/all")
    public ResponseEntity<?> getAllCustomers(@RequestParam(defaultValue = "0") Integer page) {
        try {
            Page<KhachHangDTO> khachHangPage = service.getAllCustomers(page);
            return ResponseEntity.ok(khachHangPage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi tải dữ liệu khách hàng: " + e.getMessage());
        }
    }

    // Lấy danh sách khách hàng với không phân trang
    @GetMapping("/list")
    public ResponseEntity<?> getAllCustomersList() {
        try {
            List<KhachHangDTO> customers = service.getAllCustomersList();
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi tải danh sách khách hàng: " + e.getMessage());
        }
    }

    // Lấy chi tiết khách hàng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getKhachHangDetail(@PathVariable Integer id) {
        try {
            KhachHangDTO khachHang = service.getKhachHangDetail(id);
            if (khachHang != null) {
                return ResponseEntity.ok(khachHang);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy khách hàng với ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi lấy chi tiết khách hàng: " + e.getMessage());
        }
    }

//    // Thêm khách hàng mới
    @PostMapping("/add")
    public ResponseEntity<?> addCustomer(@RequestBody KhachHangDTO khachHangDTO) {
        try {
            KhachHangDTO createdCustomer = service.addCustomerWithAddress(khachHangDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi thêm khách hàng: " + e.getMessage());
        }
    }

    // API cập nhật địa chỉ mặc định của khách hàng
    @PostMapping("/update-default-address")
    public ResponseEntity<String> updateDefaultAddress(
            @RequestParam Integer customerId,
            @RequestParam Integer addressId
    ) {
        try {
            diaChiService.updateDefaultAddress(customerId, addressId);
            return ResponseEntity.ok("Cập nhật địa chỉ mặc định thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra khi cập nhật địa chỉ");
        }
    }

//    // Cập nhật khách hàng
    @PutMapping("/update")
    public ResponseEntity<?> updateCustomer(@RequestBody KhachHangDTO khachHangDTO) {
        if (khachHangDTO.getId() == null || khachHangDTO.getMaKhachHang() == null) {
            return ResponseEntity.badRequest().body("ID hoặc mã khách hàng không được để trống.");
        }
        try {
            KhachHangDTO updatedCustomer = service.updateCustomer(khachHangDTO);
            return ResponseEntity.ok(updatedCustomer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi cập nhật khách hàng: " + e.getMessage());
        }
    }

    // Xóa khách hàng theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteKhachHangById(@PathVariable Integer id) {
        try {
            boolean isDeleted = service.deleteKhachHangById(id);
            if (isDeleted) {
                return ResponseEntity.ok("Xóa khách hàng thành công.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy khách hàng với ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi xóa khách hàng: " + e.getMessage());
        }
    }

    @GetMapping("/filter-customers")
    public ResponseEntity<List<KhachHangDTO>> filterCustomers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dobFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dobTo,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer ageRange) {

        List<KhachHangDTO> filteredCustomers = service.filterCustomers(search, dobFrom, dobTo, status, ageRange);
        return ResponseEntity.ok(filteredCustomers);
    }

    /// lấy danh sách khách hàng để bán hàng tại quầy
    @GetMapping("/ban-hang")
    public ResponseEntity<?> getKhBanHang(@RequestParam(defaultValue = "0") Integer page) {
        return ResponseEntity.ok(service.getListKhBanHang(page));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            khach_hang khachHang = service.findByEmailAndMatKhau(loginRequest.getEmail(), loginRequest.getMatKhau());
            if (khachHang == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email hoặc mật khẩu không đúng");
            }

            // Tạo phản hồi chứa thông tin cơ bản
            khach_hang response = new khach_hang(
                    khachHang.getId(),
                    khachHang.getMa_khach_hang(),
                    khachHang.getTai_khoan(),
                    khachHang.getHo_ten(),
                    khachHang.getEmail(),
                    khachHang.getSdt(),
                    khachHang.getMat_khau(),
                    khachHang.getNgay_sinh(),
                    khachHang.getGioi_tinh(),
                    khachHang.getTrang_thai(),
                    khachHang.getDiaChiKhachHang()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi đăng nhập: " + e.getMessage());
        }
    }




}
