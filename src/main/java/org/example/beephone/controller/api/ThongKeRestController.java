package org.example.beephone.controller.api;


import org.example.beephone.entity.chi_tiet_san_pham;
import org.example.beephone.service.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/thong-ke")
public class ThongKeRestController {
    @Autowired
    private ThongKeService statisticService;


    // Doanh số tháng này
    @GetMapping("/monthly-sales")
    public ResponseEntity<Map<String, Object>> getMonthlySales() {
        return ResponseEntity.ok(statisticService.getMonthlySales());
    }

    // Doanh số hôm nay
    @GetMapping("/today-sales")
    public ResponseEntity<Map<String, Object>> getTodaySales() {
        return ResponseEntity.ok(statisticService.getTodaySales());
    }

    // Hàng bán đc tháng này
    @GetMapping("/monthly-sold-products")
    public ResponseEntity<Integer> getMonthlySoldProducts() {
        return ResponseEntity.ok(statisticService.getMonthlySoldProducts());
    }

    // Sản Phẩm sắp hết hàng
    @GetMapping("/low-stock-products")
    public ResponseEntity<List<Map<String, Object>>> getLowStockProducts(
            @RequestParam(defaultValue = "10") int threshold) {
        return ResponseEntity.ok(statisticService.getLowStockProducts(threshold));
    }

    // Sản phẩm ban chay
    @GetMapping("/best-selling-products")
    public ResponseEntity<List<Map<String, Object>>> getBestSellingProducts(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(statisticService.getBestSellingProducts(limit));
    }

    // Dữ liệu biểu đồ
    @GetMapping("/monthly-chart-data")
    public List<Map<String, Object>> getMonthlyChartData() {
        return statisticService.getMonthlyChartData();
    }

    // Thống kê trạng thái của đơn hàng
    @GetMapping("/order-status")
    public ResponseEntity<Map<String, Object>> getOrderStatusStatistics() {
        return ResponseEntity.ok(statisticService.getOrderStatusStatistics());
    }
}
