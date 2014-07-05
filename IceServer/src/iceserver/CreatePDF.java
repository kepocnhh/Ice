package iceserver;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.Math.abs;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ice.*;
import java.util.Calendar;

public class CreatePDF
{

    private static Document document;
    private static Font font;
    private static Font fonttitle;
    private static String pdfrath = "C:\\ForIce\\";
    private static String pdfname = "PDFforIce";
    private static String slasht = "    ";//"+slasht+"
    private static String slashn = System.getProperty("line.separator");

    public static void _CreatePDF(Strings strlist,
            user newuser,
            DataForRecord dfropen,
            Date pingdate,
            String pdfp,
            String pdfn) throws DocumentException
    {
        pdfrath = pdfp;
        pdfname = pdfn;
        BaseColor CW = new BaseColor(255, 0, 0);
        int BW = 0;
        DataForRecord dfr;
        String[] str;
        List<String> strl = new ArrayList<String>();
        double forcass[];
        try
        {
            createpdf();
            setfont();
        }
        catch (FileNotFoundException ex)
        {
            System.out.println(ex.toString());
            return;
        }
        catch (IOException ex)
        {
            System.out.println(ex.toString());
            return;
        }
        //
        PdfPTable nestedTable = new PdfPTable(3);
        nestedTable.setWidthPercentage(99);
        PdfPCell cll = new PdfPCell(new Phrase("title"));
        PdfPTable tbl = null;
        cll.addElement(new Paragraph(dfropen.nameshop, fonttitle));
        cll.setColspan(1);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//title///////////////////////////////////////////
        cll = new PdfPCell(new Phrase("title"));
        cll.addElement(new Paragraph(dateonly(new Date()), fonttitle));
        cll.setColspan(1);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//title///////////////////////////////////////////
        cll = new PdfPCell(new Phrase("title"));
        cll.addElement(new Paragraph(dateweektoString(new Date()), fonttitle));
        cll.setColspan(1);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//title///////////////////////////////////////////
        cll = new PdfPCell(new Phrase("0"));
        cll.addElement(new Paragraph(strlist.PDFheader.get(0), font));
        tbl = new PdfPTable(1);
        tbl.addCell(new Paragraph(newuser.GetSurname() + " " + newuser.GetName() + " " + newuser.GetPatronymic(), font));
        cll.addElement(tbl);
        tbl = new PdfPTable(1);
        cll.addElement(new Paragraph("Точка", font));
        tbl.addCell(new Paragraph(dfropen.nameshop, font));
        cll.addElement(tbl);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//0///////////////////////////////////////////
        tbl.flushContent();
        cll = new PdfPCell(new Phrase("Cell 6"));
        tbl = null;
        tbl = new PdfPTable(1);
        cll.addElement(new Paragraph(strlist.PDFheader.get(1), font));
        tbl.addCell(new Paragraph(dfropen.GetX() + " " + dfropen.GetY(), font));
        cll.addElement(tbl);
        //tbl.flushContent();
        tbl = new PdfPTable(1);
        cll.addElement(new Paragraph(strlist.PDFheader.get(2), font));
        tbl.addCell(new Paragraph(datetostring(new Date()), font));
        cll.addElement(tbl);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//02///////////////////////////////////////////
        tbl.flushContent();
        cll = new PdfPCell(new Phrase("Cell 6"));
        tbl = null;
        tbl = new PdfPTable(2);
         String myminutes="";
        myminutes=""+pingdate.getMinutes();
        if(myminutes.length()==1)
        {
            myminutes="0"+myminutes;
        }
        tbl.addCell(new Paragraph(strlist.PDFheader.get(3) + " - " +pingdate.getHours()+":"+myminutes, font));
        myminutes=""+dfropen.GetDate().getMinutes();
        if(myminutes.length()==1)
        {
            myminutes="0"+myminutes;
        }
        tbl.addCell(new Paragraph("Время отпр. отчёта - "+dfropen.GetDate().getHours()+":"+myminutes, font));
        cll.addElement(tbl);
        //
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//03///////////////////////////////////////////
        tbl.flushContent();
        cll = new PdfPCell(new Paragraph("Cell 3"));
        double dmass[]=new double[dfropen.matrix[0].length];
        double amountbag = 0;//количество мешков
        for (int i = 0; i < dfropen.matrix[0].length; i++)
        {
            amountbag += dfropen.matrix[0][i];
        }
        str = ("Всего мешков (шт)\t"
                + amountbag + "\t"
                + "-\t"
                + "-\t"
                + "-\t").split("\t");
        tbl = createtable(dfropen.matrix[0],dmass ,dmass,dmass,
                getmass(strlist.DataSubSale, 0), strlist.DataSale.get(0), strlist.SessionTypes, str);
        tbl.setWidthPercentage(111);
        cll.addElement(tbl);
        cll.setColspan(1);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//1///////////////////////////////////////////
        tbl.flushContent();
        strl.add("Касса/открытие");
        forcass = new double[3];
        forcass[0] = dfropen.getCash();
        tbl = createsimple2table(strl, forcass);
        tbl.setWidthPercentage(88);
        cll.addElement(tbl);
        strl.clear();
        cll.setColspan(1);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//2///////////////////////////////////////////
        tbl.flushContent();
        cll = new PdfPCell(new Paragraph("Cell 3"));
        str = (strlist.DataSale.get(5) + "\t"
                + "ОТКРЫТИЕ\t"
                + "ЗАКРЫТИЕ").split("\t");
        strl.add(getmass(strlist.DataSubSale, 5)[0]);
        strl.add(getmass(strlist.DataSubSale, 5)[1]);
        dmass=new double[dfropen.matrix[5].length];
        tbl = createsimple3table(str, strl, dfropen.matrix[5],dmass);
        tbl.setWidthPercentage(88);
        cll.addElement(tbl);
        cll.addElement(new Paragraph(" "));
        dmass=new double[dfropen.matrix[2].length];
        tbl = createtable(dfropen.matrix[2],dmass,dmass,dmass,
                getmass(strlist.DataSubSale, 2), "Разное", strlist.SessionTypes, null);
        tbl.setWidthPercentage(88);
        cll.addElement(tbl);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//3///////////////////////////////////////////
        tbl.flushContent();
        cll = new PdfPCell(new Paragraph("Cell 3"));
        double weightall = 0;//вес мешков
        double weightin = 0;//вес мешков внутри холодильника
        for (int i = 0; i < dfropen.matrix[1].length; i++)
        {
            weightall += dfropen.matrix[1][i];
        }
            weightin= amountbag * 2400;
        str = ("ВСЕГО\t"
                + weightall + "\t"
                +"-\t"
                +"-\t"
                +"-\t"
                + "ВЕС В СКЛАД.ХОЛ.\t"
                + weightin + "\t"
                +"-\t"
                +"-\t"
                +"-\t").split("\t");
        dmass=new double[dfropen.matrix[1].length];
        tbl = createtable(dfropen.matrix[1],dmass,dmass,dmass,
                getmass(strlist.DataSubSale, 1), strlist.DataSale.get(1), strlist.SessionTypes, str);
        tbl.setWidthPercentage(111);
        cll.addElement(tbl);
        cll.setColspan(1);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//4///////////////////////////////////////////
        tbl.flushContent();
        cll = new PdfPCell(new Paragraph("Cell 3"));
        double masss[] = new double[1];//стаканы
        double massk[] = new double[1];//кепки
        double masst[] = new double[1];//термосы
        dmass=new double[dfropen.matrix[3].length];
        masss = getmass(dfropen.matrix[3],dmass,dmass,dmass, 0);
        massk = new double[4];
        for (int i = 0; i < dfropen.matrix[4].length; i++)
        {
            massk[0] += dfropen.matrix[4][i];
        }
        masst = getmass(dfropen.matrix[3],dmass,dmass,dmass, 1);
        str = (" \t"
                + "Стаканчик\t"
                + "Кепка\t"
                + "Термос").split("\t");
        tbl = createtable(masss, massk, masst, "ОТКРЫТИЕ\tПРИХОД\tУХОД\tЗАКРЫТИЕ".split("\t"), strlist.DataSale.get(3), str);
        tbl.setWidthPercentage(88);
        cll.addElement(tbl);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//5///////////////////////////////////////////
        tbl.flushContent();
        cll = new PdfPCell(new Paragraph("Cell 3"));
        dmass=new double[dfropen.matrix[4].length];
        tbl = createtable(dfropen.matrix[4],dmass,dmass,dmass,
                getmass(strlist.DataSubSale, 4), strlist.DataSale.get(4), strlist.SessionTypes, null);
        tbl.setWidthPercentage(88);
        cll.addElement(new Paragraph(" "));
        cll.addElement(tbl);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//6///////////////////////////////////////////
        //
        document.add(nestedTable);
        document.close();
    }

    public static void _CreatePDF(Strings strlist,
            user newuser,
            DataForRecord dfropen,
            DataForRecord dfrdrug,
            DataForRecord dfrsteal,
            DataForRecord dfrclose,
            Date pingdate,Date pingdateend,
            double cass[], double prom[], double inkd[], String inks[],
            String pdfp,
            String pdfn) throws DocumentException
    {
        pdfrath = pdfp;
        pdfname = pdfn;
        BaseColor CW = new BaseColor(255, 0, 255);
        int BW = 0;
        DataForRecord dfr;
        String[] str;
        List<String> strl = new ArrayList<String>();
        int hoursbegin=10;//начало раб дня
        double forcass[];
        //выручки
        double cashall;//всего выручка
        double cashs[] = new double[1];//стаканы
        double cashk[] = new double[1];//кепки
        double casht[] = new double[1];//термосы
        double masss[] = new double[1];//стаканы
        double massk[] = new double[1];//кепки
        double masst[] = new double[1];//термосы
        double weights[] = new double[1];//вес стаканы
        double weightk[] = new double[1];//вес кепки
        double weightt[] = new double[1];//вес термосы
        //вес на точке
        double amountbag[] = new double[4];//количество мешков
        double weightall[] = new double[4];//вес всего
        double weightin[] = new double[4];//вес всего в холодильнике
        double weightsell;//вес всего проданного
        double weightskt;//вес стаканы кепки термосы
        //продано штук
        double amounts;//стаканы
        double amountk;//кепки
        double amountt;//термосы
        double mulct;//штраф вес
        double mulctdelay;//штраф опоздание
        double mulctall;//штраф
        double salary;//зарплата за время
        double salaryprcnt;//зарплата за проценты
        double salaryall;//зарплата
        String mulcttitle="ШТРАФ";
        int num = 5;
        try
        {
            createpdf();
            setfont();
        }
        catch (FileNotFoundException ex)
        {
            System.out.println(ex.toString());
            return;
        }
        catch (IOException ex)
        {
            System.out.println(ex.toString());
            return;
        }

        PdfPTable nestedTable = new PdfPTable(3);
        nestedTable.setWidthPercentage(99);
        PdfPCell cll = new PdfPCell(new Phrase("title"));
        PdfPTable tbl = null;
        cll.addElement(new Paragraph(dfropen.nameshop, fonttitle));
        cll.setColspan(1);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//title///////////////////////////////////////////
        cll = new PdfPCell(new Phrase("title"));
        cll.addElement(new Paragraph(dateonly(new Date()), fonttitle));
        cll.setColspan(1);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//title///////////////////////////////////////////
        cll = new PdfPCell(new Phrase("title"));
        cll.addElement(new Paragraph(dateweektoString(new Date()), fonttitle));
        cll.setColspan(1);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//title///////////////////////////////////////////
        cll = new PdfPCell(new Phrase("title"));
        cll.addElement(new Paragraph(strlist.PDFheader.get(0), font));
        tbl = new PdfPTable(1);
        tbl.addCell(new Paragraph(newuser.GetSurname() + " " + newuser.GetName() + " " + newuser.GetPatronymic(), font));
        cll.addElement(tbl);
        tbl = new PdfPTable(1);
        cll.addElement(new Paragraph("Точка", font));
        tbl.addCell(new Paragraph(dfropen.nameshop, font));
        cll.addElement(tbl);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//0///////////////////////////////////////////
        tbl.flushContent();
        cll = new PdfPCell(new Phrase("Cell 6"));
        tbl = null;
        tbl = new PdfPTable(1);
        cll.addElement(new Paragraph(strlist.PDFheader.get(1), font));
        tbl.addCell(new Paragraph(dfrclose.GetX() + " " + dfrclose.GetY(), font));
        cll.addElement(tbl);
        //tbl.flushContent();
        tbl = new PdfPTable(1);
        cll.addElement(new Paragraph(strlist.PDFheader.get(2), font));
        tbl.addCell(new Paragraph(datetostring(new Date()), font));
        cll.addElement(tbl);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//02///////////////////////////////////////////
        tbl.flushContent();
        cll = new PdfPCell(new Phrase("Cell 6"));
        tbl = null;
        tbl = new PdfPTable(2);
         String myminutes="";
        myminutes=""+pingdate.getMinutes();
        if(myminutes.length()==1)
        {
            myminutes="0"+myminutes;
        }
        tbl.addCell(new Paragraph(strlist.PDFheader.get(3) + " - " +pingdate.getHours()+":"+myminutes, font));
        myminutes=""+dfropen.GetDate().getMinutes();
        if(myminutes.length()==1)
        {
            myminutes="0"+myminutes;
        }
        tbl.addCell(new Paragraph("Время отпр. отчёта - "+dfropen.GetDate().getHours()+":"+myminutes, font));
        cll.addElement(tbl);
        //tbl.flushContent();
        tbl = new PdfPTable(2);
        //cll.addElement(new Paragraph(strlist.PDFheader.get(4), font));
        String myminutes2="";
        myminutes2=""+pingdateend.getMinutes();
        if(myminutes2.length()==1)
        {
            myminutes2="0"+myminutes2;
        }
        tbl.addCell(new Paragraph(strlist.PDFheader.get(4)+" - " + pingdateend.getHours()+":"+myminutes2, font));
        myminutes2=""+dfrclose.GetDate().getMinutes();
        if(myminutes2.length()==1)
        {
            myminutes2="0"+myminutes2;
        }
        tbl.addCell(new Paragraph("Время отпр. отчёта - " + dfrclose.GetDate().getHours()+":"+myminutes2, font));
        cll.addElement(tbl);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//03///////////////////////////////////////////
        tbl.flushContent();
        cll = new PdfPCell(new Paragraph("Cell 3"));
        for (int i = 0; i < dfropen.matrix[0].length; i++)
        {
            amountbag[0] += dfropen.matrix[0][i];
        }
        for (int i = 0; i < dfrdrug.matrix[0].length; i++)
        {
            amountbag[1] += dfrdrug.matrix[0][i];
        }
        for (int i = 0; i < dfrsteal.matrix[0].length; i++)
        {
            amountbag[2] += dfrsteal.matrix[0][i];
        }
        for (int i = 0; i < dfrclose.matrix[0].length; i++)
        {
            amountbag[3] += dfrclose.matrix[0][i];
        }
        str = ("Всего мешков (шт)\t"
                + amountbag[0] + "\t"
                + amountbag[1] + "\t"
                + amountbag[2] + "\t"
                + amountbag[3]).split("\t");
        tbl = createtable(dfropen.matrix[0], dfrdrug.matrix[0], dfrsteal.matrix[0], dfrclose.matrix[0],
                getmass(strlist.DataSubSale, 0), strlist.DataSale.get(0), strlist.SessionTypes, str);
        tbl.setWidthPercentage(111);
        cll.addElement(tbl);
        //
        cll.addElement(new Paragraph(" "));
        //tbl.flushContent();
        //cll = new PdfPCell(new Paragraph("Cell 3"));
        for (int i = 0; i < dfropen.matrix[1].length; i++)
        {
            weightall[0] += dfropen.matrix[1][i];
        }
        for (int i = 0; i < dfrdrug.matrix[1].length; i++)
        {
            weightall[1] += dfrdrug.matrix[1][i];
        }
        for (int i = 0; i < dfrsteal.matrix[1].length; i++)
        {
            weightall[2] += dfrsteal.matrix[1][i];
        }
        for (int i = 0; i < dfrclose.matrix[1].length; i++)
        {
            weightall[3] += dfrclose.matrix[1][i];
        }
        for (int i = 0; i < amountbag.length; i++)
        {
            weightin[i] = amountbag[i] * 2400;
        }
        for (int i = 0; i < weightin.length; i++)
        {
            weightall[i] += weightin[i];
        }
        str = ("ВСЕГО\t"
                + weightall[0] + "\t"
                + weightall[1] + "\t"
                + weightall[2] + "\t"
                + weightall[3] + "\t"
                + "ВЕС В СКЛАД.ХОЛ.\t"
                + weightin[0] + "\t"
                + weightin[1] + "\t"
                + weightin[2] + "\t"
                + weightin[3]).split("\t");
        tbl = createtable(dfropen.matrix[1], dfrdrug.matrix[1], dfrsteal.matrix[1], dfrclose.matrix[1],
                getmass(strlist.DataSubSale, 1), strlist.DataSale.get(1), strlist.SessionTypes, str);
        tbl.setWidthPercentage(111);
        cll.addElement(tbl);
        //
        cll.addElement(new Paragraph("ВЕС ВСЕГО ПРОДАННОГО МОРОЖЕННОГО" + slashn + "ИЗ УЧЁТА оставшегося мороженного -", font));
        weightsell = weightall[0] + weightall[1] - weightall[2] - weightall[3];
        tbl = new PdfPTable(1);
        tbl.addCell(new Paragraph(" " + weightsell));
        cll.addElement(tbl);
        //
        cll.setColspan(1);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//1///////////////////////////////////////////
        tbl.flushContent();
        cll = new PdfPCell(new Phrase("Cell 6"));
        masss = getmass(dfropen.matrix[3], dfrdrug.matrix[3], dfrsteal.matrix[3], dfrclose.matrix[3], 0);
        massk = new double[4];
        for (int i = 0; i < dfropen.matrix[4].length; i++)
        {
            massk[0] += dfropen.matrix[4][i];
        }
        for (int i = 0; i < dfrdrug.matrix[4].length; i++)
        {
            massk[1] += dfrdrug.matrix[4][i];
        }
        for (int i = 0; i < dfrsteal.matrix[4].length; i++)
        {
            massk[2] += dfrsteal.matrix[4][i];
        }
        for (int i = 0; i < dfrclose.matrix[4].length; i++)
        {
            massk[3] += dfrclose.matrix[4][i];
        }
        masst = getmass(dfropen.matrix[3], dfrdrug.matrix[3], dfrsteal.matrix[3], dfrclose.matrix[3], 1);
        amounts = getresultmass(dfropen.matrix[3], dfrdrug.matrix[3], dfrsteal.matrix[3], dfrclose.matrix[3], 0);
        amountk = massk[0] + massk[1] - massk[2] - massk[3];
        amountt = getresultmass(dfropen.matrix[3], dfrdrug.matrix[3], dfrsteal.matrix[3], dfrclose.matrix[3], 1);
        cashs[0] = amounts * 100;
        cashk[0] = amountk * 150;
        casht[0] = amountt * 400;
        cashall = cashs[0] + cashk[0] + casht[0];
        strl.add("Выручка");
        strl.add("Касса/открытие");
        strl.add("Касса/закрытие");
        forcass = new double[3];
        forcass[0] = cashall;
        forcass[1] = dfropen.getCash();
        forcass[2] = dfrclose.getCash();
        tbl = createsimple2table(strl, forcass);
        tbl.setWidthPercentage(88);
        cll.addElement(tbl);
        strl.clear();
        for (int i = 0; i < inks.length; i++)
        {
            strl.add(strlist.DataCass.get(0) + ": " + inks[i]);
        }
        for (int i = 0; i < prom.length; i++)
        {
            strl.add(strlist.DataCass.get(1));
        }
        for (int i = 0; i < cass.length; i++)
        {
            strl.add(strlist.DataCass.get(2));
        }
        forcass = new double[inkd.length + prom.length + cass.length];
        for (int i = 0; i < inkd.length; i++)
        {
            forcass[i] = inkd[i];
        }
        for (int i = inkd.length; i - inkd.length < prom.length; i++)
        {
            forcass[i] = prom[i - inkd.length];
        }
        for (int i = inkd.length + prom.length; i - inkd.length - prom.length < cass.length; i++)
        {
            forcass[i] = cass[i - inkd.length - prom.length];
        }
        tbl = createsimple2table(strl, forcass);
        tbl.setWidthPercentage(88);
        cll.addElement(new Paragraph(" "));
        cll.addElement(tbl);
        strl.clear();
        cll.addElement(new Paragraph(" "));
        str = (" \t"
                + "Стаканчик\t"
                + "Кепка\t"
                + "Термос").split("\t");
        tbl = createtable(cashs, cashk, casht,
                "Выручка".split("\t"), "Всего выручка: " + (cashs[0] + cashk[0] + casht[0]), str);
        tbl.setWidthPercentage(88);
        cll.addElement(tbl);
        //
        str = ("Продано штук\t"
                + amounts + "\t"
                + amountk + "\t"
                + amountt + "\t"
                + " \t"
                + "Стаканчик\t"
                + "Кепка\t"
                + "Термос").split("\t");
        tbl = createtable(masss, massk, masst, "ОТКРЫТИЕ\tПРИХОД\tУХОД\tЗАКРЫТИЕ".split("\t"), strlist.DataSale.get(3), str);
        tbl.setWidthPercentage(88);
        cll.addElement(new Paragraph(" ", font));
        cll.addElement(tbl);
        weights[0] = amounts * 60;
        weightk[0] = amountk * 80;
        weightt[0] = amountt * 240;
        str = (" \tСтаканчик\t"
                + "Кепка\t"
                + "Термос").split("\t");
        tbl = createtable(weights, weightk, weightt,
                "Вес".split("\t"), "Вес проданных стаканчиков/кепок/термосов", str);
        tbl.setWidthPercentage(88);
        tbl.addCell(new PdfPCell(new Phrase("Общий вес", font)));
        weightskt = (weights[0] + weightk[0] + weightt[0]);
        PdfPCell cell3 = new PdfPCell(new Phrase("" + weightskt, font));
        cell3.setColspan(3);
        tbl.addCell(cell3);
        cll.addElement(new Paragraph(" "));
        cll.addElement(tbl);
        //
        cll.addElement(new Paragraph(" "));
        cll.addElement(new Paragraph("ВЕС ВСЕГО ПРОДАННОГО МОРОЖЕННОГО" + slashn + "ИЗ УЧЁТА СТАКАНОВ, КЕПОК И ТЕРМОСОВ - ", font));
        //tbl.flushContent();
        tbl = new PdfPTable(1);
        tbl.addCell(new Paragraph(" " + weightskt));
        cll.addElement(tbl);
        tbl = new PdfPTable(2);
        if (weightskt - weightsell > 200)
        {
            //mulct = (weightskt - weightsell);
            //mulcttitle+=" - недосып";
                mulct = 0;
                mulcttitle+=" - вес";
        }
        else
        {
            if (weightsell -  weightskt > 200)
            {
                mulct = (weightskt - weightsell) * 1.66;
                mulcttitle+=" - пересып";
            }
            else
            {
                mulct = 0;
                mulcttitle+=" - вес";
            }
        }
        tbl.addCell(new Paragraph(mulcttitle, font));
        tbl.addCell(new Paragraph(" " + mulct));
        if(dfropen.GetDate().getHours()<hoursbegin)
        {
            mulctdelay=0;
        }
        else
        {
            mulctdelay=(dfropen.GetDate().getHours()-hoursbegin)*60*15;
            if(mulctdelay<0)
                mulctdelay=0;
            mulctdelay+=dfropen.GetDate().getMinutes()*15;
        }
        tbl.addCell(new Paragraph("ШТРАФ - опоздание", font));
        tbl.addCell(new Paragraph(" " + mulctdelay));
        cll.addElement(tbl);
        //
        cll.setColspan(1);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//2///////////////////////////////////////////
        tbl.flushContent();
        cll = new PdfPCell(new Paragraph("Cell 3"));
        str = (strlist.DataSale.get(5) + "\t"
                + "ОТКРЫТИЕ\t"
                + "ЗАКРЫТИЕ").split("\t");
        strl.add(getmass(strlist.DataSubSale, 5)[0]);
        strl.add(getmass(strlist.DataSubSale, 5)[1]);
        tbl = createsimple3table(str, strl, dfropen.matrix[5], dfrclose.matrix[5]);
        tbl.setWidthPercentage(99);
        cll.addElement(tbl);
        cll.addElement(new Paragraph(" "));
        tbl = createtable(dfropen.matrix[2], dfrdrug.matrix[2], dfrsteal.matrix[2], dfrclose.matrix[2],
                getmass(strlist.DataSubSale, 2), "Разное", strlist.SessionTypes, null);
        tbl.setWidthPercentage(99);
        cll.addElement(tbl);
        //
        cll.addElement(new Paragraph(" "));
        tbl = createtable(dfropen.matrix[4], dfrdrug.matrix[4], dfrsteal.matrix[4], dfrclose.matrix[4],
                getmass(strlist.DataSubSale, 4), strlist.DataSale.get(4), strlist.SessionTypes, null);
        tbl.setWidthPercentage(99);
        cll.addElement(tbl);
        //
        cll.addElement(new Paragraph("Заработанные деньги", font));
        //tbl.flushContent();
        tbl = new PdfPTable(2);
        Calendar calendaropen1 = Calendar.getInstance();
        calendaropen1.setTime(dfropen.GetDate());
        Calendar calendarclose1 = Calendar.getInstance();
        calendarclose1.setTime(dfrclose.GetDate());
        long diff1 = calendarclose1.getTimeInMillis() - calendaropen1.getTimeInMillis();
        long seconds1 = diff1 / 1000;
        long minutes1 = seconds1 / 60;
        salary=minutes1*110/60;
        tbl.addCell(new Paragraph("За время работы", font));
        tbl.addCell(new Paragraph(" " + salary));
        salaryprcnt=(cashk[0]/100)*5;
        tbl.addCell(new Paragraph("Плюс за проценты", font));
        tbl.addCell(new Paragraph(" " + salaryprcnt));
        tbl.addCell(new Paragraph("Всего", font));
        salaryall=salaryprcnt + salary;
        tbl.addCell(new Paragraph(" " + (salaryall)));
        tbl.addCell(new Paragraph("Штраф", font));
        mulctall=mulctdelay + mulct;
        tbl.addCell(new Paragraph(" " + (mulctall)));
        tbl.addCell(new Paragraph("Итого", font));
        tbl.addCell(new Paragraph(" " + (salaryall - mulctall)));
        cll.addElement(tbl);
        //
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//3///////////////////////////////////////////
        /*
        tbl.flushContent();
        cll = new PdfPCell(new Paragraph("Cell 3"));
        for (int i = 0; i < dfropen.matrix[1].length; i++)
        {
            weightall[0] += dfropen.matrix[1][i];
        }
        for (int i = 0; i < dfrdrug.matrix[1].length; i++)
        {
            weightall[1] += dfrdrug.matrix[1][i];
        }
        for (int i = 0; i < dfrsteal.matrix[1].length; i++)
        {
            weightall[2] += dfrsteal.matrix[1][i];
        }
        for (int i = 0; i < dfrclose.matrix[1].length; i++)
        {
            weightall[3] += dfrclose.matrix[1][i];
        }
        for (int i = 0; i < amountbag.length; i++)
        {
            weightin[i] = amountbag[i] * 2400;
        }
        for (int i = 0; i < weightin.length; i++)
        {
            weightall[i] += weightin[i];
        }
        str = ("ВСЕГО\t"
                + weightall[0] + "\t"
                + weightall[1] + "\t"
                + weightall[2] + "\t"
                + weightall[3] + "\t"
                + "ВЕС В СКЛАД.ХОЛ.\t"
                + weightin[0] + "\t"
                + weightin[1] + "\t"
                + weightin[2] + "\t"
                + weightin[3]).split("\t");
        tbl = createtable(dfropen.matrix[1], dfrdrug.matrix[1], dfrsteal.matrix[1], dfrclose.matrix[1],
                getmass(strlist.DataSubSale, 1), strlist.DataSale.get(1), strlist.SessionTypes, str);
        tbl.setWidthPercentage(111);
        cll.addElement(tbl);
        cll.setColspan(1);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//4///////////////////////////////////////////
        tbl.flushContent();
        cll = new PdfPCell(new Paragraph("Cell 3"));
        str = ("Продано штук\t"
                + amounts + "\t"
                + amountk + "\t"
                + amountt + "\t"
                + " \t"
                + "Стаканчик\t"
                + "Кепка\t"
                + "Термос").split("\t");
        tbl = createtable(masss, massk, masst, "ОТКРЫТИЕ\tПРИХОД\tУХОД\tЗАКРЫТИЕ".split("\t"), strlist.DataSale.get(3), str);
        tbl.setWidthPercentage(88);
        cll.addElement(tbl);
        weights[0] = amounts * 60;
        weightk[0] = amountk * 80;
        weightt[0] = amountt * 240;
        str = (" \tСтаканчик\t"
                + "Кепка\t"
                + "Термос").split("\t");
        tbl = createtable(weights, weightk, weightt,
                "Вес".split("\t"), "Вес проданных стаканчиков/кепок/термосов", str);
        tbl.setWidthPercentage(88);
        tbl.addCell(new PdfPCell(new Phrase("Общий вес", font)));
        weightskt = (weights[0] + weightk[0] + weightt[0]);
        PdfPCell cell2 = new PdfPCell(new Phrase("" + weightskt, font));
        cell2.setColspan(3);
        tbl.addCell(cell2);
        cll.addElement(new Paragraph(" "));
        cll.addElement(tbl);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//5///////////////////////////////////////////
        tbl.flushContent();
        cll = new PdfPCell(new Paragraph("Cell 3"));
        tbl = createtable(dfropen.matrix[4], dfrdrug.matrix[4], dfrsteal.matrix[4], dfrclose.matrix[4],
                getmass(strlist.DataSubSale, 4), strlist.DataSale.get(4), strlist.SessionTypes, null);
        tbl.setWidthPercentage(99);
        cll.addElement(new Paragraph(" "));
        cll.addElement(tbl);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//6///////////////////////////////////////////
        cll = new PdfPCell(new Phrase("Cell 6"));
        cll.addElement(new Paragraph("ВЕС ВСЕГО ПРОДАННОГО МОРОЖЕННОГО" + slashn + "ИЗ УЧЁТА оставшегося мороженного -", font));
        weightsell = weightall[0] + weightall[1] - weightall[2] - weightall[3];
        tbl = new PdfPTable(1);
        tbl.addCell(new Paragraph(" " + weightsell));
        cll.addElement(tbl);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//8///////////////////////////////////////////
        cll = new PdfPCell(new Phrase("Cell 7"));
        cll.addElement(new Paragraph("ВЕС ВСЕГО ПРОДАННОГО МОРОЖЕННОГО" + slashn + "ИЗ УЧЁТА СТАКАНОВ, КЕПОК И ТЕРМОСОВ - ", font));
        tbl.flushContent();
        tbl = new PdfPTable(1);
        tbl.addCell(new Paragraph(" " + weightskt));
        cll.addElement(tbl);
        tbl = new PdfPTable(2);
        //String mulcttitle="ШТРАФ";
        if (weightskt - weightsell > 200)
        {
            //mulct = (weightskt - weightsell);
            //mulcttitle+=" - недосып";
                mulct = 0;
                mulcttitle+=" - вес";
        }
        else
        {
            if (weightsell -  weightskt > 200)
            {
                mulct = (weightskt - weightsell) * 1.66;
                mulcttitle+=" - пересып";
            }
            else
            {
                mulct = 0;
                mulcttitle+=" - вес";
            }
        }
        tbl.addCell(new Paragraph(mulcttitle, font));
        tbl.addCell(new Paragraph(" " + mulct));
        if(dfropen.GetDate().getHours()<hoursbegin)
        {
            mulctdelay=0;
        }
        else
        {
            mulctdelay=(dfropen.GetDate().getHours()-hoursbegin)*60*15;
            if(mulctdelay<0)
                mulctdelay=0;
            mulctdelay+=dfropen.GetDate().getMinutes()*15;
        }
        tbl.addCell(new Paragraph("ШТРАФ - опоздание", font));
        tbl.addCell(new Paragraph(" " + mulctdelay));
        cll.addElement(tbl);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//9///////////////////////////////////////////
        cll = new PdfPCell(new Phrase("Cell 7"));
        cll.addElement(new Paragraph(slashn+"Заработанные деньги", font));
        tbl.flushContent();
        tbl = new PdfPTable(2);
        Calendar calendaropen = Calendar.getInstance();
        calendaropen.setTime(dfropen.GetDate());
        Calendar calendarclose = Calendar.getInstance();
        calendarclose.setTime(dfrclose.GetDate());
        long diff = calendarclose.getTimeInMillis() - calendaropen.getTimeInMillis();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        salary=minutes*110/60;
        tbl.addCell(new Paragraph("За время работы", font));
        tbl.addCell(new Paragraph(" " + salary));
        salaryprcnt=(cashk[0]/100)*5;
        tbl.addCell(new Paragraph("Плюс за проценты", font));
        tbl.addCell(new Paragraph(" " + salaryprcnt));
        tbl.addCell(new Paragraph("Всего", font));
        salaryall=salaryprcnt + salary;
        tbl.addCell(new Paragraph(" " + (salaryall)));
        tbl.addCell(new Paragraph("Штраф", font));
        mulctall=mulctdelay + mulct;
        tbl.addCell(new Paragraph(" " + (mulctall)));
        tbl.addCell(new Paragraph("Итого", font));
        tbl.addCell(new Paragraph(" " + (salaryall - mulctall)));
        cll.addElement(tbl);
        cll.setBorderColor(CW);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//10//////////////////////////////////////////
        */
        //
        document.add(nestedTable);
        document.close();
    }

    private static void createpdf() throws DocumentException, FileNotFoundException
    {
        document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(pdfrath + pdfname + ".pdf"));
        document.open();
    }

    private static void setfont() throws DocumentException, IOException
    {
        //BaseFont bf = BaseFont.createFont(pdfrath + "M-R.otf", BaseFont.IDENTITY_H, true);
        //BaseFont bf = BaseFont.createFont("../M-R.otf", BaseFont.IDENTITY_H, true);
        BaseFont bf = BaseFont.createFont(ServeOneJabber.fonts, BaseFont.IDENTITY_H, true);
        font = new Font(bf, 8, Font.NORMAL);
        fonttitle = new Font(bf,16, Font.BOLDITALIC);
    }

    private static PdfPCell addcl(String s) throws DocumentException
    {
        return new PdfPCell(new Phrase(s, font));
    }

    private static PdfPTable createsimple2table(List<String> types, double[] values) throws DocumentException
    {
        PdfPTable table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        for (int i = 0; i < types.size(); i++)
        {
            table.addCell(new PdfPCell(new Phrase(types.get(i), font)));
            table.addCell(new PdfPCell(new Phrase("" + values[i], font)));
        }
        return table;
    }

    private static PdfPTable createsimple3table(String[] titles, List<String> types, double[] val1, double[] val2) throws DocumentException
    {
        PdfPTable table = new PdfPTable(3);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        for (int i = 0; i < 3; i++)
        {
            table.addCell(new PdfPCell(new Phrase(titles[i], font)));
        }
        for (int i = 0; i < types.size(); i++)
        {
            table.addCell(new PdfPCell(new Phrase(types.get(i), font)));
            table.addCell(new PdfPCell(new Phrase("" + val1[i], font)));
            table.addCell(new PdfPCell(new Phrase("" + val2[i], font)));
        }
        return table;
    }

    private static PdfPTable createtable(double[] open, double[] drug, double[] steal, double[] close,
            String[] typeproduct, String title, List<String> typesession, String[] subtitle) throws DocumentException
    {
        int num = typesession.size();
        PdfPTable table = new PdfPTable(num);
        PdfPCell cell = new PdfPCell(new Phrase(title, font));
        cell.setColspan(num);
        table.addCell(cell);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        for (int i = 0; i < num; i++)
        {
            cell = new PdfPCell(new Phrase(typesession.get(i), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderWidth(2);
            table.addCell(cell);
        }
        for (int j = 0; j < typeproduct.length; j++)
        {
            table.addCell(new PdfPCell(new Phrase(typeproduct[j], font)));
            table.addCell(new PdfPCell(new Phrase("" + open[j], font)));
            table.addCell(new PdfPCell(new Phrase("" + drug[j], font)));
            table.addCell(new PdfPCell(new Phrase("" + steal[j], font)));
            table.addCell(new PdfPCell(new Phrase("" + close[j], font)));
        }
        if (subtitle != null)
        {
            for (int i = 0; i < subtitle.length; i++)
            {
                table.addCell(addcl(subtitle[i]));
            }
        }
        return table;
    }

    private static PdfPTable createtable(double[] s, double[] k, double[] t,
            String[] typeproduct, String title, String[] subtitle) throws DocumentException
    {
        int num = 4;
        PdfPTable table = new PdfPTable(num);
        PdfPCell cell = new PdfPCell(new Phrase(title, font));
        cell.setColspan(num);
        table.addCell(cell);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        for (int j = 0; j < typeproduct.length; j++)
        {
            table.addCell(new PdfPCell(new Phrase(typeproduct[j], font)));
            table.addCell(new PdfPCell(new Phrase("" + s[j], font)));
            table.addCell(new PdfPCell(new Phrase("" + k[j], font)));
            table.addCell(new PdfPCell(new Phrase("" + t[j], font)));
        }
        for (int i = 0; i < subtitle.length; i++)
        {
            table.addCell(addcl(subtitle[i]));
        }
        return table;
    }

    static double[] getmass(double[] open, double[] drug, double[] steal, double[] close, int i)
    {
        double[] c = new double[4];
        c[0] = open[i];
        c[1] = drug[i];
        c[2] = steal[i];
        c[3] = close[i];
        return c;
    }

    static double getresultmass(double[] open, double[] drug, double[] steal, double[] close, int i)
    {
        return open[i] + drug[i] - steal[i] - close[i];
    }

    static String[] getmass(List<String> strl, int i)
    {
        return strl.toArray(new String[strl.size()])[i].split("\t");
    }

    static String datetostring(Date today)
    {
        return today.getDate() + " " + monthtoString(today.getMonth() + 1) + " " + (1900 + today.getYear()) + " " + weektoString(today.getDay());
    }
    static String dateweektoString(Date today)
    {
        return weektoString(today.getDay());
    }
    static String dateonly(Date today)
    {
        return today.getDate() + " " + monthtoString(today.getMonth() + 1) + " " + (1900 + today.getYear());
    }

    static private String weektoString(int w)
    {
        switch (w)
        {
            case 1:
                return "Понедельник";
            case 2:
                return "Вторник";
            case 3:
                return "Среда";
            case 4:
                return "Четверг";
            case 5:
                return "Пятница";
            case 6:
                return "Суббота";
            case 0:
                return "Воскресенье";
        }
        return "fail";
    }

    static private String monthtoString(int w)
    {
        switch (w)
        {
            case 1:
                return "Января";
            case 2:
                return "Февраля";
            case 3:
                return "Марта";
            case 4:
                return "Апреля";
            case 5:
                return "Мая";
            case 6:
                return "Июня";
            case 7:
                return "Июля";
            case 8:
                return "Августа";
            case 9:
                return "Сентября";
            case 10:
                return "Октября";
            case 11:
                return "Ноября";
            case 12:
                return "Декабря";
        }
        return "fail";
    }
}