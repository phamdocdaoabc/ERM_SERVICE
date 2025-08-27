package vn.ducbackend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.ducbackend.domain.dto.*;

import java.util.List;

public interface CauseCategoryService {
    CauseCategoryDetailResponse createCauseCategory(CauseCategoryDetailRequest request);

    CauseCategoryDetailResponse getDetail(Long categoryId);

    Page<CauseCategoryDetailResponse> getAllCauseCategory(Pageable pageable);

    CauseCategoryUpdateDTO updateCauseCategory(CauseCategoryUpdateDTO request);

    void deleteCauseCategory(Long id);
}
