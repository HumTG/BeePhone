package org.example.beephone.controller.api;

import org.example.beephone.entity.chi_tiet_san_pham;
import org.example.beephone.repository.ChiTietSanPhamRepository;
import org.example.beephone.service.ChiTietSanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/rest/chi-tiet-san-pham")
public class ChiTietSanPhamController {

    @Autowired
    private ChiTietSanPhamService chiTietSanPhamService;

    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(chiTietSanPhamService.getAll());
    }
    @GetMapping("/list")
    public ResponseEntity<?> getAllChiTietSanPham(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        // Lấy danh sách tất cả sản phẩm chi tiết
        List<chi_tiet_san_pham> chiTietSanPhamList = chiTietSanPhamRepository.findAll();

        // Tính toán phân trang
        int start = page * size;
        int end = Math.min((page + 1) * size, chiTietSanPhamList.size());

        // Kiểm tra nếu start lớn hơn kích thước danh sách, trả về danh sách trống
        if (start > chiTietSanPhamList.size()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        // Lấy dữ liệu phân trang
        List<chi_tiet_san_pham> paginatedList = chiTietSanPhamList.subList(start, end);

        // Tạo danh sách phản hồi
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (chi_tiet_san_pham chiTietSanPham : paginatedList) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", chiTietSanPham.getId());
            response.put("maSanPham", chiTietSanPham.getSanPham().getMa_san_pham());
            response.put("tenSanPham", chiTietSanPham.getSanPham().getTen());
            response.put("moTaSanPham", chiTietSanPham.getSanPham().getMo_ta());
            response.put("mauSac", chiTietSanPham.getMauSac().getTen());
            response.put("kichCo", chiTietSanPham.getKichCo().getTen());
            response.put("anh", chiTietSanPham.getAnh());
            response.put("giaBan", chiTietSanPham.getGia_ban());
            response.put("soLuong", chiTietSanPham.getSo_luong());
            response.put("trangThai", chiTietSanPham.getTrang_thai());

            if (chiTietSanPham.getGiamGia() != null) {
                Map<String, Object> giamGia = new HashMap<>();
                giamGia.put("maGiamGia", chiTietSanPham.getGiamGia().getMa_giam_gia());
                giamGia.put("tenGiamGia", chiTietSanPham.getGiamGia().getTen());
                giamGia.put("giaTri", chiTietSanPham.getGiamGia().getGia_tri());
                giamGia.put("ngayBatDau", chiTietSanPham.getGiamGia().getNgay_bat_dau());
                giamGia.put("ngayKetThuc", chiTietSanPham.getGiamGia().getNgay_ket_thuc());
                response.put("giamGia", giamGia);
            }

            responseList.add(response);
        }

        // Đóng gói dữ liệu phân trang vào phản hồi
        Map<String, Object> paginatedResponse = new HashMap<>();
        paginatedResponse.put("content", responseList);
        paginatedResponse.put("currentPage", page);
        paginatedResponse.put("pageSize", size);
        paginatedResponse.put("totalItems", chiTietSanPhamList.size());
        paginatedResponse.put("totalPages", (int) Math.ceil((double) chiTietSanPhamList.size() / size));

        return ResponseEntity.ok(paginatedResponse);
    }


    @PutMapping("/update-multiple")
    public ResponseEntity<List<chi_tiet_san_pham>> updateMultiple(@RequestBody List<chi_tiet_san_pham> chiTietSanPhams) {
        List<chi_tiet_san_pham> updatedChiTietSanPhams = chiTietSanPhamService.updateMultiple(chiTietSanPhams);
        return ResponseEntity.ok(updatedChiTietSanPhams);
    }

    @GetMapping("/dto")
    public ResponseEntity<?> getCtspDTO(@RequestParam(defaultValue = "0") Integer page){
        return ResponseEntity.ok(chiTietSanPhamService.getCtspDTO(page));
    }
}
