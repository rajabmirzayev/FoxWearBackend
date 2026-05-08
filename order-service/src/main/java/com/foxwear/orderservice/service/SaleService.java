package com.foxwear.orderservice.service;

import com.foxwear.common.utils.StringHelper;
import com.foxwear.orderservice.dto.request.SaleCreateRequest;
import com.foxwear.orderservice.dto.response.*;
import com.foxwear.orderservice.entity.Sale;
import com.foxwear.orderservice.entity.SaleItem;
import com.foxwear.orderservice.exception.SaleNotFoundException;
import com.foxwear.orderservice.mapper.SaleItemMapper;
import com.foxwear.orderservice.mapper.SaleMapper;
import com.foxwear.orderservice.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaleService {
    private final SaleRepository saleRepository;
    private final SaleMapper saleMapper;
    private final SaleItemMapper saleItemMapper;

    /**
     * Creates a new sale record along with its associated items.
     *
     * @param request  The sale creation data transfer object.
     * @param sellerId The ID of the cashier/seller performing the transaction.
     * @return SaleCreateResponse containing the details of the persisted sale.
     */
    @Transactional
    public SaleCreateResponse createSale(SaleCreateRequest request, Long sellerId) {
        log.info("Initiating sale creation for sellerId: {}", sellerId);

        Sale sale = saleMapper.toEntity(request);

        sale.setReceiptNumber(StringHelper.generateFWNumber());
        sale.setCashierId(sellerId);

        List<SaleItem> saleItems = request.getItems().stream()
                .map(saleItemMapper::toEntity)
                .toList();

        sale.setItems(saleItems);
        saleItems.forEach(item -> item.setSale(sale));

        var response = saleRepository.save(sale);
        log.info("Sale successfully created with receipt number: {}", response.getReceiptNumber());

        return mapToSaleCreateResponse(response);
    }

    /**
     * Retrieves a paginated list of all sales.
     *
     * @param page The page number (0-indexed).
     * @param size The number of records per page.
     * @return A page of SaleGetAllResponse objects.
     */
    @Transactional(readOnly = true)
    public Page<SaleGetAllResponse> getAll(Integer page, Integer size) {
        log.info("Fetching all sales for page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Sale> sales = saleRepository.findAll(pageable);

        return sales.map(this::mapToSaleGetAllResponse);
    }

    /**
     * Retrieves a specific sale by its unique identifier.
     *
     * @param id The ID of the sale to retrieve.
     * @return SaleGetResponse containing detailed sale information.
     * @throws SaleNotFoundException if no sale is found with the given ID.
     */
    @Transactional(readOnly = true)
    public SaleGetResponse getById(Long id) {
        log.info("Fetching sale details for id: {}", id);
        Sale sale = findSaleOrThrow(id);

        return mapToSaleGetResponse(sale);
    }

    private SaleGetAllResponse mapToSaleGetAllResponse(Sale sale) {
        SaleGetAllResponse response = saleMapper.toGetAllResponse(sale);
        List<SaleItemGetAllResponse> saleItemsRes = sale.getItems().stream()
                .map(saleItemMapper::toGetAllResponse)
                .toList();

        response.setItems(saleItemsRes);
        return response;
    }

    private SaleGetResponse mapToSaleGetResponse(Sale sale) {
        SaleGetResponse response = saleMapper.toGetResponse(sale);
        List<SaleItemGetResponse> saleItemsRes = sale.getItems().stream()
                .map(saleItemMapper::toGetResponse)
                .toList();

        response.setItems(saleItemsRes);
        return response;
    }

    private SaleCreateResponse mapToSaleCreateResponse(Sale sale) {
        SaleCreateResponse response = saleMapper.toCreateResponse(sale);

        List<SaleItemCreateResponse> items = sale.getItems().stream()
                .map(saleItemMapper::toCreateResponse)
                .toList();

        response.setItems(items);

        return response;
    }

    private Sale findSaleOrThrow(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Sale with id {} not found", id);
                    return new SaleNotFoundException("Sale not found");
                });
    }
}
