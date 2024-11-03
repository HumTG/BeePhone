package org.example.beephone.service;

import org.example.beephone.dto.SanPhamCustom;
import org.example.beephone.dto.SanPhamDTO;
import org.example.beephone.dto.Top5Seller;
import org.example.beephone.entity.*;
import org.example.beephone.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SanPhamService {

    @Autowired
    private SanPhamRepository sanPhamRepository ;

    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Autowired
    private KichCoRepository kichCoRepository;

    @Autowired
    private MauSacRepository mauSacRepository;

    @Autowired
    private ChatLieuRepository chatLieuRepository;

    @Autowired
    private NhaSanXuatRepository nhaSanXuatRepository;



//    public List<Top5Seller> getTop5(){
//        return sanPhamRepository.getTop5Seller();
//    }
//
//    public Page<SanPhamCustom> getPage(Integer page){
//        Pageable pageable = PageRequest.of(page,15);
//        return sanPhamRepository.getSanPhamPage(pageable);
//    }


    public Page<SanPhamDTO> getSanPhamWithSoLuongTon(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> results = sanPhamRepository.getSanPhamWithSoLuongTon(pageable);

        return results.map(obj -> new SanPhamDTO(
                (Integer) obj[0], // id
                (String) obj[1],  // maSanPham
                (String) obj[2],  // tenSanPham
                ((Number) obj[3]).intValue(), // soLuongTon
                (int) obj[4] // trangThai
        ));
    }

    public Page<SanPhamDTO> findSanPhamWithFilters(String maHoacTenSanPham, Integer trangThai, Integer soLuongTon, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> results = sanPhamRepository.searchSanPhamWithSoLuongTon(maHoacTenSanPham, trangThai, soLuongTon, pageable);

        return results.map(obj -> new SanPhamDTO(
                (Integer) obj[0], // id
                (String) obj[1],  // maSanPham
                (String) obj[2],  // tenSanPham
                ((Number) obj[3]).intValue(), // soLuongTon
                (Integer) obj[4] // trangThai
        ));
    }


    // Thêm sản phẩm chính vào cơ sở dữ liệu
    // Thêm sản phẩm chính vào cơ sở dữ liệu
    public san_pham addSanPham(String ten, String moTa, int nhaSanXuatId, int chatLieuId, int trangThai) {
        san_pham newSanPham = new san_pham();

        // Tạo mã sản phẩm ngẫu nhiên
        String maSanPham = "SP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        newSanPham.setMa_san_pham(maSanPham);

        newSanPham.setTen(ten);
        newSanPham.setMo_ta(moTa);
        newSanPham.setTrang_thai(trangThai);

        // Lấy và gán nhà sản xuất
        nha_san_xuat nhaSanXuat = nhaSanXuatRepository.findById(nhaSanXuatId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhà sản xuất với ID: " + nhaSanXuatId));
        newSanPham.setNhaSanXuat(nhaSanXuat);

        // Lấy và gán chất liệu
        chat_lieu chatLieu = chatLieuRepository.findById(chatLieuId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chất liệu với ID: " + chatLieuId));
        newSanPham.setChatLieu(chatLieu);

        return sanPhamRepository.save(newSanPham);
    }

    // Thêm chi tiết sản phẩm (biến thể) vào cơ sở dữ liệu cho một sản phẩm
    @Transactional
    public List<chi_tiet_san_pham> addChiTietSanPham(int sanPhamId, List<Integer> kichCoIds, List<Integer> mauSacIds, List<Integer> soLuongs, List<BigDecimal> giaBans, List<String> images) {
        san_pham sanPham = sanPhamRepository.findById(sanPhamId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm với ID: " + sanPhamId));

        List<chi_tiet_san_pham> chiTietSanPhams = new ArrayList<>();

        for (int i = 0; i < kichCoIds.size(); i++) {
            int kichCoId = kichCoIds.get(i);
            int mauSacId = mauSacIds.get(i);

            kich_co kichCo = kichCoRepository.findById(kichCoId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy kích cỡ với ID: " + kichCoId));
            mau_sac mauSac = mauSacRepository.findById(mauSacId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy màu sắc với ID: " + mauSacId));

            chi_tiet_san_pham chiTietSanPham = new chi_tiet_san_pham();
            chiTietSanPham.setSanPham(sanPham);
            chiTietSanPham.setKichCo(kichCo);
            chiTietSanPham.setMauSac(mauSac);
            chiTietSanPham.setSo_luong(soLuongs.get(i));
            chiTietSanPham.setGia_ban(giaBans.get(i));
            chiTietSanPham.setNgay_nhap(new Date());
            chiTietSanPham.setAnh(images.get(i)); // Lưu ảnh vào trường anh
            chiTietSanPham.setTrang_thai(1);

            chiTietSanPhams.add(chiTietSanPham);
        }

        return chiTietSanPhamRepository.saveAll(chiTietSanPhams);
    }

    @Transactional(readOnly = true)
    public san_pham getSanPhamDetail(int id) {
        // Lấy sản phẩm theo ID, nếu không tìm thấy sẽ ném ngoại lệ
        san_pham sanPham = sanPhamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm với ID: " + id));

        // Lấy danh sách biến thể của sản phẩm
        List<chi_tiet_san_pham> variants = chiTietSanPhamRepository.findBySanPhamId(id);
        sanPham.setVariants(variants);

        return sanPham;
    }







}