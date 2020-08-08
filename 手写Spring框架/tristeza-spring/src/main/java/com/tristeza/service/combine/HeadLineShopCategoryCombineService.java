package com.tristeza.service.combine;

import com.tristeza.model.dto.MainPageInfoDTO;
import com.tristeza.model.dto.Result;

public interface HeadLineShopCategoryCombineService {
    Result<MainPageInfoDTO> getMainPageInfo();
}
