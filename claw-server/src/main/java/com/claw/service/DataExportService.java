package com.claw.service;

import com.claw.entity.DispatchTask;
import com.claw.entity.FaultAlert;
import com.claw.entity.MaintenancePrediction;
import com.claw.entity.Vehicle;
import com.claw.entity.WorkOrder;
import com.claw.mapper.DispatchTaskMapper;
import com.claw.mapper.FaultAlertMapper;
import com.claw.mapper.MaintenancePredictionMapper;
import com.claw.mapper.VehicleMapper;
import com.claw.mapper.WorkOrderMapper;
import com.fasterxml.jackson.core.FormatSchema;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.stereotype.Service;

@Service
public class DataExportService {
    private final VehicleMapper vehicleMapper;
    private final DispatchTaskMapper taskMapper;
    private final FaultAlertMapper alertMapper;
    private final WorkOrderMapper workOrderMapper;
    private final MaintenancePredictionMapper predictionMapper;

    public byte[] exportVehiclesCsv() {
        List<Vehicle> vehicles = this.vehicleMapper.selectList(null);
        return this.generateCsv(vehicles, Vehicle.class);
    }

    public byte[] exportTasksCsv() {
        List<DispatchTask> tasks = this.taskMapper.selectList(null);
        return this.generateCsv(tasks, DispatchTask.class);
    }

    public byte[] exportAlertsCsv() {
        List<FaultAlert> alerts = this.alertMapper.selectList(null);
        return this.generateCsv(alerts, FaultAlert.class);
    }

    public byte[] exportWorkOrdersCsv() {
        List<WorkOrder> orders = this.workOrderMapper.selectList(null);
        return this.generateCsv(orders, WorkOrder.class);
    }

    public byte[] exportPredictionsCsv() {
        List<MaintenancePrediction> predictions = this.predictionMapper.selectList(null);
        return this.generateCsv(predictions, MaintenancePrediction.class);
    }

    public byte[] exportAllAsZip() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);
            this.addToZip(zos, "vehicles.csv", this.exportVehiclesCsv());
            this.addToZip(zos, "tasks.csv", this.exportTasksCsv());
            this.addToZip(zos, "alerts.csv", this.exportAlertsCsv());
            this.addToZip(zos, "work_orders.csv", this.exportWorkOrdersCsv());
            this.addToZip(zos, "maintenance_predictions.csv", this.exportPredictionsCsv());
            zos.close();
            return baos.toByteArray();
        }
        catch (Exception e) {
            throw new RuntimeException("\u5bfc\u51fa\u5931\u8d25: " + e.getMessage());
        }
    }

    private <T> byte[] generateCsv(List<T> data, Class<T> clazz) {
        try {
            CsvMapper csvMapper = new CsvMapper();
            CsvSchema schema = csvMapper.schemaFor(clazz).withHeader();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter((OutputStream)baos, StandardCharsets.UTF_8);
            writer.write(65279);
            csvMapper.writer((FormatSchema)schema).writeValue((Writer)writer, data);
            writer.close();
            return baos.toByteArray();
        }
        catch (Exception e) {
            throw new RuntimeException("CSV\u751f\u6210\u5931\u8d25: " + e.getMessage());
        }
    }

    private void addToZip(ZipOutputStream zos, String fileName, byte[] data) throws Exception {
        ZipEntry entry = new ZipEntry(fileName);
        zos.putNextEntry(entry);
        zos.write(data);
        zos.closeEntry();
    }

    public DataExportService(VehicleMapper vehicleMapper, DispatchTaskMapper taskMapper, FaultAlertMapper alertMapper, WorkOrderMapper workOrderMapper, MaintenancePredictionMapper predictionMapper) {
        this.vehicleMapper = vehicleMapper;
        this.taskMapper = taskMapper;
        this.alertMapper = alertMapper;
        this.workOrderMapper = workOrderMapper;
        this.predictionMapper = predictionMapper;
    }
}
