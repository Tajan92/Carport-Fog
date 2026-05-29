package app.services;

import app.dto.requestDTO.OrderRequestDTO;
import app.dto.responseDTO.*;
import app.dto.responseDTO.carports.CarportResponseDTO;
import app.entities.Carport;
import app.entities.Order;
import app.entities.Product;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.persistence.CustomerMapper;
import app.persistence.OrderMapper;
import app.persistence.ProductMapper;
import app.persistence.RoofMapper;
import app.services.converters.CarportConverter;
import app.services.converters.OrderConverter;
import app.services.utils.PriceCalculator;

import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private OrderConverter orderConverter;
    private OrderMapper orderMapper;
    private ProductMapper productMapper;
    private CarportService carportService;
    private UserService userService;
    private PartsListService partsListService;
    private CarportConverter carportConverter;

    public OrderService(OrderMapper orderMapper, CarportService carportService, UserService userService, PartsListService partsListService, ProductMapper productMapper) {
        this.orderMapper = orderMapper;
        this.carportService = carportService;
        this.userService = userService;
        this.partsListService = partsListService;
        this.orderConverter = new OrderConverter();
        this.carportConverter = new CarportConverter();
        this.productMapper = productMapper;
    }

    public void createOrder(OrderRequestDTO orderRequestDTO) throws DatabaseException {
        Order order = orderConverter.convertOrderToEntity(orderRequestDTO);
        orderMapper.createOrder(order);
    }

    public OrderResponseDTO getOrder(int orderId) throws DatabaseException, CalculatorException {
        Order order = orderMapper.getOrderById(orderId);

        CarportResponseDTO carportResponseDTO = carportService.getCarport(order.getCarportId());
        CustomerResponseDTO customerResponseDTO = userService.getCustomer(order.getCustomerId());
        SalesRepResponseDTO salesRepResponseDTO = userService.getSalesRep(order.getSalesRepId());
        PartsListResponseDTO partsListResponseDTO = partsListService.getPartsList(order.getCarportId());

        double retailPrice = order.getOrderPrice();
        double discount = order.getOrderDiscount();
        double serviceFee = PriceCalculator.calculateServiceFee(order.getOrderPrice());
        double totalPrice = PriceCalculator.getRevenue(order.getOrderPrice(), discount, serviceFee);

        return new OrderResponseDTO(orderId, retailPrice, discount, totalPrice, customerResponseDTO, salesRepResponseDTO, carportResponseDTO, partsListResponseDTO);
    }

    public OrderAdminResponseDTO getOrderAdmin(int orderId) throws DatabaseException, CalculatorException {
        Order order = orderMapper.getOrderById(orderId);

        CarportResponseDTO carportResponseDTO = carportService.getCarport(order.getCarportId());
        CustomerResponseDTO customerResponseDTO = userService.getCustomer(order.getCustomerId());
        SalesRepResponseDTO salesRepResponseDTO = userService.getSalesRep(order.getSalesRepId());
        PartsListResponseDTO partsListResponseDTO = partsListService.getPartsList(order.getCarportId());

        List<Product> products = productMapper.getAllProducts();

        double calculatedCostPrice = 0;
        for (Product product : products) {
            calculatedCostPrice += product.getCostPrice();
        }

        double retailPrice = order.getOrderPrice();
        double discount = order.getOrderDiscount();
        double serviceFee = PriceCalculator.calculateServiceFee(order.getOrderPrice());
        double totalPrice = PriceCalculator.getRevenue(order.getOrderPrice(), discount, serviceFee);
        double costPrice = calculatedCostPrice;

        return new OrderAdminResponseDTO(orderId, retailPrice, discount, totalPrice, costPrice, serviceFee, customerResponseDTO, salesRepResponseDTO, carportResponseDTO, partsListResponseDTO);
    }

    public List<OrderAdminResponseDTO> getAllOrders() throws DatabaseException, CalculatorException {
        List<Order> allOrders = orderMapper.getAllOrders();
        List<OrderAdminResponseDTO> responseDTOS = new ArrayList<>();
        List<Product> products = productMapper.getAllProducts();

        for (Order order : allOrders) {
            int orderId = order.getOrderId();


            CarportResponseDTO carportResponseDTO = carportService.getCarport(order.getCarportId());
            CustomerResponseDTO customerResponseDTO = userService.getCustomer(order.getCustomerId());
            SalesRepResponseDTO salesRepResponseDTO = userService.getSalesRep(order.getSalesRepId());
            PartsListResponseDTO partsListResponseDTO = partsListService.getPartsList(order.getCarportId());

            double calculatedCostPrice = 0;
            for (Product product : products) {
                calculatedCostPrice += product.getCostPrice();
            }

            double retailPrice = order.getOrderPrice();
            double discount = order.getOrderDiscount();
            double serviceFee = PriceCalculator.calculateServiceFee(order.getOrderPrice());
            double totalPrice = PriceCalculator.getRevenue(order.getOrderPrice(), discount, serviceFee);
            double costPrice = calculatedCostPrice;

            responseDTOS.add(new OrderAdminResponseDTO(orderId, retailPrice, discount, totalPrice, costPrice, serviceFee, customerResponseDTO, salesRepResponseDTO, carportResponseDTO, partsListResponseDTO));
        }
        return responseDTOS;
    }

    public List<OrderResponseDTO> getAllOrdersByCustomerId(int customerId) throws DatabaseException, CalculatorException {
        List<Order> allOrders = orderMapper.getAllOrdersByCustomerId(customerId);
        List<OrderResponseDTO> responseDTOS = new ArrayList<>();

        for (Order order : allOrders) {
            int orderId = order.getOrderId();

            CarportResponseDTO carportResponseDTO = carportService.getCarport(order.getCarportId());
            CustomerResponseDTO customerResponseDTO = userService.getCustomer(customerId);
            SalesRepResponseDTO salesRepResponseDTO = userService.getSalesRep(order.getSalesRepId());
            PartsListResponseDTO partsListResponseDTO = partsListService.getPartsList(order.getCarportId());

            double retailPrice = order.getOrderPrice();
            double discount = order.getOrderDiscount();
            double serviceFee = PriceCalculator.calculateServiceFee(order.getOrderPrice());
            double totalPrice = PriceCalculator.getRevenue(order.getOrderPrice(), discount, serviceFee);

            responseDTOS.add(new OrderResponseDTO(orderId, retailPrice, discount, totalPrice, customerResponseDTO, salesRepResponseDTO, carportResponseDTO, partsListResponseDTO));
        }
        return responseDTOS;
    }

    public void updateOrder(OrderRequestDTO orderRequestDTO, int orderId) throws DatabaseException {
        Order order = orderConverter.convertOrderToEntity(orderRequestDTO);
        order.setOrderId(orderId);
        orderMapper.updateOrder(order);
    }

    public void deleteOrder(int orderId) throws DatabaseException {
        orderMapper.removeOrderById(orderId);
    }
}

