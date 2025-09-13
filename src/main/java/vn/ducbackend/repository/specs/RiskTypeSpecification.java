package vn.ducbackend.repository.specs;

import org.springframework.data.jpa.domain.Specification;
import vn.ducbackend.domain.entity.RiskType;
import vn.ducbackend.domain.enums.PartyType;
import vn.ducbackend.domain.enums.Source;

import java.util.Collection;
import java.util.Set;

public class RiskTypeSpecification {
    public static Specification<RiskType> hasIds(Collection<Long> ids) {
        return (root, query, cb) -> root.get("id").in(ids);
    }

    public static Specification<RiskType> hasCode(String code) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("code")), "%" + code.toLowerCase() + "%");
    }

    public static Specification<RiskType> hasName(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<RiskType> hasSource(Source source) {
        return (root, query, cb) -> cb.equal(root.get("source"), source);
    }

    public static Specification<RiskType> hasObject(PartyType object) {
        return (root, query, cb) -> cb.equal(root.get("object"), object);
    }

    public static Specification<RiskType> hasNote(String note) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("note")), "%" + note.toLowerCase() + "%");
    }

    public static Specification<RiskType> hasIsActive(Boolean isActive) {
        return (root, query, cb) -> cb.equal(root.get("isActive"), isActive);
    }
}
