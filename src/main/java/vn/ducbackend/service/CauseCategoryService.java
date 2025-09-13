package vn.ducbackend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.ducbackend.domain.dto.causesCategory.CauseCategoryDetailRequest;
import vn.ducbackend.domain.dto.causesCategory.CauseCategoryDetailResponse;
import vn.ducbackend.domain.dto.causesCategory.CauseCategorySearchRequest;

import java.util.Set;

public interface CauseCategoryService {
    Long create(CauseCategoryDetailRequest request);

    CauseCategoryDetailResponse getCauseCategory(Long categoryId);

    Page<CauseCategoryDetailResponse> getListCauseCategory(Pageable pageable, Set<Long> ids);

    Long update(CauseCategoryDetailRequest request);

    void delete(Long id);

    Page<CauseCategoryDetailResponse> searchCauseCategory(Pageable pageable, CauseCategorySearchRequest request);
}
