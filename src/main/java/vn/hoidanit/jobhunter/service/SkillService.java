package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.controller.SkillController;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.SkillRepository;

import java.util.Optional;

@Service
public class SkillService {
    private SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }


    public Skill handleFineOneSkill(long id) {
        Optional<Skill> skill = this.skillRepository.findById(id);
        if (skill.isPresent()) {
            return skill.get();
        }
        return null;
    }

    public ResultPaginationDTO handleGetAllSkill(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pageSkill = this.skillRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageSkill.getTotalPages());
        mt.setTotal(pageSkill.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageSkill.getContent());
        return rs;
    }

    public Skill handleCreateSkill(Skill skill) {
        boolean isExist = this.skillRepository.existsByName(skill.getName());
        if (!isExist) {
            return this.skillRepository.save(skill);
        }
        return null;
    }

    public Skill handleUpdateSkill(Skill skill) {
        Skill existsSkill = this.handleFineOneSkill(skill.getId());
        if (existsSkill != null) {
            existsSkill.setName(skill.getName());
            return this.skillRepository.save(existsSkill);
        }
        return null;
    }

    public void handleDeleteSkill(Long id) {
        Skill skillOptional = this.handleFineOneSkill(id);

        // Delete job and subscriber (inside job_skill and subscriber_skill table)
        if (skillOptional != null) {
            skillOptional.getJobs().forEach(job -> job.getSkills().remove(skillOptional));
            skillOptional.getSubscribers().forEach(subs -> subs.getSkills().remove(skillOptional));
            this.skillRepository.delete(skillOptional);
        }
    }
}
