package vn.ducbackend.service;

import vn.ducbackend.domain.dto.CauseRequest;
import vn.ducbackend.domain.dto.CauseResponse;

public interface CauseService {
    CauseResponse createCause(CauseRequest causeRequest);
}
