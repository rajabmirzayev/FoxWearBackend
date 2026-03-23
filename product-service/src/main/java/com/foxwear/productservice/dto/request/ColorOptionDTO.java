package com.foxwear.productservice.dto.request;

import java.util.List;

public interface ColorOptionDTO {
    String getColorName();
    String getColorCode();
    List<? extends ImageDTO> getImages();
    List<? extends ItemDTO> getItems();
}
