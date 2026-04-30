package com.foxwear.orderservice.service;

import com.foxwear.common.utils.StringHelper;
import com.foxwear.orderservice.dto.request.SaleCreateRequest;
import com.foxwear.orderservice.dto.response.SaleCreateResponse;
import com.foxwear.orderservice.dto.response.SaleItemCreateResponse;
import com.foxwear.orderservice.entity.Sale;
import com.foxwear.orderservice.entity.SaleItem;
import com.foxwear.orderservice.mapper.SaleItemMapper;
import com.foxwear.orderservice.mapper.SaleMapper;
import com.foxwear.orderservice.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    private SaleCreateResponse mapToSaleCreateResponse(Sale sale) {
        SaleCreateResponse response = saleMapper.toCreateResponse(sale);

        List<SaleItemCreateResponse> items = sale.getItems().stream()
                .map(saleItemMapper::toCreateResponse)
                .toList();

        response.setItems(items);

        return response;
    }
}
