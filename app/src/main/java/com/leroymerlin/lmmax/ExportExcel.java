package com.leroymerlin.lmmax;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExportExcel extends Activity {

    Workbook wb = new HSSFWorkbook();
    Sheet sheet = null;
    CellStyle Title = wb.createCellStyle();
    int l = 2, k = -1;
    boolean d = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_excel);
        ((TextView)findViewById(R.id.load_lbl)).setText("Экспортирование Excel");

        Title.setAlignment(CellStyle.ALIGN_CENTER);
        Title.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        Font font = wb.createFont();
        font.setBold(true);
        Title.setFont(font);
        Title.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        sheet = wb.createSheet("Shop" + Memory.city);
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,4));
        Cell c = sheet.createRow(0).createCell(0);
        c.setCellValue("Магазин: " + Memory.city + " Отдел: " + Memory.otdel.replace(Memory.city, ""));
        c.setCellStyle(Title);

        Row r = sheet.createRow(1);

        sheet.setColumnWidth(0, 2000);
        c = r.createCell(0);
        c.setCellValue("Адрес");
        c.setCellStyle(Title);

        sheet.setColumnWidth(1, 3000);
        c = r.createCell(1);
        c.setCellValue("Артикул");
        c.setCellStyle(Title);

        sheet.setColumnWidth(2, 4000);
        c = r.createCell(2);
        c.setCellValue("Штрихкод");
        c.setCellStyle(Title);

        sheet.setColumnWidth(3, 10000);
        c = r.createCell(3);
        c.setCellValue("Наименование");
        c.setCellStyle(Title);

        sheet.setColumnWidth(4, 2500);
        c = r.createCell(4);
        c.setCellValue("Кол-во");
        c.setCellStyle(Title);

        um();
    }

    public void um() {
        Memory.getMetadataBuffer("tvrlst" + Memory.otdel).continueWith(task ->
        {
            if (task.getResult().getCount() > 0) {
                Memory.getTextFromFile(task.getResult()).continueWith(task1 -> {
                    Memory.g = task1.getResult().split("\n");
                    if (Memory.g.length > 0) up(); else Zik();
                    return null;
                });
            } else Zik();
            return null;
        });
    }

    private void up() {
        k++;
        if (k >= Memory.g.length)
            Zik();
        else {
            ((TextView) findViewById(R.id.load_lbl)).setText("Экспортирование Excel\n" + (k + 1) + " из " + Memory.g.length);
            Memory.getMetadataBuffer(Memory.g[k]).continueWith(task ->
            {
                if (task.getResult().getCount() > 0) {
                    m = task.getResult().getCount();
                    o = -1;
                    uz();
                } else {
                    //Memory.RemAtT(Memory.g[k]);
                    if (k < Memory.g.length) up(); else Zik();
                }
                return null;
            });
        }
    }

    int m, o = -1;

    //Заполнение таблицы
    private void uz() {
        o++;
        Memory.getTextFromFile(Memory.g[k], o).continueWith(task -> {
            String[] j = task.getResult().split("\n");

           /* if(Memory.CheckAddress(j[4])) {

                Row ra = sheet.createRow(l);

                Cell ca = ra.createCell(0);
                ca.setCellValue(j[4]);
                ca = ra.createCell(1);
                ca.setCellValue(j[0]);
                ca = ra.createCell(2);
                ca.setCellValue(j[1]);
                ca = ra.createCell(3);
                ca.setCellValue(j[2]);
                ca = ra.createCell(4);
                ca.setCellValue(j[3]);
                l++;
            } else {
                Memory.RemoveTovar(Memory.g[k], o);
                m--;
            }
            if (o < m - 1) uz(); else if (k < Memory.g.length) up(); else Zik();*/
            return null;
        });
    }


    //Сохранение
    private void Zik()
    {
        File file;
        FileOutputStream os = null;
        try {
            String s = System.getenv("EXTERNAL_STORAGE") + "/Download/BaseOfShop_" + Memory.city + "_Time_" + new SimpleDateFormat("HH_mm_dd_MM_yyyy").format(new Date()) + ".xls";
            new File(s).createNewFile();
            file = new File(s);
            os = new FileOutputStream(file);
            wb.write(os);
            showMessage("Успешно сохранено в папку: " + s);
        } catch (IOException e) {
            showMessage("Ошибка записи");
        } catch (Exception e) {
            showMessage("Ошибка сохранения");
            Log.e("TAG", e.toString());
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) { }
        }
        finish();
    }

    protected void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
