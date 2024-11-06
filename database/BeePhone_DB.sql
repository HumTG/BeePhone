﻿
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
  [id_chat_lieu] INT,
  [ten] Nvarchar(100),
  [mo_ta] Nvarchar(500),
  [trang_thai] INT,
  PRIMARY KEY ([id]),
  CONSTRAINT [FK_san_pham.id_nha_san_xuat]
    FOREIGN KEY ([id_nha_san_xuat])
      REFERENCES [nha_san_xuat]([id]),
  CONSTRAINT [FK_san_pham.id_chat_lieu]
    FOREIGN KEY ([id_chat_lieu])
      REFERENCES [chat_lieu]([id])
);

CREATE TABLE [kich_co] (
  [id] INT IDENTITY,
  [ma_kich_co] Varchar(50),
  [ten] Nvarchar(100),
  [trang_thai] INT,
  PRIMARY KEY ([id])
);

CREATE TABLE [chi_tiet_san_pham] (
  [id] INT IDENTITY,
  [id_san_pham] INT,
  [id_mau_sac] INT,
  [id_kich_co] INT,
  [id_giam_gia] INT,
  [so_luong] INT,
  [gia_nhap] Decimal,
  [gia_ban] Decimal,
  [ngay_nhap] Date,
  [mo_ta] Nvarchar(500),
  [anh] Nvarchar(200),
  [trang_thai] INT,
  PRIMARY KEY ([id]),
  CONSTRAINT [FK_chi_tiet_san_pham.id_giam_gia]
    FOREIGN KEY ([id_giam_gia])
      REFERENCES [giam_gia]([id]),
  CONSTRAINT [FK_chi_tiet_san_pham.id_mau_sac]
    FOREIGN KEY ([id_mau_sac])
      REFERENCES [mau_sac]([id]),
  CONSTRAINT [FK_chi_tiet_san_pham.id_san_pham]
    FOREIGN KEY ([id_san_pham])
      REFERENCES [san_pham]([id]),
  CONSTRAINT [FK_chi_tiet_san_pham.id_kich_co]
    FOREIGN KEY ([id_kich_co])
      REFERENCES [kich_co]([id])
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
  [dia_chi] Nvarchar(100),
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
  [gia_tri_toi_thieu] Decimal,
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
  [loai_hoa_don] INT,
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

INSERT INTO [kich_co] ([ma_kich_co], [ten], [trang_thai]) VALUES
('IP001', 'iPhone 12 Mini', 1),
('IP002', 'iPhone 12', 1),
('IP003', 'iPhone 12 Pro', 1),
('IP004', 'iPhone 12 Pro Max', 1),
('IP005', 'iPhone 13 Mini', 1),
('IP006', 'iPhone 13', 1),
('IP007', 'iPhone 13 Pro', 1),
('IP008', 'iPhone 13 Pro Max', 1),
('IP009', 'iPhone 14', 1),
('IP010', 'iPhone 14 Plus', 1),
('IP011', 'iPhone 14 Pro', 1),
('IP012', 'iPhone 14 Pro Max', 1),
('IP013', 'iPhone 15', 1),
('IP014', 'iPhone 15 Plus', 1),
('IP015', 'iPhone 15 Pro Max', 1);


INSERT INTO [nha_san_xuat] ([ma_nha_san_xuat], [ten], [trang_thai])
VALUES 
    ('NSX001', N'Spigen', 1),
    ('NSX002', N'UAG (Urban Armor Gear)', 1),
    ('NSX003', N'OtterBox', 1),
    ('NSX004', N'Caseology', 1),
    ('NSX005', N'Ringke', 1);

-- Thêm dữ liệu vào bảng [chat_lieu]
INSERT INTO [chat_lieu] ([ma_chat_lieu], [ten], [trang_thai])
VALUES 
    ('CL001', N'Nhựa cứng (Polycarbonate)', 1),
    ('CL002', N'Nhựa dẻo (TPU - Thermoplastic Polyurethane)', 1),
    ('CL003', N'Silicone', 1),
    ('CL004', N'Da', 1),
    ('CL005', N'Kim loại (Aluminum)', 1);

INSERT INTO [san_pham] ([ma_san_pham], [id_nha_san_xuat],[id_chat_lieu], [ten], [mo_ta], [trang_thai])
VALUES 
    ('SP001', 1,2, N'Ốp lưng Spigen Tough Armor', N'Ốp lưng bảo vệ tối ưu với thiết kế hai lớp chống sốc.', 1),
    ('SP002', 2,1, N'Ốp lưng UAG Monarch', N'Ốp lưng siêu bền đạt tiêu chuẩn quân đội với thiết kế mạnh mẽ.', 1),
    ('SP003', 3,2, N'Ốp lưng OtterBox Defender', N'Ốp lưng nhiều lớp bảo vệ tối đa, chống va đập hiệu quả.', 1),
    ('SP004', 4,2, N'Ốp lưng Caseology Parallax', N'Thiết kế đẹp mắt với lớp chống sốc và họa tiết 3D nổi bật.', 1),
    ('SP005', 5,3, N'Ốp lưng Ringke Fusion X', N'Ốp lưng trong suốt với khung bảo vệ chắc chắn, chống trầy xước.', 1),
	('SP006', 1,4, N'Ốp lưng Spigen Liquid Air', N'Ốp lưng mỏng nhẹ, chống trượt và bảo vệ tốt.', 1),
    ('SP007', 2,4, N'Ốp lưng UAG Plasma', N'Ốp lưng nhẹ nhưng cực kỳ bền, bảo vệ chống sốc hiệu quả.', 1),
    ('SP008', 3,5, N'Ốp lưng OtterBox Commuter', N'Thiết kế hai lớp bảo vệ và mỏng gọn cho túi xách.', 1),
    ('SP009', 4,1,N'Ốp lưng Caseology Skyfall', N'Ốp lưng trong suốt, bảo vệ chắc chắn và giữ nguyên màu sắc điện thoại.', 1),
    ('SP010', 5,1,N'Ốp lưng Ringke Onyx', N'Ốp lưng với họa tiết nổi chống trượt và chống sốc.', 1),
    ('SP011', 1,2,N'Ốp lưng Spigen Rugged Armor', N'Ốp lưng kiểu dáng mạnh mẽ, bảo vệ chống va đập với họa tiết carbon.', 1),
    ('SP012', 2,5,N'Ốp lưng UAG Pathfinder', N'Thiết kế độc đáo, siêu bền, chống va đập với nhiều màu sắc.', 1),
    ('SP013', 3,4,N'Ốp lưng OtterBox Symmetry', N'Thiết kế đơn giản nhưng bảo vệ hiệu quả, dễ dàng lắp đặt.', 1),
    ('SP014', 4,2,N'Ốp lưng Caseology Legion', N'Bảo vệ cực kỳ chắc chắn với thiết kế hiện đại, chống va đập cao.', 1),
    ('SP015', 5,1,N'Ốp lưng Ringke Air S', N'Ốp lưng siêu mỏng, nhẹ, giữ nguyên cảm giác cầm nắm tự nhiên.', 1);


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
INSERT INTO [chi_tiet_san_pham] 
([id_san_pham], [id_mau_sac], [id_kich_co], [id_giam_gia], [so_luong], [gia_nhap], [gia_ban], [ngay_nhap], [mo_ta],[anh], [trang_thai]) 
VALUES
(4, 5, 5, NULL, 64, 765742.86, 2226966.65, '2024-05-07', 'Sản phẩm chất lượng cao','anh_7.jpg' ,1),
(2, 2, 4, NULL, 60, 956303.13, 2661107.10, '2024-08-31', 'Sản phẩm chất lượng cao','anh_3.webp' ,1),
(2, 5, 4, NULL, 21, 644177.33, 2410053.16, '2023-12-02', 'Sản phẩm chất lượng cao','anh_4.webp' ,1),
(10, 5, 4, NULL, 91, 1022651.54, 1901921.26, '2024-05-15', 'Sản phẩm chất lượng cao','anh_6.jpg' ,1),
(3, 1, 4, NULL, 43, 1280756.41, 1864571.13, '2024-08-14', 'Sản phẩm chất lượng cao', 'anh_2.jpg' ,1),
(9, 2, 2, NULL, 98, 768131.29, 2222554.46, '2024-03-12', 'Sản phẩm chất lượng cao','anh_8.jpg' ,1),
(1, 3, 2, NULL, 28, 1376167.97, 1993608.21, '2024-01-08', 'Sản phẩm chất lượng cao','anh_5.jpg' ,1),
(7, 1, 1, NULL, 66, 563925.69, 2272021.49, '2023-11-16', 'Sản phẩm chất lượng cao','anh_9.jpg' ,1),
(8, 4, 1, NULL, 50, 957542.31, 2984578.36, '2024-09-02', 'Sản phẩm chất lượng cao','anh_1.jpg' ,1),
(5, 3, 3, NULL, 12, 1492034.48, 2723629.74, '2024-10-26', 'Sản phẩm chất lượng cao','anh_10.jpg' ,1);




	-- Thêm dữ liệu vào bảng [khach_hang]
INSERT INTO [khach_hang] ([ma_khach_hang], [tai_khoan], [ho_ten], [email], [sdt], [mat_khau], [ngay_sinh], [gioi_tinh], [trang_thai])
VALUES 
    (N'KH001', 'user1', N'Nguyễn Văn A', 'nguyenvana@example.com', '0123456789', 'password1', '1990-01-15', 1, 1),
    (N'KH002', 'user2', N'Trần Thị B', 'tranthib@example.com', '0123456780', 'password2', '1995-05-20', 0, 1),
    (N'KH003', 'user3', N'Lê Văn C', 'levanc@example.com', '0123456781', 'password3', '1992-03-10', 1, 1),
    (N'KH004', 'user4', N'Phạm Thị D', 'phamthid@example.com', '0123456782', 'password4', '1988-07-25', 0, 1),
    (N'KH005', 'user5', N'Nguyễn Thế E', 'nguyenthe@example.com', '0123456783', 'password5', '1996-11-30', 1, 1),
	(N'KH006', 'user6', N'Trần Văn F', 'tranvanf@example.com', '0123456784', 'password6', '1993-02-18', 1, 1),
    (N'KH007', 'user7', N'Lê Thị G', 'lethig@example.com', '0123456785', 'password7', '1990-08-12', 0, 1),
    (N'KH008', 'user8', N'Phạm Văn H', 'phamvanh@example.com', '0123456786', 'password8', '1991-12-05', 1, 1),
    (N'KH009', 'user9', N'Hoàng Thị I', 'hoangthii@example.com', '0123456787', 'password9', '1989-04-22', 0, 1),
    (N'KH010', 'user10', N'Vũ Văn J', 'vuvanj@example.com', '0123456788', 'password10', '1997-09-15', 1, 1),
    (N'KH011', 'user11', N'Nguyễn Văn K', 'nguyenvank@example.com', '0123456789', 'password11', '1985-06-07', 1, 1),
    (N'KH012', 'user12', N'Đỗ Thị L', 'dothil@example.com', '0123456790', 'password12', '1994-11-25', 0, 1),
    (N'KH013', 'user13', N'Phan Văn M', 'phanvanm@example.com', '0123456791', 'password13', '1992-03-18', 1, 1),
    (N'KH014', 'user14', N'Trịnh Thị N', 'trinhtin@example.com', '0123456792', 'password14', '1998-02-21', 0, 1),
    (N'KH015', 'user15', N'Bùi Văn O', 'buivano@example.com', '0123456793', 'password15', '1991-10-10', 1, 1);

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
INSERT INTO [nhan_vien] ([ma_nhan_vien], [ho_ten], [id_chuc_vu], [email], [sdt], [mat_khau], [ngay_sinh], [gioi_tinh], [hinh_anh],[dia_chi],[trang_thai])
VALUES 
    (N'NV001', N'Nguyễn Văn A', 1, 'nguyenvana@example.com', '0123456789', 'password1', '1990-01-15', 1, 'anh_17.jpg',N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM', 1),
    (N'NV002', N'Trần Thị B', 2, 'tranthib@example.com', '0123456780', 'password2', '1992-05-20', 0, 'anh_17.jpg', N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM',1),
    (N'NV003', N'Lê Văn C', 3, 'levanc@example.com', '0123456781', 'password3', '1988-03-10', 1, 'anh_17.jpg',N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM' ,1),
    (N'NV004', N'Phạm Thị D', 4, 'phamthid@example.com', '0123456782', 'password4', '1995-07-25', 0, 'anh_17.jpg', N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM',1),
    (N'NV005', N'Nguyễn Thế E', 5, 'nguyenthe@example.com', '0123456783', 'password5', '1996-11-30', 1, 'anh_17.jpg', N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM',1),
    (N'NV006', N'Trần Văn F', 6, 'tranvanf@example.com', '0123456784', 'password6', '1991-02-15', 1, 'anh_17.jpg', N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM',1),
    (N'NV007', N'Lê Thị G', 7, 'lethig@example.com', '0123456785', 'password7', '1993-04-20', 0, 'hanh_17.jpg', N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM',1),
    (N'NV008', N'Nguyễn Văn H', 8, 'nguyenh@example.com', '0123456786', 'password8', '1989-06-18', 1, 'anh_17.jpg',N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM' ,1),
    (N'NV009', N'Phạm Văn I', 9, 'phamvi@example.com', '0123456787', 'password9', '1990-09-12', 1, 'anh_18.jpg', N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM',1),
    (N'NV010', N'Trần Thị J', 10, 'tranthij@example.com', '0123456788', 'password10', '1995-12-28', 0, 'anh_20.jpg',N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM' ,1),
	('NV011', 'Nguyen Van A', 1, 'nguyenvana@email.com', '0123456789', 'password1', '1990-01-01', 1, 'anh_19.jpg',N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM' ,1),
	('NV012', 'Nguyen Van B', 2, 'nguyenvanb@email.com', '0123456788', 'password2', '1990-02-02', 1, 'anh_18.jpg',N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM' ,1),
	('NV013', 'Nguyen Van C', 3, 'nguyenvanc@email.com', '0123456787', 'password3', '1990-03-03', 1, 'anh_17.jpg', N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM',1),
	('NV014', 'Nguyen Van D', 1, 'nguyenvand@email.com', '0123456786', 'password4', '1990-04-04', 1, 'anh_16.jpg', N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM',1),
	('NV015', 'Nguyen Van E', 2, 'nguyenvane@email.com', '0123456785', 'password5', '1990-05-05', 1, 'anh_15.jpg', N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM',1),
	('NV016', 'Nguyen Van F', 3, 'nguyenvanf@email.com', '0123456784', 'password6', '1990-06-06', 1, 'anh_14.jpeg', N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM',1),
	('NV017', 'Nguyen Van G', 1, 'nguyenvang@email.com', '0123456783', 'password7', '1990-07-07', 1, 'anh_14.jpeg',N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM' ,1),
	('NV018', 'Nguyen Van H', 2, 'nguyenvanh@email.com', '0123456782', 'password8', '1990-08-08', 1, 'anh_13.jpg',N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM' ,1),
	('NV019', 'Nguyen Van I', 3, 'nguyenvani@email.com', '0123456781', 'password9', '1990-09-09', 1, 'anh_12.jpg',N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM' ,1),
	('NV020', 'Nguyen Van J', 1, 'nguyenvanj@email.com', '0123456780', 'password10', '1990-10-10', 1, 'anh_11.jpg', N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM',1),
	('NV021', 'Nguyen Van K', 2, 'nguyenvank@email.com', '0123456779', 'password11', '1990-11-11', 1, 'anh_10.jpg', N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM',1),
	('NV022', 'Nguyen Van L', 3, 'nguyenvanl@email.com', '0123456778', 'password12', '1990-12-12', 1, 'anh_9.jpg',N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM' ,1),
	('NV023', 'Nguyen Van M', 1, 'nguyenvanm@email.com', '0123456777', 'password13', '1991-01-01', 1, 'anh_8.jpg',N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM' ,1),
	('NV024', 'Nguyen Van N', 2, 'nguyenvann@email.com', '0123456776', 'password14', '1991-02-02', 1, 'anh_7.webp',N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM' ,1),
	('NV025', 'Nguyen Van O', 3, 'nguyenvano@email.com', '0123456775', 'password15', '1991-03-03', 1, 'anh_6.jpg',N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM' ,1),
	('NV026', 'Nguyen Van P', 1, 'nguyenvanp@email.com', '0123456774', 'password16', '1991-04-04', 1, 'anh_5.webp',N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM' ,1),
	('NV027', 'Nguyen Van Q', 2, 'nguyenvanq@email.com', '0123456773', 'password17', '1991-05-05', 1, 'anh_4.jpg',N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM' ,1),
	('NV028', 'Nguyen Van R', 3, 'nguyenvanr@email.com', '0123456772', 'password18', '1991-06-06', 1, 'anh_3.jpg',N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM' ,1),
	('NV029', 'Nguyen Van S', 1, 'nguyenvans@email.com', '0123456771', 'password19', '1991-07-07', 1, 'anh_2.webp', N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM',1),
	('NV030', 'Nguyen Van T', 2, 'nguyenvant@email.com', '0123456770', 'password20', '1991-08-08', 1, 'img_1.jpg',N'Số 1, Đường ABC, Phường 1, Quận 1, TP.HCM' ,1);



	-- Thêm dữ liệu vào bảng [khuyen_mai]
INSERT INTO khuyen_mai (ma_khuyen_mai, gia_tri, gia_tri_toi_thieu, ngay_bat_dau, ngay_ket_thuc, ngay_tao, trang_thai)
VALUES 
('KM001', 0.10, 500.00, '2024-01-01', '2024-01-31', '2024-01-01', 1),
('KM002', 0.15, 1000.00, '2024-02-01', '2024-02-28', '2024-01-15', 1),
('KM003', 0.20, 2000.00, '2024-03-01', '2024-03-31', '2024-02-01', 1),
('KM004', 0.05, 300.00, '2024-04-01', '2024-04-30', '2024-03-01', 1),
('KM005', 0.25, 1500.00, '2024-05-01', '2024-05-31', '2024-04-01', 1),
('KM006', 0.30, 2500.00, '2024-06-01', '2024-06-30', '2024-05-01', 1),
('KM007', 0.10, 700.00, '2024-07-01', '2024-07-31', '2024-06-01', 1),
('KM008', 0.15, 800.00, '2024-08-01', '2024-08-31', '2024-07-01', 1),
('KM009', 0.10, 1000.00, '2024-09-01', '2024-09-30', '2024-08-01', 1),
('KM010', 0.05, 300.00, '2024-10-01', '2024-10-31', '2024-09-01', 1),
('KM011', 0.20, 2000.00, '2024-11-01', '2024-11-30', '2024-10-01', 1),
('KM012', 0.25, 2500.00, '2024-12-01', '2024-12-31', '2024-11-01', 1),
('KM013', 0.30, 3000.00, '2024-12-01', '2024-12-31', '2024-11-01', 1),
('KM014', 0.15, 1500.00, '2024-12-15', '2025-01-15', '2024-12-01', 1),
('KM015', 0.05, 500.00, '2025-01-01', '2025-01-31', '2024-12-15', 1);


	-- Thêm dữ liệu vào bảng [hoa_don]
	INSERT INTO hoa_don (ma_hoa_don, id_khach_hang, id_nhan_vien, id_khuyen_mai, ngay_tao, tien_sau_giam_gia, thanh_tien, phuong_thuc_thanh_toan, loai_hoa_don, mo_ta, trang_thai)
	VALUES 
	('HD001', 1, 1, 1, '2024-10-01', 950.00, 1000.00, 1, 1, N'Hóa đơn bán hàng', 1),
	('HD002', 2, 1, 2, '2024-10-02', 900.00, 1000.00, 1, 1, N'Hóa đơn bán hàng', 1),
	('HD003', 3, 2, NULL, '2024-10-03', 1200.00, 1200.00, 2, 1, N'Hóa đơn mua hàng', 2),
	('HD004', 4, 2, 3, '2024-10-04', 800.00, 1000.00, 2, 2, N'Hóa đơn dịch vụ', 1),
	('HD005', 5, 3, 1, '2024-10-05', 1500.00, 2000.00, 1, 1, N'Hóa đơn bán hàng', 1),
	('HD006', 6, 3, NULL, '2024-10-06', 500.00, 500.00, 1, 1, N'Hóa đơn bán lẻ', 1),
	('HD007', 7, 4, 2, '2024-10-07', 1800.00, 2000.00, 1, 1, N'Hóa đơn bán sỉ', 1),
	('HD008', 8, 4, 3, '2024-10-08', 750.00, 1000.00, 2, 2, N'Hóa đơn dịch vụ', 1),
	('HD009', 9, 5, NULL, '2024-10-09', 2200.00, 2500.00, 1, 1, N'Hóa đơn đặc biệt', 1),
	('HD010', 10, 5, 1, '2024-10-10', 1000.00, 1200.00, 2, 1, N'Hóa đơn bán hàng', 2);


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