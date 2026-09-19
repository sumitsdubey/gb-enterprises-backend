package in.hexarise.gb_enterprises.crm.controller;

import in.hexarise.gb_enterprises.crm.domain.User;
import in.hexarise.gb_enterprises.crm.dto.ConsumerDto.*;
import in.hexarise.gb_enterprises.crm.dto.UserDto.PageResponse;
import in.hexarise.gb_enterprises.crm.service.ConsumerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/consumers")
@RequiredArgsConstructor
public class ConsumerController {

    private final ConsumerService consumerService;

    @GetMapping
    public PageResponse<ConsumerResponse> list(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String mobile,
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size) {
        return consumerService.search(name, mobile, status, page, size);
    }

    @GetMapping("/{id}")
    public ConsumerResponse get(@PathVariable Long id) { return consumerService.get(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConsumerResponse create(@Valid @RequestBody CreateConsumerRequest req,
                                   @AuthenticationPrincipal User actor) {
        return consumerService.create(req, actor);
    }

    @PatchMapping("/{id}")
    public ConsumerResponse update(@PathVariable Long id, @RequestBody UpdateConsumerRequest req,
                                   @AuthenticationPrincipal User actor) {
        return consumerService.update(id, req, actor);
    }

    @PostMapping("/{id}/images")
    public List<String> uploadImages(@PathVariable Long id,
                                     @RequestParam("files") List<MultipartFile> files,
                                     @AuthenticationPrincipal User actor) throws IOException {
        return consumerService.uploadImages(id, files, actor);
    }

    @PostMapping("/{id}/remarks")
    @ResponseStatus(HttpStatus.CREATED)
    public RemarkResponse addRemark(@PathVariable Long id, @RequestBody AddRemarkRequest req,
                                    @AuthenticationPrincipal User actor) {
        return consumerService.addRemark(id, req, actor);
    }

    @GetMapping("/{id}/feedback")
    public PageResponse<FeedbackResponse> getFeedback(@PathVariable Long id,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return consumerService.getFeedback(id, page, size);
    }

    @PostMapping("/{id}/feedback")
    @ResponseStatus(HttpStatus.CREATED)
    public FeedbackResponse addFeedback(@PathVariable Long id, @RequestBody FeedbackRequest req,
                                        @AuthenticationPrincipal User actor) {
        return consumerService.addFeedback(id, req, actor);
    }
}
