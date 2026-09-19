package in.hexarise.gb_enterprises.crm.service;

import in.hexarise.gb_enterprises.crm.domain.*;
import in.hexarise.gb_enterprises.crm.dto.ConsumerDto.*;
import in.hexarise.gb_enterprises.crm.dto.UserDto.PageResponse;
import in.hexarise.gb_enterprises.crm.exception.AppException;
import in.hexarise.gb_enterprises.crm.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsumerService {

    private final ConsumerRepository consumerRepo;
    private final LeadRepository leadRepo;
    private final RemarkRepository remarkRepo;
    private final FeedbackRepository feedbackRepo;

    @Value("${app.upload.dir}") private String uploadDir;

    public PageResponse<ConsumerResponse> search(String name, String mobile, String status, int page, int size) {
        Page<ConsumerResponse> p = consumerRepo.search(name, mobile, status,
            PageRequest.of(page, size))
            .map(ConsumerResponse::from);
        return new PageResponse<>(p.getContent(), p.getTotalElements(), p.getTotalPages(), page, size);
    }

    public ConsumerResponse create(CreateConsumerRequest req, User actor) {
        Consumer c = Consumer.builder()
            .name(req.name()).mobile(req.mobile()).address(req.address())
            .status(req.status() != null ? req.status() : Consumer.Status.INTERESTED)
            .lat(req.lat()).lng(req.lng()).googleMapUrl(req.googleMapUrl())
            .load(req.load()).type(req.type())
            .nextAction(req.nextAction()).expectedActionDate(req.expectedActionDate())
            .createdBy(actor).updatedBy(actor)
            .build();
        return ConsumerResponse.from(consumerRepo.save(c));
    }

    public ConsumerResponse get(Long id) {
        return ConsumerResponse.from(findById(id));
    }

    public ConsumerResponse update(Long id, UpdateConsumerRequest req, User actor) {
        Consumer c = findById(id);
        if (req.name() != null) c.setName(req.name());
        if (req.mobile() != null) c.setMobile(req.mobile());
        if (req.address() != null) c.setAddress(req.address());
        if (req.status() != null) c.setStatus(req.status());
        if (req.lat() != null) c.setLat(req.lat());
        if (req.lng() != null) c.setLng(req.lng());
        if (req.googleMapUrl() != null) c.setGoogleMapUrl(req.googleMapUrl());
        if (req.load() != null) c.setLoad(req.load());
        if (req.type() != null) c.setType(req.type());
        if (req.nextAction() != null) c.setNextAction(req.nextAction());
        if (req.expectedActionDate() != null) c.setExpectedActionDate(req.expectedActionDate());
        c.setUpdatedBy(actor);
        return ConsumerResponse.from(consumerRepo.save(c));
    }

    public List<String> uploadImages(Long id, List<MultipartFile> files, User actor) throws IOException {
        Consumer c = findById(id);
        Path dir = Paths.get(uploadDir, "consumers", id.toString());
        Files.createDirectories(dir);
        for (MultipartFile file : files) {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), dir.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            String url = "/uploads/consumers/" + id + "/" + filename;
            c.getImages().add(ConsumerImage.builder().consumer(c).url(url).build());
        }
        c.setUpdatedBy(actor);
        consumerRepo.save(c);
        return c.getImages().stream().map(ConsumerImage::getUrl).toList();
    }

    public RemarkResponse addRemark(Long id, AddRemarkRequest req, User actor) {
        Consumer c = findById(id);
        Remark r = Remark.builder().consumer(c).text(req.text()).createdBy(actor).build();
        return RemarkResponse.from(remarkRepo.save(r));
    }

    public PageResponse<FeedbackResponse> getFeedback(Long id, int page, int size) {
        Page<FeedbackResponse> p = feedbackRepo.findByConsumerId(id, PageRequest.of(page, size))
            .map(FeedbackResponse::from);
        return new PageResponse<>(p.getContent(), p.getTotalElements(), p.getTotalPages(), page, size);
    }

    public FeedbackResponse addFeedback(Long id, FeedbackRequest req, User actor) {
        Consumer c = findById(id);
        Feedback f = Feedback.builder().consumer(c).text(req.text()).rating(req.rating()).createdBy(actor).build();
        return FeedbackResponse.from(feedbackRepo.save(f));
    }

    private Consumer findById(Long id) {
        return consumerRepo.findById(id).orElseThrow(() -> AppException.notFound("Consumer not found"));
    }
}
