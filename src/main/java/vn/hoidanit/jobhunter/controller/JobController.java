package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    private final JobService jobService;
    private final SkillRepository skillRepository;
    private final CompanyService companyService;

    public JobController(JobService jobService, SkillRepository skillRepository, CompanyService companyService) {
        this.companyService = companyService;
        this.skillRepository = skillRepository;
        this.jobService = jobService;
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("Get job by id")
    public ResponseEntity<ResJobDTO> getJobById(@PathVariable("id") Long id) throws IdInvalidException {
        Job currentJob = this.jobService.handleFindOneJob(id);
        if (currentJob == null) throw new IdInvalidException("Id không tồn tại");
        return ResponseEntity.ok(this.jobService.convertToResJobDTO(currentJob));
    }

    @GetMapping("/jobs")
    @ApiMessage("Get all jobs")
    public ResponseEntity<ResultPaginationDTO> getAllJobs(@Filter Specification<Job> spec, Pageable pageable) {
        ResultPaginationDTO res = this.jobService.handleGetAllJob(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PostMapping("/jobs")
    @ApiMessage("Create job")
    public ResponseEntity<ResCreateJobDTO> createJob(@RequestBody Job reqJob) {
        List<Long> skillIds = reqJob.getSkills().stream().map(Skill::getId).toList();
        List<Skill> listSkill = this.skillRepository.findAllByIdIn(skillIds);

        reqJob.setSkills(listSkill);

        Job res = this.jobService.handleCreateJob(reqJob);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.convertToResCreateJobDTO(res));
    }

    @PutMapping("/jobs")
    @ApiMessage("Update a job")
    public ResponseEntity<ResUpdateJobDTO> UpdateJob(@RequestBody Job reqJob) throws IdInvalidException {
        Job existJob = this.jobService.handleFindOneJob(reqJob.getId());
        if (existJob == null) throw new IdInvalidException("Id job không tồn tại");

        Company existCompany = this.companyService.handleFindOneCompanyById(reqJob.getCompany().getId());
        if (existCompany == null) throw new IdInvalidException("Công ty không tồn tại");
        reqJob.setCompany(existCompany);

        Job res = this.jobService.handleUpdateJob(reqJob);

        return ResponseEntity.ok(this.jobService.convertToResUpdateJobDTO(res));
    }

    @DeleteMapping("jobs/{id}")
    @ApiMessage("Delete a job")
    public ResponseEntity<Void> DeleteJob(@PathVariable("id") Long id) throws IdInvalidException {
        Job existJob = this.jobService.handleFindOneJob(id);
        if (existJob == null) throw new IdInvalidException("Id job không tồn tại");
        this.jobService.handleDeleteJob(id);

        return ResponseEntity.ok(null);
    }
}
