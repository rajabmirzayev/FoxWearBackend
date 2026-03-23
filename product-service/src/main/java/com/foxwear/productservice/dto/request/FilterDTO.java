package com.foxwear.productservice.dto.request;


import com.foxwear.common.enums.Gender;

import java.math.BigInteger;
import java.util.List;

public interface FilterDTO {

    Integer getPage();

    Integer getSize();

    List<Gender> getGender();

    List<Long> getCategoryId();

    String getKeyword();

    List<String> getColor();

    List<String> getProductSize();

    BigInteger getMinPrice();

    BigInteger getMaxPrice();

}
