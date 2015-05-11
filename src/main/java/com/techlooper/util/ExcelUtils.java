package com.techlooper.util;

import com.techlooper.entity.JobEntity;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by NguyenDangKhoa on 5/6/15.
 */
public class ExcelUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);

    public static void exportSalaryReport(List<JobEntity> jobs) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Jobs");

        Row headerRow = sheet.createRow(1);
        Cell cell = headerRow.createCell(0);
        cell.setCellValue("Job ID");
        cell = headerRow.createCell(1);
        cell.setCellValue("Job Title");
        cell = headerRow.createCell(2);
        cell.setCellValue("Salary Min");
        cell = headerRow.createCell(3);
        cell.setCellValue("Salary Max");

        Map<String, Object[]> data = new HashMap<>();
        for (int i = 0; i < jobs.size(); i++) {
            JobEntity job = jobs.get(i);
            data.put(String.valueOf(i), new Object[]{job.getId(), job.getJobTitle(), job.getSalaryMin(), job.getSalaryMax()});
        }

        Set<String> keySet = data.keySet();
        int rowNum = 1;
        for (String key : keySet) {
            Row contentRow = sheet.createRow(rowNum++);
            Object[] objArr = data.get(key);
            int cellNum = 0;
            for (Object obj : objArr) {
                Cell contentCell = contentRow.createCell(cellNum++);
                if (obj instanceof String) {
                    contentCell.setCellValue((String) obj);
                } else if (obj instanceof Long)
                    contentCell.setCellValue((Long) obj);
            }
            Cell avgSalaryCell = contentRow.createCell(cellNum++);
            String salaryMinCell = "C" + rowNum;
            String salaryMaxCell = "D" + rowNum;
            avgSalaryCell.setCellType(Cell.CELL_TYPE_FORMULA);
            avgSalaryCell.setCellFormula("IF("+salaryMinCell+"=0,"+salaryMaxCell+"*0.75,IF("+salaryMaxCell+"=0,"+salaryMinCell+"*1.25,("+salaryMinCell+"+"+salaryMaxCell+")/2))");
        }

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_hh-mm");
            String exportFileName = "salary_report_" + simpleDateFormat.format(new Date()) + ".xls";
            FileOutputStream out = new FileOutputStream(new File(exportFileName));
            workbook.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
