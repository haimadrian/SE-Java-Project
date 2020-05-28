package org.spa.model.dal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.spa.common.Repository;
import org.spa.common.SPAApplication;
import org.spa.common.util.JsonUtils;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.model.Item;
import org.spa.model.Order;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Haim Adrian
 * @since 28-May-20
 */
public class OrderRepository implements Repository<Order> {
    private static final File FILE = new File(new File(SPAApplication.getWorkingDirectory(), "Repository"), "Orders.json");
    private static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);

    private final Map<String, Order> orders = new HashMap<>();

    public OrderRepository() {
        FILE.getParentFile().mkdirs();
    }

    @Override
    public List<Order> selectAll() {
        if (orders.isEmpty()) {
            if (FILE.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
                    OrdersList ordersFromFile = JsonUtils.readValue(reader, OrdersList.class);
                    if (ordersFromFile != null) {
                        ordersFromFile.getOrders().forEach(order -> orders.put(order.getOrderId(), order));
                    }
                } catch (Exception e) {
                    logger.error("Error has occurred while reading orders from file.", e);
                }
            }

            if (orders.isEmpty()) {
                // If there is no order, generate some to play with
                generateDummyData();
            }
        }

        return new ArrayList<>(orders.values());
    }

    @Override
    public List<Order> select(Predicate<Order> filter) {
        return orders.values().stream().filter(filter).collect(Collectors.toList());
    }

    @Override
    public Order create(Order order) {
        orders.put(order.getOrderId(), new Order(order));
        return order;
    }

    @Override
    public Order update(Order order) {
        orders.put(order.getOrderId(), new Order(order));
        return order;
    }

    @Override
    public Order delete(Order order) {
        return orders.remove(order.getOrderId());
    }

    @Override
    public void saveAll(Iterable<Order> orders) {
        orders.forEach(this::update);

        // Remove the dummy values so we will not store them to disk.
        this.orders.remove("#11111");
        this.orders.remove("#22222");

        if (!this.orders.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
                JsonUtils.writeValue(writer, new OrdersList(new ArrayList<>(this.orders.values())));
            } catch (Exception e) {
                logger.error("Error has occurred while writing orders to file.", e);
            }
        }
    }

    public static class OrdersList {
        @JsonProperty
        private ArrayList<Order> orders;

        public OrdersList() {
            orders = new ArrayList<>();
        }

        @JsonCreator
        public OrdersList(@JsonProperty(value = "orders") ArrayList<Order> orders) {
            this.orders = orders;
        }

        public List<Order> getOrders() {
            return orders;
        }
    }

    private void generateDummyData() {
        int idCounter = 1;
        List<Item> dummy = new ArrayList<>();
        dummy.add(new Item("" + idCounter++,"CPU",
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
              1));

        dummy.add(new Item("" + idCounter++,"SSD",
              "Crucial MX500 2.5\" 1TB SATA III 3D NAND Internal Solid State Drive (SSD) CT1000MX500SSD1",
              "Sequential reads/writes up to 560/510 MB/s and random reads/writes up to 95k/90k on all file types\n" +
                    "Accelerated by Micron 3D NAND technology\n" +
                    "Integrated Power Loss Immunity preserves all your saved work",
              114.9,
              17,
              2,
              1));

        dummy.add(new Item("" + idCounter++,"GPU",
              "EVGA GeForce RTX 2080 SUPER XC ULTRA GAMING Video Card, 08G-P4-3183-KR, 8GB GDDR6, RGB LED, Metal Backplate",
              "Real Boost Clock: 1845 MHz; Memory Detail: 8192MB GDDR6.\n" +
                    "Real-Time RAY TRACING in games for cutting-edge, hyper-realistic graphics.\n" +
                    "Dual HDB Fans offer higher performance cooling and much quieter acoustic noise.\n" +
                    "All-Metal Backplate & Adjustable RGB.",
              759.99,
              12,
              0,
              1));

        List<Item> dummy2 = new ArrayList<>();
        dummy2.add(new Item("" + idCounter++,"CPU",
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
              1));

        dummy2.add(new Item("" + idCounter++,"SSD",
              "Crucial MX500 2.5\" 1TB SATA III 3D NAND Internal Solid State Drive (SSD) CT1000MX500SSD1",
              "Sequential reads/writes up to 560/510 MB/s and random reads/writes up to 95k/90k on all file types\n" +
                    "Accelerated by Micron 3D NAND technology\n" +
                    "Integrated Power Loss Immunity preserves all your saved work",
              114.9,
              17,
              2,
              1));

        dummy2.add(new Item("" + idCounter++,"GPU",
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
              1));

        Order order1 = new Order("#11111", System.currentTimeMillis(), "Lior", dummy);
        Order order2 = new Order("#22222", System.currentTimeMillis(), "Idan", dummy2);

        orders.put(order1.getOrderId(), order1);
        orders.put(order2.getOrderId(), order2);
    }
}
