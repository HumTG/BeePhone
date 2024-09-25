
Create database BeePhone
go

use BeePhone
go

CREATE TABLE [chat_lieu] (
  [id] INT IDENTITY,
  [ma_chat_lieu] Varchar(50),
  [ten] Nvarchar(100),
  [trang_thai] INT,
  PRIMARY KEY ([id])
);

CREATE TABLE [khach_hang] (
  [id] INT IDENTITY,
  [ma_khach_hang] Nvarchar(10),
  [tai_khoan] varchar(20),
  [ho_ten] Nvarchar(100),
  [email] Nvarchar(100),
  [sdt] Varchar(15),
  [mat_khau] Varchar(100),
  [ngay_sinh] Date,
  [gioi_tinh] INT,
  [trang_thai] INT,
  PRIMARY KEY ([id])
);

CREATE TABLE [gio_hang] (
  [id] INT IDENTITY,
  [id_khach_hang] INT,
  [ma_gio_hang] Nvarchar(50),
  [ngay_tao] Date,
  [tong_tien] Decimal,
  [trang_thai] INT,
  PRIMARY KEY ([id]),
  CONSTRAINT [FK_gio_hang.id_khach_hang]
    FOREIGN KEY ([id_khach_hang])
      REFERENCES [khach_hang]([id])
);

CREATE TABLE [giam_gia] (
  [id] INT IDENTITY,
  [ma_giam_gia] varchar(50),
  [ten] Nvarchar(100),
  [gia_tri] Float,
  [ngay_bat_dau] Date,
  [ngay_ket_thuc] Date,
  [trang_thai] INT,
  PRIMARY KEY ([id])
);

CREATE TABLE [mau_sac] (
  [id] INT IDENTITY,
  [ma_mau_sac] Varchar(50),
  [ten] Nvarchar(100),
  [trang_thai] INT,
  PRIMARY KEY ([id])
);

CREATE TABLE [nha_san_xuat] (
  [id] INT IDENTITY,
  [ma_nha_san_xuat] Varchar(50),
  [ten] Nvarchar(100),
  [trang_thai] INT,
  PRIMARY KEY ([id])
);

CREATE TABLE [san_pham] (
  [id] INT IDENTITY,
  [ma_san_pham] varchar(50),
  [id_nha_san_xuat] INT,
  [ten] Nvarchar(100),
  [mo_ta] Nvarchar(500),
  [trang_thai] INT,
  PRIMARY KEY ([id]),
  CONSTRAINT [FK_san_pham.id_nha_san_xuat]
    FOREIGN KEY ([id_nha_san_xuat])
      REFERENCES [nha_san_xuat]([id])
);

CREATE TABLE [chi_tiet_san_pham] (
  [id] INT IDENTITY,
  [id_san_pham] INT,
  [id_chat_lieu] INT,
  [id_mau_sac] INT,
  [id_giam_gia] INT,
  [so_luong] INT,
  [gia_nhap] Decimal,
  [gia_ban] Decimal,
  [ngay_nhap] Date,
  [mo_ta] Nvarchar(500),
  [trang_thai] INT,
  PRIMARY KEY ([id]),
  CONSTRAINT [FK_chi_tiet_san_pham.id_chat_lieu]
    FOREIGN KEY ([id_chat_lieu])
      REFERENCES [chat_lieu]([id]),
  CONSTRAINT [FK_chi_tiet_san_pham.id_giam_gia]
    FOREIGN KEY ([id_giam_gia])
      REFERENCES [giam_gia]([id]),
  CONSTRAINT [FK_chi_tiet_san_pham.id_mau_sac]
    FOREIGN KEY ([id_mau_sac])
      REFERENCES [mau_sac]([id]),
  CONSTRAINT [FK_chi_tiet_san_pham.id_san_pham]
    FOREIGN KEY ([id_san_pham])
      REFERENCES [san_pham]([id])
);

CREATE TABLE [gio_hang_chi_tiet] (
  [id] INT IDENTITY,
  [id_gio_hang] INT,
  [ma_gio_hang_chi_tiet] Nvarchar(50),
  [id_chi_tiet_san_pham] INT,
  [so_luong] INT,
  [trang_thai] INT,
  PRIMARY KEY ([id]),
  CONSTRAINT [FK_gio_hang_chi_tiet.id_gio_hang]
    FOREIGN KEY ([id_gio_hang])
      REFERENCES [gio_hang]([id]),
  CONSTRAINT [FK_gio_hang_chi_tiet.id_chi_tiet_san_pham]
    FOREIGN KEY ([id_chi_tiet_san_pham])
      REFERENCES [chi_tiet_san_pham]([id])
);

CREATE TABLE [anh_san_pham] (
  [id] INT IDENTITY,
  [id_san_pham] INT,
  [anh_1] Varchar(200),
  [anh_2] Varchar(200),
  [anh_3] Varchar(200),
  [anh_4] Varchar(200),
  [anh_5] Varchar(200),
  PRIMARY KEY ([id]),
  CONSTRAINT [FK_anh_san_pham.id_san_pham]
    FOREIGN KEY ([id_san_pham])
      REFERENCES [san_pham]([id])
);

CREATE TABLE [chuc_vu] (
  [id] INT IDENTITY,
  [ma_chuc_vu] Nvarchar(50),
  [ten_chuc_vu] Nvarchar(100),
  [trang_thai] INT,
  PRIMARY KEY ([id])
);

CREATE TABLE [nhan_vien] (
  [id] INT IDENTITY,
  [ma_nhan_vien] Nvarchar(50),
  [ho_ten] Nvarchar(100),
  [id_chuc_vu] INT,
  [email] Nvarchar(100),
  [sdt] Varchar(15),
  [mat_khau] Varchar(100),
  [ngay_sinh] Date,
  [gioi_tinh] INT,
  [hinh_anh] Varchar(100),
  [trang_thai] INT,
  PRIMARY KEY ([id]),
  CONSTRAINT [FK_nhan_vien.id_chuc_vu]
    FOREIGN KEY ([id_chuc_vu])
      REFERENCES [chuc_vu]([id])
);

CREATE TABLE [khuyen_mai] (
  [id] INT IDENTITY,
  [ma_khuyen_mai] Nvarchar(50),
  [gia_tri] Float,
  [ngay_bat_dau] Date,
  [ngay_ket_thuc] Date,
  [ngay_tao] Date,
  [trang_thai] INT,
  PRIMARY KEY ([id])
);

CREATE TABLE [hoa_don] (
  [id] INT IDENTITY,
  [ma_hoa_don] Nvarchar(50),
  [id_khach_hang] INT,
  [id_nhan_vien] INT,
  [id_khuyen_mai] INT,
  [ngay_tao] Date,
  [tien_sau_giam_gia] Decimal,
  [thanh_tien] Decimal,
  [phuong_thuc_thanh_toan] INT,
  [mo_ta] Nvarchar(500),
  [trang_thai] INT,
  PRIMARY KEY ([id]),
  CONSTRAINT [FK_hoa_don.id_khuyen_mai]
    FOREIGN KEY ([id_khuyen_mai])
      REFERENCES [khuyen_mai]([id]),
  CONSTRAINT [FK_hoa_don.id_khach_hang]
    FOREIGN KEY ([id_khach_hang])
      REFERENCES [khach_hang]([id]),
  CONSTRAINT [FK_hoa_don.id_nhan_vien]
    FOREIGN KEY ([id_nhan_vien])
      REFERENCES [nhan_vien]([id])
);

CREATE TABLE [dia_chi_khach_hang] (
  [id] INT IDENTITY,
  [id_khach_hang] INT,
  [ma_dia_chi] Nvarchar(50),
  [dia_chi_chi_tiet] Nvarchar(200),
  [trang_thai] INT,
  PRIMARY KEY ([id]),
  CONSTRAINT [FK_dia_chi_khach_hang.id_khach_hang]
    FOREIGN KEY ([id_khach_hang])
      REFERENCES [khach_hang]([id])
);

CREATE TABLE [hoa_don_chi_tiet] (
  [id] INT IDENTITY,
  [ma_hoa_don_chi_tiet] Nvarchar(50),
  [id_hoa_don] INT,
  [id_chi_tiet_san_pham] INT,
  [so_luong] INT,
  [don_gia] Decimal,
  [trang_thai] INT,
  PRIMARY KEY ([id]),
  CONSTRAINT [FK_hoa_don_chi_tiet.id_hoa_don]
    FOREIGN KEY ([id_hoa_don])
      REFERENCES [hoa_don]([id]),
  CONSTRAINT [FK_hoa_don_chi_tiet.id_chi_tiet_san_pham]
    FOREIGN KEY ([id_chi_tiet_san_pham])
      REFERENCES [chi_tiet_san_pham]([id])
);

CREATE TABLE [tuyen_dung] (
  [id] INT IDENTITY,
  [ma_tuyen_dung] Nvarchar(50),
  [vi_tri_tuyen_dung] Nvarchar(100),
  [ngay_bat_dau] Date,
  [ngay_ket_thuc] Date,
  [hinh_anh] Varchar(50),
  [noi_dung] Nvarchar(500),
  [ngay_tao] Date,
  [trang_thai] INT,
  PRIMARY KEY ([id])
);

CREATE TABLE [dia_chi_nhan_vien] (
  [id] INT IDENTITY,
  [id_nhan_vien] INT,
  [ma_dia_chi] Nvarchar(50),
  [dia_chi] Nvarchar(200),
  [trang_thai] INT,
  PRIMARY KEY ([id]),
  CONSTRAINT [FK_dia_chi_nhan_vien.id_nhan_vien]
    FOREIGN KEY ([id_nhan_vien])
      REFERENCES [nhan_vien]([id])
);

CREATE TABLE [danh_gia] (
  [id] INT IDENTITY,
  [ma_danh_gia] Nvarchar(10),
  [id_khach_hang] INT,
  [id_san_pham_chi_tiet] INT,
  [so_sao_danh_gia] INT,
  [hinh_anh] Varchar(100),
  [mo_ta] Nvarchar(100),
  [ngay_danh_gia] Date,
  [trang_thai] INT,
  PRIMARY KEY ([id]),
  CONSTRAINT [FK_danh_gia.id_khach_hang]
    FOREIGN KEY ([id_khach_hang])
      REFERENCES [khach_hang]([id]),
  CONSTRAINT [FK_danh_gia.id_san_pham_chi_tiet]
    FOREIGN KEY ([id_san_pham_chi_tiet])
      REFERENCES [chi_tiet_san_pham]([id])
);


-- data 
INSERT INTO [nha_san_xuat] ([ma_nha_san_xuat], [ten], [trang_thai])
VALUES 
    ('NSX001', N'Spigen', 1),
    ('NSX002', N'UAG (Urban Armor Gear)', 1),
    ('NSX003', N'OtterBox', 1),
    ('NSX004', N'Caseology', 1),
    ('NSX005', N'Ringke', 1);

INSERT INTO [san_pham] ([ma_san_pham], [id_nha_san_xuat], [ten], [mo_ta], [trang_thai])
VALUES 
    ('SP001', 1, N'Ốp lưng Spigen Tough Armor', N'Ốp lưng bảo vệ tối ưu với thiết kế hai lớp chống sốc.', 1),
    ('SP002', 2, N'Ốp lưng UAG Monarch', N'Ốp lưng siêu bền đạt tiêu chuẩn quân đội với thiết kế mạnh mẽ.', 1),
    ('SP003', 3, N'Ốp lưng OtterBox Defender', N'Ốp lưng nhiều lớp bảo vệ tối đa, chống va đập hiệu quả.', 1),
    ('SP004', 4, N'Ốp lưng Caseology Parallax', N'Thiết kế đẹp mắt với lớp chống sốc và họa tiết 3D nổi bật.', 1),
    ('SP005', 5, N'Ốp lưng Ringke Fusion X', N'Ốp lưng trong suốt với khung bảo vệ chắc chắn, chống trầy xước.', 1),
	('SP006', 1, N'Ốp lưng Spigen Liquid Air', N'Ốp lưng mỏng nhẹ, chống trượt và bảo vệ tốt.', 1),
    ('SP007', 2, N'Ốp lưng UAG Plasma', N'Ốp lưng nhẹ nhưng cực kỳ bền, bảo vệ chống sốc hiệu quả.', 1),
    ('SP008', 3, N'Ốp lưng OtterBox Commuter', N'Thiết kế hai lớp bảo vệ và mỏng gọn cho túi xách.', 1),
    ('SP009', 4, N'Ốp lưng Caseology Skyfall', N'Ốp lưng trong suốt, bảo vệ chắc chắn và giữ nguyên màu sắc điện thoại.', 1),
    ('SP010', 5, N'Ốp lưng Ringke Onyx', N'Ốp lưng với họa tiết nổi chống trượt và chống sốc.', 1),
    ('SP011', 1, N'Ốp lưng Spigen Rugged Armor', N'Ốp lưng kiểu dáng mạnh mẽ, bảo vệ chống va đập với họa tiết carbon.', 1),
    ('SP012', 2, N'Ốp lưng UAG Pathfinder', N'Thiết kế độc đáo, siêu bền, chống va đập với nhiều màu sắc.', 1),
    ('SP013', 3, N'Ốp lưng OtterBox Symmetry', N'Thiết kế đơn giản nhưng bảo vệ hiệu quả, dễ dàng lắp đặt.', 1),
    ('SP014', 4, N'Ốp lưng Caseology Legion', N'Bảo vệ cực kỳ chắc chắn với thiết kế hiện đại, chống va đập cao.', 1),
    ('SP015', 5, N'Ốp lưng Ringke Air S', N'Ốp lưng siêu mỏng, nhẹ, giữ nguyên cảm giác cầm nắm tự nhiên.', 1);

-- Thêm dữ liệu vào bảng [chat_lieu]
INSERT INTO [chat_lieu] ([ma_chat_lieu], [ten], [trang_thai])
VALUES 
    ('CL001', N'Nhựa cứng (Polycarbonate)', 1),
    ('CL002', N'Nhựa dẻo (TPU - Thermoplastic Polyurethane)', 1),
    ('CL003', N'Silicone', 1),
    ('CL004', N'Da', 1),
    ('CL005', N'Kim loại (Aluminum)', 1);

-- Thêm dữ liệu vào bảng [mau_sac]
INSERT INTO [mau_sac] ([ma_mau_sac], [ten], [trang_thai])
VALUES 
    ('MS001', N'Đen', 1),
    ('MS002', N'Tráng', 1),
    ('MS003', N'Xanh dương', 1),
    ('MS004', N'Đỏ', 1),
    ('MS005', N'Hồng', 1);

-- Thêm dữ liệu vào bảng [giam_gia]
INSERT INTO [giam_gia] ([ma_giam_gia], [ten], [gia_tri], [ngay_bat_dau], [ngay_ket_thuc], [trang_thai])
VALUES 
    ('GG001', N'Giảm giá mùa hè', 10.0, '2024-06-01', '2024-06-30', 1),
    ('GG002', N'Black Friday', 20.0, '2024-11-25', '2024-11-30', 1),
    ('GG003', N'Giảm giá Tết', 15.0, '2024-01-01', '2024-01-10', 1),
    ('GG004', N'Ưu đãi đặc biệt cho thành viên', 5.0, '2024-03-01', '2024-03-31', 1),
    ('GG005', N'Khuyến mãi Giáng Sinh', 25.0, '2024-12-20', '2024-12-26', 1);

	-- Thêm dữ liệu vào bảng [chi_tiet_san_pham]
INSERT INTO [chi_tiet_san_pham] ([id_san_pham], [id_chat_lieu], [id_mau_sac], [id_giam_gia], [so_luong], [gia_nhap], [gia_ban], [ngay_nhap], [mo_ta], [trang_thai])
VALUES 
    (1, 1, 1, 1, 100, 150000, 165000, '2024-05-01', N'Ốp lưng Spigen Tough Armor - chất liệu nhựa cứng, màu đen.', 1),
    (2, 2, 3, NULL, 200, 200000, 220000, '2024-06-05', N'Ốp lưng UAG Monarch - chất liệu nhựa dẻo, màu xanh dương.', 1),
    (3, 3, 4, 2, 150, 250000, 275000, '2024-06-10', N'Ốp lưng OtterBox Defender - chất liệu silicone, màu đỏ.', 1),
    (4, 4, 2, NULL, 50, 300000, 320000, '2024-07-15', N'Ốp lưng Caseology Parallax - chất liệu da, màu trắng.', 1),
    (5, 5, 5, 3, 80, 180000, 200000, '2024-08-20', N'Ốp lưng Ringke Fusion X - chất liệu kim loại, màu hồng.', 1),
    (6, 1, 1, NULL, 120, 160000, 180000, '2024-05-02', N'Ốp lưng Spigen Liquid Air - chất liệu nhựa cứng, màu đen.', 1),
    (7, 2, 2, 4, 90, 210000, 230000, '2024-06-15', N'Ốp lưng UAG Plasma - chất liệu nhựa dẻo, màu trắng.', 1),
    (8, 3, 3, NULL, 60, 260000, 280000, '2024-07-25', N'Ốp lưng OtterBox Commuter - chất liệu silicone, màu xanh dương.', 1),
    (9, 4, 4, 5, 30, 310000, 330000, '2024-08-10', N'Ốp lưng Caseology Skyfall - chất liệu da, màu đỏ.', 1),
    (10, 5, 5, NULL, 40, 190000, 210000, '2024-09-05', N'Ốp lưng Ringke Onyx - chất liệu kim loại, màu hồng.', 1);

	-- Thêm dữ liệu vào bảng [anh_san_pham]
INSERT INTO [anh_san_pham] ([id_san_pham], [anh_1], [anh_2], [anh_3], [anh_4], [anh_5])
VALUES 
    (1, 'anh1_spigen_tough_armor.png', 'anh2_spigen_tough_armor.png', 'anh3_spigen_tough_armor.png', 'anh4_spigen_tough_armor.png', 'anh5_spigen_tough_armor.png'),
    (2, 'anh1_uag_monarch.png', 'anh2_uag_monarch.png', 'anh3_uag_monarch.png', 'anh4_uag_monarch.png', 'anh5_uag_monarch.png'),
    (3, 'anh1_otterbox_defender.png', 'anh2_otterbox_defender.png', 'anh3_otterbox_defender.png', 'anh4_otterbox_defender.png', 'anh5_otterbox_defender.png'),
    (4, 'anh1_caseology_parallax.png', 'anh2_caseology_parallax.png', 'anh3_caseology_parallax.png', 'anh4_caseology_parallax.png', 'anh5_caseology_parallax.png'),
    (5, 'anh1_ringke_fusion_x.png', 'anh2_ringke_fusion_x.png', 'anh3_ringke_fusion_x.png', 'anh4_ringke_fusion_x.png', 'anh5_ringke_fusion_x.png'),
    (6, 'anh1_spigen_liquid_air.png', 'anh2_spigen_liquid_air.png', 'anh3_spigen_liquid_air.png', 'anh4_spigen_liquid_air.png', 'anh5_spigen_liquid_air.png'),
    (7, 'anh1_uag_plasma.png', 'anh2_uag_plasma.png', 'anh3_uag_plasma.png', 'anh4_uag_plasma.png', 'anh5_uag_plasma.png'),
    (8, 'anh1_otterbox_commuter.png', 'anh2_otterbox_commuter.png', 'anh3_otterbox_commuter.png', 'anh4_otterbox_commuter.png', 'anh5_otterbox_commuter.png'),
    (9, 'anh1_caseology_skyfall.png', 'anh2_caseology_skyfall.png', 'anh3_caseology_skyfall.png', 'anh4_caseology_skyfall.png', 'anh5_caseology_skyfall.png'),
    (10, 'anh1_ringke_onyx.png', 'anh2_ringke_onyx.png', 'anh3_ringke_onyx.png', 'anh4_ringke_onyx.png', 'anh5_ringke_onyx.png');

	-- Thêm dữ liệu vào bảng [khach_hang]
INSERT INTO [khach_hang] ([ma_khach_hang], [tai_khoan], [ho_ten], [email], [sdt], [mat_khau], [ngay_sinh], [gioi_tinh], [trang_thai])
VALUES 
    (N'KH001', 'user1', N'Nguyễn Văn A', 'nguyenvana@example.com', '0123456789', 'password1', '1990-01-15', 1, 1),
    (N'KH002', 'user2', N'Trần Thị B', 'tranthib@example.com', '0123456780', 'password2', '1995-05-20', 0, 1),
    (N'KH003', 'user3', N'Lê Văn C', 'levanc@example.com', '0123456781', 'password3', '1992-03-10', 1, 1),
    (N'KH004', 'user4', N'Phạm Thị D', 'phamthid@example.com', '0123456782', 'password4', '1988-07-25', 0, 1),
    (N'KH005', 'user5', N'Nguyễn Thế E', 'nguyenthe@example.com', '0123456783', 'password5', '1996-11-30', 1, 1);

-- Thêm dữ liệu vào bảng [dia_chi_khach_hang]
INSERT INTO [dia_chi_khach_hang] ([id_khach_hang], [ma_dia_chi], [dia_chi_chi_tiet], [trang_thai])
VALUES 
    (1, N'DC001', N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM', 1),
    (1, N'DC002', N'Số 5, Đường XYZ, Phường 2, Quận 2, TP.HCM', 1),
    (2, N'DC003', N'Số 10, Đường DEF, Phường 3, Quận 3, TP.HCM', 1),
    (3, N'DC004', N'Số 15, Đường GHI, Phường 4, Quận 4, TP.HCM', 1),
    (4, N'DC005', N'Số 20, Đường JKL, Phường 5, Quận 5, TP.HCM', 1),
    (5, N'DC006', N'Số 25, Đường MNO, Phường 6, Quận 6, TP.HCM', 1);


	-- Thêm dữ liệu vào bảng [chuc_vu]
INSERT INTO [chuc_vu] ([ma_chuc_vu], [ten_chuc_vu], [trang_thai])
VALUES 
    (N'CV001', N'Quản lý', 1),
    (N'CV002', N'Nhân viên bán hàng', 1),
    (N'CV003', N'Nhân viên kho', 1),
    (N'CV004', N'Kế toán', 1),
    (N'CV005', N'Nhân viên kỹ thuật', 1),
    (N'CV006', N'Chăm sóc khách hàng', 1),
    (N'CV007', N'Trưởng phòng', 1),
    (N'CV008', N'Trợ lý', 1),
    (N'CV009', N'Giám đốc', 1),
    (N'CV010', N'Nhân viên Marketing', 1);

	-- Thêm dữ liệu vào bảng [nhan_vien]
INSERT INTO [nhan_vien] ([ma_nhan_vien], [ho_ten], [id_chuc_vu], [email], [sdt], [mat_khau], [ngay_sinh], [gioi_tinh], [hinh_anh], [trang_thai])
VALUES 
    (N'NV001', N'Nguyễn Văn A', 1, 'nguyenvana@example.com', '0123456789', 'password1', '1990-01-15', 1, 'hinh_anh_a.png', 1),
    (N'NV002', N'Trần Thị B', 2, 'tranthib@example.com', '0123456780', 'password2', '1992-05-20', 0, 'hinh_anh_b.png', 1),
    (N'NV003', N'Lê Văn C', 3, 'levanc@example.com', '0123456781', 'password3', '1988-03-10', 1, 'hinh_anh_c.png', 1),
    (N'NV004', N'Phạm Thị D', 4, 'phamthid@example.com', '0123456782', 'password4', '1995-07-25', 0, 'hinh_anh_d.png', 1),
    (N'NV005', N'Nguyễn Thế E', 5, 'nguyenthe@example.com', '0123456783', 'password5', '1996-11-30', 1, 'hinh_anh_e.png', 1),
    (N'NV006', N'Trần Văn F', 6, 'tranvanf@example.com', '0123456784', 'password6', '1991-02-15', 1, 'hinh_anh_f.png', 1),
    (N'NV007', N'Lê Thị G', 7, 'lethig@example.com', '0123456785', 'password7', '1993-04-20', 0, 'hinh_anh_g.png', 1),
    (N'NV008', N'Nguyễn Văn H', 8, 'nguyenh@example.com', '0123456786', 'password8', '1989-06-18', 1, 'hinh_anh_h.png', 1),
    (N'NV009', N'Phạm Văn I', 9, 'phamvi@example.com', '0123456787', 'password9', '1990-09-12', 1, 'hinh_anh_i.png', 1),
    (N'NV010', N'Trần Thị J', 10, 'tranthij@example.com', '0123456788', 'password10', '1995-12-28', 0, 'hinh_anh_j.png', 1);

-- Thêm dữ liệu vào bảng [dia_chi_nhan_vien]
INSERT INTO [dia_chi_nhan_vien] ([id_nhan_vien], [ma_dia_chi], [dia_chi], [trang_thai])
VALUES 
    (1, N'DCNV001', N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM', 1),
    (1, N'DCNV002', N'Số 3, Đường DEF, Phường 2, Quận 1, TP.HCM', 1),
    (2, N'DCNV003', N'Số 5, Đường GHI, Phường 1, Quận 3, TP.HCM', 1),
    (3, N'DCNV004', N'Số 7, Đường JKL, Phường 2, Quận 2, TP.HCM', 1),
    (4, N'DCNV005', N'Số 9, Đường MNO, Phường 1, Quận 4, TP.HCM', 1),
    (5, N'DCNV006', N'Số 11, Đường PQR, Phường 3, Quận 5, TP.HCM', 1),
    (6, N'DCNV007', N'Số 13, Đường STU, Phường 2, Quận 6, TP.HCM', 1),
    (7, N'DCNV008', N'Số 15, Đường VWX, Phường 1, Quận 7, TP.HCM', 1),
    (8, N'DCNV009', N'Số 17, Đường YZ, Phường 2, Quận 8, TP.HCM', 1),
    (9, N'DCNV010', N'Số 19, Đường ABCD, Phường 3, Quận 9, TP.HCM', 1);

	-- Thêm dữ liệu vào bảng [khuyen_mai]
INSERT INTO [khuyen_mai] ([ma_khuyen_mai], [gia_tri], [ngay_bat_dau], [ngay_ket_thuc], [ngay_tao], [trang_thai])
VALUES 
    (N'KM001', 10.0, '2024-01-01', '2024-01-15', '2023-12-01', 1),
    (N'KM002', 15.0, '2024-02-01', '2024-02-14', '2024-01-15', 1),
    (N'KM003', 20.0, '2024-03-01', '2024-03-31', '2024-02-20', 1),
    (N'KM004', 25.0, '2024-04-01', '2024-04-30', '2024-03-10', 1),
    (N'KM005', 30.0, '2024-05-01', '2024-05-31', '2024-04-20', 1);

	-- Thêm dữ liệu vào bảng [hoa_don]
INSERT INTO [hoa_don] ([ma_hoa_don], [id_khach_hang], [id_nhan_vien], [id_khuyen_mai], [ngay_tao], [tien_sau_giam_gia], [thanh_tien], [phuong_thuc_thanh_toan], [mo_ta], [trang_thai])
VALUES 
    (N'HDB001', 1, 1, 1, '2024-01-05', 150000, 165000, 1, N'Thanh toán bằng tiền mặt', 6),
    (N'HDB002', 2, 2, 2, '2024-02-10', 250000, 265000, 2, N'Thẻ tín dụng', 6),
    (N'HDB003', 3, 1, 3, '2024-03-15', 350000, 370000, 1, N'Thanh toán bằng tiền mặt', 6),
    (N'HDB004', 4, 3, 4, '2024-04-20', 450000, 475000, 2, N'Thẻ ghi nợ', 6),
    (N'HDB005', 5, 4, 5, '2024-05-25', 550000, 580000, 1, N'Thanh toán bằng chuyển khoản', 6);

	-- Thêm dữ liệu vào bảng [hoa_don_chi_tiet]
INSERT INTO [hoa_don_chi_tiet] ([ma_hoa_don_chi_tiet], [id_hoa_don], [id_chi_tiet_san_pham], [so_luong], [don_gia], [trang_thai])
VALUES 
    (N'HDBCT001', 1, 1, 2, 75000, 1),
    (N'HDBCT002', 1, 2, 1, 150000, 1),
    (N'HDBCT003', 1, 3, 3, 120000, 1),
    (N'HDBCT004', 2, 4, 1, 250000, 1),
    (N'HDBCT005', 2, 5, 2, 175000, 1),
    (N'HDBCT006', 3, 6, 1, 300000, 1),
    (N'HDBCT007', 3, 7, 2, 200000, 1),
    (N'HDBCT008', 3, 8, 1, 85000, 1),
    (N'HDBCT009', 4, 9, 5, 120000, 1),
    (N'HDBCT010', 4, 10, 2, 50000, 1),
    (N'HDBCT011', 5, 1, 1, 75000, 1),
    (N'HDBCT012', 5, 2, 4, 150000, 1),
    (N'HDBCT013', 5, 3, 1, 250000, 1),
    (N'HDBCT014', 1, 4, 2, 175000, 1),
    (N'HDBCT015', 2, 5, 3, 300000, 1),
    (N'HDBCT016', 2, 6, 1, 125000, 1),
    (N'HDBCT017', 2, 7, 2, 200000, 1),
    (N'HDBCT018', 2, 8, 1, 90000, 1),
    (N'HDBCT019', 3, 9, 4, 250000, 1),
    (N'HDBCT020', 4, 10, 1, 50000, 1);



