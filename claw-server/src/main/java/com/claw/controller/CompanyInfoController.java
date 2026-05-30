package com.claw.controller;

import com.claw.common.Result;
import com.claw.entity.CompanyInfo;
import com.claw.service.CompanyInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "公司信息")
@RestController
@RequestMapping(value = {"/api/v1/company-info"})
public class CompanyInfoController {
    private final CompanyInfoService companyInfoService;

    public CompanyInfoController(CompanyInfoService companyInfoService) {
        this.companyInfoService = companyInfoService;
    }

    @Operation(summary = "获取公司信息")
    @GetMapping
    public Result<CompanyInfo> get() {
        return Result.success(companyInfoService.getCompanyInfo());
    }

    @Operation(summary = "更新公司信息")
    @PutMapping
    public Result<CompanyInfo> update(@RequestBody CompanyInfo info) {
        return Result.success(companyInfoService.updateCompanyInfo(info));
    }
}
