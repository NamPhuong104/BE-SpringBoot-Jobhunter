package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResResumeDTO;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private final JobService jobService;
    private final UserService userService;
    private final ResumeService resumeService;

    public ResumeController(JobService jobService, UserService userService, ResumeService resumeService) {
        this.resumeService = resumeService;
        this.jobService = jobService;
        this.userService = userService;
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Get resume by id")
    public ResponseEntity<ResResumeDTO> getResumeById(@Valid @PathVariable("id") Long id) throws IdInvalidException {
        Resume currentResume = this.resumeService.handleFindResumeById(id);
        if (currentResume == null) throw new IdInvalidException("Resume không tồn tại");
        return ResponseEntity.ok().body(this.resumeService.convertToResume(currentResume));
    }

    @GetMapping("/resumes")
    @ApiMessage("Get all resumes")
    public ResponseEntity<ResultPaginationDTO> getAllResume(@Filter Specification<Resume> spec, Pageable pageable) {
        ResultPaginationDTO res = this.resumeService.handleGetAllResume(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PostMapping("/resumes")
    @ApiMessage("Create resume")
    public ResponseEntity<ResCreateResumeDTO> createResume(@Valid @RequestBody Resume reqData) throws IdInvalidException {
        User currentUser = this.userService.handleFindUserById(reqData.getUser().getId());
        Job currentJob = this.jobService.handleFindOneJob(reqData.getJob().getId());

        if (currentUser == null) throw new IdInvalidException("User không tồn tai !!!!");
        if (currentJob == null) throw new IdInvalidException("Job không tồn tai !!!!");

        reqData.setUser(currentUser);
        reqData.setJob(currentJob);

        ResCreateResumeDTO res = this.resumeService.handleCreateResume(reqData);

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/resumes")
    @ApiMessage("Update resume")
    public ResponseEntity<ResCreateResumeDTO> updateResume(@Valid @RequestBody Resume reqData) throws IdInvalidException {
        return ResponseEntity.ok().body(this.resumeService.handleUpdateResume(reqData));
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete resume")
    public ResponseEntity<Void> deleteResume(@Valid @PathVariable("id") long id) throws IdInvalidException {
        Resume currentResume = this.resumeService.handleFindResumeById(id);
        if (currentResume == null) throw new IdInvalidException("Resume không tồn tại");
        this.resumeService.handleDeleteResume(currentResume.getId());
        return ResponseEntity.ok(null);
    }
}
