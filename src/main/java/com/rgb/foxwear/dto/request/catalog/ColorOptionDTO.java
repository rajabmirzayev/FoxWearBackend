package com.rgb.foxwear.dto.request.catalog;

import java.util.List;

public interface ColorOptionDTO {
    String getColorName();
    String getColorCode();
    List<? extends ImageDTO> getImages();
    List<? extends ItemDTO> getItems();
}
