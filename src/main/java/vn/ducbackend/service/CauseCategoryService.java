package vn.ducbackend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.ducbackend.domain.dto.*;

import java.util.List;
import java.util.Set;

public interface CauseCategoryService {
    Long create(CauseCategoryDetailRequest request);

    CauseCategoryDetailResponse getCauseCategory(Long categoryId);

    Page<CauseCategoryDetailResponse> getListCauseCategory(Pageable pageable, Set<Long> ids);

    Long update(CauseCategoryUpdateDTO request);

    void delete(Long id);
}
