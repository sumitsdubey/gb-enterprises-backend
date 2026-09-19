package in.hexarise.gb_enterprises.crm.service;

import in.hexarise.gb_enterprises.crm.domain.Lead;
import in.hexarise.gb_enterprises.crm.domain.User;
import in.hexarise.gb_enterprises.crm.dto.LeadDto.*;
import in.hexarise.gb_enterprises.crm.dto.UserDto.PageResponse;
import in.hexarise.gb_enterprises.crm.exception.AppException;
import in.hexarise.gb_enterprises.crm.repository.LeadRepository;
import in.hexarise.gb_enterprises.crm.util.LeadExcelParser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeadService {

    private final LeadRepository leadRepo;
    private final LeadExcelParser parser;

    public PageResponse<LeadResponse> search(String name, String mobile, Boolean valid, Boolean alreadyInstalled, int page, int size) {
        Page<LeadResponse> p = leadRepo.search(name, mobile, valid, alreadyInstalled,
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")))
            .map(LeadResponse::from);
        return new PageResponse<>(p.getContent(), p.getTotalElements(), p.getTotalPages(), page, size);
    }

    public LeadResponse create(CreateLeadRequest req, User actor) {
        Lead lead = Lead.builder()
            .name(req.name()).mobile(req.mobile()).address(req.address()).load(req.load())
            .valid(req.valid() != null ? req.valid() : true)
            .alreadyInstalled(req.alreadyInstalled() != null ? req.alreadyInstalled() : false)
            .createdBy(actor).build();
        return LeadResponse.from(leadRepo.save(lead));
    }

    public LeadResponse update(Long id, UpdateLeadRequest req) {
        Lead lead = leadRepo.findById(id).orElseThrow(() -> AppException.notFound("Lead not found"));
        if (req.valid() != null) lead.setValid(req.valid());
        if (req.alreadyInstalled() != null) lead.setAlreadyInstalled(req.alreadyInstalled());
        if (req.name() != null) lead.setName(req.name());
        if (req.mobile() != null) lead.setMobile(req.mobile());
        if (req.address() != null) lead.setAddress(req.address());
        if (req.load() != null) lead.setLoad(req.load());
        return LeadResponse.from(leadRepo.save(lead));
    }

    public List<LeadResponse> upload(MultipartFile file, User actor) {
        List<Lead> leads = parser.parse(file).stream().map(dto ->
            Lead.builder().name(dto.name()).mobile(dto.mobile()).address(dto.address()).load(dto.load())
                .createdBy(actor).build()
        ).toList();
        return leadRepo.saveAll(leads).stream().map(LeadResponse::from).toList();
    }

    public LeadResponse get(Long id) {
        return LeadResponse.from(leadRepo.findById(id).orElseThrow(() -> AppException.notFound("Lead not found")));
    }

    public void delete(Long id) {
        Lead lead = leadRepo.findById(id).orElseThrow(() -> AppException.notFound("Lead not found"));
        leadRepo.delete(lead);
    }
}
