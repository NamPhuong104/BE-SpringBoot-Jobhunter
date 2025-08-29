package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResResumeDTO;
import vn.hoidanit.jobhunter.repository.ResumeRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;

    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public ResResumeDTO convertToResume(Resume resume) {
        ResResumeDTO res = new ResResumeDTO();

        res.setId(resume.getId());
        res.setEmail(resume.getEmail());
        res.setUrl(resume.getUrl());
        res.setStatus(resume.getStatus());
        res.setCreatedAt(resume.getCreatedAt());
        res.setUpdatedAt(resume.getUpdatedAt());

//        res.setUser(new ResResumeDTO.User(resume.getUser().getId(), resume.getUser().getName()));
//        res.setJob(new ResResumeDTO.Job(resume.getJob().getId(), resume.getJob().getName()));

        if (resume.getUser() != null) {
            ResResumeDTO.User user = new ResResumeDTO.User();
            user.setId(resume.getUser().getId());
            user.setName(resume.getUser().getName());
            res.setUser(user);
        }
        if (resume.getJob() != null) {
            ResResumeDTO.Job job = new ResResumeDTO.Job();
            job.setId(resume.getJob().getId());
            job.setName(resume.getJob().getName());
            res.setJob(job);
        }
        return res;
    }

    public Resume handleFindResumeById(Long id) {
        Optional<Resume> res = this.resumeRepository.findById(id);
        if (res.isPresent()) return res.get();
        return null;
    }

    public ResultPaginationDTO handleGetAllResume(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> resumePage = this.resumeRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(resumePage.getTotalPages());
        mt.setTotal(resumePage.getTotalElements());

        rs.setMeta(mt);

        Page<ResResumeDTO> convertData = resumePage.map(this::convertToResume);
        List<ResResumeDTO> dtoList = convertData.getContent();
        rs.setResult(dtoList);

        return rs;
    }

    public ResCreateResumeDTO handleCreateResume(Resume reqData) {
        ResCreateResumeDTO res = new ResCreateResumeDTO();

        Resume resume = this.resumeRepository.save(reqData);

        res.setId(resume.getId());
        res.setCreatedAt(resume.getCreatedAt());
        res.setCreatedBy(resume.getCreatedBy());

        return res;
    }

    public ResCreateResumeDTO handleUpdateResume(Resume reqData) throws IdInvalidException {
        ResCreateResumeDTO res = new ResCreateResumeDTO();

        Resume currentResume = this.handleFindResumeById(reqData.getId());
        if (currentResume == null) throw new IdInvalidException("Resume không tồn tại");

        currentResume.setStatus(reqData.getStatus());

        Resume data = this.resumeRepository.save(currentResume);

        res.setId(data.getId());
        res.setUpdatedBy(data.getUpdatedBy());
        res.setUpdatedAt(data.getUpdatedAt());
        res.setCreatedAt(data.getCreatedAt());
        res.setCreatedBy(data.getCreatedBy());

        return res;
    }

    public void handleDeleteResume(Long id) {
        this.resumeRepository.deleteById(id);
    }
}
