package app.services;

import app.dto.requestDTO.OrderRequestDTO;
import app.dto.responseDTO.CustomerResponseDTO;
import app.dto.responseDTO.OrderResponseDTO;
import app.dto.responseDTO.PartsListResponseDTO;
import app.dto.responseDTO.SalesRepResponseDTO;
import app.dto.responseDTO.carports.CarportResponseDTO;
import app.entities.Carport;
import app.entities.Order;
import app.exceptions.CalculatorException;
import app.exceptions.DatabaseException;
import app.persistence.CustomerMapper;
import app.persistence.OrderMapper;
import app.persistence.RoofMapper;
import app.services.converters.CarportConverter;
import app.services.converters.OrderConverter;

import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private OrderConverter orderConverter;
    private OrderMapper orderMapper;
    private CarportService carportService;
    private UserService userService;
    private PartsListService partsListService;
    private CarportConverter carportConverter;

    public OrderService(OrderMapper orderMapper, CarportService carportService, UserService userService, PartsListService partsListService){
        this.orderMapper = orderMapper;
        this.carportService = carportService;
        this.userService = userService;
        this.partsListService = partsListService;
        this.orderConverter = new OrderConverter();
        this.carportConverter = new CarportConverter();
    }

    public void createOrder(OrderRequestDTO orderRequestDTO) throws DatabaseException {
        Order order = orderConverter.convertOrderToEntity(orderRequestDTO);
        orderMapper.createOrder(order);
    }

    public OrderResponseDTO getOrder(int orderId) throws DatabaseException, CalculatorException {
        Order order = orderMapper.getOrderById(orderId);
        double orderPrice = order.getOrderPrice();
        CarportResponseDTO carportResponseDTO = carportService.getCarport(order.getCarportId());
        CustomerResponseDTO customerResponseDTO = userService.getCustomer(order.getCustomerId());
        SalesRepResponseDTO salesRepResponseDTO = userService.getSalesRep(order.getSalesRepId());
        PartsListResponseDTO partsListResponseDTO = partsListService.getPartsList(order.getCarportId());

        return new OrderResponseDTO(orderId, orderPrice, customerResponseDTO, salesRepResponseDTO, carportResponseDTO, partsListResponseDTO);
    }

    public List<OrderResponseDTO> getAllOrders() throws DatabaseException, CalculatorException {
        List<Order> allOrders = orderMapper.getAllOrders();
        List<OrderResponseDTO> responseDTOS = new ArrayList<>();

        for (Order order : allOrders) {
            int orderId = order.getOrderId();
            double orderPrice = order.getOrderPrice();
            CarportResponseDTO carportResponseDTO = carportService.getCarport(order.getCarportId());
            CustomerResponseDTO customerResponseDTO = userService.getCustomer(order.getCustomerId());
            SalesRepResponseDTO salesRepResponseDTO = userService.getSalesRep(order.getSalesRepId());
            PartsListResponseDTO partsListResponseDTO = partsListService.getPartsList(order.getCarportId());

            responseDTOS.add(new OrderResponseDTO(orderId, orderPrice, customerResponseDTO, salesRepResponseDTO, carportResponseDTO, partsListResponseDTO));
        }

        return responseDTOS;
    }

    public List<OrderResponseDTO> getAllOrdersByCustomerId(int customerId) throws DatabaseException, CalculatorException {
        List<Order> allOrders = orderMapper.getAllOrdersByCustomerId(customerId);
        List<OrderResponseDTO> responseDTOS = new ArrayList<>();

        for (Order order : allOrders) {
            int orderId = order.getOrderId();
            double orderPrice = order.getOrderPrice();
            CarportResponseDTO carportResponseDTO = carportService.getCarport(order.getCarportId());
            CustomerResponseDTO customerResponseDTO = userService.getCustomer(customerId);
            SalesRepResponseDTO salesRepResponseDTO = userService.getSalesRep(order.getSalesRepId());
            PartsListResponseDTO partsListResponseDTO = partsListService.getPartsList(order.getCarportId());

            responseDTOS.add(new OrderResponseDTO(orderId, orderPrice, customerResponseDTO, salesRepResponseDTO, carportResponseDTO, partsListResponseDTO));
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

