package vn.ducbackend.repository.specs;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import vn.ducbackend.domain.entity.CauseCategory;
import vn.ducbackend.domain.entity.SystemCauseCategory;

public class CauseCategorySpecs {
    public static Specification<CauseCategory> hasName(String name) {
        return (root, query, cb) ->
                (name == null || name.isBlank())
                        ? null
                        : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<CauseCategory> hasCode(String code) {
        return (root, query, cb) ->
                (code == null || code.isBlank())
                        ? null
                        : cb.equal(cb.lower(root.get("code")), code.toLowerCase());
    }

    public static Specification<CauseCategory> hasSystemId(Long systemId) {
        return (root, query, cb) -> {
            if (systemId == null) return null;

            Root<SystemCauseCategory> sysRoot = query.from(SystemCauseCategory.class);
            Predicate join = cb.equal(sysRoot.get("causeCategoryId"), root.get("id"));
            Predicate filter = cb.equal(sysRoot.get("systemId"), systemId);
            return cb.and(join, filter);
        };
    }
}
