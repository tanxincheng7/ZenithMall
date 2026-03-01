-- ZenithMall Test Data
-- 测试数据初始化脚本

-- ============================================
-- 用户测试数据
-- 密码均为: 123456 (BCrypt加密后)
-- ============================================
INSERT INTO `user` (`id`, `username`, `password`, `phone`, `email`, `status`) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '13800138001', 'admin@zenith.com', 1),
(2, 'user01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '13800138002', 'user01@example.com', 1),
(3, 'user02', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '13800138003', 'user02@example.com', 1);

-- ============================================
-- 商品分类测试数据
-- ============================================
INSERT INTO `category` (`id`, `name`, `parent_id`, `sort`) VALUES
-- 顶级分类
(1, '电子产品', 0, 1),
(2, '服装鞋帽', 0, 2),
(3, '食品饮料', 0, 3),
(4, '家居用品', 0, 4),
-- 二级分类（电子产品）
(11, '手机', 1, 1),
(12, '电脑', 1, 2),
(13, '数码配件', 1, 3),
-- 二级分类（服装鞋帽）
(21, '男装', 2, 1),
(22, '女装', 2, 2),
(23, '鞋靴', 2, 3),
-- 二级分类（食品饮料）
(31, '零食', 3, 1),
(32, '饮料', 3, 2),
(33, '生鲜', 3, 3);

-- ============================================
-- 商品测试数据
-- ============================================
INSERT INTO `product` (`id`, `category_id`, `name`, `description`, `price`, `stock`, `main_image`, `status`) VALUES
-- 手机
(1, 11, 'iPhone 15 Pro', '苹果最新旗舰手机，A17 Pro芯片，钛金属边框', 7999.00, 100, 'https://example.com/images/iphone15pro.jpg', 1),
(2, 11, '华为 Mate 60 Pro', '华为旗舰手机，卫星通信，昆仑玻璃', 6999.00, 150, 'https://example.com/images/mate60pro.jpg', 1),
(3, 11, '小米14 Ultra', '徕卡光学镜头，骁龙8 Gen3', 5999.00, 200, 'https://example.com/images/mi14ultra.jpg', 1),
-- 电脑
(4, 12, 'MacBook Pro 14"', 'M3 Pro芯片，16GB内存，512GB存储', 15999.00, 50, 'https://example.com/images/macbookpro14.jpg', 1),
(5, 12, '联想拯救者 Y9000P', 'i9-14900HX，RTX4060，16GB内存', 9999.00, 80, 'https://example.com/images/lenovo-y9000p.jpg', 1),
-- 数码配件
(6, 13, 'AirPods Pro 2', '主动降噪，空间音频，USB-C充电盒', 1899.00, 300, 'https://example.com/images/airpodspro2.jpg', 1),
(7, 13, '罗技 MX Master 3S', '无线鼠标，8K DPI，静音滚轮', 699.00, 500, 'https://example.com/images/mxmaster3s.jpg', 1),
-- 男装
(8, 21, '优衣库 男士纯棉T恤', '100%纯棉，舒适透气，多色可选', 99.00, 1000, 'https://example.com/images/uniqlo-tshirt.jpg', 1),
(9, 21, ' Levi''s 501 牛仔裤', '经典直筒，水洗蓝，舒适耐穿', 499.00, 500, 'https://example.com/images/levis-501.jpg', 1),
-- 女装
(10, 22, 'ZARA 连衣裙', '时尚设计，优雅大方，夏季新品', 299.00, 300, 'https://example.com/images/zara-dress.jpg', 1),
(11, 22, 'ONLY 针织衫', '柔软舒适，简约百搭', 399.00, 400, 'https://example.com/images/only-sweater.jpg', 1),
-- 零食
(12, 31, '三只松鼠 坚果礼包', '混合坚果，1680g，健康美味', 129.00, 2000, 'https://example.com/images/nuts-gift.jpg', 1),
(13, 31, '良品铺子 肉脯', '猪肉脯，200g，独立包装', 39.90, 3000, 'https://example.com/images/jerky.jpg', 1),
-- 饮料
(14, 32, '可口可乐 330ml*24', '经典可乐，整箱装', 68.00, 5000, 'https://example.com/images/coca-cola.jpg', 1),
(15, 32, '农夫山泉 550ml*24', '天然饮用水，整箱装', 36.00, 8000, 'https://example.com/images/nongfu-spring.jpg', 1);

-- ============================================
-- 购物车测试数据
-- ============================================
INSERT INTO `cart` (`user_id`, `product_id`, `quantity`, `selected`) VALUES
-- user01的购物车
(2, 1, 1, 1),
(2, 6, 2, 1),
(2, 12, 3, 0),
-- user02的购物车
(3, 2, 1, 1),
(3, 8, 2, 1),
(3, 14, 1, 1);

-- ============================================
-- 订单测试数据
-- ============================================
INSERT INTO `order` (`id`, `order_no`, `user_id`, `total_amount`, `status`, `payment_time`) VALUES
(1, 'ORD20240301001', 2, 11797.00, 1, '2024-03-01 10:30:00'),
(2, 'ORD20240301002', 2, 79.80, 3, '2024-03-01 11:15:00'),
(3, 'ORD20240301003', 3, 6999.00, 1, '2024-03-01 14:20:00'),
(4, 'ORD20240301004', 3, 198.00, 0, NULL);

-- ============================================
-- 订单项测试数据
-- ============================================
INSERT INTO `order_item` (`order_id`, `product_id`, `product_name`, `product_image`, `unit_price`, `quantity`, `total_price`) VALUES
-- 订单1的订单项
(1, 1, 'iPhone 15 Pro', 'https://example.com/images/iphone15pro.jpg', 7999.00, 1, 7999.00),
(1, 6, 'AirPods Pro 2', 'https://example.com/images/airpodspro2.jpg', 1899.00, 2, 3798.00),
-- 订单2的订单项
(2, 13, '良品铺子 肉脯', 'https://example.com/images/jerky.jpg', 39.90, 2, 79.80),
-- 订单3的订单项
(3, 2, '华为 Mate 60 Pro', 'https://example.com/images/mate60pro.jpg', 6999.00, 1, 6999.00),
-- 订单4的订单项
(4, 8, '优衣库 男士纯棉T恤', 'https://example.com/images/uniqlo-tshirt.jpg', 99.00, 2, 198.00);

-- ============================================
-- 支付记录测试数据
-- ============================================
INSERT INTO `payment` (`order_id`, `pay_no`, `pay_amount`, `pay_status`, `pay_time`) VALUES
(1, 'PAY20240301001', 11797.00, 1, '2024-03-01 10:30:15'),
(2, 'PAY20240301002', 79.80, 1, '2024-03-01 11:15:30'),
(3, 'PAY20240301003', 6999.00, 1, '2024-03-01 14:20:45'),
(4, 'PAY20240301004', 198.00, 0, NULL);
