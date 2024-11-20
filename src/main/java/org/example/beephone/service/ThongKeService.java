package org.example.beephone.service;

import org.example.beephone.entity.chi_tiet_san_pham;
import org.example.beephone.entity.hoa_don;
import org.example.beephone.repository.ChiTietSanPhamRepository;
import org.example.beephone.repository.HoaDonChiTietRepository;
import org.example.beephone.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ThongKeService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    // Doanh số tháng này (đơn hàng và doanh số)
    public Map<String, Object> getMonthlySales() {
        Date firstDayOfMonth = Date.valueOf(LocalDate.now().withDayOfMonth(1));
        Date lastDayOfMonth = Date.valueOf(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));

        List<hoa_don> monthlyOrders = hoaDonRepository.findCompletedOrdersByDateRange(firstDayOfMonth, lastDayOfMonth);
        int totalOrders = monthlyOrders.size();
        BigDecimal totalRevenue = monthlyOrders.stream()
                .map(hoa_don::getTien_sau_giam_gia)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> result = new HashMap<>();
        result.put("totalOrders", totalOrders);
        result.put("totalRevenue", totalRevenue);
        return result;
    }

    // Doanh số hôm nay (đơn hàng và doanh số)
    public Map<String, Object> getTodaySales() {
        Date today = Date.valueOf(LocalDate.now());

        List<hoa_don> dailyOrders = hoaDonRepository.findCompletedOrdersByDate(today);
        int totalOrders = dailyOrders.size();
        BigDecimal totalRevenue = dailyOrders.stream()
                .map(hoa_don::getTien_sau_giam_gia)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> result = new HashMap<>();
        result.put("totalOrders", totalOrders);
        result.put("totalRevenue", totalRevenue);
        return result;
    }

    // Hàng bán được tháng này
    public int getMonthlySoldProducts() {
        Date firstDayOfMonth = Date.valueOf(LocalDate.now().withDayOfMonth(1));
        Date lastDayOfMonth = Date.valueOf(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));

        return hoaDonChiTietRepository.findSoldQuantityByDateRange(firstDayOfMonth, lastDayOfMonth);
    }

    // Sản phẩm bán chạy
    public List<Map<String, Object>> getBestSellingProducts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Object[]> rawData = hoaDonChiTietRepository.findBestSellingProducts(pageable);

        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rawData) {
            Map<String, Object> productData = new HashMap<>();
            productData.put("giaBan", row[0]); // Giá bán
            productData.put("anh", row[1]);   // Ảnh sản phẩm
            productData.put("tenSanPham", row[2]); // Tên sản phẩm
            productData.put("soLuongDaBan", row[3]); // Số lượng đã bán
            result.add(productData);
        }
        return result;
    }

    // Sản phẩm sắp hết hàng
    public List<Map<String, Object>> getLowStockProducts(int threshold) {
        List<Object[]> rawData = chiTietSanPhamRepository.findLowStockProducts(threshold);

        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rawData) {
            chi_tiet_san_pham chiTietSanPham = (chi_tiet_san_pham) row[0];
            String tenSanPham = (String) row[1];

            Map<String, Object> productData = new HashMap<>();
            productData.put("chiTietSanPham", chiTietSanPham);
            productData.put("tenSanPham", tenSanPham);
            result.add(productData);
        }
        return result;
    }

    // Dữ liệu biểu đồ thống kê
    public List<Map<String, Object>> getMonthlyChartData() {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        List<Map<String, Object>> result = new ArrayList<>();

        for (LocalDate date = firstDayOfMonth; !date.isAfter(lastDayOfMonth); date = date.plusDays(1)) {
            Date sqlDate = Date.valueOf(date);

            Integer hoaDonCount = hoaDonRepository.countByNgayTaoAndTrangThai(sqlDate); // Đếm hóa đơn
            Integer sanPhamCount = hoaDonRepository.sumSanPhamByNgayTao(sqlDate); // Tổng sản phẩm bán

            Map<String, Object> data = new HashMap<>();
            data.put("date", date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            data.put("hoaDonCount", hoaDonCount == null ? 0 : hoaDonCount); // Gán 0 nếu null
            data.put("sanPhamCount", sanPhamCount == null ? 0 : sanPhamCount); // Gán 0 nếu null

            result.add(data);
        }

        return result;
    }

    // Thống kê trạng thái của đơn hàng

    public Map<String, Object> getOrderStatusStatistics() {
        List<Object[]> rawData = hoaDonRepository.findOrderStatusStatistics();

        Map<String, Object> result = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Integer> data = new ArrayList<>();

        for (Object[] row : rawData) {
            int status = (int) row[0];
            long count = (long) row[1];

            String statusLabel = getStatusLabel(status);
            labels.add(statusLabel);
            data.add((int) count);
        }

        result.put("labels", labels);
        result.put("data", data);
        return result;
    }

    private String getStatusLabel(int status) {
        switch (status) {
            case 1: return "Chờ Xác Nhận";
            case 2: return "Xác Nhận";
            case 3: return "Chờ Vận Chuyển";
            case 4: return "Vận Chuyển";
            case 5: return "Đã Thanh Toán";
            case 6: return "Thành Công";
            case 7: return "Đã Hủy";
            default: return "Khác";
        }
    }
}
