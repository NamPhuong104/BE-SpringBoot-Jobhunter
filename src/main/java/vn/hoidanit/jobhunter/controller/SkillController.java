package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;


@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("/skills/{id}")
    @ApiMessage("Get skill by id")
    public ResponseEntity<Skill> getSkillByid(@Valid @PathVariable("id") Long id) throws IdInvalidException {
        Skill existSkill = this.skillService.handleFineOneSkill(id);
        if (existSkill == null) throw new IdInvalidException("Id không tồn tại !!!");
        return ResponseEntity.ok(existSkill);
    }

    @GetMapping("/skills")
    @ApiMessage("Get all skills")
    public ResponseEntity<ResultPaginationDTO> getAllSkill(@Filter Specification<Skill> spec, Pageable pageable) {
        ResultPaginationDTO res = this.skillService.handleGetAllSkill(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(res);

    }

    @PostMapping("/skills")
    @ApiMessage("Create skill")
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill reqSkill) throws IdInvalidException {
        Skill res = this.skillService.handleCreateSkill(reqSkill);
        if (res == null) throw new IdInvalidException(reqSkill.getName() + " Đã tồn tại !!!");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/skills")
    @ApiMessage("Update skill")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill reqskill) throws IdInvalidException {
        if (reqskill.getId() == 0) throw new IdInvalidException("Id không đc bỏ trống");
        Skill res = this.skillService.handleUpdateSkill(reqskill);
        if (res == null) throw new IdInvalidException("id " + reqskill.getId() + " không tồn tại !!!");
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete skill")
    public ResponseEntity<Void> deleteSkill(@Valid @PathVariable("id") Long id) throws IdInvalidException {
        Skill isExistSkill = this.skillService.handleFineOneSkill(id);
        if (isExistSkill == null) throw new IdInvalidException("Id không tồn tại !!!");
        return ResponseEntity.ok(null);
    }
}
