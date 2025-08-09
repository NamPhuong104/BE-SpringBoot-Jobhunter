package vn.hoidanit.jobhunter.domain.dto.company;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.hoidanit.jobhunter.domain.Company;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Company toEntity(CompanyDTO.Create dto);

    // 🆕 Mapping cho Update
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Company toEntity(CompanyDTO.Update dto);
}
