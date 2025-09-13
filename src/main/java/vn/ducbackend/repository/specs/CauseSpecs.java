package vn.ducbackend.repository.specs;
import org.springframework.data.jpa.domain.Specification;
import vn.ducbackend.domain.entity.Cause;
import vn.ducbackend.domain.enums.Source;

public class CauseSpecs {
    public static Specification<Cause> hasCode(String code) {
        return (root, query, cb) ->
                (code == null || code.isBlank())
                        ? null
                        : cb.equal(cb.lower(root.get("code")), code.toLowerCase());
    }

    public static Specification<Cause> hasName(String name) {
        return (root, query, cb) ->
                (name == null || name.isBlank())
                        ? null
                        : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Cause> hasCauseCategoryId(Long causeCategoryId) {
        return (root, query, cb) ->
                (causeCategoryId == null)
                        ? null
                        : cb.equal(root.get("causeCategoryId"), causeCategoryId);
    }

    public static Specification<Cause> hasSource(Source source) {
        return (root, query, cb) ->
                (source == null)
                        ? null
                        : cb.equal(root.get("source"), source);
    }

    public static Specification<Cause> hasIsActive(Boolean isActive) {
        return (root, query, cb) ->
                (isActive == null)
                        ? null
                        : cb.equal(root.get("isActive"), isActive);
    }
}
