package vn.ducbackend.service;

import vn.ducbackend.domain.dto.CauseCategoryDTO;
import vn.ducbackend.domain.dto.CauseCategoryDetailRequest;
import vn.ducbackend.domain.dto.CauseCategoryDetailResponse;
import vn.ducbackend.domain.dto.CauseCategoryUpdateDTO;

public interface CauseCategoryService {
    CauseCategoryDTO createCauseCategory(CauseCategoryDetailRequest request);

    CauseCategoryDetailResponse getDetail(Long categoryId);

    CauseCategoryUpdateDTO updateCauseCategory(CauseCategoryUpdateDTO request);

    void deleteCauseCategory(Long id);
}
