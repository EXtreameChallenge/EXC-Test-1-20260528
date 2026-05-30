package com.claw.controller;

import com.claw.service.DataExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="\u6570\u636e\u5bfc\u51fa")
@RestController
@RequestMapping(value={"/api/v1/export"})
public class DataExportController {
    private final DataExportService exportService;

    @Operation(summary="\u5bfc\u51fa\u8f66\u8f86\u6570\u636eCSV")
    @GetMapping(value={"/vehicles"})
    @PreAuthorize(value="hasAuthority('PERM_analytics:view')")
    public ResponseEntity<byte[]> exportVehicles() {
        byte[] data = this.exportService.exportVehiclesCsv();
        return this.csvResponse(data, "vehicles.csv");
    }

    @Operation(summary="\u5bfc\u51fa\u4efb\u52a1\u6570\u636eCSV")
    @GetMapping(value={"/tasks"})
    @PreAuthorize(value="hasAuthority('PERM_analytics:view')")
    public ResponseEntity<byte[]> exportTasks() {
        byte[] data = this.exportService.exportTasksCsv();
        return this.csvResponse(data, "tasks.csv");
    }

    @Operation(summary="\u5bfc\u51fa\u544a\u8b66\u6570\u636eCSV")
    @GetMapping(value={"/alerts"})
    @PreAuthorize(value="hasAuthority('PERM_analytics:view')")
    public ResponseEntity<byte[]> exportAlerts() {
        byte[] data = this.exportService.exportAlertsCsv();
        return this.csvResponse(data, "alerts.csv");
    }

    @Operation(summary="\u5bfc\u51fa\u5de5\u5355\u6570\u636eCSV")
    @GetMapping(value={"/work-orders"})
    @PreAuthorize(value="hasAuthority('PERM_analytics:view')")
    public ResponseEntity<byte[]> exportWorkOrders() {
        byte[] data = this.exportService.exportWorkOrdersCsv();
        return this.csvResponse(data, "work_orders.csv");
    }

    @Operation(summary="\u5bfc\u51fa\u7ef4\u62a4\u9884\u6d4bCSV")
    @GetMapping(value={"/predictions"})
    @PreAuthorize(value="hasAuthority('PERM_analytics:view')")
    public ResponseEntity<byte[]> exportPredictions() {
        byte[] data = this.exportService.exportPredictionsCsv();
        return this.csvResponse(data, "maintenance_predictions.csv");
    }

    @Operation(summary="\u5bfc\u51fa\u5168\u90e8\u6570\u636eZIP")
    @GetMapping(value={"/all"})
    @PreAuthorize(value="hasAuthority('PERM_analytics:view')")
    public ResponseEntity<byte[]> exportAll() {
        byte[] data = this.exportService.exportAllAsZip();
        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=claw_data_export.zip").contentType(MediaType.APPLICATION_OCTET_STREAM).body(data);
    }

    private ResponseEntity<byte[]> csvResponse(byte[] data, String filename) {
        String encodedFilename;
        try { encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.name()).replace("+", "%20"); } catch (Exception e) { encodedFilename = filename; }
        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFilename).contentType(new MediaType("text", "csv", StandardCharsets.UTF_8)).body(data);
    }

    public DataExportController(DataExportService exportService) {
        this.exportService = exportService;
    }
}
