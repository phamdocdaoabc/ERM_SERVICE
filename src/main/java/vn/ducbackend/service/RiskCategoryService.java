package vn.ducbackend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.ducbackend.domain.dto.*;

import java.util.Set;

public interface RiskCategoryService {
    Long create(RiskCategoryRequest riskCategoryRequest);

    Page<RiskCategoryDetailResponse> getListRiskCategory(Pageable pageable, Set<Long> ids);

    RiskCategoryDetailResponse getRiskCategory(Long id);

    void delete(Long id);

    Long update(RiskCategoryUpdateDTO request);
}
