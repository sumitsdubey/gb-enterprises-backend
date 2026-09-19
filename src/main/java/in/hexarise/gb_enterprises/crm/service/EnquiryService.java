package in.hexarise.gb_enterprises.crm.service;

import in.hexarise.gb_enterprises.crm.domain.Enquiry;
import in.hexarise.gb_enterprises.crm.dto.EnquiryDto.*;
import in.hexarise.gb_enterprises.crm.dto.UserDto.PageResponse;
import in.hexarise.gb_enterprises.crm.exception.AppException;
import in.hexarise.gb_enterprises.crm.repository.EnquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnquiryService {

    private final EnquiryRepository enquiryRepo;

    public EnquiryResponse create(EnquiryRequest req) {
        Enquiry e = Enquiry.builder()
            .name(req.name()).mobile(req.mobile()).city(req.city())
            .consumerType(req.consumerType()).monthlyBill(req.monthlyBill())
            .preferredSystem(req.preferredSystem()).build();
        return EnquiryResponse.from(enquiryRepo.save(e));
    }

    public PageResponse<EnquiryResponse> list(int page, int size) {
        Page<EnquiryResponse> p = enquiryRepo.findAll(
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")))
            .map(EnquiryResponse::from);
        return new PageResponse<>(p.getContent(), p.getTotalElements(), p.getTotalPages(), page, size);
    }

    public void delete(Long id) {
        if (!enquiryRepo.existsById(id)) throw AppException.notFound("Enquiry not found");
        enquiryRepo.deleteById(id);
    }

    public EnquiryResponse update(Long id, UpdateEnquiryRequest req) {
        Enquiry e = enquiryRepo.findById(id).orElseThrow(() -> AppException.notFound("Enquiry not found"));
        if (req.name() != null) e.setName(req.name());
        if (req.mobile() != null) e.setMobile(req.mobile());
        if (req.city() != null) e.setCity(req.city());
        if (req.consumerType() != null) e.setConsumerType(req.consumerType());
        if (req.monthlyBill() != null) e.setMonthlyBill(req.monthlyBill());
        if (req.preferredSystem() != null) e.setPreferredSystem(req.preferredSystem());
        return EnquiryResponse.from(enquiryRepo.save(e));
    }
}
