package vn.ducbackend.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IdsResponse<T> {
    private T id;
}
