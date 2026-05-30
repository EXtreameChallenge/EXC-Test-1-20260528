package com.claw.service;

import com.claw.entity.CompanyInfo;
import com.claw.mapper.CompanyInfoMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyInfoService {
    private final CompanyInfoMapper companyInfoMapper;

    public CompanyInfoService(CompanyInfoMapper companyInfoMapper) {
        this.companyInfoMapper = companyInfoMapper;
    }

    public CompanyInfo getCompanyInfo() {
        List<CompanyInfo> list = companyInfoMapper.selectList(null);
        if (list.isEmpty()) {
            CompanyInfo defaultInfo = new CompanyInfo();
            defaultInfo.setName("轻行Claw智行科技有限公司");
            defaultInfo.setAddress("北京市海淀区中关村科技园");
            defaultInfo.setContact("010-88888888");
            defaultInfo.setFleetSize(12);
            companyInfoMapper.insert(defaultInfo);
            return defaultInfo;
        }
        return list.get(0);
    }

    @Transactional
    public CompanyInfo updateCompanyInfo(CompanyInfo info) {
        CompanyInfo existing = getCompanyInfo();
        if (info.getName() != null) existing.setName(info.getName());
        if (info.getAddress() != null) existing.setAddress(info.getAddress());
        if (info.getContact() != null) existing.setContact(info.getContact());
        if (info.getFleetSize() != null) existing.setFleetSize(info.getFleetSize());
        existing.setUpdatedAt(LocalDateTime.now());
        companyInfoMapper.updateById(existing);
        return existing;
    }
}
