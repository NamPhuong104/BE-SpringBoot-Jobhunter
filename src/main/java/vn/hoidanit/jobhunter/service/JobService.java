package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;


    public JobService(JobRepository jobRepository, SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
    }

    public ResJobDTO convertToResJobDTO(Job job) {
        ResJobDTO res = new ResJobDTO();

        res.setId(job.getId());
        res.setName(job.getName());
        res.setLocation(job.getLocation());
        res.setSalary(job.getSalary());
        res.setQuantity(job.getQuantity());
        res.setLevel(job.getLevel());
        res.setDescription(job.getDescription());
        res.setStartDate(job.getStartDate());
        res.setEndDate(job.getEndDate());
        res.setActive(job.isActive());
        res.setCreatedAt(job.getCreatedAt());
        res.setUpdatedAt(job.getUpdatedAt());

        if (job.getCompany() != null)
            res.setCompany(new ResJobDTO.CompanyDTO(job.getCompany().getId(), job.getCompany().getName(), job.getCompany().getLogo()));

        if (job.getSkills() != null) {
            List<ResJobDTO.SkillDTO> skillSummaryDTO = job.getSkills().stream().map(skill -> new ResJobDTO.SkillDTO(skill.getId(), skill.getName())).
                    collect(Collectors.toList());
            res.setSkills(skillSummaryDTO);
        }
        return res;
    }

    public ResCreateJobDTO convertToResCreateJobDTO(Job job) {
        ResCreateJobDTO res = new ResCreateJobDTO();

        res.setName(job.getName());
        res.setLocation(job.getLocation());
        res.setSalary(job.getSalary());
        res.setQuantity(job.getQuantity());
        res.setLevel(job.getLevel());
        res.setDescription(job.getDescription());
        res.setStartDate(job.getStartDate());
        res.setEndDate(job.getEndDate());
        res.setActive(job.isActive());
        res.setCreatedAt(job.getCreatedAt());

        if (job.getCompany() != null)
            res.setCompany(new ResCreateJobDTO.CompanyDTO(job.getCompany().getId(), job.getCompany().getName()));

        if (job.getSkills() != null) {
            List<ResCreateJobDTO.SkillDTO> skillSummaryDTO = job.getSkills().stream().map(skill -> new ResCreateJobDTO.SkillDTO(skill.getId(), skill.getName())).collect(Collectors.toList());
            res.setSkills(skillSummaryDTO);
        }
        return res;
    }

    public ResUpdateJobDTO convertToResUpdateJobDTO(Job job) {
        ResUpdateJobDTO res = new ResUpdateJobDTO();

        res.setId(job.getId());
        res.setName(job.getName());
        res.setLocation(job.getLocation());
        res.setSalary(job.getSalary());
        res.setQuantity(job.getQuantity());
        res.setLevel(job.getLevel());
        res.setDescription(job.getDescription());
        res.setStartDate(job.getStartDate());
        res.setEndDate(job.getEndDate());
        res.setActive(job.isActive());
        res.setUpdatedAt(job.getUpdatedAt());

        if (job.getCompany() != null)
            res.setCompany(new ResUpdateJobDTO.CompanyDTO(job.getCompany().getId(), job.getCompany().getName()));

        if (job.getSkills() != null) {
            List<String> skillNames = job.getSkills()
                    .stream()
                    .map(Skill::getName)
                    .collect(Collectors.toList());
            res.setSkills(skillNames);
        }

        return res;
    }

    public ResultPaginationDTO handleGetAllJob(Specification<Job> spec, Pageable pageable) {
        Page<Job> jobPage = this.jobRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(jobPage.getTotalPages());
        mt.setTotal(jobPage.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(jobPage.get());

        List<ResJobDTO> dtoJob = jobPage.getContent().stream().map(this::convertToResJobDTO).collect(Collectors.toList());
        rs.setResult(dtoJob);

//        Page<ResJobDTO> dtoPage = jobPage.map(this::convertToResJobDTO);
//        List<ResJobDTO> dtoList = dtoPage.getContent();
//        rs.setResult(dtoList);
        return rs;
    }

    public Job handleFindOneJob(Long id) {
        Optional<Job> res = this.jobRepository.findById(id);
        if (res.isPresent()) {
            return res.get();
        }
        return null;
    }


    public Job handleCreateJob(Job reqJob) {
        return this.jobRepository.save(reqJob);
    }

    public Job handleUpdateJob(Job reqJob) {
        Job currentJob = this.handleFindOneJob(reqJob.getId());

        if (currentJob == null) return null;

        currentJob.setId(reqJob.getId());
        currentJob.setName(reqJob.getName());
        currentJob.setLocation(reqJob.getLocation());
        currentJob.setSalary(reqJob.getSalary());
        currentJob.setQuantity(reqJob.getQuantity());
        currentJob.setLevel(reqJob.getLevel());
        currentJob.setDescription(reqJob.getDescription());
        currentJob.setStartDate(reqJob.getStartDate());
        currentJob.setEndDate(reqJob.getEndDate());
        currentJob.setActive(reqJob.isActive());

        if (reqJob.getCompany() != null) currentJob.setCompany(reqJob.getCompany());

        if (reqJob.getSkills() != null) {
            List<Long> skillsIds = reqJob.getSkills().stream().map(Skill::getId).collect(Collectors.toList());
            List<Skill> skillEntities = skillRepository.findAllByIdIn(skillsIds);
            List<Long> foundIds = skillEntities.stream().map(Skill::getId).collect(Collectors.toList());
            List<Long> missing = skillsIds.stream().filter(id -> !foundIds.contains(id)).collect(Collectors.toList());

            if (!missing.isEmpty()) throw new IllegalArgumentException("Một số skill không tồn tại: " + missing);
            currentJob.setSkills(skillEntities);
        }


        return jobRepository.save(currentJob);
    }

    public void handleDeleteJob(long id) {
        Job currentJob = this.handleFindOneJob(id);
        this.jobRepository.deleteById(currentJob.getId());

    }
}
