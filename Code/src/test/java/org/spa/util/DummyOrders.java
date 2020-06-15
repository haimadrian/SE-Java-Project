package org.spa.util;

import org.spa.controller.SPAApplication;
import org.spa.controller.item.Item;
import org.spa.controller.order.Order;
import org.spa.controller.order.OrderSystem;
import org.spa.model.ItemImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DummyOrders {

    public static void fillInDummyData(boolean shouldClearBefore) {
        OrderSystem orderSystem = SPAApplication.getInstance().getOrderSystem();
        List<Item> items1 = new ArrayList<>();
        List<Item> items2 = new ArrayList<>();
        List<Item> items3 = new ArrayList<>();
        if (shouldClearBefore) {
            Map<String, Order> ordersMap = orderSystem.getOrdersMap();
            ordersMap.values().forEach(order -> orderSystem.deleteOrder(order.getOrderId()));
        }

        int idCounter = 1;
        items1.add(new ItemImpl("" + idCounter++, "CPU",
                "Intel Core i9-9900K Coffee Lake 8-Core, 16-Thread, 95W BX80684I99900K Desktop Processor",
                "9th Gen Intel Processor\n" +
                        "Intel UHD Graphics 630\n" +
                        "Only Compatible with Intel 300 Series Motherboard\n" +
                        "Socket LGA 1151 (300 Series)\n" +
                        "3.6 GHz Max Turbo Frequency 5.0 GHz\n" +
                        "Unlocked Processor\n" +
                        "DDR4 Support\n" +
                        "Intel Optane Memory and SSD Supported\n" +
                        "Cooling device not included - Processor Only\n" +
                        "Intel Turbo Boost Technology 2.0 and Intel vPro technology offer pro-level performance for gaming, creating, and overall productivity",
                600,
                12,
                0,
                10));

        items2.add(new ItemImpl("" + idCounter++, "CPU",
                "ASUS ROG STRIX Z490-F GAMING LGA 1200 (Intel 10th Gen)",
                "Intel Z490 SATA 6Gb/s ATX Intel Motherboard (16 Power Stages, DDR4 4600, Intel 2.5Gb Ethernet, USB 3.2 Front Panel Type-C, Dual M.2 and AURA Sync)\n" +
                        "Intel LGA 1200 socket: Designed to unleash the maximum performance of 10th Gen Intel Core processors\n" +
                        "Robust Power Solution: 14+2 Dr. MOS power stages with ProCool II power connector, high-quality alloy chokes and durable capacitors to provide reliable power even when push the CPU performance to the limit\n" +
                        "Optimized Thermal Design: Except comprehensive heatsink, heatpipe and fan headers, features low-noise AI cooling to balance thermals and acoustics by reducing fan speeds and maintaining a 5 Celsius delta\n" +
                        "High-performance Gaming Networking: On-board Intel 2.5Gb Ethernet with ASUS LANGuard\n" +
                        "Best Gaming Connectivity: Supports HDMI 1.4 and DisplayPort 1.4 output, and featuring dual M.2, front panel USB 3.2 Gen 2 Type-C connector\n" +
                        "Industry-leading Gaming Audio: High fidelity audio with the SupremeFX S1220A codec, DTS Sound Unbound and Sonic Studio III draws you deeper into the game action\n" +
                        "Unmatched Personalization",
                299,
                10,
                2,
                5));

        items2.add(new ItemImpl("" + idCounter++, "SSD",
                "Crucial MX500 2.5\" 1TB SATA III 3D NAND Internal Solid State Drive (SSD) CT1000MX500SSD1",
                "Sequential reads/writes up to 560/510 MB/s and random reads/writes up to 95k/90k on all file types\n" +
                        "Accelerated by Micron 3D NAND technology\n" +
                        "Integrated Power Loss Immunity preserves all your saved work",
                114.9,
                17,
                2,
                1));

        items3.add(new ItemImpl("" + idCounter++, "GPU",
                "EVGA GeForce RTX 2060 KO ULTRA GAMING Video Card, 06G-P4-2068-KR, 6GB GDDR6, Dual Fans, Metal Backplate",
                "Real Boost Clock: 1755 MHz; Memory Detail: 6144MB GDDR6\n" +
                        "Real-Time RAY TRACING in games for cutting-edge, hyper-realistic graphics\n" +
                        "Dual Fans offer higher performance cooling and low acoustic noise\n" +
                        "Built for EVGA Precision X1 + All-Metal Backplate, Pre-Installed",
                374,
                15,
                0,
                20));

        items3.add(new ItemImpl("" + idCounter++, "GPU",
                "EVGA GeForce RTX 2080 Ti GAMING Video Card, 11G-P4-2380-KR, 11GB GDDR6, RGB LED Logo, Metal Backplate",
                "Real Boost Clock: 1545 MHz; Memory Detail: 11264MB GDDR6\n" +
                        "Real-Time RAY TRACING in games for cutting-edge, hyper-realistic graphics\n" +
                        "Blower fan design allows for maximum cooling in itx and small systems\n" +
                        "Adjustable RGB",
                1299.99,
                10,
                0,
                6));

        items3.add(new ItemImpl("" + idCounter++, "GPU",
                "MSI GeForce RTX 2080 TI GAMING X TRIO Video Card",
                "11GB 352-Bit GDDR6\n" +
                        "Core Clock 1350 MHz\n" +
                        "Boost Clock 1755 MHz\n" +
                        "1 x HDMI 2.0b 3 x DisplayPort 1.4\n" +
                        "4352 CUDA Cores\n" +
                        "PCI Express 3.0 x16",
                1249.99,
                10,
                0,
                2));


        orderSystem.createOrder("Idan", items1);
        orderSystem.createOrder("Lior", items1);
        orderSystem.createOrder("Haim", items2);
        orderSystem.createOrder("David", items3);
        orderSystem.createOrder("Ben", items2);
    }
}